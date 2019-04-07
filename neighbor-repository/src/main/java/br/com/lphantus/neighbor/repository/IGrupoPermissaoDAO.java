package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IGrupoPermissaoDAO extends IGenericDAO<GrupoPermissao> {

	GrupoPermissaoDTO buscaPorNome(String nome) throws DAOException;

	List<GrupoPermissaoDTO> buscarTodos() throws DAOException;

}
