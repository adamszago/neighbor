package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.service.IGrupoPermissaoService;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.IPermissaoService;
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
@ManagedBean(name = "moduloAcessoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class ModuloAcessoController extends AbstractController {

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
	private IGrupoPermissaoService grupoPermissaoService;

	// Modulo de Acesso
	private ModuloAcessoDTO moduloAcesso;
	private String nomeAnteriorModuloAcesso;
	private List<ModuloAcessoDTO> modulosAcesso;

	// Permissao
	private List<PermissaoDTO> permissoes = null;

	// Grupo Permissao
	private List<GrupoPermissaoDTO> grupos;
	private TreeNode treePermissoes;
	private TreeNode[] selectedPermissoes;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public ModuloAcessoController() {

	}

	/**
	 * Metodo para início de cadastro. Este metodo e chamado no construtor do
	 * controller Tambem e responsavel por listar os registros existentes no
	 * banco e alimentar a lista
	 */
	private void atualizarTela() {
		this.moduloAcesso = new ModuloAcessoDTO();
		listarModulosAcesso();
		listarPermissoes();
		this.nomeAnteriorModuloAcesso = null;
	}

	/**
	 * Método de Salvar registro Este método utilizar o objeto Service obtido
	 * atarvés de injeção pelo construtor. A classe JsfUtil providencia
	 * mensagens do tipo FacesMessage para serem enviadas A classe
	 * GerenciadoMensagem usa mensagens pre-definidas em arquivo properties para
	 * serem enviadas.
	 * 
	 * Obtem as Permissoes selecionadas atraves do array de objetos
	 * "permissoesSelecionadas" e seta na lista listaPermissoesSelecionadas que
	 * por sua vez e setada no objeto moduloAcesso a ser gravado
	 */
	@Secured({ "ROLE_CADASTRO_MODULOACESSO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarModuloAcesso(final ActionEvent event) {
		if (validarNomeModulo()) {
			try {
				this.moduloAcesso.setPermissoes(new HashSet<PermissaoDTO>(this
						.gerarPermissoesSelecionadas(getSelectedPermissoes())));
				this.moduloAcesso.setCondominio(condominioUsuarioLogado());

				this.moduloAcessoService.save(ModuloAcessoDTO.Builder
						.getInstance().createEntity(this.moduloAcesso));

				registrarHistorico("Gravou Modulo de Acesso: - "
						+ this.moduloAcesso.getNome());

				atualizarTela();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("SAVE_OK"));
			} catch (final Exception e) {
				getLogger().error("Erro ao salvar modulo de acesso.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("NOME_MODULO_NOK"));
		}
	}

	public boolean validarNomeModulo() {
		boolean isValido = true;
		// valida nome do modulo para nao duplicar modulos padrao do sistema
		if (this.moduloAcesso.getIdModuloAcesso() != null) {
			if (!this.moduloAcesso.getNome().equals(
					this.nomeAnteriorModuloAcesso)) {
				if (this.moduloAcesso.getNome().equals("Root")
						|| this.moduloAcesso.getNome().equals("Morador")) {
					isValido = false;
				} else {
					isValido = true;
				}
			}
		}
		return isValido;
	}

	/**
	 * Alterar Modulo de Acesso, seta as permissoes selecionados no Modulo de
	 * Acesso a ser alterado e faz update do Modulo de Acesso no banco em
	 * seguida chama o metodo atualizar tela
	 */
	@Secured({ "ROLE_EDITAR_MODULOACESSO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void alterarModuloAcesso() {
		if (validarNomeModulo()) {
			try {
				this.moduloAcesso.setPermissoes(new HashSet<PermissaoDTO>(this
						.gerarPermissoesSelecionadas(getSelectedPermissoes())));
				this.moduloAcesso.setCondominio(condominioUsuarioLogado());
				this.moduloAcessoService.update(ModuloAcessoDTO.Builder
						.getInstance().createEntity(this.moduloAcesso));

				registrarHistorico("Atualizou dados do Modulo de Acesso: "
						+ this.moduloAcesso.getIdModuloAcesso() + " - "
						+ this.moduloAcesso.getNome());

				atualizarTela();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			} catch (final ServiceException e) {
				getLogger().error("Erro ao alterar modulo de acesso.", e);
				JsfUtil.addErrorMessage(e.getMessage());
			}
		} else {
			JsfUtil.addErrorMessage(GerenciadorMensagem
					.getMensagem("NOME_MODULO_NOK"));
		}
	}

	/**
	 * Popula a lista de Modulos de Acesso com registros existentes no banco na
	 * tabela "moduloacesso", com excecao do Modulo de Acesso ROOT cujo id e 3
	 * (tabela: moduloacesso).
	 */
	@Secured({ "ROLE_LISTAR_MODULOACESSO", "ROLE_ROOT" })
	public void listarModulosAcesso() {
		this.modulosAcesso = new ArrayList<ModuloAcessoDTO>();
		final UsuarioDTO userLogado = usuarioLogado();
		try {
			if (userLogado != null) {
				boolean parametro;
				CondominioDTO condominio;
				if (userLogado.isRoot()) {
					parametro = true;
					condominio = null;
				} else {
					parametro = false;
					condominio = condominioUsuarioLogado();
				}
				this.modulosAcesso = this.moduloAcessoService
						.buscarPorCondominio(condominio, parametro);
			}
		} catch (final ServiceException e) {
			JsfUtil.addSuccessMessage(e.getMessage());
		}
	}

	/**
	 * Prepara o objeto moduloAcesso a ser editado e popula o array de objetos
	 * permissoesSelecionadas com a lista de permissoes do Modulo de Acesso a
	 * ser editado.
	 */
	public void listarPermissoesModuloEditar() {
		try {
			if (usuarioLogado().isRoot()) {
				gerarTreePermissoes(this.permissaoService.buscarTodas(),
						this.moduloAcesso.getPermissoesList());
			} else {
				gerarTreePermissoes(
						this.permissaoService.findByPlano(usuarioLogado()
								.getPlano()),
						this.moduloAcesso.getPermissoesList());
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void listarPermissoes() {
		try {
			final UsuarioDTO usuario = usuarioLogado();
			if (usuario.isRoot()) {
				this.permissoes = this.permissaoService.buscarTodas();
			} else {
				this.permissoes = this.permissaoService.findByPlano(usuario
						.getPlano());
			}

			gerarTreePermissoes(this.permissoes, null);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void gerarTreePermissoes(final List<PermissaoDTO> permissoes,
			final List<PermissaoDTO> permissoesSelecionadas) {
		try {
			// verificar os grupos que serao gerados na arvore
			final List<GrupoPermissaoDTO> gruposPermissaoAGerar = new ArrayList<GrupoPermissaoDTO>();
			final List<GrupoPermissaoDTO> lista = this.grupoPermissaoService
					.buscarTodos();

			for (final GrupoPermissaoDTO grupo : lista) {
				for (final PermissaoDTO permissaoAGerar : permissoes) {
					if (grupo.equals(permissaoAGerar.getGrupo())) {
						if (!gruposPermissaoAGerar.contains(grupo)) {
							gruposPermissaoAGerar.add(grupo);
						}
					}
				}
			}

			this.treePermissoes = new DefaultTreeNode("Root", null);
			// gerar a arvora com as permissoes da cada grupo
			for (final GrupoPermissaoDTO grupoPermisao : gruposPermissaoAGerar) {
				final TreeNode nodeGrupo = new DefaultTreeNode(
						grupoPermisao.getNome(), this.treePermissoes);
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
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public List<PermissaoDTO> gerarPermissoesSelecionadas(final TreeNode[] nodes) {
		final List<PermissaoDTO> permissoes = new ArrayList<PermissaoDTO>();
		try {

			if (nodes.length > 0) {
				for (int c = 0; c < nodes.length; c++) {
					final PermissaoDTO p = this.permissaoService
							.buscaPorLabel(nodes[c].getData().toString());
					if (p != null) {
						permissoes.add(p);
					}
				}
			}

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return permissoes;
	}

	// Direcionamento de paginas

	@Secured({ "ROLE_EDITAR_MODULOACESSO", "ROLE_ROOT" })
	public String editarModuloAcesso() {
		this.nomeAnteriorModuloAcesso = this.moduloAcesso.getNome();
		listarPermissoesModuloEditar();
		return "/pages/administracao/cadmoduloacesso.jsf";
	}

	@Secured({ "ROLE_CADASTRO_MODULOACESSO", "ROLE_ROOT" })
	public String novoModuloAcesso() {
		listarPermissoes();
		this.moduloAcesso = new ModuloAcessoDTO();
		return "/pages/administracao/cadmoduloacesso.jsf";
	}

	@Secured({ "ROLE_LISTA_MODULOACESSO", "ROLE_ROOT" })
	public String pageListaModuloAcesso() {
		atualizarTela();
		return "/pages/administracao/listamoduloacesso.jsf";
	}

	// GETTER AND SETTER

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
