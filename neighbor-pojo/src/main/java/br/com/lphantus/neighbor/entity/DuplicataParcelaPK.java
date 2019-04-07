package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DuplicataParcelaPK implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID_DUPLICATA")
	private Long idDuplicata;

	@Column(name = "NUM_PARCELA")
	private Long numeroParcela;

	/**
	 * @return the idDuplicata
	 */
	public Long getIdDuplicata() {
		return this.idDuplicata;
	}

	/**
	 * @param idDuplicata
	 *            the idDuplicata to set
	 */
	public void setIdDuplicata(final Long idDuplicata) {
		this.idDuplicata = idDuplicata;
	}

	/**
	 * @return the numeroParcela
	 */
	public Long getNumeroParcela() {
		return this.numeroParcela;
	}

	/**
	 * @param numeroParcela
	 *            the numeroParcela to set
	 */
	public void setNumeroParcela(final Long numeroParcela) {
		this.numeroParcela = numeroParcela;
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
				+ ((this.idDuplicata == null) ? 0 : this.idDuplicata.hashCode());
		result = (prime * result)
				+ ((this.numeroParcela == null) ? 0 : this.numeroParcela
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
		if (!(obj instanceof DuplicataParcelaPK)) {
			return false;
		}
		final DuplicataParcelaPK other = (DuplicataParcelaPK) obj;
		if (this.idDuplicata == null) {
			if (other.idDuplicata != null) {
				return false;
			}
		} else if (!this.idDuplicata.equals(other.idDuplicata)) {
			return false;
		}
		if (this.numeroParcela == null) {
			if (other.numeroParcela != null) {
				return false;
			}
		} else if (!this.numeroParcela.equals(other.numeroParcela)) {
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
		return "DuplicataParcelaPK [idDuplicata=" + this.idDuplicata
				+ ", numeroParcela=" + this.numeroParcela + "]";
	}

}
