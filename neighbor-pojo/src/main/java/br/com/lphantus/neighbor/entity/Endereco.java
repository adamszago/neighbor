package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.EnderecoDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ENDERECO")
public class Endereco implements IEntity<Long, EnderecoDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ENDERECO")
	private Long idEndereco;

	@Column(name = "LOGRADOURO")
	private String logradouro;

	@Column(name = "BAIRRO")
	private String bairro;

	@Column(name = "CIDADE")
	private String cidade;

	@Column(name = "UF")
	private String uf;

	@Column(name = "CEP")
	private String cep;

	/**
	 * @return the idEndereco
	 */
	public Long getIdEndereco() {
		return this.idEndereco;
	}

	/**
	 * @param idEndereco
	 *            the idEndereco to set
	 */
	public void setIdEndereco(final Long idEndereco) {
		this.idEndereco = idEndereco;
	}

	/**
	 * @return the logradouro
	 */
	public String getLogradouro() {
		return this.logradouro;
	}

	/**
	 * @param logradouro
	 *            the logradouro to set
	 */
	public void setLogradouro(final String logradouro) {
		this.logradouro = logradouro;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return this.bairro;
	}

	/**
	 * @param bairro
	 *            the bairro to set
	 */
	public void setBairro(final String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return this.cidade;
	}

	/**
	 * @param cidade
	 *            the cidade to set
	 */
	public void setCidade(final String cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return this.uf;
	}

	/**
	 * @param uf
	 *            the uf to set
	 */
	public void setUf(final String uf) {
		this.uf = uf;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return this.cep;
	}

	/**
	 * @param cep
	 *            the cep to set
	 */
	public void setCep(final String cep) {
		this.cep = cep;
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
				+ ((this.idEndereco == null) ? 0 : this.idEndereco.hashCode());
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
		if (!(obj instanceof Endereco)) {
			return false;
		}
		final Endereco other = (Endereco) obj;
		if (this.idEndereco == null) {
			if (other.idEndereco != null) {
				return false;
			}
		} else if (!this.idEndereco.equals(other.idEndereco)) {
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
		return "Endereco [idEndereco=" + this.idEndereco + ", logradouro="
				+ this.logradouro + ", bairro=" + this.bairro + ", cidade="
				+ this.cidade + ", uf=" + this.uf + ", cep=" + this.cep + "]";
	}

	@Override
	public EnderecoDTO createDto() {
		return EnderecoDTO.Builder.getInstance().create(this);
	}

}
