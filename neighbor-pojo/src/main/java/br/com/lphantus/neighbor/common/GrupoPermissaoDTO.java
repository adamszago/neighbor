package br.com.lphantus.neighbor.common;

import java.util.List;

import br.com.lphantus.neighbor.entity.GrupoPermissao;

public class GrupoPermissaoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idGrupo;
	private String nome;
	private List<PermissaoDTO> permissoes;

	/**
	 * @return the idGrupo
	 */
	public Long getIdGrupo() {
		return this.idGrupo;
	}

	/**
	 * @param idGrupo
	 *            the idGrupo to set
	 */
	public void setIdGrupo(final Long idGrupo) {
		this.idGrupo = idGrupo;
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
	 * @return the permissoes
	 */
	public List<PermissaoDTO> getPermissoes() {
		return this.permissoes;
	}

	/**
	 * @param permissoes
	 *            the permissoes to set
	 */
	public void setPermissoes(final List<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
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
				+ ((this.idGrupo == null) ? 0 : this.idGrupo.hashCode());
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
		if (!(obj instanceof GrupoPermissaoDTO)) {
			return false;
		}
		final GrupoPermissaoDTO other = (GrupoPermissaoDTO) obj;
		if (this.idGrupo == null) {
			if (other.idGrupo != null) {
				return false;
			}
		} else if (!this.idGrupo.equals(other.idGrupo)) {
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
		return "GrupoPermissaoDTO [idGrupo=" + this.idGrupo + ", nome="
				+ this.nome + ", permissoes=" + this.permissoes + "]";
	}

	public static class Builder extends
			DTOBuilder<GrupoPermissaoDTO, GrupoPermissao> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public GrupoPermissaoDTO create(final GrupoPermissao grupoPermissao) {
			final GrupoPermissaoDTO dto = new GrupoPermissaoDTO();
			dto.setIdGrupo(grupoPermissao.getIdGrupo());
			dto.setNome(grupoPermissao.getNome());

			// TODO: revisar
			// dto.setPermissoes(PermissaoDTO.Builder.getInstance().createList(
			// grupoPermissao.getPermissoes()));

			return dto;
		}

		@Override
		public GrupoPermissao createEntity(final GrupoPermissaoDTO outer) {
			final GrupoPermissao entidade = new GrupoPermissao();
			entidade.setIdGrupo(outer.getIdGrupo());
			entidade.setNome(outer.getNome());

			// TODO: revisar
			// entidade.setPermissoes(PermissaoDTO.Builder.getInstance()
			// .createListEntity(outer.getPermissoes()));

			return entidade;
		}
	}

}
