package br.com.lphantus.neighbor.common;

import br.com.lphantus.neighbor.entity.Usuario;

public class UsuarioDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private PessoaDTO pessoa;
	private String login;
	private String senha;
	private boolean ativo;
	private boolean root;
	private boolean sindico;
	private ModuloAcessoDTO moduloAcesso;
	private PlanoDTO plano;
	private CondominioDTO condominio;

	/**
	 * @return the login
	 */
	public String getLogin() {
		return this.login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(final String login) {
		this.login = login;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return this.senha;
	}

	/**
	 * @param senha
	 *            the senha to set
	 */
	public void setSenha(final String senha) {
		this.senha = senha;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * @return the root
	 */
	public boolean isRoot() {
		return this.root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public void setRoot(final boolean root) {
		this.root = root;
	}

	/**
	 * @return the sindico
	 */
	public boolean isSindico() {
		return this.sindico;
	}

	/**
	 * @param sindico
	 *            the sindico to set
	 */
	public void setSindico(final boolean sindico) {
		this.sindico = sindico;
	}

	/**
	 * @return the pessoa
	 */
	public PessoaDTO getPessoa() {
		return this.pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(final PessoaDTO pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the moduloAcesso
	 */
	public ModuloAcessoDTO getModuloAcesso() {
		return this.moduloAcesso;
	}

	/**
	 * @param moduloAcesso
	 *            the moduloAcesso to set
	 */
	public void setModuloAcesso(final ModuloAcessoDTO moduloAcesso) {
		this.moduloAcesso = moduloAcesso;
	}

	/**
	 * @return the plano
	 */
	public PlanoDTO getPlano() {
		return this.plano;
	}

	/**
	 * @param plano
	 *            the plano to set
	 */
	public void setPlano(final PlanoDTO plano) {
		this.plano = plano;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.pessoa == null) ? 0 : this.pessoa.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof UsuarioDTO)) {
			return false;
		}
		final UsuarioDTO other = (UsuarioDTO) obj;
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
		return "UsuarioDTO [login=" + this.login + ", senha=" + this.senha
				+ ", ativo=" + this.ativo + ", root=" + this.root
				+ ", sindico=" + this.sindico + ", moduloAcesso="
				+ this.moduloAcesso + ", plano=" + this.plano + ", pessoa="
				+ this.pessoa + ", condominio=" + this.condominio + "]";
	}

	public static class Builder extends DTOBuilder<UsuarioDTO, Usuario> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public UsuarioDTO create(final Usuario usuario) {
			final UsuarioDTO dto = new UsuarioDTO();

			dto.setAtivo(usuario.isAtivo());
			dto.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(usuario));
			dto.setLogin(usuario.getLogin());
			if (usuario.getModuloAcesso() != null) {
				dto.setModuloAcesso(usuario.getModuloAcesso().createDto());
			}
			if (usuario.getPlano() != null) {
				dto.setPlano(usuario.getPlano().createDto());
			}
			dto.setRoot(usuario.isRoot());
			dto.setSenha(usuario.getSenha());
			dto.setSindico(usuario.isSindico());
			if (usuario.getCondominio() != null) {
				dto.setCondominio(usuario.getCondominio().createDto());
			}

			return dto;
		}

		@Override
		public Usuario createEntity(final UsuarioDTO outer) {
			final Usuario entidade = PessoaFisicaDTO.Builder.getInstance()
					.createEntityUsuario((PessoaFisicaDTO) outer.getPessoa());
			entidade.setIdPessoa(outer.getPessoa().getIdPessoa());
			entidade.setAtivo(outer.isAtivo());
			entidade.setLogin(outer.getLogin());
			if (null != outer.getModuloAcesso()) {
				entidade.setModuloAcesso(ModuloAcessoDTO.Builder.getInstance()
						.createEntity(outer.getModuloAcesso()));
			}
			if (null != outer.getPlano()) {
				entidade.setPlano(PlanoDTO.Builder.getInstance().createEntity(
						outer.getPlano()));
			}
			entidade.setRoot(outer.isRoot());
			entidade.setSenha(outer.getSenha());
			entidade.setSindico(outer.isSindico());
			if (null != outer.getCondominio()) {
				entidade.setCondominio(CondominioDTO.Builder.getInstance()
						.createEntity(outer.getCondominio()));
			}

			return entidade;
		}

	}

}
