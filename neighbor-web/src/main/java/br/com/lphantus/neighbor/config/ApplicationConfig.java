package br.com.lphantus.neighbor.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import br.com.lphantus.neighbor.Application;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesConfiguracao;

/**
 * Classe responsável por inicializar a aplicação (contexto spring)
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
@Configuration
@ComponentScan(basePackageClasses = { Application.class })
public class ApplicationConfig {

	/**
	 * Preenche os campos anotados com <code>@Value</code> com o conteúdo do
	 * arquivo de propriedades.
	 * 
	 * @return Um bean spring.
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		final PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new ClassPathResource[] {
				new ClassPathResource(
						ConstantesConfiguracao.CONF_CAMINHO_LOCATION),
				new ClassPathResource(ConstantesConfiguracao.CONF_MAIL_LOCATION),
				new ClassPathResource(
						ConstantesConfiguracao.CONF_PARAMETROS_LOCATION),
				new ClassPathResource(
						ConstantesConfiguracao.CONF_PERSISTENCE_LOCATION) });
		return ppc;
	}

}