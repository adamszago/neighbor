package br.com.lphantus.neighbor.service.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.lphantus.neighbor.common.AbstractDTO;
import br.com.lphantus.neighbor.repository.IGenericDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.service.IGenericService;
import br.com.lphantus.neighbor.service.exception.ServiceException;
import br.com.lphantus.neighbor.util.GerenciadorMensagem;

/**
 * Classe abstrata da camada de serviços.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 23/11/2014
 *
 * @param <Chave>
 *            A chave da entidade
 * @param <TipoDTO>
 *            O DTO da entidade
 * @param <Entidade>
 *            A classe da entidade
 */
public abstract class GenericService<Chave extends Serializable, TipoDTO extends AbstractDTO, Entidade>
		implements IGenericService<Chave, TipoDTO, Entidade> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext context;

	@Autowired
	private IGenericDAO<Entidade> genericDAO;

	protected void saveValidate(final Entidade entity) throws ServiceException {
		if (entity == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("PARAMETRO_NULO"));
		}
	}

	@Override
	public void save(final Entidade entity) throws ServiceException {
		saveValidate(entity);

		try {
			this.genericDAO.save(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void saveOrUpdate(final Entidade entity) throws ServiceException {
		if (entity == null) {
			throw new ServiceException(
					"Erro ao salvar entidade, parametro nulo.");
		}

		try {
			this.genericDAO.saveOrUpdate(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	protected void updateValidate(final Entidade entity)
			throws ServiceException {
		if (entity == null) {
			throw new ServiceException(
					GerenciadorMensagem.getMensagem("PARAMETRO_NULO"));
		}
	}

	@Override
	public void update(final Entidade entity) throws ServiceException {
		updateValidate(entity);

		try {
			this.genericDAO.update(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void delete(final Entidade entity) throws ServiceException {
		if (entity == null) {
			throw new ServiceException(
					"Erro ao salvar entidade, parametro nulo.");
		}

		try {
			this.genericDAO.delete(entity);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Entidade findById(final Chave id) throws ServiceException {
		if ((id == null)) {
			throw new ServiceException("Erro ao buscar objeto, parametro nulo.");
		}

		try {
			return this.genericDAO.findById(getPersistenceClass(), id);
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Entidade> findAll() throws ServiceException {
		try {
			return this.genericDAO.findAll();
		} catch (final DAOException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void refresh(final Entidade entity) throws ServiceException {
		this.genericDAO.refresh(entity);
	}

	@SuppressWarnings("unchecked")
	public Class<Entidade> getPersistenceClass() {
		return (Class<Entidade>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[2];
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

}
