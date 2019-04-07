package br.com.lphantus.neighbor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.faces.mvc.JsfView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import br.com.lphantus.neighbor.utils.Constantes.ConstantesView;

/**
 * Habilita o spring MVC, configurando os itens necessários
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	/**
	 * Constrói o bean de renderização da view.
	 * 
	 * @return Um bean spring
	 */
	@Bean
	public UrlBasedViewResolver faceletsViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix(ConstantesView.VIEWS);
		resolver.setSuffix(ConstantesView.SUFIXO_PAGINAS);
		resolver.setViewClass(JsfView.class);
		return resolver;
	}

}
