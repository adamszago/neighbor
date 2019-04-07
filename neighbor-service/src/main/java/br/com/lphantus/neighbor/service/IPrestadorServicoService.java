package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.PrestadorServicoDTO;
import br.com.lphantus.neighbor.entity.PrestadorServico;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author florenar
 * @param <T>
 * @version 1.2 Alterado em 07/09/2013 por Adams Zago Inclusao metodos de
 *          servico agendado
 * 
 */
public interface IPrestadorServicoService extends IGenericService<Long, PrestadorServicoDTO, PrestadorServico> {

	public PrestadorServicoDTO buscarPorId(PrestadorServicoDTO prestador) throws ServiceException;

	public boolean existeDocumento(final PrestadorServicoDTO prestador) throws ServiceException;

	public List<PrestadorServicoDTO> buscarConfirmadosPorCondominio(final CondominioDTO condominio) throws ServiceException;

	public List<PrestadorServicoDTO> buscarPorCondominio(final CondominioDTO condominio) throws ServiceException;
}
