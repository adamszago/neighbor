package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorAgregadoPK;

public class MoradorAgregadoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private MoradorDTO morador;
	private AgregadoDTO agregado;
	private String parentesco;
	private Date dataInicio;
	private Date dataFim;

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the agregado
	 */
	public AgregadoDTO getAgregado() {
		return this.agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final AgregadoDTO agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the parentesco
	 */
	public String getParentesco() {
		return this.parentesco;
	}

	/**
	 * @param parentesco
	 *            the parentesco to set
	 */
	public void setParentesco(final String parentesco) {
		this.parentesco = parentesco;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return this.dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return this.dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
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
		result = (prime * result) + ((this.agregado == null) ? 0 : this.agregado.hashCode());
		result = (prime * result) + ((this.morador == null) ? 0 : this.morador.hashCode());
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
		if (!(obj instanceof MoradorAgregadoDTO)) {
			return false;
		}
		final MoradorAgregadoDTO other = (MoradorAgregadoDTO) obj;
		if (this.agregado == null) {
			if (other.agregado != null) {
				return false;
			}
		} else if (!this.agregado.equals(other.agregado)) {
			return false;
		}
		if (this.morador == null) {
			if (other.morador != null) {
				return false;
			}
		} else if (!this.morador.equals(other.morador)) {
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
		return "MoradorAgregadoDTO [morador=" + this.morador + ", agregado=" + this.agregado + ", parentesco=" + this.parentesco + ", dataInicio=" + this.dataInicio + ", dataFim="
				+ this.dataFim + "]";
	}

	public static class Builder extends DTOBuilder<MoradorAgregadoDTO, MoradorAgregado> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public MoradorAgregadoDTO create(final MoradorAgregado entity) {
			final MoradorAgregadoDTO retorno = new MoradorAgregadoDTO();

			retorno.setDataFim(entity.getDataFim());
			retorno.setDataInicio(entity.getDataInicio());
			retorno.setParentesco(entity.getParentesco());
			retorno.setAgregado(AgregadoDTO.Builder.getInstance().create(entity.getAgregado()));
			// TODO: revisar
			// retorno.setMorador(MoradorDTO.Builder.getInstance().createList(entity.getMorador()));

			return retorno;
		}

		@Override
		public MoradorAgregado createEntity(final MoradorAgregadoDTO outer) {
			final MoradorAgregado retorno = new MoradorAgregado();
			retorno.setChaveComposta(new MoradorAgregadoPK());
			retorno.getChaveComposta().setIdAgregado(outer.getAgregado().getPessoa().getIdPessoa());
			retorno.getChaveComposta().setIdMorador(outer.getMorador().getPessoa().getIdPessoa());
			retorno.setDataFim(outer.getDataFim());
			retorno.setDataInicio(outer.getDataInicio());
			retorno.setParentesco(outer.getParentesco());
			// TODO: revisar
			// retorno.setAgregado(AgregadoDTO.Builder.getInstance()
			// .createListEntity(outer.getAgregado()));
			// retorno.setMorador(MoradorDTO.Builder.getInstance().createListEntity(outer.getMorador()));

			return retorno;
		}

	}

}
