package br.com.lphantus.neighbor.exception;

import javax.faces.context.ExceptionHandlerFactory;

public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	public CustomExceptionHandlerFactory(final javax.faces.context.ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public CustomExceptionHandler getExceptionHandler() {
		return new CustomExceptionHandler(this.parent.getExceptionHandler());
	}

}
