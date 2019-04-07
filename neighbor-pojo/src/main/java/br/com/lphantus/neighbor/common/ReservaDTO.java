package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.Reserva;

public class ReservaDTO extends AbstractDTO {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataSolicitacao;
	private Date dataReserva;
	private boolean pago;
	private ItemReservaDTO itemReserva;
	private MoradorDTO morador;
	private CondominioDTO condominio;

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
	 * @return the dataSolicitacao
	 */
	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	/**
	 * @param dataSolicitacao
	 *            the dataSolicitacao to set
	 */
	public void setDataSolicitacao(Date dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	/**
	 * @return the dataReserva
	 */
	public Date getDataReserva() {
		return dataReserva;
	}

	/**
	 * @param dataReserva
	 *            the dataReserva to set
	 */
	public void setDataReserva(Date dataReserva) {
		this.dataReserva = dataReserva;
	}

	/**
	 * @return the pago
	 */
	public boolean isPago() {
		return pago;
	}

	/**
	 * @param pago
	 *            the pago to set
	 */
	public void setPago(boolean pago) {
		this.pago = pago;
	}

	/**
	 * @return the itemReserva
	 */
	public ItemReservaDTO getItemReserva() {
		return itemReserva;
	}

	/**
	 * @param itemReserva
	 *            the itemReserva to set
	 */
	public void setItemReserva(ItemReservaDTO itemReserva) {
		this.itemReserva = itemReserva;
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
	public void setMorador(MoradorDTO morador) {
		this.morador = morador;
	}

	/**
	 * @return the condominio
	 */
	public CondominioDTO getCondominio() {
		return condominio;
	}

	/**
	 * @param condominio
	 *            the condominio to set
	 */
	public void setCondominio(CondominioDTO condominio) {
		this.condominio = condominio;
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
		if (!(obj instanceof ReservaDTO))
			return false;
		ReservaDTO other = (ReservaDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static class Builder extends DTOBuilder<ReservaDTO, Reserva> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ReservaDTO create(Reserva reserva) {
			ReservaDTO dto = new ReservaDTO();
			dto.setCondominio(reserva.getCondominio().createDto());
			dto.setDataReserva(reserva.getDataReserva());
			dto.setDataSolicitacao(reserva.getDataSolicitacao());
			dto.setId(reserva.getId());
			if (null != reserva.getItemReserva()) {
				dto.setItemReserva(reserva.getItemReserva().createDto());
			}
			MoradorDTO morador = new MoradorDTO();
			morador.setPessoa(PessoaFisicaDTO.Builder.getInstance().create(
					reserva.getMorador()));
			dto.setMorador(morador);
			dto.setPago(reserva.isPago());
			return dto;
		}

		@Override
		public Reserva createEntity(ReservaDTO outer) {
			Reserva entidade = new Reserva();
			entidade.setCondominio(CondominioDTO.Builder.getInstance()
					.createEntity(outer.getCondominio()));
			entidade.setDataReserva(outer.getDataReserva());
			entidade.setDataSolicitacao(outer.getDataSolicitacao());
			entidade.setId(outer.getId());
			if (null != outer.getItemReserva()) {
				entidade.setItemReserva(ItemReservaDTO.Builder.getInstance()
						.createEntity(outer.getItemReserva()));
			}
			if (null != outer.getMorador()) {
				entidade.setMorador(PessoaFisicaDTO.Builder.getInstance()
						.createEntityMorador(outer.getMorador().getPessoa()));
			}
			entidade.setPago(outer.isPago());
			return entidade;
		}
	}

}
