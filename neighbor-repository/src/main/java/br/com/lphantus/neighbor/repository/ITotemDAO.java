package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.AgregadoDTO;
import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.MoradorDTO;
import br.com.lphantus.neighbor.common.TotemDTO;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface ITotemDAO extends IGenericDAO<Totem> {

	TotemDTO existeUsuarioCadastrado(TotemDTO totem) throws DAOException;

	TotemDTO buscarAgregadoTotem(AgregadoDTO agregado) throws DAOException;

	TotemDTO buscarMoradorTotem(MoradorDTO morador) throws DAOException;

	List<TotemDTO> findAllAtivos() throws DAOException;

	List<TotemDTO> buscarTodosAtivosPorCondominio(CondominioDTO condominio)
			throws DAOException;

	List<TotemDTO> buscarMoradorSemTotemOuInativoPorCondominio(
			CondominioDTO condominio) throws DAOException;

	List<TotemDTO> buscarAgregadoSemTotemOuInativoPorCondominio(
			CondominioDTO condominio) throws DAOException;

	List<TotemDTO> buscarTodosAtivosPorMorador(MoradorDTO morador)
			throws DAOException;

	List<TotemDTO> buscarMoradorSemTotemOuInativoPorMorador(MoradorDTO morador)
			throws DAOException;

	List<TotemDTO> buscarAgregadoSemTotemOuInativoPorMorador(MoradorDTO morador)
			throws DAOException;
}