package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.TipoPrestadorDTO;
import br.com.lphantus.neighbor.entity.TipoPrestador;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ITipoPrestadorService extends
		IGenericService<Long, TipoPrestadorDTO, TipoPrestador> {

	List<TipoPrestadorDTO> findAllTipoPrestador() throws ServiceException;

}
