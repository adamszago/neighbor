package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IGrupoPermissaoService;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IPermissaoService;
import br.com.lphantus.neighbor.service.IPlanoService;
import br.com.lphantus.neighbor.service.IUsuarioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
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
@ManagedBean(name = "acessoRootController")
@Transactional(propagation = Propagation.SUPPORTS)
public class AcessoRootController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Services
	@Autowired
	private IPermissaoService permissaoService;

	@Autowired
	private IModuloAcessoService moduloAcessoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private ICondominioService condominioService;

	@Autowired
	private IPlanoService planoService;

	@Autowired
	private IGrupoPermissaoService grupoPermissaoService;

	// Modulo Acesso
	private ModuloAcessoDTO[] modulosAcessoSelecionados;
	private ModuloAcessoDTO moduloAcesso;
	private String nomeAnteriorModuloAcesso;
	private List<ModuloAcessoDTO> modulosAcesso;

	// Permissao
	private List<PermissaoDTO> permissoes;
	private List<GrupoPermissaoDTO> grupos;
	private TreeNode[] selectedPermissoes;
	private TreeNode treePermissoes;

	// Usuario
	private UsuarioDTO usuario;
	private List<UsuarioDTO> usuarios;
	private String senhaConfirmacao;
	private String loginAnterior;
	private boolean atualizarItemTela;

	// Condominio
	private List<CondominioDTO> condominios;
	private CondominioDTO condominio;

	// Plano
	private List<PlanoDTO> planos;

	/**
	 * Construtor padrao
	 */
	public AcessoRootController() {

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
		moduloAcesso = new ModuloAcessoDTO();
		modulosAcesso = new ArrayList<ModuloAcessoDTO>();
		modulosAcessoSelecionados = null;
		permissoes = new ArrayList<PermissaoDTO>();
		usuario = new UsuarioDTO();
		loginAnterior = null;
		usuarios = new ArrayList<UsuarioDTO>();
		senhaConfirmacao = null;
		condominio = new CondominioDTO();
		nomeAnteriorModuloAcesso = null;
	}

	/*
	 * ==========================================================================
	 * ========================================= MODULO DE ACESSO
	 * ================
	 * ==========================================================
	 * ========================================
	 */

	public List<PermissaoDTO> gerarPermissoesSelecionadas(final TreeNode[] nodes) {
		final List<PermissaoDTO> permissoes = new ArrayList<PermissaoDTO>();
		try {
			if (nodes.length > 0) {
				for (int c = 0; c < nodes.length; c++) {
					final PermissaoDTO p = permissaoService
							.buscaPorLabel(nodes[c].getData().toString());
					if (p != null) {
						permissoes.add(p);
					}
				}
			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar permissoes selecionadas.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar permissoes selecionadas.", e);
		}
		return permissoes;
	}

	@Secured({ "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarModuloAcesso() {
		if (validarNomeModulo()) {
			try {
				moduloAcesso.setPermissoes(new HashSet<PermissaoDTO>(this
						.gerarPermissoesSelecionadas(getSelectedPermissoes())));
				moduloAcessoService.save(ModuloAcessoDTO.Builder.getInstance()
						.createEntity(moduloAcesso));
				moduloAcesso = new ModuloAcessoDTO();
				listarModulosAcesso();
				listarPermissoes();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("SAVE_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao gravar modulo de acesso.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			} catch (final Exception e) {
				getLogger().error("Erro ao gravar modulo de acesso.", e);
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("NOME_MODULO_NOK"));
		}
	}

	public boolean validarNomeModulo() {
		boolean isValido = true;
		// valida nome do modulo para nao duplicar modulos padrao do sistema
		if (moduloAcesso.getIdModuloAcesso() != null) {
			if (!moduloAcesso.getNome().equals(nomeAnteriorModuloAcesso)) {
				if (moduloAcesso.getNome().equals("Root")
						|| moduloAcesso.getNome().equals("Morador")) {
					isValido = false;
				} else {
					isValido = true;
				}
			}
		}
		return isValido;
	}

	@Secured({ "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarModuloAcesso() {
		if (validarNomeModulo()) {
			try {
				moduloAcesso.setPermissoes(new HashSet<PermissaoDTO>(this
						.gerarPermissoesSelecionadas(getSelectedPermissoes())));
				moduloAcesso.setCondominio(condominioUsuarioLogado());
				moduloAcessoService.update(ModuloAcessoDTO.Builder
						.getInstance().createEntity(moduloAcesso));
				moduloAcesso = new ModuloAcessoDTO();
				listarModulosAcesso();
				listarPermissoes();
				nomeAnteriorModuloAcesso = null;
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao alterar modulo de acesso.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			} catch (final Exception e) {
				getLogger().error("Erro ao alterar modulo de acesso.", e);
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("NOME_MODULO_NOK"));
		}
	}

	public void listarModulosAcesso() {
		try {
			modulosAcesso = moduloAcessoService.buscarPorCondominio(null, true);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar modulos de acesso.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar modulos de acesso.", e);
		}
	}

	public void listarPermissoes() {
		try {
			final List<PermissaoDTO> todasPermissoes = permissaoService
					.buscarTodas();
			gerarTreePermissoes(todasPermissoes, null);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar permissoes.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar permissoes.", e);
		}
	}

	public void listarPermissoesModuloEditar() {
		try {
			final List<PermissaoDTO> todos = permissaoService.buscarTodas();
			gerarTreePermissoes(todos, moduloAcesso.getPermissoesList());
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar permissoes modulo editar.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar permissoes modulo editar.", e);
		}
	}

	public void gerarTreePermissoes(final List<PermissaoDTO> permissoes,
			final List<PermissaoDTO> permissoesSelecionadas) {
		try {
			// verificar os grupos que serao gerados na arvore
			final List<GrupoPermissaoDTO> gruposPermissaoAGerar = new ArrayList<GrupoPermissaoDTO>();
			final List<GrupoPermissaoDTO> todosGrupos = grupoPermissaoService
					.buscarTodos();
			for (final GrupoPermissaoDTO grupo : todosGrupos) {
				for (final PermissaoDTO permissaoAGerar : permissoes) {
					if (grupo.equals(permissaoAGerar.getGrupo())) {
						if (!gruposPermissaoAGerar.contains(grupo)) {
							gruposPermissaoAGerar.add(grupo);
						}
					}
				}
			}

			treePermissoes = new DefaultTreeNode("Root", null);
			// gerar a arvora com as permissoes da cada grupo
			for (final GrupoPermissaoDTO grupoPermisao : gruposPermissaoAGerar) {
				final TreeNode nodeGrupo = new DefaultTreeNode(
						grupoPermisao.getNome(), treePermissoes);
				for (final PermissaoDTO permissao : grupoPermisao
						.getPermissoes()) {
					for (final PermissaoDTO permissaoAGerar : permissoes) {
						if (permissao.equals(permissaoAGerar)) { // criar node
							final TreeNode nodePermissao = new DefaultTreeNode(
									permissaoAGerar.getLabel(), nodeGrupo);
							if (permissoesSelecionadas != null) { // selecionar
								// node
								for (final PermissaoDTO permissaoSelecionada : permissoesSelecionadas) {
									if (permissaoAGerar
											.equals(permissaoSelecionada)) {
										nodeGrupo.setExpanded(true);
										nodePermissao.setSelected(true);
									}
								}
							}
						}
					}
				}
			}

		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar arvore de permissoes.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar arvore de permissoes.", e);
		}
	}

	public String getAnoCorrente() {
		final Calendar cl = Calendar.getInstance();
		final int campo = cl.get(Calendar.YEAR);
		return String.format("%d", campo);
	}

	// direcionamento de paginas

	@Secured({ "ROLE_ROOT" })
	public String novoModuloAcesso() {
		moduloAcesso = new ModuloAcessoDTO();
		listarPermissoes();
		return "/pages/root/cadmoduloacesso.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String editarModuloAcesso() {
		nomeAnteriorModuloAcesso = moduloAcesso.getNome();
		listarPermissoesModuloEditar();
		return "/pages/root/cadmoduloacesso.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String pageListarModulosAcesso() {
		listarModulosAcesso();
		return "/pages/root/listamoduloacesso.jsf";
	}

	/*
	 * ==========================================================================
	 * ========================================= USUARIO
	 * ========================
	 * ==================================================
	 * ========================================
	 */

	@Secured({ "ROLE_CADASTRO_USUARIO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarUsuario() {
		if (validarCampoSenha()) {
			if (validarCondominio()) {
				try {
					usuario.setRoot(false);
					usuario.setAtivo(true);

					usuarioService.save(UsuarioDTO.Builder.getInstance()
							.createEntity(usuario));

					// bug 43
					// usuario = new Usuario();
					// listarUsuarios();
					atualizarItemTela = true;
					usuario = new UsuarioDTO();

					JsfUtil.addSuccessMessage(GerenciadorMensagem
							.getMensagem("SAVE_OK"));
				} catch (final ServiceException e) {
					getLogger().error("Erro ao gravar usuario.", e);
					JsfUtil.addErrorMessage(e.getMessage());
				} catch (final Exception e) {
					getLogger().error("Erro ao gravar usuario.", e);
				}
			}// fim if condominio nulo
		} else { // fim if senha diferente
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("USUARIO_ERRO_CONFIRMA_SENHA"));
		}
	}

	// se retornar false NAO passou na validacao
	public boolean validarCampoSenha() {
		if (senhaConfirmacao.equals(usuario.getSenha())) {
			return true;
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("USUARIO_ERRO_CONFIRMA_SENHA"));
			return false;
		}
	}

	// se retornar false NAO passou na validacao
	public boolean validarCondominio() {

		final CondominioDTO condominio = usuario.getCondominio();

		if (condominio != null) {
			return true;
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("SELECIONE_CONDOMINIO"));
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

		try {
			if (usuario.isAtivo()) {
				usuario.setAtivo(false);
			} else {
				usuario.setAtivo(true);
			}

			usuarioService.update(UsuarioDTO.Builder.getInstance()
					.createEntity(usuario));
			usuario = new UsuarioDTO();
			listarUsuarios();

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao inativar usuario.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao inativar usuario.", e);
		}
	}

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

			usuario = new UsuarioDTO();
			loginAnterior = null;

			// bug 43
			// listarUsuarios();
			atualizarItemTela = true;

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao efetuar alteracao.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao efetuar alteracao.", e);
		}
	}

	/**
	 * Seta a variavel usuario para null
	 */
	public void limparUsaurio(final ActionEvent event) {
		usuario = null;
	}

	public void listarPlanos() {
		try {
			// plano nao se relaciona com entidades, elas que se relacionam a
			// ele, por isso nao precisa ter um metodo especifico de buscar
			// todos
			planos = PlanoDTO.Builder.getInstance().createList(
					planoService.findAll());
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar planos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar planos.", e);
		}
	}

	public void listarUsuarios() {
		try {
			if (atualizarItemTela) {
				atualizarTela();
				atualizarItemTela = false;
			}

			final UsuarioDTO userLogado = usuarioLogado();
			if (userLogado != null) {

				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}

				usuarios.clear();
				final List<UsuarioDTO> lista = usuarioService
						.buscarPorParametros(condominio);
				usuarios.addAll(lista);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar usuarios.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar usuarios.", e);
		}
	}

	public void listarCondominios() {
		try {
			condominios = condominioService.buscarTodos();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar condominios.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar condominios.", e);
		}
	}

	// Direcionamento de Paginas

	@Secured({ "ROLE_CADASTRO_USUARIO", "ROLE_ROOT" })
	public String novoUsuario() {
		usuario = new UsuarioDTO();
		usuario.setPessoa(new PessoaFisicaDTO());
		listarModulosAcesso();
		listarCondominios();
		listarPlanos();

		return "/pages/root/cadusuario.jsf";
	}

	@Secured({ "ROLE_EDITAR_USUARIO", "ROLE_ROOT" })
	public String editarUsuario() {
		loginAnterior = usuario.getLogin();
		listarModulosAcesso();
		listarCondominios();
		listarPlanos();

		return "/pages/root/cadusuario.jsf";
	}

	@Secured({ "ROLE_ROOT" })
	public String pageListarUsuarios() {
		listarUsuarios();
		return "/pages/root/listausuario.jsf";
	}

	// GETTERS E SETTERS

	/**
	 * @return the modulosAcessoSelecionados
	 */
	public ModuloAcessoDTO[] getModulosAcessoSelecionados() {
		return modulosAcessoSelecionados;
	}

	/**
	 * @param modulosAcessoSelecionados
	 *            the modulosAcessoSelecionados to set
	 */
	public void setModulosAcessoSelecionados(
			ModuloAcessoDTO[] modulosAcessoSelecionados) {
		this.modulosAcessoSelecionados = modulosAcessoSelecionados;
	}

	/**
	 * @return the moduloAcesso
	 */
	public ModuloAcessoDTO getModuloAcesso() {
		return moduloAcesso;
	}

	/**
	 * @param moduloAcesso
	 *            the moduloAcesso to set
	 */
	public void setModuloAcesso(ModuloAcessoDTO moduloAcesso) {
		this.moduloAcesso = moduloAcesso;
	}

	/**
	 * @return the nomeAnteriorModuloAcesso
	 */
	public String getNomeAnteriorModuloAcesso() {
		return nomeAnteriorModuloAcesso;
	}

	/**
	 * @param nomeAnteriorModuloAcesso
	 *            the nomeAnteriorModuloAcesso to set
	 */
	public void setNomeAnteriorModuloAcesso(String nomeAnteriorModuloAcesso) {
		this.nomeAnteriorModuloAcesso = nomeAnteriorModuloAcesso;
	}

	/**
	 * @return the modulosAcesso
	 */
	public List<ModuloAcessoDTO> getModulosAcesso() {
		return modulosAcesso;
	}

	/**
	 * @param modulosAcesso
	 *            the modulosAcesso to set
	 */
	public void setModulosAcesso(List<ModuloAcessoDTO> modulosAcesso) {
		this.modulosAcesso = modulosAcesso;
	}

	/**
	 * @return the permissoes
	 */
	public List<PermissaoDTO> getPermissoes() {
		return permissoes;
	}

	/**
	 * @param permissoes
	 *            the permissoes to set
	 */
	public void setPermissoes(List<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
	}

	/**
	 * @return the grupos
	 */
	public List<GrupoPermissaoDTO> getGrupos() {
		return grupos;
	}

	/**
	 * @param grupos
	 *            the grupos to set
	 */
	public void setGrupos(List<GrupoPermissaoDTO> grupos) {
		this.grupos = grupos;
	}

	/**
	 * @return the selectedPermissoes
	 */
	public TreeNode[] getSelectedPermissoes() {
		return selectedPermissoes;
	}

	/**
	 * @param selectedPermissoes
	 *            the selectedPermissoes to set
	 */
	public void setSelectedPermissoes(TreeNode[] selectedPermissoes) {
		this.selectedPermissoes = selectedPermissoes;
	}

	/**
	 * @return the treePermissoes
	 */
	public TreeNode getTreePermissoes() {
		return treePermissoes;
	}

	/**
	 * @param treePermissoes
	 *            the treePermissoes to set
	 */
	public void setTreePermissoes(TreeNode treePermissoes) {
		this.treePermissoes = treePermissoes;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioDTO getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
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
	public void setUsuarios(List<UsuarioDTO> usuarios) {
		this.usuarios = usuarios;
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
	public void setSenhaConfirmacao(String senhaConfirmacao) {
		this.senhaConfirmacao = senhaConfirmacao;
	}

	/**
	 * @return the loginAnterior
	 */
	public String getLoginAnterior() {
		return loginAnterior;
	}

	/**
	 * @param loginAnterior
	 *            the loginAnterior to set
	 */
	public void setLoginAnterior(String loginAnterior) {
		this.loginAnterior = loginAnterior;
	}

	/**
	 * @return the atualizarItemTela
	 */
	public boolean isAtualizarItemTela() {
		return atualizarItemTela;
	}

	/**
	 * @param atualizarItemTela
	 *            the atualizarItemTela to set
	 */
	public void setAtualizarItemTela(boolean atualizarItemTela) {
		this.atualizarItemTela = atualizarItemTela;
	}

	/**
	 * @return the condominios
	 */
	public List<CondominioDTO> getCondominios() {
		return condominios;
	}

	/**
	 * @param condominios
	 *            the condominios to set
	 */
	public void setCondominios(List<CondominioDTO> condominios) {
		this.condominios = condominios;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the planos
	 */
	public List<PlanoDTO> getPlanos() {
		return planos;
	}

	/**
	 * @param planos
	 *            the planos to set
	 */
	public void setPlanos(List<PlanoDTO> planos) {
		this.planos = planos;
	}

}
