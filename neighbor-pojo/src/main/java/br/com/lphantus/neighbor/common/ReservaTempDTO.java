package br.com.lphantus.neighbor.common;

import java.util.Date;

import br.com.lphantus.neighbor.entity.ReservaTemp;
import br.com.lphantus.neighbor.common.PessoaFisicaDTO;

public class ReservaTempDTO extends AbstractDTO {

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

	public static class Builder extends DTOBuilder<ReservaTempDTO, ReservaTemp> {

		private static Builder instance = new Builder();

		private Builder() {

		}

		public static Builder getInstance() {
			return instance;
		}

		@Override
		public ReservaTempDTO create(ReservaTemp reservaTemp) {
			ReservaTempDTO dto = new ReservaTempDTO();
			dto.setCondominio(reservaTemp.getCondominio().createDto());
			dto.setDataReserva(reservaTemp.getDataReserva());
			dto.setDataSolicitacao(reservaTemp.getDataSolicitacao());
			dto.setId(reservaTemp.getId());
			dto.setItemReserva(reservaTemp.getItemReserva().createDto());
			dto.setMorador(reservaTemp.getMorador().createDto());
			dto.setPago(reservaTemp.isPago());
			return dto;
		}

		@Override
		public ReservaTemp createEntity(ReservaTempDTO outer) {
			ReservaTemp entidade = new ReservaTemp();
			entidade.setCondominio(CondominioDTO.Builder.getInstance()
					.createEntity(outer.getCondominio()));
			entidade.setDataReserva(outer.getDataReserva());
			entidade.setDataSolicitacao(outer.getDataSolicitacao());
			entidade.setId(outer.getId());
			entidade.setItemReserva(ItemReservaDTO.Builder.getInstance()
					.createEntity(outer.getItemReserva()));
			entidade.setMorador(PessoaFisicaDTO.Builder.getInstance()
					.createEntityMorador(outer.getMorador().getPessoa()));
			entidade.setPago(outer.isPago());
			return entidade;
		}
	}

}
