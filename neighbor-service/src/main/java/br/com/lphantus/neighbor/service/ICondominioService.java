package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ICondominioService extends
		IGenericService<Long, CondominioDTO, Condominio> {

	CondominioDTO buscarPorNome(String nome) throws ServiceException;

	List<CondominioDTO> buscarTodos() throws ServiceException;

}
