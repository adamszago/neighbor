package br.com.lphantus.neighbor.entity;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;

@Entity
@Table(name = "ANIMAL_ESTIMACAO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnimalEstimacao implements IEntity<Long, AnimalEstimacaoDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_ANIMAL")
	private Long id;

	@Column(name = "NOME_ANIMAL")
	private String nome;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_TIPO_ANIMAL", referencedColumnName = "ID_TIPO_ANIMAL"))
	private TipoAnimal tipoAnimal;

	@Column(name = "RACA_ANIMAL")
	private String raca;

	@Column(name = "PORTE_ANIMAL")
	private String porte;

	@Column(name = "ANIMAL_VACINADO")
	private boolean vacinado;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_VISTORIA_VACINA_ANIMAL")
	private Date dataVistoriaCartao;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumns(@JoinColumn(name = "ID_DONO", referencedColumnName = "ID_MORADOR"))
	private Morador dono;

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
	 * @return the tipoAnimal
	 */
	public TipoAnimal getTipoAnimal() {
		return this.tipoAnimal;
	}

	/**
	 * @param tipoAnimal
	 *            the tipoAnimal to set
	 */
	public void setTipoAnimal(final TipoAnimal tipoAnimal) {
		this.tipoAnimal = tipoAnimal;
	}

	/**
	 * @return the raca
	 */
	public String getRaca() {
		return this.raca;
	}

	/**
	 * @param raca
	 *            the raca to set
	 */
	public void setRaca(final String raca) {
		this.raca = raca;
	}

	/**
	 * @return the porte
	 */
	public String getPorte() {
		return this.porte;
	}

	/**
	 * @param porte
	 *            the porte to set
	 */
	public void setPorte(final String porte) {
		this.porte = porte;
	}

	/**
	 * @return the vacinado
	 */
	public boolean isVacinado() {
		return this.vacinado;
	}

	/**
	 * @param vacinado
	 *            the vacinado to set
	 */
	public void setVacinado(final boolean vacinado) {
		this.vacinado = vacinado;
	}

	/**
	 * @return the dataVistoriaCartao
	 */
	public Date getDataVistoriaCartao() {
		return this.dataVistoriaCartao;
	}

	/**
	 * @param dataVistoriaCartao
	 *            the dataVistoriaCartao to set
	 */
	public void setDataVistoriaCartao(final Date dataVistoriaCartao) {
		this.dataVistoriaCartao = dataVistoriaCartao;
	}

	/**
	 * @return the dono
	 */
	public Morador getDono() {
		return this.dono;
	}

	/**
	 * @param dono
	 *            the dono to set
	 */
	public void setDono(final Morador dono) {
		this.dono = dono;
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
		if (!(obj instanceof AnimalEstimacao)) {
			return false;
		}
		final AnimalEstimacao other = (AnimalEstimacao) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public AnimalEstimacaoDTO createDto() {
		return AnimalEstimacaoDTO.Builder.getInstance().create(this);
	}

}
