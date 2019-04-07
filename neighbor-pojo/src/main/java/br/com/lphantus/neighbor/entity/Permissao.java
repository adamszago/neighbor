package br.com.lphantus.neighbor.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.PermissaoDTO;

@Entity
@Table(name = "PERMISSAO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Permissao implements IEntity<Long, PermissaoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PERMISSAO")
	private Long idPermissao;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "LABEL")
	private String label;

	@Column(name = "DESCRICAO")
	private String descricao;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumns(@JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID_GRUPO"))
	private GrupoPermissao grupo;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinColumns(@JoinColumn(name = "ID_PLANO", referencedColumnName = "ID_PLANO"))
	private Set<Plano> planos;

	@Transient
	private List<Plano> planosList;

	public Long getIdPermissao() {
		return this.idPermissao;
	}

	public void setIdPermissao(final Long idPermissao) {
		this.idPermissao = idPermissao;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public GrupoPermissao getGrupo() {
		return this.grupo;
	}

	public void setGrupo(final GrupoPermissao grupo) {
		this.grupo = grupo;
	}

	public Set<Plano> getPlanos() {
		return this.planos;
	}

	public void setPlanos(final Set<Plano> planos) {
		this.planos = planos;
	}

	public List<Plano> getPlanosList() {
		this.planosList = new ArrayList<Plano>(this.getPlanos());

		return this.planosList;
	}

	public void setPlanosList(final List<Plano> planosList) {
		this.planosList = planosList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.idPermissao == null) ? 0 : this.idPermissao.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Permissao other = (Permissao) obj;
		if (this.idPermissao == null) {
			if (other.idPermissao != null) {
				return false;
			}
		} else if (!this.idPermissao.equals(other.idPermissao)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return this.label;
	}

	@Override
	public PermissaoDTO createDto() {
		return PermissaoDTO.Builder.getInstance().create(this);
	}

}
