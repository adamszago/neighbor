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

import br.com.lphantus.neighbor.common.DocumentoDTO;

@Entity
@Table(name = "DOCUMENTO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Documento implements IEntity<Long, DocumentoDTO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_DOCUMENTO")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_CONDOMINIO", referencedColumnName = "ID_CONDOMINIO"))
	private Condominio condominio;

	@Column(name = "DESCRICAO_DOCUMENTO", length = 255)
	private String descricaoDocumento;

	@Column(name = "NOME_DOCUMENTO", length = 255)
	private String nomeDocumento;

	@Column(name = "TIPO_DOCUMENTO", length = 3)
	private String tipoDocumento;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_CADASTRO")
	private Date dataCadastro;

	@Column(name = "ATIVO_DOCUMENTO")
	private boolean ativo = true;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the condominio
	 */
	public Condominio getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(Condominio condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the descricaoDocumento
	 */
	public String getDescricaoDocumento() {
		return descricaoDocumento;
	}

	/**
	 * @param descricaoDocumento
	 *            the descricaoDocumento to set
	 */
	public void setDescricaoDocumento(String descricaoDocumento) {
		this.descricaoDocumento = descricaoDocumento;
	}

	/**
	 * @return the nomeDocumento
	 */
	public String getNomeDocumento() {
		return nomeDocumento;
	}

	/**
	 * @param nomeDocumento
	 *            the nomeDocumento to set
	 */
	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return dataCadastro;
	}

	/**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Documento other = (Documento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Documento [id=" + id + ", condominio=" + condominio
				+ ", descricaoDocumento=" + descricaoDocumento
				+ ", nomeDocumento=" + nomeDocumento + ", tipoDocumento="
				+ tipoDocumento + ", dataCadastro=" + dataCadastro + ", ativo="
				+ ativo + "]";
	}

	@Override
	public DocumentoDTO createDto() {
		return DocumentoDTO.Builder.getInstance().create(this);
	}

}
