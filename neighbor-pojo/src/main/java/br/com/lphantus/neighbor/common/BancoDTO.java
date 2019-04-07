package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Banco;

public class BancoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long codigoBanco;
	private String nomeBanco;
	private String sigla;
	private Date datacadastro;
	private CondominioDTO condominio;
	private boolean ativo = true;

	/**
	 * @return the codigoBanco
	 */
	public Long getCodigoBanco() {
		return codigoBanco;
	}

	/**
	 * @param codigoBanco
	 *            the codigoBanco to set
	 */
	public void setCodigoBanco(Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	/**
	 * @return the nomeBanco
	 */
	public String getNomeBanco() {
		return nomeBanco;
	}

	/**
	 * @param nomeBanco
	 *            the nomeBanco to set
	 */
	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla
	 *            the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the datacadastro
	 */
	public Date getDatacadastro() {
		return datacadastro;
	}

	/**
	 * @param datacadastro
	 *            the datacadastro to set
	 */
	public void setDatacadastro(Date datacadastro) {
		this.datacadastro = datacadastro;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoBanco == null) ? 0 : codigoBanco.hashCode());
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
		if (!(obj instanceof BancoDTO))
			return false;
		BancoDTO other = (BancoDTO) obj;
		if (codigoBanco == null) {
			if (other.codigoBanco != null)
				return false;
		} else if (!codigoBanco.equals(other.codigoBanco))
			return false;
		return true;
	}

	public static class Builder extends DTOBuilder<BancoDTO, Banco> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public BancoDTO create(final Banco banco) {
			final BancoDTO dto = new BancoDTO();

			dto.setAtivo(banco.isAtivo());
			dto.setCodigoBanco(banco.getCodigoBanco());

			if (null != banco.getCondominio()) {
				dto.setCondominio(banco.getCondominio().createDto());
			}

			dto.setDatacadastro(banco.getDatacadastro());
			dto.setNomeBanco(banco.getNomeBanco());
			dto.setSigla(banco.getSigla());

			return dto;
		}

		@Override
		public Banco createEntity(final BancoDTO outer) {
			final Banco entidade = new Banco();

			entidade.setAtivo(outer.isAtivo());
			entidade.setCodigoBanco(outer.getCodigoBanco());

			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			entidade.setDatacadastro(outer.getDatacadastro());
			entidade.setNomeBanco(outer.getNomeBanco());
			entidade.setSigla(outer.getSigla());
			return entidade;
		}

	}
}
