package br.com.lphantus.neighbor.common;

import java.util.List;

import br.com.lphantus.neighbor.entity.UnidadeHabitacional;

/**
 * @author Elias
 * 
 */
public class UnidadeHabitacionalDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long idUnidade;
	private String identificacao;
	private boolean interfoneOk;
	private List<MoradorUnidadeHabitacionalDTO> moradores;
	private CondominioDTO condominio;
	private boolean ativo = true;
	private EnderecoDTO endereco;
	private Long numero;
	private String complemento;

	/**
	 * @return the idUnidade
	 */
	public Long getIdUnidade() {
		return this.idUnidade;
	}

	/**
	 * @param idUnidade
	 *            the idUnidade to set
	 */
	public void setIdUnidade(final Long idUnidade) {
		this.idUnidade = idUnidade;
	}

	/**
	 * @return the identificacao
	 */
	public String getIdentificacao() {
		return this.identificacao;
	}

	/**
	 * @param identificacao
	 *            the identificacao to set
	 */
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @return the moradores
	 */
	public List<MoradorUnidadeHabitacionalDTO> getMoradores() {
		return this.moradores;
	}

	/**
	 * @param moradores
	 *            the moradores to set
	 */
	public void setMoradores(final List<MoradorUnidadeHabitacionalDTO> moradores) {
		this.moradores = moradores;
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

	/**
	 * @return the interfoneOk
	 */
	public boolean isInterfoneOk() {
		return this.interfoneOk;
	}

	/**
	 * @param interfoneOk
	 *            the interfoneOk to set
	 */
	public void setInterfoneOk(final boolean interfoneOk) {
		this.interfoneOk = interfoneOk;
	}

	/**
	 * @return the endereco
	 */
	public EnderecoDTO getEndereco() {
		if (null == this.endereco) {
			this.endereco = new EnderecoDTO();
		}
		return this.endereco;
	}

	/**
	 * @param endereco
	 *            the endereco to set
	 */
	public void setEndereco(final EnderecoDTO endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the numero
	 */
	public Long getNumero() {
		return this.numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(final Long numero) {
		this.numero = numero;
	}

	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		return this.complemento;
	}

	/**
	 * @param complemento
	 *            the complemento to set
	 */
	public void setComplemento(final String complemento) {
		this.complemento = complemento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((getIdUnidade() == null) ? 0 : getIdUnidade().hashCode());
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
		if (!(obj instanceof UnidadeHabitacionalDTO)) {
			return false;
		}
		final UnidadeHabitacionalDTO other = (UnidadeHabitacionalDTO) obj;
		if (getIdUnidade() == null) {
			if (other.getIdUnidade() != null) {
				return false;
			}
		} else if (!getIdUnidade().equals(other.getIdUnidade())) {
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
		return "UnidadeHabitacionalDTO [idUnidade=" + this.idUnidade
				+ ", identificacao=" + this.identificacao + ", interfoneOk="
				+ this.interfoneOk + ", moradores=" + this.moradores
				+ ", condominio=" + this.condominio + ", ativo=" + this.ativo
				+ ", endereco=" + this.endereco + ", numero=" + this.numero
				+ ", complemento=" + this.complemento + "]";
	}

	public static class Builder extends
			DTOBuilder<UnidadeHabitacionalDTO, UnidadeHabitacional> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param entity
		 * @return a Instancia de {@link UnidadeHabitacionalDTO}
		 */
		@Override
		public UnidadeHabitacionalDTO create(final UnidadeHabitacional entity) {
			final UnidadeHabitacionalDTO dto = new UnidadeHabitacionalDTO();
			dto.setAtivo(entity.isAtivo());

			if (null != entity.getCondominio()) {
				dto.setCondominio(entity.getCondominio().createDto());
			}

			dto.setIdUnidade(entity.getIdUnidade());
			dto.setIdentificacao(entity.getIdentificacao());
			dto.setInterfoneOk(entity.isInterfoneOk());

			// TODO: revisar
			// dto.setMoradores(MoradorUnidadeHabitacionalDTO.Builder
			// .getInstance().createList(
			// unidadeHabitacional.getMoradores()));

			if (null != entity.getEndereco()) {
				dto.setEndereco(entity.getEndereco().createDto());
			}

			dto.setNumero(entity.getNumero());
			dto.setComplemento(entity.getComplemento());

			return dto;
		}

		@Override
		public UnidadeHabitacional createEntity(
				final UnidadeHabitacionalDTO outer) {
			final UnidadeHabitacional entidade = new UnidadeHabitacional();
			entidade.setAtivo(outer.isAtivo());

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			entidade.setIdUnidade(outer.getIdUnidade());
			entidade.setIdentificacao(outer.getIdentificacao());
			entidade.setInterfoneOk(outer.isInterfoneOk());

			entidade.setMoradores(MoradorUnidadeHabitacionalDTO.Builder
					.getInstance().createListEntity(outer.getMoradores()));

			if (null != outer.getEndereco()) {
				entidade.setEndereco(EnderecoDTO.Builder.getInstance()
						.createEntity(outer.getEndereco()));
			}

			entidade.setNumero(outer.getNumero());
			entidade.setComplemento(outer.getComplemento());

			return entidade;
		}

	}

}
