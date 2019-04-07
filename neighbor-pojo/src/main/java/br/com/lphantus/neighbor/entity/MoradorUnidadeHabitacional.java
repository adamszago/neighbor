package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;

@Entity
@Table(name = "MORADOR_UNIDADE_HABITACIONAL")
public class MoradorUnidadeHabitacional implements
		IEntity<MoradorUnidadeHabitacionalPK, MoradorUnidadeHabitacionalDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MoradorUnidadeHabitacionalPK chave;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR", insertable = false, updatable = false))
	private Morador morador;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_UNIDADE_HABITACIONAL", referencedColumnName = "ID_UNIDADE_HABITACIONAL", insertable = false, updatable = false))
	private UnidadeHabitacional unidadeHabitacional;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INICIO")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_FIM")
	private Date dataFim;

	@Column(name = "ADIMPLENTE")
	private boolean adimplente = true;

	@Column(name = "RESPONSAVEL_FINANCEIRO")
	private boolean responsavelFinanceiro;

	@Column(name = "TIPO_MORADOR")
	private String tipoMorador;

	/**
	 * @return the chave
	 */
	public MoradorUnidadeHabitacionalPK getChave() {
		return this.chave;
	}

	/**
	 * @param chave
	 *            the chave to set
	 */
	public void setChave(final MoradorUnidadeHabitacionalPK chave) {
		this.chave = chave;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final Morador morador) {
		this.morador = morador;
	}

	/**
	 * @return the unidadeHabitacional
	 */
	public UnidadeHabitacional getUnidadeHabitacional() {
		return this.unidadeHabitacional;
	}

	/**
	 * @param unidadeHabitacional
	 *            the unidadeHabitacional to set
	 */
	public void setUnidadeHabitacional(
			final UnidadeHabitacional unidadeHabitacional) {
		this.unidadeHabitacional = unidadeHabitacional;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return this.dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(final Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return this.dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(final Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the tipoMorador
	 */
	public String getTipoMorador() {
		return this.tipoMorador;
	}

	/**
	 * @param tipoMorador
	 *            the tipoMorador to set
	 */
	public void setTipoMorador(final String tipoMorador) {
		this.tipoMorador = tipoMorador;
	}

	/**
	 * @return the adimplente
	 */
	public boolean isAdimplente() {
		return this.adimplente;
	}

	/**
	 * @param adimplente
	 *            the adimplente to set
	 */
	public void setAdimplente(final boolean adimplente) {
		this.adimplente = adimplente;
	}

	/**
	 * @return the responsavelFinanceiro
	 */
	public boolean isResponsavelFinanceiro() {
		return this.responsavelFinanceiro;
	}

	/**
	 * @param responsavelFinanceiro
	 *            the responsavelFinanceiro to set
	 */
	public void setResponsavelFinanceiro(final boolean responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
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
				+ ((this.chave == null) ? 0 : this.chave.hashCode());
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
		if (!(obj instanceof MoradorUnidadeHabitacional)) {
			return false;
		}
		final MoradorUnidadeHabitacional other = (MoradorUnidadeHabitacional) obj;
		if (this.chave == null) {
			if (other.chave != null) {
				return false;
			}
		} else if (!this.chave.equals(other.chave)) {
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
		return "MoradorUnidadeHabitacional [chave=" + this.chave
				+ ", unidadeHabitacional=" + this.unidadeHabitacional
				+ ", dataInicio=" + this.dataInicio + ", dataFim="
				+ this.dataFim + ", adimplente=" + this.adimplente
				+ ", tipoMorador=" + this.tipoMorador + "]";
	}

	@Override
	public MoradorUnidadeHabitacionalDTO createDto() {
		return MoradorUnidadeHabitacionalDTO.Builder.getInstance().create(this);
	}

}
