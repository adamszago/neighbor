package br.com.lphantus.neighbor.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.com.lphantus.neighbor.common.RateioDTO;
import br.com.lphantus.neighbor.service.ICentroCustoService;
import br.com.lphantus.neighbor.service.IRateioService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "balanceteController")
@Transactional(propagation = Propagation.SUPPORTS)
public class BalanceteController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IRateioService rateioService;

	@Autowired
	private ICentroCustoService centroCustoService;

	private List<RateioDTO> listaRateios;
	private TreeNode treeRoot;
	private Date selectedDate;

	// private final Set<TreeNode> aRemover = new HashSet<TreeNode>();

	/**
	 * Construtor padrao
	 */
	public BalanceteController() {

	}

	public void decrementMonth() throws Exception {
		final Calendar c = Calendar.getInstance();
		c.setTime(this.selectedDate);
		c.add(Calendar.MONTH, -1);

		this.selectedDate = c.getTime();
		popularListaRateios();
		gerarArvoreBalancete();
	}

	public void incrementMonth() throws Exception {
		final Calendar c = Calendar.getInstance();
		c.setTime(this.selectedDate);
		c.add(Calendar.MONTH, 1);

		this.selectedDate = c.getTime();
		popularListaRateios();
		gerarArvoreBalancete();
	}

	@Secured({ "ROLE_BALANCETE", "ROLE_ROOT" })
	public String pageBalancete() {

		try {
			this.selectedDate = new Date();
			popularListaRateios();
			gerarArvoreBalancete();
			// removerSelecionados();

		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gerar balancete mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gerar balancete mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return "/pages/financeiro/balancete.jsf";
	}

	private void popularListaRateios() throws ServiceException {
		this.listaRateios = new ArrayList<RateioDTO>();
		this.listaRateios.addAll(this.rateioService.buscarPorMesAtual(
				condominioUsuarioLogado(), this.selectedDate));
	}

	private void gerarArvoreBalancete() {
		try {
			this.treeRoot = new DefaultTreeNode("Root", null);
			final TreeNode treeDespesa = criarArvore("Despesas");
			final TreeNode treeReceita = criarArvore("Receitas");

			final Map<CentroCustoDTO, List<RateioDTO>> mapaEntrada, mapaSaida;
			mapaEntrada = new HashMap<CentroCustoDTO, List<RateioDTO>>();
			mapaSaida = new HashMap<CentroCustoDTO, List<RateioDTO>>();
			popularMapas(mapaEntrada, mapaSaida);

			gerarFilhos(treeDespesa, treeDespesa);
			gerarFilhos(treeReceita, treeReceita);

			popularSomas(treeDespesa, mapaSaida);
			popularSomas(treeReceita, mapaEntrada);

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private TreeNode criarArvore(final String nomeArvore) {
		final CentroCustoDTO centro = new CentroCustoDTO();
		centro.setNome(nomeArvore);
		final RateioDTO rateio = new RateioDTO();
		rateio.setCentroCusto(centro);
		rateio.setValor(BigDecimal.ZERO);
		final TreeNode retorno = new DefaultTreeNode(rateio, this.treeRoot);
		retorno.setExpanded(true);
		retorno.setSelectable(false);
		retorno.setSelected(false);
		return retorno;
	}

	private void gerarFilhos(final TreeNode pai, final TreeNode raiz)
			throws ServiceException {
		final List<CentroCustoDTO> filhos = new ArrayList<CentroCustoDTO>();
		if (pai == raiz) {
			filhos.addAll(this.centroCustoService
					.findCentrosCustoRaizByCondominio(condominioUsuarioLogado()));
		} else {
			final RateioDTO dto = (RateioDTO) pai.getData();
			filhos.addAll(this.centroCustoService.findCentrosCustoFilhos(dto
					.getCentroCusto()));
		}

		for (final CentroCustoDTO filho : filhos) {
			final RateioDTO rateio = new RateioDTO();
			rateio.setCentroCusto(filho);
			rateio.setValor(BigDecimal.ZERO);

			final TreeNode treeFilho = new DefaultTreeNode(rateio, pai);
			treeFilho.setExpanded(true);
			treeFilho.setSelectable(false);
			treeFilho.setSelected(false);

			gerarFilhos(treeFilho, raiz);
		}

	}

	private void popularMapas(
			final Map<CentroCustoDTO, List<RateioDTO>> mapaEntrada,
			final Map<CentroCustoDTO, List<RateioDTO>> mapaSaida) {
		for (final RateioDTO registro : this.listaRateios) {
			Map<CentroCustoDTO, List<RateioDTO>> mapaColocar;
			if (BigDecimal.ZERO.compareTo(registro.getValor()) < 0) {
				// se eh entrada
				mapaColocar = mapaEntrada;
			} else {
				// se eh saida
				mapaColocar = mapaSaida;
			}

			if (mapaColocar.containsKey(registro.getCentroCusto())) {
				mapaColocar.get(registro.getCentroCusto()).add(registro);
			} else {
				mapaColocar.put(
						registro.getCentroCusto(),
						new ArrayList<RateioDTO>(Arrays
								.asList(new RateioDTO[] { registro })));
			}
		}
	}

	private void popularSomas(final TreeNode nodo,
			final Map<CentroCustoDTO, List<RateioDTO>> mapaValores) {

		if ((nodo == null) || (nodo == this.treeRoot)) {
			return;
		}

		if (nodo.getChildren().isEmpty()) {

			// caso base
			TreeNode nodoPesquisa = nodo;
			while (true) {
				if ((nodoPesquisa == null)
						|| (nodoPesquisa.getParent() == this.treeRoot)
						|| (nodoPesquisa.getParent() == null)) {
					// parar a recursao em "despesa" e "receita"
					break;
				} else {

					final TreeNode paiAtual = nodoPesquisa.getParent();

					final RateioDTO rateio = (RateioDTO) nodoPesquisa.getData();
					final CentroCustoDTO centroCusto = rateio.getCentroCusto();

					if (mapaValores.containsKey(centroCusto)) {
						for (final RateioDTO rateioObtidoNaBase : mapaValores
								.get(centroCusto)) {
							rateio.setValor(rateio.getValor().abs()
									.add(rateioObtidoNaBase.getValor().abs()));
						}
					}

					final RateioDTO rateioPai = (RateioDTO) paiAtual.getData();
					rateioPai.setValor(rateioPai.getValor().abs()
							.add(rateio.getValor()));

					// deixar somente os centros de custo para os quais
					// houve entrada/saida de valores
					// if (rateio.getValor().equals(BigDecimal.ZERO)) {
					// this.aRemover.add(nodoPesquisa);
					// }

					nodoPesquisa = paiAtual;
				}
			}
		} else {

			final List<TreeNode> listaFilhos = new ArrayList<TreeNode>(
					nodo.getChildren());

			int i = 0;
			while (true) {
				if (i >= listaFilhos.size()) {
					break;
				} else {
					final TreeNode filho = listaFilhos.get(i);
					popularSomas(filho, mapaValores);
					i = i + 1;
				}
			}
		}
	}

	// GETTERS E SETTERS

	/**
	 * @return the selectedDate
	 */
	public Date getSelectedDate() {
		return this.selectedDate;
	}

	/**
	 * @param selectedDate
	 *            the selectedDate to set
	 */
	public void setSelectedDate(final Date selectedDate) {
		this.selectedDate = selectedDate;
	}

	/**
	 * @return the treeRoot
	 */
	public TreeNode getTreeRoot() {
		return this.treeRoot;
	}

	/**
	 * @param treeRoot
	 *            the treeRoot to set
	 */
	public void setTreeRoot(final TreeNode treeRoot) {
		this.treeRoot = treeRoot;
	}

}