package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.entity.PessoaJuridica;

public class PessoaJuridicaDTO extends PessoaDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String cnpj;
	private String nomeFantasia;

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return this.cnpj;
	}

	/**
	 * @param cnpj
	 *            the cnpj to set
	 */
	public void setCnpj(final String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the nomeFantasia
	 */
	public String getNomeFantasia() {
		return this.nomeFantasia;
	}

	/**
	 * @param nomeFantasia
	 *            the nomeFantasia to set
	 */
	public void setNomeFantasia(final String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
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
				+ ((this.idPessoa == null) ? 0 : this.idPessoa.hashCode());
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
		if (!(obj instanceof PessoaJuridicaDTO)) {
			return false;
		}
		final PessoaJuridicaDTO other = (PessoaJuridicaDTO) obj;
		if (this.idPessoa == null) {
			if (other.idPessoa != null) {
				return false;
			}
		} else if (!this.idPessoa.equals(other.idPessoa)) {
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
		return "PessoaJuridicaDTO [idPessoa=" + this.idPessoa + ", nome="
				+ this.nome + ", dataNascimento=" + this.dataNascimento
				+ ", dataCadastro=" + this.dataCadastro + ", mail=" + this.mail
				+ ", possuiSenhaTotem=" + this.possuiSenhaTotem + ", idade="
				+ this.idade + ", ativo=" + this.ativo + ", cnpj=" + this.cnpj
				+ ", nomeFantasia=" + this.nomeFantasia + "]";
	}

	public static class Builder extends
			DTOBuilder<PessoaJuridicaDTO, PessoaJuridica> {
		private static Builder instance;

		public static Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public PessoaJuridicaDTO create(final PessoaJuridica entity) {
			final PessoaJuridicaDTO retorno = new PessoaJuridicaDTO();

			retorno.setAtivo(entity.isAtivo());
			retorno.setCnpj(entity.getCnpj());
			retorno.setDataCadastro(entity.getDataCadastro());
			retorno.setDataNascimento(entity.getDataNascimento());
			retorno.setIdade(entity.getIdade());
			retorno.setIdPessoa(entity.getIdPessoa());
			retorno.setMail(entity.getMail());
			retorno.setNome(entity.getNome());
			retorno.setNomeFantasia(entity.getNomeFantasia());
			retorno.setPossuiSenhaTotem(entity.isPossuiSenhaTotem());

			return retorno;
		}

		private void transformaEntidade(final PessoaJuridica retorno,
				final PessoaJuridicaDTO outer) {
			retorno.setAtivo(outer.isAtivo());
			retorno.setCnpj(outer.getCnpj());
			retorno.setDataCadastro(outer.getDataCadastro());
			retorno.setDataNascimento(outer.getDataNascimento());
			retorno.setIdade(outer.getIdade());
			retorno.setIdPessoa(outer.getIdPessoa());
			retorno.setMail(outer.getMail());
			retorno.setNome(outer.getNome());
			retorno.setNomeFantasia(outer.getNomeFantasia());
			retorno.setPossuiSenhaTotem(outer.isPossuiSenhaTotem());
		}

		@Override
		public PessoaJuridica createEntity(final PessoaJuridicaDTO outer) {
			final PessoaJuridica retorno = new PessoaJuridica();

			transformaEntidade(retorno, outer);

			return retorno;
		}

		public Condominio createEntityCondominio(final PessoaJuridicaDTO outer) {
			final Condominio retorno = new Condominio();

			transformaEntidade(retorno, outer);

			return retorno;
		}

	}

}
