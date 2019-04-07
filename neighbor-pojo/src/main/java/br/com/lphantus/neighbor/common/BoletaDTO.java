package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;

import br.com.lphantus.neighbor.entity.Boleta;
import br.com.lphantus.neighbor.entity.Pessoa;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.PessoaJuridica;
import br.com.lphantus.neighbor.enums.StatusBoleta;

public class BoletaDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date dataDocumento;
	private Date dataProcessamento;
	private Date dataVencimento;
	private CondominioDTO emissor;
	private PessoaDTO sacado;
	private CarteiraDTO carteira;
	private DuplicataDTO duplicata;
	private StatusBoleta statusBoleto;
	private String localParaPagamento;
	private String instrucao1;
	private String instrucao2;
	private String instrucao3;

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
	 * @return the dataDocumento
	 */
	public Date getDataDocumento() {
		return this.dataDocumento;
	}

	/**
	 * @param dataDocumento
	 *            the dataDocumento to set
	 */
	public void setDataDocumento(final Date dataDocumento) {
		this.dataDocumento = dataDocumento;
	}

	/**
	 * @return the dataProcessamento
	 */
	public Date getDataProcessamento() {
		return this.dataProcessamento;
	}

	/**
	 * @param dataProcessamento
	 *            the dataProcessamento to set
	 */
	public void setDataProcessamento(final Date dataProcessamento) {
		this.dataProcessamento = dataProcessamento;
	}

	/**
	 * @return the dataVencimento
	 */
	public Date getDataVencimento() {
		return this.dataVencimento;
	}

	/**
	 * @param dataVencimento
	 *            the dataVencimento to set
	 */
	public void setDataVencimento(final Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	/**
	 * @return the emissor
	 */
	public CondominioDTO getEmissor() {
		return this.emissor;
	}

	/**
	 * @param emissor
	 *            the emissor to set
	 */
	public void setEmissor(final CondominioDTO emissor) {
		this.emissor = emissor;
	}

	/**
	 * @return the sacado
	 */
	public PessoaDTO getSacado() {
		return this.sacado;
	}

	/**
	 * @param sacado
	 *            the sacado to set
	 */
	public void setSacado(final PessoaDTO sacado) {
		this.sacado = sacado;
	}

	/**
	 * @return the carteira
	 */
	public CarteiraDTO getCarteira() {
		return this.carteira;
	}

	/**
	 * @param carteira
	 *            the carteira to set
	 */
	public void setCarteira(final CarteiraDTO carteira) {
		this.carteira = carteira;
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

	/**
	 * @return the statusBoleto
	 */
	public StatusBoleta getStatusBoleto() {
		return this.statusBoleto;
	}

	/**
	 * @param statusBoleto
	 *            the statusBoleto to set
	 */
	public void setStatusBoleto(final StatusBoleta statusBoleto) {
		this.statusBoleto = statusBoleto;
	}

	/**
	 * @return the localParaPagamento
	 */
	public String getLocalParaPagamento() {
		return this.localParaPagamento;
	}

	/**
	 * @param localParaPagamento
	 *            the localParaPagamento to set
	 */
	public void setLocalParaPagamento(final String localParaPagamento) {
		this.localParaPagamento = localParaPagamento;
	}

	/**
	 * @return the instrucao1
	 */
	public String getInstrucao1() {
		return this.instrucao1;
	}

	/**
	 * @param instrucao1
	 *            the instrucao1 to set
	 */
	public void setInstrucao1(final String instrucao1) {
		this.instrucao1 = instrucao1;
	}

	/**
	 * @return the instrucao2
	 */
	public String getInstrucao2() {
		return this.instrucao2;
	}

	/**
	 * @param instrucao2
	 *            the instrucao2 to set
	 */
	public void setInstrucao2(final String instrucao2) {
		this.instrucao2 = instrucao2;
	}

	/**
	 * @return the instrucao3
	 */
	public String getInstrucao3() {
		return this.instrucao3;
	}

	/**
	 * @param instrucao3
	 *            the instrucao3 to set
	 */
	public void setInstrucao3(final String instrucao3) {
		this.instrucao3 = instrucao3;
	}

	public BigDecimal getValor() {
		BigDecimal retorno = BigDecimal.ZERO;
		if (null != duplicata && null != duplicata.getParcelas()) {
			for (DuplicataParcelaDTO parcela : duplicata.getParcelas()) {
				retorno = retorno.add(parcela.getValor());
			}
		}
		return retorno;
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
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof BoletaDTO)) {
			return false;
		}
		final BoletaDTO other = (BoletaDTO) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public static class Builder extends DTOBuilder<BoletaDTO, Boleta> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public BoletaDTO create(final Boleta boleto) {
			final BoletaDTO dto = new BoletaDTO();

			if (null != boleto.getCarteira()) {
				dto.setCarteira(boleto.getCarteira().createDto());
			}

			dto.setDataDocumento(boleto.getDataDocumento());
			dto.setDataProcessamento(boleto.getDataProcessamento());
			dto.setDataVencimento(boleto.getDataVencimento());

			if (null != boleto.getDuplicata()) {
				dto.setDuplicata(boleto.getDuplicata().createDto());
			}

			dto.setStatusBoleto(boleto.getStatusBoleto());
			dto.setEmissor(boleto.getEmissor().createDto());

			if (null != boleto.getSacado()) {
				if (boleto.getSacado() instanceof PessoaFisica) {
					dto.setSacado(PessoaFisicaDTO.Builder.getInstance().create((PessoaFisica) boleto.getSacado()));
				} else if (boleto.getSacado() instanceof PessoaJuridica) {
					dto.setSacado(PessoaJuridicaDTO.Builder.getInstance().create((PessoaJuridica) boleto.getSacado()));
				}
			}

			dto.setLocalParaPagamento(boleto.getLocalParaPagamento());
			dto.setInstrucao1(boleto.getInstrucao1());
			dto.setInstrucao2(boleto.getInstrucao2());
			dto.setInstrucao3(boleto.getInstrucao3());

			return dto;
		}

		@Override
		public Boleta createEntity(final BoletaDTO outer) {
			final Boleta entidade = new Boleta();

			if (null != outer.getCarteira()) {
				entidade.setCarteira(CarteiraDTO.Builder.getInstance().createEntity(outer.getCarteira()));
			}

			entidade.setDataDocumento(outer.getDataDocumento());
			entidade.setDataProcessamento(outer.getDataProcessamento());
			entidade.setDataVencimento(outer.getDataVencimento());

			if (null != outer.getDuplicata()) {
				entidade.setDuplicata(DuplicataDTO.Builder.getInstance().createEntity(outer.getDuplicata()));
			}

			entidade.setStatusBoleto(outer.getStatusBoleto());
			entidade.setEmissor(CondominioDTO.Builder.getInstance().createEntity(outer.getEmissor()));
			if (null != outer.getSacado()) {
				entidade.setSacado(new Pessoa());
				entidade.getSacado().setIdPessoa(outer.getSacado().getIdPessoa());
			}
			entidade.setLocalParaPagamento(outer.getLocalParaPagamento());
			entidade.setInstrucao1(outer.getInstrucao1());
			entidade.setInstrucao2(outer.getInstrucao2());
			entidade.setInstrucao3(outer.getInstrucao3());
			return entidade;
		}

	}

}
