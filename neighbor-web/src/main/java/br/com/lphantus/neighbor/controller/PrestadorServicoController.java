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
import javax.faces.event.AjaxBehaviorEvent;
import javax.imageio.stream.FileImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PessoaJuridicaDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.common.TipoPrestadorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.comparator.OrdenaServicosPorDataDecrescente;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.entity.TipoPrestador;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IPrestadorServicoService;
import br.com.lphantus.neighbor.service.IServicoPrestadoService;
import br.com.lphantus.neighbor.service.ITipoPrestadorService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorLabel;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "prestadorServicoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class PrestadorServicoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IPrestadorServicoService prestadorService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private ITipoPrestadorService tipoPrestadorService;

	@Autowired
	private IServicoPrestadoService servicoPrestadoService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	// Diretorio
	// TODO: revisar os parametros @Value
	@Value("{neighbor.caminho.fotos}")
	private String diretorioFotos;

	// Imagem
	@Value("{neighbor.caminho.imagemsemfoto}")
	private String imagemSemFoto;

	@Value("${neighbor.param.isdemo}")
	private String sistemaIsDemo;

	public static final String PREFIXO_FOTO = "prestador_";
	public static final String EXTENSAO_ARQUIVO = ".png";

	private StreamedContent fotoCapturada;
	private byte[] bytesFotoCapturada;
	private StreamedContent fotoTelaCadastro;
	private StreamedContent fotoCadastrada;

	// Prestador Servico
	private PrestadorServicoDTO prestadorServico;
	private List<PrestadorServicoDTO> prestadoresServico;
	private PrestadorServicoDTO prestadorServicoCadastrado;
	private ServicoPrestadoDTO servicoPrestado;
	private boolean possuiCPF = true;
	private boolean possuiRG;
	private boolean entradaLivre = false;
	private Long idPrestador;
	private TipoPrestadorDTO tipoPrestador;
	private List<TipoPrestadorDTO> tiposPrestadores;
	private List<ServicoPrestadoDTO> servicosAgendados = new ArrayList<ServicoPrestadoDTO>();
	private ServicoPrestadoDTO servicoAgendado;
	private String tipoPessoa = "";

	// Morador
	private MoradorDTO morador;
	private String casa;

	// Controle
	private Date dataMinimaServico;

	// historico

	/**
	 * Construtor padrao
	 */
	public PrestadorServicoController() {

	}

	@PostConstruct
	public void initialize() {
		this.prestadorServicoCadastrado = new PrestadorServicoDTO();
	}

	public void atualizarTela(boolean primeiraVez) {
		this.fotoCapturada = null;
		this.bytesFotoCapturada = null;
		this.fotoTelaCadastro = null;
		this.fotoCadastrada = null;
		this.idPrestador = null;
		this.prestadorServico = new PrestadorServicoDTO();
		this.prestadoresServico = new ArrayList<PrestadorServicoDTO>();
		this.tipoPessoa = "";
		this.prestadorServico.setPessoa(new PessoaDTO());
		this.tipoPrestador = new TipoPrestadorDTO();
		this.morador = new MoradorDTO();
		this.servicoPrestado = new ServicoPrestadoDTO();
		this.casa = BigDecimal.ZERO.toString();
		this.dataMinimaServico = new Date();
		this.entradaLivre = false;
		this.bytesFotoCapturada = null;
		this.servicoAgendado = new ServicoPrestadoDTO();
		
		if ( !primeiraVez ){
			gerarListaPrestador();
		}
	}

	public String validarServicoAgendado() {
		try {
			this.prestadorServico = prestadorService.buscarPorId(this.servicoAgendado.getPrestadorServico());
		} catch (ServiceException e) {
			this.prestadorServico = servicoAgendado.getPrestadorServico();
		}
		if (this.prestadorServico.getPessoa() instanceof PessoaFisicaDTO) {
			this.tipoPessoa = "F";
		} else if (this.prestadorServico.getPessoa() instanceof PessoaJuridicaDTO) {
			this.tipoPessoa = "J";
		}
		this.prestadorServico.setServicoAgendado(this.servicoAgendado);
		return "/pages/cadastros/cadprestador.jsf";
	}

	public void trocaTipoPessoa(final AjaxBehaviorEvent event) {
		if ( null == prestadorServico ){
			atualizarTela(false);
		}else{
			if (this.tipoPessoa.equalsIgnoreCase("J")) {
				this.prestadorServico.setPessoa(new PessoaJuridicaDTO());
			} else if (this.tipoPessoa.equalsIgnoreCase("F")) {
				this.prestadorServico.setPessoa(new PessoaFisicaDTO());
			} else {
				this.prestadorServico.setPessoa(new PessoaDTO());
			}
		}
	}

	@Secured({ "ROLE_CADASTRO_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravar() {

		final String result = "";
		try {

			if (StringUtils.isBlank(this.tipoPessoa)) {
				throw new ServiceException(GerenciadorLabel.getMensagem("msg.sel.tipo.documento"));
			}

			final boolean isDemo = Boolean.valueOf(sistemaIsDemo);
			final boolean fotoNaoNula = this.bytesFotoCapturada != null;
			// linha abaixo somente para testes sem webcam
			// final boolean fotoNaoNula = true;

			// bug 73
			if (isDemo || fotoNaoNula) {

				this.prestadorServico.setCondominio(condominioUsuarioLogado());

				if (this.prestadorServico.getTipoPrestador() == null) {
					throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_TIPO_PRESTADOR"));
				}

				final PrestadorServico entidade = PrestadorServicoDTO.Builder.getInstance().createEntity(this.prestadorServico);
				entidade.getPessoa().setDataCadastro(new Date());

				this.prestadorService.save(entidade);

				this.prestadorServico = entidade.createDto();
				if (this.bytesFotoCapturada != null) {
					salvarFoto();
				}

				registrarHistorico(String.format("Gravou prestador de servico. Nome: %s", entidade.getPessoa().getNome()));

				this.prestadorServicoCadastrado = entidade.createDto();

				atualizarTela(false);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

				return "/pages/listagem/listaprestador.jsf";
			} else {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("FOTO_NOK"));
				return null;
			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar prestador.", e);
			atualizarFotoCapturada();
			atualizarFotoTelaCadastro();
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar prestador.", e);
		}
		return result;
	}

	@Secured({ "ROLE_EDITAR_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterar() {
		try {
			boolean fotoNula = this.bytesFotoCapturada == null;
			// linha somente de testes
			// boolean fotoNula = false;
			if (fotoNula) {
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("FOTO_NOK"));
			} else {
				this.prestadorService.update(PrestadorServicoDTO.Builder.getInstance().createEntity(this.prestadorServico));

				registrarHistorico("Atualizou dados do Prestador de Servico: " + this.prestadorServico.getPessoa().getIdPessoa() + " - "
						+ this.prestadorServico.getPessoa().getNome());

				atualizarTela(false);
				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			}
		} catch (final Exception e) {
			getLogger().error("Erro ao alterar prestador.", e);
			atualizarFotoCapturada();
			atualizarFotoTelaCadastro();
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}

	@Secured({ "ROLE_EXCLUIR_TIPO_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarTipo() {
		try {
			if (this.tipoPrestador.isAtivo()) {
				this.tipoPrestador.setAtivo(false);
			} else {
				this.tipoPrestador.setAtivo(true);
			}

			this.tipoPrestadorService.update(TipoPrestadorDTO.Builder.getInstance().createEntity(this.tipoPrestador));

			registrarHistorico("Alterou status Tipo Animal: " + this.tipoPrestador.getId() + " - " + this.tipoPrestador.getTipoPrestador() + "para " + this.tipoPrestador.isAtivo());

			this.tipoPrestador = new TipoPrestadorDTO();
			gerarListaTiposPrestadores();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			this.tiposPrestadores = TipoPrestadorDTO.Builder.getInstance().createList(this.tipoPrestadorService.findAll());
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar tipo.", e);
			JsfUtil.addSuccessMessage(e.getMessage());
		}
		return null;
	}

	public void limpar() {
		this.prestadorServico = new PrestadorServicoDTO();
	}

	@Secured({ "ROLE_REGISTRAR_PRESTACAO_SERVICO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarServicoPrestado() {
		if ((this.casa == null) || this.casa.isEmpty() || this.casa.trim().equalsIgnoreCase(BigDecimal.ZERO.toString())) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
			// 22/06/2015: removida obrigatoriedade data fim do servico
			//		} else if (this.servicoPrestado.getDataFimServico() == null) {
			//			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("INFORME_DATA_FIM_SERVICO"));
		} else {
			try {
				this.morador = moradorService.buscarDetalhesMorador(this.moradorService.buscarPrincipal(this.casa, condominioUsuarioLogado()));
				this.servicoPrestado.setMorador(this.morador);
				this.servicoPrestado.setPrestadorServico(this.prestadorServico);
				this.servicoPrestado.setDataServico(new Date());
				if ( null == servicoPrestado.getDataFimServico() ){
					this.servicoPrestado.setDataFimServico(new Date());
				}
				if (this.prestadorServico.getServicosPrestados() == null) {
					this.prestadorServico.setServicosPrestados(new HashSet<ServicoPrestadoDTO>());
				}
				this.prestadorServico.getServicosPrestados().add(this.servicoPrestado);

				this.prestadorService.update(PrestadorServicoDTO.Builder.getInstance().createEntity(this.prestadorServico));

				final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(this.morador);

				final String historico = String.format("Registrou Prestacao de Servico. Prestador de Servico: %s | Morador: %s | Casa: %s", this.prestadorServico.getPessoa()
						.getNome(), this.morador.getPessoa().getNome(), unidade.getIdentificacao());
				registrarHistorico(historico);
				
				atualizarTela(false);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("PRESTACAO_SERVICO_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao registrar servico prestado.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			}
		}
	}

	@Secured({ "ROLE_REGISTRAR_PRESTACAO_SERVICO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarServicoPrestadorCadastrado() {
		this.prestadorServico = this.prestadorServicoCadastrado;
		this.prestadorServicoCadastrado = new PrestadorServicoDTO();
		registrarServicoPrestado();
		listarPrestadores();
	}

	public void cancelarServicoPrestadorCadastrado() {
		this.prestadorServicoCadastrado = new PrestadorServicoDTO();
	}

	public void buscarProprietario() {
		try {
			final MoradorDTO moradorTemp = this.moradorService.buscarPrincipal(this.casa, condominioUsuarioLogado());

			final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(moradorTemp);

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

	@Secured({ "ROLE_ADICIONAR_ENTRADA_LIVRE_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void adicionarEntradaLivre() {
		MoradorDTO objetoMorador = morador;
		this.entradaLivre = true;
		if ((this.casa == null) || this.casa.isEmpty() || this.casa.trim().equalsIgnoreCase(BigDecimal.ZERO.toString())) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("SELECIONE_MORADOR"));
		} else {
			if (objetoMorador.getPessoa().getIdPessoa() == null) {
				try {
					objetoMorador = this.moradorService.buscarPrincipal(this.casa, condominioUsuarioLogado());
				} catch (final ServiceException e) {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
				}
			}
			entradaLivre();
			try {
				this.prestadorService.update(PrestadorServicoDTO.Builder.getInstance().createEntity(this.prestadorServico));
				final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(objetoMorador);

				registrarHistorico(String.format("Adicionou Entrada Livre - Prestador de Servico: %d - %s | Morador: %s | Casa: %s", this.prestadorServico.getPessoa()
						.getIdPessoa(), this.prestadorServico.getPessoa().getNome(), objetoMorador.getPessoa().getNome(), unidade.getIdentificacao()));
				
				atualizarTela(false);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao adicionar entrada livre.", e);
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
			}
		}
	}

	public void entradaLivre() {
		if (this.entradaLivre) {
			if (this.prestadorServico.getEntradasLivres() == null) {
				this.prestadorServico.setEntradasLivres(new HashSet<MoradorDTO>());
			}
			this.prestadorServico.getEntradasLivres().add(this.morador);
			this.morador = new MoradorDTO();
		}
	}

	@Secured({ "ROLE_REMOVER_ENTRADA_LIVRE_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void removerEntradaLivre() {
		try {
			final Iterator<MoradorDTO> itMoradorEntradaLivre = this.prestadorServico.getEntradasLivres().iterator();
			while (itMoradorEntradaLivre.hasNext()) {
				final MoradorDTO m = itMoradorEntradaLivre.next();
				if (m.getPessoa().getIdPessoa().equals(this.morador.getPessoa().getIdPessoa())) {
					itMoradorEntradaLivre.remove();
				}
			}
			this.prestadorService.update(PrestadorServicoDTO.Builder.getInstance().createEntity(this.prestadorServico));

			final UnidadeHabitacionalDTO unidade = this.unidadeHabitacionalService.buscarUnidadeHabitacionalMorador(this.morador);

			registrarHistorico("Removeu Entrada Livre -  " + "Prestador de Servico: " + this.prestadorServico.getPessoa().getIdPessoa() + " - "
					+ this.prestadorServico.getPessoa().getNome() + " | Morador - ID: " + this.morador.getPessoa().getIdPessoa() + " - Casa: " + unidade.getIdentificacao()
					+ " - Nome: " + this.morador.getPessoa().getNome());
			
			atualizarTela(false);
			
			this.prestadorServico = prestadorService.buscarPorId(prestadorServico);
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao remver entrada livre.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}

	public void gerarListaPrestador() {

		this.prestadorServico = new PrestadorServicoDTO();
		this.prestadoresServico = new ArrayList<PrestadorServicoDTO>();
		this.morador = new MoradorDTO();
		this.servicoPrestado = new ServicoPrestadoDTO();
		this.casa = BigDecimal.ZERO.toString();
		this.dataMinimaServico = new Date();
		this.entradaLivre = false;
	}
	
	private void preencheAbaPrestadores(){
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {
				CondominioDTO condominio;

				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}

				this.prestadoresServico.addAll(this.prestadorService.buscarPorCondominio(condominio));
				
				// ordena lista de servicos prestados pela data da visita, de forma decrescente
				for(PrestadorServicoDTO item:prestadoresServico){
					List<ServicoPrestadoDTO> visitas = new ArrayList<ServicoPrestadoDTO>(item.getServicosPrestados());
					Collections.sort(visitas, new OrdenaServicosPorDataDecrescente());
					item.setServicosPrestadosList(visitas);
				}
				
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista prestador.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}
	
	private void preencheAbaServicos(){
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {
				CondominioDTO condominio;

				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}

				this.servicosAgendados = this.servicoPrestadoService.buscarServicosAgendados(condominio);

			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista prestador.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
	}
	
	public void onTabChange(TabChangeEvent event){
		if ( event.getTab().getId().equals("abaPrestadores") ){
			preencheAbaPrestadores();
		}else if ( event.getTab().getId().equals("abaServicos") ){
			preencheAbaServicos();
		}
	}

	public void gerarListaTiposPrestadores() {
		this.tiposPrestadores = new ArrayList<TipoPrestadorDTO>();
		try {
			this.tiposPrestadores = this.tipoPrestadorService.findAllTipoPrestador();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista tipos prestadores.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void salvarFoto() {
		try {
			if ((this.prestadorServico != null) && (this.prestadorServico.getPessoa().getIdPessoa() != null) && (this.bytesFotoCapturada != null)) {
				final FileImageOutputStream imageOutput = new FileImageOutputStream(new File(diretorioFotos + PREFIXO_FOTO + this.prestadorServico.getPessoa().getIdPessoa()
						+ EXTENSAO_ARQUIVO));
				imageOutput.write(this.bytesFotoCapturada, 0, this.bytesFotoCapturada.length);
				imageOutput.close();
			}
		} catch (final Exception e) {
			getLogger().error("Erro ao salva foto.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void onCapture(final CaptureEvent captureEvent) {
		final byte[] bytesImagem = captureEvent.getData();

		final ByteArrayInputStream bais = new ByteArrayInputStream(bytesImagem);
		this.fotoCapturada = new DefaultStreamedContent(bais, "image/png");
		this.bytesFotoCapturada = bytesImagem;
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
		// bug 18
		final boolean bytesFotoCapturadaNaoNulo = null != this.bytesFotoCapturada;
		if (bytesFotoCapturadaNaoNulo) {
			final ByteArrayInputStream bais = new ByteArrayInputStream(this.bytesFotoCapturada);
			this.fotoTelaCadastro = new DefaultStreamedContent(bais, "image/png");
		}
	}

	public void atualizarFotoCapturada() {
		// bug 18
		final boolean bytesFotoCapturadaNaoNulo = null != this.bytesFotoCapturada;
		if (bytesFotoCapturadaNaoNulo) {
			final ByteArrayInputStream bais = new ByteArrayInputStream(this.bytesFotoCapturada);
			this.fotoCapturada = new DefaultStreamedContent(bais, "image/png");
		}
	}

	@Secured({ "ROLE_CADASTRO_TIPO_PRESTADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void cadastrarTipoPrestador() {
		try {
			final TipoPrestador entidade = TipoPrestadorDTO.Builder.getInstance().createEntity(this.tipoPrestador);
			this.tipoPrestadorService.save(entidade);
			gerarListaTiposPrestadores();
			// bug 17
			this.tipoPrestador = new TipoPrestadorDTO();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao cadastrar tipo prestador.", e);
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem(e.getMessage()));
		} catch (final Exception e) {
			getLogger().error("Erro ao cadastrar tipo prestador.", e);
		}
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_CADASTRO_TIPO_PRESTADOR", "ROLE_ROOT" })
	public String novoTipoPrestador() {
		this.tipoPrestador = new TipoPrestadorDTO();
		return "/pages/cadastros/manutencaotipoprestador.jsf";
	}

	@Secured({ "ROLE_CADASTRO_PRESTADOR", "ROLE_ROOT" })
	public String novoPrestador() throws ServiceException {
		atualizarTela(true);
		gerarListaTiposPrestadores();
		return "/pages/cadastros/cadprestador.jsf";
	}

	@Secured({ "ROLE_LISTA_PRESTADOR", "ROLE_ROOT" })
	public String listarPrestadores() {

		gerarListaPrestador();
		preencheAbaPrestadores();
		
		return "/pages/listagem/listaprestador.jsf";
	}

	public String editarPrestador() {
		if (this.prestadorServico.getPessoa().getIdPessoa() != null) {
			final File foto = new File(diretorioFotos + PREFIXO_FOTO + this.prestadorServico.getPessoa().getIdPessoa() + EXTENSAO_ARQUIVO);
			if (foto.exists()) {
				FileInputStream fis;
				try {
					fis = new FileInputStream(foto);
					this.fotoCadastrada = new DefaultStreamedContent(fis, "image/png");
				} catch (final FileNotFoundException e) {
					getLogger().error("Erro ao editar prestador.", e);
					JsfUtil.addErrorMessage(e.getMessage());
				}
			}
		}

		try {
			if (this.prestadorServico.getPessoa() instanceof PessoaFisicaDTO) {
				this.tipoPessoa = "F";
			} else if (this.prestadorServico.getPessoa() instanceof PessoaJuridicaDTO) {
				this.tipoPessoa = "J";
			}
			PrestadorServicoDTO tmp = prestadorService.buscarPorId(prestadorServico);
			prestadorServico = tmp;
		} catch (ServiceException ex) {
			// TODO: resolver excecao
			ex.printStackTrace();
		}

		return "/pages/cadastros/cadprestador.jsf";
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
	 * @return the servicosAgendados
	 */
	public List<ServicoPrestadoDTO> getServicosAgendados() {
		return servicosAgendados;
	}

	/**
	 * @param servicosAgendados
	 *            the servicosAgendados to set
	 */
	public void setServicosAgendados(List<ServicoPrestadoDTO> servicosAgendados) {
		this.servicosAgendados = servicosAgendados;
	}

	/**
	 * @return the servicoAgendado
	 */
	public ServicoPrestadoDTO getServicoAgendado() {
		return servicoAgendado;
	}

	/**
	 * @param servicoAgendado
	 *            the servicoAgendado to set
	 */
	public void setServicoAgendado(ServicoPrestadoDTO servicoAgendado) {
		this.servicoAgendado = servicoAgendado;
	}

	/**
	 * @return the prestadorServico
	 */
	public PrestadorServicoDTO getPrestadorServico() {
		return prestadorServico;
	}

	/**
	 * @param prestadorServico
	 *            the prestadorServico to set
	 */
	public void setPrestadorServico(PrestadorServicoDTO prestadorServico) {
		this.prestadorServico = prestadorServico;
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
	 * @return the prestadoresServico
	 */
	public List<PrestadorServicoDTO> getPrestadoresServico() {
		return prestadoresServico;
	}

	/**
	 * @param prestadoresServico
	 *            the prestadoresServico to set
	 */
	public void setPrestadoresServico(List<PrestadorServicoDTO> prestadoresServico) {
		this.prestadoresServico = prestadoresServico;
	}

	/**
	 * @return the servicoPrestado
	 */
	public ServicoPrestadoDTO getServicoPrestado() {
		return servicoPrestado;
	}

	/**
	 * @param servicoPrestado
	 *            the servicoPrestado to set
	 */
	public void setServicoPrestado(ServicoPrestadoDTO servicoPrestado) {
		this.servicoPrestado = servicoPrestado;
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
	 * @return the prestadorServicoCadastrado
	 */
	public PrestadorServicoDTO getPrestadorServicoCadastrado() {
		return prestadorServicoCadastrado;
	}

	/**
	 * @param prestadorServicoCadastrado
	 *            the prestadorServicoCadastrado to set
	 */
	public void setPrestadorServicoCadastrado(PrestadorServicoDTO prestadorServicoCadastrado) {
		this.prestadorServicoCadastrado = prestadorServicoCadastrado;
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
	 * @return the dataMinimaServico
	 */
	public Date getDataMinimaServico() {
		return dataMinimaServico;
	}

	/**
	 * @param dataMinimaServico
	 *            the dataMinimaServico to set
	 */
	public void setDataMinimaServico(Date dataMinimaServico) {
		this.dataMinimaServico = dataMinimaServico;
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
	 * @return the idPrestador
	 */
	public Long getIdPrestador() {
		return idPrestador;
	}

	/**
	 * @param idPrestador
	 *            the idPrestador to set
	 */
	public void setIdPrestador(Long idPrestador) {
		this.idPrestador = idPrestador;
	}

	/**
	 * @return the tipoPrestador
	 */
	public TipoPrestadorDTO getTipoPrestador() {
		return tipoPrestador;
	}

	/**
	 * @param tipoPrestador
	 *            the tipoPrestador to set
	 */
	public void setTipoPrestador(TipoPrestadorDTO tipoPrestador) {
		this.tipoPrestador = tipoPrestador;
	}

	/**
	 * @return the tiposPrestadores
	 */
	public List<TipoPrestadorDTO> getTiposPrestadores() {
		return tiposPrestadores;
	}

	/**
	 * @param tiposPrestadores
	 *            the tiposPrestadores to set
	 */
	public void setTiposPrestadores(List<TipoPrestadorDTO> tiposPrestadores) {
		this.tiposPrestadores = tiposPrestadores;
	}

	/**
	 * @return the tipoPessoa
	 */
	public String getTipoPessoa() {
		return this.tipoPessoa;
	}

	/**
	 * @param tipoPessoa
	 *            the tipoPessoa to set
	 */
	public void setTipoPessoa(final String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

}
