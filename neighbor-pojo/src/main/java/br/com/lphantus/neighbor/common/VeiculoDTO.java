package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.Veiculo;

/**
 * @author Elias
 * 
 */
public class VeiculoDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String placa;
	private MarcaVeiculoDTO marca;
	private String modelo;
	private String cor;
	private MoradorDTO proprietario;
	private boolean ativo;

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
	 * @return the placa
	 */
	public String getPlaca() {
		return this.placa;
	}

	/**
	 * @param placa
	 *            the placa to set
	 */
	public void setPlaca(final String placa) {
		this.placa = placa;
	}

	/**
	 * @return the marca
	 */
	public MarcaVeiculoDTO getMarca() {
		return this.marca;
	}

	/**
	 * @param marca
	 *            the marca to set
	 */
	public void setMarca(final MarcaVeiculoDTO marca) {
		this.marca = marca;
	}

	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return this.modelo;
	}

	/**
	 * @param modelo
	 *            the modelo to set
	 */
	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	/**
	 * @return the cor
	 */
	public String getCor() {
		return this.cor;
	}

	/**
	 * @param cor
	 *            the cor to set
	 */
	public void setCor(final String cor) {
		this.cor = cor;
	}

	/**
	 * @return the proprietario
	 */
	public MoradorDTO getProprietario() {
		return this.proprietario;
	}

	/**
	 * @param proprietario
	 *            the proprietario to set
	 */
	public void setProprietario(final MoradorDTO proprietario) {
		this.proprietario = proprietario;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((getPlaca() == null) ? 0 : getPlaca().hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof VeiculoDTO)) {
			return false;
		}
		final VeiculoDTO other = (VeiculoDTO) obj;
		if (getPlaca() == null) {
			if (other.getPlaca() != null) {
				return false;
			}
		} else if (!getPlaca().equals(other.getPlaca())) {
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
		return "VeiculoDTO [id=" + this.id + ", placa=" + this.placa
				+ ", marca=" + this.marca + ", modelo=" + this.modelo
				+ ", cor=" + this.cor + ", proprietario=" + this.proprietario
				+ ", ativo=" + this.ativo + "]";
	}

	public static class Builder extends DTOBuilder<VeiculoDTO, Veiculo> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param veiculo
		 * @return a Instancia de {@link VeiculoDTO}
		 */
		@Override
		public VeiculoDTO create(final Veiculo veiculo) {
			final VeiculoDTO dto = new VeiculoDTO();
			dto.setAtivo(veiculo.isAtivo());
			dto.setCor(veiculo.getCor());
			dto.setId(veiculo.getId());
			if (null != veiculo.getMarca()) {
				dto.setMarca(veiculo.getMarca().createDto());
			}
			dto.setModelo(veiculo.getModelo());
			dto.setPlaca(veiculo.getPlaca());

			{
				final MoradorDTO morador = new MoradorDTO();
				morador.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(
						veiculo.getProprietario()));
				morador.setUnidadeHabitacional(MoradorUnidadeHabitacionalDTO.Builder
						.getInstance().createList(
								veiculo.getProprietario()
										.getUnidadeHabitacional()));
				for (final MoradorUnidadeHabitacionalDTO item : morador
						.getUnidadeHabitacional()) {
					item.setMorador(morador);
				}
				dto.setProprietario(morador);
			}
			return dto;
		}

		@Override
		public Veiculo createEntity(final VeiculoDTO outer) {
			final Veiculo entidade = new Veiculo();
			entidade.setAtivo(outer.isAtivo());
			entidade.setCor(outer.getCor());
			entidade.setId(outer.getId());
			if (null != outer.getMarca()) {
				entidade.setMarca(MarcaVeiculoDTO.Builder.getInstance()
						.createEntity(outer.getMarca()));
			}
			entidade.setModelo(outer.getModelo());
			entidade.setPlaca(outer.getPlaca());

			final Morador morador = new Morador();
			morador.setIdPessoa(outer.getProprietario().getPessoa()
					.getIdPessoa());
			entidade.setProprietario(morador);
			return entidade;
		}

	}

}
