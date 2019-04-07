package br.com.lphantus.neighbor.service.integracao.mail;

import java.io.Serializable;

public class EmailTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String assunto;
	private String textoCorpo;
	private String destinatario;
	private String remetente;
	private String remetenteNome;
	private String usuario;
	private String senha;

	private String host;
	private Integer port;
	
	private boolean enviado = false;
	
	public EmailTO(final String assunto, final String textoCorpo, final String destinatario, final String remetente, final String remetenteNome, 
			final String usuario, final String senha, String host, Integer port){
		this.assunto = assunto;
		this.textoCorpo = textoCorpo;
		this.destinatario = destinatario;
		this.remetente = remetente;
		this.remetenteNome = remetenteNome;
		this.usuario = usuario;
		this.senha = senha;
		this.host = host;
		this.port = port;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the assunto
	 */
	public String getAssunto() {
		return assunto;
	}

	/**
	 * @return the textoCorpo
	 */
	public String getTextoCorpo() {
		return textoCorpo;
	}

	/**
	 * @return the destinatario
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * @return the remetente
	 */
	public String getRemetente() {
		return remetente;
	}

	/**
	 * @return the remetenteNome
	 */
	public String getRemetenteNome() {
		return remetenteNome;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * @return the enviado
	 */
	public boolean isEnviado() {
		return enviado;
	}

	/**
	 * @param enviado the enviado to set
	 */
	public void setEnviado(boolean enviado) {
		this.enviado = enviado;
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
		result = prime * result + ((destinatario == null) ? 0 : destinatario.hashCode());
		result = prime * result + ((remetente == null) ? 0 : remetente.hashCode());
		result = prime * result + ((textoCorpo == null) ? 0 : textoCorpo.hashCode());
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
		if (getClass() != obj.getClass())
			return false;
		EmailTO other = (EmailTO) obj;
		if (destinatario == null) {
			if (other.destinatario != null)
				return false;
		} else if (!destinatario.equals(other.destinatario))
			return false;
		if (remetente == null) {
			if (other.remetente != null)
				return false;
		} else if (!remetente.equals(other.remetente))
			return false;
		if (textoCorpo == null) {
			if (other.textoCorpo != null)
				return false;
		} else if (!textoCorpo.equals(other.textoCorpo))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmailTO [assunto=" + assunto + ", textoCorpo=" + textoCorpo 
				+ ", destinatario=" + destinatario + ", remetente=" + remetente 
				+ ", remetenteNome=" + remetenteNome + ", usuario=" + usuario 
				+ ", senha=" + senha + ", host=" + host + ", port=" + port 
				+ ", enviado=" + enviado + "]";
	}

}
