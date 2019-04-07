package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.MoradorAgregadoDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.entity.MoradorAgregado;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IMoradorAgregadoDAO extends IGenericDAO<MoradorAgregado> {

	List<MoradorAgregadoDTO> listarAgregadosAtivosMorador(MoradorDTO morador)
			throws DAOException;

}