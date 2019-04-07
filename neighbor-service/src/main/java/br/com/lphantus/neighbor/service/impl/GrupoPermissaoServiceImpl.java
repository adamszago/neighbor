package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.entity.GrupoPermissao;
import br.com.lphantus.neighbor.repository.IGrupoPermissaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IGrupoPermissaoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class GrupoPermissaoServiceImpl extends
		GenericService<Long, GrupoPermissaoDTO, GrupoPermissao> implements
		IGrupoPermissaoService {

	@Autowired
	private IGrupoPermissaoDAO grupoPermissaoDAO;

	@Override
	public GrupoPermissaoDTO buscaPorNome(final String nome)
			throws ServiceException {
		try {
			return this.grupoPermissaoDAO.buscaPorNome(nome);
		} catch (final DAOException daoException) {
			throw new ServiceException("Erro ao buscar por nome.", daoException);
		}
	}

	@Override
	public List<GrupoPermissaoDTO> buscarTodos() throws ServiceException {
		try {
			return this.grupoPermissaoDAO.buscarTodos();
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar todos grupos de permissao.",
					exception);
			throw new ServiceException(exception.getMessage(), exception);
		}
	}

}
