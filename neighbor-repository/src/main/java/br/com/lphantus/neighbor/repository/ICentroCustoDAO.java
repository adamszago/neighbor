package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.CentroCusto;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ICentroCustoDAO extends IGenericDAO<CentroCusto> {

	List<CentroCustoDTO> buscarPorCondominio(Boolean status,
			CondominioDTO condominio) throws DAOException;

	List<CentroCustoDTO> findCentrosCustoRaizByCondominio(
			CondominioDTO condominio) throws DAOException;

	List<CentroCustoDTO> findCentrosCustoFilhos(CentroCustoDTO centroCustoPai)
			throws DAOException;

}
