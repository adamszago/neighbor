package br.com.lphantus.neighbor.controller.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.lphantus.neighbor.config.ApplicationConfig;
import br.com.lphantus.neighbor.config.AsyncConfig;
import br.com.lphantus.neighbor.config.BatchConfig;
import br.com.lphantus.neighbor.config.JpaConfig;
import br.com.lphantus.neighbor.config.SecurityConfig;
import br.com.lphantus.neighbor.config.WebFlowConfig;
import br.com.lphantus.neighbor.config.WebMvcConfig;
import br.com.lphantus.neighbor.entity.Totem;
import br.com.lphantus.neighbor.service.ITotemService;
import br.com.lphantus.neighbor.service.exception.ServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfig.class, BatchConfig.class,
		AsyncConfig.class, ApplicationConfig.class, SecurityConfig.class,
		WebFlowConfig.class, WebMvcConfig.class })
public class DataIntegrationTest {

	@Autowired
	private ITotemService totemService;

	@Test
	public void testSaveParticipatesInATransaction() {
		
		SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
		DriverManagerDataSource dataSource = new DriverManagerDataSource("", "neighbor", "neighbor");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		builder.bind("java:jdbc/lpneighbor_ds", dataSource);
		try {
		    builder.activate();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		try {
			Totem totem = totemService.findById(3L);
			totem.setSenha("0513");
			totemService.saveOrUpdate(totem);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
