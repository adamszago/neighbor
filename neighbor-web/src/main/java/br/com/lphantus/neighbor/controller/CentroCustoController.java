package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.entity.CentroCusto;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * @author Joander Vieira Candido
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "centroCustoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class CentroCustoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ICentroCustoService centroCustoService;

	private List<CentroCustoDTO> entities;
	private CentroCustoDTO entity;

	private String valueSelectStatus;

	private final String TODOS = "TODOS";
	private final String ATIVOS = "ATIVOS";
	private final String INATIVOS = "INATIVOS";

	private TreeNode treeRootCentrosCusto;
	private TreeNode nodeCentroCustoSelecionado;

	private String emptyMessageLista;

	/**
	 * Construtor padrao
	 */
	public CentroCustoController() {

	}

	@PostConstruct
	public void initialize() {
		this.valueSelectStatus = this.ATIVOS;
	}

	private void gerarLista() {
		this.entities = new ArrayList<CentroCustoDTO>();

		Boolean parametro;
		if (this.valueSelectStatus.equals(this.ATIVOS)) {
			parametro = true;
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			parametro = false;
		} else {
			parametro = null;
		}
		gerarListaCentrosParametrizada(parametro);
	}

	private void gerarListaCentrosParametrizada(final Boolean parametro) {
		this.entities = new ArrayList<CentroCustoDTO>();
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities.addAll(this.centroCustoService
						.buscarPorCondominio(parametro, condominio));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista centros de custo.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista centros de custo.", e);
		}
	}

	public void onSelectStatus() {
		gerarLista();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ativarDesativar() {
		try {
			if (this.entity != null) {
				if (this.entity.isAtivo()) {
					this.entity.setAtivo(false);
				} else {
					this.entity.setAtivo(true);
				}
				this.centroCustoService.update(CentroCustoDTO.Builder
						.getInstance().createEntity(this.entity));
				gerarLista();
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void gerarArvoreCentrosCusto() {
		try {
			this.treeRootCentrosCusto = new DefaultTreeNode("Root", null);
			treeRootCentrosCusto.setExpanded(true);
			gerarFilhos(this.treeRootCentrosCusto);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void gerarFilhos(final TreeNode pai) throws ServiceException {
		final List<CentroCustoDTO> filhos = new ArrayList<CentroCustoDTO>();
		if (pai == this.treeRootCentrosCusto) {
			filhos.addAll(this.centroCustoService
					.findCentrosCustoRaizByCondominio(condominioUsuarioLogado()));
		} else {
			final CentroCustoDTO dto = (CentroCustoDTO) pai.getData();
			filhos.addAll(this.centroCustoService.findCentrosCustoFilhos(dto));
		}

		for (final CentroCustoDTO filho : filhos) {
			final TreeNode treeFilho = new DefaultTreeNode(filho, pai);
			treeFilho.setExpanded(true);
			if (!pai.getChildren().contains(treeFilho)) {
				pai.getChildren().add(treeFilho);
			}
			gerarFilhos(treeFilho);
		}

	}

	public void limparObjetoCentroCusto() {
		this.entity = new CentroCustoDTO();
		desselecionarPai();
	}

	@Secured({ "ROLE_ADICIONAR_CENTRO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarCentroCusto() {
		try {

			this.entity.setDataCadastro(new Date());
			this.entity.setCondominio(condominioUsuarioLogado());
			if (null != this.nodeCentroCustoSelecionado) {
				this.entity
						.setCentroCustoPai((CentroCustoDTO) this.nodeCentroCustoSelecionado
								.getData());
			}

			final CentroCusto entidade = CentroCustoDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.centroCustoService.save(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			this.valueSelectStatus = this.TODOS;

			return pageListaCentroCusto();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar centro custo.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar centro custo.", e);
		}

		return "";
	}

	@Secured({ "ROLE_EDITAR_LANCAMENTO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {
			if (((this.nodeCentroCustoSelecionado == null) || (this.nodeCentroCustoSelecionado
					.getData() == null))
					|| this.nodeCentroCustoSelecionado.getData().toString()
							.equalsIgnoreCase("root")) {
				this.entity.setCentroCustoPai(null);
			} else {
				this.entity
						.setCentroCustoPai((CentroCustoDTO) this.nodeCentroCustoSelecionado
								.getData());
			}
			final CentroCusto entidade = CentroCustoDTO.Builder.getInstance()
					.createEntity(this.entity);
			this.centroCustoService.update(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			this.valueSelectStatus = this.TODOS;

			return pageListaCentroCusto();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
		}

		return null;
	}

	public String getEmptyMessageLista() {
		if (this.valueSelectStatus.equals(this.ATIVOS)) {
			this.emptyMessageLista = "Sem centros de custo ativos para exibir";
		} else if (this.valueSelectStatus.equals(this.INATIVOS)) {
			this.emptyMessageLista = "Sem centros de custo inativos para exibir";
		} else if (this.valueSelectStatus.equals(this.TODOS)) {
			this.emptyMessageLista = "Sem centros de custo cadastrados para exibir";
		}
		return this.emptyMessageLista;
	}

	@Secured({ "ROLE_LISTAR_CENTRO", "ROLE_ROOT" })
	public String pageListaCentroCusto() {

		gerarLista();
		gerarArvoreCentrosCusto();

		return "/pages/financeiro/listacentrocusto.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_CENTRO", "ROLE_ROOT" })
	public String pageCadastroCentroCusto() {

		limparObjetoCentroCusto();
		gerarArvoreCentrosCusto();
		limparCentrosInativos();

		return "/pages/financeiro/cadcentrocusto.jsf";
	}

	@Secured({ "ROLE_LISTAR_CENTRO", "ROLE_ROOT" })
	public String editarCentroCusto() {

		gerarArvoreCentrosCusto();
		limparCentroCustoOriginal();

		return "/pages/financeiro/cadcentrocusto.jsf";
	}

	private void desselecionarPai() {
		final List<TreeNode> nosPesquisa = new ArrayList<TreeNode>();
		if (null != this.treeRootCentrosCusto) {
			nosPesquisa.addAll(this.treeRootCentrosCusto.getChildren());
			while (true) {
				if (nosPesquisa.isEmpty()) {
					break;
				} else {
					final TreeNode noAtual = nosPesquisa.remove(0);
					noAtual.setSelected(false);
					nosPesquisa.addAll(noAtual.getChildren());
				}
			}
		}
	}

	private void limparCentrosInativos() {
		final List<TreeNode> nosPesquisa = new ArrayList<TreeNode>();
		nosPesquisa.addAll(this.treeRootCentrosCusto.getChildren());
		while (true) {
			if (nosPesquisa.isEmpty()) {
				break;
			} else {
				final TreeNode noAtual = nosPesquisa.remove(0);
				final CentroCustoDTO centroAtual = (CentroCustoDTO) noAtual
						.getData();
				if (!centroAtual.isAtivo() || centroAtual.isExcluido()) {
					noAtual.getParent().getChildren().remove(noAtual);
				}
				nosPesquisa.addAll(noAtual.getChildren());
			}
		}
	}

	/**
	 * Remove a entidade atual da lista de centros de custo. Seleciona o centro
	 * de custo pai (edicao)
	 */
	private void limparCentroCustoOriginal() {
		final List<TreeNode> nosPesquisa = new ArrayList<TreeNode>();
		nosPesquisa.addAll(this.treeRootCentrosCusto.getChildren());
		while (true) {
			if (nosPesquisa.isEmpty()) {
				break;
			} else {
				final TreeNode noAtual = nosPesquisa.remove(0);
				final CentroCustoDTO centroAtual = (CentroCustoDTO) noAtual
						.getData();
				if (centroAtual.getId().equals(this.entity.getId())) {
					noAtual.getParent().setSelected(true);
					noAtual.getParent().getChildren().remove(noAtual);
					break;
				} else {
					noAtual.setExpanded(true);
					nosPesquisa.addAll(noAtual.getChildren());
				}
			}
		}
	}

	/**
	 * @return the entities
	 */
	public List<CentroCustoDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<CentroCustoDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public CentroCustoDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final CentroCustoDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the treeRootCentrosCusto
	 */
	public TreeNode getTreeRootCentrosCusto() {
		return this.treeRootCentrosCusto;
	}

	/**
	 * @param treeRootCentrosCusto
	 *            the treeRootCentrosCusto to set
	 */
	public void setTreeRootCentrosCusto(final TreeNode treeRootCentrosCusto) {
		this.treeRootCentrosCusto = treeRootCentrosCusto;
	}

	/**
	 * @return the valueSelectStatus
	 */
	public String getValueSelectStatus() {
		return this.valueSelectStatus;
	}

	/**
	 * @param valueSelectStatus
	 *            the valueSelectStatus to set
	 */
	public void setValueSelectStatus(final String valueSelectStatus) {
		if ((valueSelectStatus != null) && !valueSelectStatus.equals("")) {
			this.valueSelectStatus = valueSelectStatus;
		}
	}

	/**
	 * @return the nodeCentroCustoSelecionado
	 */
	public TreeNode getNodeCentroCustoSelecionado() {
		return this.nodeCentroCustoSelecionado;
	}

	/**
	 * @param nodeCentroCustoSelecionado
	 *            the nodeCentroCustoSelecionado to set
	 */
	public void setNodeCentroCustoSelecionado(
			final TreeNode nodeCentroCustoSelecionado) {
		this.nodeCentroCustoSelecionado = nodeCentroCustoSelecionado;
	}

}