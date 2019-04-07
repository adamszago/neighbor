package br.com.lphantus.neighbor.repository;

import br.com.lphantus.neighbor.entity.Banco;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IBancoDAO extends IGenericDAO<Banco> {

	boolean existeBanco(Long id) throws DAOException;

}
