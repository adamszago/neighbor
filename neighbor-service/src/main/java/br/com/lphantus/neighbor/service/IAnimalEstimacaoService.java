package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.AnimalEstimacaoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.AnimalEstimacao;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author aalencar
 * @param <T>
 * 
 */
public interface IAnimalEstimacaoService extends
		IGenericService<Long, AnimalEstimacaoDTO, AnimalEstimacao> {

	public List<AnimalEstimacaoDTO> listarAnimaisMorador(
			final MoradorDTO morador) throws ServiceException;

	public List<AnimalEstimacaoDTO> findByCondominio(CondominioDTO condominio)
			throws ServiceException;

}
