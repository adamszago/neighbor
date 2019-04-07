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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.SQLDelete;

import br.com.lphantus.neighbor.common.CarteiraDTO;

@Entity
@Table(name = "CARTEIRA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SQLDelete(sql = "update Carteira set ATIVO_CARTEIRA = 0 where id_carteira=?")
public class Carteira implements IEntity<Long, CarteiraDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_CARTEIRA")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NOME")
	private String nome;

	@Column(name = "DESCRICAO")
	private String descricao;

	@Column(name = "SALDO")
	private BigDecimal saldo;

	@Column(name = "ATIVO")
	private boolean ativo = true;

	@Column(name = "EXCLUIDO")
	private boolean excluido = false;

	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Column(name = "CC_CARTEIRA", nullable = true)
	private String numeroConta;

	@Column(name = "DIG_CC", nullable = true)
	private String digitoConta;

	@Column(name = "AGENCIA", nullable = true)
	private String numeroAgencia;

	@Column(name = "DIG_AGENCIA", nullable = true)
	private String digitoAgencia;

	@Column(name = "NOSSO_NUM", nullable = true)
	private String nossoNumero;

	@Column(name = "DIG_NOSSO_NUM", nullable = true)
	private String digitoNossoNumero;

	@Column(name = "NUMERO_CARTEIRA", nullable = true)
	private String numeroCarteira;

	@Column(name = "CODIGO_BANCO", nullable = true)
	private String banco;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	/**
	 * @return the id
	 */
	public Long getId() {
		if (this.id != null) {
			this.id = this.id.equals(Long.valueOf(0)) ? null : this.id;
		}
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
	 * @return the descricao
	 */
	public String getDescricao() {
		return this.descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return this.saldo;
	}

	/**
	 * @param saldo
	 *            the saldo to set
	 */
	public void setSaldo(final BigDecimal saldo) {
		this.saldo = saldo;
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
	 * @return the banco
	 */
	public String getBanco() {
		return this.banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(final String banco) {
		this.banco = banco;
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
	 * @return the numeroConta
	 */
	public String getNumeroConta() {
		return this.numeroConta;
	}

	/**
	 * @param numeroConta
	 *            the numeroConta to set
	 */
	public void setNumeroConta(final String numeroConta) {
		this.numeroConta = numeroConta;
	}

	/**
	 * @return the digitoConta
	 */
	public String getDigitoConta() {
		return this.digitoConta;
	}

	/**
	 * @param digitoConta
	 *            the digitoConta to set
	 */
	public void setDigitoConta(final String digitoConta) {
		this.digitoConta = digitoConta;
	}

	/**
	 * @return the numeroAgencia
	 */
	public String getNumeroAgencia() {
		return this.numeroAgencia;
	}

	/**
	 * @param numeroAgencia
	 *            the numeroAgencia to set
	 */
	public void setNumeroAgencia(final String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	/**
	 * @return the digitoAgencia
	 */
	public String getDigitoAgencia() {
		return this.digitoAgencia;
	}

	/**
	 * @param digitoAgencia
	 *            the digitoAgencia to set
	 */
	public void setDigitoAgencia(final String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	/**
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return this.nossoNumero;
	}

	/**
	 * @param nossoNumero
	 *            the nossoNumero to set
	 */
	public void setNossoNumero(final String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	/**
	 * @return the digitoNossoNumero
	 */
	public String getDigitoNossoNumero() {
		return this.digitoNossoNumero;
	}

	/**
	 * @param digitoNossoNumero
	 *            the digitoNossoNumero to set
	 */
	public void setDigitoNossoNumero(final String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	/**
	 * @return the numeroCarteira
	 */
	public String getNumeroCarteira() {
		return this.numeroCarteira;
	}

	/**
	 * @param numeroCarteira
	 *            the numeroCarteira to set
	 */
	public void setNumeroCarteira(final String numeroCarteira) {
		this.numeroCarteira = numeroCarteira;
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
		final Carteira other = (Carteira) obj;
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
		return "Carteira [id=" + this.id + ", nome=" + this.nome
				+ ", descricao=" + this.descricao + ", saldo=" + this.saldo
				+ ", ativo=" + this.ativo + ", excluido=" + this.excluido
				+ ", dataCadastro=" + this.dataCadastro + ", numeroConta="
				+ this.numeroConta + ", digitoConta=" + this.digitoConta
				+ ", numeroAgencia=" + this.numeroAgencia + ", digitoAgencia="
				+ this.digitoAgencia + ", nossoNumero=" + this.nossoNumero
				+ ", digitoNossoNumero=" + this.digitoNossoNumero
				+ ", numeroCarteira=" + this.numeroCarteira + ", banco="
				+ this.banco + ", condominio=" + this.condominio + "]";
	}

	@Override
	public CarteiraDTO createDto() {
		return CarteiraDTO.Builder.getInstance().create(this);
	}

}
