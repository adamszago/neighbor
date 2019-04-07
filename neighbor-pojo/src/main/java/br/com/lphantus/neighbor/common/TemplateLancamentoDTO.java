package br.com.lphantus.neighbor.common;

import java.math.BigDecimal;
import java.util.Date;

import br.com.lphantus.neighbor.entity.TemplateLancamento;

public class TemplateLancamentoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataCadastro;
	private String nome;
	private BigDecimal valor;
	private String obs;
	private boolean ativo = true;
	private String casas;
	private String blocos;
	private String repeticao;
	private Date paramRepeticao;
	private LancamentoTipoDTO tipoLancamento;
	private CondominioDTO condominio;
	private CentroCustoDTO centroCusto;

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
	 * @return the obs
	 */
	public String getObs() {
		return this.obs;
	}

	/**
	 * @param obs
	 *            the obs to set
	 */
	public void setObs(final String obs) {
		this.obs = obs;
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
	 * @return the casas
	 */
	public String getCasas() {
		return this.casas;
	}

	/**
	 * @param casas
	 *            the casas to set
	 */
	public void setCasas(final String casas) {
		this.casas = casas;
	}

	/**
	 * @return the blocos
	 */
	public String getBlocos() {
		return this.blocos;
	}

	/**
	 * @param blocos
	 *            the blocos to set
	 */
	public void setBlocos(final String blocos) {
		this.blocos = blocos;
	}

	/**
	 * @return the repeticao
	 */
	public String getRepeticao() {
		return this.repeticao;
	}

	/**
	 * @param repeticao
	 *            the repeticao to set
	 */
	public void setRepeticao(final String repeticao) {
		this.repeticao = repeticao;
	}

	/**
	 * @return the paramRepeticao
	 */
	public Date getParamRepeticao() {
		return this.paramRepeticao;
	}

	/**
	 * @param paramRepeticao
	 *            the paramRepeticao to set
	 */
	public void setParamRepeticao(final Date paramRepeticao) {
		this.paramRepeticao = paramRepeticao;
	}

	/**
	 * @return the tipoLancamento
	 */
	public LancamentoTipoDTO getTipoLancamento() {
		return this.tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(final LancamentoTipoDTO tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
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
	 * @return the centroCusto
	 */
	public CentroCustoDTO getCentroCusto() {
		return this.centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(final CentroCustoDTO centroCusto) {
		this.centroCusto = centroCusto;
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
		if (!(obj instanceof TemplateLancamentoDTO)) {
			return false;
		}
		final TemplateLancamentoDTO other = (TemplateLancamentoDTO) obj;
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
		return "TemplateLancamentoDTO [id=" + this.id + ", dataCadastro="
				+ this.dataCadastro + ", nome=" + this.nome + ", valor="
				+ this.valor + ", obs=" + this.obs + ", ativo=" + this.ativo
				+ ", casas=" + this.casas + ", blocos=" + this.blocos
				+ ", repeticao=" + this.repeticao + ", paramRepeticao="
				+ this.paramRepeticao + ", tipoLancamento="
				+ this.tipoLancamento + ", condominio=" + this.condominio
				+ ", centroCusto=" + this.centroCusto + "]";
	}

	public static class Builder extends
			DTOBuilder<TemplateLancamentoDTO, TemplateLancamento> {

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
		public TemplateLancamentoDTO create(final TemplateLancamento entity) {
			final TemplateLancamentoDTO dto = new TemplateLancamentoDTO();
			dto.setBlocos(entity.getBlocos());
			dto.setCasas(entity.getCasas());
			if (null != entity.getCondominio()) {
				dto.setCondominio(entity.getCondominio().createDto());
			}
			dto.setDataCadastro(entity.getDataCadastro());
			dto.setAtivo(entity.isAtivo());
			dto.setId(entity.getId());
			dto.setNome(entity.getNome());
			dto.setObs(entity.getObs());
			dto.setParamRepeticao(entity.getParamRepeticao());
			dto.setRepeticao(entity.getRepeticao());
			if (null != entity.getTipoLancamento()) {
				dto.setTipoLancamento(entity.getTipoLancamento().createDto());
			}
			if (null != entity.getCentroCusto()) {
				dto.setCentroCusto(entity.getCentroCusto().createDto());
			}
			dto.setValor(entity.getValor());
			return dto;
		}

		@Override
		public TemplateLancamento createEntity(final TemplateLancamentoDTO outer) {
			final TemplateLancamento retorno = new TemplateLancamento();
			retorno.setBlocos(outer.getBlocos());
			retorno.setCasas(outer.getCasas());
			if (null != outer.getCondominio()) {
				retorno.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}
			retorno.setDataCadastro(outer.getDataCadastro());
			retorno.setAtivo(outer.isAtivo());
			retorno.setId(outer.getId());
			retorno.setNome(outer.getNome());
			retorno.setObs(outer.getObs());
			retorno.setParamRepeticao(outer.getParamRepeticao());
			retorno.setRepeticao(outer.getRepeticao());
			if (null != outer.getTipoLancamento()) {
				retorno.setTipoLancamento(LancamentoTipoDTO.Builder
						.getInstance().createEntity(outer.getTipoLancamento()));
			}
			if (null != outer.getCentroCusto()) {
				retorno.setCentroCusto(CentroCustoDTO.Builder.getInstance()
						.createEntity(outer.getCentroCusto()));
			}
			retorno.setValor(outer.getValor());
			return retorno;
		}
	}
}
