package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.common.TelefoneDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Telefone;
import br.com.lphantus.neighbor.enums.EnumTempoAcesso;
import br.com.lphantus.neighbor.enums.EnumTipoAcesso;
import br.com.lphantus.neighbor.service.IAnimalEstimacaoService;
import br.com.lphantus.neighbor.service.IMensagemService;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IMoradorAgregadoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IOcorrenciaService;
import br.com.lphantus.neighbor.service.IReservaService;
import br.com.lphantus.neighbor.service.IServicoPrestadoService;
import br.com.lphantus.neighbor.service.ITelefoneService;
import br.com.lphantus.neighbor.service.ITotemService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.IVeiculoService;
import br.com.lphantus.neighbor.service.IVisitaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * 
 * @author aalencar Anotacao
 * @ManageBean identifica como o controle sera conhecido na pagina.
 * @Scope de request determina que o controle fica disponivel apenas durante a
 *        requisicao. Esta classe faz a interface entre a visao (xhtml) e as
 *        demais classes de servico e DAO
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "moradorPageController")
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorPageController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IAnimalEstimacaoService serviceAnimal;

	@Autowired
	private IMensagemService serviceMensagem;

	@Autowired
	private IMoradorService serviceMorador;

	@Autowired
	private IMoradorAgregadoService serviceMoradorAgregado;

	@Autowired
	private IOcorrenciaService serviceOcorrencia;

	@Autowired
	private IReservaService serviceReserva;

	@Autowired
	private IServicoPrestadoService serviceServicoPrestado;

	@Autowired
	private ITelefoneService serviceTelefone;

	@Autowired
	private ITotemService serviceTotem;

	@Autowired
	private IVeiculoService serviceVeiculo;

	@Autowired
	private IVisitaService serviceVisita;

	@Autowired
	private IModuloAcessoService serviceModuloAcesso;

	@Autowired
	private IUsuarioService serviceUsuario;

	// Entidades
	private MoradorDTO morador;
	private HistoricoDTO historico;
	private AgregadoDTO agregado;
	private AnimalEstimacaoDTO animalEstimacao;
	private MensagemDTO mensagem;
	private ReservaDTO reserva;
	private VeiculoDTO veiculo;
	private VisitaDTO visitaAgendada;
	private VisitanteDTO visitanteAgendado;
	private ServicoPrestadoDTO servicoAgendado;
	private PrestadorServicoDTO prestadorAgendado;
	private TelefoneDTO telefone;

	// Controle
	private Date dataInicial;
	private boolean dataInicialBloqueada;
	private boolean dataFinalBloqueada;
	private boolean documentoVisivel;
	// Listagens
	private List<MoradorAgregadoDTO> agregados = new ArrayList<MoradorAgregadoDTO>();
	private List<VeiculoDTO> veiculos = new ArrayList<VeiculoDTO>();
	private final List<TelefoneDTO> telefones = new ArrayList<TelefoneDTO>();
	private List<AnimalEstimacaoDTO> animaisEstimacao = new ArrayList<AnimalEstimacaoDTO>();
	private List<VisitaDTO> visitas = new ArrayList<VisitaDTO>();
	private List<VisitaDTO> visitasAgendadas = new ArrayList<VisitaDTO>();
	private List<ServicoPrestadoDTO> servicosPrestados = new ArrayList<ServicoPrestadoDTO>();
	private List<ServicoPrestadoDTO> servicosAgendados = new ArrayList<ServicoPrestadoDTO>();
	private List<OcorrenciaDTO> ocorrencias = new ArrayList<OcorrenciaDTO>();
	private List<MensagemDTO> mensagensRecebidas = new ArrayList<MensagemDTO>();
	private List<MensagemDTO> mensagensEnviadas = new ArrayList<MensagemDTO>();
	private List<ReservaDTO> reservas = new ArrayList<ReservaDTO>();

	private List<EnumTempoAcesso> tempoAcesso;
	private EnumTempoAcesso tempoAcessoVisita, tempoAcessoPrestador;

	private List<EnumTipoAcesso> tiposAcesso;
	private EnumTipoAcesso tipoAcessoVisita, tipoAcessoPrestador;

	private Date horaCarregamentoPermissoes = null;
	private List<ModuloAcessoDTO> modulos = null;

	/**
	 * Construtor padrao
	 */
	public MoradorPageController() {
	}

	@PostConstruct
	public void initialize() {
		this.atualizarTela();
	}

	/**
	 * Metodo para adicionar telefones ao morador principal da casa
	 */
	public void adicionarTelefone(final ActionEvent e) {
		if (this.morador.getTelefones() == null) {
			this.morador.setTelefones(new ArrayList<TelefoneDTO>());
		}
		if ((this.telefone.getNumero() == null) || this.telefone.getNumero().trim().equals("")) {
			JsfUtil.addErrorMessage("Favor informar o numero do telefone!");
		} else if ((this.telefone.getTipoTelefone() == null) || this.telefone.getTipoTelefone().trim().equals("")) {
			JsfUtil.addErrorMessage("Favor informar o tipo do telefone!");
		} else {
			final String telefoneTrim = this.telefone.getNumero().trim();
			boolean houveErro = false;
			for (final TelefoneDTO telefoneLoop : this.morador.getTelefones()) {
				if (telefoneLoop.getNumero().trim().equalsIgnoreCase(telefoneTrim)) {
					JsfUtil.addErrorMessage("Este telefone já foi adicionado!");
					houveErro = true;
					break;
				}
			}
			if (houveErro == false) {
				this.telefone.setMorador(this.morador);
				this.morador.getTelefones().add(this.telefone);
				this.telefone = new TelefoneDTO();
				JsfUtil.addSuccessMessage("Telefone Adicionado!");
			}
		}
	}

	public String agendarVisita() {
		this.visitaAgendada = new VisitaDTO();
		this.visitaAgendada.setMorador(this.morador);
		this.visitaAgendada.setAtiva(true);
		this.visitaAgendada.setConfirmado(false);
		if (this.visitanteAgendado == null) {
			this.visitaAgendada.setVisitante(new VisitanteDTO());
			this.visitaAgendada.getVisitante().setCondominio(condominioUsuarioLogado());
		} else {
			this.visitaAgendada.setVisitante(this.visitanteAgendado);
		}

		return "agendarVisita";
	}

	public String agendarServico() {
		this.servicoAgendado = new ServicoPrestadoDTO();
		this.servicoAgendado.setMorador(this.morador);
		this.servicoAgendado.setAtivo(true);
		this.servicoAgendado.setConfirmado(false);

		if (this.prestadorAgendado == null) {
			this.servicoAgendado.setPrestadorServico(new PrestadorServicoDTO());
			this.servicoAgendado.getPrestadorServico().setCondominio(condominioUsuarioLogado());
			this.servicoAgendado.getPrestadorServico().setPessoa(new PessoaFisicaDTO());
		} else {
			this.servicoAgendado.setPrestadorServico(this.prestadorAgendado);
		}

		return "agendarServico";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarVisita() {
		try {
			visitaAgendada.setTipoAcesso(tempoAcessoVisita.getTipo());
			visitaAgendada.getVisitante().getPessoa().setDataCadastro(new Date());
			this.serviceVisita.salvarVisitaAgendada(this.visitaAgendada);
			try {
				registrarHistorico(String.format("Registrou visita agendada \"%s\" para \"%s\"", this.visitaAgendada.getVisitante().getPessoa().getNome(), this.morador.getPessoa()
						.getNome()));

				TotemDTO totem = serviceTotem.registrarTotemVisitaAgendada(visitaAgendada);
				serviceTotem.geraArquivoSenhas(totem);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("TOTEM_AG_VISITA", totem.getSenha()));

			} catch (final Exception exception) {
				getLogger().error("Erro ao registrar visita na visao do morador.", exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}

			atualizarTela();

			this.visitas = this.serviceVisita.buscaVisitasAtivasConfirmadasByMorador(this.morador);
			this.visitasAgendadas = this.serviceVisita.buscaVisitasAgendadasByMorador(this.morador);
			this.visitaAgendada = new VisitaDTO();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

		} catch (final ServiceException e) {
			getLogger().error("Erro ao registrar visita.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removerVisita() {
		try {
			serviceVisita.removerVisitaAgendada(this.visitaAgendada);
			registrarHistorico(String.format("Removeu visita agendada para: %s de %d - %s", this.visitaAgendada.getVisitante().getPessoa().getNome(), this.morador.getPessoa()
					.getIdPessoa(), this.morador.getPessoa().getNome()));

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			this.visitas = this.serviceVisita.buscaVisitasAtivasConfirmadasByMorador(this.morador);
			this.visitasAgendadas = this.serviceVisita.buscaVisitasAgendadasByMorador(this.morador);
			this.visitaAgendada = new VisitaDTO();

			atualizarTela();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao remover visita na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void registrarServico() {
		try {
			servicoAgendado.setTipoAcesso(tempoAcessoPrestador.getTipo());
			this.serviceServicoPrestado.salvarServicoAgendado(this.servicoAgendado);
			try {
				registrarHistorico(String.format("Registrou servico agendado \"%s\" para \"%s\"", this.servicoAgendado.getPrestadorServico().getPessoa().getNome(), this.morador
						.getPessoa().getNome()));

				TotemDTO totem = serviceTotem.registrarTotemServicoAgendado(servicoAgendado);
				serviceTotem.geraArquivoSenhas(totem);

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("TOTEM_AG_SERVICO", totem.getSenha()));
			} catch (final Exception e) {
				getLogger().error("Erro ao registrar servico.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			}

			this.servicosPrestados = this.serviceServicoPrestado.getServicosAtivosConfirmadosByMorador(this.morador);
			this.servicosAgendados = this.serviceServicoPrestado.buscarServicosAgendadosMorador(this.morador);

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

		} catch (final ServiceException e) {
			getLogger().error("Erro ao registrar servico na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void removerPrestador() {
		try {
			serviceServicoPrestado.removerPrestadorAgendado(this.servicoAgendado);
			registrarHistorico(String.format("Removeu servico agendado para: %s de %d - %s", this.servicoAgendado.getPrestadorServico().getPessoa().getNome(), this.morador
					.getPessoa().getIdPessoa(), this.morador.getPessoa().getNome()));

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			this.servicosPrestados = this.serviceServicoPrestado.getServicosAtivosConfirmadosByMorador(this.morador);
			this.servicosAgendados = this.serviceServicoPrestado.buscarServicosAgendadosMorador(this.morador);

			atualizarTela();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao remover prestador na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 * Método para remover o telefone da lista do morador.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String removerTelefone() {
		try {
			this.morador.getTelefones().remove(this.telefone);

			// bug: so atualizar o telefone se o id dele nao for nulo
			if ((this.telefone != null) && (this.telefone.getId() != null)) {
				this.serviceTelefone.delete(TelefoneDTO.Builder.getInstance().createEntity(this.telefone));

				registrarHistorico("Removeu Telefone (" + this.telefone.getNumero() + "-" + this.telefone.getTipoTelefone() + ") do Morador: "
						+ this.morador.getPessoa().getIdPessoa() + " - " + this.morador.getPessoa().getNome());
			}

			this.telefone = new TelefoneDTO();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao remover telefones na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao remover telefones na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public void novaVisitaAgendada() {
		this.visitaAgendada = new VisitaDTO();
	}

	public void novoServicoPrestado() {
		this.servicoAgendado = new ServicoPrestadoDTO();
	}

	public boolean eMorador() throws ServiceException {
		final MoradorDTO moradorTemp = this.serviceMorador.buscarMoradorUsuario(usuarioLogado());
		if (moradorTemp != null) {
			return true;
		} else {
			return false;
		}
	}

	private void habilitaBotoes(final EnumTempoAcesso tempoAcessoComparacao) {
		dataInicialBloqueada = false;
		dataFinalBloqueada = false;
		if (EnumTempoAcesso.ENTRADA_UNICA.equals(tempoAcessoComparacao)) {
			this.dataInicialBloqueada = false;
			this.dataFinalBloqueada = true;
		} else if (EnumTempoAcesso.ENTRADA_LIVRE.equals(tempoAcessoComparacao)) {
			this.dataInicialBloqueada = true;
			this.dataFinalBloqueada = true;
		} else if (EnumTempoAcesso.PERIODO_DIAS.equals(tempoAcessoComparacao)) {
			this.dataInicialBloqueada = false;
			this.dataFinalBloqueada = false;
		}
	}

	public void trocaTempoAgendamento(final AjaxBehaviorEvent event) {
		visitaAgendada.setTipoAcesso(tempoAcessoVisita.getTipo());
		habilitaBotoes(tempoAcessoVisita);
	}

	public void trocaTempoNoCondominio(final AjaxBehaviorEvent event) {
		servicoAgendado.setTipoAcesso(tempoAcessoPrestador.getTipo());
		habilitaBotoes(tempoAcessoPrestador);
	}

	private void habilitaDocumento(final EnumTipoAcesso tipoAcessoComparacao) {
		this.documentoVisivel = false;
		if (EnumTipoAcesso.PEDESTRE.equals(tipoAcessoComparacao)) {
			this.documentoVisivel = false;
		} else if (EnumTipoAcesso.MOTORISTA.equals(tipoAcessoComparacao)) {
			this.documentoVisivel = true;
		}
	}

	public void trocaTipoEntradaVisitante(final AjaxBehaviorEvent event) {
		visitaAgendada.setTipoAcesso(tipoAcessoVisita.getTipo());
		habilitaDocumento(tipoAcessoVisita);
	}

	public void trocaTipoEntradaPrestador(final AjaxBehaviorEvent event) {
		servicoAgendado.setTipoAcesso(tipoAcessoPrestador.getTipo());
		habilitaDocumento(tipoAcessoPrestador);
	}

	private void atualizarTela() {
		try {
			final MoradorDTO moradorTemp = this.serviceMorador.buscarMoradorUsuario(usuarioLogado());
			this.morador = moradorTemp;
			this.dataInicial = new Date();

		} catch (final ServiceException e) {
			getLogger().error("Erro ao atualizar as informacoes do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void zerarCamposAgendamento() {
		this.dataInicialBloqueada = true;
		this.dataFinalBloqueada = true;
		this.documentoVisivel = false;

		tipoAcessoPrestador = null;
		tempoAcessoPrestador = null;

		tipoAcessoVisita = null;
		tempoAcessoVisita = null;
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String lerMensagem() {
		try {
			this.mensagem.setLido(true);
			this.serviceMensagem.update(MensagemDTO.Builder.getInstance().createEntity(this.mensagem));

			final String inserida = String.format("Leu mensagem - ID: %d REMETENTE: %s", this.mensagem.getId(), this.mensagem.getRemetente().getPessoa().getNome());

			registrarHistorico(inserida);

		} catch (final ServiceException e) {
			getLogger().error("Erro ao ler mensagem na pagina do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "LerMensagem";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarMorador() {
		try {

			final UsuarioDTO usuario = usuarioLogado();

			// limpar os dados de agregados e moradores, guardar a unidade habitacional
			this.morador.setAgregados(null);
			UnidadeHabitacionalDTO unidade = morador.getUnidadeHabitacional().get(0).getUnidadeHabitacional();
			this.morador.setUnidadeHabitacional(null);

			final Morador entidade = MoradorDTO.Builder.getInstance().createEntity(this.morador);
			entidade.setLogin(usuario.getLogin());
			entidade.setSenha(usuario.getSenha());
			if (null != usuario.getPlano()) {
				entidade.setPlano(PlanoDTO.Builder.getInstance().createEntity(usuario.getPlano()));
			}
			if (null != usuario.getModuloAcesso()) {
				entidade.setModuloAcesso(ModuloAcessoDTO.Builder.getInstance().createEntity(usuario.getModuloAcesso()));
			}
			if (null != usuario.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(usuario.getCondominio()));
			}

			// atualizar telefones
			if ((null != entidade.getTelefones()) && !entidade.getTelefones().isEmpty()) {
				for (final Telefone telefone : entidade.getTelefones()) {
					if (null == telefone.getId()) {
						this.serviceTelefone.save(telefone);
					} else {
						this.serviceTelefone.update(telefone);
					}
				}
			}

			this.serviceMorador.update(entidade);

			registrarHistorico(String.format("Atualizou dados do morador. Nome: %s | Casa: %s", morador.getPessoa().getNome(), unidade.getIdentificacao()));

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			return pagMoradorPrincipal();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar o morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	// direcionamento de paginas

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorPrincipal() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.telefone = new TelefoneDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar a pagina principal do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/principal.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorAgregados() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.agregados = this.serviceMoradorAgregado.listarAgregadosAtivosMorador(this.morador);
				this.agregado = new AgregadoDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar informacoes de agregados do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/agregados.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorAnimais() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.animaisEstimacao = this.serviceAnimal.listarAnimaisMorador(this.morador);
				this.animalEstimacao = new AnimalEstimacaoDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar informacao de animais do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/animais.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorMensagens() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.mensagensRecebidas = this.serviceMensagem.listarMensagensPorMorador(this.morador);
				this.mensagensEnviadas = this.serviceMensagem.listarMensagensPorMorador(usuarioLogado());
				this.mensagem = new MensagemDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar mensagens do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/mensagens.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorOcorrencias() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.ocorrencias = this.serviceOcorrencia.listarOcorrenciasPorMorador(this.morador);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar ocorrencias do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/ocorrencias.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorPrestadores() {

		gerarDadosAcesso();

		try {
			if (this.eMorador()) {
				atualizarTela();

				zerarCamposAgendamento();

				this.servicosPrestados = this.serviceServicoPrestado.getServicosAtivosConfirmadosByMorador(this.morador);
				this.servicosAgendados = this.serviceServicoPrestado.buscarServicosAgendadosMorador(this.morador);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar prestadores cadastrados para o morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/prestadores.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorVeiculos() {
		try {
			if (this.eMorador()) {
				atualizarTela();
				this.veiculos = this.serviceVeiculo.listarVeiculosMorador(this.morador);
				this.veiculo = new VeiculoDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar veiculos do morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/veiculos.jsf";
	}

	@Secured({ "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String pagMoradorVisitantes() {

		gerarDadosAcesso();

		try {
			if (this.eMorador()) {
				atualizarTela();
				zerarCamposAgendamento();
				this.visitas = this.serviceVisita.buscaVisitasAtivasConfirmadasByMorador(this.morador);
				this.visitasAgendadas = this.serviceVisita.buscaVisitasAgendadasByMorador(this.morador);
				this.visitaAgendada = new VisitaDTO();
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao acessar visitantes associados ao morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/morador/visitantes.jsf";
	}

	private void gerarDadosAcesso() {
		tempoAcesso = new ArrayList<EnumTempoAcesso>();
		tempoAcesso.addAll(Arrays.asList(EnumTempoAcesso.values()));

		tiposAcesso = new ArrayList<EnumTipoAcesso>();
		tiposAcesso.addAll(Arrays.asList(EnumTipoAcesso.values()));
	}

	private void buscaModulosAcesso() throws ServiceException {
		Date horaAtual = new Date();

		boolean carregar = false;

		if (null == horaCarregamentoPermissoes || null == modulos) {
			// se os objetos nao tiverem sido inicializados, carrega as
			// permissoes
			carregar = true;
		} else {
			// atualiza as permissoes a cada 5 minutos
			Calendar dataAnterior = GregorianCalendar.getInstance();
			dataAnterior.setTime(horaAtual);
			dataAnterior.add(Calendar.MINUTE, -5);
			if (dataAnterior.after(horaCarregamentoPermissoes)) {
				carregar = true;
			}
		}

		if (carregar) {
			UsuarioDTO sindico = serviceUsuario.buscarSindico(condominioUsuarioLogado());
			modulos = new ArrayList<ModuloAcessoDTO>();
			if ( null != sindico ){
				sindico = serviceUsuario.getUsuarioByLogin(sindico.getLogin());
				horaCarregamentoPermissoes = horaAtual;
				modulos = Arrays.asList(new ModuloAcessoDTO[] { sindico.getModuloAcesso() });
			}
		}
	}

	public boolean isContemReservas() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_CADASTRO_ITEM_RESERVA")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemOcorrencias() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_CADASTRO_OCORRENCIA")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemMensagemParaSindico() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_MENSAGEM_PARA_SINDICO")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemMensagemDoSindico() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_MENSAGEM_DO_SINDICO")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemTotem() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_TOTEN")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemBoletos() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_2A_VIA_BOLETOS")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	public boolean isContemArquivos() {
		try {
			buscaModulosAcesso();
			if (null != modulos) {
				for (ModuloAcessoDTO moduloCondominio : modulos) {
					if (null != moduloCondominio.getPermissoes()) {
						for (PermissaoDTO permissao : moduloCondominio.getPermissoes()) {

							if (permissao.getNome().contains("ROLE_BAIXAR_ARQUIVOS")) {
								return true;
							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			getLogger().error("Erro ao procurar permissao de reservas.", e);
		}
		return false;
	}

	// GETTER E SETTER

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the historico
	 */
	public HistoricoDTO getHistorico() {
		return this.historico;
	}

	/**
	 * @param historico
	 *            the historico to set
	 */
	public void setHistorico(final HistoricoDTO historico) {
		this.historico = historico;
	}

	/**
	 * @return the agregado
	 */
	public AgregadoDTO getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final AgregadoDTO agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the animalEstimacao
	 */
	public AnimalEstimacaoDTO getAnimalEstimacao() {
		return this.animalEstimacao;
	}

	/**
	 * @param animalEstimacao
	 *            the animalEstimacao to set
	 */
	public void setAnimalEstimacao(final AnimalEstimacaoDTO animalEstimacao) {
		this.animalEstimacao = animalEstimacao;
	}

	/**
	 * @return the mensagem
	 */
	public MensagemDTO getMensagem() {
		return this.mensagem;
	}

	/**
	 * @param mensagem
	 *            the mensagem to set
	 */
	public void setMensagem(final MensagemDTO mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @return the reserva
	 */
	public ReservaDTO getReserva() {
		return this.reserva;
	}

	/**
	 * @param reserva
	 *            the reserva to set
	 */
	public void setReserva(final ReservaDTO reserva) {
		this.reserva = reserva;
	}

	/**
	 * @return the veiculo
	 */
	public VeiculoDTO getVeiculo() {
		return this.veiculo;
	}

	/**
	 * @param veiculo
	 *            the veiculo to set
	 */
	public void setVeiculo(final VeiculoDTO veiculo) {
		this.veiculo = veiculo;
	}

	/**
	 * @return the visitaAgendada
	 */
	public VisitaDTO getVisitaAgendada() {
		return this.visitaAgendada;
	}

	/**
	 * @param visitaAgendada
	 *            the visitaAgendada to set
	 */
	public void setVisitaAgendada(final VisitaDTO visitaAgendada) {
		this.visitaAgendada = visitaAgendada;
	}

	/**
	 * @return the visitanteAgendado
	 */
	public VisitanteDTO getVisitanteAgendado() {
		return this.visitanteAgendado;
	}

	/**
	 * @param visitanteAgendado
	 *            the visitanteAgendado to set
	 */
	public void setVisitanteAgendado(final VisitanteDTO visitanteAgendado) {
		this.visitanteAgendado = visitanteAgendado;
	}

	/**
	 * @return the servicoAgendado
	 */
	public ServicoPrestadoDTO getServicoAgendado() {
		return this.servicoAgendado;
	}

	/**
	 * @param servicoAgendado
	 *            the servicoAgendado to set
	 */
	public void setServicoAgendado(final ServicoPrestadoDTO servicoAgendado) {
		this.servicoAgendado = servicoAgendado;
	}

	/**
	 * @return the prestadorAgendado
	 */
	public PrestadorServicoDTO getPrestadorAgendado() {
		return this.prestadorAgendado;
	}

	/**
	 * @param prestadorAgendado
	 *            the prestadorAgendado to set
	 */
	public void setPrestadorAgendado(final PrestadorServicoDTO prestadorAgendado) {
		this.prestadorAgendado = prestadorAgendado;
	}

	/**
	 * @return the telefone
	 */
	public TelefoneDTO getTelefone() {
		return this.telefone;
	}

	/**
	 * @param telefone
	 *            the telefone to set
	 */
	public void setTelefone(final TelefoneDTO telefone) {
		this.telefone = telefone;
	}

	/**
	 * @return the dataInicial
	 */
	public Date getDataInicial() {
		return this.dataInicial;
	}

	/**
	 * @param dataInicial
	 *            the dataInicial to set
	 */
	public void setDataInicial(final Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the agregados
	 */
	public List<MoradorAgregadoDTO> getAgregados() {
		return this.agregados;
	}

	/**
	 * @param agregados
	 *            the agregados to set
	 */
	public void setAgregados(final List<MoradorAgregadoDTO> agregados) {
		this.agregados = agregados;
	}

	/**
	 * @return the veiculos
	 */
	public List<VeiculoDTO> getVeiculos() {
		return this.veiculos;
	}

	/**
	 * @param veiculos
	 *            the veiculos to set
	 */
	public void setVeiculos(final List<VeiculoDTO> veiculos) {
		this.veiculos = veiculos;
	}

	/**
	 * @return the animaisEstimacao
	 */
	public List<AnimalEstimacaoDTO> getAnimaisEstimacao() {
		return this.animaisEstimacao;
	}

	/**
	 * @param animaisEstimacao
	 *            the animaisEstimacao to set
	 */
	public void setAnimaisEstimacao(final List<AnimalEstimacaoDTO> animaisEstimacao) {
		this.animaisEstimacao = animaisEstimacao;
	}

	/**
	 * @return the visitas
	 */
	public List<VisitaDTO> getVisitas() {
		return this.visitas;
	}

	/**
	 * @param visitas
	 *            the visitas to set
	 */
	public void setVisitas(final List<VisitaDTO> visitas) {
		this.visitas = visitas;
	}

	/**
	 * @return the visitasAgendadas
	 */
	public List<VisitaDTO> getVisitasAgendadas() {
		return this.visitasAgendadas;
	}

	/**
	 * @param visitasAgendadas
	 *            the visitasAgendadas to set
	 */
	public void setVisitasAgendadas(final List<VisitaDTO> visitasAgendadas) {
		this.visitasAgendadas = visitasAgendadas;
	}

	/**
	 * @return the servicosPrestados
	 */
	public List<ServicoPrestadoDTO> getServicosPrestados() {
		return this.servicosPrestados;
	}

	/**
	 * @param servicosPrestados
	 *            the servicosPrestados to set
	 */
	public void setServicosPrestados(final List<ServicoPrestadoDTO> servicosPrestados) {
		this.servicosPrestados = servicosPrestados;
	}

	/**
	 * @return the servicosAgendados
	 */
	public List<ServicoPrestadoDTO> getServicosAgendados() {
		return this.servicosAgendados;
	}

	/**
	 * @param servicosAgendados
	 *            the servicosAgendados to set
	 */
	public void setServicosAgendados(final List<ServicoPrestadoDTO> servicosAgendados) {
		this.servicosAgendados = servicosAgendados;
	}

	/**
	 * @return the ocorrencias
	 */
	public List<OcorrenciaDTO> getOcorrencias() {
		return this.ocorrencias;
	}

	/**
	 * @param ocorrencias
	 *            the ocorrencias to set
	 */
	public void setOcorrencias(final List<OcorrenciaDTO> ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	/**
	 * @return the mensagensRecebidas
	 */
	public List<MensagemDTO> getMensagensRecebidas() {
		return this.mensagensRecebidas;
	}

	/**
	 * @param mensagensRecebidas
	 *            the mensagensRecebidas to set
	 */
	public void setMensagensRecebidas(final List<MensagemDTO> mensagensRecebidas) {
		this.mensagensRecebidas = mensagensRecebidas;
	}

	/**
	 * @return the mensagensEnviadas
	 */
	public List<MensagemDTO> getMensagensEnviadas() {
		return this.mensagensEnviadas;
	}

	/**
	 * @param mensagensEnviadas
	 *            the mensagensEnviadas to set
	 */
	public void setMensagensEnviadas(final List<MensagemDTO> mensagensEnviadas) {
		this.mensagensEnviadas = mensagensEnviadas;
	}

	/**
	 * @return the dataInicialBloqueada
	 */
	public boolean isDataInicialBloqueada() {
		return this.dataInicialBloqueada;
	}

	/**
	 * @param dataInicialBloqueada
	 *            the dataInicialBloqueada to set
	 */
	public void setDataInicialBloqueada(final boolean dataInicialBloqueada) {
		this.dataInicialBloqueada = dataInicialBloqueada;
	}

	/**
	 * @return the dataFinalBloqueada
	 */
	public boolean isDataFinalBloqueada() {
		return this.dataFinalBloqueada;
	}

	/**
	 * @param dataFinalBloqueada
	 *            the dataFinalBloqueada to set
	 */
	public void setDataFinalBloqueada(final boolean dataFinalBloqueada) {
		this.dataFinalBloqueada = dataFinalBloqueada;
	}

	/**
	 * @return the documentoVisivel
	 */
	public boolean isDocumentoVisivel() {
		return this.documentoVisivel;
	}

	/**
	 * @param documentoVisivel
	 *            the documentoVisivel to set
	 */
	public void setDocumentoVisivel(final boolean documentoVisivel) {
		this.documentoVisivel = documentoVisivel;
	}

	/**
	 * @return the reservas
	 */
	public List<ReservaDTO> getReservas() {
		return this.reservas;
	}

	/**
	 * @param reservas
	 *            the reservas to set
	 */
	public void setReservas(final List<ReservaDTO> reservas) {
		this.reservas = reservas;
	}

	/**
	 * @return the telefones
	 */
	public List<TelefoneDTO> getTelefones() {
		return this.telefones;
	}

	/**
	 * @return the tempoAcesso
	 */
	public List<EnumTempoAcesso> getTempoAcesso() {
		return tempoAcesso;
	}

	/**
	 * @param tempoAcesso
	 *            the tempoAcesso to set
	 */
	public void setTempoAcesso(List<EnumTempoAcesso> tempoAcesso) {
		this.tempoAcesso = tempoAcesso;
	}

	/**
	 * @return the tipoAcessoVisita
	 */
	public EnumTipoAcesso getTipoAcessoVisita() {
		return tipoAcessoVisita;
	}

	/**
	 * @param tipoAcessoVisita
	 *            the tipoAcessoVisita to set
	 */
	public void setTipoAcessoVisita(EnumTipoAcesso tipoAcessoVisita) {
		this.tipoAcessoVisita = tipoAcessoVisita;
	}

	/**
	 * @return the tipoAcessoPrestador
	 */
	public EnumTipoAcesso getTipoAcessoPrestador() {
		return tipoAcessoPrestador;
	}

	/**
	 * @param tipoAcessoPrestador
	 *            the tipoAcessoPrestador to set
	 */
	public void setTipoAcessoPrestador(EnumTipoAcesso tipoAcessoPrestador) {
		this.tipoAcessoPrestador = tipoAcessoPrestador;
	}

	/**
	 * @return the tiposAcesso
	 */
	public List<EnumTipoAcesso> getTiposAcesso() {
		return tiposAcesso;
	}

	/**
	 * @param tiposAcesso
	 *            the tiposAcesso to set
	 */
	public void setTiposAcesso(List<EnumTipoAcesso> tiposAcesso) {
		this.tiposAcesso = tiposAcesso;
	}

	/**
	 * @return the tempoAcessoVisita
	 */
	public EnumTempoAcesso getTempoAcessoVisita() {
		return tempoAcessoVisita;
	}

	/**
	 * @param tempoAcessoVisita
	 *            the tempoAcessoVisita to set
	 */
	public void setTempoAcessoVisita(EnumTempoAcesso tempoAcessoVisita) {
		this.tempoAcessoVisita = tempoAcessoVisita;
	}

	/**
	 * @return the tempoAcessoPrestador
	 */
	public EnumTempoAcesso getTempoAcessoPrestador() {
		return tempoAcessoPrestador;
	}

	/**
	 * @param tempoAcessoPrestador
	 *            the tempoAcessoPrestador to set
	 */
	public void setTempoAcessoPrestador(EnumTempoAcesso tempoAcessoPrestador) {
		this.tempoAcessoPrestador = tempoAcessoPrestador;
	}

}
