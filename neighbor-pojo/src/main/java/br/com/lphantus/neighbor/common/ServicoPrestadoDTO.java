package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.ServicoPrestado;

public class ServicoPrestadoDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private MoradorDTO morador;
	private PrestadorServicoDTO prestadorServico;
	private Date dataServico;
	private Date dataFimServico;
	// private Date dataAgendamentoServico;
	private Date inicioAgendamentoServico;
	private Date fimAgendamentoServico;
	private boolean confirmado;
	private boolean ativo = true;
	private Long tipoAcesso;
	private String placaUtilizada;

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
	 * @return the morador
	 */
	public MoradorDTO getMorador() {
		return this.morador;
	}

	/**
	 * @param morador
	 *            the morador to set
	 */
	public void setMorador(final MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the prestadorServico
	 */
	public PrestadorServicoDTO getPrestadorServico() {
		return this.prestadorServico;
	}

	/**
	 * @param prestadorServico
	 *            the prestadorServico to set
	 */
	public void setPrestadorServico(final PrestadorServicoDTO prestadorServico) {
		this.prestadorServico = prestadorServico;
	}

	/**
	 * @return the dataServico
	 */
	public Date getDataServico() {
		return this.dataServico;
	}

	/**
	 * @param dataServico
	 *            the dataServico to set
	 */
	public void setDataServico(final Date dataServico) {
		this.dataServico = dataServico;
	}

	/**
	 * @return the dataFimServico
	 */
	public Date getDataFimServico() {
		return this.dataFimServico;
	}

	/**
	 * @param dataFimServico
	 *            the dataFimServico to set
	 */
	public void setDataFimServico(final Date dataFimServico) {
		this.dataFimServico = dataFimServico;
	}

	/**
	 * @return the dataAgendamentoServico
	 */
	// public Date getDataAgendamentoServico() {
	// return this.dataAgendamentoServico;
	// }

	/**
	 * @param dataAgendamentoServico
	 *            the dataAgendamentoServico to set
	 */
	// public void setDataAgendamentoServico(final Date dataAgendamentoServico)
	// {
	// this.dataAgendamentoServico = dataAgendamentoServico;
	// }

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
		return this.confirmado;
	}

	/**
	 * @param confirmado
	 *            the confirmado to set
	 */
	public void setConfirmado(final boolean confirmado) {
		this.confirmado = confirmado;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return this.ativo;
	}

	/**
	 * @param ativa
	 *            the ativo to set
	 */
	public void setAtivo(final boolean ativo) {
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
	 * @param placaUtilizada
	 *            the placaUtilizada to set
	 */
	public void setPlacaUtilizada(String placaUtilizada) {
		this.placaUtilizada = placaUtilizada;
	}

	public static class Builder extends DTOBuilder<ServicoPrestadoDTO, ServicoPrestado> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ServicoPrestadoDTO create(final ServicoPrestado prestador) {
			final ServicoPrestadoDTO dto = new ServicoPrestadoDTO();
			dto.setAtivo(prestador.isAtivo());
			dto.setConfirmado(prestador.isConfirmado());
			// dto.setDataAgendamentoServico(prestado.getDataAgendamentoServico());
			dto.setInicioAgendamentoServico(prestador.getInicioAgendamentoServico());
			dto.setFimAgendamentoServico(prestador.getFimAgendamentoServico());
			dto.setDataFimServico(prestador.getDataFimServico());
			dto.setDataServico(prestador.getDataServico());
			dto.setId(prestador.getId());
			if (null != prestador.getMorador()) {
				dto.setMorador(prestador.getMorador().createDto());
			}
			dto.setTipoAcesso(prestador.getTipoAcesso());
			dto.setPlacaUtilizada(prestador.getPlacaUtilizada());
			// if (null != prestado.getPrestadorServico()) {
			// dto.setPrestadorServico(prestado.getPrestadorServico()
			// .createDto());
			// }
			return dto;
		}

		@Override
		public ServicoPrestado createEntity(final ServicoPrestadoDTO outer) {
			final ServicoPrestado entidade = new ServicoPrestado();
			entidade.setAtivo(outer.isAtivo());
			entidade.setConfirmado(outer.isConfirmado());
			// entidade.setDataAgendamentoServico(outer
			// .getDataAgendamentoServico());
			entidade.setInicioAgendamentoServico(outer.getInicioAgendamentoServico());
			entidade.setFimAgendamentoServico(outer.getFimAgendamentoServico());
			entidade.setDataFimServico(outer.getDataFimServico());
			entidade.setDataServico(outer.getDataServico());
			entidade.setId(outer.getId());
			entidade.setMorador(PessoaFisicaDTO.Builder.getInstance().createEntityMorador(outer.getMorador().getPessoa()));
			entidade.setTipoAcesso(outer.getTipoAcesso());
			entidade.setPlacaUtilizada(outer.getPlacaUtilizada());
			// entidade.setPrestadorServico(PrestadorServicoDTO.Builder
			// .getInstance().createEntity(outer.getPrestadorServico()));
			return entidade;
		}
	}

}
