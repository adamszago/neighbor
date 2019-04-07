package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;

import br.com.lphantus.neighbor.entity.ItemReserva;

public class ItemReservaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private BigDecimal preco;
	private int carenciaDiasReserva;
	private boolean ativo;
	private boolean necessitaPagamento = false;
	private boolean necessitaAprovar = false;
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
	 * @return the preco
	 */
	public BigDecimal getPreco() {
		return this.preco;
	}

	/**
	 * @param preco
	 *            the preco to set
	 */
	public void setPreco(final BigDecimal preco) {
		this.preco = preco;
	}

	/**
	 * @return the carenciaDiasReserva
	 */
	public int getCarenciaDiasReserva() {
		return this.carenciaDiasReserva;
	}

	/**
	 * @param carenciaDiasReserva
	 *            the carenciaDiasReserva to set
	 */
	public void setCarenciaDiasReserva(final int carenciaDiasReserva) {
		this.carenciaDiasReserva = carenciaDiasReserva;
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

	/**
	 * @return the necessitaPagamento
	 */
	public boolean isNecessitaPagamento() {
		return this.necessitaPagamento;
	}

	/**
	 * @param necessitaPagamento
	 *            the necessitaPagamento to set
	 */
	public void setNecessitaPagamento(final boolean necessitaPagamento) {
		this.necessitaPagamento = necessitaPagamento;
	}

	/**
	 * @return the necessitaAprovar
	 */
	public boolean isNecessitaAprovar() {
		return necessitaAprovar;
	}

	/**
	 * @param necessitaAprovar
	 *            the necessitaAprovar to set
	 */
	public void setNecessitaAprovar(final boolean necessitaAprovar) {
		this.necessitaAprovar = necessitaAprovar;
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
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof ItemReservaDTO)) {
			return false;
		}
		final ItemReservaDTO other = (ItemReservaDTO) obj;
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
		return "ItemReservaDTO [id=" + this.id + ", nome=" + this.nome + ", preco=" + this.preco + ", carenciaDiasReserva=" + this.carenciaDiasReserva + ", ativo=" + this.ativo
				+ ", necessitaPagamento=" + this.necessitaPagamento + ", condominio=" + this.condominio + "]";
	}

	public static class Builder extends DTOBuilder<ItemReservaDTO, ItemReserva> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ItemReservaDTO create(final ItemReserva itemReserva) {
			final ItemReservaDTO dto = new ItemReservaDTO();

			dto.setAtivo(itemReserva.isAtivo());
			dto.setCarenciaDiasReserva(itemReserva.getCarenciaDiasReserva());

			if (null != itemReserva.getCondominio()) {
				dto.setCondominio(itemReserva.getCondominio().createDto());
			}

			dto.setId(itemReserva.getId());
			dto.setNecessitaPagamento(itemReserva.isNecessitaPagamento());
			dto.setNecessitaAprovar(itemReserva.isNecessitaAprovar());
			dto.setNome(itemReserva.getNome());
			dto.setPreco(itemReserva.getPreco());

			return dto;
		}

		@Override
		public ItemReserva createEntity(final ItemReservaDTO outer) {
			final ItemReserva entidade = new ItemReserva();
			entidade.setAtivo(outer.isAtivo());
			entidade.setCarenciaDiasReserva(outer.getCarenciaDiasReserva());
			entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			entidade.setId(outer.getId());
			entidade.setNecessitaPagamento(outer.isNecessitaPagamento());
			entidade.setNecessitaAprovar(outer.isNecessitaAprovar());
			entidade.setNome(outer.getNome());
			entidade.setPreco(outer.getPreco());
			return entidade;
		}
	}

}
