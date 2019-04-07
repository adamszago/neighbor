package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Telefone;

public class TelefoneDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String numero;
	private String tipoTelefone;
	private MoradorDTO morador;

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
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @return the tipoTelefone
	 */
	public String getTipoTelefone() {
		return tipoTelefone;
	}

	/**
	 * @param tipoTelefone
	 *            the tipoTelefone to set
	 */
	public void setTipoTelefone(String tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(MoradorDTO morador) {
		this.morador = morador;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TelefoneDTO))
			return false;
		TelefoneDTO other = (TelefoneDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelefoneDTO [id=" + id + ", numero=" + numero
				+ ", tipoTelefone=" + tipoTelefone + "]";
	}

	public static class Builder extends DTOBuilder<TelefoneDTO, Telefone> {
		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public TelefoneDTO create(final Telefone telefone) {
			final TelefoneDTO dto = new TelefoneDTO();
			dto.setId(telefone.getId());
			dto.setNumero(telefone.getNumero());
			dto.setTipoTelefone(telefone.getTipoTelefone());
			// dto.setMorador(telefone.getMorador().createDto());

			// if(null != telefone.getMorador()){
			// dto.setMorador(telefone.getMorador().createDto());
			// }
			return dto;
		}

		@Override
		public Telefone createEntity(final TelefoneDTO outer) {
			final Telefone entidade = new Telefone();
			entidade.setId(outer.getId());
			/*
			 * entidade.setMorador(MoradorDTO.Builder.getInstance().createEntity(
			 * outer.getMorador()));
			 */
			entidade.setNumero(outer.getNumero());
			entidade.setTipoTelefone(outer.getTipoTelefone());
			return entidade;
		}
	}

}
