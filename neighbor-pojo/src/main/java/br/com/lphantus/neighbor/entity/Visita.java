package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.VisitaDTO;

@Entity
@Table(name = "VISITA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Visita implements IEntity<Long, VisitaDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VISITA")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR"))
	private Morador morador;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumns(@JoinColumn(name = "ID_VISITANTE", referencedColumnName = "ID_VISITANTE"))
	private Visitante visitante;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INICIO_VISITA")
	private Date inicioVisita;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIM_VISITA")
	private Date fimVisita;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INICIO_AGENDAMENTO_VISITA")
	private Date inicioAgendamentoVisita;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIM_AGENDAMENTO_VISITA")
	private Date fimAgendamentoVisita;

	@Column(name = "VISITA_CONFIRMADA")
	private boolean confirmado;

	@Column(name = "VISITA_ATIVA")
	private boolean ativa;

	@Column(name = "TIPO_ACESSO")
	private Long tipoAcesso;
	
	@Column(name = "PLACA_UTILIZADA", length = 9)
	private String placaUtilizada;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final Morador morador) {
		this.morador = morador;
	}

	/**
	 * @return the visitante
	 */
	public Visitante getVisitante() {
		return visitante;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(final Visitante visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the inicioVisita
	 */
	public Date getInicioVisita() {
		return inicioVisita;
	}

	/**
	 * @param inicioVisita
	 *            the inicioVisita to set
	 */
	public void setInicioVisita(Date inicioVisita) {
		this.inicioVisita = inicioVisita;
	}

	/**
	 * @return the fimVisita
	 */
	public Date getFimVisita() {
		return fimVisita;
	}

	/**
	 * @param fimVisita
	 *            the fimVisita to set
	 */
	public void setFimVisita(Date fimVisita) {
		this.fimVisita = fimVisita;
	}

	/**
	 * @return the inicioAgendamentoVisita
	 */
	public Date getInicioAgendamentoVisita() {
		return inicioAgendamentoVisita;
	}

	/**
	 * @param inicioAgendamentoVisita
	 *            the inicioAgendamentoVisita to set
	 */
	public void setInicioAgendamentoVisita(Date inicioAgendamentoVisita) {
		this.inicioAgendamentoVisita = inicioAgendamentoVisita;
	}

	/**
	 * @return the fimAgendamentoVisita
	 */
	public Date getFimAgendamentoVisita() {
		return fimAgendamentoVisita;
	}

	/**
	 * @param fimAgendamentoVisita
	 *            the fimAgendamentoVisita to set
	 */
	public void setFimAgendamentoVisita(Date fimAgendamentoVisita) {
		this.fimAgendamentoVisita = fimAgendamentoVisita;
	}

	/**
	 * @return the confirmado
	 */
	public boolean isConfirmado() {
		return confirmado;
	}

	/**
	 * @param confirmado
	 *            the confirmado to set
	 */
	public void setConfirmado(final boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @return the ativa
	 */
	public boolean isAtiva() {
		return ativa;
	}

	/**
	 * @param ativa
	 *            the ativa to set
	 */
	public void setAtiva(final boolean ativa) {
		this.ativa = ativa;
	}

	/**
	 * @return the tipoAcesso
	 */
	public Long getTipoAcesso() {
		return tipoAcesso;
	}

	/**
	 * @param tipoAcesso
	 *            the tipoAcesso to set
	 */
	public void setTipoAcesso(Long tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	/**
	 * @return the placaUtilizada
	 */
	public String getPlacaUtilizada() {
		return placaUtilizada;
	}

	/**
	 * @param placaUtilizada the placaUtilizada to set
	 */
	public void setPlacaUtilizada(String placaUtilizada) {
		this.placaUtilizada = placaUtilizada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Visita other = (Visita) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public VisitaDTO createDto() {
		return VisitaDTO.Builder.getInstance().create(this);
	}

}
