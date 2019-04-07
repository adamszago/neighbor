package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.BoletaDTO;
import br.com.lphantus.neighbor.enums.StatusBoleta;

@Entity
@Table(name = "BOLETO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Boleta implements IEntity<Long, BoletaDTO> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_BOLETO")
	private Long id;

	@Column(name = "DAT_DOC_BOL")
	@Temporal(TemporalType.DATE)
	private Date dataDocumento;

	@Column(name = "DAT_PROC_BOL")
	@Temporal(TemporalType.DATE)
	private Date dataProcessamento;

	@Column(name = "DAT_VENC_BOL")
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_EMISSOR", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio emissor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_SACADO", referencedColumnName = "ID_PESSOA"))
	private Pessoa sacado;

	@OneToOne(fetch = FetchType.LAZY, cascade = {})
	@JoinColumns(@JoinColumn(name = "ID_DUPLICATA", referencedColumnName = "ID_DUPLICATA"))
	private Duplicata duplicata;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CARTEIRA", referencedColumnName = "ID_CARTEIRA"))
	private Carteira carteira;

	@Enumerated
	@Column(name = "STATUS_BOLETO")
	private StatusBoleta statusBoleto;

	@Column(name = "LOCAL_PAGAMENTO_BOLETO")
	private String localParaPagamento;

	@Column(name = "INSTRUCAO_BOLETO_1")
	private String instrucao1;

	@Column(name = "INSTRUCAO_BOLETO_2")
	private String instrucao2;

	@Column(name = "INSTRUCAO_BOLETO_3")
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
	public Condominio getEmissor() {
		return this.emissor;
	}

	/**
	 * @param emissor
	 *            the emissor to set
	 */
	public void setEmissor(final Condominio emissor) {
		this.emissor = emissor;
	}

	/**
	 * @return the sacado
	 */
	public Pessoa getSacado() {
		return this.sacado;
	}

	/**
	 * @param sacado
	 *            the sacado to set
	 */
	public void setSacado(final Pessoa sacado) {
		this.sacado = sacado;
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
	 * @return the duplicata
	 */
	public Duplicata getDuplicata() {
		return this.duplicata;
	}

	/**
	 * @param duplicata
	 *            the duplicata to set
	 */
	public void setDuplicata(final Duplicata duplicata) {
		this.duplicata = duplicata;
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
		final Boleta other = (Boleta) obj;
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
		return "Boleta [id=" + this.id + ", dataDocumento=" + this.dataDocumento + ", dataProcessamento=" + this.dataProcessamento + ", dataVencimento=" + this.dataVencimento + ", emissor="
				+ this.emissor + ", sacado=" + this.sacado + ", duplicata=" + this.duplicata + ", carteira=" + this.carteira + ", statusBoleto=" + this.statusBoleto + ", localParaPagamento="
				+ this.localParaPagamento + ", instrucao1=" + this.instrucao1 + ", instrucao2=" + this.instrucao2 + ", instrucao3=" + this.instrucao3 + "]";
	}

	@Override
	public BoletaDTO createDto() {
		return BoletaDTO.Builder.getInstance().create(this);
	}

}
