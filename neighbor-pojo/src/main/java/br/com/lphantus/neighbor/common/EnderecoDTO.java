package br.com.lphantus.neighbor.common;

import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;

import br.com.lphantus.neighbor.entity.Endereco;

public class EnderecoDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long idEndereco;
	private String logradouro;
	private String bairro;
	private String cidade;
	private UnidadeFederativa uf;
	private String cep;

	/**
	 * @return the idEndereco
	 */
	public Long getIdEndereco() {
		return this.idEndereco;
	}

	/**
	 * @param idEndereco
	 *            the idEndereco to set
	 */
	public void setIdEndereco(final Long idEndereco) {
		this.idEndereco = idEndereco;
	}

	/**
	 * @return the logradouro
	 */
	public String getLogradouro() {
		return this.logradouro;
	}

	/**
	 * @param logradouro
	 *            the logradouro to set
	 */
	public void setLogradouro(final String logradouro) {
		this.logradouro = logradouro;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return this.bairro;
	}

	/**
	 * @param bairro
	 *            the bairro to set
	 */
	public void setBairro(final String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the cidade
	 */
	public String getCidade() {
		return this.cidade;
	}

	/**
	 * @param cidade
	 *            the cidade to set
	 */
	public void setCidade(final String cidade) {
		this.cidade = cidade;
	}

	/**
	 * @return the uf
	 */
	public UnidadeFederativa getUf() {
		return this.uf;
	}

	/**
	 * @param uf
	 *            the uf to set
	 */
	public void setUf(final UnidadeFederativa uf) {
		this.uf = uf;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return this.cep;
	}

	/**
	 * @param cep
	 *            the cep to set
	 */
	public void setCep(final String cep) {
		this.cep = cep;
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
				+ ((this.idEndereco == null) ? 0 : this.idEndereco.hashCode());
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
		if (!(obj instanceof EnderecoDTO)) {
			return false;
		}
		final EnderecoDTO other = (EnderecoDTO) obj;
		if (this.idEndereco == null) {
			if (other.idEndereco != null) {
				return false;
			}
		} else if (!this.idEndereco.equals(other.idEndereco)) {
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
		return "EnderecoDTO [idEndereco=" + this.idEndereco + ", logradouro="
				+ this.logradouro + ", bairro=" + this.bairro + ", cidade="
				+ this.cidade + ", uf=" + this.uf + ", cep=" + this.cep + "]";
	}

	public static class Builder extends DTOBuilder<EnderecoDTO, Endereco> {

		private static Builder instance;

		private Builder() {

		}

		public static Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public EnderecoDTO create(final Endereco entity) {
			final EnderecoDTO dto = new EnderecoDTO();
			dto.setBairro(entity.getBairro());
			dto.setCidade(entity.getCidade());
			dto.setIdEndereco(entity.getIdEndereco());
			dto.setLogradouro(entity.getLogradouro());
			if (null != entity.getUf()) {
				for (final UnidadeFederativa estado : UnidadeFederativa
						.values()) {
					if (estado.getSigla().equals(entity.getUf())) {
						dto.setUf(estado);
						break;
					}
				}
			}
			dto.setCep(entity.getCep());
			return dto;
		}

		@Override
		public Endereco createEntity(final EnderecoDTO outer) {
			final Endereco entidade = new Endereco();
			entidade.setBairro(outer.getBairro());
			entidade.setCidade(outer.getCidade());
			entidade.setIdEndereco(outer.getIdEndereco());
			entidade.setLogradouro(outer.getLogradouro());
			if (null != outer.getUf()) {
				entidade.setUf(outer.getUf().getSigla());
			}
			entidade.setCep(outer.getCep());
			return entidade;
		}

	}

}
