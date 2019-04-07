package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.MarcaVeiculo;

public class MarcaVeiculoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String marca;
	private boolean ativo = true;
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
	 * @return the marca
	 */
	public String getMarca() {
		return this.marca;
	}

	/**
	 * @param marca
	 *            the marca to set
	 */
	public void setMarca(final String marca) {
		this.marca = marca;
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
		if (!(obj instanceof MarcaVeiculoDTO)) {
			return false;
		}
		final MarcaVeiculoDTO other = (MarcaVeiculoDTO) obj;
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
		return "MarcaVeiculoDTO [id=" + this.id + ", marca=" + this.marca
				+ ", ativo=" + this.ativo + ", condominio=" + this.condominio
				+ "]";
	}

	public static class Builder extends
			DTOBuilder<MarcaVeiculoDTO, MarcaVeiculo> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public MarcaVeiculoDTO create(final MarcaVeiculo marcaVeiculo) {
			final MarcaVeiculoDTO dto = new MarcaVeiculoDTO();
			dto.setAtivo(marcaVeiculo.isAtivo());
			dto.setId(marcaVeiculo.getId());
			dto.setMarca(marcaVeiculo.getMarca());
			if (marcaVeiculo.getCondominio() != null) {
				dto.setCondominio(marcaVeiculo.getCondominio().createDto());
			}
			return dto;
		}

		@Override
		public MarcaVeiculo createEntity(final MarcaVeiculoDTO outer) {
			final MarcaVeiculo entidade = new MarcaVeiculo();
			entidade.setAtivo(outer.isAtivo());
			entidade.setId(outer.getId());
			entidade.setMarca(outer.getMarca());
			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}
			return entidade;
		}
	}

}
