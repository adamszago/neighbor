package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.ContratoDTO;

@Entity
@Table(name = "CONTRATO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contrato implements IEntity<Long, ContratoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = -7352003124224629255L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CONTRATO")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO")
	private Condominio condominio;

	@Column(name = "LIMITE_USUARIOS")
	private Long limiteUsuarios;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Condominio getCondominio() {
		return this.condominio;
	}

	public void setCondominio(final Condominio condominio) {
		this.condominio = condominio;
	}

	public Long getLimiteUsuarios() {
		return this.limiteUsuarios;
	}

	public void setLimiteUsuarios(final Long limiteUsuarios) {
		this.limiteUsuarios = limiteUsuarios;
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
		final Contrato other = (Contrato) obj;
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
	public ContratoDTO createDto() {
		return ContratoDTO.Builder.getInstance().create(this);
	}

}
