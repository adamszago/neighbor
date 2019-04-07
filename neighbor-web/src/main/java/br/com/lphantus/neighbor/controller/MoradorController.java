package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ContratoDTO;
import br.com.lphantus.neighbor.common.EnderecoDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.common.TelefoneDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.common.VeiculoDTO;
import br.com.lphantus.neighbor.common.VisitaDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Endereco;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.Telefone;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.service.IAgregadoService;
import br.com.lphantus.neighbor.service.IAnimalEstimacaoService;
import br.com.lphantus.neighbor.service.IContratoService;
import br.com.lphantus.neighbor.service.IEnderecoService;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IMoradorAgregadoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IMoradorUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IOcorrenciaService;
import br.com.lphantus.neighbor.service.IServicoPrestadoService;
import br.com.lphantus.neighbor.service.ITelefoneService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.IVeiculoService;
import br.com.lphantus.neighbor.service.IVisitaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.integracao.mail.TextoMensagemMail;
import br.com.lphantus.neighbor.service.seguranca.Criptografia;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.util.comparator.NomeUfComparator;
import br.com.lphantus.neighbor.utils.Constantes;

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
@ManagedBean(name = "moradorController")
@Transactional(propagation = Propagation.SUPPORTS)
public class MoradorController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IAnimalEstimacaoService animalService;

	@Autowired
	private IAgregadoService agregadoService;

	@Autowired
	private IContratoService contratoService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IMoradorAgregadoService moradorAgregadoService;

	@Autowired
	private IMoradorUnidadeHabitacionalService moradorUnidadeHabitacionalService;

	@Autowired
	private IOcorrenciaService serviceOcorrencia;

	@Autowired
	private ITelefoneService telefoneService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IUnidadeHabitacionalService unidadeHabitacionalService;

	@Autowired
	private IVeiculoService veiculoService;

	@Autowired
	private IVisitaService visitaService;

	@Autowired
	private IServicoPrestadoService servicoPrestadoService;

	@Autowired
	private IEnderecoService enderecoService;

	@Autowired
	private IMailManager mailManager;

	// Morador
	private MoradorDTO morador;
	private MoradorUnidadeHabitacionalDTO moradorSelecionado;
	private List<MoradorUnidadeHabitacionalDTO> moradores = null;
	private List<MoradorUnidadeHabitacionalDTO> moradoresAtivos = null;

	// Agregado
	private MoradorAgregadoDTO agregado;
	private AgregadoDTO agregadoSelecionado;
	private List<AgregadoDTO> agregados = null;
	private Date dataMaximaNascimento = new Date();

	// Atributos morador
	private String paginaDestino;
	private TelefoneDTO telefone;
	private List<VeiculoDTO> veiculosMorador = null;
	private List<VisitaDTO> visitas;
	private List<ServicoPrestadoDTO> servicosPrestados;
	private List<SelectItem> estados = new ArrayList<SelectItem>();

	@SuppressWarnings("unused")
	private MoradorUnidadeHabitacionalDTO unidadeHabitacional;

	// historico
	private HistoricoDTO historico;

	// Variaveis
	private boolean atualizaListaMoradores;

	/**
	 * Construtor padrao
	 */
	public MoradorController() {

	}

	@PostConstruct
	public void initialize() {
		atualizarTela();
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {

		this.morador = new MoradorDTO();
		this.agregado = new MoradorAgregadoDTO();
		this.agregado.setAgregado(new AgregadoDTO());
		this.agregado.getAgregado().setPessoa(new PessoaFisicaDTO());
		this.unidadeHabitacional = new MoradorUnidadeHabitacionalDTO();
		this.agregadoSelecionado = new AgregadoDTO();
		this.telefone = new TelefoneDTO();
		this.veiculosMorador = new ArrayList<VeiculoDTO>();
		this.visitas = new ArrayList<VisitaDTO>();

		gerarListaMoradores();
		gerarListaMoradorAtivo();
		gerarListaAgregado();
		gerarListaEstados();

	}

	private void gerarListaEstados() {
		this.estados = new ArrayList<SelectItem>();

		final List<UnidadeFederativa> listaEstados = Arrays.asList(UnidadeFederativa.values());
		Collections.sort(listaEstados, new NomeUfComparator());

		for (final UnidadeFederativa estado : listaEstados) {
			if (!estado.equals(UnidadeFederativa.DESCONHECIDO)) {
				this.estados.add(new SelectItem(estado, estado.getSigla()));
			}
		}
	}

	public String buscarDadosMorador() throws Exception {
		try {
			this.morador = this.moradorService.buscarDetalhesMorador(this.morador);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	private void gerarListaMoradorAtivo() {
		this.moradoresAtivos = new ArrayList<MoradorUnidadeHabitacionalDTO>();
		
		Iterator<MoradorUnidadeHabitacionalDTO> iterator = moradores.iterator();
		while(iterator.hasNext() ){
			MoradorUnidadeHabitacionalDTO moradorAtual = iterator.next();
			if ( null != moradorAtual && null != moradorAtual.getMorador() ){
				if ( moradorAtual.getMorador().getPessoa().isAtivo() ){
					moradoresAtivos.add(moradorAtual);
				}
			}
		}
	}

	private void gerarListaMoradores() {
		this.moradores = new ArrayList<MoradorUnidadeHabitacionalDTO>();
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.moradores = this.moradorUnidadeHabitacionalService.listarMoradoresCondominio(condominio, null);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de moradores", e);
		}
	}

	private void gerarListaAgregado() {
		this.agregados = new ArrayList<AgregadoDTO>();
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.agregados = this.agregadoService.listarTodosAgregados(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista de agregados.", e);
		}
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 */
	@Secured({ "ROLE_CADASTRO_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravar() {
		try {

			// verificar se o condominio do usuario logado possui limite para
			// cadastrar mais usuarios.
			if (this.contratoService.verificarLimiteUsuarioBycondominio(condominioUsuarioLogado())) {

				final MoradorUnidadeHabitacionalDTO unidade = this.morador.getUnidadeHabitacional().get(0);

				if (condominioUsuarioLogado() != null) {
					unidade.getUnidadeHabitacional().setCondominio(condominioUsuarioLogado());
				}

				final Morador entidade = MoradorDTO.Builder.getInstance().createEntity(this.morador);

				criarEntidadesAgregados(entidade);

				this.moradorService.save(entidade);

				this.morador = entidade.createDto();

				this.atualizaListaMoradores = true;
				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

				this.atualizarTela();
			} else {
				getLogger().info("Cadastro de morador nao autorizado.");
				Long limiteUsuario = null;
				final ContratoDTO contrato = this.contratoService.getContratoByCondominio(condominioUsuarioLogado());
				if (contrato != null) {
					limiteUsuario = contrato.getLimiteUsuarios();
				}
				JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("LIMITE_USUARIO_ATINGIDO", limiteUsuario));
			}
			return "/pages/listagem/listamorador.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar morador.", e);
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}

	private void criarEntidadesAgregados(final Morador entidade) {
		entidade.setAgregados(new ArrayList<MoradorAgregado>());
		for (final MoradorAgregadoDTO relacionamento : this.morador.getAgregados()) {
			final MoradorAgregado adicionar = MoradorAgregadoDTO.Builder.getInstance().createEntity(relacionamento);
			adicionar.setMorador(entidade);
			adicionar.setAgregado(AgregadoDTO.Builder.getInstance().createEntity(relacionamento.getAgregado()));
			entidade.getAgregados().add(adicionar);
		}
	}

	public List<OcorrenciaDTO> getOcorrenciasMorador() {
		final List<OcorrenciaDTO> lista = new ArrayList<OcorrenciaDTO>();
		try {
			lista.addAll(this.serviceOcorrencia.listarOcorrenciasPorMorador(this.morador));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage("Erro ao buscar ocorrencias do morador.");
		}
		return lista;
	}

	public void limparMorador(final ActionEvent e) {
	}

	/**
	 * Exclui um registro da tabela morador
	 * 
	 * @throws ServiceException
	 */

	@Secured({ "ROLE_LISTA_DETALHES_MORADOR", "ROLE_ROOT" })
	public String listaVeiculos() {
		try {
			// this.morador.setVeiculos(new
			// HashSet<Veiculo>(veiculoService.listarVeiculosMorador(morador)));
			this.morador.setVeiculos(this.veiculoService.listarVeiculosMorador(this.morador));
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
		return null;

	}

	@Secured({ "ROLE_LISTA_DETALHES_MORADOR", "ROLE_ROOT" })
	public String listaAnimais() {
		try {
			// this.morador.setAnimaisEstimacao(new
			// HashSet<AnimalEstimacao>(animalService.listarAnimaisMorador(morador)));
			this.morador.setAnimaisEstimacao(this.animalService.listarAnimaisMorador(this.morador));
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("ERROR_MESSAGE"));
		}
		return null;

	}

	// @Secured({ "ROLE_EDITAR_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {

			final UsuarioDTO usuario = this.usuarioService.buscaUsuarioMorador(this.morador.getPessoa().getIdPessoa());

			final Morador entidade = PessoaFisicaDTO.Builder.getInstance().createEntityMorador(this.morador.getPessoa());

			// atualizar agregados
			for (final MoradorAgregadoDTO agr : this.morador.getAgregados()) {
				Agregado agregadoSalvar = AgregadoDTO.Builder.getInstance().createEntity(agr.getAgregado());
				final MoradorAgregado moradorAgregado = MoradorAgregadoDTO.Builder.getInstance().createEntity(agr);
				moradorAgregado.setMorador(MoradorDTO.Builder.getInstance().createEntity(this.morador));
				moradorAgregado.setAgregado(agregadoSalvar);
				if (null == agregadoSalvar.getIdPessoa()) {
					Date dataCadastro = new Date();
					agregadoSalvar.setDataCadastro(dataCadastro);
					agregadoService.save(agregadoSalvar);
					moradorAgregado.setAgregado(null);
					moradorAgregado.setMorador(null);
					moradorAgregado.setDataInicio(new Date());
					moradorAgregado.getChaveComposta().setIdMorador(morador.getPessoa().getIdPessoa());
					moradorAgregado.getChaveComposta().setIdAgregado(agregadoSalvar.getIdPessoa());
					moradorAgregadoService.save(moradorAgregado);
				} else {
					agregadoService.update(agregadoSalvar);
					moradorAgregadoService.update(moradorAgregado);
				}
			}

			// atualizar enderecos
			for (final MoradorUnidadeHabitacionalDTO relacionamento : this.morador.getUnidadeHabitacional()) {
				if (null != relacionamento.getUnidadeHabitacional()) {

					if ((null != relacionamento.getUnidadeHabitacional().getEndereco())) {
						final Endereco endereco = EnderecoDTO.Builder.getInstance().createEntity(relacionamento.getUnidadeHabitacional().getEndereco());
						if (null == relacionamento.getUnidadeHabitacional().getEndereco().getIdEndereco()) {
							this.enderecoService.save(endereco);
						} else {
							this.enderecoService.update(endereco);
						}
						final UnidadeHabitacional unidade = UnidadeHabitacionalDTO.Builder.getInstance().createEntity(relacionamento.getUnidadeHabitacional());
						unidade.setEndereco(endereco);
						this.unidadeHabitacionalService.update(unidade);
					}

					moradorUnidadeHabitacionalService.atualizarRelacionamento(relacionamento, morador);
				}
			}

			// atualizar telefones
			if ((null != entidade.getTelefones()) && !entidade.getTelefones().isEmpty()) {
				for (final Telefone telefone : entidade.getTelefones()) {
					if (null == telefone.getId()) {
						this.telefoneService.save(telefone);
					} else {
						this.telefoneService.update(telefone);
					}
				}
			}

			final Random rand = new Random();
			String novaSenha;
			do {
				novaSenha = rand.nextInt(9999) + 1000 + Constantes.VAZIO;
			} while (Criptografia.criptografar(novaSenha).equals(usuario.getSenha()));

			entidade.setLogin(usuario.getLogin());
			if (usuario.getPessoa() != null && !entidade.getMail().equals(usuario.getPessoa().getMail())) {
				entidade.setSenha(novaSenha);
			} else {
				entidade.setSenha(usuario.getSenha());
			}

			if (null != usuario.getPlano()) {
				entidade.setPlano(PlanoDTO.Builder.getInstance().createEntity(usuario.getPlano()));
			}
			if (null != usuario.getModuloAcesso()) {
				entidade.setModuloAcesso(ModuloAcessoDTO.Builder.getInstance().createEntity(usuario.getModuloAcesso()));
			}
			if (null != usuario.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(usuario.getCondominio()));
			}

			entidade.setUnidadeHabitacional(null);
			entidade.setAgregados(null);

			this.moradorService.saveOrUpdate(entidade);

			// se alterou o e-mail, envia novamente a senha
			if (usuario.getPessoa() != null && !entidade.getMail().equals(usuario.getPessoa().getMail())) {
				this.mailManager.enviarEmailUsuarioCadastrado(TextoMensagemMail.gerarTextoUsuarioCadastrado(this.morador, usuario.getLogin(), novaSenha), this.morador.getPessoa()
						.getMail());
			}

			registrarHistorico(String.format("Atualizou dados do Morador '%d - %s'", morador.getPessoa().getIdPessoa(), morador.getPessoa().getNome()));

			// bug 43
			// atualizarTela();
			this.atualizaListaMoradores = true;

			// bug 93
			// FacesContext.getCurrentInstance().getExternalContext().redirect(String.format("../../%s",listarMoradores()));
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			return "/pages/listagem/listamorador.jsf";

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}

	/**
	 * Metodo para adicionar moradores agregados ao morador principal da casa
	 */
	public void adicionarAgregado(final ActionEvent e) {

		if (this.morador.getAgregados() == null) {
			this.morador.setAgregados(new ArrayList<MoradorAgregadoDTO>());
		}

		final AgregadoDTO novoAgregado = this.agregado.getAgregado();

		if (((novoAgregado.getPessoa() == null) || (novoAgregado.getPessoa().getNome() == null)) || novoAgregado.getPessoa().getNome().trim().equals("")) {
			JsfUtil.addErrorMessage("Favor informar o nome do agregado!");
			return;
		}

		if ((novoAgregado.getPessoa() != null)) {

			if ((novoAgregado.getPessoa().getDataNascimento() == null) || novoAgregado.getPessoa().getDataNascimento().equals("")) {
				JsfUtil.addErrorMessage("Favor informar a data de nascimento do agregado!");
				return;
			}

			if ((this.agregado.getParentesco() == null) || this.agregado.getParentesco().trim().equals("")) {
				JsfUtil.addErrorMessage("Favor informar o parentesco do agregado!");
				return;
			} else {

				final MoradorAgregadoDTO novoRelacionamento = new MoradorAgregadoDTO();
				novoRelacionamento.setParentesco(this.agregado.getParentesco());
				novoRelacionamento.setMorador(this.morador);
				novoRelacionamento.setAgregado(novoAgregado);
				novoRelacionamento.setDataInicio(new Date());
				this.morador.getAgregados().add(novoRelacionamento);

				criaRelacionamento();
				JsfUtil.addSuccessMessage("Agregado adicionado!");
			}
		}
	}

	private void criaRelacionamento() {
		final MoradorAgregadoDTO relacionamento = new MoradorAgregadoDTO();
		relacionamento.setAgregado(new AgregadoDTO());
		relacionamento.setMorador(this.morador);
		this.agregado = relacionamento;
		// this.morador.getAgregados().add(relacionamento);
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

	/**
	 * 
	 * Método para remover o telefone da lista do morador.
	 */
	public String removerTelefone() {
		try {
			this.morador.getTelefones().remove(this.telefone);

			// bug: so atualizar o telefone se o id dele nao for nulo
			if ((this.telefone != null) && (this.telefone.getId() != null)) {
				this.telefoneService.delete(TelefoneDTO.Builder.getInstance().createEntity(this.telefone));

				registrarHistorico("Removeu Telefone (" + this.telefone.getNumero() + "-" + this.telefone.getTipoTelefone() + ") do Morador: "
						+ this.morador.getPessoa().getIdPessoa() + " - " + this.morador.getPessoa().getNome());
			}

			this.telefone = new TelefoneDTO();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * Método para remover o agregado da lista do morador.
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String removerAgregado() {
		try {

			this.morador.getAgregados().remove(this.agregado);
			this.agregado.getAgregado().getPessoa().setAtivo(false);
			this.agregado.setDataFim(new Date());

			// bug: so atualizar o agregado se o id dele nao for nulo
			if (null != this.agregado.getAgregado().getPessoa().getIdPessoa()) {
				this.moradorAgregadoService.update(MoradorAgregadoDTO.Builder.getInstance().createEntity(this.agregado));
				registrarHistorico("Removeu Agregado (" + this.agregado.getAgregado().getPessoa().getNome() + "-" + this.agregado.getParentesco() + ") do Morador: "
						+ this.morador.getPessoa().getIdPessoa() + " - " + this.morador.getPessoa().getNome());
			}

			this.agregado = new MoradorAgregadoDTO();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("DELETE_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	@Secured({ "ROLE_ALTERAR_STATUS_INTERFONE", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarStatusInterfone(final MoradorUnidadeHabitacionalDTO moradorSelecionado) {
		this.moradorSelecionado = moradorSelecionado;
		String novoStatus = "";
		try {
			final UnidadeHabitacionalDTO unidade = this.moradorSelecionado.getUnidadeHabitacional();
			UnidadeHabitacional entidade = unidadeHabitacionalService.findById(unidade.getIdUnidade());
			
			morador = moradorSelecionado.getMorador();
			if (entidade.isInterfoneOk()) {
				entidade.setInterfoneOk(false);
				novoStatus = "INATIVO";
			} else {
				entidade.setInterfoneOk(true);
				novoStatus = "ATIVO";
			}

			if (null == unidade.getEndereco().getIdEndereco()) {
				entidade.setEndereco(null);
			}

			this.unidadeHabitacionalService.update(entidade);
			
			// bug: quando manda atualizar os itens na mesma transacao, o status do interfone nao atualiza na tela
			new Thread(new Runnable() {
				@Override
				public void run() {
					atualizarTela();
				}
			}).start();
			
			registrarHistorico(String.format("Alterou status do interfone para: %s. Morador: %d - %s", novoStatus, this.morador.getPessoa().getIdPessoa(), this.morador.getPessoa()
					.getNome()));
			
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_ALTERAR_STATUS_MORADOR", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterarStatus() {
		String novoStatus = "";
		try {
			if (this.moradorSelecionado.getMorador().getPessoa().isAtivo()) {
				this.moradorSelecionado.getMorador().getPessoa().setAtivo(false);
				novoStatus = "INATIVO";
			} else {

				final MoradorDTO existente = this.moradorService.validaMoradorExistente(this.moradorSelecionado.getUnidadeHabitacional().getIdentificacao(),
						condominioUsuarioLogado());

				if (existente != null) {
					throw new ServiceException("Já existe um morador cadastrado para esta casa");
				} else {
					this.moradorSelecionado.getMorador().getPessoa().setAtivo(true);
					novoStatus = "ATIVO";
				}
			}
			this.moradorService.update(MoradorDTO.Builder.getInstance().createEntity(this.moradorSelecionado.getMorador()));

			registrarHistorico(String.format("Alterou status do morador para: %s. Morador: %d - %s ", novoStatus, this.moradorSelecionado.getMorador().getPessoa().getIdPessoa(),
					this.moradorSelecionado.getMorador().getPessoa().getNome()));

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public void getVisitasMorador() {
		try {
			if ((this.morador != null) && (this.morador.getPessoa().getIdPessoa() != null)) {
				this.visitas = this.visitaService.buscaVisitasByMorador(this.morador);
			} else {
				this.visitas = new ArrayList<VisitaDTO>();
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void getPrestacaoServicoMorador() {
		if ((this.morador != null) && (this.morador.getPessoa().getIdPessoa() != null)) {
			try {
				this.servicosPrestados = this.servicoPrestadoService.getPrestacaoServicoByMorador(this.morador);
			} catch (final ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}
		} else {
			this.servicosPrestados = new ArrayList<ServicoPrestadoDTO>();
		}
	}

	public void limparObjetosNovoMorador() {
		// criar novo morador
		this.morador = new MoradorDTO();

		// criar novo relacionamento de unidade habitacional
		final MoradorUnidadeHabitacionalDTO moradorUnidade = new MoradorUnidadeHabitacionalDTO();
		moradorUnidade.setUnidadeHabitacional(new UnidadeHabitacionalDTO());

		// carrega a unidade habitacional com o endereco do condominio
		moradorUnidade.getUnidadeHabitacional().setEndereco(condominioUsuarioLogado().getEndereco());
		moradorUnidade.getUnidadeHabitacional().getEndereco().setIdEndereco(null);

		// associar a unidade habitacional
		final ArrayList<MoradorUnidadeHabitacionalDTO> listaUnidades = new ArrayList<MoradorUnidadeHabitacionalDTO>();
		listaUnidades.add(moradorUnidade);
		this.morador.setUnidadeHabitacional(listaUnidades);

		// criar nova lista de agregados, telefones e historico
		this.morador.setAgregados(new ArrayList<MoradorAgregadoDTO>());
		this.telefone = new TelefoneDTO();
		this.historico = new HistoricoDTO();
	}

	// Direcionamento de Paginas
	@Secured({ "ROLE_LISTA_MORADOR", "ROLE_ROOT" })
	public String listarMoradores() {
		gerarListaMoradores();
		gerarListaMoradorAtivo();

		return "/pages/listagem/listamorador.jsf";
	}

	@Secured({ "ROLE_CADASTRO_MORADOR", "ROLE_ROOT" })
	public String novoMorador() {
		limparObjetosNovoMorador();
		return "/pages/cadastros/cadmorador.jsf";
	}

	@Secured({ "ROLE_CADASTRO_MORADOR", "ROLE_ROOT" })
	public String pageListaMoradoresAdm() {
		this.gerarListaMoradores();
		return "/pages/administracao/listamoradoradm.jsf";
	}

	@Secured({ "ROLE_EDITAR_MORADOR", "ROLE_ROOT" })
	public String editarMorador() {
		try {
			this.morador = this.moradorService.buscarDetalhesMorador(this.morador);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "/pages/cadastros/cadmorador.xhtml";
	}

	// Getter and Setter
	public String editarAgregado() {
		return null;
	}

	/**
	 * @return the dataMaximaNascimento
	 */
	public Date getDataMaximaNascimento() {
		return this.dataMaximaNascimento;
	}

	/**
	 * @param dataMaximaNascimento
	 *            the dataMaximaNascimento to set
	 */
	public void setDataMaximaNascimento(final Date dataMaximaNascimento) {
		this.dataMaximaNascimento = dataMaximaNascimento;
	}

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
	 * @return the moradorSelecionado
	 */
	public MoradorUnidadeHabitacionalDTO getMoradorSelecionado() {
		return this.moradorSelecionado;
	}

	/**
	 * @param moradorSelecionado
	 *            the moradorSelecionado to set
	 */
	public void setMoradorSelecionado(final MoradorUnidadeHabitacionalDTO moradorSelecionado) {
		this.moradorSelecionado = moradorSelecionado;
	}

	/**
	 * @return the moradores
	 */
	public List<MoradorUnidadeHabitacionalDTO> getMoradores() {
		if (this.atualizaListaMoradores) {
			atualizarTela();
			this.atualizaListaMoradores = false;
		}
		return this.moradores;
	}

	/**
	 * @return the paginaDestino
	 */
	public String getPaginaDestino() {
		return this.paginaDestino;
	}

	/**
	 * @param paginaDestino
	 *            the paginaDestino to set
	 */
	public void setPaginaDestino(final String paginaDestino) {
		this.paginaDestino = paginaDestino;
	}

	/**
	 * @return the agregado
	 */
	public MoradorAgregadoDTO getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final MoradorAgregadoDTO agregado) {
		this.agregado = agregado;
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
	 * @return the veiculosMorador
	 */
	public List<VeiculoDTO> getVeiculosMorador() {
		return this.veiculosMorador;
	}

	/**
	 * @param veiculosMorador
	 *            the veiculosMorador to set
	 */
	public void setVeiculosMorador(final List<VeiculoDTO> veiculosMorador) {
		this.veiculosMorador = veiculosMorador;
	}

	/**
	 * @return the agregadoSelecionado
	 */
	public AgregadoDTO getAgregadoSelecionado() {
		return this.agregadoSelecionado;
	}

	/**
	 * @param agregadoSelecionado
	 *            the agregadoSelecionado to set
	 */
	public void setAgregadoSelecionado(final AgregadoDTO agregadoSelecionado) {
		this.agregadoSelecionado = agregadoSelecionado;
	}

	/**
	 * @return the agregados
	 */
	public List<AgregadoDTO> getAgregados() {
		return this.agregados;
	}

	/**
	 * @param agregados
	 *            the agregados to set
	 */
	public void setAgregados(final List<AgregadoDTO> agregados) {
		this.agregados = agregados;
	}

	/**
	 * @return the moradoresAtivos
	 */
	public List<MoradorUnidadeHabitacionalDTO> getMoradoresAtivos() {
		if (this.atualizaListaMoradores) {
			atualizarTela();
			this.atualizaListaMoradores = false;
		}
		return this.moradoresAtivos;
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
	 * @return the estados
	 */
	public List<SelectItem> getEstados() {
		return this.estados;
	}

}
