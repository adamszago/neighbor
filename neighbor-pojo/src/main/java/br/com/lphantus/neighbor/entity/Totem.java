package br.com.lphantus.neighbor.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.TotemDTO;

@Entity
@Table(name = "TOTEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Totem implements IEntity<Long, TotemDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = -7352003124224629255L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TOTEM")
	private Long id;

	@Column(name = "SENHA")
	private String senha;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR"))
	private Morador morador;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_AGREGADO", referencedColumnName = "ID_AGREGADO"))
	private Agregado agregado;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_VISITANTE", referencedColumnName = "ID_VISITANTE"))
	private Visitante visitante;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_PRESTADOR", referencedColumnName = "ID_PRESTADOR"))
	private PrestadorServico prestador;

	@Column(name = "ATIVO")
	private Boolean ativo = false;

	public Totem() {
	}

	public Totem(final Morador morador) {
		this.morador = morador;
	}

	public Totem(final Agregado agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final Morador morador) {
		this.morador = morador;
	}

	/**
	 * @return the agregado
	 */
	public Agregado getAgregado() {
		return agregado;
	}

	/**
	 * @param agregado
	 *            the agregado to set
	 */
	public void setAgregado(final Agregado agregado) {
		this.agregado = agregado;
	}

	/**
	 * @return the visitante
	 */
	public Visitante getVisitante() {
		return visitante;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(Visitante visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the prestador
	 */
	public PrestadorServico getPrestador() {
		return prestador;
	}

	/**
	 * @param prestador
	 *            the prestador to set
	 */
	public void setPrestador(PrestadorServico prestador) {
		this.prestador = prestador;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
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
		result = (prime * result) + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Totem)) {
			return false;
		}
		final Totem other = (Totem) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Totem [id=" + id + ", senha=" + senha + ", morador=" + morador
				+ ", agregado=" + agregado + ", visitante=" + visitante
				+ ", prestador=" + prestador + ", ativo=" + ativo + "]";
	}

	@Override
	public TotemDTO createDto() {
		return TotemDTO.Builder.getInstance().create(this);
	}
}