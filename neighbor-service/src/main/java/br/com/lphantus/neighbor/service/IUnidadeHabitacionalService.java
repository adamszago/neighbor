package br.com.lphantus.neighbor.service;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IUnidadeHabitacionalService extends
		IGenericService<Long, UnidadeHabitacionalDTO, UnidadeHabitacional> {

	public UnidadeHabitacionalDTO buscarUnidadeHabitacionalMorador(
			final MoradorDTO morador) throws ServiceException;

	public Long obterUltimaUnidadeInserida() throws ServiceException;

}
