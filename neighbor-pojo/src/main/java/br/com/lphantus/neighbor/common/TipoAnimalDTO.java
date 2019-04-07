package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.TipoAnimal;

public class TipoAnimalDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String tipoAnimal;
	private CondominioDTO condominio;
	private boolean ativo = true;

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
	 * @return the tipoAnimal
	 */
	public String getTipoAnimal() {
		return this.tipoAnimal;
	}

	/**
	 * @param tipoAnimal
	 *            the tipoAnimal to set
	 */
	public void setTipoAnimal(final String tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
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
		if (!(obj instanceof TipoAnimalDTO)) {
			return false;
		}
		final TipoAnimalDTO other = (TipoAnimalDTO) obj;
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
		return "TipoAnimalDTO [id=" + this.id + ", tipoAnimal="
				+ this.tipoAnimal + ", condominio=" + this.condominio
				+ ", ativo=" + this.ativo + "]";
	}

	public static class Builder extends DTOBuilder<TipoAnimalDTO, TipoAnimal> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public TipoAnimalDTO create(final TipoAnimal tipo) {
			final TipoAnimalDTO dto = new TipoAnimalDTO();
			dto.setAtivo(tipo.isAtivo());
			if (null != tipo.getCondominio()) {
				dto.setCondominio(tipo.getCondominio().createDto());
			}
			dto.setId(tipo.getId());
			dto.setTipoAnimal(tipo.getTipoAnimal());
			return dto;
		}

		@Override
		public TipoAnimal createEntity(final TipoAnimalDTO outer) {
			final TipoAnimal entidade = new TipoAnimal();
			entidade.setAtivo(outer.isAtivo());
			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}
			entidade.setId(outer.getId());
			entidade.setTipoAnimal(outer.getTipoAnimal());
			return entidade;
		}
	}

}
