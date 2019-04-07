package br.com.lphantus.neighbor.service.integracao.batch;

/**
 * Classe para agregar os registros de totem, independente da pessoa associada,
 * e da versao do gerador.
 * 
 * @author elias.policena@lphantus.com.br
 *
 */
public class TotemTO {

	private Long idTotem;
	private Boolean ativo;
	private String senha;
	private String idPessoa;
	private String nome;
	private String identificacao;
	private String tipoCancela;
	private Long tipoTotem;
	private Long tipoAcesso;
	private String dataInicial;
	private String dataFinal;

	/**
	 * @return the idTotem
	 */
	public Long getIdTotem() {
		return this.idTotem;
	}

	/**
	 * @param idTotem
	 *            the idTotem to set
	 */
	public void setIdTotem(final Long idTotem) {
		this.idTotem = idTotem;
	}

	/**
	 * @return the ativo
	 */
	public Boolean getAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(final Boolean ativo) {
		this.ativo = ativo;
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
	 * @return the idPessoa
	 */
	public String getIdPessoa() {
		return this.idPessoa;
	}

	/**
	 * @param idPessoa
	 *            the idPessoa to set
	 */
	public void setIdPessoa(final Long idPessoa) {
		switch (this.tipoTotem.intValue()) {
		case 1:
			this.idPessoa = String.format("M%d", idPessoa);
			break;
		case 2:
			this.idPessoa = String.format("A%d", idPessoa);
			break;
		case 3:
			this.idPessoa = String.format("V%d", idPessoa);
			break;
		case 4:
			this.idPessoa = String.format("P%d", idPessoa);
			break;
		default:
			this.idPessoa = String.format("%d", idPessoa);
			break;
		}
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
	 * @return the identificacao
	 */
	public String getIdentificacao() {
		return this.identificacao;
	}

	/**
	 * @param identificacao
	 *            the identificacao to set
	 */
	public void setIdentificacao(final String identificacao) {
		this.identificacao = identificacao;
	}

	/**
	 * @return the tipoTotem
	 */
	public Long getTipoTotem() {
		return tipoTotem;
	}

	/**
	 * @param tipoTotem
	 *            the tipoTotem to set
	 */
	public void setTipoTotem(Long tipoTotem) {
		this.tipoTotem = tipoTotem;
	}

	/**
	 * @return the tipoCancela
	 */
	public String getTipoCancela() {
		return tipoCancela;
	}

	/**
	 * @param tipoCancela
	 *            the tipoCancela to set
	 */
	public void setTipoCancela(String tipoCancela) {
		this.tipoCancela = tipoCancela;
	}

	/**
	 * @return the tipoAcesso
	 */
	public Long getTipoAcesso() {
		return tipoAcesso;
	}

	/**
	 * @param tipoAcesso
	 *            the tipoAcesso to set
	 */
	public void setTipoAcesso(Long tipoAcesso) {
		this.tipoAcesso = tipoAcesso;
	}

	/**
	 * @return the dataInicial
	 */
	public String getDataInicial() {
		return dataInicial;
	}

	/**
	 * @param dataInicial
	 *            the dataInicial to set
	 */
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the dataFinal
	 */
	public String getDataFinal() {
		return dataFinal;
	}

	/**
	 * @param dataFinal
	 *            the dataFinal to set
	 */
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
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
				+ ((this.idTotem == null) ? 0 : this.idTotem.hashCode());
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
		if (!(obj instanceof TotemTO)) {
			return false;
		}
		final TotemTO other = (TotemTO) obj;
		if (this.idTotem == null) {
			if (other.idTotem != null) {
				return false;
			}
		} else if (!this.idTotem.equals(other.idTotem)) {
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
		return "TotemTO [idTotem=" + idTotem + ", ativo=" + ativo + ", senha="
				+ senha + ", idPessoa=" + idPessoa + ", nome=" + nome
				+ ", identificacao=" + identificacao + ", tipoCancela="
				+ tipoCancela + ", tipoTotem=" + tipoTotem + "]";
	}

}