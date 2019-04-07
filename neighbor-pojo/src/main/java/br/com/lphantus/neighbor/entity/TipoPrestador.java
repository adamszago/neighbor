package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.TipoPrestadorDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TIPO_PRESTADOR")
public class TipoPrestador implements IEntity<Long,TipoPrestadorDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TIPO_PRESTADOR")
	private Long id;

	@Column(name = "TIPO_PRESTADOR")
	private String tipoPrestador;

	@Column(name = "ATIVO")
	private boolean ativo = true;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the tipoPrestador
	 */
	public String getTipoPrestador() {
		return tipoPrestador;
	}

	/**
	 * @param tipoPrestador the tipoPrestador to set
	 */
	public void setTipoPrestador(String tipoPrestador) {
		this.tipoPrestador = tipoPrestador;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
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
		final TipoPrestador other = (TipoPrestador) obj;
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
	public TipoPrestadorDTO createDto() {
		return TipoPrestadorDTO.Builder.getInstance().create(this);
	}

}
