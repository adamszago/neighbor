package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.TipoAnimalDTO;
import br.com.lphantus.neighbor.entity.TipoAnimal;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ITipoAnimalDAO extends IGenericDAO<TipoAnimal> {

	public List<TipoAnimalDTO> findAtivos() throws DAOException;

	public List<TipoAnimalDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

}
