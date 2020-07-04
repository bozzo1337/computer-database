package com.excilys.cdb.exception;

public class UnknownMappingSourceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnknownMappingSourceException() {
		super("Mapping source not recognized");
	}
}
