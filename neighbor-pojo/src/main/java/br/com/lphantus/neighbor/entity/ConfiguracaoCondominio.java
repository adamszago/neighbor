package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;

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

import br.com.lphantus.neighbor.common.ConfiguracaoCondominioDTO;

@Entity
@Table(name = "CONFIG_CONDOMINIO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfiguracaoCondominio implements
		IEntity<Long, ConfiguracaoCondominioDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CONFIGURACAO")
	private Long idConfiguracao;

	// ------------------------------------------------------- FINANCEIRO
	
	@Column(name = "DT_FATURAS", nullable = false)
	private Integer dataFaturas;

	@Column(name = "LIM_DIAS", nullable = true)
	private Long limiteDias;

	@Column(name = "DUP_MORA_RECBTO", nullable = true)
	private BigDecimal moraOutrosRecebimentos;

	@Column(name = "DUP_MULT_VENCTO", nullable = true)
	private BigDecimal multaVencimento;

	@Column(name = "DUP_TX_DIA", nullable = true)
	private BigDecimal taxaDia;

	@Column(name = "DUP_DESC", nullable = true)
	private BigDecimal desconto;

	@Column(name = "DUP_ABAT", nullable = true)
	private BigDecimal abatimento;
	
	// ------------------------------------------------------- FINANCEIRO

	@Column(name = "USU_AVISADO")
	private boolean avisado = false;

	@Column(name = "QTD_DIAS_VISITA")
	private Integer qtdeDiasVisita;

	@Column(name = "QTD_DIAS_SERVICO")
	private Integer qtdeDiasServico;

	@Column(name = "QTD_ENT_LIV_PRE", nullable = true)
	private Integer qtdeEntradaLivrePrestador;

	@Column(name = "QTD_ENT_LIV_VIS", nullable = true)
	private Integer qtdeEntradaLivreVisitante;

	@Column(name = "QTD_DIAS_MIN_RES", nullable = true)
	private Integer qtdeDiasMinimoReserva;

	// em megabytes
	@Column(name = "TAMANHO_MAXIMO_ARQUIVOS", nullable = true)
	private Integer tamanhoMaximoArquivos;

	@Column(name = "CPF_EMISSOR_DOCUMENTOS", nullable = true)
	private String cpfEmissorDocumentos;
	
	@Column(name = "NOME_EMISSOR_DOCUMENTOS", nullable = true)
	private String nomeEmissorDocumentos;
	
	@Column(name = "EXIBE_TODAS_RESERVAS")
	private boolean exibeTodasReservas = false;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, optional = false, targetEntity = Condominio.class)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

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
	 * @param nomeEmissorDocumentos the nomeEmissorDocumentos to set
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
	 * @param exibeTodasReservas the exibeTodasReservas to set
	 */
	public void setExibeTodasReservas(boolean exibeTodasReservas) {
		this.exibeTodasReservas = exibeTodasReservas;
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
				+ ((this.idConfiguracao == null) ? 0 : this.idConfiguracao
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
		if (!(obj instanceof ConfiguracaoCondominio)) {
			return false;
		}
		final ConfiguracaoCondominio other = (ConfiguracaoCondominio) obj;
		if (this.idConfiguracao == null) {
			if (other.idConfiguracao != null) {
				return false;
			}
		} else if (!this.idConfiguracao.equals(other.idConfiguracao)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfiguracaoCondominio [idConfiguracao=" + idConfiguracao
				+ ", dataFaturas=" + dataFaturas + ", limiteDias=" + limiteDias
				+ ", moraOutrosRecebimentos=" + moraOutrosRecebimentos
				+ ", multaVencimento=" + multaVencimento + ", taxaDia="
				+ taxaDia + ", desconto=" + desconto + ", abatimento="
				+ abatimento + ", avisado=" + avisado + ", qtdeDiasVisita="
				+ qtdeDiasVisita + ", qtdeDiasServico=" + qtdeDiasServico
				+ ", qtdeEntradaLivrePrestador=" + qtdeEntradaLivrePrestador
				+ ", qtdeEntradaLivreVisitante=" + qtdeEntradaLivreVisitante
				+ ", qtdeDiasMinimoReserva=" + qtdeDiasMinimoReserva
				+ ", tamanhoMaximoArquivos=" + tamanhoMaximoArquivos
				+ ", cpfEmissorDocumentos=" + cpfEmissorDocumentos
				+ ", nomeEmissorDocumentos=" + nomeEmissorDocumentos
				+ ", condominio=" + condominio + "]";
	}

	@Override
	public ConfiguracaoCondominioDTO createDto() {
		return ConfiguracaoCondominioDTO.Builder.getInstance().create(this);
	}

}
