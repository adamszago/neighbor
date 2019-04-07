package br.com.lphantus.neighbor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Habilita configuração de scheduling
 * 
 * @author elias.policena@lphantus.com.br
 * @since 08/11/2014
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

}
