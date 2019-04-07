package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SindicoCondominioPK implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_SINDICO")
	private Long idSindico;

	@Column(name = "ID_CONDOMINIO")
	private Long idCondominio;

	/**
	 * @return the idSindico
	 */
	public Long getIdSindico() {
		return this.idSindico;
	}

	/**
	 * @param idSindico
	 *            the idSindico to set
	 */
	public void setIdSindico(final Long idSindico) {
		this.idSindico = idSindico;
	}

	/**
	 * @return the idCondominio
	 */
	public Long getIdCondominio() {
		return this.idCondominio;
	}

	/**
	 * @param idCondominio
	 *            the idCondominio to set
	 */
	public void setIdCondominio(final Long idCondominio) {
		this.idCondominio = idCondominio;
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
				+ ((this.idCondominio == null) ? 0 : this.idCondominio
						.hashCode());
		result = (prime * result)
				+ ((this.idSindico == null) ? 0 : this.idSindico.hashCode());
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
		if (!(obj instanceof SindicoCondominioPK)) {
			return false;
		}
		final SindicoCondominioPK other = (SindicoCondominioPK) obj;
		if (this.idCondominio == null) {
			if (other.idCondominio != null) {
				return false;
			}
		} else if (!this.idCondominio.equals(other.idCondominio)) {
			return false;
		}
		if (this.idSindico == null) {
			if (other.idSindico != null) {
				return false;
			}
		} else if (!this.idSindico.equals(other.idSindico)) {
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
		return "SindicoCondominioPK [idSindico=" + this.idSindico
				+ ", idCondominio=" + this.idCondominio + "]";
	}

}
