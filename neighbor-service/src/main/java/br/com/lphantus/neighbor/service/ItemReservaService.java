package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ItemReservaDTO;
import br.com.lphantus.neighbor.entity.ItemReserva;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ItemReservaService extends IGenericService<Long, ItemReservaDTO, ItemReserva> {

	public List<ItemReservaDTO> findAtivos() throws ServiceException;

	public List<ItemReservaDTO> listarPorCondominio(CondominioDTO condominio,
			Boolean status) throws ServiceException;

}
