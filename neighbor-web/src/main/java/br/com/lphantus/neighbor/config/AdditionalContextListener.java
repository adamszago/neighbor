package br.com.lphantus.neighbor.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AdditionalContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // destroy those contexts maybe
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("Executando loader listener ....... ");
    	
        ApplicationContext context = new ClassPathXmlApplicationContext();
        sce.getServletContext().setAttribute("/", context);
        
        try {
        	WebAppInitializer initializer = new WebAppInitializer();
			initializer.onStartup(sce.getServletContext());
		} catch (ServletException e) {
			e.printStackTrace();
		}
        
        System.out.println("Loader listener finalizado!");
    }

}
