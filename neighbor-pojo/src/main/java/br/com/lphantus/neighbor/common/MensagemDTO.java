package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Mensagem;

public class MensagemDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private UsuarioDTO remetente;
	private UsuarioDTO destinatario;
	private String assunto;
	private String mensagem;
	private Date dataEnvio;
	private boolean lido;

	/**
	 * @return the id
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the remetente
	 */
	public UsuarioDTO getRemetente() {
		return this.remetente;
	}

	/**
	 * @param remetente
	 *            the remetente to set
	 */
	public void setRemetente(final UsuarioDTO remetente) {
		this.remetente = remetente;
	}

	/**
	 * @return the destinatario
	 */
	public UsuarioDTO getDestinatario() {
		return this.destinatario;
	}

	/**
	 * @param destinatario
	 *            the destinatario to set
	 */
	public void setDestinatario(final UsuarioDTO destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * @return the assunto
	 */
	public String getAssunto() {
		return this.assunto;
	}

	/**
	 * @param assunto
	 *            the assunto to set
	 */
	public void setAssunto(final String assunto) {
		this.assunto = assunto;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return this.mensagem;
	}

	/**
	 * @param mensagem
	 *            the mensagem to set
	 */
	public void setMensagem(final String mensagem) {
		this.mensagem = mensagem;
	}

	/**
	 * @return the dataEnvio
	 */
	public Date getDataEnvio() {
		return this.dataEnvio;
	}

	/**
	 * @param dataEnvio
	 *            the dataEnvio to set
	 */
	public void setDataEnvio(final Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	/**
	 * @return the lido
	 */
	public boolean isLido() {
		return this.lido;
	}

	/**
	 * @param lido
	 *            the lido to set
	 */
	public void setLido(final boolean lido) {
		this.lido = lido;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof MensagemDTO))
			return false;
		MensagemDTO other = (MensagemDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder extends DTOBuilder<MensagemDTO, Mensagem> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public MensagemDTO create(final Mensagem mensagem) {
			final MensagemDTO dto = new MensagemDTO();
			dto.setAssunto(mensagem.getAssunto());
			dto.setDataEnvio(mensagem.getDataEnvio());
			dto.setDestinatario(UsuarioDTO.Builder.getInstance().create(
					mensagem.getDestinatario()));
			dto.setId(mensagem.getId());
			dto.setLido(mensagem.isLido());
			dto.setMensagem(mensagem.getMensagem());
			dto.setRemetente(UsuarioDTO.Builder.getInstance().create(
					mensagem.getRemetente()));
			return dto;
		}

		@Override
		public Mensagem createEntity(final MensagemDTO outer) {
			final Mensagem entidade = new Mensagem();
			entidade.setAssunto(outer.getAssunto());
			entidade.setDataEnvio(outer.getDataEnvio());
			entidade.setDestinatario(UsuarioDTO.Builder.getInstance()
					.createEntity(outer.getDestinatario()));
			if (null != outer.getId()) {
				entidade.setId(outer.getId());
			}
			entidade.setLido(outer.isLido());
			entidade.setMensagem(outer.getMensagem());
			entidade.setRemetente(UsuarioDTO.Builder.getInstance()
					.createEntity(outer.getRemetente()));
			return entidade;
		}
	}

}
