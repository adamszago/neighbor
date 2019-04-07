package br.com.lphantus.neighbor.repository;

import java.util.List;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IPermissaoDAO extends IGenericDAO<Permissao> {

	PermissaoDTO buscaPorLabel(String label) throws DAOException;

	PermissaoDTO buscaPorNome(String nome) throws DAOException;

	void atualizaGrupo(PermissaoDTO permissaoBanco, GrupoPermissaoDTO grp) throws DAOException;

	List<PermissaoDTO> buscarTodas() throws DAOException;

	List<PermissaoDTO> findByPlano(PlanoDTO plano) throws DAOException;

}
