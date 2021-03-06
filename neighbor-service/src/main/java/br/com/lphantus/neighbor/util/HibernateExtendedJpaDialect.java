package br.com.lphantus.neighbor.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;

/**
 * Como o controle de transações não é muito bem "entrelaçado" quando utilizamos
 * Spring + Hibernate (falta de aderência à especificação EJB), precisamos
 * utilizar esta implementação, que realiza reset das conexões quando a
 * transação é finalizada, liberando os recursos para serem executados em
 * paralelo (READ_COMMITED). Ver
 * http://stackoverflow.com/questions/5234240/hibernatespringjpaisolation
 * -does-not-work
 * 
 * @author elias.policena@lphantus.com.br
 * @since 10/12/2014
 *
 */
public class HibernateExtendedJpaDialect extends HibernateJpaDialect {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory
			.getLogger(HibernateExtendedJpaDialect.class);

	/**
	 * This method is overridden to set custom isolation levels on the
	 * connection
	 * 
	 * @param entityManager
	 * @param definition
	 * @return
	 * @throws PersistenceException
	 * @throws SQLException
	 * @throws TransactionException
	 */
	@Override
	public Object beginTransaction(final EntityManager entityManager,
			final TransactionDefinition definition)
			throws PersistenceException, SQLException, TransactionException {
		Session session = (Session) entityManager.getDelegate();
		if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
			getSession(entityManager).getTransaction().setTimeout(
					definition.getTimeout());
		}

		entityManager.getTransaction().begin();
		logger.debug("Transaction started");

		session.doWork(new Work() {

			public void execute(Connection connection) throws SQLException {
				logger.debug("The connection instance is {}", connection);
				logger.debug(
						"The isolation level of the connection is {} and the isolation level set on the transaction is {}",
						connection.getTransactionIsolation(),
						definition.getIsolationLevel());
				DataSourceUtils.prepareConnectionForTransaction(connection,
						definition);
			}
		});

		return prepareTransaction(entityManager, definition.isReadOnly(),
				definition.getName());
	}

}