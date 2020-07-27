package com.excilys.cdb.exception.validation;

public abstract class ValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public ValidationException(String msg) {
		super(msg);
	}
}
