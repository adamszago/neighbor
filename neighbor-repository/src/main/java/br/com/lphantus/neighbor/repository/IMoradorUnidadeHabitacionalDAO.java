package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorUnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.MoradorUnidadeHabitacional;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMoradorUnidadeHabitacionalDAO extends IGenericDAO<MoradorUnidadeHabitacional> {

	List<MoradorUnidadeHabitacionalDTO> listarMoradoresCondominio(CondominioDTO condominio, Boolean status) throws DAOException;

}