package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.ReservaTempDTO;

/**
 * @author Adams Zago Classe para Reserva de salão, churrasqueira entre outros.
 */
@Entity
@Table(name = "RESERVA_TEMP")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReservaTemp implements IEntity<Long, ReservaTempDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_RESERVA")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_SOLICITACAO")
	private Date dataSolicitacao;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_RESERVA")
	private Date dataReserva;

	@Column(name = "PAGO")
	private boolean pago = false;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_ITEM_RESERVA", referencedColumnName = "ID_ITEM_RESERVA") })
	private ItemReserva itemReserva;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR") })
	private Morador morador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO") })
	private Condominio condominio;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the dataSolicitacao
	 */
	public Date getDataSolicitacao() {
		return this.dataSolicitacao;
	}

	/**
	 * @param dataSolicitacao
	 *            the dataSolicitacao to set
	 */
	public void setDataSolicitacao(final Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	/**
	 * @return the dataReserva
	 */
	public Date getDataReserva() {
		return this.dataReserva;
	}

	/**
	 * @param dataReserva
	 *            the dataReserva to set
	 */
	public void setDataReserva(final Date dataReserva) {
		this.dataReserva = dataReserva;
	}

	/**
	 * @return the pago
	 */
	public boolean isPago() {
		return this.pago;
	}

	/**
	 * @param pago
	 *            the pago to set
	 */
	public void setPago(final boolean pago) {
		this.pago = pago;
	}

	/**
	 * @return the itemReserva
	 */
	public ItemReserva getItemReserva() {
		return this.itemReserva;
	}

	/**
	 * @param itemReserva
	 *            the itemReserva to set
	 */
	public void setItemReserva(final ItemReserva itemReserva) {
		this.itemReserva = itemReserva;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final Morador morador) {
		this.morador = morador;
	}

	/**
	 * @return the condominio
	 */
	public Condominio getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final Condominio condominio) {
		this.condominio = condominio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		final ReservaTemp other = (ReservaTemp) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public ReservaTempDTO createDto() {
		return ReservaTempDTO.Builder.getInstance().create(this);
	}

}
