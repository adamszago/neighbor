package br.com.lphantus.neighbor.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.component.AgendaSindicoEvento;
import br.com.lphantus.neighbor.entity.AgendaSindico;
import br.com.lphantus.neighbor.service.IAgendaSindicoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;
import br.com.lphantus.neighbor.util.JsfUtil;

@Controller
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ManagedBean(name = "agendaSindicoController")
@Transactional(propagation = Propagation.SUPPORTS)
public class AgendaSindicoController extends AbstractController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private IAgendaSindicoService agendaSindicoService;

	private final ScheduleModel eventModel = new DefaultScheduleModel();
	private AgendaSindicoEvento event = new AgendaSindicoEvento();

	/**
	 * Construtor padrao
	 */
	public AgendaSindicoController() {

	}

	@Secured({ "ROLE_AGENDA_SINDICO", "ROLE_ROOT" })
	public String pageAgenda() {
		carregarAgenda();
		return "/pages/administracao/agendasindico.jsf";
	}

	private void carregarAgenda() {
		try {
			CondominioDTO condominio;
			if (usuarioLogado().isRoot()) {
				condominio = null;
			} else {
				condominio = condominioUsuarioLogado();
			}

			final List<AgendaSindico> lista = this.agendaSindicoService
					.listarPorCondominio(condominio);
			if ((null != lista) && !lista.isEmpty()) {
				this.eventModel.clear();
				for (final AgendaSindico item : lista) {
					this.eventModel.addEvent(new AgendaSindicoEvento(item));
				}
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao listar itens da agenda.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao listar itens da agenda.", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void gravarEvento() {
		if (this.event.getEndDate() == null) {
			this.event.setEndDate(this.event.getStartDate());
		}
		try {
			if (this.event.getId() == null) {
				this.agendaSindicoService.gravarAgendaSindico(this.event,
						condominioUsuarioLogado());
				this.eventModel.addEvent(this.event);
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("SAVE_OK"));
			} else {
				this.agendaSindicoService.atualizarAgendaSindico(this.event);
				this.eventModel.updateEvent(this.event);
				JsfUtil.addSuccessMessage(GerenciadorMensagem
						.getMensagem("ALTER_OK"));
			}
		} catch (final ServiceException e) {
			getLogger().error("Erro ao gravar evento da agenda.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao gravar evento da agenda.", e);
		}
		this.event = new AgendaSindicoEvento();
	}

	public void onEventSelect(final SelectEvent selectEvent) {
		this.event = (AgendaSindicoEvento) selectEvent.getObject();
	}

	public void onDateSelect(final SelectEvent selectEvent) {
		this.event = new AgendaSindicoEvento();
		this.event.setTitle("");

		final Date dt = (Date) selectEvent.getObject();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);

		this.event.setStartDate(dt);
		this.event.setEndDate(dt);
	}

	public void onEventMove(final ScheduleEntryMoveEvent event) {

		this.event = (AgendaSindicoEvento) event.getScheduleEvent();

		// --------------------------------------------------------------
		final Calendar cal = Calendar.getInstance();
		cal.setTime(this.event.getEndDate());
		cal.add(Calendar.DAY_OF_YEAR, event.getDayDelta());
		// --------------------------------------------------------------

		try {
			this.agendaSindicoService.atualizarAgendaSindico(this.event);
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao mudar data do evento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao mudar data do evento.", e);
		}
	}

	public void onEventResize(final ScheduleEntryResizeEvent event) {

		this.event = (AgendaSindicoEvento) event.getScheduleEvent();

		try {
			this.agendaSindicoService.atualizarAgendaSindico(this.event);
			JsfUtil.addSuccessMessage(GerenciadorMensagem
					.getMensagem("ALTER_OK"));
		} catch (final ServiceException e) {
			getLogger().error("Erro ao modificar data final do evento.", e);
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (final Exception e) {
			getLogger().error("Erro ao modificar data fianl do evento.", e);
		}
	}

	/**
	 * @return the eventModel
	 */
	public ScheduleModel getEventModel() {
		return this.eventModel;
	}

	/**
	 * @return the event
	 */
	public AgendaSindicoEvento getEvent() {
		return this.event;
	}

}