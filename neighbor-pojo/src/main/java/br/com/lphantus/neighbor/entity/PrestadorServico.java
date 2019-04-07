package br.com.lphantus.neighbor.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.PrestadorServicoDTO;

@Entity
@Table(name = "PRESTADOR_SERVICO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PrestadorServico implements IEntity<Long, PrestadorServicoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PRESTADOR")
	private Long idPrestador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_TIPO_PRESTADOR", referencedColumnName = "ID_TIPO_PRESTADOR"))
	private TipoPrestador tipoPrestador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "prestadorServico", cascade = CascadeType.ALL)
	@OrderBy("dataServico desc")
	private Set<ServicoPrestado> servicosPrestados;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(/* name="ENTRADALIVRE_PRESTADOR" */)
	private Set<Morador> entradasLivres;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumns(value = { @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA") })
	private Pessoa pessoa;

	@Transient
	private List<Morador> entradasLivresList;

	@Transient
	private ServicoPrestado servicoAgendado;

	@Transient
	private List<ServicoPrestado> servicosPrestadosList;

	/**
	 * @return the idPrestador
	 */
	public Long getIdPrestador() {
		return this.idPrestador;
	}

	/**
	 * @param idPrestador
	 *            the idPrestador to set
	 */
	public void setIdPrestador(final Long idPrestador) {
		this.idPrestador = idPrestador;
	}

	/**
	 * @return the tipoPrestador
	 */
	public TipoPrestador getTipoPrestador() {
		return this.tipoPrestador;
	}

	/**
	 * @param tipoPrestador
	 *            the tipoPrestador to set
	 */
	public void setTipoPrestador(final TipoPrestador tipoPrestador) {
		this.tipoPrestador = tipoPrestador;
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

	/**
	 * @return the servicosPrestados
	 */
	public Set<ServicoPrestado> getServicosPrestados() {
		return this.servicosPrestados;
	}

	/**
	 * @param servicosPrestados
	 *            the servicosPrestados to set
	 */
	public void setServicosPrestados(
			final Set<ServicoPrestado> servicosPrestados) {
		this.servicosPrestados = servicosPrestados;
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
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final Pessoa pessoa) {
		this.pessoa = pessoa;
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
	 * @return the servicoAgendado
	 */
	public ServicoPrestado getServicoAgendado() {
		return this.servicoAgendado;
	}

	/**
	 * @param servicoAgendado
	 *            the servicoAgendado to set
	 */
	public void setServicoAgendado(final ServicoPrestado servicoAgendado) {
		this.servicoAgendado = servicoAgendado;
	}

	/**
	 * @return the servicosPrestadosList
	 */
	public List<ServicoPrestado> getServicosPrestadosList() {
		return this.servicosPrestadosList;
	}

	/**
	 * @param servicosPrestadosList
	 *            the servicosPrestadosList to set
	 */
	public void setServicosPrestadosList(
			final List<ServicoPrestado> servicosPrestadosList) {
		this.servicosPrestadosList = servicosPrestadosList;
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
				+ ((this.idPrestador == null) ? 0 : this.idPrestador.hashCode());
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
		if (!(obj instanceof PrestadorServico)) {
			return false;
		}
		final PrestadorServico other = (PrestadorServico) obj;
		if (this.idPrestador == null) {
			if (other.idPrestador != null) {
				return false;
			}
		} else if (!this.idPrestador.equals(other.idPrestador)) {
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
		return "PrestadorServico [idPrestador=" + this.idPrestador
				+ ", tipoPrestador=" + this.tipoPrestador + ", condominio="
				+ this.condominio + ", servicosPrestados="
				+ this.servicosPrestados + ", entradasLivres="
				+ this.entradasLivres + ", pessoa=" + this.pessoa
				+ ", entradasLivresList=" + this.entradasLivresList
				+ ", servicoAgendado=" + this.servicoAgendado
				+ ", servicosPrestadosList=" + this.servicosPrestadosList + "]";
	}

	@Override
	public PrestadorServicoDTO createDto() {
		return PrestadorServicoDTO.Builder.getInstance().create(this);
	}

}
