package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IDuplicataDAO extends IGenericDAO<Duplicata> {

	List<DuplicataDTO> buscarPorCondominio(CondominioDTO condominio)
			throws DAOException;

	List<DuplicataDTO> buscarAtivasPorFatura(FaturaDTO item)
			throws DAOException;

}