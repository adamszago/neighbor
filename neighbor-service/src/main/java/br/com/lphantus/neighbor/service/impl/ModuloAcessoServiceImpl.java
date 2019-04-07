package br.com.lphantus.neighbor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.CondominioDTO;
import br.com.lphantus.neighbor.common.ModuloAcessoDTO;
import br.com.lphantus.neighbor.entity.ModuloAcesso;
import br.com.lphantus.neighbor.repository.IModuloAcessoDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IModuloAcessoService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@Service
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional(propagation = Propagation.SUPPORTS)
public class ModuloAcessoServiceImpl extends GenericService<Long, ModuloAcessoDTO, ModuloAcesso> implements IModuloAcessoService {

	@Autowired
	private IModuloAcessoDAO moduloAcessoDao;

	@Override
	public List<ModuloAcessoDTO> findAllWithoutRoot() throws ServiceException {
		try {
			final String hql = "FROM ModuloAcesso WHERE nome <> 'Root' ";
			return ModuloAcessoDTO.Builder.getInstance().createList(this.moduloAcessoDao.findByQuery(hql));
		} catch (final Exception exception) {
			getLogger().debug("Erro ao buscar todas sem root.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public List<ModuloAcessoDTO> findAllByIdCondominioWithoutRoot(final Long idCondominio) throws ServiceException {
		try {
			if (idCondominio != null) {
				final String hql = "SELECT modulo " + "FROM ModuloAcesso modulo " + "INNER JOIN modulo.permissoes permissoes " + "INNER JOIN modulo.condominio condominio "
						+ "WHERE condominio.idPessoa = " + idCondominio + " OR modulo.nome = 'Morador'" + " AND modulo.nome <> 'Root' ";

				return ModuloAcessoDTO.Builder.getInstance().createList(this.moduloAcessoDao.findByQuery(hql));
			} else {
				throw new ServiceException("ID Condominio null");
			}
		} catch (final Exception exception) {
			getLogger().debug("Erro ao buscar por condominio sem root.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public ModuloAcessoDTO getModuloPadraoMorador() throws ServiceException {
		try {

			ModuloAcessoDTO retorno = moduloAcessoDao.findModuloByNome("Morador");
			return retorno;

		} catch (final DAOException daoException) {
			getLogger().debug("Erro ao buscar modulo padrao morador.", daoException);
			throw new ServiceException(daoException.getMessage());
		} catch (final Exception exception) {
			getLogger().debug("Erro ao buscar modulo padrao morador.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public void save(final ModuloAcesso moduloAcesso) throws ServiceException {
		try {
			if (this.moduloAcessoDao.findModuloByNome(moduloAcesso.getNome()) != null) {
				throw new ServiceException("Já existe um módulo de acesso com este nome!");
			}
			this.moduloAcessoDao.save(moduloAcesso);
		} catch (final DAOException daoException) {
			getLogger().debug("Erro ao salvar.", daoException);
			throw new ServiceException(daoException.getMessage());
		} catch (final Exception exception) {
			getLogger().debug("Erro ao salvar.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}

	@Override
	public ModuloAcessoDTO buscaModuloRoot() throws ServiceException {
		try {
			return this.moduloAcessoDao.findModuloByNome("Root");
		} catch (final DAOException daoException) {
			getLogger().debug("Erro ao buscar modulo root.", daoException);
			throw new ServiceException("Erro ao buscar modulo do root.", daoException);
		}
	}

	@Override
	public ModuloAcessoDTO buscaPorNome(final String nomeModulo) throws ServiceException {
		try {
			return this.moduloAcessoDao.findModuloByNome(nomeModulo);
		} catch (final DAOException daoException) {
			getLogger().debug("Erro ao buscar por nome.", daoException);
			throw new ServiceException(String.format("Erro ao buscar modulo '%s'.", nomeModulo), daoException);
		}
	}

	@Override
	public List<ModuloAcessoDTO> buscarPorCondominio(CondominioDTO condominio, boolean buscarRoot) throws ServiceException {
		try {
			return moduloAcessoDao.buscarPorCondominio(condominio, buscarRoot);
		} catch (final DAOException exception) {
			getLogger().debug("Erro ao buscar por condominio.", exception);
			throw new ServiceException(exception.getMessage());
		}
	}
}
