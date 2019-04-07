package br.com.lphantus.neighbor.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.MovimentacaoDTO;

@Entity
@Table(name = "MOVIMENTACAO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Movimentacao implements IEntity<Long, MovimentacaoDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_MOVIMENTACAO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TEXTO_LIVRE")
	private String textoLivre;

	@Column(name = "VALOR_MOVIMENTACAO")
	private BigDecimal valor;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_MOVIMENTACAO")
	private Date dataMovimentacao;

	@Column(name = "DATA_CADASTRO_MOVIMENTACAO")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Column(name = "ATIVO_MOVIMENTACAO")
	private boolean ativo = true;

	@Column(name = "EXCLUIDO_MOVIMENTACAO")
	private boolean excluido = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_CARTEIRA", referencedColumnName = "ID_CARTEIRA") })
	private Carteira carteira;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name = "DUPLICATA_MOVIMENTACAO", joinColumns = { @JoinColumn(name = "ID_MOVIMENTACAO") }, inverseJoinColumns = { @JoinColumn(name = "ID_DUPLICATA") })
	private List<Duplicata> duplicatas;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movimentacao")
	private List<Rateio> rateios;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumns(value = { @JoinColumn(name = "ID_LANCAMENTO", referencedColumnName = "ID_LANCAMENTO") })
	private Lancamento lancamento;

	// determina se quitou a duplicata associada a movimentacao, ao dar baixa
	@Transient
	private boolean quitou;

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
	 * @return the textoLivre
	 */
	public String getTextoLivre() {
		return this.textoLivre;
	}

	/**
	 * @param textoLivre
	 *            the textoLivre to set
	 */
	public void setTextoLivre(final String textoLivre) {
		this.textoLivre = textoLivre;
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
	 * @return the dataMovimentacao
	 */
	public Date getDataMovimentacao() {
		return this.dataMovimentacao;
	}

	/**
	 * @param dataMovimentacao
	 *            the dataMovimentacao to set
	 */
	public void setDataMovimentacao(final Date dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
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
	 * @return the carteira
	 */
	public Carteira getCarteira() {
		return this.carteira;
	}

	/**
	 * @param carteira
	 *            the carteira to set
	 */
	public void setCarteira(final Carteira carteira) {
		this.carteira = carteira;
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
	 * @return the rateios
	 */
	public List<Rateio> getRateios() {
		return this.rateios;
	}

	/**
	 * @param rateios
	 *            the rateios to set
	 */
	public void setRateios(final List<Rateio> rateios) {
		this.rateios = rateios;
	}

	/**
	 * @return the lancamento
	 */
	public Lancamento getLancamento() {
		return lancamento;
	}

	/**
	 * @param lancamento
	 *            the lancamento to set
	 */
	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	/**
	 * @return the quitou
	 */
	public boolean isQuitou() {
		return this.quitou;
	}

	/**
	 * @param quitou
	 *            the quitou to set
	 */
	public void setQuitou(final boolean quitou) {
		this.quitou = quitou;
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
		if (!(obj instanceof Movimentacao)) {
			return false;
		}
		final Movimentacao other = (Movimentacao) obj;
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
		return "Movimentacao [id=" + this.id + ", textoLivre=" + this.textoLivre + ", valor=" + this.valor + ", dataMovimentacao=" + this.dataMovimentacao + ", dataCadastro="
				+ this.dataCadastro + ", ativo=" + this.ativo + ", excluido=" + this.excluido + ", carteira=" + this.carteira + ", duplicatas=" + this.duplicatas + ", quitou="
				+ this.quitou + "]";
	}

	@Override
	public MovimentacaoDTO createDto() {
		return MovimentacaoDTO.Builder.getInstance().create(this);
	}

}
