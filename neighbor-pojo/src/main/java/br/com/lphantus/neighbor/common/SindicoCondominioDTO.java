package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.SindicoCondominio;

public class SindicoCondominioDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private SindicoDTO sindico;
	private CondominioDTO condominio;
	private Date inicioMandato;
	private Date fimMandato;

	/**
	 * @return the sindico
	 */
	public SindicoDTO getSindico() {
		return this.sindico;
	}

	/**
	 * @param sindico
	 *            the sindico to set
	 */
	public void setSindico(final SindicoDTO sindico) {
		this.sindico = sindico;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final CondominioDTO condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the inicioMandato
	 */
	public Date getInicioMandato() {
		return this.inicioMandato;
	}

	/**
	 * @param inicioMandato
	 *            the inicioMandato to set
	 */
	public void setInicioMandato(final Date inicioMandato) {
		this.inicioMandato = inicioMandato;
	}

	/**
	 * @return the fimMandato
	 */
	public Date getFimMandato() {
		return this.fimMandato;
	}

	/**
	 * @param fimMandato
	 *            the fimMandato to set
	 */
	public void setFimMandato(final Date fimMandato) {
		this.fimMandato = fimMandato;
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
				+ ((this.condominio == null) ? 0 : this.condominio.hashCode());
		result = (prime * result)
				+ ((this.sindico == null) ? 0 : this.sindico.hashCode());
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
		if (!(obj instanceof SindicoCondominioDTO)) {
			return false;
		}
		final SindicoCondominioDTO other = (SindicoCondominioDTO) obj;
		if (this.condominio == null) {
			if (other.condominio != null) {
				return false;
			}
		} else if (!this.condominio.equals(other.condominio)) {
			return false;
		}
		if (this.sindico == null) {
			if (other.sindico != null) {
				return false;
			}
		} else if (!this.sindico.equals(other.sindico)) {
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
		return "SindicoCondominioDTO [sindico=" + this.sindico
				+ ", condominio=" + this.condominio + ", inicioMandato="
				+ this.inicioMandato + ", fimMandato=" + this.fimMandato + "]";
	}

	public static class Builder extends
			DTOBuilder<SindicoCondominioDTO, SindicoCondominio> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public SindicoCondominioDTO create(final SindicoCondominio entity) {
			final SindicoCondominioDTO retorno = new SindicoCondominioDTO();

			retorno.setInicioMandato(entity.getInicioMandato());
			retorno.setFimMandato(entity.getFimMandato());
			// TODO: revisar
			// retorno.setCondominio(CondominioDTO.Builder.getInstance()
			// .createList(entity.getCondominio()));
			// retorno.setSindico(SindicoDTO.Builder.getInstance().createList(
			// entity.getSindico()));

			return retorno;
		}

		@Override
		public SindicoCondominio createEntity(final SindicoCondominioDTO outer) {
			final SindicoCondominio retorno = new SindicoCondominio();

			retorno.setInicioMandato(outer.getInicioMandato());
			retorno.setFimMandato(outer.getFimMandato());

			// TODO: revisar
			// retorno.setCondominio(CondominioDTO.Builder.getInstance()
			// .createListEntity(outer.getCondominio()));
			// retorno.setSindico(SindicoDTO.Builder.getInstance()
			// .createListEntity(outer.getSindico()));

			return retorno;
		}
	}

}
