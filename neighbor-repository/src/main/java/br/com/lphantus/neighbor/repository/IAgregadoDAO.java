package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.Agregado;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IAgregadoDAO extends IGenericDAO<Agregado> {

	List<AgregadoDTO> listarAgregadosMorador(MoradorDTO morador)
			throws DAOException;

	List<AgregadoDTO> listarTodosAgregados(CondominioDTO condominio)
			throws DAOException;

}
