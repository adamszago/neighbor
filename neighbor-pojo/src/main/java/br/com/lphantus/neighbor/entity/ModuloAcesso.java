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

import br.com.lphantus.neighbor.common.ModuloAcessoDTO;

@Entity
@Table(name = "MODULO_ACESSO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ModuloAcesso implements IEntity<Long, ModuloAcessoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_MODULO_ACESSO")
	private Long idModuloAcesso;

	@Column(name = "NOME", unique = true)
	private String nome;

	@Column(name = "DESCRICAO")
	private String descricao;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID_PERMISSAO"))
	private Set<Permissao> permissoes;

	@Transient
	private List<Permissao> permissoesList;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

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
	public Set<Permissao> getPermissoes() {
		return this.permissoes;
	}

	/**
	 * @param permissoes
	 *            the permissoes to set
	 */
	public void setPermissoes(final Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public List<Permissao> getPermissoesList() {

		if (this.getPermissoes() != null) {
			this.permissoesList = new ArrayList<Permissao>(this.getPermissoes());
		}

		return this.permissoesList;
	}

	public void setPermissoesList(final List<Permissao> permissoesList) {
		this.permissoesList = permissoesList;
	}

	/**
	 * @return the condominio
	 */
	public Condominio getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final Condominio condominio) {
		this.condominio = condominio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.idModuloAcesso == null) ? 0 : this.idModuloAcesso
						.hashCode());
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
		final ModuloAcesso other = (ModuloAcesso) obj;
		if (this.idModuloAcesso == null) {
			if (other.idModuloAcesso != null) {
				return false;
			}
		} else if (!this.idModuloAcesso.equals(other.idModuloAcesso)) {
			return false;
		}
		return true;
	}

	@Override
	public ModuloAcessoDTO createDto() {
		return ModuloAcessoDTO.Builder.getInstance().create(this);
	}

}
