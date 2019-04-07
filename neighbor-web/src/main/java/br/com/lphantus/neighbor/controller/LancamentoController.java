package br.com.lphantus.neighbor.controller;

import static br.com.lphantus.neighbor.enums.ValueSelectStatus.ATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.INATIVOS;
import static br.com.lphantus.neighbor.enums.ValueSelectStatus.TODOS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
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
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.component.MoradorDataModel;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IMoradorService;
import br.com.lphantus.neighbor.service.IPessoaFisicaService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "lancamentoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private IPessoaFisicaService pessoaFisicaService;

	@Autowired
	private ICentroCustoService centroCustoService;

	@Autowired
	private IMoradorService moradorService;

	private List<LancamentoDTO> entities;
	private LancamentoDTO entity;

	private PessoaFisicaDTO pessoaSelecionada;

	private List<MoradorDTO> pessoasLista = new ArrayList<MoradorDTO>();
	private final MoradorDataModel pessoas = new MoradorDataModel();

	private String valueSelectStatus;

	private boolean todosMoradores = true;
	private String casas, blocos;

	private TreeNode treeRootCentrosCusto;
	private TreeNode nodeCentroCustoSelecionado;

	private boolean edicao;

	/**
	 * Construtor padrao
	 */
	public LancamentoController() {

	}

	@PostConstruct
	public void initialize() {
		this.valueSelectStatus = ATIVOS;
		this.entity = new LancamentoDTO();
		this.entities = new ArrayList<LancamentoDTO>();
	}

	public boolean isAlteracao() {
		return !((this.entity.getId() == null) || (this.entity.getId() == 0));
	}

	private void limparSelecao() {
		this.casas = "";
		this.blocos = "";
	}

	public void limparObjetoLancamento() {
		this.entity = new LancamentoDTO();
		this.entity.setDataCadastro(new Date());
		limparSelecao();
	}

	private void gerarListaParametrizada(final Boolean parametro) {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.lancamentoService.buscarPorCondominio(condominio, parametro);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista todos lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista todos lancamentos.", e);
		}
	}

	public void gerarListaLancamento() {
		Boolean parametro;
		if (this.valueSelectStatus.equals(ATIVOS)) {
			parametro = Boolean.TRUE;
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			parametro = Boolean.FALSE;
		} else {
			parametro = null;
		}
		gerarListaParametrizada(parametro);
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
			final List<CentroCustoDTO> lista = this.centroCustoService.findCentrosCustoRaizByCondominio(condominioUsuarioLogado());
			for (final CentroCustoDTO centro : lista) {
				if (centro.isLancavel()) {
					filhos.add(centro);
				}
			}
		} else {
			final CentroCustoDTO dto = (CentroCustoDTO) pai.getData();
			final List<CentroCustoDTO> lista = this.centroCustoService.findCentrosCustoFilhos(dto);
			for (final CentroCustoDTO centro : lista) {
				if (centro.isLancavel()) {
					filhos.add(centro);
				}
			}
		}

		for (final CentroCustoDTO filho : filhos) {
			final TreeNode treeFilho = new DefaultTreeNode(filho, pai);
			treeFilho.setExpanded(true);
			
			// edicao: carregar o centro de custo selecionado
			if ( edicao ){
				if ( entity != null && entity.getCentroCusto() != null ){
					if ( entity.getCentroCusto().equals(filho) ){
						treeFilho.setSelected(true);
						nodeCentroCustoSelecionado = treeFilho;
					}
				}
			}
			
			if (!pai.getChildren().contains(treeFilho)) {
				pai.getChildren().add(treeFilho);
			}
			gerarFilhos(treeFilho);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void ativarDesativar() {
		try {
			if (this.entity != null) {

				this.lancamentoService.alterarStatus(this.entity.getId(), !this.entity.isAtivo());

				gerarListaLancamento();

				JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao alterar status lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarLancamentoReceber() {
		try {

			if (null != this.nodeCentroCustoSelecionado) {
				final CentroCustoDTO centro = (CentroCustoDTO) this.nodeCentroCustoSelecionado.getData();
				this.entity.setCentroCusto(centro);
			}

			this.entity.setCondominio(condominioUsuarioLogado());

			final List<PessoaDTO> listaPessoas = obtemListaPessoasMoradores();

			this.lancamentoService.gravarLancamentoEntrada(this.entity, listaPessoas);

			gerarListaLancamento();

			registrarHistorico("Cadastro lancamento no valor de R$ " + this.entity.getValor() + " para as casas " + this.casas + " blocos " + this.blocos + ".");

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

			return "/pages/financeiro/listalancamento.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar lancamento.", e);
		}

		return "";
	}

	@SuppressWarnings("unchecked")
	private List<PessoaDTO> obtemListaPessoasMoradores() {
		final List<PessoaDTO> retorno = new ArrayList<PessoaDTO>();
		for (final MoradorDTO morador : (List<MoradorDTO>) this.pessoas.getWrappedData()) {
			retorno.add(morador.getPessoa());
		}
		return retorno;
	}

	@Secured({ "ROLE_EDITAR_LANCAMENTO", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {
			if (null != this.nodeCentroCustoSelecionado) {
				final CentroCustoDTO centro = (CentroCustoDTO) this.nodeCentroCustoSelecionado.getData();
				this.entity.setCentroCusto(centro);
			}
			
			final Lancamento entidade = LancamentoDTO.Builder.getInstance().createEntity(this.entity);
			this.lancamentoService.update(entidade);

			// retorna tela de listagem
			this.valueSelectStatus = TODOS;
			gerarListaLancamento();
			gerarArvoreCentrosCusto();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listalancamento.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
		}

		return null;
	}

	public void carregarListaMoradores() {
		try {
			if (null == this.pessoasLista) {
				this.pessoasLista = new ArrayList<MoradorDTO>();
			}

			this.pessoasLista.clear();

			this.pessoasLista.addAll(this.pessoaFisicaService.buscarResponsaveisFinanceiros(this.casas, this.blocos, condominioUsuarioLogado()));

			this.pessoas.setWrappedData(this.pessoasLista);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar responsaveis financeiros.", e);
		}
	}

	public void carregarListaMoradoresEvento() {
		carregarListaMoradores();
	}

	public void onRowSelect(final SelectEvent event) {
		final PessoaFisicaDTO selecionada = (PessoaFisicaDTO) event.getObject();
		getLogger().debug("Pessoa selecionada " + selecionada);
		this.entity.setPessoa(selecionada);
	}

	public void onRowUnselect(final UnselectEvent event) {
		this.entity.setPessoa(null);
	}

	public void setValueSelectStatus(final String selectStatusLancamento) {
		if ((selectStatusLancamento != null) && !selectStatusLancamento.equals("")) {
			this.valueSelectStatus = selectStatusLancamento;
		}
	}

	public String getValueSelectStatus() {
		return this.valueSelectStatus;
	}

	@Secured({ "ROLE_LISTAR_LANCAMENTO", "ROLE_ROOT" })
	public String pageListaLancamento() {
		this.edicao = false;
		
		this.valueSelectStatus = TODOS;
		gerarListaLancamento();
		gerarArvoreCentrosCusto();

		return "/pages/financeiro/listalancamento.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_LANCAMENTO", "ROLE_ROOT" })
	public String pageCadastroLancamento() {
		edicao = false;
		
		limparObjetoLancamento();
		this.pessoasLista = new ArrayList<MoradorDTO>();
		this.pessoas.setWrappedData(this.pessoasLista);
		gerarArvoreCentrosCusto();

		return "/pages/financeiro/cadlancamento.jsf";
	}

	@Secured({ "ROLE_EDITAR_LANCAMENTO", "ROLE_ROOT" })
	public String editarLancamento() {
		edicao = true;
		final ArrayList<MoradorDTO> listaUnica = new ArrayList<MoradorDTO>();
		
		try {
			listaUnica.add(this.moradorService.buscarPorPessoa(this.entity.getPessoa()));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar responsavel financeiro.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		this.pessoas.setWrappedData(listaUnica);
		return "/pages/financeiro/cadlancamento.jsf";
	}

	public boolean isExisteCentroCustoLancavel() {
		return this.centroCustoService.existeCentroCustoLancavel(condominioUsuarioLogado());
	}

	/**
	 * @return the entities
	 */
	public List<LancamentoDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<LancamentoDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the entity
	 */
	public LancamentoDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final LancamentoDTO entity) {
		this.entity = entity;
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
	public void setNodeCentroCustoSelecionado(final TreeNode nodeCentroCustoSelecionado) {
		this.nodeCentroCustoSelecionado = nodeCentroCustoSelecionado;
	}

}