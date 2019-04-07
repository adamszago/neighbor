package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MoradorAgregadoPK implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_MORADOR")
	private Long idMorador;

	@Column(name = "ID_AGREGADO")
	private Long idAgregado;

	/**
	 * @return the idMorador
	 */
	public Long getIdMorador() {
		return this.idMorador;
	}

	/**
	 * @param idMorador
	 *            the idMorador to set
	 */
	public void setIdMorador(final Long idMorador) {
		this.idMorador = idMorador;
	}

	/**
	 * @return the idAgregado
	 */
	public Long getIdAgregado() {
		return this.idAgregado;
	}

	/**
	 * @param idAgregado
	 *            the idAgregado to set
	 */
	public void setIdAgregado(final Long idAgregado) {
		this.idAgregado = idAgregado;
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
				+ ((this.idAgregado == null) ? 0 : this.idAgregado.hashCode());
		result = (prime * result)
				+ ((this.idMorador == null) ? 0 : this.idMorador.hashCode());
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
		if (!(obj instanceof MoradorAgregadoPK)) {
			return false;
		}
		final MoradorAgregadoPK other = (MoradorAgregadoPK) obj;
		if (this.idAgregado == null) {
			if (other.idAgregado != null) {
				return false;
			}
		} else if (!this.idAgregado.equals(other.idAgregado)) {
			return false;
		}
		if (this.idMorador == null) {
			if (other.idMorador != null) {
				return false;
			}
		} else if (!this.idMorador.equals(other.idMorador)) {
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
		return "MoradorAgregadoPK [idMorador=" + this.idMorador
				+ ", idAgregado=" + this.idAgregado + "]";
	}

}
