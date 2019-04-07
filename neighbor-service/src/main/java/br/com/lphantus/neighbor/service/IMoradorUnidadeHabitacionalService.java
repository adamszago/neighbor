package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacionalPK;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IMoradorUnidadeHabitacionalService extends IGenericService<MoradorUnidadeHabitacionalPK, MoradorUnidadeHabitacionalDTO, MoradorUnidadeHabitacional> {

	List<MoradorUnidadeHabitacionalDTO> listarMoradoresCondominio(CondominioDTO condominio, Boolean status) throws ServiceException;

	void atualizarRelacionamento(MoradorUnidadeHabitacionalDTO relacionamento, MoradorDTO morador) throws ServiceException;

}