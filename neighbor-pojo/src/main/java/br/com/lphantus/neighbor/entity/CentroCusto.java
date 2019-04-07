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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.CentroCustoDTO;

@Entity
@Table(name = "CENTRO_CUSTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CentroCusto implements IEntity<Long, CentroCustoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CENTRO_CUSTO")
	private Long id;

	@Column(name = "NOME_CENTRO_CUSTO")
	private String nome;

	@Column(name = "DESCRICAO_CENTRO_CUSTO")
	private String descricao;

	@Column(name = "LANCAVEL_CENTRO_CUSTO")
	private boolean lancavel = true;

	@Column(name = "ATIVO_CENTRO_CUSTO")
	private boolean ativo = true;

	@Column(name = "EXCLUIDO_CENTRO_CUSTO")
	private boolean excluido = false;

	@Column(name = "DATA_CADASTRO_CENTRO_CUSTO")
	private Date dataCadastro;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(value = { @JoinColumn(name = "ID_CENTRO_CUSTO_PAI", referencedColumnName = "ID_CENTRO_CUSTO") })
	private CentroCusto centroCustoPai;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

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

	public String getStatus() {
		if (this.ativo) {
			return "Ativo";
		} else {
			return "Inativo";
		}
	}

	public String getLancavelText() {
		if (this.lancavel) {
			return "Sim";
		} else {
			return "Não";
		}
	}

	public Long getId() {
		if (this.id != null) {
			this.id = this.id.equals(Long.valueOf(0)) ? null : this.id;
		}
		return this.id;
	}

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
	 * @return the lancavel
	 */
	public boolean isLancavel() {
		return this.lancavel;
	}

	/**
	 * @param lancavel
	 *            the lancavel to set
	 */
	public void setLancavel(final boolean lancavel) {
		this.lancavel = lancavel;
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
	 * @return the centroCustoPai
	 */
	public CentroCusto getCentroCustoPai() {
		return this.centroCustoPai;
	}

	/**
	 * @param centroCustoPai
	 *            the centroCustoPai to set
	 */
	public void setCentroCustoPai(final CentroCusto centroCustoPai) {
		this.centroCustoPai = centroCustoPai;
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
		final CentroCusto other = (CentroCusto) obj;
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
	public String toString() {
		return "CentroCusto [id=" + this.id + ", nome=" + this.nome
				+ ", descricao=" + this.descricao + ", lancavel="
				+ this.lancavel + ", centroCustoPai=" + this.centroCustoPai
				+ ", ativo=" + this.ativo + ", excluido=" + this.excluido
				+ ", condominio=" + this.condominio + ", dataCadastro="
				+ this.dataCadastro + "]";
	}

	@Override
	public CentroCustoDTO createDto() {
		return CentroCustoDTO.Builder.getInstance().create(this);
	}

}
