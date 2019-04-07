package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ReservaDTO;
import br.com.lphantus.neighbor.entity.Reserva;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IReservaDAO extends IGenericDAO<Reserva> {

	@Override
	public void save(Reserva reserva) throws DAOException;

	public ReservaDTO verificaDisponibilidade(ReservaDTO reserva) throws DAOException;

	public ReservaDTO verificarCarencia(ReservaDTO reserva) throws DAOException;

	public ReservaDTO verificarReservaDataCliente(ReservaDTO reserva) throws DAOException;

	public List<ReservaDTO> listarReservaPorMorador(MoradorDTO morador, CondominioDTO condominio) throws DAOException;

	public List<ReservaDTO> listarReservaPorCondominio(CondominioDTO condominio) throws DAOException;
}
