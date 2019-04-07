package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Plano;

public class PlanoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idPlano;
	private String nome;
	private String descricao;

	/**
	 * @return the idPlano
	 */
	public Long getIdPlano() {
		return this.idPlano;
	}

	/**
	 * @param idPlano
	 *            the idPlano to set
	 */
	public void setIdPlano(final Long idPlano) {
		this.idPlano = idPlano;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlanoDTO [idPlano=" + this.idPlano + ", nome=" + this.nome
				+ ", descricao=" + this.descricao + "]";
	}

	public static class Builder extends DTOBuilder<PlanoDTO, Plano> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public PlanoDTO create(final Plano plano) {
			final PlanoDTO dto = new PlanoDTO();
			dto.setDescricao(plano.getDescricao());
			dto.setIdPlano(plano.getIdPlano());
			dto.setNome(plano.getNome());
			return dto;
		}

		@Override
		public Plano createEntity(final PlanoDTO outer) {
			final Plano entidade = new Plano();
			entidade.setDescricao(outer.getDescricao());
			entidade.setIdPlano(outer.getIdPlano());
			entidade.setNome(outer.getNome());
			return entidade;
		}
	}

}
