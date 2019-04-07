package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CentroCustoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.CentroCusto;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface ICentroCustoService extends
		IGenericService<Long, CentroCustoDTO, CentroCusto> {

	List<CentroCustoDTO> findCentrosCustoRaizByCondominio(
			CondominioDTO condominio) throws ServiceException;

	List<CentroCustoDTO> findCentrosCustoFilhos(CentroCustoDTO centroCustoPai)
			throws ServiceException;

	List<CentroCustoDTO> buscarPorCondominio(Boolean status,
			CondominioDTO condominio) throws ServiceException;

	boolean existeCentroCustoLancavel(CondominioDTO condominio);

}
