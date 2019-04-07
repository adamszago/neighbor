package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.SequenciaFoto;

public class SequenciaFotoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;

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

	public static class Builder extends
			DTOBuilder<SequenciaFotoDTO, SequenciaFoto> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public SequenciaFotoDTO create(SequenciaFoto sequenciaFoto) {
			SequenciaFotoDTO dto = new SequenciaFotoDTO();
			dto.setId(sequenciaFoto.getId());
			return dto;
		}

		@Override
		public SequenciaFoto createEntity(SequenciaFotoDTO outer) {
			SequenciaFoto entidade = new SequenciaFoto();
			entidade.setId(outer.getId());
			return entidade;
		}
	}

}
