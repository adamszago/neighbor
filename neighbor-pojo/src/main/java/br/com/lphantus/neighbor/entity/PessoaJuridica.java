package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PESSOA_JURIDICA")
@PrimaryKeyJoinColumns(@PrimaryKeyJoinColumn(name = "ID_PESSOA_JURIDICA", referencedColumnName = "ID_PESSOA"))
public class PessoaJuridica extends Pessoa implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "CNPJ")
	private String cnpj;

	@Column(name = "NOME_FANTASIA")
	private String nomeFantasia;

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return this.cnpj;
	}

	/**
	 * @param cnpj
	 *            the cnpj to set
	 */
	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the nomeFantasia
	 */
	public String getNomeFantasia() {
		return this.nomeFantasia;
	}

	/**
	 * @param nomeFantasia
	 *            the nomeFantasia to set
	 */
	public void setNomeFantasia(final String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result)
				+ ((getIdPessoa() == null) ? 0 : getIdPessoa().hashCode());
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
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof PessoaJuridica)) {
			return false;
		}
		final PessoaJuridica other = (PessoaJuridica) obj;
		if (getIdPessoa() == null) {
			if (other.getIdPessoa() != null) {
				return false;
			}
		} else if (!getIdPessoa().equals(other.getIdPessoa())) {
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
		return "PessoaJuridica [cnpj=" + this.cnpj + ", nomeFantasia="
				+ this.nomeFantasia + "]";
	}

}
