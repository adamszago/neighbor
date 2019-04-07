package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.Condominio;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ICondominioDAO extends IGenericDAO<Condominio> {

	CondominioDTO buscarPorNome(String nome) throws DAOException;

	List<CondominioDTO> buscarTodos() throws DAOException;

}
