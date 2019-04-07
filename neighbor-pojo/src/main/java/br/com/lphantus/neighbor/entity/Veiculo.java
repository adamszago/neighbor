package br.com.lphantus.neighbor.entity;

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

import br.com.lphantus.neighbor.common.VeiculoDTO;

/**
 * @author Adams Zago
 * 
 */
@Entity
@Table(name = "VEICULO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Veiculo implements IEntity<Long, VeiculoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_VEICULO")
	private Long id;

	@Column(name = "PLACA")
	private String placa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_MARCA_VEICULO", referencedColumnName = "ID_MARCA_VEICULO"))
	private MarcaVeiculo marca;

	@Column(name = "MODELO")
	private String modelo;

	@Column(name = "COR")
	private String cor;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumns(@JoinColumn(name = "ID_DONO", referencedColumnName = "ID_MORADOR"))
	private Morador proprietario;

	@Column(name = "ATIVO")
	private boolean ativo = true;

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
	 * @return the placa
	 */
	public String getPlaca() {
		return this.placa;
	}

	/**
	 * @param placa
	 *            the placa to set
	 */
	public void setPlaca(final String placa) {
		this.placa = placa;
	}

	/**
	 * @return the marca
	 */
	public MarcaVeiculo getMarca() {
		return this.marca;
	}

	/**
	 * @param marca
	 *            the marca to set
	 */
	public void setMarca(final MarcaVeiculo marca) {
		this.marca = marca;
	}

	/**
	 * @return the modelo
	 */
	public String getModelo() {
		return this.modelo;
	}

	/**
	 * @param modelo
	 *            the modelo to set
	 */
	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	/**
	 * @return the cor
	 */
	public String getCor() {
		return this.cor;
	}

	/**
	 * @param cor
	 *            the cor to set
	 */
	public void setCor(final String cor) {
		this.cor = cor;
	}

	/**
	 * @return the proprietario
	 */
	public Morador getProprietario() {
		return this.proprietario;
	}

	/**
	 * @param proprietario
	 *            the proprietario to set
	 */
	public void setProprietario(final Morador proprietario) {
		this.proprietario = proprietario;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final boolean ativo) {
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
		if (!(obj instanceof Veiculo)) {
			return false;
		}
		final Veiculo other = (Veiculo) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
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
		return "Veiculo [id=" + this.id + ", placa=" + this.placa + ", marca="
				+ this.marca + ", modelo=" + this.modelo + ", cor=" + this.cor
				+ ", proprietario=" + this.proprietario + ", ativo="
				+ this.ativo + "]";
	}

	@Override
	public VeiculoDTO createDto() {
		return VeiculoDTO.Builder.getInstance().create(this);
	}
}
