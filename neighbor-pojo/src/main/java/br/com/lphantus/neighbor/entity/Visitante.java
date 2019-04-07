package br.com.lphantus.neighbor.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.VisitanteDTO;

@Entity
@Table(name = "VISITANTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@PrimaryKeyJoinColumns(@PrimaryKeyJoinColumn(name = "ID_VISITANTE", referencedColumnName = "ID_PESSOA_FISICA"))
public class Visitante extends PessoaFisica implements
		IEntity<Long, VisitanteDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "visitante", cascade = CascadeType.ALL)
	@OrderBy("inicioVisita desc")
	private Set<Visita> visitas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(/* name="entradalivre_visitante" */)
	private Set<Morador> entradasLivres;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@Transient
	private Visita visitaAgendada;

	@Transient
	private List<Visita> visitasList;

	@Transient
	private List<Morador> entradasLivresList;

	/**
	 * @return the visitas
	 */
	public Set<Visita> getVisitas() {
		return this.visitas;
	}

	/**
	 * @param visitas
	 *            the visitas to set
	 */
	public void setVisitas(final Set<Visita> visitas) {
		this.visitas = visitas;
	}

	/**
	 * @return the visitaAgendada
	 */
	public Visita getVisitaAgendada() {
		return this.visitaAgendada;
	}

	/**
	 * @param visitaAgendada
	 *            the visitaAgendada to set
	 */
	public void setVisitaAgendada(final Visita visitaAgendada) {
		this.visitaAgendada = visitaAgendada;
	}

	/**
	 * @return the visitasList
	 */
	public List<Visita> getVisitasList() {
		return this.visitasList;
	}

	/**
	 * @param visitasList
	 *            the visitasList to set
	 */
	public void setVisitasList(final List<Visita> visitasList) {
		this.visitasList = visitasList;
	}

	/**
	 * @return the entradasLivres
	 */
	public Set<Morador> getEntradasLivres() {
		return this.entradasLivres;
	}

	/**
	 * @param entradasLivres
	 *            the entradasLivres to set
	 */
	public void setEntradasLivres(final Set<Morador> entradasLivres) {
		this.entradasLivres = entradasLivres;
	}

	/**
	 * @return the entradasLivresList
	 */
	public List<Morador> getEntradasLivresList() {
		return this.entradasLivresList;
	}

	/**
	 * @param entradasLivresList
	 *            the entradasLivresList to set
	 */
	public void setEntradasLivresList(final List<Morador> entradasLivresList) {
		this.entradasLivresList = entradasLivresList;
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
				+ ((this.getIdPessoa() == null) ? 0 : this.getIdPessoa()
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
		if (!(obj instanceof Visitante)) {
			return false;
		}
		final Visitante other = (Visitante) obj;
		if (this.getIdPessoa() == null) {
			if (other.getIdPessoa() != null) {
				return false;
			}
		} else if (!this.getIdPessoa().equals(other.getIdPessoa())) {
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
		return "Visitante [visitas=" + this.visitas + ", entradasLivres="
				+ this.entradasLivres + ", condominio=" + this.condominio
				+ ", visitaAgendada=" + this.visitaAgendada + ", visitasList="
				+ this.visitasList + ", entradasLivresList="
				+ this.entradasLivresList + "]";
	}

	@Override
	public VisitanteDTO createDto() {
		return VisitanteDTO.Builder.getInstance().create(this);
	}

}
