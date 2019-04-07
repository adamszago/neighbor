package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Condominio;

public class CondominioDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private String nomeAbreviado;
	private boolean possuiBloco;
	private PessoaJuridicaDTO pessoa;
	private EnderecoDTO endereco;
	private Long numero;
	private String complemento;
	private boolean contaSuspensa = false;

	/**
	 * @return the nomeAbreviado
	 */
	public String getNomeAbreviado() {
		return this.nomeAbreviado;
	}

	/**
	 * @param nomeAbreviado
	 *            the nomeAbreviado to set
	 */
	public void setNomeAbreviado(final String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}

	/**
	 * @return the possuiBloco
	 */
	public boolean isPossuiBloco() {
		return this.possuiBloco;
	}

	/**
	 * @param possuiBloco
	 *            the possuiBloco to set
	 */
	public void setPossuiBloco(final boolean possuiBloco) {
		this.possuiBloco = possuiBloco;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaJuridicaDTO getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaJuridicaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the endereco
	 */
	public EnderecoDTO getEndereco() {
		if (null == this.endereco) {
			this.endereco = new EnderecoDTO();
		}
		return this.endereco;
	}

	/**
	 * @param endereco
	 *            the endereco to set
	 */
	public void setEndereco(final EnderecoDTO endereco) {
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

	/**
	 * @return the contaSuspensa
	 */
	public boolean isContaSuspensa() {
		return this.contaSuspensa;
	}

	/**
	 * @param contaSuspensa
	 *            the contaSuspensa to set
	 */
	public void setContaSuspensa(final boolean contaSuspensa) {
		this.contaSuspensa = contaSuspensa;
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
				+ ((this.pessoa == null) ? 0 : this.pessoa.hashCode());
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
		if (!(obj instanceof CondominioDTO)) {
			return false;
		}
		final CondominioDTO other = (CondominioDTO) obj;
		if (this.pessoa == null) {
			if (other.pessoa != null) {
				return false;
			}
		} else if (!this.pessoa.equals(other.pessoa)) {
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
		return "CondominioDTO [nomeAbreviado=" + this.nomeAbreviado
				+ ", possuiBloco=" + this.possuiBloco + ", pessoa="
				+ this.pessoa + ", endereco=" + this.endereco + ", numero="
				+ this.numero + ", complemento=" + this.complemento + "]";
	}

	public static final class Builder extends
			DTOBuilder<CondominioDTO, Condominio> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public CondominioDTO create(final Condominio entity) {
			final CondominioDTO dto = new CondominioDTO();

			dto.setNomeAbreviado(entity.getNomeAbreviado());
			dto.setPossuiBloco(entity.isPossuiBloco());
			dto.setPessoa(PessoaJuridicaDTO.Builder.getInstance()
					.create(entity));

			if (null != entity.getEndereco()) {
				dto.setEndereco(entity.getEndereco().createDto());
			}

			dto.setComplemento(entity.getComplemento());
			dto.setNumero(entity.getNumero());
			dto.setContaSuspensa(entity.isContaSuspensa());

			return dto;
		}

		@Override
		public Condominio createEntity(final CondominioDTO outer) {
			final Condominio entity = PessoaJuridicaDTO.Builder.getInstance()
					.createEntityCondominio(outer.getPessoa());

			entity.setNomeAbreviado(outer.getNomeAbreviado());
			entity.setPossuiBloco(outer.isPossuiBloco());

			if (null != outer.getEndereco()) {
				entity.setEndereco(EnderecoDTO.Builder.getInstance()
						.createEntity(outer.getEndereco()));
			}

			entity.setNumero(outer.getNumero());
			entity.setComplemento(outer.getComplemento());
			entity.setContaSuspensa(outer.isContaSuspensa());

			return entity;
		}
	}

}
