package br.com.lphantus.neighbor.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.entity.Plano;

public class PermissaoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idPermissao;
	private String nome;
	private String label;
	private String descricao;
	private GrupoPermissaoDTO grupo;
	private Set<PlanoDTO> planos;
	private List<PlanoDTO> planosList;

	/**
	 * @return the idPermissao
	 */
	public Long getIdPermissao() {
		return this.idPermissao;
	}

	/**
	 * @param idPermissao
	 *            the idPermissao to set
	 */
	public void setIdPermissao(final Long idPermissao) {
		this.idPermissao = idPermissao;
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
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(final String label) {
		this.label = label;
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
	 * @return the grupo
	 */
	public GrupoPermissaoDTO getGrupo() {
		return this.grupo;
	}

	/**
	 * @param grupo
	 *            the grupo to set
	 */
	public void setGrupo(final GrupoPermissaoDTO grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the planos
	 */
	public Set<PlanoDTO> getPlanos() {
		return this.planos;
	}

	/**
	 * @param planos
	 *            the planos to set
	 */
	public void setPlanos(final Set<PlanoDTO> planos) {
		this.planos = planos;
	}

	/**
	 * @return the planosList
	 */
	public List<PlanoDTO> getPlanosList() {
		return this.planosList;
	}

	/**
	 * @param planosList
	 *            the planosList to set
	 */
	public void setPlanosList(final List<PlanoDTO> planosList) {
		this.planosList = planosList;
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
		result = (prime * result) + ((this.idPermissao == null) ? 0 : this.idPermissao.hashCode());
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
		if (!(obj instanceof PermissaoDTO)) {
			return false;
		}
		final PermissaoDTO other = (PermissaoDTO) obj;
		if (this.idPermissao == null) {
			if (other.idPermissao != null) {
				return false;
			}
		} else if (!this.idPermissao.equals(other.idPermissao)) {
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
		return "PermissaoDTO [idPermissao=" + this.idPermissao + ", nome=" + this.nome + ", label=" + this.label + ", descricao=" + this.descricao + ", grupo=" + this.grupo
				+ ", planos=" + this.planos + ", planosList=" + this.planosList + "]";
	}

	public static class Builder extends DTOBuilder<PermissaoDTO, Permissao> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public PermissaoDTO create(final Permissao permissao) {
			final PermissaoDTO dto = new PermissaoDTO();

			if (null != permissao.getGrupo()) {
				dto.setGrupo(permissao.getGrupo().createDto());
			}

			dto.setIdPermissao(permissao.getIdPermissao());
			dto.setLabel(permissao.getLabel());
			dto.setNome(permissao.getNome());
			dto.setDescricao(permissao.getDescricao());

			final Set<PlanoDTO> planos = new HashSet<PlanoDTO>();
			if (permissao.getPlanos() != null && !permissao.getPlanos().isEmpty()) {
				for (final Plano plano : permissao.getPlanos()) {
					planos.add(plano.createDto());
				}
			}
			dto.setPlanos(planos);

			dto.setPlanosList(new ArrayList<PlanoDTO>(planos));
			return dto;
		}

		@Override
		public Permissao createEntity(final PermissaoDTO outer) {
			final Permissao entidade = new Permissao();

			if (outer.getGrupo() != null) {
				entidade.setGrupo(GrupoPermissaoDTO.Builder.getInstance().createEntity(outer.getGrupo()));
			}
			entidade.setIdPermissao(outer.getIdPermissao());
			entidade.setLabel(outer.getLabel());
			entidade.setNome(outer.getNome());
			entidade.setDescricao(outer.getDescricao());

			final Set<Plano> planos = new HashSet<Plano>();
			if ((outer.getPlanos() != null) && !outer.getPlanos().isEmpty()) {
				for (final PlanoDTO plano : outer.getPlanos()) {
					planos.add(PlanoDTO.Builder.getInstance().createEntity(plano));
				}
			}
			entidade.setPlanos(planos);

			entidade.setPlanosList(new ArrayList<Plano>(planos));

			return entidade;
		}
	}

}
