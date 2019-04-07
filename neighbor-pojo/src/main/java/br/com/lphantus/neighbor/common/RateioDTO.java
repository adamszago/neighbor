package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;

import br.com.lphantus.neighbor.entity.Rateio;

public class RateioDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private BigDecimal valor;
	private CentroCustoDTO centroCusto;
	private MovimentacaoDTO movimentacao;
	private CondominioDTO condominio;

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
	public CentroCustoDTO getCentroCusto() {
		return this.centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(final CentroCustoDTO centroCusto) {
		this.centroCusto = centroCusto;
	}

	/**
	 * @return the movimentacao
	 */
	public MovimentacaoDTO getMovimentacao() {
		return this.movimentacao;
	}

	/**
	 * @param movimentacao
	 *            the movimentacao to set
	 */
	public void setMovimentacao(final MovimentacaoDTO movimentacao) {
		this.movimentacao = movimentacao;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
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
		if (!(obj instanceof RateioDTO)) {
			return false;
		}
		final RateioDTO other = (RateioDTO) obj;
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
		return "RateioDTO [id=" + this.id + ", valor=" + this.valor
				+ ", centroCusto=" + this.centroCusto + ", movimentacao="
				+ this.movimentacao + ", condominio=" + this.condominio + "]";
	}

	public static class Builder extends DTOBuilder<RateioDTO, Rateio> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public RateioDTO create(final Rateio rateio) {
			final RateioDTO dto = new RateioDTO();

			if (null != rateio.getCentroCusto()) {
				dto.setCentroCusto(rateio.getCentroCusto().createDto());
			}
			if (null != rateio.getCondominio()) {
				dto.setCondominio(rateio.getCondominio().createDto());
			}
			dto.setId(rateio.getId());
			if (null != rateio.getMovimentacao()) {
				dto.setMovimentacao(rateio.getMovimentacao().createDto());
			}
			dto.setValor(rateio.getValor());
			return dto;
		}

		@Override
		public Rateio createEntity(final RateioDTO outer) {
			final Rateio entidade = new Rateio();
			if (null != outer.getCentroCusto()) {
				entidade.setCentroCusto(CentroCustoDTO.Builder.getInstance()
						.createEntity(outer.getCentroCusto()));
			}
			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}
			entidade.setId(outer.getId());
			if (null != outer.getMovimentacao()) {
				entidade.setMovimentacao(MovimentacaoDTO.Builder.getInstance()
						.createEntity(outer.getMovimentacao()));
			}
			entidade.setValor(outer.getValor());
			return entidade;
		}
	}

}
