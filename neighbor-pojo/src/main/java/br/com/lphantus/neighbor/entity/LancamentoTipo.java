package br.com.lphantus.neighbor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;

import br.com.lphantus.neighbor.common.LancamentoTipoDTO;

@Entity
@Table(name = "LANCAMENTO_TIPO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SQLDelete(sql = "update LANCAMENTO_TIPO set ATIVO_LANCAMENTO_TIPO = 0 where ID_LANCAMENTO_TIPO=?")
public class LancamentoTipo implements IEntity<Long, LancamentoTipoDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_LANCAMENTO_TIPO")
	private Long id;

	@Column(name = "NOME_LANCAMENTO_TIPO")
	private String nome;

	@Column(name = "DESCRICAO_LANCAMENTO_TIPO")
	private String descricao;

	@Column(name = "ATIVO_LANCAMENTO_TIPO")
	private Boolean ativo = true;

	@Column(name = "EXCLUIDO_LANCAMENTO_TIPO")
	private Boolean excluido = false;

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

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the excluido
	 */
	public Boolean getExcluido() {
		return this.excluido;
	}

	/**
	 * @param excluido
	 *            the excluido to set
	 */
	public void setExcluido(final Boolean excluido) {
		this.excluido = excluido;
	}

	@Override
	public String toString() {
		return "LancamentoTipo [id=" + this.id + ", nome=" + this.nome
				+ ", descricao=" + this.descricao + ", ativo=" + this.ativo
				+ ", excluido=" + this.excluido + "]";
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
		final LancamentoTipo other = (LancamentoTipo) obj;
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
	public LancamentoTipoDTO createDto() {
		return LancamentoTipoDTO.Builder.getInstance().create(this);
	}

}
