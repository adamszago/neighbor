package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Contrato;

public class ContratoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private CondominioDTO condominio;
	private Long limiteUsuarios;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the limiteUsuarios
	 */
	public Long getLimiteUsuarios() {
		return limiteUsuarios;
	}

	/**
	 * @param limiteUsuarios
	 *            the limiteUsuarios to set
	 */
	public void setLimiteUsuarios(Long limiteUsuarios) {
		this.limiteUsuarios = limiteUsuarios;
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
		result = prime * result
				+ ((condominio == null) ? 0 : condominio.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((limiteUsuarios == null) ? 0 : limiteUsuarios.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ContratoDTO)) {
			return false;
		}
		ContratoDTO other = (ContratoDTO) obj;
		if (condominio == null) {
			if (other.condominio != null) {
				return false;
			}
		} else if (!condominio.equals(other.condominio)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (limiteUsuarios == null) {
			if (other.limiteUsuarios != null) {
				return false;
			}
		} else if (!limiteUsuarios.equals(other.limiteUsuarios)) {
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
		return "ContratoDTO [id=" + id + ", condominio=" + condominio
				+ ", limiteUsuarios=" + limiteUsuarios + "]";
	}

	public static class Builder extends DTOBuilder<ContratoDTO, Contrato> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ContratoDTO create(Contrato contrato) {
			ContratoDTO dto = new ContratoDTO();
			dto.setCondominio(contrato.getCondominio().createDto());
			dto.setId(contrato.getId());
			dto.setLimiteUsuarios(contrato.getLimiteUsuarios());
			return dto;
		}

		@Override
		public Contrato createEntity(ContratoDTO outer) {
			Contrato dto = new Contrato();
			dto.setCondominio(CondominioDTO.Builder.getInstance().createEntity(
					outer.condominio));
			dto.setId(outer.getId());
			dto.setLimiteUsuarios(outer.limiteUsuarios);
			return dto;
		}

	}

}
