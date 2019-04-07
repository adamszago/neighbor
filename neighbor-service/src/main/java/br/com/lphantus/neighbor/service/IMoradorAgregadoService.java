package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.entity.MoradorAgregadoPK;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IMoradorAgregadoService extends
		IGenericService<MoradorAgregadoPK, MoradorAgregadoDTO, MoradorAgregado> {

	public List<MoradorAgregadoDTO> listarAgregadosAtivosMorador(
			MoradorDTO morador) throws ServiceException;

}