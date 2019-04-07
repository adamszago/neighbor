package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.ServicoPrestadoDTO;
import br.com.lphantus.neighbor.entity.ServicoPrestado;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IServicoPrestadoDAO extends IGenericDAO<ServicoPrestado> {

	List<ServicoPrestadoDTO> buscarServicoPorMorador(MoradorDTO morador)
			throws DAOException;

	List<ServicoPrestadoDTO> buscarServicosAgendados(CondominioDTO condominio)
			throws DAOException;

	List<ServicoPrestadoDTO> buscarServicosAgendadosMorador(MoradorDTO morador)
			throws DAOException;

}
