package br.com.lphantus.neighbor.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.utils.IdadeUtil;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pessoa implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PESSOA")
	private Long idPessoa;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "NASCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Column(name = "EMAIL")
	private String mail;

	@Column(name = "STATUS")
	private boolean ativo = true;

	@OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;

	@Transient
	private Boolean possuiSenhaTotem;

	@Transient
	private int idade;

	public int getIdade() {
		this.idade = IdadeUtil.calculaIdade(this.dataNascimento);
		return this.idade;
	}

	public void setIdade(final int idade) {
		this.idade = idade;
	}

	/**
	 * @return the idPessoa
	 */
	public Long getIdPessoa() {
		return this.idPessoa;
	}

	/**
	 * @param idPessoa
	 *            the idPessoa to set
	 */
	public void setIdPessoa(final Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento() {
		return this.dataNascimento;
	}

	/**
	 * @param dataNascimento
	 *            the dataNascimento to set
	 */
	public void setDataNascimento(final Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	/**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
	public void setDataCadastro(final Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(final String mail) {
		this.mail = mail;
	}

	/**
	 * @return the possuiSenhaTotem
	 */
	public Boolean isPossuiSenhaTotem() {
		return this.possuiSenhaTotem;
	}

	/**
	 * @param possuiSenhaTotem
	 *            the possuiSenhaTotem to set
	 */
	public void setPossuiSenhaTotem(final Boolean possuiSenhaTotem) {
		this.possuiSenhaTotem = possuiSenhaTotem;
	}

	/**
	 * @return the lancamentos
	 */
	public List<Lancamento> getLancamentos() {
		return this.lancamentos;
	}

	/**
	 * @param lancamentos
	 *            the lancamentos to set
	 */
	public void setLancamentos(final List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 7919;
		int result = 1;
		result = (prime * result)
				+ ((this.idPessoa == null) ? 0 : this.idPessoa.hashCode());
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
		if (!(obj instanceof Pessoa)) {
			return false;
		}
		final Pessoa other = (Pessoa) obj;
		if (this.idPessoa == null) {
			if (other.idPessoa != null) {
				return false;
			}
		} else if (!this.idPessoa.equals(other.idPessoa)) {
			return false;
		}
		return true;
	}

}
