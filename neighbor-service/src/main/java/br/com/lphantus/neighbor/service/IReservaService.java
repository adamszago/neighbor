package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.common.ReservaTempDTO;
import br.com.lphantus.neighbor.entity.Reserva;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IReservaService extends IGenericService<Long, ReservaDTO, Reserva> {

	public void validarReserva(final ReservaDTO reserva) throws ServiceException;

	public void save(final ReservaDTO reserva, final boolean solicitarAprovacao) throws ServiceException;

	public void deletarReservaPendente(final ReservaTempDTO res) throws ServiceException;

	public ReservaTempDTO converterReservaParaReservaTemp(final ReservaDTO res);

	public List<ReservaDTO> listarReservaPorMorador(final MoradorDTO morador, CondominioDTO condominioDTO) throws ServiceException;

	public List<ReservaDTO> listarReservaPorCondominio(final CondominioDTO condominio) throws ServiceException;

	public List<ReservaTempDTO> getReservasPendenteAprovacao(CondominioDTO condominioDTO) throws ServiceException;

	public boolean itemReservaEstaPendenteAprovacaoNaData(final ReservaDTO reserva) throws ServiceException;
}
