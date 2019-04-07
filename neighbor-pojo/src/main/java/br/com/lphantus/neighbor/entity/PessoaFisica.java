package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "PESSOA_FISICA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumn(name = "ID_PESSOA_FISICA", referencedColumnName = "ID_PESSOA")
public class PessoaFisica extends Pessoa implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "CNH")
	private String cnh;

	@Column(name = "RG")
	private String rg;

	@Column(name = "CPF")
	private String cpf;

	@Column(name = "SEXO")
	private String sexo;

	@Column(name = "PROFISSAO")
	private String profissao;

	/**
	 * @return the cnh
	 */
	public String getCnh() {
		return this.cnh;
	}

	/**
	 * @param cnh
	 *            the cnh to set
	 */
	public void setCnh(final String cnh) {
		this.cnh = cnh;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return this.rg;
	}

	/**
	 * @param rg
	 *            the rg to set
	 */
	public void setRg(final String rg) {
		this.rg = rg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return this.cpf;
	}

	/**
	 * @param cpf
	 *            the cpf to set
	 */
	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return this.sexo;
	}

	/**
	 * @param sexo
	 *            the sexo to set
	 */
	public void setSexo(final String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the profissao
	 */
	public String getProfissao() {
		return this.profissao;
	}

	/**
	 * @param profissao
	 *            the profissao to set
	 */
	public void setProfissao(final String profissao) {
		this.profissao = profissao;
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
		if (!(obj instanceof PessoaFisica)) {
			return false;
		}
		final PessoaFisica other = (PessoaFisica) obj;
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
		return "PessoaFisica [cnh=" + this.cnh + ", rg=" + this.rg + ", cpf="
				+ this.cpf + ", sexo=" + this.sexo + ", profissao="
				+ this.profissao + "]";
	}

}
