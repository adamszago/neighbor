package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.entity.ModuloAcesso;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author Joander Vieira
 * @param <T>
 * 
 */
public interface IModuloAcessoService extends IGenericService<Long, ModuloAcessoDTO, ModuloAcesso> {

	List<ModuloAcessoDTO> findAllWithoutRoot() throws ServiceException;

	List<ModuloAcessoDTO> findAllByIdCondominioWithoutRoot(final Long idCondominio) throws ServiceException;

	ModuloAcessoDTO getModuloPadraoMorador() throws ServiceException;

	ModuloAcessoDTO buscaModuloRoot() throws ServiceException;

	ModuloAcessoDTO buscaPorNome(String nomeModulo) throws ServiceException;

	List<ModuloAcessoDTO> buscarPorCondominio(CondominioDTO condominio, boolean buscarRoot) throws ServiceException;

}
