package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

import br.com.lphantus.neighbor.common.CarteiraDTO;
import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;
import br.com.lphantus.neighbor.common.MovimentacaoDTO;
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.common.UsuarioDTO;
import br.com.lphantus.neighbor.component.DuplicataParcelaDataModel;
import br.com.lphantus.neighbor.entity.Movimentacao;
import br.com.lphantus.neighbor.entity.Rateio;
import br.com.lphantus.neighbor.service.ICarteiraService;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.IDuplicataParcelaService;
import br.com.lphantus.neighbor.service.IMovimentacaoService;
import br.com.lphantus.neighbor.service.IRateioService;
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
@ManagedBean(name = "movimentacaoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class MovimentacaoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IMovimentacaoService movimentacaoService;

	@Autowired
	private ICarteiraService carteiraService;

	@Autowired
	private IDuplicataParcelaService duplicataParcelaService;

	@Autowired
	private IRateioService rateioService;

	@Autowired
	private ICentroCustoService centroCustoService;

	private DuplicataDTO duplicataOrigem;

	private MovimentacaoDTO entity;
	private List<MovimentacaoDTO> entities;

	private List<CarteiraDTO> carteiras;
	private final DuplicataParcelaDataModel parcelaModelo = new DuplicataParcelaDataModel();
	private DuplicataParcelaDTO parcelaBaixa;

	private String tipoMovimento, tipoRateio;
	private RateioDTO rateio, rateioSelecionado;
	private List<RateioDTO> listaRateios;
	private BigDecimal percentual;

	private TreeNode treeRootCentrosCusto;
	private TreeNode nodeCentroCustoSelecionado;

	/**
	 * Construtor padrao
	 */
	public MovimentacaoController() {

	}

	private void gerarListaCarteiras() throws ServiceException {
		this.carteiras = this.carteiraService.buscarPorParametros(condominioUsuarioLogado(), Boolean.TRUE);
	}

	public void onRowSelect(final SelectEvent event) {
		final DuplicataParcelaDTO selecionada = (DuplicataParcelaDTO) event.getObject();

		if (null != selecionada) {
			this.entity.setValor(selecionada.getValor());
		}
	}

	public void onRowUnselect(final UnselectEvent event) {
		this.entity.setValor(this.duplicataOrigem.getFatura().getValor());
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
			if (!pai.getChildren().contains(treeFilho)) {
				pai.getChildren().add(treeFilho);
			}
			gerarFilhos(treeFilho);
		}

	}

	/**
	 * Metodo invocado na acao da baixa da carteira
	 * 
	 * @return Navegacao
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String paginaBaixa() {
		try {
			gerarListaCarteiras();

			this.parcelaModelo.setWrappedData(this.duplicataParcelaService.buscarPorDuplicata(this.duplicataOrigem));

			this.entity = new MovimentacaoDTO();
			this.entity.setValor(this.duplicataOrigem.getFatura().getValor());

			return "/pages/financeiro/baixaduplicata.jsf";
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao buscar iniciar baixa.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao buscar iniciar baixa.", e);
		}
		return "";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String finalizarBaixa() {
		try {

			if (null == this.parcelaBaixa) {
				throw new ServiceException(GerenciadorMensagem.getMensagem("SELECIONE_PARCELA_BAIXA"));
			}

			this.parcelaBaixa.setDuplicata(this.duplicataOrigem);
			this.movimentacaoService.finalizarBaixa(this.entity, this.parcelaBaixa);

			final DuplicataController duplicataController = JsfUtil.avaliarExpressao("#{duplicataController}", DuplicataController.class);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

			return duplicataController.pageListaDuplicata();
		} catch (final ServiceException e) {
			getLogger().debug("Erro ao finalizar baixa.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao finalizar baixa.", e);
		}
		return "";
	}

	private void gerarListaMovimentacao() {
		try {
			final UsuarioDTO user = usuarioLogado();
			if (usuarioLogado() != null) {
				CondominioDTO condominio;
				if (user.isRoot()) {
					condominio = null;
				} else {
					condominio = condominioUsuarioLogado();
				}
				this.entities = this.movimentacaoService.buscarPorCondominio(condominio);
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gerar lista todas movimentacoes.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar lista todas movimentacoes.", e);
		}
	}

	private void carregarListaRateios() {
		try {
			this.listaRateios = this.rateioService.buscarPorMovimentacao(this.entity);
		} catch (final ServiceException e) {
			getLogger().error("Erro ao buscar rateios para movimentacao.", e);
		}
	}

	public void limparObjetoMovimento() {
		this.entity = new MovimentacaoDTO();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String alterar() {
		try {

			final Movimentacao entidade = MovimentacaoDTO.Builder.getInstance().createEntity(this.entity);
			this.movimentacaoService.update(entidade);

			salvarRateios(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("ALTER_OK"));

			return pageListaMovimentacao();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao atualizar movimento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao atualizar movimento.", e);
		}

		return null;
	}

	private void salvarRateios(final Movimentacao entidade) throws ServiceException {
		if ((this.listaRateios != null) && !this.listaRateios.isEmpty()) {
			for (final RateioDTO rateioItem : this.listaRateios) {
				final Rateio rateioItemEntidade = RateioDTO.Builder.getInstance().createEntity(rateioItem);
				if ((rateioItem.getId() == null) || rateioItem.getId().equals(BigDecimal.ZERO.intValue())) {
					rateioItemEntidade.setMovimentacao(entidade);
					this.rateioService.save(rateioItemEntidade);
				} else {
					rateioItemEntidade.setMovimentacao(entidade);
					this.rateioService.update(rateioItemEntidade);
				}
			}
		}
	}

	public void geraRateio() {
		try {

			if (this.percentual != null) {
				// percentual dividido por
				this.rateio.setValor(this.entity.getValor().multiply(this.percentual.divide(new BigDecimal("100"))));
			}

			final BigDecimal somaAtual = somaValores();
			if (this.rateio.getValor().add(somaAtual).compareTo(this.entity.getValor()) > 0) {
				throw new ServiceException("Valor excede total da movimentação.");
			}

			this.rateio.setCondominio(condominioUsuarioLogado());

			this.listaRateios.add(this.rateio);
			final CentroCustoDTO centro = (CentroCustoDTO) this.nodeCentroCustoSelecionado.getData();
			this.rateio.setCentroCusto(centro);
			this.rateio = new RateioDTO();
			this.nodeCentroCustoSelecionado = null;
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gerar rateio.", e);
		}
	}

	private BigDecimal somaValores() {
		BigDecimal retorno = BigDecimal.ZERO;
		for (final RateioDTO item : this.listaRateios) {
			retorno = retorno.add(item.getValor());
		}
		return retorno;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String gravarMovimento() {
		try {

			final Movimentacao entidade = MovimentacaoDTO.Builder.getInstance().createEntity(this.entity);
			this.movimentacaoService.save(entidade);

			salvarRateios(entidade);

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("SAVE_OK"));

			return pageListaMovimentacao();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar lancamento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar lancamento.", e);
		}

		return "";
	}

	@Secured({ "ROLE_ADICIONAR_MOVIMENTACAO", "ROLE_ROOT" })
	public String paginaCadastro() {
		try {
			limparObjetoMovimento();
			gerarListaCarteiras();
			this.rateio = new RateioDTO();
			this.listaRateios = new ArrayList<RateioDTO>();
			gerarArvoreCentrosCusto();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao iniciar pagina de cadastro.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao iniciar pagina de cadastro.", e);
		}
		return "/pages/financeiro/cadmovimentacao.jsf";
	}

	@Secured({ "ROLE_EDITAR_MOVIMENTACAO", "ROLE_ROOT" })
	public String editarMovimentacao() {
		try {
			gerarListaCarteiras();
		} catch (final ServiceException e) {
			getLogger().error("Erro ao iniciar pagina de cadastro.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao iniciar pagina de cadastro.", e);
		}
		carregarListaRateios();
		this.rateio = new RateioDTO();
		if (this.entity.getValor().compareTo(BigDecimal.ZERO) >= 0) {
			this.tipoMovimento = "IN";
		} else {
			this.tipoMovimento = "OUT";
		}
		return "/pages/financeiro/cadmovimentacao.jsf";
	}

	@Secured({ "ROLE_LISTAR_MOVIMENTACAO", "ROLE_ROOT" })
	public String pageListaMovimentacao() {
		gerarListaMovimentacao();
		return "/pages/financeiro/listamovimentacao.jsf";
	}

	public boolean isAlteracao() {
		return !((this.entity.getId() == null) || (this.entity.getId() == 0));
	}

	/**
	 * @return the duplicataOrigem
	 */
	public DuplicataDTO getDuplicataOrigem() {
		return this.duplicataOrigem;
	}

	/**
	 * @param duplicataOrigem
	 *            the duplicataOrigem to set
	 */
	public void setDuplicataOrigem(final DuplicataDTO duplicataOrigem) {
		this.duplicataOrigem = duplicataOrigem;
	}

	/**
	 * @return the entity
	 */
	public MovimentacaoDTO getEntity() {
		return this.entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(final MovimentacaoDTO entity) {
		this.entity = entity;
	}

	/**
	 * @return the entities
	 */
	public List<MovimentacaoDTO> getEntities() {
		return this.entities;
	}

	/**
	 * @param entities
	 *            the entities to set
	 */
	public void setEntities(final List<MovimentacaoDTO> entities) {
		this.entities = entities;
	}

	/**
	 * @return the carteiras
	 */
	public List<CarteiraDTO> getCarteiras() {
		return this.carteiras;
	}

	/**
	 * @param carteiras
	 *            the carteiras to set
	 */
	public void setCarteiras(final List<CarteiraDTO> carteiras) {
		this.carteiras = carteiras;
	}

	/**
	 * @return the parcelaModelo
	 */
	public DuplicataParcelaDataModel getParcelaModelo() {
		return this.parcelaModelo;
	}

	/**
	 * @return the parcelaBaixa
	 */
	public DuplicataParcelaDTO getParcelaBaixa() {
		return this.parcelaBaixa;
	}

	/**
	 * @param parcelaBaixa
	 *            the parcelaBaixa to set
	 */
	public void setParcelaBaixa(final DuplicataParcelaDTO parcelaBaixa) {
		this.parcelaBaixa = parcelaBaixa;
	}

	/**
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		return this.tipoMovimento;
	}

	/**
	 * @param tipoMovimento
	 *            the tipoMovimento to set
	 */
	public void setTipoMovimento(final String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the rateio
	 */
	public RateioDTO getRateio() {
		return this.rateio;
	}

	/**
	 * @param rateio
	 *            the rateio to set
	 */
	public void setRateio(final RateioDTO rateio) {
		this.rateio = rateio;
	}

	/**
	 * @return the listaRateios
	 */
	public List<RateioDTO> getListaRateios() {
		return this.listaRateios;
	}

	/**
	 * @return the rateioSelecionado
	 */
	public RateioDTO getRateioSelecionado() {
		return this.rateioSelecionado;
	}

	/**
	 * @param rateioSelecionado
	 *            the rateioSelecionado to set
	 */
	public void setRateioSelecionado(final RateioDTO rateioSelecionado) {
		this.rateioSelecionado = rateioSelecionado;
	}

	/**
	 * @return the tipoRateio
	 */
	public String getTipoRateio() {
		return this.tipoRateio;
	}

	/**
	 * @param tipoRateio
	 *            the tipoRateio to set
	 */
	public void setTipoRateio(final String tipoRateio) {
		this.tipoRateio = tipoRateio;
	}

	/**
	 * @return the percentual
	 */
	public BigDecimal getPercentual() {
		return this.percentual;
	}

	/**
	 * @param percentual
	 *            the percentual to set
	 */
	public void setPercentual(final BigDecimal percentual) {
		this.percentual = percentual;
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

	/**
	 * @param listaRateios
	 *            the listaRateios to set
	 */
	public void setListaRateios(final List<RateioDTO> listaRateios) {
		this.listaRateios = listaRateios;
	}

}
