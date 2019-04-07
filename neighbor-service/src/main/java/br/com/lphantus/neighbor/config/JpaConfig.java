package br.com.lphantus.neighbor.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import br.com.lphantus.neighbor.util.HibernateExtendedJpaDialect;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesJPA;

/**
 * Habilita o gerenciamento de transações e configura os beans necessários para
 * que o spring funcione com JPA.
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
@Configuration
@EnableTransactionManagement(order = 1)
public class JpaConfig {

	@Value("${hibernate.dialect}")
	private String dialect;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto;
	@Value("${hibernate.show_sql}")
	private Boolean showSql;
	@Value("${hibernate.format_sql}")
	private Boolean formatSql;

	/**
	 * Configura a fábrica de entitymanagers
	 * 
	 * @return Bean spring
	 */
	@Bean
	@Qualifier("entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean configureEntityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();

		bean.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
		bean.setPersistenceUnitName(ConstantesJPA.PERSISTENCE_UNIT);
		bean.setJtaDataSource(configureDataSource());
		bean.setJpaVendorAdapter(configureJpaVendor());
		bean.setJpaProperties(configureJpaProperties());
		bean.setJpaDialect(new HibernateExtendedJpaDialect());

		return bean;
	}

	/**
	 * Inicialização do datasource.
	 * 
	 * @return Um bean Spring
	 */
	@Bean
	@Qualifier("dataSource")
	public DataSource configureDataSource() {
		final JndiDataSourceLookup lookup = new JndiDataSourceLookup();
		final DataSource ds = lookup.getDataSource(ConstantesJPA.JNDI_DATASOURCE);
		return ds;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager manager = new JpaTransactionManager(entityManagerFactory);
		manager.setNestedTransactionAllowed(true);
		manager.setRollbackOnCommitFailure(true);
		manager.setDataSource(configureDataSource());
		return manager;
		// JtaTransactionManager manager = new JtaTransactionManager();
		// manager.setTransactionManagerName("java:jboss/TransactionManager");
		// manager.setUserTransactionName("java:jboss/UserTransaction");
		// return manager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Configura as propriedades jpa do datasource
	 * 
	 * @return Propriedades
	 */
	private Properties configureJpaProperties() {
		final Properties jpaProperties = new Properties();
		jpaProperties.put(org.hibernate.cfg.Environment.DIALECT, this.dialect);
		jpaProperties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, this.hbm2ddlAuto);
		jpaProperties.put(org.hibernate.cfg.Environment.AUTOCOMMIT, Boolean.TRUE);
		jpaProperties.put(org.hibernate.cfg.Environment.FORMAT_SQL, Boolean.valueOf(this.formatSql));
		jpaProperties.put(org.hibernate.cfg.Environment.SHOW_SQL, Boolean.valueOf(this.showSql));
		jpaProperties.put(org.hibernate.cfg.Environment.USE_SECOND_LEVEL_CACHE, Boolean.FALSE);
		// jpaProperties
		// .put("hibernate.transaction.jta.platform",
		// "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");
		// jpaProperties.put("hibernate.transaction.factory_class",
		// "org.hibernate.transaction.JTATransactionFactory");
		// jpaProperties.put("hibernate.transaction.manager_lookup_class",
		// "org.hibernate.transaction.JBossTransactionManagerLookup");
		return jpaProperties;
	}

	/**
	 * Configura o adapter jpa da aplicacao (se é hibernate, openjpa,
	 * eclipselink, etc).
	 * 
	 * @return O adapter.
	 */
	@Bean
	public JpaVendorAdapter configureJpaVendor() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(Boolean.valueOf(this.showSql));
		adapter.setGenerateDdl(false);
		return adapter;
	}

}
