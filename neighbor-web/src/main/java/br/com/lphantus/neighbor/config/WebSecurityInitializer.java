package br.com.lphantus.neighbor.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Inicializa o spring com o configurador especifico.
 * 
 * @author elias.policena@lphantus.com.br
 * @since 16/11/2014
 *
 */
@Order(2)
public class WebSecurityInitializer extends
		AbstractSecurityWebApplicationInitializer {

	/**
	 * Construtor de classe.
	 */
	public WebSecurityInitializer() {
	}

}
