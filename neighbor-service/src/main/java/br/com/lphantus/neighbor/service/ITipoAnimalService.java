package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TipoAnimalDTO;
import br.com.lphantus.neighbor.entity.TipoAnimal;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ITipoAnimalService extends
		IGenericService<Long, TipoAnimalDTO, TipoAnimal> {

	public List<TipoAnimalDTO> findAtivos() throws ServiceException;

	public List<TipoAnimalDTO> buscarPorCondominio(CondominioDTO condominio)
			throws ServiceException;

}
