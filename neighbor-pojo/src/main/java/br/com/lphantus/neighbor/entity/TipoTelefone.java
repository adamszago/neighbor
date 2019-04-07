package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.TipoTelefoneDTO;

/**
 * @author Adams Zago
 * 
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TIPO_TELEFONE")
public class TipoTelefone implements IEntity<Long, TipoTelefoneDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TIPO_TELEFONE")
	private Long id;

	@Column(name = "TIPO_TELEFONE")
	private String tipo_telefone;

	@Column(name = "ATIVO")
	private boolean ativo = true;

	public boolean isAtivo() {
		return this.ativo;
	}

	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTipo_telefone() {
		return this.tipo_telefone;
	}

	public void setTipo_telefone(final String tipo_telefone) {
		this.tipo_telefone = tipo_telefone;
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
		final TipoTelefone other = (TipoTelefone) obj;
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
	public TipoTelefoneDTO createDto() {
		return TipoTelefoneDTO.Builder.getInstance().create(this);
	}

}
