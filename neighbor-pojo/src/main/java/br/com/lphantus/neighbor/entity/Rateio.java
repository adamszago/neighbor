package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.RateioDTO;

@Entity
@Table(name = "RATEIO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rateio implements IEntity<Long, RateioDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_RATEIO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "VALOR_RATEIO")
	private BigDecimal valor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CENTRO_CUSTO")
	private CentroCusto centroCusto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MOVIMENTACAO")
	private Movimentacao movimentacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO")
	private Condominio condominio;

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
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return this.valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the centroCusto
	 */
	public CentroCusto getCentroCusto() {
		return this.centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(final CentroCusto centroCusto) {
		this.centroCusto = centroCusto;
	}

	/**
	 * @return the movimentacao
	 */
	public Movimentacao getMovimentacao() {
		return this.movimentacao;
	}

	/**
	 * @param movimentacao
	 *            the movimentacao to set
	 */
	public void setMovimentacao(final Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
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
		if (!(obj instanceof Rateio)) {
			return false;
		}
		final Rateio other = (Rateio) obj;
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
		return "Rateio [id=" + this.id + ", valor=" + this.valor
				+ ", centroCusto=" + this.centroCusto + ", movimentacao="
				+ this.movimentacao + ", condominio=" + this.condominio + "]";
	}

	@Override
	public RateioDTO createDto() {
		return RateioDTO.Builder.getInstance().create(this);
	}

}