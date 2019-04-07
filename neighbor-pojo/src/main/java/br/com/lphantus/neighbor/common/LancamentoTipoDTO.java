package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.LancamentoTipo;

public class LancamentoTipoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String descricao;
	private Boolean ativo;
	private Boolean excluido;

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

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the excluido
	 */
	public Boolean getExcluido() {
		return this.excluido;
	}

	/**
	 * @param excluido
	 *            the excluido to set
	 */
	public void setExcluido(final Boolean excluido) {
		this.excluido = excluido;
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
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof LancamentoTipoDTO)) {
			return false;
		}
		final LancamentoTipoDTO other = (LancamentoTipoDTO) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public static class Builder extends
			DTOBuilder<LancamentoTipoDTO, LancamentoTipo> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public LancamentoTipoDTO create(final LancamentoTipo lancamentoTipo) {
			final LancamentoTipoDTO dto = new LancamentoTipoDTO();
			dto.setAtivo(lancamentoTipo.getAtivo());
			dto.setDescricao(lancamentoTipo.getDescricao());
			dto.setExcluido(lancamentoTipo.getExcluido());
			dto.setId(lancamentoTipo.getId());
			dto.setNome(lancamentoTipo.getNome());
			return dto;
		}

		@Override
		public LancamentoTipo createEntity(final LancamentoTipoDTO outer) {
			final LancamentoTipo entidade = new LancamentoTipo();
			entidade.setAtivo(outer.getAtivo());
			entidade.setDescricao(outer.getDescricao());
			entidade.setExcluido(outer.getExcluido());
			entidade.setId(outer.getId());
			entidade.setNome(outer.getNome());
			return entidade;
		}
	}

}
