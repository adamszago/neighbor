package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;

import br.com.lphantus.neighbor.entity.DuplicataParcela;
import br.com.lphantus.neighbor.entity.DuplicataParcelaPK;

public class DuplicataParcelaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long idDuplicata;
	private Long numeroParcela;
	private Date dataPagamento;
	private Date dataLimiteVencto;
	private BigDecimal moraOutrosRecebimentos;
	private BigDecimal multaVencimento;
	private BigDecimal taxaDia;
	private BigDecimal desconto;
	private BigDecimal abatimento;
	private BigDecimal valor;
	private boolean quitada;
	private DuplicataDTO duplicata;

	/**
	 * @return the idDuplicata
	 */
	public Long getIdDuplicata() {
		return this.idDuplicata;
	}

	/**
	 * @param idDuplicata
	 *            the idDuplicata to set
	 */
	public void setIdDuplicata(final Long idDuplicata) {
		this.idDuplicata = idDuplicata;
	}

	/**
	 * @return the numeroParcela
	 */
	public Long getNumeroParcela() {
		return this.numeroParcela;
	}

	/**
	 * @param numeroParcela
	 *            the numeroParcela to set
	 */
	public void setNumeroParcela(final Long numeroParcela) {
		this.numeroParcela = numeroParcela;
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
	public void setMoraOutrosRecebimentos(
			final BigDecimal moraOutrosRecebimentos) {
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

	/**
	 * @return the duplicata
	 */
	public DuplicataDTO getDuplicata() {
		return this.duplicata;
	}

	/**
	 * @param duplicata
	 *            the duplicata to set
	 */
	public void setDuplicata(final DuplicataDTO duplicata) {
		this.duplicata = duplicata;
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
				+ ((this.idDuplicata == null) ? 0 : this.idDuplicata.hashCode());
		result = (prime * result)
				+ ((this.numeroParcela == null) ? 0 : this.numeroParcela
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
		if (!(obj instanceof DuplicataParcelaDTO)) {
			return false;
		}
		final DuplicataParcelaDTO other = (DuplicataParcelaDTO) obj;
		if (this.idDuplicata == null) {
			if (other.idDuplicata != null) {
				return false;
			}
		} else if (!this.idDuplicata.equals(other.idDuplicata)) {
			return false;
		}
		if (this.numeroParcela == null) {
			if (other.numeroParcela != null) {
				return false;
			}
		} else if (!this.numeroParcela.equals(other.numeroParcela)) {
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
		return "DuplicataParcelaDTO [idDuplicata=" + this.idDuplicata
				+ ", numeroParcela=" + this.numeroParcela + ", dataPagamento="
				+ this.dataPagamento + ", dataLimiteVencto="
				+ this.dataLimiteVencto + ", moraOutrosRecebimentos="
				+ this.moraOutrosRecebimentos + ", multaVencimento="
				+ this.multaVencimento + ", taxaDia=" + this.taxaDia
				+ ", desconto=" + this.desconto + ", abatimento="
				+ this.abatimento + ", valor=" + this.valor + ", quitada="
				+ this.quitada + "]";
	}

	public static class Builder extends
			DTOBuilder<DuplicataParcelaDTO, DuplicataParcela> {

		private static Builder instance;

		public static Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public DuplicataParcelaDTO create(final DuplicataParcela entity) {
			final DuplicataParcelaDTO dto = new DuplicataParcelaDTO();

			dto.setAbatimento(entity.getAbatimento());
			dto.setDataLimiteVencto(entity.getDataLimiteVencto());
			dto.setDataPagamento(entity.getDataPagamento());
			dto.setDesconto(entity.getDesconto());

			// TODO: REVER
			// dto.setDuplicata(entity.get);

			dto.setIdDuplicata(entity.getDuplicataParcelaPK().getIdDuplicata());
			dto.setMoraOutrosRecebimentos(entity.getMoraOutrosRecebimentos());
			dto.setMultaVencimento(entity.getMultaVencimento());
			dto.setNumeroParcela(entity.getDuplicataParcelaPK()
					.getNumeroParcela());
			dto.setTaxaDia(entity.getTaxaDia());
			dto.setQuitada(entity.isQuitada());
			dto.setValor(entity.getValor());

			return dto;
		}

		@Override
		public DuplicataParcela createEntity(final DuplicataParcelaDTO outer) {
			final DuplicataParcela entity = new DuplicataParcela();

			entity.setAbatimento(outer.getAbatimento());
			entity.setDataLimiteVencto(outer.getDataLimiteVencto());
			entity.setDataPagamento(outer.getDataPagamento());
			entity.setDesconto(outer.getDesconto());

			// TODO: REVER
			// dto.setDuplicata(entity.get);

			final DuplicataParcelaPK chave = new DuplicataParcelaPK();
			chave.setIdDuplicata(outer.getIdDuplicata());
			chave.setNumeroParcela(outer.getNumeroParcela());
			entity.setDuplicataParcelaPK(chave);

			entity.setMoraOutrosRecebimentos(outer.getMoraOutrosRecebimentos());
			entity.setMultaVencimento(outer.getMultaVencimento());
			entity.setTaxaDia(outer.getTaxaDia());
			entity.setQuitada(outer.isQuitada());
			entity.setValor(outer.getValor());

			return entity;
		}

	}

}
