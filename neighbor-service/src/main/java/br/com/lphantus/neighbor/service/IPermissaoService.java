package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.service.exception.ServiceException;

/**
 * 
 * @author Joander Vieira
 * @param <T>
 * 
 */
public interface IPermissaoService extends IGenericService<Long, PermissaoDTO, Permissao> {

	PermissaoDTO buscaPorLabel(String label) throws ServiceException;

	PermissaoDTO buscaPorNome(String nome) throws ServiceException;

	List<PermissaoDTO> findByPlano(PlanoDTO plano) throws ServiceException;

	PermissaoDTO buscaPermissaoRoot() throws ServiceException;

	void atualizaGrupo(PermissaoDTO permissaoBanco, GrupoPermissaoDTO grp) throws ServiceException;

	List<PermissaoDTO> buscarTodas() throws ServiceException;

}
