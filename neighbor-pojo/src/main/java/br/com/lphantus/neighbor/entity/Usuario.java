package br.com.lphantus.neighbor.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "USUARIO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_PESSOA_FISICA")
public class Usuario extends PessoaFisica implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Column(name = "LOGIN")
	private String login;

	@Column(name = "SENHA")
	private String senha;

	@Column(name = "ROOT")
	private boolean root;

	@Column(name = "SINDICO")
	private boolean sindico;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_MODULO_ACESSO", referencedColumnName = "ID_MODULO_ACESSO"))
	private ModuloAcesso moduloAcesso;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_PLANO", referencedColumnName = "ID_PLANO"))
	private Plano plano;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	/**
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(final String login) {
		this.login = login;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return this.senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return the root
	 */
	public boolean isRoot() {
		return this.root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(final boolean root) {
		this.root = root;
	}

	/**
	 * @return the sindico
	 */
	public boolean isSindico() {
		return this.sindico;
	}

	/**
	 * @param sindico
	 *            the sindico to set
	 */
	public void setSindico(final boolean sindico) {
		this.sindico = sindico;
	}

	/**
	 * @return the moduloAcesso
	 */
	public ModuloAcesso getModuloAcesso() {
		return this.moduloAcesso;
	}

	/**
	 * @param moduloAcesso
	 *            the moduloAcesso to set
	 */
	public void setModuloAcesso(final ModuloAcesso moduloAcesso) {
		this.moduloAcesso = moduloAcesso;
	}

	/**
	 * @return the plano
	 */
	public Plano getPlano() {
		return this.plano;
	}

	/**
	 * @param plano
	 *            the plano to set
	 */
	public void setPlano(final Plano plano) {
		this.plano = plano;
	}

	/**
	 * @return the condominio
	 */
	public Condominio getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final Condominio condominio) {
		this.condominio = condominio;
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
				+ ((this.getIdPessoa() == null) ? 0 : this.getIdPessoa()
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
		if (!(obj instanceof Usuario)) {
			return false;
		}
		final Usuario other = (Usuario) obj;
		if (this.getIdPessoa() == null) {
			if (other.getIdPessoa() != null) {
				return false;
			}
		} else if (!this.getIdPessoa().equals(other.getIdPessoa())) {
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
		return "Usuario [login=" + this.login + ", senha=" + this.senha
				+ ", root=" + this.root + ", sindico=" + this.sindico
				+ ", moduloAcesso=" + this.moduloAcesso + ", plano="
				+ this.plano + ", condominio=" + this.condominio + "]";
	}

}
