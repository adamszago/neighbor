package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IAgregadoService extends
		IGenericService<Long, AgregadoDTO, Agregado> {

	List<AgregadoDTO> listarAgregadosMorador(MoradorDTO morador)
			throws ServiceException;

	List<AgregadoDTO> listarTodosAgregados(CondominioDTO condominio)
			throws ServiceException;

}
