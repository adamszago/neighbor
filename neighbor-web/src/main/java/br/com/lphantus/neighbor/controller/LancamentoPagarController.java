package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.LancamentoDTO;
import br.com.lphantus.neighbor.common.PessoaDTO;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.component.PrestadorServicoDataModel;
import br.com.lphantus.neighbor.entity.Lancamento;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.ILancamentoService;
import br.com.lphantus.neighbor.service.IPrestadorServicoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "lancamentoPagarController")
@Transactional(propagation = Propagation.SUPPORTS)
public class LancamentoPagarController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String TODOS = "TODOS";
	private final static String ATIVOS = "ATIVOS";
	private final static String INATIVOS = "INATIVOS";

	@Autowired
	private ILancamentoService lancamentoService;

	@Autowired
	private ICentroCustoService centroCustoService;

	@Autowired
	private IPrestadorServicoService prestadorServicoService;

	@Autowired
	private ICarteiraService carteiraService;

	private List<LancamentoDTO> entities;
	private LancamentoDTO entity;
	private boolean edicao;

	private List<PrestadorServicoDTO> pessoasSelecionadas;

	private List<PrestadorServicoDTO> pessoasLista = new ArrayList<PrestadorServicoDTO>();
	private List<CarteiraDTO> carteiras;
	private final PrestadorServicoDataModel pessoas = new PrestadorServicoDataModel();

	private String valueSelectStatus;
	private String emptyMessageLista;

	private TreeNode treeRootCentrosCusto;
	private TreeNode nodeCentroCustoSelecionado;

	/**
	 * Construtor padrao
	 */
	public LancamentoPagarController() {

	}

	@PostConstruct
	public void initialize() {
		this.valueSelectStatus = ATIVOS;
		this.entities = new ArrayList<LancamentoDTO>();
	}

	private void limparSelecao() {
	}

	public void limparObjetoLancamento() {
		this.entity = new LancamentoDTO();
		this.entity.setDataCadastro(new Date());
		limparSelecao();
	}

	private void gerarListaLancamentoAtivo() {
		try {
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (usuarioLogado().isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.lancamentoService.buscarPorCondominioPagar(condominio, Boolean.TRUE);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista lancamentos ativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de lancamentos ativos.", e);
		}
	}

	private void gerarListaLancamentoInativo() {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				final CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.lancamentoService.buscarPorCondominioPagar(condominio, Boolean.FALSE);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista lancamentos inativos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista de lancamentos inativos.", e);
		}
	}

	public boolean isExisteCentroCustoLancavel() {
		return this.centroCustoService.existeCentroCustoLancavel(condominioUsuarioLogado());
	}

	private void gerarListaTodosLancamento() {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.lancamentoService.buscarPorCondominioPagar(condominio, null);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista todos lancamentos.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista todos lancamentos.", e);
		}
	}

	private void gerarListaCarteiras() {
		try {
			this.carteiras = this.carteiraService.buscarPorParametros(condominioUsuarioLogado(), Boolean.TRUE);
		} catch (ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	public void gerarListaLancamento() {
		if (this.valueSelectStatus.equals(TODOS)) {
			gerarListaTodosLancamento();
		} else if (this.valueSelectStatus.equals(ATIVOS)) {
			gerarListaLancamentoAtivo();
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			gerarListaLancamentoInativo();
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

			// se for edicao, seleciona o centro de custo da entidade
			if (edicao) {
				if (entity != null && entity.getCentroCusto() != null) {
					if (filho.equals(entity.getCentroCusto())) {
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

	public String getEmptyMessageLista() {
		if (this.valueSelectStatus.equals(ATIVOS)) {
			this.emptyMessageLista = "Sem lançamentos ativos para exibir";
		} else if (this.valueSelectStatus.equals(INATIVOS)) {
			this.emptyMessageLista = "Sem lançamentos inativos para exibir";
		} else if (this.valueSelectStatus.equals(TODOS)) {
			this.emptyMessageLista = "Sem lançamentos cadastrados para exibir";
		}
		return this.emptyMessageLista;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarLancamentoPagar() {
		try {

			if (null != this.nodeCentroCustoSelecionado) {
				final CentroCustoDTO centro = (CentroCustoDTO) this.nodeCentroCustoSelecionado.getData();
				this.entity.setCentroCusto(centro);
			}

			List<PrestadorServicoDTO> prestadores = new ArrayList<PrestadorServicoDTO>(pessoasSelecionadas);
			List<PessoaDTO> pessoas = new ArrayList<PessoaDTO>();
			for (PrestadorServicoDTO prestador : prestadores) {
				pessoas.add(prestador.getPessoa());
			}

			this.entity.setCondominio(condominioUsuarioLogado());
			this.lancamentoService.gravarLancamentoSaida(this.entity, pessoas);

			gerarListaLancamento();

			registrarHistorico(String.format("Cadastro lancamento a pagar no valor de R$ %s para as pessoas %s.",
					this.entity.getValor(), obterNomesPessoas(pessoas)));

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

			return "/pages/financeiro/listalancamentopagar.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar lancamento.", e);
		}

		return "";
	}

	private String obterNomesPessoas(List<PessoaDTO> pessoas) {
		final StringBuilder sBuilder = new StringBuilder("[");
		for (int i = 0; i < pessoas.size(); ++i) {
			final PessoaDTO atual = pessoas.get(i);
			if (i == 0) {
				sBuilder.append(String.format("\"%s\"", atual.getNome()));
			} else {
				sBuilder.append(String.format(",\"%s\"", atual.getNome()));
			}
		}
		return sBuilder.append("]").toString();
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

			this.valueSelectStatus = TODOS;
			gerarListaLancamento();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			return "/pages/financeiro/listalancamentopagar.jsf";
		} catch (final ServiceException e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao atualizar lancamento.", e);
		}
		return "";
	}

	public void carregarListaPrestadores() {
		try {
			if (null == this.pessoasLista) {
				this.pessoasLista = new ArrayList<PrestadorServicoDTO>();
			}

			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}

			this.pessoasLista.clear();
			this.pessoasLista.addAll(this.prestadorServicoService.buscarPorCondominio(condominio));

			this.pessoas.setWrappedData(this.pessoasLista);
			
			this.pessoasSelecionadas = new ArrayList<PrestadorServicoDTO>(pessoasLista);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar prestadores de servico.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao buscar prestadores de servico.", e);
		}
	}

	public void carregarListaPrestadoresEvento(final AjaxBehaviorEvent event) {
		carregarListaPrestadores();
	}

	public void onRowSelect(final SelectEvent event) {
		final PessoaFisicaDTO selecionada = (PessoaFisicaDTO) event.getObject();
		getLogger().debug("Pessoa selecionada " + selecionada);
		this.entity.setPessoa(selecionada);
	}

	public void onRowUnselect(final UnselectEvent event) {
		this.entity.setPessoa(null);
	}

	public void onTableFilter(final FilterEvent event) {
		// TODO: colocar os dados filtrados na tabela
		getLogger().info("Pessoas " + event.getData());
		getLogger().info("Mapa " + event.getFilters());
		// pessoas.setWrappedData(event.getData());
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
		edicao = false;

		this.valueSelectStatus = TODOS;
		gerarListaLancamento();

		return "/pages/financeiro/listalancamentopagar.jsf";
	}

	@Secured({ "ROLE_ADICIONAR_LANCAMENTO", "ROLE_ROOT" })
	public String pageCadastroLancamento() {
		edicao = false;

		limparObjetoLancamento();
		carregarListaPrestadores();
		gerarArvoreCentrosCusto();
		gerarListaCarteiras();

		return "/pages/financeiro/cadlancamentopagar.jsf";
	}

	@Secured({ "ROLE_EDITAR_LANCAMENTO", "ROLE_ROOT" })
	public String editarLancamento() {

		try {
			final List<PrestadorServicoDTO> listaUnica = new ArrayList<PrestadorServicoDTO>();

			PrestadorServicoDTO example = new PrestadorServicoDTO();
			example.setPessoa(entity.getPessoa());
			PrestadorServicoDTO prestador = prestadorServicoService.buscarPorId(example);

			this.edicao = true;

			listaUnica.add(prestador);

			this.pessoas.setWrappedData(listaUnica);

			gerarArvoreCentrosCusto();
		} catch (ServiceException e) {
			getLogger().error("Erro ao buscar prestador de servico do lancamento a pagar.", e);
		}
		return "/pages/financeiro/cadlancamentopagar.jsf";
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
	 * @return the carteiras
	 */
	public List<CarteiraDTO> getCarteiras() {
		return carteiras;
	}

	/**
	 * @param carteiras
	 *            the carteiras to set
	 */
	public void setCarteiras(List<CarteiraDTO> carteiras) {
		this.carteiras = carteiras;
	}


	/**
	 * @return the pessoasSelecionadas
	 */
	public List<PrestadorServicoDTO> getPessoasSelecionadas() {
		return pessoasSelecionadas;
	}

	/**
	 * @param pessoasSelecionadas the pessoasSelecionadas to set
	 */
	public void setPessoasSelecionadas(List<PrestadorServicoDTO> pessoasSelecionadas) {
		this.pessoasSelecionadas = pessoasSelecionadas;
	}

	/**
	 * @return the pessoas
	 */
	public PrestadorServicoDataModel getPessoas() {
		return this.pessoas;
	}

	public List<PrestadorServicoDTO> getPessoasLista() {
		return this.pessoasLista;
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