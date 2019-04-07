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
import org.hibernate.annotations.SQLDelete;

import br.com.lphantus.neighbor.common.LancamentoDTO;

@Entity
@Table(name = "LANCAMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SQLDelete(sql = "update Lancamento set ATIVO_LANCAMENTO = 0 where id_lancamento =?")
public class Lancamento implements IEntity<Long, LancamentoDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_LANCAMENTO")
	private Long id;

	@Column(name = "NOME_LANCAMENTO")
	private String nome;

	@Column(name = "VALOR_LANCAMENTO")
	private BigDecimal valor;

	@Column(name = "DATA_LANCAMENTO")
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name = "OBS_LANCAMENTO")
	private String obs;

	@Column(name = "ATIVO_LANCAMENTO")
	private boolean ativo = true;

	@Column(name = "EXCLUIDO_LANCAMENTO")
	private boolean excluido = false;

	@Column(name = "DATA_CADASTRO_LANCAMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_LANCAMENTO_TIPO", referencedColumnName = "ID_LANCAMENTO_TIPO"))
	private LancamentoTipo tipoLancamento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA", insertable = true, updatable = false))
	private Pessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_FATURA", referencedColumnName = "ID_FATURA"))
	private Fatura fatura;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID_CENTRO_CUSTO"))
	private CentroCusto centroCusto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CARTEIRA", referencedColumnName = "ID_CARTEIRA"))
	private Carteira carteira;

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
	 * @return the data
	 */
	public Date getData() {
		return this.data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(final Date data) {
		this.data = data;
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
	public Boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the excluido
	 */
	public Boolean isExcluido() {
		return this.excluido;
	}

	/**
	 * @param excluido
	 *            the excluido to set
	 */
	public void setExcluido(final Boolean excluido) {
		this.excluido = excluido;
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
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the fatura
	 */
	public Fatura getFatura() {
		return this.fatura;
	}

	/**
	 * @param fatura
	 *            the fatura to set
	 */
	public void setFatura(final Fatura fatura) {
		this.fatura = fatura;
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

	/**
	 * @return the carteira
	 */
	public Carteira getCarteira() {
		return carteira;
	}

	/**
	 * @param carteira
	 *            the carteira to set
	 */
	public void setCarteira(Carteira carteira) {
		this.carteira = carteira;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Lancamento other = (Lancamento) obj;
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
		return "Lancamento [id=" + id + ", nome=" + nome + ", valor=" + valor + ", data=" + data + ", obs=" + obs + ", ativo=" 
				+ ativo + ", excluido=" + excluido + ", dataCadastro=" + dataCadastro + ", tipoLancamento=" 
				+ tipoLancamento + ", pessoa=" + pessoa + ", fatura=" + fatura + ", condominio=" + condominio
				+ ", centroCusto=" + centroCusto + ", carteira=" + carteira + "]";
	}

	@Override
	public LancamentoDTO createDto() {
		return LancamentoDTO.Builder.getInstance().create(this);
	}

}
