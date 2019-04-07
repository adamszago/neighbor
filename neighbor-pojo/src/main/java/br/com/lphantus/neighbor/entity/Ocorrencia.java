package br.com.lphantus.neighbor.entity;

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

import br.com.lphantus.neighbor.common.OcorrenciaDTO;

@Entity
@Table(name = "OCORRENCIA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ocorrencia implements IEntity<Long, OcorrenciaDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 224608768208771155L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_OCORRENCIA")
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_OCORRENCIA")
	private Date dataOcorrencia;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_REGISTRO_OCORRENCIA")
	private Date dataRegistroOcorrencia;

	@Column(name = "DESCRICAO_INCIDENCIA")
	private String descricao;

	@Column(name = "GRAVIDADE_OCORRENCIA")
	private String gravidade;

	@Column(name = "GEROU_NOTIFICACAO")
	private boolean gerouNotificacao;

	@Column(name = "GEROU_MULTA")
	private boolean gerouMulta;

	@Column(name = "ENTREGUE")
	private boolean entregue = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID_PESSOA"))
	private Pessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

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
	 * @return the dataOcorrencia
	 */
	public Date getDataOcorrencia() {
		return this.dataOcorrencia;
	}

	/**
	 * @param dataOcorrencia
	 *            the dataOcorrencia to set
	 */
	public void setDataOcorrencia(final Date dataOcorrencia) {
		this.dataOcorrencia = dataOcorrencia;
	}

	/**
	 * @return the dataRegistroOcorrencia
	 */
	public Date getDataRegistroOcorrencia() {
		return this.dataRegistroOcorrencia;
	}

	/**
	 * @param dataRegistroOcorrencia
	 *            the dataRegistroOcorrencia to set
	 */
	public void setDataRegistroOcorrencia(final Date dataRegistroOcorrencia) {
		this.dataRegistroOcorrencia = dataRegistroOcorrencia;
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
	 * @return the gravidade
	 */
	public String getGravidade() {
		return this.gravidade;
	}

	/**
	 * @param gravidade
	 *            the gravidade to set
	 */
	public void setGravidade(final String gravidade) {
		this.gravidade = gravidade;
	}

	/**
	 * @return the gerouNotificacao
	 */
	public boolean isGerouNotificacao() {
		return this.gerouNotificacao;
	}

	/**
	 * @param gerouNotificacao
	 *            the gerouNotificacao to set
	 */
	public void setGerouNotificacao(final boolean gerouNotificacao) {
		this.gerouNotificacao = gerouNotificacao;
	}

	/**
	 * @return the gerouMulta
	 */
	public boolean isGerouMulta() {
		return this.gerouMulta;
	}

	/**
	 * @param gerouMulta
	 *            the gerouMulta to set
	 */
	public void setGerouMulta(final boolean gerouMulta) {
		this.gerouMulta = gerouMulta;
	}

	/**
	 * @return the entregue
	 */
	public boolean isEntregue() {
		return this.entregue;
	}

	/**
	 * @param entregue
	 *            the entregue to set
	 */
	public void setEntregue(final boolean entregue) {
		this.entregue = entregue;
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
		if (!(obj instanceof Ocorrencia)) {
			return false;
		}
		final Ocorrencia other = (Ocorrencia) obj;
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
		return "Ocorrencia [id=" + this.id + ", dataOcorrencia="
				+ this.dataOcorrencia + ", dataRegistroOcorrencia="
				+ this.dataRegistroOcorrencia + ", descricao=" + this.descricao
				+ ", gravidade=" + this.gravidade + ", gerouNotificacao="
				+ this.gerouNotificacao + ", gerouMulta=" + this.gerouMulta
				+ ", entregue=" + this.entregue + ", pessoa=" + this.pessoa
				+ "]";
	}

	@Override
	public OcorrenciaDTO createDto() {
		return OcorrenciaDTO.Builder.getInstance().create(this);
	}

}
