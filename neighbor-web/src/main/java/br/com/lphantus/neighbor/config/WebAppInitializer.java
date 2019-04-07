package br.com.lphantus.neighbor.config;

import javax.faces.webapp.FacesServlet;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.primefaces.webapp.filter.FileUploadFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.lphantus.neighbor.utils.Constantes.ConstantesConfiguracao;
import br.com.lphantus.neighbor.utils.Constantes.ConstantesView;

import com.sun.faces.config.ConfigureListener;

/**
 * Classe responsável por inicializar o aplicativo web.
 * 
 * @author Elias da Silva Policena { elias.policena@lphantus.com.br }
 * @since 14/10/2014
 *
 */
@Order(1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected String[] getServletMappings() {
		return new String[] { ConstantesConfiguracao.SERVLET_MAPPING };
		// return new String[] {};
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] {};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] {};
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		servletContext.log("Inicializando aplicacao ....... ");

		// este parametro nao esta funcionando muito bem quando adicionado
		// juntamente com os demais
		servletContext.setInitParameter("javax.faces.FACELETS_SKIP_COMMENTS", "true");

		// Create the root appcontext
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();

		// registra as configuracoes da aplicacao
		rootContext.register(ApplicationConfig.class);

		// Manage the lifecycle of the root appcontext
		servletContext.setInitParameter(ConstantesConfiguracao.HTML_ESCAPE_KEY, "true");
		servletContext.setInitParameter(ConstantesConfiguracao.SPRING_PROFILE_KEY, ConstantesConfiguracao.SPRING_PROFILE_VALUE);

		// tags <context-param>
		// servletContext.setInitParameter("com.sun.faces.serializationProvider",
		// "org.jboss.web.jsf.integration.serialization.JBossSerializationProvider");
		servletContext.setInitParameter("com.sun.faces.responseBufferSize", "500000");
		servletContext.setInitParameter("com.sun.faces.numberOfViewsInSession", "3");
		servletContext.setInitParameter("com.sun.faces.numberOfLogicalViews", "10");
		servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "-1");

		servletContext.setInitParameter("javax.faces.FACELETS_LIBRARIES", "/WEB-INF/springsecurity.taglib.xml");
		
		// servletContext.setInitParameter("primefaces.UPLOADER", "auto");
		
		// ---------------------------------------------------------
		// TODO: encontrar uma maneira de determinar se is_demo aqui
		servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Production");

		servletContext.setInitParameter("javax.faces.STATE_SAVING_METHOD", "server");
		// ---------------------------------------------------------

		servletContext.setInitParameter("javax.servlet.jsp.jstl.fmt.localizationContext", "resources.application");
		servletContext.setInitParameter("primefaces.THEME", "sam");

		// registro de listeners da aplicacao
		servletContext.addListener(new ConfigureListener());
		servletContext.addListener(new RequestContextListener());
		servletContext.addListener(new ContextLoaderListener(rootContext));

		// registro do jsf
		Dynamic facesServlet = servletContext.addServlet(ConstantesView.FACES_SERVLET, new FacesServlet());
		facesServlet.addMapping("*.jsf");
		facesServlet.setLoadOnStartup(1);

		// registro de filtro de upload do primefaces
		javax.servlet.FilterRegistration.Dynamic primefacesFilter = servletContext.addFilter("PrimeFaces FileUpload Filter", new FileUploadFilter());
		primefacesFilter.addMappingForUrlPatterns(null, true, "*.jsf");
		primefacesFilter.addMappingForServletNames(null, true, ConstantesView.FACES_SERVLET);

		// configuracao de encode do sistema
		// final CharacterEncodingFilter characterEncodingFilter = new
		// CharacterEncodingFilter();
		// characterEncodingFilter
		// .setEncoding(ConstantesConfiguracao.ENCODE_SISTEMA);
		// characterEncodingFilter.setForceEncoding(true);
		// javax.servlet.FilterRegistration.Dynamic
		// characterEncodingFilterRegister = servletContext
		// .addFilter("Character Encoding Filter", characterEncodingFilter);
		// characterEncodingFilterRegister.addMappingForUrlPatterns(null, true,
		// "/*");

		// now the config for the Dispatcher servlet
		// AnnotationConfigWebApplicationContext mvcContext = new
		// AnnotationConfigWebApplicationContext();
		// mvcContext.register(WebMvcConfig.class);

		// se descomentar o trecho abaixo, carrega dois contextos spring
		// The main Spring MVC servlet.
		// Dynamic appServlet = servletContext.addServlet("appServlet",
		// new DispatcherServlet(mvcContext));
		// appServlet.setLoadOnStartup(1);
	}

	@Override
	protected Filter[] getServletFilters() {

		return new Filter[] {};
	}

}