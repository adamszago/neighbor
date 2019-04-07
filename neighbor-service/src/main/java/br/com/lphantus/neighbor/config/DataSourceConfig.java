package br.com.lphantus.neighbor.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;

/**
 * Habilita configuração de conexão via datasource.
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
@Configuration
public interface DataSourceConfig {

	/**
	 * Retorna um datasource
	 * 
	 * @return O datasource.
	 */
	public DataSource dataSource();

}