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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.DuplicataDTO;

@Entity
@Table(name = "DUPLICATA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Duplicata implements IEntity<Long, DuplicataDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DUPLICATA")
	private Long id;

	@Column(name = "DAT_CAD_DUP")
	@Temporal(TemporalType.DATE)
	private Date dataCadastro;

	@Column(name = "ATIVO_DUPLICATA")
	private boolean ativo = true;

	@Column(name = "EXCLUIDO_DUPLICATA")
	private boolean excluido = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FATURA", referencedColumnName = "ID_FATURA")
	private Fatura fatura;

	@ManyToMany(mappedBy = "duplicatas", fetch = FetchType.LAZY)
	private List<Movimentacao> movimentacoes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "duplicata", cascade = {})
	private List<DuplicataParcela> parcelas;

	@Transient
	private boolean aberto;

	@Transient
	private BigDecimal valorPago;

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
	 * @return the valorPago
	 */
	public BigDecimal getValorPago() {
		/*
		 * calcular o valor pago através da entidade DuplicataMovimentação for
		 * (Movimentacao movimentacao : getMovimentacoes()) total +=
		 * movimentacao.getValor();
		 */
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
	 * @return the aberto
	 */
	public boolean isAberto() {
		if (null == getValorPago()) {
			return true;
		}
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
	 * @return the movimentacoes
	 */
	public List<Movimentacao> getMovimentacoes() {
		return this.movimentacoes;
	}

	/**
	 * @param movimentacoes
	 *            the movimentacoes to set
	 */
	public void setMovimentacoes(final List<Movimentacao> movimentacoes) {
		this.movimentacoes = movimentacoes;
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
	 * @return the parcelas
	 */
	public List<DuplicataParcela> getParcelas() {
		return this.parcelas;
	}

	/**
	 * @param parcelas
	 *            the parcelas to set
	 */
	public void setParcelas(final List<DuplicataParcela> parcelas) {
		this.parcelas = parcelas;
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
		final Duplicata other = (Duplicata) obj;
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
		return "Duplicata [id=" + this.id + ", dataCadastro="
				+ this.dataCadastro + ", ativo=" + this.ativo + ", excluido="
				+ this.excluido + ", fatura=" + this.fatura
				+ ", movimentacoes=" + this.movimentacoes + ", condominio="
				+ this.condominio + ", parcelas=" + this.parcelas + ", aberto="
				+ this.aberto + ", valorPago=" + this.valorPago + "]";
	}

	@Override
	public DuplicataDTO createDto() {
		return DuplicataDTO.Builder.getInstance().create(this);
	}

}
