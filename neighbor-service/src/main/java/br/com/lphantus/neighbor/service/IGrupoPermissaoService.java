package br.com.lphantus.neighbor.service;

import java.util.List;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.service.exception.ServiceException;

public interface IGrupoPermissaoService extends
		IGenericService<Long, GrupoPermissaoDTO, GrupoPermissao> {

	GrupoPermissaoDTO buscaPorNome(String nome) throws ServiceException;

	List<GrupoPermissaoDTO> buscarTodos() throws ServiceException;

}
