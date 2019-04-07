package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.AgendaSindico;

public class AgendaSindicoDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataInicio;
	private Date dataFim;
	private String titulo;
	private String descricao;
	private CondominioDTO condominio;

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
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo
	 *            the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
		AgendaSindicoDTO other = (AgendaSindicoDTO) obj;
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
		return "AgendaSindicoDTO [id=" + id + ", dataInicio=" + dataInicio
				+ ", dataFim=" + dataFim + ", titulo=" + titulo
				+ ", descricao=" + descricao + ", condominio=" + condominio
				+ "]";
	}

	/**
	 * Classe para gerar o DTO a partir da entidade e vice-versa.
	 * 
	 * @author elias
	 *
	 */
	public static class Builder extends
			DTOBuilder<AgendaSindicoDTO, AgendaSindico> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public AgendaSindicoDTO create(AgendaSindico entity) {
			AgendaSindicoDTO retorno = new AgendaSindicoDTO();
			if (entity.getCondominio() != null) {
				retorno.setCondominio(entity.getCondominio().createDto());
			}
			retorno.setDataFim(entity.getDataFim());
			retorno.setDataInicio(entity.getDataInicio());
			retorno.setDescricao(entity.getDescricao());
			retorno.setId(entity.getId());
			retorno.setTitulo(entity.getTitulo());
			return retorno;
		}

		@Override
		public AgendaSindico createEntity(AgendaSindicoDTO outer) {
			AgendaSindico retorno = new AgendaSindico();
			if (outer.getCondominio() != null) {
				retorno.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}
			retorno.setDataFim(outer.getDataFim());
			retorno.setDataInicio(outer.getDataInicio());
			retorno.setDescricao(outer.getDescricao());
			retorno.setId(outer.getId());
			retorno.setTitulo(outer.getTitulo());
			return retorno;
		}
	}

}
