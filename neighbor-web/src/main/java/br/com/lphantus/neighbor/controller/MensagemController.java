package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.MensagemDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IMensagemService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorLabel;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.Utilitarios;

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
@ManagedBean(name = "mensagemController")
@Transactional(propagation = Propagation.SUPPORTS)
public class MensagemController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IMensagemService service;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMailManager mailManager;

	// Mensagem
	private MensagemDTO mensagem;
	private boolean mensagemDoSindico = false;
	// private List<Mensagem> mensagens = null;
	private List<MensagemDTO> mensagensRecebidas = new ArrayList<MensagemDTO>();
	private List<MensagemDTO> mensagensEnviadas = new ArrayList<MensagemDTO>();
	private String destinatarios;

	// Controle
	private boolean todos = true;

	// Morador
	private List<MoradorDTO> moradoresDestino;
	private MoradorDTO[] moradoresSelecionados;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public MensagemController() {

	}

	@PostConstruct
	public void initialize() {
	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {
		this.mensagem = new MensagemDTO();
		this.todos = true;
		verificarUsuarioLogado();
		// this.mensagens = this.listarMensagens();
		this.mensagensEnviadas = this.gerarMensagensEnviadas();
		this.mensagensRecebidas = this.gerarMensagensRecebidas();
	}

	private List<MensagemDTO> gerarMensagensEnviadas() {
		List<MensagemDTO> resultado = new ArrayList<MensagemDTO>();
		try {
			if (usuarioLogado().isRoot()) {
				resultado = MensagemDTO.Builder.getInstance().createList(this.service.findAll());
			} else {
				resultado = this.service.listarMensagensPorMorador(usuarioLogado());
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return resultado;
	}

	private List<MensagemDTO> gerarMensagensRecebidas() {
		List<MensagemDTO> resultado = new ArrayList<MensagemDTO>();
		final UsuarioDTO userLogado = usuarioLogado();
		try {
			if (userLogado.isRoot()) {
				resultado = MensagemDTO.Builder.getInstance().createList(this.service.findAll());
			}

			final MoradorDTO moradorUsuario = this.moradorService.buscarMoradorUsuario(userLogado);

			if ((moradorUsuario != null) && !userLogado.isSindico()) {
				resultado = this.service.listarMensagensRecebidasUsuario(moradorUsuario.getPessoa().getIdPessoa());
			}
			if (userLogado.isSindico()) {
				resultado = this.service.listarMensagensRecebidasUsuario(userLogado.getPessoa().getIdPessoa());
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return resultado;
	}

	public void listarMoradoresDestino() {
		this.moradoresDestino = new ArrayList<MoradorDTO>();
		try {
			this.moradoresDestino = this.moradorService.listarMoradoresCondominio(condominioUsuarioLogado(), true);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void verificarUsuarioLogado() {
		final UsuarioDTO userLogado = usuarioLogado();
		if (!userLogado.isRoot()) {
			this.mensagem.setRemetente(userLogado);

			if (userLogado.isSindico()) {

				this.mensagemDoSindico = true;
			} else {
				UsuarioDTO u = new UsuarioDTO();
				try {
					u = this.usuarioService.buscarSindico(condominioUsuarioLogado());
				} catch (final ServiceException e) {
					JsfUtil.addErrorMessage(e.getMessage());
				}
				this.mensagem.setDestinatario(u);
				if (u != null) {
					this.mensagem.setDestinatario(u);
				}

			}
		}
	}

	// @Secured({"ROLE_MENSAGEM_DO_SINDICO", "ROLE_MENSAGEM_PARA_SINDICO",
	// "ROLE_ROOT"})
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravar(final ActionEvent e) {

		final String dataAtual = Utilitarios.diaCorrente();

		try {
			if (StringUtils.isBlank(this.mensagem.getMensagem())) {
				throw new ServiceException(GerenciadorLabel.getMensagem("msg.obrigatoria"));
			}
			if (this.mensagem.getMensagem().length() > 5000) {
				throw new ServiceException("Limite de caracteres da mensagem excedido!");
			}
			this.mensagem.setDataEnvio(new Date());
			if (this.mensagemDoSindico) {
				if (this.todos) {
					final List<MoradorDTO> lista = this.moradorService.listarMoradoresCondominio(condominioUsuarioLogado(), true);
					for (final MoradorDTO m : lista) {
						salvarMensagem(m, dataAtual);
					}
				} else {
					for (final MoradorDTO m : this.moradoresSelecionados) {

						salvarMensagem(m, dataAtual);
					}
				}
			} else {

				UsuarioDTO u = new UsuarioDTO();
				u = this.usuarioService.buscarSindico(condominioUsuarioLogado());

				final boolean temUsuario = u != null;
				if (temUsuario) {

					// Sindico sindico = sindicoService.buscarSindico(u);

					// if (sindico == null) {
					// throw new ServiceException(
					// "Não há síndico cadastrado, contate o suporte.");
					// } else {

					// bug 41 - isativo ou getEnable?
					if (u.isAtivo()) {
						this.service.saveMensagemSindico(this.mensagem);
						this.mailManager.enviarEmailAvisoMensagemRecebida(u);

						registrarHistorico(String.format("Mensagem Enviada | Data: %s | Assunto: %s", dataAtual, this.mensagem.getAssunto()));

						atualizarTela();
					} else {
						throw new ServiceException("Síndico deste condomínio não está ativo, contate o suporte.");
					}
				} else {
					throw new ServiceException("Não há síndico cadastrado, contate o suporte.");
				}

			}

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ENVIO_OK"));

		} catch (final ServiceException se) {
			JsfUtil.addErrorMessage(se.getMessage());
		}
	}

	private void salvarMensagem(final MoradorDTO m, final String dataAtual) throws ServiceException {
		final UsuarioDTO destinatario = this.usuarioService.buscaUsuarioMorador(m.getPessoa().getIdPessoa());
		this.mensagem.setDestinatario(destinatario);

		this.service.saveMensagemDoSindico(this.mensagem);
		this.mailManager.enviarEmailAvisoMensagemRecebida(m);

		registrarHistorico(String.format("Mensagem Enviada | Data: %s | Assunto: %s", dataAtual, this.mensagem.getAssunto()));
	}

	public List<Integer> gerarSequenciaCasas() {
		final Scanner s = new Scanner(this.destinatarios);
		s.useDelimiter(",");
		final List<Integer> result = new ArrayList<Integer>();
		while (s.hasNext()) {
			final String proximo = s.next();
			boolean primeiro = true;
			String valorInicial = "";
			String valorFinal = "";
			if (proximo.contains("-")) {
				for (int i = 0; i < proximo.length(); i++) {

					if (proximo.charAt(i) == '-') {
						primeiro = false;
					}
					if (Character.isDigit(proximo.charAt(i)) && primeiro) {
						valorInicial += proximo.charAt(i);
					}
					if (Character.isDigit(proximo.charAt(i)) && !primeiro) {
						valorFinal += proximo.charAt(i);
					}
				}

				int valorUm = Integer.parseInt(valorInicial);
				final int valorDois = Integer.parseInt(valorFinal);
				while (valorUm <= valorDois) {
					result.add(valorUm);
					// result += valorUm + ", ";
					valorUm++;
				}

			} else {
				result.add(Integer.parseInt(proximo));
				// result += proximo + ", ";
			}
		}
		s.close();
		return result;
	}

	@Secured({ "ROLE_MENSAGEM_DO_SINDICO", "ROLE_MENSAGEM_PARA_SINDICO", "ROLE_ROOT" })
	public void consultar(final ActionEvent event) {
		this.moradoresDestino = new ArrayList<MoradorDTO>();
		final UsuarioDTO userLogado = usuarioLogado();
		try {
			if (userLogado != null) {
				if (userLogado.isRoot()) {
					this.setMoradoresDestino(this.moradorService.findBuscaPersonalizada(this.gerarSequenciaCasas()));
				} else {

					final CondominioDTO condominioUsuario = condominioUsuarioLogado();

					for (final MoradorDTO mor : this.moradorService.findBuscaPersonalizada(this.gerarSequenciaCasas())) {

						final UsuarioDTO usuarioMorador = this.usuarioService.buscaUsuarioMorador(mor.getPessoa().getIdPessoa());

						final CondominioDTO condominioMorador = usuarioMorador.getCondominio();

						if (condominioMorador != null) {
							if (condominioMorador.getPessoa().getIdPessoa().equals(condominioUsuario.getPessoa().getIdPessoa())) {
								this.moradoresDestino.add(mor);
							}
						}
					}
				}
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_MENSAGEM_DO_SINDICO", "ROLE_MENSAGEM_PARA_SINDICO", "ROLE_ROOT" })
	private List<MensagemDTO> listarMensagens() throws ServiceException {
		List<MensagemDTO> msgs = new ArrayList<MensagemDTO>();
		final UsuarioDTO userLogado = usuarioLogado();
		if (userLogado != null) {
			if (userLogado.isRoot()) {
				msgs = MensagemDTO.Builder.getInstance().createList(this.service.findAll());
			} else {
				final List<MensagemDTO> lista = MensagemDTO.Builder.getInstance().createList(this.service.findAll());

				final CondominioDTO condominioUsuario = this.usuarioService.getCondominioUsuario(userLogado);

				for (final MensagemDTO msg : lista) {

					final CondominioDTO condominioMensagem = this.usuarioService.getCondominioUsuario(msg.getRemetente());

					if ((condominioMensagem != null) && condominioMensagem.getPessoa().getIdPessoa().equals(condominioUsuario.getPessoa().getIdPessoa())) {
						msgs.add(msg);
					}
				}
			}
		}
		return msgs;
	}

	@Secured({ "ROLE_LISTA_MENSAGEM", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String lerMensagem() {

		try {
			this.mensagem.setLido(true);
			this.service.update(MensagemDTO.Builder.getInstance().createEntity(this.mensagem));

			registrarHistorico(String.format("Leu Mensagem DE: \"%s\" - ID mensagem: %d", this.mensagem.getRemetente().getPessoa().getNome(), this.mensagem.getId()));

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return "LerMensagem";
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_LISTA_MENSAGEM", "ROLE_ROOT" })
	public String listaMensagens() {
		this.atualizarTela();
		return "/pages/comunicacao/listamensagens.jsf";
	}

	@Secured({ "ROLE_MENSAGEM_PARA_SINDICO", "ROLE_PAGINA_MORADOR", "ROLE_ROOT" })
	public String novaMensagemSindico() {
		this.atualizarTela();
		return "/pages/comunicacao/mensagemsindico.jsf";
	}

	@Secured({ "ROLE_MENSAGEM_DO_SINDICO", "ROLE_ROOT" })
	public String novaMensagemDoSindico() {
		atualizarTela();
		this.listarMoradoresDestino();
		return "/pages/comunicacao/mensagemdosindico.jsf";
	}

	public String novo() {
		atualizarTela();
		return null;
	}

	// GETTERS E SETTERS

	/**
	 * @return the mensagem
	 */
	public MensagemDTO getMensagem() {
		return mensagem;
	}

	/**
	 * @param mensagem
	 *            the mensagem to set
	 */
	public void setMensagem(MensagemDTO mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @return the mensagemDoSindico
	 */
	public boolean isMensagemDoSindico() {
		return mensagemDoSindico;
	}

	/**
	 * @param mensagemDoSindico
	 *            the mensagemDoSindico to set
	 */
	public void setMensagemDoSindico(boolean mensagemDoSindico) {
		this.mensagemDoSindico = mensagemDoSindico;
	}

	/**
	 * @return the mensagensRecebidas
	 */
	public List<MensagemDTO> getMensagensRecebidas() {
		return mensagensRecebidas;
	}

	/**
	 * @param mensagensRecebidas
	 *            the mensagensRecebidas to set
	 */
	public void setMensagensRecebidas(List<MensagemDTO> mensagensRecebidas) {
		this.mensagensRecebidas = mensagensRecebidas;
	}

	/**
	 * @return the mensagensEnviadas
	 */
	public List<MensagemDTO> getMensagensEnviadas() {
		return mensagensEnviadas;
	}

	/**
	 * @param mensagensEnviadas
	 *            the mensagensEnviadas to set
	 */
	public void setMensagensEnviadas(List<MensagemDTO> mensagensEnviadas) {
		this.mensagensEnviadas = mensagensEnviadas;
	}

	/**
	 * @return the destinatarios
	 */
	public String getDestinatarios() {
		return destinatarios;
	}

	/**
	 * @param destinatarios
	 *            the destinatarios to set
	 */
	public void setDestinatarios(String destinatarios) {
		this.destinatarios = destinatarios;
	}

	/**
	 * @return the todos
	 */
	public boolean isTodos() {
		return todos;
	}

	/**
	 * @param todos
	 *            the todos to set
	 */
	public void setTodos(boolean todos) {
		this.todos = todos;
	}

	/**
	 * @return the moradoresDestino
	 */
	public List<MoradorDTO> getMoradoresDestino() {
		return moradoresDestino;
	}

	/**
	 * @param moradoresDestino
	 *            the moradoresDestino to set
	 */
	public void setMoradoresDestino(List<MoradorDTO> moradoresDestino) {
		this.moradoresDestino = moradoresDestino;
	}

	/**
	 * @return the moradoresSelecionados
	 */
	public MoradorDTO[] getMoradoresSelecionados() {
		return moradoresSelecionados;
	}

	/**
	 * @param moradoresSelecionados
	 *            the moradoresSelecionados to set
	 */
	public void setMoradoresSelecionados(MoradorDTO[] moradoresSelecionados) {
		this.moradoresSelecionados = moradoresSelecionados;
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

}
