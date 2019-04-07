package br.com.lphantus.neighbor.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.ReservaTempDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.service.IReservaService;
import br.com.lphantus.neighbor.service.IUnidadeHabitacionalService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;
import br.com.lphantus.neighbor.utils.Utilitarios;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "aprovacaoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class AprovacaoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Service
	@Autowired
	private IReservaService serviceReserva;

	@Autowired
	private IUnidadeHabitacionalService serviceUnidade;

	// Reserva
	private ReservaDTO reserva;
	private List<ReservaDTO> reservas;
	private ReservaTempDTO reservaTemp;
	private List<ReservaTempDTO> reservasTemp;
	private int qtdItensPendentes;

	// historico
	private HistoricoDTO historico;

	/**
	 * Construtor padrao
	 */
	public AprovacaoController() {

	}

	@PostConstruct
	public void initialize() {
		this.atualizarTela(false);
	}

	/**
	 * Atualiza todos os componentes da view
	 * 
	 * @param naoEhPrimeiraVez
	 * 
	 * @throws ServiceException
	 */
	private void atualizarTela(boolean naoEhPrimeiraVez) {
		this.reserva = new ReservaDTO();
		this.reservas = new ArrayList<ReservaDTO>();
		this.reservaTemp = new ReservaTempDTO();

		if (naoEhPrimeiraVez) {
			gerarListaReservaTemp();
		}
	}

	public void gerarListaReservaTemp() {
		this.reservasTemp = new ArrayList<ReservaTempDTO>();
		try {
			this.reservasTemp = this.serviceReserva.getReservasPendenteAprovacao(condominioUsuarioLogado());
			this.qtdItensPendentes = this.reservasTemp.size();
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_APROVAR_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void aprovarReserva() {
		try {
			this.reservaTemp.setCondominio(condominioUsuarioLogado());
			this.serviceReserva.save(converterReservaTempParaReserva(this.reservaTemp), false);

			final UnidadeHabitacionalDTO unidade = this.serviceUnidade.buscarUnidadeHabitacionalMorador(this.reservaTemp.getMorador());

			registrarHistorico(String.format("Aprovou Reserva. Item: %s | Data reserva: %s | Morador: %s | Casa: %s ", this.reservaTemp.getItemReserva().getNome(),
					Utilitarios.formatarDia(this.reservaTemp.getDataReserva()), this.reservaTemp.getMorador().getPessoa().getNome(), unidade.getIdentificacao()));

			this.serviceReserva.deletarReservaPendente(this.reservaTemp);

			gerarListaReservaTemp();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("RESERVA_APROVADA"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	@Secured({ "ROLE_APROVAR_RESERVA", "ROLE_ROOT" })
	@Transactional(propagation = Propagation.REQUIRED)
	public void reprovarReserva() {
		try {
			this.serviceReserva.deletarReservaPendente(this.reservaTemp);

			final UnidadeHabitacionalDTO unidade = this.serviceUnidade.buscarUnidadeHabitacionalMorador(this.reservaTemp.getMorador());

			registrarHistorico(String.format("Reprovou Reserva. Item: %s | Data reserva: %s | Morador: %s | Casa: %s ", this.reservaTemp.getItemReserva().getNome(),
					Utilitarios.formatarDia(this.reservaTemp.getDataReserva()), this.reservaTemp.getMorador().getPessoa().getNome(), unidade.getIdentificacao()));

			gerarListaReservaTemp();

			JsfUtil.addSuccessMessage(GerenciadorMensagem.getMensagem("RESERVA_REPROVADA"));
		} catch (final ServiceException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}

	private ReservaDTO converterReservaTempParaReserva(final ReservaTempDTO resTemp) {
		final ReservaDTO res = new ReservaDTO();
		res.setCondominio(resTemp.getCondominio());
		res.setDataReserva(resTemp.getDataReserva());
		res.setDataSolicitacao(resTemp.getDataSolicitacao());
		res.setItemReserva(resTemp.getItemReserva());
		res.setMorador(resTemp.getMorador());
		res.setPago(resTemp.isPago());

		return res;
	}

	// direcionamento de paginas

	public String pageListaAprovacao() {
		atualizarTela(true);
		return "/pages/administracao/listaaprovacao.jsf";
	}

	// GETTERS E SETTERS

	/**
	 * @return the reserva
	 */
	public ReservaDTO getReserva() {
		return reserva;
	}

	/**
	 * @param reserva
	 *            the reserva to set
	 */
	public void setReserva(ReservaDTO reserva) {
		this.reserva = reserva;
	}

	/**
	 * @return the reservas
	 */
	public List<ReservaDTO> getReservas() {
		return reservas;
	}

	/**
	 * @param reservas
	 *            the reservas to set
	 */
	public void setReservas(List<ReservaDTO> reservas) {
		this.reservas = reservas;
	}

	/**
	 * @return the reservaTemp
	 */
	public ReservaTempDTO getReservaTemp() {
		return reservaTemp;
	}

	/**
	 * @param reservaTemp
	 *            the reservaTemp to set
	 */
	public void setReservaTemp(ReservaTempDTO reservaTemp) {
		this.reservaTemp = reservaTemp;
	}

	/**
	 * @return the reservasTemp
	 */
	public List<ReservaTempDTO> getReservasTemp() {
		return reservasTemp;
	}

	/**
	 * @param reservasTemp
	 *            the reservasTemp to set
	 */
	public void setReservasTemp(List<ReservaTempDTO> reservasTemp) {
		this.reservasTemp = reservasTemp;
	}

	/**
	 * @return the qtdItensPendentes
	 */
	public int getQtdItensPendentes() {
		return qtdItensPendentes;
	}

	/**
	 * @param qtdItensPendentes
	 *            the qtdItensPendentes to set
	 */
	public void setQtdItensPendentes(int qtdItensPendentes) {
		this.qtdItensPendentes = qtdItensPendentes;
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
