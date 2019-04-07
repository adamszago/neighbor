package br.com.lphantus.neighbor.component;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

import br.com.lphantus.neighbor.entity.AgendaSindico;

public class AgendaSindicoEvento implements ScheduleEvent {

	private final DefaultScheduleEvent eventoDefault = new DefaultScheduleEvent();

	private Long id;
	private Date dataInicio;
	private Date dataFim;

	private String titulo;
	private String descricao;

	public AgendaSindicoEvento() {
		super();
	}

	public AgendaSindicoEvento(final AgendaSindico agenda) {
		super();
		this.id = agenda.getId();
		this.dataFim = agenda.getDataFim();
		this.dataInicio = agenda.getDataInicio();
		this.titulo = agenda.getTitulo();
		this.descricao = agenda.getDescricao();
	}

	@Override
	public void setId(final String idParametro) {
		if (StringUtils.isNumeric(idParametro)) {
			this.id = Long.valueOf(idParametro);
		}
	}

	@Override
	public String getId() {
		if (null == this.id) {
			return null;
		} else {
			return this.id.toString();
		}
	}

	@Override
	public Object getData() {
		return this.eventoDefault.getData();
	}

	@Override
	public Date getStartDate() {
		return this.dataInicio;
	}

	public void setStartDate(final Date data) {
		this.dataInicio = data;
	}

	@Override
	public Date getEndDate() {
		return this.dataFim;
	}

	public void setEndDate(final Date data) {
		this.dataFim = data;
	}

	@Override
	public String getStyleClass() {
		return this.eventoDefault.getStyleClass();
	}

	@Override
	public String getTitle() {
		return this.titulo;
	}

	public void setTitle(final String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean isAllDay() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	public AgendaSindico criarEntidade() {
		final AgendaSindico retorno = new AgendaSindico();
		retorno.setDataFim(this.dataFim);
		retorno.setDataInicio(this.dataInicio);
		retorno.setDescricao(this.descricao);
		retorno.setId(this.id);
		retorno.setTitulo(this.titulo);
		return retorno;
	}

}
