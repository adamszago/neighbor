package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.SindicoCondominioDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "SINDICO_CONDOMINIO")
public class SindicoCondominio implements IEntity<SindicoCondominioPK,SindicoCondominioDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SindicoCondominioPK chave;

	@ManyToOne
	@MapsId(value = "idSindico")
	@JoinColumns(@JoinColumn(name = "ID_SINDICO", referencedColumnName = "ID_SINDICO"))
	private Sindico sindico;

	@ManyToOne
	@MapsId(value = "idCondominio")
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@Column(name = "INICIO_MANDATO")
	@Temporal(TemporalType.DATE)
	private Date inicioMandato;

	@Column(name = "FIM_MANDATO")
	@Temporal(TemporalType.DATE)
	private Date fimMandato;

	/**
	 * @return the chave
	 */
	public SindicoCondominioPK getChave() {
		return this.chave;
	}

	/**
	 * @param chave
	 *            the chave to set
	 */
	public void setChave(final SindicoCondominioPK chave) {
		this.chave = chave;
	}

	/**
	 * @return the sindico
	 */
	public Sindico getSindico() {
		return this.sindico;
	}

	/**
	 * @param sindico
	 *            the sindico to set
	 */
	public void setSindico(final Sindico sindico) {
		this.sindico = sindico;
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
	 * @return the inicioMandato
	 */
	public Date getInicioMandato() {
		return this.inicioMandato;
	}

	/**
	 * @param inicioMandato
	 *            the inicioMandato to set
	 */
	public void setInicioMandato(final Date inicioMandato) {
		this.inicioMandato = inicioMandato;
	}

	/**
	 * @return the fimMandato
	 */
	public Date getFimMandato() {
		return this.fimMandato;
	}

	/**
	 * @param fimMandato
	 *            the fimMandato to set
	 */
	public void setFimMandato(final Date fimMandato) {
		this.fimMandato = fimMandato;
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
		if (!(obj instanceof SindicoCondominio)) {
			return false;
		}
		final SindicoCondominio other = (SindicoCondominio) obj;
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
		return "SindicoCondominio [chave=" + this.chave + ", sindico="
				+ this.sindico + ", condominio=" + this.condominio
				+ ", inicioMandato=" + this.inicioMandato + ", fimMandato="
				+ this.fimMandato + "]";
	}

	@Override
	public SindicoCondominioDTO createDto() {
		return SindicoCondominioDTO.Builder.getInstance().create(this);
	}

}
