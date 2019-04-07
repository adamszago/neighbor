package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;

public class MoradorUnidadeHabitacionalDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private MoradorDTO morador;
	private UnidadeHabitacionalDTO unidadeHabitacional;
	private Date dataInicio;
	private Date dataFim;
	private boolean adimplente = true;
	private String tipoMorador;

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
	 * @return the unidadeHabitacional
	 */
	public UnidadeHabitacionalDTO getUnidadeHabitacional() {
		return this.unidadeHabitacional;
	}

	/**
	 * @param unidadeHabitacional
	 *            the unidadeHabitacional to set
	 */
	public void setUnidadeHabitacional(
			final UnidadeHabitacionalDTO unidadeHabitacional) {
		this.unidadeHabitacional = unidadeHabitacional;
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

	/**
	 * @return the tipoMorador
	 */
	public String getTipoMorador() {
		return this.tipoMorador;
	}

	/**
	 * @param tipoMorador
	 *            the tipoMorador to set
	 */
	public void setTipoMorador(final String tipoMorador) {
		this.tipoMorador = tipoMorador;
	}

	/**
	 * @return the adimplente
	 */
	public boolean isAdimplente() {
		return this.adimplente;
	}

	/**
	 * 
	 * @param adimplente
	 *            the adimplente to set
	 */
	public void setAdimplente(final boolean adimplente) {
		this.adimplente = adimplente;
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
				+ ((this.morador == null) ? 0 : this.morador.hashCode());
		result = (prime * result)
				+ ((this.unidadeHabitacional == null) ? 0
						: this.unidadeHabitacional.hashCode());
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
		if (!(obj instanceof MoradorUnidadeHabitacionalDTO)) {
			return false;
		}
		final MoradorUnidadeHabitacionalDTO other = (MoradorUnidadeHabitacionalDTO) obj;
		if (this.morador == null) {
			if (other.morador != null) {
				return false;
			}
		} else if (!this.morador.equals(other.morador)) {
			return false;
		}
		if (this.unidadeHabitacional == null) {
			if (other.unidadeHabitacional != null) {
				return false;
			}
		} else if (!this.unidadeHabitacional.equals(other.unidadeHabitacional)) {
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
		return "MoradorUnidadeHabitacionalDTO [unidadeHabitacional="
				+ this.unidadeHabitacional + ", dataInicio=" + this.dataInicio
				+ ", dataFim=" + this.dataFim + ", adimplente="
				+ this.adimplente + ", tipoMorador=" + this.tipoMorador + "]";
	}

	public static class Builder
			extends
			DTOBuilder<MoradorUnidadeHabitacionalDTO, MoradorUnidadeHabitacional> {

		private static Builder instance;

		private Builder() {

		}

		public static final Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public MoradorUnidadeHabitacionalDTO create(
				final MoradorUnidadeHabitacional entity) {
			final MoradorUnidadeHabitacionalDTO retorno = new MoradorUnidadeHabitacionalDTO();

			retorno.setDataFim(entity.getDataFim());
			retorno.setDataInicio(entity.getDataInicio());
			retorno.setTipoMorador(entity.getTipoMorador());

			if (null != entity.getUnidadeHabitacional()) {
				retorno.setUnidadeHabitacional(entity.getUnidadeHabitacional()
						.createDto());
			} else {
				System.out.println("Unidade do relacionamento eh nula.");
			}

			// TODO: revisar
			// retorno.setMorador(MoradorUnidadeHabitacionalDTO.Builder.getInstance().createList(entity.getMorador()));

			return retorno;
		}

		/**
		 * Este gera tambem o morador.
		 * 
		 * @param entity
		 * @return
		 */
		public MoradorUnidadeHabitacionalDTO createFromEntity(
				final MoradorUnidadeHabitacional entity) {
			final MoradorUnidadeHabitacionalDTO retorno = new MoradorUnidadeHabitacionalDTO();

			retorno.setDataFim(entity.getDataFim());
			retorno.setDataInicio(entity.getDataInicio());
			retorno.setTipoMorador(entity.getTipoMorador());
			if (null != entity.getUnidadeHabitacional()) {
				retorno.setUnidadeHabitacional(UnidadeHabitacionalDTO.Builder
						.getInstance().create(entity.getUnidadeHabitacional()));
			}

			final Morador morador = entity.getMorador();
			final MoradorDTO dto = new MoradorDTO();
			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(morador));

			dto.setAgregados(MoradorAgregadoDTO.Builder.getInstance()
					.createList(morador.getAgregados()));
			dto.setAnimaisEstimacao(AnimalEstimacaoDTO.Builder.getInstance()
					.createList(morador.getAnimaisEstimacao()));
			dto.setVeiculos(VeiculoDTO.Builder.getInstance().createList(
					morador.getVeiculos()));
			dto.setTelefones(TelefoneDTO.Builder.getInstance().createList(
					morador.getTelefones()));
			retorno.setMorador(dto);

			return retorno;
		}

		@Override
		public MoradorUnidadeHabitacional createEntity(
				final MoradorUnidadeHabitacionalDTO outer) {
			final MoradorUnidadeHabitacional retorno = new MoradorUnidadeHabitacional();

			retorno.setDataFim(outer.getDataFim());
			retorno.setDataInicio(outer.getDataInicio());
			retorno.setTipoMorador(outer.getTipoMorador());
			if (null != outer.getUnidadeHabitacional()) {
				retorno.setUnidadeHabitacional(UnidadeHabitacionalDTO.Builder
						.getInstance().createEntity(
								outer.getUnidadeHabitacional()));
			}
			if (null != outer.getMorador()) {
				retorno.setMorador(PessoaFisicaDTO.Builder.getInstance()
						.createEntityMorador(outer.getMorador().getPessoa()));
			}

			return retorno;
		}

	}

}
