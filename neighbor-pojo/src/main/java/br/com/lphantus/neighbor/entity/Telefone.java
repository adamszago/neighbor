package br.com.lphantus.neighbor.entity;

import javax.persistence.CascadeType;
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

import br.com.lphantus.neighbor.common.TelefoneDTO;

/**
 * @author Adams Zago Classe para telefones de moradores, visitantes e
 *         prestadores de servico
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TELEFONE")
public class Telefone implements IEntity<Long,TelefoneDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TELEFONE")
	private Long id;

	@Column(name = "NUMERO")
	private String numero;

	@Column(name = "TIPO_TELEFONE")
	private String tipoTelefone;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_DONO", referencedColumnName = "ID_MORADOR"))
	private Morador morador;

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
	 * @return the numero
	 */
	public String getNumero() {
		return this.numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(final String numero) {
		this.numero = numero;
	}

	/**
	 * @return the tipoTelefone
	 */
	public String getTipoTelefone() {
		return this.tipoTelefone;
	}

	/**
	 * @param tipoTelefone
	 *            the tipoTelefone to set
	 */
	public void setTipoTelefone(final String tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final Morador morador) {
		this.morador = morador;
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
		final Telefone other = (Telefone) obj;
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
	public TelefoneDTO createDto() {
		return TelefoneDTO.Builder.getInstance().create(this);
	}

}
