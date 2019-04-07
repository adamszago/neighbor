package br.com.lphantus.neighbor.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.comparator.OrdenaVisitasPorDataDecrescente;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IVisitaService;
import br.com.lphantus.neighbor.service.IVisitanteService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "visitanteController")
@Transactional(propagation = Propagation.SUPPORTS)
public class VisitanteController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// TODO: verificar o problema com os parametros @Value
	@Value("${neighbor.caminho.fotos}")
	private String diretorioFotos;

	@Value("${neighbor.caminho.imagemsemfoto}")
	private String imagemSemFoto;

	// Services
	@Autowired
	private IVisitaService visitaService;

	@Autowired
	private IVisitanteService visitanteService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	// Diretorio
	public static final String PREFIXO_FOTO = "visitante_";
	public static final String EXTENSAO_ARQUIVO = ".png";

	// Imagem
	private StreamedContent fotoCapturada;
	private byte[] bytesFotoCapturada;
	private StreamedContent fotoTelaCadastro;
	private StreamedContent fotoCadastrada;

	// Visitante
	private VisitanteDTO visitante;
	private List<VisitanteDTO> visitantes;
	private boolean entradaLivre = false;
	private VisitaDTO visita;
	private VisitanteDTO visitanteCadastrado;
	private List<VisitaDTO> visitasAgendadas = new ArrayList<VisitaDTO>();
	private VisitaDTO visitaAgendada;

	// Morador
	private MoradorDTO morador;
	private boolean possuiCPF = true;
	private boolean possuiRG;
	private String casa;

	// Controle
	private Date dataInicial;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor de classe
	 */
	public VisitanteController() {

	}

	@PostConstruct
	public void initialize() {
		visitanteCadastrado = new VisitanteDTO();
		visitanteCadastrado.setPessoa(new PessoaFisicaDTO());
		fotoCadastrada = null;
		this.atualizarTela(true);
	}

	public void atualizarTela(boolean primeiraVez) {
		visitante = new VisitanteDTO();
		visitantes = new ArrayList<VisitanteDTO>();
		morador = new MoradorDTO();
		visita = new VisitaDTO();
		casa = BigDecimal.ZERO.toString();
		entradaLivre = false;
		bytesFotoCapturada = null;
		fotoCadastrada = null;
		visitante = new VisitanteDTO();
		dataInicial = new Date();
		visitaAgendada = new VisitaDTO();
		visitasAgendadas = new ArrayList<VisitaDTO>();
		
		if (!primeiraVez) {
			gerarListaVisitante();
		}
	}

	@Secured({ "ROLE_CADASTRO_VISITANTE", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravar() {
		historico = new HistoricoDTO();
		final String result = null;
		try {
			if (this.bytesFotoCapturada == null) {
				// if (false) {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("FOTO_NOK"));
				return null;
			} else {
				if (condominioUsuarioLogado() != null) {
					visitante.setCondominio(condominioUsuarioLogado());
				}

				Visitante entidade = VisitanteDTO.Builder.getInstance().createEntity(visitante);
				entidade.setDataCadastro(new Date());
				visitanteService.save(entidade);
				visitante.getPessoa().setIdPessoa(entidade.getIdPessoa());
				if (bytesFotoCapturada != null) {
					salvarFoto();
				}

				registrarHistorico(String.format("Gravou visitante. Nome: %s", visitante.getPessoa().getNome()));

				visitanteCadastrado = entidade.createDto();
				atualizarTela(false);
				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

				return "/pages/listagem/listavisitante.jsf";

			}
		} catch (final Exception e) {

			getLogger().error("Erro ao gravar.", e);

			final boolean bytesFotoCapturaValidos = null != bytesFotoCapturada;
			if (bytesFotoCapturaValidos) {
				atualizarFotoCapturada();
				atualizarFotoTelaCadastro();
			}

			JsfUtil.addErrorMessage(e.getMessage());
		}

		return result;
	}

	public void buscarProprietario() {
		try {

			final MoradorDTO moradorTemp = moradorService.buscarPrincipal(casa, condominioUsuarioLogado());

			final UnidadeHabitacionalDTO unidade = unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(moradorTemp);

			if ((moradorTemp != null) && (unidade.getCondominio() != null)) {
				if (unidade.getCondominio().getPessoa().getIdPessoa().equals(condominioUsuarioLogado().getPessoa().getIdPessoa())) {
					this.setMorador(moradorTemp);
				} else {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
				}
			} else {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("CASA_INVALIDA"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar proprietario.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void limpar() throws ServiceException {
		atualizarTela(false);
	}

	@Secured({ "ROLE_REGISTRAR_VISITA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarVisita() {
		if ((casa == null) || casa.isEmpty() || casa.trim().toString().equalsIgnoreCase(BigDecimal.ZERO.toString())) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
		} else {
			try {
				morador = moradorService.buscarDetalhesMorador(moradorService.buscarPrincipal(casa, condominioUsuarioLogado()));
				visita.setMorador(morador);
				visita.setVisitante(visitante);
				visita.setInicioVisita(new Date());
				if ( null == visita.getFimVisita() ){
					visita.setFimVisita(new Date());
				}
				visita.setAtiva(true);
				visita.setConfirmado(true);
				if (visitante.getVisitas() == null) {
					visitante.setVisitas(new HashSet<VisitaDTO>());
				}
				visitante.getVisitas().add(visita);

				visitanteService.update(VisitanteDTO.Builder.getInstance().createEntity(visitante));

				final UnidadeHabitacionalDTO unidade = unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(morador);

				registrarHistorico(String.format("Registrou Visita. Visitante: %s | Morador: %s | Casa: %s", visitante.getPessoa().getNome(), morador.getPessoa().getNome(),
						unidade.getIdentificacao()));

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("VISITA_OK"));

				atualizarTela(false);

			} catch (final ServiceException e) {
				getLogger().error("Erro ao registrar visita.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			}
		}
	}

	@Secured({ "ROLE_REGISTRAR_VISITA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarVisitaVisitanteCadastrado() {
		visitante = visitanteCadastrado;
		visitanteCadastrado = new VisitanteDTO();
		registrarVisita();
		listarVisitantes();
	}

	public void cancelarVisitaVisitanteCadastrado() {
		visitanteCadastrado = new VisitanteDTO();
	}

	@Secured({ "ROLE_EDITAR_VISITANTE", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		String result = null;
		try {
			visitanteService.update(VisitanteDTO.Builder.getInstance().createEntity(visitante));

			registrarHistorico(String.format("Atualizou dados do Visitante: %d - %s", visitante.getPessoa().getIdPessoa(), visitante.getPessoa().getNome()));

			atualizarTela(false);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			result = "visitanteCadastrado";
		} catch (final Exception e) {

			getLogger().error("Erro ao alterar visitante.", e);

			atualizarFotoCapturada();
			atualizarFotoTelaCadastro();
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
		return result;
	}

	@Secured({ "ROLE_ADICIONAR_ENTRADA_LIVRE_VISITANTE", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void adicionarEntradaLivre() {
		MoradorDTO objetoMorador = morador;
		entradaLivre = true;
		if ((casa == null) || casa.isEmpty() || casa.trim().equalsIgnoreCase(BigDecimal.ZERO.toString())) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
		} else {
			if (objetoMorador.getPessoa().getIdPessoa() == null) {
				try {
					objetoMorador = moradorService.buscarPrincipal(casa, condominioUsuarioLogado());
				} catch (final ServiceException e) {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
				}
			}
			entradaLivre();
			try {
				visitanteService.update(VisitanteDTO.Builder.getInstance().createEntity(visitante));
				final UnidadeHabitacionalDTO unidade = unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(objetoMorador);

				registrarHistorico(String.format("Adicionou entrada livre - Visitante: %d - %s | Morador: %s | Casa %s ", visitante.getPessoa().getIdPessoa(), visitante
						.getPessoa().getNome(), objetoMorador.getPessoa().getNome(), unidade.getIdentificacao()));
				
				atualizarTela(false);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao adicionar entrada livre.", e);
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
			}
		}
	}

	public void entradaLivre() {
		if (entradaLivre) {
			if (visitante.getEntradasLivres() == null) {
				visitante.setEntradasLivres(new HashSet<MoradorDTO>());
			}
			visitante.getEntradasLivres().add(morador);
			morador = new MoradorDTO();
		}
	}

	@Secured({ "ROLE_REMOVER_ENTRADA_LIVRE_VISITANTE", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void removerEntradaLivre() {
		try {
			final Iterator<MoradorDTO> itMoradorEntradaLivre = visitante.getEntradasLivres().iterator();
			while (itMoradorEntradaLivre.hasNext()) {
				final MoradorDTO m = itMoradorEntradaLivre.next();
				if (m.getPessoa().getIdPessoa().equals(morador.getPessoa().getIdPessoa())) {
					itMoradorEntradaLivre.remove();
				}
			}
			visitanteService.update(VisitanteDTO.Builder.getInstance().createEntity(visitante));

			final UnidadeHabitacionalDTO unidade = unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(morador);

			String historico = String.format("Removeu Entrada Livre - Visitante: %d - %s | Morador - ID: %d - Casa: %s - Nome: %s", 
					visitante.getPessoa().getIdPessoa(), visitante.getPessoa().getNome(), morador.getPessoa().getIdPessoa(),
					unidade.getIdentificacao(), morador.getPessoa().getNome());
			registrarHistorico(historico);

			atualizarTela(false);
			
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao remover entrada livre.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}

	public void gerarListaVisitante() {
		visitante = new VisitanteDTO();
		morador = new MoradorDTO();
		visita = new VisitaDTO();
		visitantes = new ArrayList<VisitanteDTO>();
	}
	
	private void preencheAbaVisitantes(){
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			CondominioDTO condominio = condominioUsuarioLogado();
			if (userLogado != null) {
				if (userLogado.isRoot()) {
					condominio = null;
				}
			}
			
			visitantes.clear();
			visitantes.addAll(visitanteService.buscarPorStatus(condominio, null));
			
			// ordena lista de visitas pela data da visita, de forma decrescente
			for(VisitanteDTO item:visitantes){
				List<VisitaDTO> visitas = new ArrayList<VisitaDTO>(item.getVisitas());
				Collections.sort(visitas, new OrdenaVisitasPorDataDecrescente());
				item.setVisitasList(visitas);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de visitantes.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}
	
	private void preencheAbaVisitas(){
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			CondominioDTO condominio = condominioUsuarioLogado();
			if (userLogado != null) {
				if (userLogado.isRoot()) {
					condominio = null;
				}
			}
			
			visitasAgendadas.clear();
			visitasAgendadas = visitaService.buscaVisitasAgendadas(condominio);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de visitas agendadas.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}
	
	public void onTabChange(TabChangeEvent event){
		if ( event.getTab().getId().equals("tbVisitasConfirmadas") ){
			preencheAbaVisitantes();
		}else if ( event.getTab().getId().equals("tbVisitasAgendadas") ){
			preencheAbaVisitas();
		}
	}

	public void onCapture(final CaptureEvent captureEvent) {
		final byte[] bytesImagem = captureEvent.getData();

		final ByteArrayInputStream bais = new ByteArrayInputStream(bytesImagem);
		fotoCapturada = new DefaultStreamedContent(bais, "image/png");
		bytesFotoCapturada = bytesImagem;
	}
	
	public void handleFileUpload(FileUploadEvent event) {
		try {
			final byte[] bytesImagem = IOUtils.toByteArray(event.getFile().getInputstream());
			this.fotoCapturada = new DefaultStreamedContent(new ByteArrayInputStream(bytesImagem), "image/png");
			this.bytesFotoCapturada = bytesImagem;
		} catch (IOException e) {
			getLogger().error("Ocorreu um erro ao enviar foto.", e);
		}
    }

	public void atualizarFotoTelaCadastro() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytesFotoCapturada);
		fotoTelaCadastro = new DefaultStreamedContent(bais, "image/png");
	}

	public void atualizarFotoCapturada() {
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytesFotoCapturada);
		fotoCapturada = new DefaultStreamedContent(bais, "image/png");
	}

	public void salvarFoto() {
		try {
			if ((visitante != null) && (visitante.getPessoa().getIdPessoa() != null) && (bytesFotoCapturada != null)) {
				final FileImageOutputStream imageOutput = new FileImageOutputStream(
						new File(diretorioFotos + PREFIXO_FOTO + visitante.getPessoa().getIdPessoa() + EXTENSAO_ARQUIVO));
				imageOutput.write(bytesFotoCapturada, 0, bytesFotoCapturada.length);
				imageOutput.close();
			}
		} catch (final Exception e) {
			getLogger().error("Erro ao salvar foto.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_EDITAR_VISITANTE", "ROLE_ROOT" })
	public String editarVisitante() {
		if (visitante.getPessoa().getIdPessoa() != null) {
			final File foto = new File(diretorioFotos + PREFIXO_FOTO + visitante.getPessoa().getIdPessoa() + EXTENSAO_ARQUIVO);
			if (foto.exists()) {
				FileInputStream fis;
				try {
					fis = new FileInputStream(foto);
					fotoCadastrada = new DefaultStreamedContent(fis, "image/png");
				} catch (final FileNotFoundException e) {
					JsfUtil.addErrorMessage(e.getMessage());
				}
			}
		}

		return "/pages/cadastros/cadvisitante.jsf";
	}

	@Secured({ "ROLE_CADASTRO_VISITANTE", "ROLE_ROOT" })
	public String novoVisitante() throws ServiceException {
		atualizarTela(false);
		return "/pages/cadastros/cadvisitante.jsf";
	}

	@Secured({ "ROLE_LISTA_VISITANTE", "ROLE_ROOT" })
	public String listarVisitantes() {
		
		gerarListaVisitante();
		preencheAbaVisitantes();
		
		return "/pages/listagem/listavisitante.jsf";
	}

	// CONFIRMACAO VISITAS AGENDADAS
	// @Secured({ "ROLE_LISTA_VISITANTE", "ROLE_CADASTRO_VISITANTE", "ROLE_ROOT"
	// })
	public String validarVisitaAgendada() {
		visitante = visitaAgendada.getVisitante();
		visitante.setVisitaAgendada(visitaAgendada);
		return "/pages/cadastros/cadvisitante.jsf";
	}

	// @Secured({ "ROLE_LISTA_VISITANTE", "ROLE_CADASTRO_VISITANTE", "ROLE_ROOT"
	// })
	@Transactional(propagation = Propagation.REQUIRED)
	public String confirmacaoVisita() {
		final String caminho = "/pages/listagem/listavisitante.jsf";
		try {
			if (visitante.getVisitaAgendada() == null) {
				JsfUtil.addErrorMessage("Visitante não possui visita agendada!");
			} else {
				final VisitaDTO visitaAgendadaConfirmada = visitante.getVisitaAgendada();
				visitaAgendadaConfirmada.setConfirmado(true);
				// TODO trocar para inicio da visita.
				// visitaAgendadaConfirmada.setDataVisita(new Date());
				visitaService.confirmarVisitaAgendada(visitaAgendadaConfirmada);
				visitante.setVisitaAgendada(null);
				this.atualizarTela(false);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao confirmar visitacao.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return caminho;
	}

	// GETTER AND SETTER

	/**
	 * @return the possuiCPF
	 */
	public boolean isPossuiCPF() {
		return possuiCPF;
	}

	/**
	 * @param possuiCPF
	 *            the possuiCPF to set
	 */
	public void setPossuiCPF(boolean possuiCPF) {
		this.possuiCPF = possuiCPF;
	}

	/**
	 * @return the possuiRG
	 */
	public boolean isPossuiRG() {
		return possuiRG;
	}

	/**
	 * @param possuiRG
	 *            the possuiRG to set
	 */
	public void setPossuiRG(boolean possuiRG) {
		this.possuiRG = possuiRG;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the casa
	 */
	public String getCasa() {
		return casa;
	}

	/**
	 * @param casa
	 *            the casa to set
	 */
	public void setCasa(String casa) {
		this.casa = casa;
	}

	/**
	 * @return the visitante
	 */
	public VisitanteDTO getVisitante() {
		return visitante;
	}

	/**
	 * @return the visitantes
	 */
	public List<VisitanteDTO> getVisitantes() {
		return visitantes;
	}

	/**
	 * @param visitantes
	 *            the visitantes to set
	 */
	public void setVisitantes(List<VisitanteDTO> visitantes) {
		this.visitantes = visitantes;
	}

	/**
	 * @return the visita
	 */
	public VisitaDTO getVisita() {
		return visita;
	}

	/**
	 * @param visita
	 *            the visita to set
	 */
	public void setVisita(VisitaDTO visita) {
		this.visita = visita;
	}

	/**
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return dataInicial;
	}

	/**
	 * @param dataInicial
	 *            the dataInicial to set
	 */
	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(VisitanteDTO visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the entradaLivre
	 */
	public boolean isEntradaLivre() {
		return entradaLivre;
	}

	/**
	 * @param entradaLivre
	 *            the entradaLivre to set
	 */
	public void setEntradaLivre(boolean entradaLivre) {
		this.entradaLivre = entradaLivre;
	}

	/**
	 * @return the visitanteCadastrado
	 */
	public VisitanteDTO getVisitanteCadastrado() {
		return visitanteCadastrado;
	}

	/**
	 * @param visitanteCadastrado
	 *            the visitanteCadastrado to set
	 */
	public void setVisitanteCadastrado(VisitanteDTO visitanteCadastrado) {
		this.visitanteCadastrado = visitanteCadastrado;
	}

	/**
	 * @return the bytesFotoCapturada
	 */
	public byte[] getBytesFotoCapturada() {
		return bytesFotoCapturada;
	}

	/**
	 * @param bytesFotoCapturada
	 *            the bytesFotoCapturada to set
	 */
	public void setBytesFotoCapturada(byte[] bytesFotoCapturada) {
		this.bytesFotoCapturada = bytesFotoCapturada;
	}

	/**
	 * @return the historico
	 */
	public HistoricoDTO getHistorico() {
		return historico;
	}

	/**
	 * @param historico
	 *            the historico to set
	 */
	public void setHistorico(HistoricoDTO historico) {
		this.historico = historico;
	}

	/**
	 * @return the fotoCapturada
	 */
	public StreamedContent getFotoCapturada() {
		return fotoCapturada;
	}

	/**
	 * @param fotoCapturada
	 *            the fotoCapturada to set
	 */
	public void setFotoCapturada(StreamedContent fotoCapturada) {
		this.fotoCapturada = fotoCapturada;
	}

	/**
	 * @return the fotoTelaCadastro
	 */
	public StreamedContent getFotoTelaCadastro() {
		return fotoTelaCadastro;
	}

	/**
	 * @param fotoTelaCadastro
	 *            the fotoTelaCadastro to set
	 */
	public void setFotoTelaCadastro(StreamedContent fotoTelaCadastro) {
		this.fotoTelaCadastro = fotoTelaCadastro;
	}

	/**
	 * @return the fotoCadastrada
	 */
	public StreamedContent getFotoCadastrada() {
		return fotoCadastrada;
	}

	/**
	 * @param fotoCadastrada
	 *            the fotoCadastrada to set
	 */
	public void setFotoCadastrada(StreamedContent fotoCadastrada) {
		this.fotoCadastrada = fotoCadastrada;
	}

	/**
	 * @return the visitasAgendadas
	 */
	public List<VisitaDTO> getVisitasAgendadas() {
		return visitasAgendadas;
	}

	/**
	 * @param visitasAgendadas
	 *            the visitasAgendadas to set
	 */
	public void setVisitasAgendadas(List<VisitaDTO> visitasAgendadas) {
		this.visitasAgendadas = visitasAgendadas;
	}

	/**
	 * @return the visitaAgendada
	 */
	public VisitaDTO getVisitaAgendada() {
		return visitaAgendada;
	}

	/**
	 * @param visitaAgendada
	 *            the visitaAgendada to set
	 */
	public void setVisitaAgendada(VisitaDTO visitaAgendada) {
		this.visitaAgendada = visitaAgendada;
	}

}
