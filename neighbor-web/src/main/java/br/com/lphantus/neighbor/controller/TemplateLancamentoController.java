package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

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
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.component.MoradorDataModel;
import br.com.lphantus.neighbor.entity.TemplateLancamento;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.ILancamentoTipoService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.ITemplateLancamentoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

/**
 * 
 * @author elias
 * 
 */
@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "templateLancamentoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class TemplateLancamentoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String TODOS = "TODOS";
	private final static String ATIVOS = "ATIVOS";
	private final static String INATIVOS = "INATIVOS";

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	@Autowired
	private ITemplateLancamentoService templateLancamentoService;

	@Autowired
	private ILancamentoTipoService lancamentoTipoService;

	@Autowired
	private ICentroCustoService centroCustoService;

	private TemplateLancamentoDTO template;
	private List<TemplateLancamentoDTO> templates;

	private PessoaFisicaDTO pessoaSelecionada;

	private List<MoradorDTO> pessoasLista = new ArrayList<MoradorDTO>();
	private final MoradorDataModel pessoas = new MoradorDataModel();

	private String valueSelectStatus;
	private String emptyMessageLista;

	private boolean todosMoradores = true;
	private String casas, blocos;

	private TreeNode treeRootCentrosCusto;
	private TreeNode nodeCentroCustoSelecionado;

	/**
	 * Construtor padrao
	 */
	public TemplateLancamentoController() {

	}

	@PostConstruct
	public void initialize() {
		this.valueSelectStatus = ATIVOS;
		this.templates = new ArrayList<TemplateLancamentoDTO>();
	}

	private void limparSelecao() {
		this.casas = "";
		this.blocos = "";
	}

	public void limparObjetoTemplate() {
		this.template = new TemplateLancamentoDTO();
		this.template.setDataCadastro(new Date());
		limparSelecao();
	}

	public void gerarListaTemplate() {
		if (this.valueSelectStatus.equals(TODOS)) {
			gerarListaTodosTemplates();
		} else if (this.valueSelectStatus.equals(ATIVOS)) {
			gerarListaTemplatesAtivo();
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			gerarListaTemplatesInativo();
		}
	}

	private void gerarListaTemplatesInativo() {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.templates = this.templateLancamentoService
						.buscarInativosCondominio(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista templates inativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de templates inativos.", e);
		}
	}

	private void gerarListaTemplatesAtivo() {
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.templates = this.templateLancamentoService
						.buscarAtivosCondominio(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista templates ativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de templates ativos.", e);
		}
	}

	private void gerarListaTodosTemplates() {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.templates = this.templateLancamentoService
						.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista todos templates.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista todos templates.", e);
		}
	}

	private void gerarArvoreCentrosCusto() {
		try {
			this.treeRootCentrosCusto = new DefaultTreeNode("Root", null);
			gerarFilhos(this.treeRootCentrosCusto);
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void gerarFilhos(final TreeNode pai) throws ServiceException {
		final List<CentroCustoDTO> filhos = new ArrayList<CentroCustoDTO>();
		if (pai == this.treeRootCentrosCusto) {
			final List<CentroCustoDTO> lista = this.centroCustoService
					.findCentrosCustoRaizByCondominio(condominioUsuarioLogado());
			for (final CentroCustoDTO centro : lista) {
				if (centro.isLancavel()) {
					filhos.add(centro);
				}
			}
		} else {
			final CentroCustoDTO dto = (CentroCustoDTO) pai.getData();
			final List<CentroCustoDTO> lista = this.centroCustoService
					.findCentrosCustoFilhos(dto);
			for (final CentroCustoDTO centro : lista) {
				if (centro.isLancavel()) {
					filhos.add(centro);
				}
			}
		}

		for (final CentroCustoDTO filho : filhos) {
			final TreeNode treeFilho = new DefaultTreeNode(filho, pai);
			if (!pai.getChildren().contains(treeFilho)) {
				pai.getChildren().add(treeFilho);
			}
			gerarFilhos(treeFilho);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ativarDesativar() {
		try {
			if (this.template != null) {

				this.templateLancamentoService.alterarStatus(
						this.template.getId(), !this.template.isAtivo());

				gerarListaTemplate();

				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar status lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public String getEmptyMessageLista() {
		if (this.valueSelectStatus.equals(ATIVOS)) {
			this.emptyMessageLista = "Sem templates ativos para exibir";
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			this.emptyMessageLista = "Sem templates inativos para exibir";
		} else if (this.valueSelectStatus.equals(TODOS)) {
			this.emptyMessageLista = "Sem templates cadastrados para exibir";
		}
		return this.emptyMessageLista;
	}

	@Secured({ "ROLE_ADICIONAR_AUTOMATICO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarTemplateReceber() {
		try {

			this.template.setCondominio(condominioUsuarioLogado());
			this.template.setTipoLancamento(this.lancamentoTipoService
					.findById(1L).createDto());

			this.template.setCasas(this.casas);
			this.template.setBlocos(this.blocos);
			final TemplateLancamento entidade = TemplateLancamentoDTO.Builder
					.getInstance().createEntity(this.template);

			this.templateLancamentoService.save(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("SAVE_OK"));

			return "/pages/financeiro/listatemplatelancamento.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar template.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar template.", e);
		}

		return "";
	}

	@Secured({ "ROLE_EDITAR_AUTOMATICO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {

			final TemplateLancamento entidade = TemplateLancamentoDTO.Builder
					.getInstance().createEntity(this.template);

			this.templateLancamentoService.update(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listatemplatelancamento.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao editar template.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao editar template.", e);
		}

		return null;
	}

	public void carregarListaMoradores() {
		try {
			if (null == this.pessoasLista) {
				this.pessoasLista = new ArrayList<MoradorDTO>();
			}

			this.pessoasLista.clear();
			this.pessoasLista.addAll(this.pessoaFisicaService
					.buscarResponsaveisFinanceiros(this.casas, this.blocos,
							condominioUsuarioLogado()));

			this.pessoas.setWrappedData(this.pessoasLista);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
		}
	}

	public void carregarListaMoradoresEvento(final AjaxBehaviorEvent event) {
		carregarListaMoradores();
	}

	public void setValueSelectStatus(final String selectStatusLancamento) {
		if ((selectStatusLancamento != null)
				&& !selectStatusLancamento.equals("")) {
			this.valueSelectStatus = selectStatusLancamento;
		}
	}

	public String getValueSelectStatus() {
		return this.valueSelectStatus;
	}

	@Secured({ "ROLE_LISTAR_AUTOMATICO", "ROLE_ROOT" })
	public String pageListaAutomatico() {

		this.valueSelectStatus = TODOS;
		gerarListaTemplate();
		gerarArvoreCentrosCusto();

		return "/pages/financeiro/listatemplatelancamento.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_AUTOMATICO", "ROLE_ROOT" })
	public String pageCadastroAutomatico() {
		limparObjetoTemplate();
		carregarListaMoradores();
		gerarArvoreCentrosCusto();

		return "/pages/financeiro/cadtemplatelancamento.jsf";
	}

	@Secured({ "ROLE_EDITAR_AUTOMATICO", "ROLE_ROOT" })
	public String editarTemplateLancamento() {

		return "/pages/financeiro/cadtemplatelancamento.jsf";
	}

	public boolean isExisteCentroCustoLancavel() {
		return this.centroCustoService
				.existeCentroCustoLancavel(condominioUsuarioLogado());
	}

	/**
	 * @return the templates
	 */
	public List<TemplateLancamentoDTO> getTemplates() {
		return this.templates;
	}

	/**
	 * @param templates
	 *            the templates to set
	 */
	public void setTemplates(final List<TemplateLancamentoDTO> templates) {
		this.templates = templates;
	}

	/**
	 * @return the template
	 */
	public TemplateLancamentoDTO getTemplate() {
		return this.template;
	}

	/**
	 * @param template
	 *            the template to set
	 */
	public void setTemplate(final TemplateLancamentoDTO template) {
		this.template = template;
	}

	/**
	 * @return the pessoaSelecionada
	 */
	public PessoaFisicaDTO getPessoaSelecionada() {
		return this.pessoaSelecionada;
	}

	/**
	 * @param pessoaSelecionada
	 *            the pessoaSelecionada to set
	 */
	public void setPessoaSelecionada(final PessoaFisicaDTO pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	/**
	 * @return the pessoas
	 */
	public MoradorDataModel getPessoas() {
		return this.pessoas;
	}

	public List<MoradorDTO> getPessoasLista() {
		return this.pessoasLista;
	}

	/**
	 * @return the todosMoradores
	 */
	public boolean isTodosMoradores() {
		return this.todosMoradores;
	}

	/**
	 * @param todosMoradores
	 *            the todosMoradores to set
	 */
	public void setTodosMoradores(final boolean todosMoradores) {
		this.todosMoradores = todosMoradores;
	}

	/**
	 * @return the casas
	 */
	public String getCasas() {
		return this.casas;
	}

	/**
	 * @param casas
	 *            the casas to set
	 */
	public void setCasas(final String casas) {
		this.casas = casas;
	}

	/**
	 * @return the blocos
	 */
	public String getBlocos() {
		return this.blocos;
	}

	/**
	 * @param blocos
	 *            the blocos to set
	 */
	public void setBlocos(final String blocos) {
		this.blocos = blocos;
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