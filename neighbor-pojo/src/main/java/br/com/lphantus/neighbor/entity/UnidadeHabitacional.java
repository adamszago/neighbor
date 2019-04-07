package br.com.lphantus.neighbor.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;

@Entity
@Table(name = "UNIDADE_HABITACIONAL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UnidadeHabitacional implements
		IEntity<Long, UnidadeHabitacionalDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_UNIDADE_HABITACIONAL")
	private Long idUnidade;

	@Column(name = "IDENTIFICACAO")
	private String identificacao;

	@Column(name = "BLOCO")
	private String bloco;

	@Column(name = "INTERFONE_OK")
	private boolean interfoneOk;

	@OneToMany(mappedBy = "unidadeHabitacional")
	private List<MoradorUnidadeHabitacional> moradores;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, optional = false, targetEntity = Condominio.class)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@Column(name = "ATIVO")
	private boolean ativo = true;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_ENDERECO", referencedColumnName = "ID_ENDERECO"))
	private Endereco endereco;

	@Column(name = "NUMERO")
	private Long numero;

	@Column(name = "COMPLEMENTO")
	private String complemento;

	/**
	 * @return the idUnidade
	 */
	public Long getIdUnidade() {
		return this.idUnidade;
	}

	/**
	 * @param idUnidade
	 *            the idUnidade to set
	 */
	public void setIdUnidade(final Long idUnidade) {
		this.idUnidade = idUnidade;
	}

	/**
	 * @return the identificacao
	 */
	public String getIdentificacao() {
		return this.identificacao;
	}

	/**
	 * @param identificacao
	 *            the identificacao to set
	 */
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @return the bloco
	 */
	public String getBloco() {
		return this.bloco;
	}

	/**
	 * @param bloco
	 *            the bloco to set
	 */
	public void setBloco(final String bloco) {
		this.bloco = bloco;
	}

	/**
	 * @return the interfoneOk
	 */
	public boolean isInterfoneOk() {
		return this.interfoneOk;
	}

	/**
	 * @param interfoneOk
	 *            the interfoneOk to set
	 */
	public void setInterfoneOk(final boolean interfoneOk) {
		this.interfoneOk = interfoneOk;
	}

	/**
	 * @return the moradores
	 */
	public List<MoradorUnidadeHabitacional> getMoradores() {
		return this.moradores;
	}

	/**
	 * @param moradores
	 *            the moradores to set
	 */
	public void setMoradores(final List<MoradorUnidadeHabitacional> moradores) {
		this.moradores = moradores;
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
	 * @return the endereco
	 */
	public Endereco getEndereco() {
		return this.endereco;
	}

	/**
	 * @param endereco
	 *            the endereco to set
	 */
	public void setEndereco(final Endereco endereco) {
		this.endereco = endereco;
	}

	/**
	 * @return the numero
	 */
	public Long getNumero() {
		return this.numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(final Long numero) {
		this.numero = numero;
	}

	/**
	 * @return the complemento
	 */
	public String getComplemento() {
		return this.complemento;
	}

	/**
	 * @param complemento
	 *            the complemento to set
	 */
	public void setComplemento(final String complemento) {
		this.complemento = complemento;
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
				+ ((this.idUnidade == null) ? 0 : this.idUnidade.hashCode());
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
		if (!(obj instanceof UnidadeHabitacional)) {
			return false;
		}
		final UnidadeHabitacional other = (UnidadeHabitacional) obj;
		if (this.idUnidade == null) {
			if (other.idUnidade != null) {
				return false;
			}
		} else if (!this.idUnidade.equals(other.idUnidade)) {
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
		return "UnidadeHabitacional [idUnidade=" + this.idUnidade
				+ ", identificacao=" + this.identificacao + ", bloco="
				+ this.bloco + ", interfoneOk=" + this.interfoneOk
				+ ", condominio=" + this.condominio + ", ativo=" + this.ativo
				+ ", endereco=" + this.endereco + ", numero=" + this.numero
				+ ", complemento=" + this.complemento + "]";
	}

	@Override
	public UnidadeHabitacionalDTO createDto() {
		return UnidadeHabitacionalDTO.Builder.getInstance().create(this);
	}

}
