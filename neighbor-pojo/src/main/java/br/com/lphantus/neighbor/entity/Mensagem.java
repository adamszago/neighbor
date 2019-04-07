package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.MensagemDTO;

@Entity
@Table(name = "MENSAGEM")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Mensagem implements Comparable<Mensagem>,
		IEntity<Long, MensagemDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = -3203143276027121722L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_MENSAGEM")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_REMETENTE", referencedColumnName = "ID_USUARIO"))
	private Usuario remetente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_DESTINATARIO", referencedColumnName = "ID_USUARIO"))
	private Usuario destinatario;

	@Column(name = "ASSUNTO")
	private String assunto;

	@Basic
	@Column(name = "CONTEUDO", length = 5000)
	private String mensagem;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_ENVIO")
	private Date dataEnvio;

	@Column(name = "LIDO")
	private boolean lido = false;

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
	public Usuario getRemetente() {
		return this.remetente;
	}

	/**
	 * @param remetente
	 *            the remetente to set
	 */
	public void setRemetente(final Usuario remetente) {
		this.remetente = remetente;
	}

	/**
	 * @return the destinatario
	 */
	public Usuario getDestinatario() {
		return this.destinatario;
	}

	/**
	 * @param destinatario
	 *            the destinatario to set
	 */
	public void setDestinatario(final Usuario destinatario) {
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
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		if (!(obj instanceof Mensagem)) {
			return false;
		}
		final Mensagem other = (Mensagem) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
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
		return "Mensagem [id=" + this.id + ", remetente=" + this.remetente
				+ ", destinatario=" + this.destinatario + ", assunto="
				+ this.assunto + ", mensagem=" + this.mensagem + ", dataEnvio="
				+ this.dataEnvio + ", lido=" + this.lido + "]";
	}

	@Override
	public int compareTo(final Mensagem mensagem) {
		return this.dataEnvio.compareTo(mensagem.getDataEnvio());
	}

	@Override
	public MensagemDTO createDto() {
		return MensagemDTO.Builder.getInstance().create(this);
	}
}
