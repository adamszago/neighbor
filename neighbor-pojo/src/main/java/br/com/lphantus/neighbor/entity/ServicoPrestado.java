package br.com.lphantus.neighbor.entity;

import java.util.Date;

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

import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;

@Entity
@Table(name = "SERVICO_PRESTADO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ServicoPrestado implements IEntity<Long, ServicoPrestadoDTO> {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_SERVICO_PRESTADO")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID_MORADOR"))
	private Morador morador;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns(@JoinColumn(name = "ID_PRESTADOR", referencedColumnName = "ID_PRESTADOR"))
	private PrestadorServico prestadorServico;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_SERVICO")
	private Date dataServico;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_FIM_SERVICO")
	private Date dataFimServico;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INICIO_AGENDAMENTO_SERVICO")
	private Date inicioAgendamentoServico;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIM_AGENDAMENTO_SERVICO")
	private Date fimAgendamentoServico;

	@Column(name = "SERVICO_CONFIRMADO")
	private boolean confirmado;

	@Column(name = "SERVICO_ATIVO")
	private boolean ativo;

	@Column(name = "TIPO_ACESSO")
	private Long tipoAcesso;
	
	@Column(name = "PLACA_UTILIZADA", length = 9)
	private String placaUtilizada;

	// getter and setter

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the morador
	 */
	public Morador getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(Morador morador) {
		this.morador = morador;
	}

	/**
	 * @return the prestadorServico
	 */
	public PrestadorServico getPrestadorServico() {
		return prestadorServico;
	}

	/**
	 * @param prestadorServico
	 *            the prestadorServico to set
	 */
	public void setPrestadorServico(PrestadorServico prestadorServico) {
		this.prestadorServico = prestadorServico;
	}

	/**
	 * @return the dataServico
	 */
	public Date getDataServico() {
		return dataServico;
	}

	/**
	 * @param dataServico
	 *            the dataServico to set
	 */
	public void setDataServico(Date dataServico) {
		this.dataServico = dataServico;
	}

	/**
	 * @return the dataFimServico
	 */
	public Date getDataFimServico() {
		return dataFimServico;
	}

	/**
	 * @param dataFimServico
	 *            the dataFimServico to set
	 */
	public void setDataFimServico(Date dataFimServico) {
		this.dataFimServico = dataFimServico;
	}

	/**
	 * @return the inicioAgendamentoServico
	 */
	public Date getInicioAgendamentoServico() {
		return inicioAgendamentoServico;
	}

	/**
	 * @param inicioAgendamentoServico
	 *            the inicioAgendamentoServico to set
	 */
	public void setInicioAgendamentoServico(Date inicioAgendamentoServico) {
		this.inicioAgendamentoServico = inicioAgendamentoServico;
	}

	/**
	 * @return the fimAgendamentoServico
	 */
	public Date getFimAgendamentoServico() {
		return fimAgendamentoServico;
	}

	/**
	 * @param fimAgendamentoServico
	 *            the fimAgendamentoServico to set
	 */
	public void setFimAgendamentoServico(Date fimAgendamentoServico) {
		this.fimAgendamentoServico = fimAgendamentoServico;
	}

	/**
	 * @return the confirmado
	 */
	public boolean isConfirmado() {
		return confirmado;
	}

	/**
	 * @param confirmado
	 *            the confirmado to set
	 */
	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * @param ativo
	 *            the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
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
	 * @return the placaUtilizada
	 */
	public String getPlacaUtilizada() {
		return placaUtilizada;
	}

	/**
	 * @param placaUtilizada the placaUtilizada to set
	 */
	public void setPlacaUtilizada(String placaUtilizada) {
		this.placaUtilizada = placaUtilizada;
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
		final ServicoPrestado other = (ServicoPrestado) obj;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public ServicoPrestadoDTO createDto() {
		return ServicoPrestadoDTO.Builder.getInstance().create(this);
	}

}
