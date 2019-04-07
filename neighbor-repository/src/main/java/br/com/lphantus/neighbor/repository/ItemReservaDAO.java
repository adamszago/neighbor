package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.entity.ItemReserva;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ItemReservaDAO extends IGenericDAO<ItemReserva> {

	public List<ItemReservaDTO> findAtivos() throws DAOException;

	public List<ItemReservaDTO> listarPorCondominio(CondominioDTO condominio,
			Boolean status) throws DAOException;
}
