package br.com.lphantus.neighbor.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;

@Entity
@Table(name = "GRUPO_PERMISSAO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GrupoPermissao implements IEntity<Long, GrupoPermissaoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_GRUPO")
	private Long idGrupo;

	@Column(name = "NOME")
	private String nome;

	// incluido em 250712 por @adams
	@OneToMany(mappedBy = "grupo", fetch = FetchType.EAGER, cascade = {
			CascadeType.PERSIST, CascadeType.REFRESH })
	private List<Permissao> permissoes;

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
	public List<Permissao> getPermissoes() {
		return this.permissoes;
	}

	/**
	 * @param permissoes
	 *            the permissoes to set
	 */
	public void setPermissoes(final List<Permissao> permissoes) {
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
		if (!(obj instanceof GrupoPermissao)) {
			return false;
		}
		final GrupoPermissao other = (GrupoPermissao) obj;
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
		return "GrupoPermissao [idGrupo=" + this.idGrupo + ", nome="
				+ this.nome + ", permissoes=" + this.permissoes + "]";
	}

	@Override
	public GrupoPermissaoDTO createDto() {
		return GrupoPermissaoDTO.Builder.getInstance().create(this);
	}

}
