package br.com.lphantus.neighbor.common;

import java.util.Date;

public class PessoaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	Long idPessoa;
	String nome;
	Date dataNascimento;
	Date dataCadastro;
	String mail;
	Boolean possuiSenhaTotem;
	int idade;
	boolean ativo = true;

	/**
	 * @return the idPessoa
	 */
	public Long getIdPessoa() {
		return this.idPessoa;
	}

	/**
	 * @param idPessoa
	 *            the idPessoa to set
	 */
	public void setIdPessoa(final Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return this.nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(final String nome) {
		this.nome = nome;
	}

	/**
	 * @return the dataNascimento
	 */
	public Date getDataNascimento() {
		return this.dataNascimento;
	}

	/**
	 * @param dataNascimento
	 *            the dataNascimento to set
	 */
	public void setDataNascimento(final Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return this.dataCadastro;
	}

	/**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
	public void setDataCadastro(final Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
		return this.mail;
	}

	/**
	 * @param mail
	 *            the mail to set
	 */
	public void setMail(final String mail) {
		this.mail = mail;
	}

	/**
	 * @return the possuiSenhaTotem
	 */
	public Boolean isPossuiSenhaTotem() {
		return this.possuiSenhaTotem;
	}

	/**
	 * @param possuiSenhaTotem
	 *            the possuiSenhaTotem to set
	 */
	public void setPossuiSenhaTotem(final Boolean possuiSenhaTotem) {
		this.possuiSenhaTotem = possuiSenhaTotem;
	}

	/**
	 * @return the idade
	 */
	public int getIdade() {
		return this.idade;
	}

	/**
	 * @param idade
	 *            the idade to set
	 */
	public void setIdade(final int idade) {
		this.idade = idade;
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

}
