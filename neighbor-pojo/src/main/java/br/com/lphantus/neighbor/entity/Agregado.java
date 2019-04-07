package br.com.lphantus.neighbor.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.AgregadoDTO;

@Entity
@Table(name = "AGREGADO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumns(@PrimaryKeyJoinColumn(name = "ID_AGREGADO", referencedColumnName = "ID_PESSOA_FISICA"))
public class Agregado extends PessoaFisica implements
		IEntity<Long, AgregadoDTO> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "agregado", cascade = CascadeType.ALL)
	private List<MoradorAgregado> morador;

	/**
	 * @return the morador
	 */
	public List<MoradorAgregado> getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final List<MoradorAgregado> morador) {
		this.morador = morador;
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
		if (!(obj instanceof Agregado)) {
			return false;
		}
		final Agregado other = (Agregado) obj;
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
		return "Agregado [morador=" + this.morador + "]";
	}

	@Override
	public AgregadoDTO createDto() {
		return AgregadoDTO.Builder.getInstance().create(this);
	}

}
