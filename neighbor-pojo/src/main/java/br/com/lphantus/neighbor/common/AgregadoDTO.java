package br.com.lphantus.neighbor.common;

import java.util.List;

import br.com.lphantus.neighbor.entity.Agregado;

public class AgregadoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private List<MoradorAgregadoDTO> morador;
	private PessoaFisicaDTO pessoa;

	public AgregadoDTO() {
		pessoa = new PessoaFisicaDTO();
	}

	/**
	 * @return the morador
	 */
	public List<MoradorAgregadoDTO> getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final List<MoradorAgregadoDTO> morador) {
		this.morador = morador;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaFisicaDTO getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaFisicaDTO pessoa) {
		this.pessoa = pessoa;
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
		result = (prime * result) + ((pessoa == null) ? 0 : pessoa.hashCode());
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
		if (!(obj instanceof AgregadoDTO)) {
			return false;
		}
		final AgregadoDTO other = (AgregadoDTO) obj;
		if (pessoa == null) {
			if (other.pessoa != null) {
				return false;
			}
		} else if (!pessoa.equals(other.pessoa)) {
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
		return "AgregadoDTO [morador=" + morador + ", pessoa=" + pessoa + "]";
	}

	public static class Builder extends DTOBuilder<AgregadoDTO, Agregado> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public AgregadoDTO create(final Agregado agregado) {

			final AgregadoDTO dto = new AgregadoDTO();

			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance()
					.create(agregado));
			// dto.setMorador(morador)

			return dto;
		}

		@Override
		public Agregado createEntity(final AgregadoDTO outer) {

			Agregado entidade = PessoaFisicaDTO.Builder.getInstance()
					.createEntityAgregado(outer.getPessoa());

			// TODO: revisar
			// entidade.setMorador(morador)

			return entidade;
		}
	}

}
