package com.excilys.cdb.exception;

public class PersistenceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersistenceException(String message, Throwable e) {
		super(message, e);
	}
	
	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException() {	}
}
