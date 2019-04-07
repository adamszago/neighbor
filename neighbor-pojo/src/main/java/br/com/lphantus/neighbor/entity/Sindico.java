package br.com.lphantus.neighbor.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.SindicoDTO;

@Entity
@Table(name = "SINDICO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sindico implements IEntity<Long, SindicoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 5499184523530257217L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SINDICO")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA"))
	private Pessoa pessoa;

	@OneToMany
	private List<SindicoCondominio> condominio;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the condominio
	 */
	public List<SindicoCondominio> getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final List<SindicoCondominio> condominio) {
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
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof Sindico)) {
			return false;
		}
		final Sindico other = (Sindico) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
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
		return "Sindico [id=" + this.id + ", pessoa=" + this.pessoa
				+ ", condominio=" + this.condominio + "]";
	}

	@Override
	public SindicoDTO createDto() {
		return SindicoDTO.Builder.getInstance().create(this);
	}

}
