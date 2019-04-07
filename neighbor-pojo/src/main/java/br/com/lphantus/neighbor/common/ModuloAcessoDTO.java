package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.lphantus.neighbor.entity.ModuloAcesso;
import br.com.lphantus.neighbor.entity.Permissao;

public class ModuloAcessoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idModuloAcesso;
	private String nome;
	private String descricao;
	private Set<PermissaoDTO> permissoes;
	private List<PermissaoDTO> permissoesList;
	private CondominioDTO condominio;

	/**
	 * @return the idModuloAcesso
	 */
	public Long getIdModuloAcesso() {
		return this.idModuloAcesso;
	}

	/**
	 * @param idModuloAcesso
	 *            the idModuloAcesso to set
	 */
	public void setIdModuloAcesso(final Long idModuloAcesso) {
		this.idModuloAcesso = idModuloAcesso;
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
	 * @return the permissoes
	 */
	public Set<PermissaoDTO> getPermissoes() {
		return this.permissoes;
	}

	/**
	 * @param permissoes
	 *            the permissoes to set
	 */
	public void setPermissoes(final Set<PermissaoDTO> permissoes) {
		this.permissoes = permissoes;
	}

	/**
	 * @return the permissoesList
	 */
	public List<PermissaoDTO> getPermissoesList() {
		return this.permissoesList;
	}

	/**
	 * @param permissoesList
	 *            the permissoesList to set
	 */
	public void setPermissoesList(final List<PermissaoDTO> permissoesList) {
		this.permissoesList = permissoesList;
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
				+ ((this.idModuloAcesso == null) ? 0 : this.idModuloAcesso
						.hashCode());
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
		if (!(obj instanceof ModuloAcessoDTO)) {
			return false;
		}
		final ModuloAcessoDTO other = (ModuloAcessoDTO) obj;
		if (this.idModuloAcesso == null) {
			if (other.idModuloAcesso != null) {
				return false;
			}
		} else if (!this.idModuloAcesso.equals(other.idModuloAcesso)) {
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
		return "ModuloAcessoDTO [idModuloAcesso=" + this.idModuloAcesso
				+ ", nome=" + this.nome + ", descricao=" + this.descricao
				+ ", permissoes=" + this.permissoes + ", permissoesList="
				+ this.permissoesList + ", condominio=" + this.condominio + "]";
	}

	public static class Builder extends
			DTOBuilder<ModuloAcessoDTO, ModuloAcesso> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ModuloAcessoDTO create(final ModuloAcesso acesso) {
			final ModuloAcessoDTO dto = new ModuloAcessoDTO();
			dto.setDescricao(acesso.getDescricao());
			dto.setIdModuloAcesso(acesso.getIdModuloAcesso());
			dto.setNome(acesso.getNome());

			if (acesso.getCondominio() != null) {
				dto.setCondominio(acesso.getCondominio().createDto());
			}

			final Set<PermissaoDTO> permissoes = new HashSet<PermissaoDTO>();
			if ((null != acesso.getPermissoes())
					&& !acesso.getPermissoes().isEmpty()) {
				for (final Permissao permissao : acesso.getPermissoes()) {
					permissoes.add(permissao.createDto());
				}
			}
			dto.setPermissoes(permissoes);

			dto.setPermissoesList(new ArrayList<PermissaoDTO>(permissoes));
			return dto;
		}

		@Override
		public ModuloAcesso createEntity(final ModuloAcessoDTO outer) {
			final ModuloAcesso entidade = new ModuloAcesso();

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			entidade.setDescricao(outer.getDescricao());
			entidade.setIdModuloAcesso(outer.getIdModuloAcesso());
			entidade.setNome(outer.getNome());

			final Set<Permissao> permissoes = new HashSet<Permissao>();
			if ((null != outer.getPermissoes())
					&& !outer.getPermissoes().isEmpty()) {
				for (final PermissaoDTO permissao : outer.getPermissoes()) {
					permissoes.add(PermissaoDTO.Builder.getInstance()
							.createEntity(permissao));
				}
			}
			entidade.setPermissoes(permissoes);
			entidade.setPermissoesList(new ArrayList<Permissao>(permissoes));
			return entidade;
		}
	}

}
