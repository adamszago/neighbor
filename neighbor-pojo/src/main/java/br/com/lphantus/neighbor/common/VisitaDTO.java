package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Visita;

/**
 * @author Elias
 */
public class VisitaDTO extends AbstractDTO {

	private static final long serialVersionUID = 1L;

	private Long id;
	private MoradorDTO morador;
	private VisitanteDTO visitante;
	// private Date dataVisita;
	// private Date dataAgendamentoVisita;
	private Date inicioVisita;
	private Date fimVisita;
	private Date inicioAgendamentoVisita;
	private Date fimAgendamentoVisita;
	private boolean confirmado;
	private boolean ativa = true;
	private Long tipoAcesso;
	private String placaUtilizada;

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
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the visitante
	 */
	public VisitanteDTO getVisitante() {
		return visitante;
	}

	/**
	 * @param visitante
	 *            the visitante to set
	 */
	public void setVisitante(final VisitanteDTO visitante) {
		this.visitante = visitante;
	}

	/**
	 * @return the inicioVisita
	 */
	public Date getInicioVisita() {
		return inicioVisita;
	}

	/**
	 * @param inicioVisita
	 *            the inicioVisita to set
	 */
	public void setInicioVisita(Date inicioVisita) {
		this.inicioVisita = inicioVisita;
	}

	/**
	 * @return the fimVisita
	 */
	public Date getFimVisita() {
		return fimVisita;
	}

	/**
	 * @param fimVisita
	 *            the fimVisita to set
	 */
	public void setFimVisita(Date fimVisita) {
		this.fimVisita = fimVisita;
	}

	/**
	 * @return the inicioAgendamentoVisita
	 */
	public Date getInicioAgendamentoVisita() {
		return inicioAgendamentoVisita;
	}

	/**
	 * @param inicioAgendamentoVisita
	 *            the inicioAgendamentoVisita to set
	 */
	public void setInicioAgendamentoVisita(Date inicioAgendamentoVisita) {
		this.inicioAgendamentoVisita = inicioAgendamentoVisita;
	}

	/**
	 * @return the fimAgendamentoVisita
	 */
	public Date getFimAgendamentoVisita() {
		return fimAgendamentoVisita;
	}

	/**
	 * @param fimAgendamentoVisita
	 *            the fimAgendamentoVisita to set
	 */
	public void setFimAgendamentoVisita(Date fimAgendamentoVisita) {
		this.fimAgendamentoVisita = fimAgendamentoVisita;
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
	public void setConfirmado(final boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @return the ativa
	 */
	public boolean isAtiva() {
		return ativa;
	}

	/**
	 * @param ativa
	 *            the ativa to set
	 */
	public void setAtiva(final boolean ativa) {
		this.ativa = ativa;
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
	 * @param placaUtilizada
	 *            the placaUtilizada to set
	 */
	public void setPlacaUtilizada(String placaUtilizada) {
		this.placaUtilizada = placaUtilizada;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((getId() == null) ? 0 : getId().hashCode());
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
		if (!(obj instanceof VisitaDTO)) {
			return false;
		}
		final VisitaDTO other = (VisitaDTO) obj;
		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!getId().equals(other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "VisitaDTO [ Id=" + getId() + "]";
	}

	public static class Builder extends DTOBuilder<VisitaDTO, Visita> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		/**
		 * @param visita
		 * @return A Instancia de {@link VisitaDTO}
		 */
		@Override
		public VisitaDTO create(final Visita visita) {
			final VisitaDTO dto = new VisitaDTO();
			dto.setAtiva(visita.isAtiva());
			dto.setConfirmado(visita.isConfirmado());
			dto.setInicioVisita(visita.getInicioVisita());
			dto.setFimVisita(visita.getFimVisita());
			dto.setInicioAgendamentoVisita(visita.getInicioAgendamentoVisita());
			dto.setFimAgendamentoVisita(visita.getFimAgendamentoVisita());
			// dto.setDataAgendamentoVisita(visita.getDataAgendamentoVisita());
			// dto.setDataVisita(visita.getDataVisita());
			dto.setId(visita.getId());
			dto.setMorador(visita.getMorador().createDto());
			dto.setTipoAcesso(visita.getTipoAcesso());
			dto.setPlacaUtilizada(visita.getPlacaUtilizada());
			// dto.setVisitante(visita.getVisitante().createDto());
			return dto;
		}

		@Override
		public Visita createEntity(final VisitaDTO outer) {
			final Visita entidade = new Visita();
			entidade.setAtiva(outer.isAtiva());
			entidade.setConfirmado(outer.isConfirmado());
			// entidade.setDataAgendamentoVisita(outer.getDataAgendamentoVisita());
			// entidade.setDataVisita(outer.getDataVisita());
			entidade.setInicioVisita(outer.getInicioVisita());
			entidade.setFimVisita(outer.getFimVisita());
			entidade.setInicioAgendamentoVisita(outer.getInicioAgendamentoVisita());
			entidade.setFimAgendamentoVisita(outer.getFimAgendamentoVisita());
			entidade.setId(outer.getId());
			entidade.setMorador(PessoaFisicaDTO.Builder.getInstance().createEntityMorador(outer.getMorador().getPessoa()));
			entidade.setTipoAcesso(outer.getTipoAcesso());
			entidade.setPlacaUtilizada(outer.getPlacaUtilizada());
			// entidade.setVisitante(VisitanteDTO.Builder.getInstance()
			// .createEntity(outer.getVisitante()));
			return entidade;
		}

	}

}
