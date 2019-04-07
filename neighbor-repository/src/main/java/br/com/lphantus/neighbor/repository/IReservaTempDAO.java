package br.com.lphantus.neighbor.repository;

import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaTempDTO;
import br.com.lphantus.neighbor.entity.ReservaTemp;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IReservaTempDAO extends IGenericDAO<ReservaTemp> {

	public void save(ReservaTemp reserva) throws DAOException;

	public ReservaTempDTO verificaDisponibilidade(ReservaTempDTO reserva)
			throws DAOException;

	public ReservaTempDTO verificarCarencia(ReservaTempDTO reserva)
			throws DAOException;

	public ReservaTempDTO verificarReservaDataCliente(ReservaTempDTO reserva)
			throws DAOException;

	public List<ReservaTempDTO> listarReservaPorMorador(MoradorDTO morador)
			throws DAOException;

	boolean itemReservaEstaPendenteMorador(ItemReservaDTO itemReserva,
			MoradorDTO morador) throws DAOException;

	boolean itemReservaEstaPendenteAprovacaoNaData(ItemReservaDTO itemReserva,
			Date data) throws DAOException;

	public List<ReservaTempDTO> buscaReservasPendentesAprovacao(
			CondominioDTO condominio) throws DAOException;
}
