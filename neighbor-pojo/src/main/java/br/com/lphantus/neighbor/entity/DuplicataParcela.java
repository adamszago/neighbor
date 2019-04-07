package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.DuplicataParcelaDTO;

@Entity
@Table(name = "DUPLICATA_PARCELA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DuplicataParcela implements IEntity<DuplicataParcelaPK, DuplicataParcelaDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DuplicataParcelaPK duplicataParcelaPK;

	// atualizado quando a duplicata for paga
	@Column(name = "DAT_PAG_DUP")
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	// data limite para pagamento da duplicata
	@Column(name = "DAT_LIM_PAG_DUP")
	@Temporal(TemporalType.DATE)
	private Date dataLimiteVencto;

	// mora e outros recebimentos
	@Column(name = "MORA_RECBTOS")
	private BigDecimal moraOutrosRecebimentos;

	// taxa cobrada por ter vencido a fatura
	@Column(name = "MULTA_VENCIMENTO")
	private BigDecimal multaVencimento;

	// taxa cobrada por dia passado do vencimento
	@Column(name = "TAXA_DIA")
	private BigDecimal taxaDia;

	// desconto dado
	@Column(name = "DESCONTO")
	private BigDecimal desconto;

	// abatimento
	@Column(name = "ABATIMENTO")
	private BigDecimal abatimento;

	// valor parcela
	@Column(name = "VALOR")
	private BigDecimal valor;

	@Column(name = "QUITADA")
	private boolean quitada;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumns(@JoinColumn(name = "ID_DUPLICATA", referencedColumnName = "ID_DUPLICATA"))
	@MapsId(value = "idDuplicata")
	private Duplicata duplicata;

	/**
	 * @return the duplicataParcelaPK
	 */
	public DuplicataParcelaPK getDuplicataParcelaPK() {
		return this.duplicataParcelaPK;
	}

	/**
	 * @param duplicataParcelaPK
	 *            the duplicataParcelaPK to set
	 */
	public void setDuplicataParcelaPK(final DuplicataParcelaPK duplicataParcelaPK) {
		this.duplicataParcelaPK = duplicataParcelaPK;
	}

	/**
	 * @return the dataPagamento
	 */
	public Date getDataPagamento() {
		return this.dataPagamento;
	}

	/**
	 * @param dataPagamento
	 *            the dataPagamento to set
	 */
	public void setDataPagamento(final Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	/**
	 * @return the dataLimiteVencto
	 */
	public Date getDataLimiteVencto() {
		return this.dataLimiteVencto;
	}

	/**
	 * @param dataLimiteVencto
	 *            the dataLimiteVencto to set
	 */
	public void setDataLimiteVencto(final Date dataLimiteVencto) {
		this.dataLimiteVencto = dataLimiteVencto;
	}

	/**
	 * @return the moraOutrosRecebimentos
	 */
	public BigDecimal getMoraOutrosRecebimentos() {
		return this.moraOutrosRecebimentos;
	}

	/**
	 * @param moraOutrosRecebimentos
	 *            the moraOutrosRecebimentos to set
	 */
	public void setMoraOutrosRecebimentos(final BigDecimal moraOutrosRecebimentos) {
		this.moraOutrosRecebimentos = moraOutrosRecebimentos;
	}

	/**
	 * @return the multaVencimento
	 */
	public BigDecimal getMultaVencimento() {
		return this.multaVencimento;
	}

	/**
	 * @param multaVencimento
	 *            the multaVencimento to set
	 */
	public void setMultaVencimento(final BigDecimal multaVencimento) {
		this.multaVencimento = multaVencimento;
	}

	/**
	 * @return the taxaDia
	 */
	public BigDecimal getTaxaDia() {
		return this.taxaDia;
	}

	/**
	 * @param taxaDia
	 *            the taxaDia to set
	 */
	public void setTaxaDia(final BigDecimal taxaDia) {
		this.taxaDia = taxaDia;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return this.desconto;
	}

	/**
	 * @param desconto
	 *            the desconto to set
	 */
	public void setDesconto(final BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the abatimento
	 */
	public BigDecimal getAbatimento() {
		return this.abatimento;
	}

	/**
	 * @param abatimento
	 *            the abatimento to set
	 */
	public void setAbatimento(final BigDecimal abatimento) {
		this.abatimento = abatimento;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {
		return this.valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(final BigDecimal valor) {
		this.valor = valor;
	}

	/**
	 * @return the duplicata
	 */
	public Duplicata getDuplicata() {
		return this.duplicata;
	}

	/**
	 * @param duplicata
	 *            the duplicata to set
	 */
	public void setDuplicata(final Duplicata duplicata) {
		this.duplicata = duplicata;
	}

	/**
	 * @return the quitada
	 */
	public boolean isQuitada() {
		return this.quitada;
	}

	/**
	 * @param quitada
	 *            the quitada to set
	 */
	public void setQuitada(final boolean quitada) {
		this.quitada = quitada;
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
		result = (prime * result) + ((this.duplicataParcelaPK == null) ? 0 : this.duplicataParcelaPK.hashCode());
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
		if (!(obj instanceof DuplicataParcela)) {
			return false;
		}
		final DuplicataParcela other = (DuplicataParcela) obj;
		if (this.duplicataParcelaPK == null) {
			if (other.duplicataParcelaPK != null) {
				return false;
			}
		} else if (!this.duplicataParcelaPK.equals(other.duplicataParcelaPK)) {
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
		return "DuplicataParcela [duplicataParcelaPK=" + this.duplicataParcelaPK + ", dataPagamento=" + this.dataPagamento + ", dataLimiteVencto=" + this.dataLimiteVencto
				+ ", moraOutrosRecebimentos=" + this.moraOutrosRecebimentos + ", multaVencimento=" + this.multaVencimento + ", taxaDia=" + this.taxaDia + ", desconto=" + this.desconto
				+ ", abatimento=" + this.abatimento + ", valor=" + this.valor + ", quitada=" + this.quitada + ", duplicata=" + this.duplicata + "]";
	}

	@Override
	public DuplicataParcelaDTO createDto() {
		return DuplicataParcelaDTO.Builder.getInstance().create(this);
	}

}
