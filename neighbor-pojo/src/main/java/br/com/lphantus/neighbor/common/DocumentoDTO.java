package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Documento;

public class DocumentoDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private CondominioDTO condominio;
	private String descricaoDocumento;
	private String nomeDocumento;
	private String tipoDocumento;
	private Date dataCadastro;
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
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(CondominioDTO condominio) {
		this.condominio = condominio;
	}


	/**
	 * @return the descricaoDocumento
	 */
	public String getDescricaoDocumento() {
		return descricaoDocumento;
	}

	/**
	 * @param descricaoDocumento the descricaoDocumento to set
	 */
	public void setDescricaoDocumento(String descricaoDocumento) {
		this.descricaoDocumento = descricaoDocumento;
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
	 * @return the nomeDocumento
	 */
	public String getNomeDocumento() {
		return nomeDocumento;
	}

	/**
	 * @param nomeDocumento the nomeDocumento to set
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
	 * @param dataCadastro the dataCadastro to set
	 */
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
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
		DocumentoDTO other = (DocumentoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DocumentoDTO [id=" + id + ", condominio=" + condominio
				+ ", descricaoDocumento=" + descricaoDocumento
				+ ", nomeDocumento=" + nomeDocumento + ", tipoDocumento="
				+ tipoDocumento + ", dataCadastro=" + dataCadastro + ", ativo="
				+ ativo + "]";
	}

	public static class Builder extends DTOBuilder<DocumentoDTO, Documento> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public DocumentoDTO create(final Documento entidade) {
			final DocumentoDTO dto = new DocumentoDTO();
			
			dto.setAtivo(entidade.isAtivo());
			dto.setDescricaoDocumento(entidade.getDescricaoDocumento());
			if ( null != entidade.getCondominio() ){
				dto.setCondominio(entidade.getCondominio().createDto());
			}
			dto.setId(entidade.getId());
			dto.setNomeDocumento(entidade.getNomeDocumento());
			dto.setDataCadastro(entidade.getDataCadastro());
			dto.setTipoDocumento(entidade.getTipoDocumento());

			return dto;
		}

		@Override
		public Documento createEntity(final DocumentoDTO outer) {
			final Documento entidade = new Documento();
			
			entidade.setAtivo(outer.isAtivo());
			entidade.setDescricaoDocumento(outer.getDescricaoDocumento());
			if ( null != outer.getCondominio() ){
				entidade.setCondominio(CondominioDTO.Builder.getInstance().createEntity(outer.getCondominio()));
			}
			entidade.setId(outer.getId());
			entidade.setNomeDocumento(outer.getNomeDocumento());
			entidade.setDataCadastro(outer.getDataCadastro());
			entidade.setTipoDocumento(outer.getTipoDocumento());
			
			return entidade;
		}

	}

}
