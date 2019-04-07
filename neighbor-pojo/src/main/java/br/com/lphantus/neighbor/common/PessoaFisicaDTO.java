package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.entity.Morador;
import br.com.lphantus.neighbor.entity.PessoaFisica;
import br.com.lphantus.neighbor.entity.Usuario;
import br.com.lphantus.neighbor.entity.Visitante;

public class PessoaFisicaDTO extends PessoaDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String cnh;
	private String rg;
	private String cpf;
	private String sexo;
	private String profissao;

	/**
	 * @return the cnh
	 */
	public String getCnh() {
		return this.cnh;
	}

	/**
	 * @param cnh
	 *            the cnh to set
	 */
	public void setCnh(final String cnh) {
		this.cnh = cnh;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return this.rg;
	}

	/**
	 * @param rg
	 *            the rg to set
	 */
	public void setRg(final String rg) {
		this.rg = rg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return this.cpf;
	}

	/**
	 * @param cpf
	 *            the cpf to set
	 */
	public void setCpf(final String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the sexo
	 */
	public String getSexo() {
		return this.sexo;
	}

	/**
	 * @param sexo
	 *            the sexo to set
	 */
	public void setSexo(final String sexo) {
		this.sexo = sexo;
	}

	/**
	 * @return the profissao
	 */
	public String getProfissao() {
		return this.profissao;
	}

	/**
	 * @param profissao
	 *            the profissao to set
	 */
	public void setProfissao(final String profissao) {
		this.profissao = profissao;
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
		if (!(obj instanceof PessoaFisicaDTO)) {
			return false;
		}
		final PessoaFisicaDTO other = (PessoaFisicaDTO) obj;
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
		return "PessoaFisicaDTO [idPessoa=" + this.idPessoa + ", nome="
				+ this.nome + ", dataNascimento=" + this.dataNascimento
				+ ", dataCadastro=" + this.dataCadastro + ", mail=" + this.mail
				+ ", possuiSenhaTotem=" + this.possuiSenhaTotem + ", idade="
				+ this.idade + ", ativo=" + this.ativo + ", cnh=" + this.cnh
				+ ", rg=" + this.rg + ", cpf=" + this.cpf + ", sexo="
				+ this.sexo + ", profissao=" + this.profissao + "]";
	}

	public static class Builder extends
			DTOBuilder<PessoaFisicaDTO, PessoaFisica> {

		private static Builder instance;

		public static Builder getInstance() {
			if (null == instance) {
				instance = new Builder();
			}
			return instance;
		}

		@Override
		public PessoaFisicaDTO create(final PessoaFisica entity) {
			final PessoaFisicaDTO retorno = new PessoaFisicaDTO();

			retorno.setAtivo(entity.isAtivo());
			retorno.setCnh(entity.getCnh());
			retorno.setCpf(entity.getCpf());
			retorno.setDataCadastro(entity.getDataCadastro());
			retorno.setDataNascimento(entity.getDataNascimento());
			retorno.setIdade(entity.getIdade());
			retorno.setIdPessoa(entity.getIdPessoa());
			retorno.setMail(entity.getMail());
			retorno.setNome(entity.getNome());
			retorno.setPossuiSenhaTotem(entity.isPossuiSenhaTotem());
			retorno.setProfissao(entity.getProfissao());
			retorno.setRg(entity.getRg());
			retorno.setSexo(entity.getSexo());

			return retorno;
		}

		private void transformaEntidade(final PessoaFisicaDTO outer,
				final PessoaFisica retorno) {
			retorno.setAtivo(outer.isAtivo());
			retorno.setCnh(outer.getCnh());
			retorno.setCpf(outer.getCpf());
			retorno.setDataCadastro(outer.getDataCadastro());
			retorno.setDataNascimento(outer.getDataNascimento());
			retorno.setIdade(outer.getIdade());
			retorno.setIdPessoa(outer.getIdPessoa());
			retorno.setMail(outer.getMail());
			retorno.setNome(outer.getNome());
			retorno.setPossuiSenhaTotem(outer.isPossuiSenhaTotem());
			retorno.setProfissao(outer.getProfissao());
			retorno.setRg(outer.getRg());
			retorno.setSexo(outer.getSexo());
		}

		@Override
		public PessoaFisica createEntity(final PessoaFisicaDTO outer) {
			final PessoaFisica retorno = new PessoaFisica();

			transformaEntidade(outer, retorno);

			return retorno;
		}

		public Morador createEntityMorador(final PessoaFisicaDTO outer) {
			final Morador retorno = new Morador();

			transformaEntidade(outer, retorno);

			return retorno;
		}

		public Agregado createEntityAgregado(final PessoaFisicaDTO outer) {
			final Agregado retorno = new Agregado();

			transformaEntidade(outer, retorno);

			return retorno;
		}

		public Usuario createEntityUsuario(final PessoaFisicaDTO outer) {
			final Usuario retorno = new Usuario();
			transformaEntidade(outer, retorno);
			return retorno;
		}

		public Visitante createEntityVisitante(final PessoaFisicaDTO outer) {
			final Visitante retorno = new Visitante();
			transformaEntidade(outer, retorno);
			return retorno;
		}

	}

}
