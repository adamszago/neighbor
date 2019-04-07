package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;

@Entity
@Table(name = "MORADOR_AGREGADO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MoradorAgregado implements
		IEntity<MoradorAgregadoPK, MoradorAgregadoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MoradorAgregadoPK chaveComposta;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR", insertable = false, updatable = false))
	private Morador morador;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_AGREGADO", referencedColumnName = "ID_AGREGADO", insertable = false, updatable = false))
	private Agregado agregado;

	@Column(name = "PARENTESCO_AGREGADO")
	private String parentesco;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM")
	private Date dataFim;

	/**
	 * @return the chaveComposta
	 */
	public MoradorAgregadoPK getChaveComposta() {
		return this.chaveComposta;
	}

	/**
	 * @param chaveComposta
	 *            the chaveComposta to set
	 */
	public void setChaveComposta(final MoradorAgregadoPK chaveComposta) {
		this.chaveComposta = chaveComposta;
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
	 * @return the agregado
	 */
	public Agregado getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final Agregado agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the parentesco
	 */
	public String getParentesco() {
		return this.parentesco;
	}

	/**
	 * @param parentesco
	 *            the parentesco to set
	 */
	public void setParentesco(final String parentesco) {
		this.parentesco = parentesco;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return this.dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return this.dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.chaveComposta == null) ? 0 : this.chaveComposta
						.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MoradorAgregado)) {
			return false;
		}
		final MoradorAgregado other = (MoradorAgregado) obj;
		if (this.chaveComposta == null) {
			if (other.chaveComposta != null) {
				return false;
			}
		} else if (!this.chaveComposta.equals(other.chaveComposta)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MoradorAgregado [chaveComposta=" + this.chaveComposta
				+ ", morador=" + this.morador + ", agregado=" + this.agregado
				+ ", parentesco=" + this.parentesco + ", dataInicio="
				+ this.dataInicio + ", dataFim=" + this.dataFim + "]";
	}

	@Override
	public MoradorAgregadoDTO createDto() {
		return MoradorAgregadoDTO.Builder.getInstance().create(this);
	}

}
