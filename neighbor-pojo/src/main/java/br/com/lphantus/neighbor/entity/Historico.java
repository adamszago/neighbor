package br.com.lphantus.neighbor.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import br.com.lphantus.neighbor.common.HistoricoDTO;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "HISTORICO")
public class Historico implements IEntity<Long, HistoricoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = -490240852543576668L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "DATA_HORA_ACAO")
	private Date dataHoraAcao;

	@Column(name = "ACAO_EXECUTADA", length = 255)
	private String acaoExecutada;

	@Column(name = "USUARIO")
	private String usuario;

	@Column(name = "CONDOMINIO")
	private String condominio;

	@Column(name = "ID_CONDOMINIO")
	private Long idCondominio;

	public Historico() {
	}

	public Historico(final Date dataHoraAcao, final String acaoExecutada,
			final String usuario, final String condominio,
			final Long idCondominio) {
		this.dataHoraAcao = dataHoraAcao;
		this.acaoExecutada = acaoExecutada;
		this.usuario = usuario;
		this.condominio = condominio;
		this.idCondominio = idCondominio;
	}

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
	 * @return the dataHoraAcao
	 */
	public Date getDataHoraAcao() {
		return this.dataHoraAcao;
	}

	/**
	 * @param dataHoraAcao
	 *            the dataHoraAcao to set
	 */
	public void setDataHoraAcao(final Date dataHoraAcao) {
		this.dataHoraAcao = dataHoraAcao;
	}

	/**
	 * @return the acaoExecutada
	 */
	public String getAcaoExecutada() {
		return this.acaoExecutada;
	}

	/**
	 * @param acaoExecutada
	 *            the acaoExecutada to set
	 */
	public void setAcaoExecutada(final String acaoExecutada) {
		this.acaoExecutada = acaoExecutada;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return this.usuario;
	}

	/**
	 * @param usuario
	 *            the usuario to set
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the condominio
	 */
	public String getCondominio() {
		return this.condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(final String condominio) {
		this.condominio = condominio;
	}

	/**
	 * @return the idCondominio
	 */
	public Long getIdCondominio() {
		return this.idCondominio;
	}

	/**
	 * @param idCondominio
	 *            the idCondominio to set
	 */
	public void setIdCondominio(final Long idCondominio) {
		this.idCondominio = idCondominio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
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
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Historico other = (Historico) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public HistoricoDTO createDto() {
		return HistoricoDTO.Builder.getInstance().create(this);
	}

}
