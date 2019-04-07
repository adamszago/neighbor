package br.com.lphantus.neighbor.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.CondominioDTO;

@Entity
@Table(name = "CONDOMINIO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumns(@PrimaryKeyJoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_PESSOA_JURIDICA"))
public class Condominio extends PessoaJuridica implements
		IEntity<Long, CondominioDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "NOME_ABREVIADO")
	private String nomeAbreviado;

	@Column(name = "POSSUI_BLOCO", nullable = false)
	private boolean possuiBloco = false;

	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, targetEntity = Endereco.class)
	@JoinColumns(@JoinColumn(name = "ID_ENDERECO", referencedColumnName = "ID_ENDERECO"))
	private Endereco endereco;

	@Column(name = "NUMERO")
	private Long numero;

	@Column(name = "COMPLEMENTO")
	private String complemento;

	@Column(name = "CT_SUSPENSA")
	private boolean contaSuspensa;

	/**
	 * @return the nomeAbreviado
	 */
	public String getNomeAbreviado() {
		return nomeAbreviado;
	}

	/**
	 * @param nomeAbreviado
	 *            the nomeAbreviado to set
	 */
	public void setNomeAbreviado(final String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}

	/**
	 * @return the possuiBloco
	 */
	public boolean isPossuiBloco() {
		return possuiBloco;
	}

	/**
	 * @param possuiBloco
	 *            the possuiBloco to set
	 */
	public void setPossuiBloco(final boolean possuiBloco) {
		this.possuiBloco = possuiBloco;
	}

	/**
	 * @return the endereco
	 */
	public Endereco getEndereco() {
		return endereco;
	}

	/**
	 * @param endereco
	 *            the endereco to set
	 */
	public void setEndereco(final Endereco endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the numero
	 */
	public Long getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(final Long numero) {
		this.numero = numero;
	}

	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		return complemento;
	}

	/**
	 * @param complemento
	 *            the complemento to set
	 */
	public void setComplemento(final String complemento) {
		this.complemento = complemento;
	}

	/**
	 * @return the contaSuspensa
	 */
	public boolean isContaSuspensa() {
		return contaSuspensa;
	}

	/**
	 * @param contaSuspensa
	 *            the contaSuspensa to set
	 */
	public void setContaSuspensa(final boolean contaSuspensa) {
		this.contaSuspensa = contaSuspensa;
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
		if (!(obj instanceof Condominio)) {
			return false;
		}
		final Condominio other = (Condominio) obj;
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
		return "Condominio [nomeAbreviado=" + nomeAbreviado + ", possuiBloco="
				+ possuiBloco + ", endereco=" + endereco + ", numero=" + numero
				+ ", complemento=" + complemento + "]";
	}

	@Override
	public CondominioDTO createDto() {
		return CondominioDTO.Builder.getInstance().create(this);
	}

}
