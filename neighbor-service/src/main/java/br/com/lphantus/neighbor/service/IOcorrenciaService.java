package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.OcorrenciaDTO;
import br.com.lphantus.neighbor.entity.Ocorrencia;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author aalencar
 * @param <T>
 * 
 */
public interface IOcorrenciaService extends IGenericService<Long, OcorrenciaDTO, Ocorrencia> {

	List<OcorrenciaDTO> listarOcorrenciasPorMorador(final MoradorDTO morador)
			throws ServiceException;

	List<OcorrenciaDTO> listarOcorrenciasPorCondominio(
			final CondominioDTO condominio) throws ServiceException;

}
