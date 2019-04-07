package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.BancoDTO;

@Entity
@Table(name = "BANCO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Banco implements IEntity<Long,BancoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CODIGO_BANCO")
	private Long codigoBanco;

	@Column(name = "NOME_BANCO")
	private String nomeBanco;

	@Column(name = "SIGLA_BANCO")
	private String sigla;

	@Column(name = "DATA_CADASTRO_BANCO")
	private Date datacadastro;

	@Column(name = "BANCO_ATIVO")
	private boolean ativo = true;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "CONDOMINIO_BANCO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	/**
	 * @return the codigoBanco
	 */
	public Long getCodigoBanco() {
		return this.codigoBanco;
	}

	/**
	 * @param codigoBanco
	 *            the codigoBanco to set
	 */
	public void setCodigoBanco(final Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return this.nomeBanco;
	}

	/**
	 * @param nomeBanco
	 *            the nomeBanco to set
	 */
	public void setNomeBanco(final String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return this.sigla;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(final String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the datacadastro
	 */
	public Date getDatacadastro() {
		return this.datacadastro;
	}

	/**
	 * @param datacadastro
	 *            the datacadastro to set
	 */
	public void setDatacadastro(final Date datacadastro) {
		this.datacadastro = datacadastro;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.codigoBanco == null) ? 0 : this.codigoBanco.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Banco other = (Banco) obj;
		if (this.codigoBanco == null) {
			if (other.codigoBanco != null) {
				return false;
			}
		} else if (!this.codigoBanco.equals(other.codigoBanco)) {
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
		return "Banco [codigoBanco=" + this.codigoBanco + ", nomeBanco="
				+ this.nomeBanco + ", sigla=" + this.sigla + ", datacadastro="
				+ this.datacadastro + ", condominio=" + this.condominio
				+ ", ativo=" + this.ativo + "]";
	}

	@Override
	public BancoDTO createDto() {
		return BancoDTO.Builder.getInstance().create(this);
	}

}
