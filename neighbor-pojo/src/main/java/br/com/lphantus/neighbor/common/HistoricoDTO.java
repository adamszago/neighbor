package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Historico;

public class HistoricoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataHoraAcao;
	private String acaoExecutada;
	private String usuario;
	private String condominio;
	private Long idCondominio;

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
	 * @return the dataHoraAcao
	 */
	public Date getDataHoraAcao() {
		return dataHoraAcao;
	}

	/**
	 * @param dataHoraAcao
	 *            the dataHoraAcao to set
	 */
	public void setDataHoraAcao(Date dataHoraAcao) {
		this.dataHoraAcao = dataHoraAcao;
	}

	/**
	 * @return the acaoExecutada
	 */
	public String getAcaoExecutada() {
		return acaoExecutada;
	}

	/**
	 * @param acaoExecutada
	 *            the acaoExecutada to set
	 */
	public void setAcaoExecutada(String acaoExecutada) {
		this.acaoExecutada = acaoExecutada;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the condominio
	 */
	public String getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(String condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the idCondominio
	 */
	public Long getIdCondominio() {
		return idCondominio;
	}

	/**
	 * @param idCondominio
	 *            the idCondominio to set
	 */
	public void setIdCondominio(Long idCondominio) {
		this.idCondominio = idCondominio;
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
		if (!(obj instanceof HistoricoDTO))
			return false;
		HistoricoDTO other = (HistoricoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder extends DTOBuilder<HistoricoDTO, Historico> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public HistoricoDTO create(Historico historico) {
			HistoricoDTO dto = new HistoricoDTO();
			dto.setAcaoExecutada(historico.getAcaoExecutada());
			dto.setCondominio(historico.getCondominio());
			dto.setDataHoraAcao(historico.getDataHoraAcao());
			dto.setId(historico.getId());
			dto.setIdCondominio(historico.getIdCondominio());
			dto.setUsuario(historico.getUsuario());
			return dto;
		}

		@Override
		public Historico createEntity(HistoricoDTO outer) {
			Historico entidade = new Historico();
			entidade.setAcaoExecutada(outer.getAcaoExecutada());
			entidade.setCondominio(outer.getCondominio());
			entidade.setDataHoraAcao(outer.getDataHoraAcao());
			entidade.setId(outer.getId());
			entidade.setIdCondominio(outer.getIdCondominio());
			entidade.setUsuario(outer.getUsuario());
			return entidade;
		}
	}

}
