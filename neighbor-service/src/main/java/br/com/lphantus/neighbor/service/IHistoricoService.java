package br.com.lphantus.neighbor.service;

import java.util.Date;
import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.HistoricoDTO;
import br.com.lphantus.neighbor.common.UtilizacaoDTO;
import br.com.lphantus.neighbor.entity.Historico;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author Joander Vieira
 * @param <T>
 * 
 */
public interface IHistoricoService extends
		IGenericService<Long, HistoricoDTO, Historico> {

	public List<HistoricoDTO> findAllByIdCondominio(Long idCondominio)
		throws ServiceException;
	
	public List<UtilizacaoDTO> consultaUtilizacao(Date dataMes, CondominioDTO condominio)
		throws ServiceException;

}
