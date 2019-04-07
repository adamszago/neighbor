package br.com.lphantus.neighbor.repository;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

import br.com.lphantus.neighbor.repository.exception.DAOException;

public interface IGenericDAO<T> {

	/**
	 * Persiste um entity
	 * 
	 * @param <T>
	 * @param entity
	 * @return T
	 */
	void save(T entity) throws DAOException;

	/**
	 * Persiste ou Update um entity
	 * 
	 * @param entity
	 * @return
	 * @throws DAOException
	 */
	void saveOrUpdate(T entity) throws DAOException;

	/**
	 * Atualiza um entity
	 * 
	 * @param <T>
	 * @param entity
	 * @return T
	 */
	void update(T entity) throws DAOException;

	/**
	 * Consulta um entity atraves do identificador id
	 * 
	 * @param beanClass
	 * @param id
	 * @return T
	 */
	T findById(Class<T> beanClass, Serializable id) throws DAOException;

	/**
	 * Realiza uma consulta recebendo um hql como parametro.
	 * 
	 * @param hql
	 * @return List
	 */
	List<T> findByQuery(String hql) throws DAOException;

	/**
	 * Realiza uma consulta de acordo com os criterions e retorna um unico
	 * registro
	 * 
	 * @param criterion
	 * @return T
	 */
	T findByCriteriaUnique(Class<T> clazz, Criterion[] criterion)
			throws DAOException;

	/**
	 * Recarrega o objeto passado como parametro com os valores do banco.
	 * 
	 * @param entity
	 * @return T
	 */
	void delete(T entity) throws DAOException;

	/**
	 * Atualiza as informacoes do objeto recem gravado no banco. Util quando
	 * queremos saber o id gerado no banco apos ter salvo.
	 * 
	 * @param beanClass
	 * @return void
	 */
	void refresh(T entity);

	List<T> findAll() throws DAOException;

}
