package br.com.lphantus.neighbor.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.UtilizacaoDTO;
import br.com.lphantus.neighbor.service.ICondominioService;
import br.com.lphantus.neighbor.service.IHistoricoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "utilizacaoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class UtilizacaoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IHistoricoService historicoService;

	@Autowired
	private ICondominioService condominioService;

	private Date selectedDate = new Date();
	private CartesianChartModel animatedModel = new CartesianChartModel();
	private Long maxY = 0L;

	/**
	 * Construtor padrao
	 */
	public UtilizacaoController() {

	}

	public void decrementMonth() throws Exception {
		final Calendar c = Calendar.getInstance();
		c.setTime(this.selectedDate);
		c.add(Calendar.MONTH, -1);

		this.selectedDate = c.getTime();

		popularListaCondominios();
		gerarDadosUtilizacao();
	}

	public void incrementMonth() throws Exception {
		final Calendar c = Calendar.getInstance();
		c.setTime(this.selectedDate);
		c.add(Calendar.MONTH, 1);

		this.selectedDate = c.getTime();

		popularListaCondominios();
		gerarDadosUtilizacao();
	}

	@Secured({ "ROLE_UTILIZACAO", "ROLE_ROOT" })
	public String pageUtilizacao() {

		try {
			this.selectedDate = new Date();

			popularListaCondominios();
			gerarDadosUtilizacao();
			// removerSelecionados();

		} catch (final ServiceException e) {
			getLogger().debug("Erro ao gerar dados de utilizacao mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().debug("Erro ao gerar dados de utilizacao mensal.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return "/pages/relatorios/utilizacao.jsf";
	}

	private void popularListaCondominios() throws ServiceException {
	}

	private void gerarDadosUtilizacao() {
		try {

			if (null == selectedDate) {
				selectedDate = new Date();
			}
			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}
			List<UtilizacaoDTO> utilizacao = historicoService.consultaUtilizacao(selectedDate, condominio);
			constroiGrafico(utilizacao);

		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private void constroiGrafico(List<UtilizacaoDTO> listaUtilizacao) {
		animatedModel = initLinearModel(listaUtilizacao);
//		animatedModel.setTitle("Line Chart");
//		animatedModel.setAnimate(true);
//		animatedModel.setLegendPosition("se");

	}

	private CartesianChartModel initLinearModel(List<UtilizacaoDTO> listaUtilizacao) {
//		Axis yAxis = animatedModel.getAxis(AxisType.Y);
//		yAxis.setMin(0);
		Long max = 0L;

		CartesianChartModel model = new CartesianChartModel();
		for (UtilizacaoDTO utilizacao : listaUtilizacao) {
//			LineChartSeries serie = new LineChartSeries();
			ChartSeries serie = new ChartSeries();
			serie.setLabel(utilizacao.getCondominio().getPessoa().getNome());

			max = obterMaximoUtilizacao(max, utilizacao);

			serie.set("00:00", utilizacao.getHora0());
			serie.set("01:00", utilizacao.getHora1());
			serie.set("02:00", utilizacao.getHora2());
			serie.set("03:00", utilizacao.getHora3());
			serie.set("04:00", utilizacao.getHora4());
			serie.set("05:00", utilizacao.getHora5());
			serie.set("06:00", utilizacao.getHora6());
			serie.set("07:00", utilizacao.getHora7());
			serie.set("08:00", utilizacao.getHora8());
			serie.set("09:00", utilizacao.getHora9());
			serie.set("10:00", utilizacao.getHora10());
			serie.set("11:00", utilizacao.getHora11());
			serie.set("12:00", utilizacao.getHora12());
			serie.set("13:00", utilizacao.getHora13());
			serie.set("14:00", utilizacao.getHora14());
			serie.set("15:00", utilizacao.getHora15());
			serie.set("16:00", utilizacao.getHora16());
			serie.set("17:00", utilizacao.getHora17());
			serie.set("18:00", utilizacao.getHora18());
			serie.set("19:00", utilizacao.getHora19());
			serie.set("20:00", utilizacao.getHora20());
			serie.set("21:00", utilizacao.getHora21());
			serie.set("22:00", utilizacao.getHora22());
			serie.set("23:00", utilizacao.getHora23());
			
			model.addSeries(serie);

		}
//		maxY = ((long) (max * 1.2));
//		yAxis.setMax(((long) (max * 1.2)));

		return model;
	}

	private Long obterMaximoUtilizacao(Long max, UtilizacaoDTO utilizacao) {
		Long novoMax = max;
		if (utilizacao.getHora0() > max) {
			novoMax = utilizacao.getHora0();
		}
		if (utilizacao.getHora1() > max) {
			novoMax = utilizacao.getHora1();
		}
		if (utilizacao.getHora2() > max) {
			novoMax = utilizacao.getHora2();
		}
		if (utilizacao.getHora3() > max) {
			novoMax = utilizacao.getHora3();
		}
		if (utilizacao.getHora4() > max) {
			novoMax = utilizacao.getHora4();
		}
		if (utilizacao.getHora5() > max) {
			novoMax = utilizacao.getHora5();
		}
		if (utilizacao.getHora6() > max) {
			novoMax = utilizacao.getHora6();
		}
		if (utilizacao.getHora7() > max) {
			novoMax = utilizacao.getHora7();
		}
		if (utilizacao.getHora8() > max) {
			novoMax = utilizacao.getHora8();
		}
		if (utilizacao.getHora9() > max) {
			novoMax = utilizacao.getHora9();
		}
		if (utilizacao.getHora10() > max) {
			novoMax = utilizacao.getHora10();
		}
		if (utilizacao.getHora11() > max) {
			novoMax = utilizacao.getHora11();
		}
		if (utilizacao.getHora12() > max) {
			novoMax = utilizacao.getHora12();
		}
		if (utilizacao.getHora13() > max) {
			novoMax = utilizacao.getHora13();
		}
		if (utilizacao.getHora14() > max) {
			novoMax = utilizacao.getHora14();
		}
		if (utilizacao.getHora15() > max) {
			novoMax = utilizacao.getHora15();
		}
		if (utilizacao.getHora16() > max) {
			novoMax = utilizacao.getHora16();
		}
		if (utilizacao.getHora17() > max) {
			novoMax = utilizacao.getHora17();
		}
		if (utilizacao.getHora18() > max) {
			novoMax = utilizacao.getHora18();
		}
		if (utilizacao.getHora19() > max) {
			novoMax = utilizacao.getHora19();
		}
		if (utilizacao.getHora20() > max) {
			novoMax = utilizacao.getHora20();
		}
		if (utilizacao.getHora21() > max) {
			novoMax = utilizacao.getHora21();
		}
		if (utilizacao.getHora22() > max) {
			novoMax = utilizacao.getHora22();
		}
		if (utilizacao.getHora23() > max) {
			novoMax = utilizacao.getHora23();
		}

		return novoMax;
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
	 * @return the animatedModel
	 */
	public CartesianChartModel getAnimatedModel() {
		return animatedModel;
	}

	/**
	 * @param animatedModel
	 *            the animatedModel to set
	 */
	public void setAnimatedModel(CartesianChartModel animatedModel) {
		this.animatedModel = animatedModel;
	}

	/**
	 * @return the maxY
	 */
	public Long getMaxY() {
		return maxY;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxY(Long maxY) {
		this.maxY = maxY;
	}

}