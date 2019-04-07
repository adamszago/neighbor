package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.TemplateLancamentoDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TEMPLATE_LANCAMENTO")
public class TemplateLancamento implements IEntity<Long, TemplateLancamentoDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_TEMPLATE")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CADASTRO_TEMPLATE")
	private Date dataCadastro;

	@Column(name = "NOME_TEMPLATE")
	private String nome;

	@Column(name = "VALOR_TEMPLATE")
	private BigDecimal valor;

	@Column(name = "OBS_TEMPLATE")
	private String obs;

	@Column(name = "ATIVO_TEMPLATE")
	private boolean ativo = true;

	@Column(name = "CASAS_TEMPLATE")
	private String casas;

	@Column(name = "BLOCOS_TEMPLATE")
	private String blocos;

	@Column(name = "RPT_TEMPLATE")
	private String repeticao;

	@Column(name = "PARAM_RPT_TEMPLATE")
	private Date paramRepeticao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TEMPLATE_TIPO")
	private LancamentoTipo tipoLancamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID_CENTRO_CUSTO"))
	private CentroCusto centroCusto;

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
	public LancamentoTipo getTipoLancamento() {
		return this.tipoLancamento;
	}

	/**
	 * @param tipoLancamento
	 *            the tipoLancamento to set
	 */
	public void setTipoLancamento(final LancamentoTipo tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
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
	 * @return the centroCusto
	 */
	public CentroCusto getCentroCusto() {
		return this.centroCusto;
	}

	/**
	 * @param centroCusto
	 *            the centroCusto to set
	 */
	public void setCentroCusto(final CentroCusto centroCusto) {
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
		if (!(obj instanceof TemplateLancamento)) {
			return false;
		}
		final TemplateLancamento other = (TemplateLancamento) obj;
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
		return "TemplateLancamento [id=" + this.id + ", dataCadastro="
				+ this.dataCadastro + ", nome=" + this.nome + ", valor="
				+ this.valor + ", obs=" + this.obs + ", ativo=" + this.ativo
				+ ", casas=" + this.casas + ", blocos=" + this.blocos
				+ ", repeticao=" + this.repeticao + ", paramRepeticao="
				+ this.paramRepeticao + ", tipoLancamento="
				+ this.tipoLancamento + ", condominio=" + this.condominio + "]";
	}

	@Override
	public TemplateLancamentoDTO createDto() {
		return TemplateLancamentoDTO.Builder.getInstance().create(this);
	}

}
