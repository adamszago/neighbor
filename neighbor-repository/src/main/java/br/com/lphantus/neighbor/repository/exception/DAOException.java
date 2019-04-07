package br.com.lphantus.neighbor.repository.exception;

public class DAOException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DAOException(String s) {
		super(s);
	}

	public DAOException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}

}
