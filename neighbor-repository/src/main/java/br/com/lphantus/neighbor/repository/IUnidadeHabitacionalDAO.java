package br.com.lphantus.neighbor.repository;

import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.UnidadeHabitacionalDTO;
import br.com.lphantus.neighbor.entity.UnidadeHabitacional;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IUnidadeHabitacionalDAO extends
		IGenericDAO<UnidadeHabitacional> {

	public UnidadeHabitacionalDTO buscarUnidadeHabitacionalMorador(
			final MoradorDTO morador) throws DAOException;

	public Long obterUltimaUnidadeInserida() throws DAOException;
	
}
