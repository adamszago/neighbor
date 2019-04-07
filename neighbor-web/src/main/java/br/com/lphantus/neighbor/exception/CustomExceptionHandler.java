package br.com.lphantus.neighbor.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.lphantus.neighbor.service.IMailManager;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	
	@Autowired
	private IMailManager mailManager;

	private javax.faces.context.ExceptionHandler wrapped;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public CustomExceptionHandler(){
		
	}
	
	public CustomExceptionHandler(javax.faces.context.ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public javax.faces.context.ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	@Override
	public void handle() throws FacesException {
		for (final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();) {
			ExceptionQueuedEvent proximo = it.next();
			Throwable t = proximo.getContext().getException();
			while ((t instanceof FacesException || t instanceof ELException) && t.getCause() != null) {
				t = t.getCause();
			}
			
			ApplicationContext ctx = SpringContext.getApplicationContext();
			if ( null != ctx ){
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				if ( null == t.getLocalizedMessage() ){
					pw.write(String.format("%s - ", t.getMessage()));
				}else{
					pw.write(String.format("%s - ", t.getLocalizedMessage()));
				}
				t.printStackTrace(pw);
				String stackTrace = sw.toString();
				
				ctx.getBean(IMailManager.class).enviarEmailErroOperacional(stackTrace);
			}
			
			if (t instanceof FileNotFoundException || t instanceof ViewExpiredException) {
				final FacesContext facesContext = FacesContext.getCurrentInstance();
				final ExternalContext externalContext = facesContext.getExternalContext();
				final Map<String, Object> requestMap = externalContext.getRequestMap();
				try {
					logger.info(String.format("LOG 1 %s: %s", t.getClass().getSimpleName(), t.getMessage()));
					String message;
					if (t instanceof ViewExpiredException) {
						final String viewId = ((ViewExpiredException) t).getViewId();
						message = "View is expired. <a href='/ifos" + viewId + "'>Back</a>";
					} else {
						message = t.getMessage();
						// beware, don't leak internal
						// info!
					}
					requestMap.put("errorMsg", message);
					try {
						externalContext.dispatch("/error.jsp");
					} catch (final IOException e) {
						logger.info(String.format("LOG 2 %s: %s", t.getClass().getSimpleName(), t.getMessage()));
					}
					facesContext.responseComplete();
				} finally {
					it.remove();
				}
			}
		}
		getWrapped().handle();
	}

}
