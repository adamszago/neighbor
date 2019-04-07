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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.ReservaDTO;

/**
 * @author Adams Zago Classe para Reserva de salão, churrasqueira entre outros.
 */
@Entity
@Table(name = "RESERVA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reserva implements IEntity<Long, ReservaDTO> {

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

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_ITEM_RESERVA", referencedColumnName = "ID_ITEM_RESERVA"))
	private ItemReserva itemReserva;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR"))
	private Morador morador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getDataSolicitacao() {
		return this.dataSolicitacao;
	}

	public void setDataSolicitacao(final Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public Date getDataReserva() {
		return this.dataReserva;
	}

	public void setDataReserva(final Date dataReserva) {
		this.dataReserva = dataReserva;
	}

	public boolean isPago() {
		return this.pago;
	}

	public void setPago(final boolean pago) {
		this.pago = pago;
	}

	public ItemReserva getItemReserva() {
		return this.itemReserva;
	}

	public void setItemReserva(final ItemReserva itemReserva) {
		this.itemReserva = itemReserva;
	}

	public Morador getMorador() {
		return this.morador;
	}

	public void setMorador(final Morador morador) {
		this.morador = morador;
	}

	public Condominio getCondominio() {
		return this.condominio;
	}

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
		final Reserva other = (Reserva) obj;
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
	public ReservaDTO createDto() {
		return ReservaDTO.Builder.getInstance().create(this);
	}

}
