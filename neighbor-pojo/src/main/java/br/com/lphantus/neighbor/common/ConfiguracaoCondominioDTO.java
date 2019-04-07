package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;

import br.com.lphantus.neighbor.entity.ConfiguracaoCondominio;

public class ConfiguracaoCondominioDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idConfiguracao;
	private Integer dataFaturas;
	private Long limiteDias;
	private BigDecimal moraOutrosRecebimentos;
	private BigDecimal multaVencimento;
	private BigDecimal taxaDia;
	private BigDecimal desconto;
	private BigDecimal abatimento;
	private boolean avisado = false;
	private Integer qtdeDiasVisita;
	private Integer qtdeDiasServico;
	private Integer qtdeEntradaLivrePrestador;
	private Integer qtdeEntradaLivreVisitante;
	private Integer qtdeDiasMinimoReserva;
	private Integer tamanhoMaximoArquivos;
	private String cpfEmissorDocumentos;
	private String nomeEmissorDocumentos;
	private boolean exibeTodasReservas = false;
	private CondominioDTO condominio;

	/**
	 * @return the idConfiguracao
	 */
	public Long getIdConfiguracao() {
		return this.idConfiguracao;
	}

	/**
	 * @param idConfiguracao
	 *            the idConfiguracao to set
	 */
	public void setIdConfiguracao(final Long idConfiguracao) {
		this.idConfiguracao = idConfiguracao;
	}

	/**
	 * @return the dataFaturas
	 */
	public Integer getDataFaturas() {
		return this.dataFaturas;
	}

	/**
	 * @param dataFaturas
	 *            the dataFaturas to set
	 */
	public void setDataFaturas(final Integer dataFaturas) {
		this.dataFaturas = dataFaturas;
	}

	/**
	 * @return the limiteDias
	 */
	public Long getLimiteDias() {
		return this.limiteDias;
	}

	/**
	 * @param limiteDias
	 *            the limiteDias to set
	 */
	public void setLimiteDias(final Long limiteDias) {
		this.limiteDias = limiteDias;
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
	 * @return the avisado
	 */
	public boolean isAvisado() {
		return this.avisado;
	}

	/**
	 * @param avisado
	 *            the avisado to set
	 */
	public void setAvisado(final boolean avisado) {
		this.avisado = avisado;
	}

	/**
	 * @return the qtdeDiasVisita
	 */
	public Integer getQtdeDiasVisita() {
		return this.qtdeDiasVisita;
	}

	/**
	 * @param qtdeDiasVisita
	 *            the qtdeDiasVisita to set
	 */
	public void setQtdeDiasVisita(final Integer qtdeDiasVisita) {
		this.qtdeDiasVisita = qtdeDiasVisita;
	}

	/**
	 * @return the qtdeDiasServico
	 */
	public Integer getQtdeDiasServico() {
		return this.qtdeDiasServico;
	}

	/**
	 * @param qtdeDiasServico
	 *            the qtdeDiasServico to set
	 */
	public void setQtdeDiasServico(final Integer qtdeDiasServico) {
		this.qtdeDiasServico = qtdeDiasServico;
	}

	/**
	 * @return the qtdeEntradaLivrePrestador
	 */
	public Integer getQtdeEntradaLivrePrestador() {
		return qtdeEntradaLivrePrestador;
	}

	/**
	 * @param qtdeEntradaLivrePrestador
	 *            the qtdeEntradaLivrePrestador to set
	 */
	public void setQtdeEntradaLivrePrestador(Integer qtdeEntradaLivrePrestador) {
		this.qtdeEntradaLivrePrestador = qtdeEntradaLivrePrestador;
	}

	/**
	 * @return the qtdeEntradaLivreVisitante
	 */
	public Integer getQtdeEntradaLivreVisitante() {
		return qtdeEntradaLivreVisitante;
	}

	/**
	 * @param qtdeEntradaLivreVisitante
	 *            the qtdeEntradaLivreVisitante to set
	 */
	public void setQtdeEntradaLivreVisitante(Integer qtdeEntradaLivreVisitante) {
		this.qtdeEntradaLivreVisitante = qtdeEntradaLivreVisitante;
	}

	/**
	 * @return the qtdeDiasMinimoReserva
	 */
	public Integer getQtdeDiasMinimoReserva() {
		return qtdeDiasMinimoReserva;
	}

	/**
	 * @param qtdeDiasMinimoReserva
	 *            the qtdeDiasMinimoReserva to set
	 */
	public void setQtdeDiasMinimoReserva(Integer qtdeDiasMinimoReserva) {
		this.qtdeDiasMinimoReserva = qtdeDiasMinimoReserva;
	}

	/**
	 * @return the tamanhoMaximoArquivos
	 */
	public Integer getTamanhoMaximoArquivos() {
		return tamanhoMaximoArquivos;
	}

	/**
	 * @param tamanhoMaximoArquivos
	 *            the tamanhoMaximoArquivos to set
	 */
	public void setTamanhoMaximoArquivos(Integer tamanhoMaximoArquivos) {
		this.tamanhoMaximoArquivos = tamanhoMaximoArquivos;
	}

	/**
	 * @return the cpfEmissorDocumentos
	 */
	public String getCpfEmissorDocumentos() {
		return cpfEmissorDocumentos;
	}

	/**
	 * @param cpfEmissorDocumentos
	 *            the cpfEmissorDocumentos to set
	 */
	public void setCpfEmissorDocumentos(String cpfEmissorDocumentos) {
		this.cpfEmissorDocumentos = cpfEmissorDocumentos;
	}

	/**
	 * @return the nomeEmissorDocumentos
	 */
	public String getNomeEmissorDocumentos() {
		return nomeEmissorDocumentos;
	}

	/**
	 * @param nomeEmissorDocumentos
	 *            the nomeEmissorDocumentos to set
	 */
	public void setNomeEmissorDocumentos(String nomeEmissorDocumentos) {
		this.nomeEmissorDocumentos = nomeEmissorDocumentos;
	}

	/**
	 * @return the exibeTodasReservas
	 */
	public boolean isExibeTodasReservas() {
		return exibeTodasReservas;
	}

	/**
	 * @param exibeTodasReservas
	 *            the exibeTodasReservas to set
	 */
	public void setExibeTodasReservas(boolean exibeTodasReservas) {
		this.exibeTodasReservas = exibeTodasReservas;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.idConfiguracao == null) ? 0 : this.idConfiguracao.hashCode());
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
		if (!(obj instanceof ConfiguracaoCondominioDTO)) {
			return false;
		}
		final ConfiguracaoCondominioDTO other = (ConfiguracaoCondominioDTO) obj;
		if (this.idConfiguracao == null) {
			if (other.idConfiguracao != null) {
				return false;
			}
		} else if (!this.idConfiguracao.equals(other.idConfiguracao)) {
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
		return "ConfiguracaoCondominioDTO [idConfiguracao=" + idConfiguracao + ", dataFaturas=" + dataFaturas + ", limiteDias=" + limiteDias + ", moraOutrosRecebimentos="
				+ moraOutrosRecebimentos + ", multaVencimento=" + multaVencimento + ", taxaDia=" + taxaDia + ", desconto=" + desconto + ", abatimento=" + abatimento + ", avisado="
				+ avisado + ", qtdeDiasVisita=" + qtdeDiasVisita + ", qtdeDiasServico=" + qtdeDiasServico + ", qtdeEntradaLivrePrestador=" + qtdeEntradaLivrePrestador
				+ ", qtdeEntradaLivreVisitante=" + qtdeEntradaLivreVisitante + ", qtdeDiasMinimoReserva=" + qtdeDiasMinimoReserva + ", tamanhoMaximoArquivos="
				+ tamanhoMaximoArquivos + ", cpfEmissorDocumentos=" + cpfEmissorDocumentos + ", nomeEmissorDocumentos=" + nomeEmissorDocumentos + ", condominio=" + condominio
				+ "]";
	}

	public static class Builder extends DTOBuilder<ConfiguracaoCondominioDTO, ConfiguracaoCondominio> {

		private static Builder instance;

		private Builder() {

		}

		public static Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public ConfiguracaoCondominioDTO create(final ConfiguracaoCondominio entity) {
			final ConfiguracaoCondominioDTO dto = new ConfiguracaoCondominioDTO();
			dto.setAbatimento(entity.getAbatimento());
			dto.setAvisado(entity.isAvisado());
			if (null != entity.getCondominio()) {
				dto.setCondominio(entity.getCondominio().createDto());
			}
			dto.setDataFaturas(entity.getDataFaturas());
			dto.setDesconto(entity.getDesconto());
			dto.setIdConfiguracao(entity.getIdConfiguracao());
			dto.setLimiteDias(entity.getLimiteDias());
			dto.setMoraOutrosRecebimentos(entity.getMoraOutrosRecebimentos());
			dto.setMultaVencimento(entity.getMultaVencimento());
			dto.setTaxaDia(entity.getTaxaDia());
			dto.setQtdeDiasServico(entity.getQtdeDiasServico());
			dto.setQtdeDiasVisita(entity.getQtdeDiasVisita());
			dto.setQtdeEntradaLivrePrestador(entity.getQtdeEntradaLivrePrestador());
			dto.setQtdeEntradaLivreVisitante(entity.getQtdeEntradaLivreVisitante());
			dto.setQtdeDiasMinimoReserva(entity.getQtdeDiasMinimoReserva());
			dto.setTamanhoMaximoArquivos(entity.getTamanhoMaximoArquivos());
			dto.setCpfEmissorDocumentos(entity.getCpfEmissorDocumentos());
			dto.setExibeTodasReservas(entity.isExibeTodasReservas());

			return dto;
		}

		@Override
		public ConfiguracaoCondominio createEntity(final ConfiguracaoCondominioDTO outer) {
			final ConfiguracaoCondominio entidade = new ConfiguracaoCondominio();
			entidade.setAbatimento(outer.getAbatimento());
			entidade.setAvisado(outer.isAvisado());
			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			}
			entidade.setDataFaturas(outer.getDataFaturas());
			entidade.setDesconto(outer.getDesconto());
			entidade.setIdConfiguracao(outer.getIdConfiguracao());
			entidade.setLimiteDias(outer.getLimiteDias());
			entidade.setMoraOutrosRecebimentos(outer.getMoraOutrosRecebimentos());
			entidade.setMultaVencimento(outer.getMultaVencimento());
			entidade.setTaxaDia(outer.getTaxaDia());
			entidade.setQtdeDiasServico(outer.getQtdeDiasServico());
			entidade.setQtdeDiasVisita(outer.getQtdeDiasVisita());
			entidade.setQtdeEntradaLivrePrestador(outer.getQtdeEntradaLivrePrestador());
			entidade.setQtdeEntradaLivreVisitante(outer.getQtdeEntradaLivreVisitante());
			entidade.setQtdeDiasMinimoReserva(outer.getQtdeDiasMinimoReserva());
			entidade.setTamanhoMaximoArquivos(outer.getTamanhoMaximoArquivos());
			entidade.setCpfEmissorDocumentos(outer.getCpfEmissorDocumentos());
			entidade.setExibeTodasReservas(outer.isExibeTodasReservas());

			return entidade;
		}

	}

}
