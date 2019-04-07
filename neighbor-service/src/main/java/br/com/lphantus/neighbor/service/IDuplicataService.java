package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.DuplicataDTO;
import br.com.lphantus.neighbor.common.FaturaDTO;
import br.com.lphantus.neighbor.entity.Duplicata;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IDuplicataService extends IGenericService<Long, DuplicataDTO, Duplicata> {

	List<DuplicataDTO> buscarPorCondominio(CondominioDTO condominio) throws ServiceException;

	List<DuplicataDTO> buscarAtivasPorFatura(FaturaDTO item) throws ServiceException;

}