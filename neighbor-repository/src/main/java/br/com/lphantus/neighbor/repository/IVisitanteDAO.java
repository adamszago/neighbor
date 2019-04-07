package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IVisitanteDAO extends IGenericDAO<Visitante> {

	boolean existeCpf(VisitanteDTO visitante) throws DAOException;

	List<VisitanteDTO> findAllAtivosByIdCondominio(Long id) throws DAOException;

	List<VisitanteDTO> buscarPorStatus(CondominioDTO condominio, Boolean status)
			throws DAOException;
}
