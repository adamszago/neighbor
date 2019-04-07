package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.VisitanteDTO;
import br.com.lphantus.neighbor.entity.Visitante;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author florenar
 * @param <T>
 * 
 */
public interface IVisitanteService extends
		IGenericService<Long, VisitanteDTO, Visitante> {

	public boolean existeCpf(final VisitanteDTO visitante)
			throws ServiceException;

	public List<VisitanteDTO> getAllVisitantesConfirmados()
			throws ServiceException;

	public List<VisitanteDTO> getAllVisitantesConfirmadosByCondominio(
			final CondominioDTO condominio) throws ServiceException;

	public List<VisitanteDTO> buscarPorStatus(CondominioDTO condominio,
			Boolean status) throws ServiceException;

}
