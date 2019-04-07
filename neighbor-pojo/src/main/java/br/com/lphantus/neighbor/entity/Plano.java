package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.PlanoDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "PLANO")
public class Plano implements IEntity<Long,PlanoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PLANO")
	private Long idPlano;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "DESCRICAO")
	private String descricao;

	/**
	 * @return the idPlano
	 */
	public Long getIdPlano() {
		return this.idPlano;
	}

	/**
	 * @param idPlano
	 *            the idPlano to set
	 */
	public void setIdPlano(final Long idPlano) {
		this.idPlano = idPlano;
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
	 * @return the descricao
	 */
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.idPlano == null) ? 0 : this.idPlano.hashCode());
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
		final Plano other = (Plano) obj;
		if (this.idPlano == null) {
			if (other.idPlano != null) {
				return false;
			}
		} else if (!this.idPlano.equals(other.idPlano)) {
			return false;
		}
		return true;
	}

	@Override
	public PlanoDTO createDto() {
		return PlanoDTO.Builder.getInstance().create(this);
	}

}
