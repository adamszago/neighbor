package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.entity.Duplicata;

public class DuplicataDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataCadastro;
	private boolean ativo = true;
	private boolean excluido = false;
	private FaturaDTO fatura;
	private List<MovimentacaoDTO> movimentacoes;
	private CondominioDTO condominio;
	private List<DuplicataParcelaDTO> parcelas;
	private boolean aberto;
	private BigDecimal valorPago;
	private Long numeroParcelas;

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
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	/**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
	public void setDataCadastro(final Date dataCadastro) {
		this.dataCadastro = dataCadastro;
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

	/**
	 * @return the excluido
	 */
	public boolean isExcluido() {
		return this.excluido;
	}

	/**
	 * @param excluido
	 *            the excluido to set
	 */
	public void setExcluido(final boolean excluido) {
		this.excluido = excluido;
	}

	/**
	 * @return the fatura
	 */
	public FaturaDTO getFatura() {
		return this.fatura;
	}

	/**
	 * @param fatura
	 *            the fatura to set
	 */
	public void setFatura(final FaturaDTO fatura) {
		this.fatura = fatura;
	}

	/**
	 * @return the movimentacoes
	 */
	public List<MovimentacaoDTO> getMovimentacoes() {
		return this.movimentacoes;
	}

	/**
	 * @param movimentacoes
	 *            the movimentacoes to set
	 */
	public void setMovimentacoes(final List<MovimentacaoDTO> movimentacoes) {
		this.movimentacoes = movimentacoes;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the parcelas
	 */
	public List<DuplicataParcelaDTO> getParcelas() {
		return this.parcelas;
	}

	/**
	 * @param parcelas
	 *            the parcelas to set
	 */
	public void setParcelas(final List<DuplicataParcelaDTO> parcelas) {
		this.parcelas = parcelas;
	}

	/**
	 * @return the aberto
	 */
	public boolean isAberto() {
		return this.aberto;
	}

	/**
	 * @param aberto
	 *            the aberto to set
	 */
	public void setAberto(final boolean aberto) {
		this.aberto = aberto;
	}

	/**
	 * @return the valorPago
	 */
	public BigDecimal getValorPago() {
		return this.valorPago;
	}

	/**
	 * @param valorPago
	 *            the valorPago to set
	 */
	public void setValorPago(final BigDecimal valorPago) {
		this.valorPago = valorPago;
	}

	/**
	 * @return the numeroParcelas
	 */
	public Long getNumeroParcelas() {
		return this.numeroParcelas;
	}

	/**
	 * @param numeroParcelas
	 *            the numeroParcelas to set
	 */
	public void setNumeroParcelas(final Long numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
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
		if (!(obj instanceof DuplicataDTO)) {
			return false;
		}
		final DuplicataDTO other = (DuplicataDTO) obj;
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
		return "DuplicataDTO [id=" + this.id + ", dataCadastro="
				+ this.dataCadastro + ", ativo=" + this.ativo + ", excluido="
				+ this.excluido + ", fatura=" + this.fatura
				+ ", movimentacoes=" + this.movimentacoes + ", condominio="
				+ this.condominio + ", parcelas=" + this.parcelas + ", aberto="
				+ this.aberto + ", valorPago=" + this.valorPago
				+ ", numeroParcelas=" + this.numeroParcelas + "]";
	}

	public static class Builder extends DTOBuilder<DuplicataDTO, Duplicata> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public DuplicataDTO create(final Duplicata duplicata) {
			final DuplicataDTO dto = new DuplicataDTO();

			dto.setAberto(duplicata.isAberto());
			dto.setAtivo(duplicata.isAtivo());

			if (null != duplicata.getCondominio()) {
				dto.setCondominio(duplicata.getCondominio().createDto());
			}

			dto.setDataCadastro(duplicata.getDataCadastro());
			dto.setExcluido(duplicata.isExcluido());

			if (null != duplicata.getFatura()) {
				dto.setFatura(duplicata.getFatura().createDto());
			}

			dto.setId(duplicata.getId());

			if (null != duplicata.getMovimentacoes()) {
				dto.setMovimentacoes(MovimentacaoDTO.Builder.getInstance()
						.createList(duplicata.getMovimentacoes()));
			}

			if (null != duplicata.getParcelas()) {
				dto.setParcelas(DuplicataParcelaDTO.Builder.getInstance()
						.createList(duplicata.getParcelas()));
				for (DuplicataParcelaDTO dupliParc : dto.getParcelas()) {
					dupliParc.setDuplicata(dto);
				}
				dto.setNumeroParcelas(Long.valueOf(dto.getParcelas().size()));
			} else {
				dto.setNumeroParcelas(0L);
			}

			dto.setValorPago(duplicata.getValorPago());

			return dto;
		}

		@Override
		public Duplicata createEntity(final DuplicataDTO outer) {
			final Duplicata retorno = new Duplicata();

			retorno.setAberto(outer.isAberto());
			retorno.setAtivo(outer.isAtivo());

			if (null != outer.getCondominio()) {
				retorno.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			retorno.setDataCadastro(outer.getDataCadastro());
			retorno.setExcluido(outer.isExcluido());

			if (null != outer.getFatura()) {
				retorno.setFatura(FaturaDTO.Builder.getInstance().createEntity(
						outer.getFatura()));
			}

			retorno.setId(outer.getId());

			if (null != outer.getMovimentacoes()) {
				retorno.setMovimentacoes(MovimentacaoDTO.Builder.getInstance()
						.createListEntity(outer.getMovimentacoes()));
			}

			if (null != outer.getParcelas()) {
				retorno.setParcelas(DuplicataParcelaDTO.Builder.getInstance()
						.createListEntity(outer.getParcelas()));
			}

			retorno.setValorPago(outer.getValorPago());

			return retorno;
		}
	}

}
