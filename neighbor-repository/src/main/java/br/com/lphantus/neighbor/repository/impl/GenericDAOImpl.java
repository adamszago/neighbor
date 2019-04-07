package br.com.lphantus.neighbor.repository.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.lphantus.neighbor.common.AbstractDTO;
import br.com.lphantus.neighbor.repository.IGenericDAO;
import br.com.lphantus.neighbor.repository.exception.DAOException;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

/**
 * Classe abstrata de comunicacao com a base de dados.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 26/09/2014
 * 
 * @param <Chave>
 *            A chave da entidade
 * @param <Entidade>
 *            A classe da entidade
 */
@Transactional(propagation = Propagation.REQUIRED)
public class GenericDAOImpl<Chave, TipoDTO extends AbstractDTO, Entidade>
		implements IGenericDAO<Entidade> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = ConstantesJPA.PERSISTENCE_UNIT)
	private EntityManager entityManager;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Entidade entity) throws DAOException {
		try {
			entityManager.persist(entity);
		} catch (PersistenceException e) {
			getLogger().error("Erro ao salvar dados.", e);
			throw new DAOException("Erro ao salvar dados.", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveOrUpdate(Entidade entity) throws DAOException {
		try {
			entityManager.merge(entity);
		} catch (PersistenceException e) {
			getLogger().error("Erro ao atualizar dados.", e);
			throw new DAOException("Erro ao atualizar dados.", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(final Entidade entity) throws DAOException {
		try {
			entityManager.merge(entity);
		} catch (PersistenceException e) {
			getLogger().error("Erro ao atualizar dados.", e);
			throw new DAOException("Erro ao atualizar dados.", e);
		}
	}

	@Override
	public Entidade findById(Class<Entidade> beanClass, Serializable id)
			throws DAOException {
		return entityManager.find(beanClass, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entidade> findAll() throws DAOException {
		String hql = String.format("SELECT entidade FROM %s entidade",
				getPersistenceClass().getSimpleName());
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Entidade> findByQuery(String hql) throws DAOException {
		Query query = entityManager.createQuery(hql);
		return query.getResultList();
	}

	@Override
	public Entidade findByCriteriaUnique(Class<Entidade> clazz,
			Criterion[] criterion) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Entidade entity) throws DAOException {
		final Entidade entidade;
		if (!entityManager.contains(entity)) {
			entidade = entityManager.merge(entity);
		} else {
			entidade = entity;
		}
		this.entityManager.remove(entidade);
		// this.entityManager.flush();
	}

	@Override
	public void refresh(Entidade entity) {
		entityManager.refresh(entity);
	}

	@SuppressWarnings("unchecked")
	public Class<Entidade> getPersistenceClass() {
		return (Class<Entidade>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[2];
	}

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

}
