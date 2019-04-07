package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.TipoTelefone;

public class TipoTelefoneDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String tipo_telefone;
	private boolean ativo;

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
	 * @return the tipo_telefone
	 */
	public String getTipo_telefone() {
		return tipo_telefone;
	}

	/**
	 * @param tipo_telefone
	 *            the tipo_telefone to set
	 */
	public void setTipo_telefone(String tipo_telefone) {
		this.tipo_telefone = tipo_telefone;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TipoTelefoneDTO))
			return false;
		TipoTelefoneDTO other = (TipoTelefoneDTO) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TipoTelefoneDTO [ Id=" + getId() + "]";
	}

	public static class Builder extends
			DTOBuilder<TipoTelefoneDTO, TipoTelefone> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param tipoTelefone
		 * @return a Instancia de {@link TipoTelefoneDTO}
		 */
		public TipoTelefoneDTO create(TipoTelefone tipoTelefone) {
			TipoTelefoneDTO dto = new TipoTelefoneDTO();
			dto.setAtivo(tipoTelefone.isAtivo());
			dto.setId(tipoTelefone.getId());
			dto.setTipo_telefone(tipoTelefone.getTipo_telefone());
			return dto;
		}

		@Override
		public TipoTelefone createEntity(TipoTelefoneDTO outer) {
			TipoTelefone entidade = new TipoTelefone();
			entidade.setAtivo(outer.isAtivo());
			entidade.setId(outer.getId());
			entidade.setTipo_telefone(outer.getTipo_telefone());
			return entidade;
		}

	}

}
