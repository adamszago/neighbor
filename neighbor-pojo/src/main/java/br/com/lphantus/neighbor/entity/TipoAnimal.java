package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.TipoAnimalDTO;

/**
 * @author Adams Zago
 * 
 */
@Entity
@Table(name = "TIPO_ANIMAL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoAnimal implements IEntity<Long, TipoAnimalDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TIPO_ANIMAL")
	private Long id;

	@Column(name = "TIPO_ANIMAL")
	private String tipoAnimal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO") })
	private Condominio condominio;

	@Column(name = "ATIVO")
	private boolean ativo = true;

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
	 * @return the tipoAnimal
	 */
	public String getTipoAnimal() {
		return this.tipoAnimal;
	}

	/**
	 * @param tipoAnimal
	 *            the tipoAnimal to set
	 */
	public void setTipoAnimal(final String tipoAnimal) {
		if (null == tipoAnimal) {
			this.tipoAnimal = tipoAnimal;
		} else {
			this.tipoAnimal = tipoAnimal.toUpperCase();
		}
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
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		final TipoAnimal other = (TipoAnimal) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public TipoAnimalDTO createDto() {
		return TipoAnimalDTO.Builder.getInstance().create(this);
	}

}
