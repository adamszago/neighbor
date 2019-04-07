package br.com.lphantus.neighbor.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IMailManager;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.service.seguranca.Criptografia;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * 
 * @author Joander Vieira
 * @ManageBean identifica como o controle sera conhecido na pagina.
 * @Scope de request determina que o controle fica disponivel apenas durante a
 *        requisicao. Esta classe faz a interface entre a visao (xhtml) e as
 *        demais classes de servico e DAO
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "usuarioController")
@Transactional(propagation = Propagation.SUPPORTS)
public class UsuarioController extends AbstractController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IMoradorService moradorService;

	@Autowired
	private IMailManager mailManager;

	@Autowired
	private ICondominioService condominioService;

	@Autowired
	private ApplicationContext context;

	// Usuario
	private UsuarioDTO usuario;
	private List<UsuarioDTO> usuarios;
	private String loginAnterior;
	private boolean atualizarItemTela;

	// Morador
	private MoradorDTO morador;

	// Controle
	private Boolean usuarioIsLogado;
	private String senhaConfirmacao;

	// historico
	private HistoricoDTO historico;

	// Parametro Sistema, busca do arquivo properties se e a versao demostracao
	// ou producao do sistema
	@Value("${neighbor.param.isdemo}")
	private String sistemaIsDemo;

	/**
	 * Construtor de classe
	 */
	public UsuarioController() {

	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar as listas
	 */
	private void atualizarTela() {
		usuarios = null;
		usuarioIsLogado = null;
		senhaConfirmacao = null;
		usuario = new UsuarioDTO();
		loginAnterior = null;
		morador = new MoradorDTO();
	}

	public String selecionarMorador() {

		// this.usuario.setMorador(this.morador);
		usuario.getPessoa().setNome(morador.getPessoa().getNome());
		((PessoaFisicaDTO) usuario.getPessoa()).setCpf(morador.getPessoa().getCpf());

		return null;
	}

	/**
	 * Popula a lista de Usuarios "usuariosComum" com registros existentes no
	 * banco na tabela "usuario", com excecao do Usuario ROOT cujo id e 1
	 * (tabela: usuario).
	 */
	@Secured({ "ROLE_LISTA_USUARIO", "ROLE_ROOT" })
	public void listarUsuarios() {
		usuarios = new ArrayList<UsuarioDTO>();
		try {
			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {

				if (atualizarItemTela) {
					atualizarTela();
					atualizarItemTela = false;
				}

				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				usuarios = usuarioService.findAllByIdCondominioWithoutRoot(condominio);
			}
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 * 
	 */
	@Secured({ "ROLE_CADASTRO_USUARIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarUsuario() {
		if (validarCampoSenha()) {
			try {
				if (condominioUsuarioLogado() != null) {
					usuario.setCondominio(condominioUsuarioLogado());
				}

				usuario.setRoot(false);
				usuario.setAtivo(true);
				// TODO: revisar este trecho
				// if ((this.morador != null)
				// && (this.morador.getIdMorador() != null)) {
				// this.usuario.setMorador(this.morador);
				// }

				usuarioService.save(UsuarioDTO.Builder.getInstance().createEntity(usuario));

				registrarHistorico("Gravou Usuario: - " + usuario.getPessoa().getNome(), null);

				// bug 43
				// atualizarTela();
				atualizarItemTela = true;
				usuario = new UsuarioDTO();
				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));
			} catch (final ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}

		}
	}

	// se retornar false NAO passou na validacao
	public boolean validarCampoSenha() {
		if (senhaConfirmacao.equals(usuario.getSenha())) {
			return true;
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("USUARIO_ERRO_CONFIRMA_SENHA"));
			return false;
		}
	}

	/**
	 * Inativar Usuario, seta a propiedade "enable" do usuario para "false" ou
	 * "true", sendo que se estado atual for "true", seta para "false", se for
	 * "false", seta para "true"
	 */
	@Secured({ "ROLE_ALTERAR_STATUS_USUARIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void inativarUsuario() {
		String novoStatus = "";
		try {
			if (usuario.isAtivo()) {
				usuario.setAtivo(false);
				novoStatus = "INATIVOU";
			} else {
				usuario.setAtivo(true);
				novoStatus = "ATIVOU";
			}

			usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuario));

			registrarHistorico(novoStatus + " Usuario: " + usuario.getPessoa().getIdPessoa() + " - " + usuario.getPessoa().getNome(), null);

			atualizarTela();
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		atualizarTela();

	}

	/**
	 * Alterar usuario, seta os Modulos de Acesso selecionados no usuario a ser
	 * alterado e faz update do usuario no banco em seguida chama o metodo
	 * atualizar tela
	 */
	@Secured({ "ROLE_EDITAR_USUARIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarUsuario() {
		if ((senhaConfirmacao != null) && (senhaConfirmacao != "")) {
			if (validarCampoSenha()) {
				efetuarAlteracao();
			}
		} else {
			efetuarAlteracao();
		}

	}

	private void efetuarAlteracao() {
		try {

			usuarioService.update(usuario, loginAnterior);

			registrarHistorico("Atualizou dados do Usuario: " + usuario.getPessoa().getIdPessoa() + " - " + usuario.getPessoa().getNome(), null);

			// bug 43
			// atualizarTela();
			atualizarItemTela = true;
			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * Efetua Logout do usuario na sessao e seta parametros relacionados ao
	 * usuario da sessao para null em seguida direciona para pagina de login
	 */
	public String logout() {
		usuarioService.logout();
		usuarioIsLogado = false;
		return "loginForm";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String enviarEmailRecuperacao() {
		if ((usuario.getLogin() != null) && !usuario.getLogin().isEmpty()) {
			try {
				usuario = usuarioService.getUsuarioByLogin(usuario.getLogin());
				if (usuario == null) {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("USUARIO_NOK"));
				} else {
					if ((usuario.getPessoa().getMail() == null) || usuario.getPessoa().getMail().equals("")) {
						JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("USUARIO_SEM_EMAIL"));
					} else {
						final Random rand = new Random();
						final String novaSenha = rand.nextInt(9999) + 1000 + "";

						Usuario usuarioBanco = usuarioService.findById(usuario.getPessoa().getIdPessoa());
						usuarioBanco.setSenha(Criptografia.criptografar(novaSenha));
						usuarioService.update(usuarioBanco);

						this.registrarHistorico(String.format("Usuario %s solicitou recuperação de senha.", usuario.getLogin()), usuario);
						mailManager.enviarEmailRecuperacao(usuario, novaSenha);

						JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ENVIO_USUARIO_SENHA_OK"));
					}
				}
			} catch (final ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}

		} else if ((usuario.getPessoa().getMail() != null) && !usuario.getPessoa().getMail().isEmpty()) {
			try {
				usuario = usuarioService.getUsuarioByEmail(usuario.getPessoa().getMail());
				if (usuario == null) {
					JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("EMAIL_NOK"));
				} else {
					final Random rand = new Random();
					final String novaSenha = rand.nextInt(9999) + 1000 + "";
					usuario.setSenha(novaSenha);
					usuarioService.update(UsuarioDTO.Builder.getInstance().createEntity(usuario));

					this.registrarHistorico("Usuario " + usuario.getLogin() + " solicitou recuperação de senha", usuario);

					mailManager.enviarEmailRecuperacao(usuario, novaSenha);

					JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ENVIO_USUARIO_SENHA_OK"));
				}
			} catch (final ServiceException e) {
				JsfUtil.addErrorMessage(e.getMessage());
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem.getMensagem("USUARIO_OU_EMAIL_NOK"));
		}

		usuario = new UsuarioDTO();
		return "/pages/acesso/esquecisenha.jsf?status=ok";
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_EDITAR_USUARIO", "ROLE_ROOT" })
	public String editarUsuario() {
		loginAnterior = usuario.getLogin();
		return "editarUsuario";
	}

	@Secured({ "ROLE_CADASTRO_USUARIO", "ROLE_ROOT" })
	public String novoUsuario() {
		atualizarTela();
		return "/pages/administracao/cadusuario.jsf";
	}

	@Secured({ "ROLE_LISTA_USUARIO", "ROLE_ROOT" })
	public String pageListaUsuarios() {
		listarUsuarios();
		atualizarTela();
		return "/pages/administracao/listausuario.jsf";
	}

	public String pageRedefinirSenha() {
		usuario = new UsuarioDTO();
		return "/pages/acesso/redefinirsenha.jsf";
	}

	public void checarContaSuspensa() throws IOException {
		final CondominioDTO condominio = condominioUsuarioLogado();
		if (null != condominio) {
			try {
				Condominio cond = condominioService.findById(condominio.getPessoa().getIdPessoa());
				if (null != cond && cond.isContaSuspensa()) {
					final String caminho = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
					FacesContext.getCurrentInstance().getExternalContext().redirect(String.format("%s/pages/acesso/suspenso.jsf", caminho));
				}
			} catch (ServiceException e) {
				getLogger().error("Erro ao buscar por id.", e);
			}
		}
	}

	// GETTER AND SETTER

	public UsuarioDTO getUserLogado() {
		return usuarioService.getUsuarioLogado();
	}

	public Boolean getUserLogadoMorador() throws ServiceException {
		boolean retorno;
		final UsuarioDTO usuario = usuarioLogado();
		if (usuario == null) {
			retorno = true;
		} else {
			if (usuario.isRoot() || usuario.isSindico()) {
				retorno = false;
			} else {
				final MoradorDTO morador = moradorService.buscarMoradorUsuario(usuario);
				if (morador == null) {
					retorno = false;
				} else {
					retorno = true;
				}
			}
		}

		return retorno;
	}

	public Boolean getTerraNova() {
		return (null != usuarioLogado() && usuarioLogado().getLogin().toLowerCase().contains("terranova."));
	}

	public Boolean getFour() {
		return (null != usuarioLogado() && usuarioLogado().getLogin().toLowerCase().contains("four."));
	}

	public String getUrlImagem() {
		String retorno;
		if (getTerraNova()) {
			retorno = "../../resources/images/logo_terranova.gif";
		} else {
			if (getFour()) {
				retorno = "../../resources/images/logo_four.jpg";
			} else {
				retorno = "../../resources/images/logo_neighbor.gif";
			}
		}
		return retorno;
	}

	public Boolean getBackgroundCustomizado() {
		if (getTerraNova()) {
			return true;
		}
		return false;
	}

	public String getUrlCssCustomizado() {
		String retorno;
		if (getTerraNova()) {
			retorno = "../../resources/css/terranova-min.css";
		} else {
			retorno = "";
		}
		return retorno;
	}

	public Boolean getUsuarioIsLogado() {
		if ((usuarioIsLogado == null) || (usuarioIsLogado == false)) {
			if (null == usuarioService) {
				usuarioIsLogado = false;
			} else {
				usuarioIsLogado = usuarioService.isLogado();
			}
		}
		return usuarioIsLogado;
	}

	public void setUsuarioIsLogado(final Boolean usuarioIsLogado) {
		this.usuarioIsLogado = usuarioIsLogado;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioDTO getUsuario() {
		if (null == usuario) {
			usuario = new UsuarioDTO();
		}
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(final UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the senhaConfirmacao
	 */
	public String getSenhaConfirmacao() {
		return senhaConfirmacao;
	}

	/**
	 * @param senhaConfirmacao
	 *            the senhaConfirmacao to set
	 */
	public void setSenhaConfirmacao(final String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
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
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the usuarios
	 */
	public List<UsuarioDTO> getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios
	 *            the usuarios to set
	 */
	public void setUsuarios(final List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
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
	public void setHistorico(final HistoricoDTO historico) {
		this.historico = historico;
	}

	/**
	 * @return the sistemaIsDemo
	 */
	public String getSistemaIsDemo() {
		return sistemaIsDemo;
	}

	/**
	 * @param sistemaIsDemo
	 *            the sistemaIsDemo to set
	 */
	public void setSistemaIsDemo(final String sistemaIsDemo) {
		this.sistemaIsDemo = sistemaIsDemo;
	}

}
