package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MoradorUnidadeHabitacionalPK implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_MORADOR")
	private Long idMorador;

	@Column(name = "ID_UNIDADE_HABITACIONAL")
	private Long idUnidade;

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
	 * @return the idUnidade
	 */
	public Long getIdUnidade() {
		return this.idUnidade;
	}

	/**
	 * @param idUnidade
	 *            the idUnidade to set
	 */
	public void setIdUnidade(final Long idUnidade) {
		this.idUnidade = idUnidade;
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
				+ ((this.idMorador == null) ? 0 : this.idMorador.hashCode());
		result = (prime * result)
				+ ((this.idUnidade == null) ? 0 : this.idUnidade.hashCode());
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
		if (!(obj instanceof MoradorUnidadeHabitacionalPK)) {
			return false;
		}
		final MoradorUnidadeHabitacionalPK other = (MoradorUnidadeHabitacionalPK) obj;
		if (this.idMorador == null) {
			if (other.idMorador != null) {
				return false;
			}
		} else if (!this.idMorador.equals(other.idMorador)) {
			return false;
		}
		if (this.idUnidade == null) {
			if (other.idUnidade != null) {
				return false;
			}
		} else if (!this.idUnidade.equals(other.idUnidade)) {
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
		return "MoradorUnidadeHabitacionalPK [idMorador=" + this.idMorador
				+ ", idUnidade=" + this.idUnidade + "]";
	}

}
