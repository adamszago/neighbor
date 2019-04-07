package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.FaturaDTO;

@Entity
@Table(name = "FATURA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fatura implements IEntity<Long, FaturaDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_FATURA")
	private Long id;

	@Column(name = "NOME_FATURA")
	private String nome;

	@Column(name = "VALOR_FATURA")
	private BigDecimal valor;

	@Column(name = "DATA_FATURA")
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name = "DATA_CADASTRO_FATURA")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(name = "DATA_PAGAMENTO_FATURA")
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	@Column(name = "OBS_FATURA")
	private String obs;

	@Column(name = "ATIVO_FATURA")
	private Boolean ativo = true;

	@Column(name = "EXCLUIDO_FATURA")
	private Boolean excluido = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@OneToMany(mappedBy = "fatura", fetch = FetchType.LAZY)
	private List<Lancamento> lancamentos;

	@OneToMany(mappedBy = "fatura", fetch = FetchType.LAZY)
	private List<Duplicata> duplicatas;

	@Transient
	private boolean aberto;

	@Transient
	private BigDecimal valorPago;

	@Transient
	private BigDecimal valoraPagar; // Valor a Pagar das duplicatas em aberto,

	// com
	// cálculo de juros

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
	 * @return the valoraPagar
	 */
	public BigDecimal getValoraPagar() {
		return this.valoraPagar;
	}

	/**
	 * @param valoraPagar
	 *            the valoraPagar to set
	 */
	public void setValoraPagar(final BigDecimal valoraPagar) {
		this.valoraPagar = valoraPagar;
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
	public Boolean getAtivo() {
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
	public Boolean getExcluido() {
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
	 * @return the lancamentos
	 */
	public List<Lancamento> getLancamentos() {
		return this.lancamentos;
	}

	/**
	 * @param lancamentos
	 *            the lancamentos to set
	 */
	public void setLancamentos(final List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	/**
	 * @return the duplicatas
	 */
	public List<Duplicata> getDuplicatas() {
		return this.duplicatas;
	}

	/**
	 * @param duplicatas
	 *            the duplicatas to set
	 */
	public void setDuplicatas(final List<Duplicata> duplicatas) {
		this.duplicatas = duplicatas;
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

	@Override
	public String toString() {
		return "Fatura [id=" + this.id + ", nome=" + this.nome + ", valor="
				+ this.valor + ", valorPago=" + this.valorPago
				+ ", valoraPagar=" + this.valoraPagar + ", data=" + this.data
				+ ", dataCadastro=" + this.dataCadastro + ", dataPagamento="
				+ this.dataPagamento + ", aberto=" + this.aberto + ", obs="
				+ this.obs + ", ativo=" + this.ativo + ", excluido="
				+ this.excluido + ", lancamentos=" + this.lancamentos
				+ ", duplicatas=" + this.duplicatas + ", condominio="
				+ this.condominio + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		final Fatura other = (Fatura) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public FaturaDTO createDto() {
		return FaturaDTO.Builder.getInstance().create(this);
	}

}
