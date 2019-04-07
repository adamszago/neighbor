package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.entity.AgendaSindico;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IAgendaSindicoDAO extends IGenericDAO<AgendaSindico> {

	void atualizarAgendaSindico(AgendaSindico evento) throws DAOException;

	List<AgendaSindico> listarPorCondominio(CondominioDTO condominio)
			throws DAOException;

}
