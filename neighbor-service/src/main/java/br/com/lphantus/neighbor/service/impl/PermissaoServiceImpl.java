package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.GrupoPermissaoDTO;
import br.com.lphantus.neighbor.common.PermissaoDTO;
import br.com.lphantus.neighbor.common.PlanoDTO;
import br.com.lphantus.neighbor.entity.Permissao;
import br.com.lphantus.neighbor.repository.IPermissaoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IPermissaoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class PermissaoServiceImpl extends GenericService<Long, PermissaoDTO, Permissao> implements IPermissaoService {

	@Autowired
	private IPermissaoDAO permissaoDAO;

	@Override
	public PermissaoDTO buscaPorLabel(final String label) throws ServiceException {
		try {
			return this.permissaoDAO.buscaPorLabel(label);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar permissoes por label.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public List<PermissaoDTO> findByPlano(final PlanoDTO plano) throws ServiceException {
		try {
			return this.permissaoDAO.findByPlano(plano);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar permissoes por plano.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public PermissaoDTO buscaPorNome(final String nome) throws ServiceException {
		try {
			return this.permissaoDAO.buscaPorNome(nome);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar permissoes por nome.", exception);
			throw new ServiceException("Erro ao buscar pelo nome.", exception);
		}
	}

	@Override
	public PermissaoDTO buscaPermissaoRoot() throws ServiceException {
		try {
			return this.permissaoDAO.buscaPorNome("ROLE_ROOT");
		} catch (final DAOException exception) {
			getLogger().error("Erro ao buscar permissao root.", exception);
			throw new ServiceException("Erro ao buscar permissao do root.", exception);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void atualizaGrupo(final PermissaoDTO permissaoBanco, final GrupoPermissaoDTO grp) throws ServiceException {
		try {
			this.permissaoDAO.atualizaGrupo(permissaoBanco, grp);
		} catch (final DAOException exception) {
			getLogger().error("Erro ao atualizar grupo da permissao.", exception);
			throw new ServiceException("Erro ao atualizar grupo da permissao.", exception);
		}
	}

	@Override
	public List<PermissaoDTO> buscarTodas() throws ServiceException {
		try {
			return this.permissaoDAO.buscarTodas();
		} catch (final DAOException exception) {
			getLogger().debug("Erro ao buscar todas permissoes.", exception);
			throw new ServiceException("Erro ao buscar permissoes.", exception);
		}
	}

}
