package com.excilys.cdb.exception.mapping;

public class UnknownMappingSourceException extends MappingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UnknownMappingSourceException() {
		super("Mapping source not recognized");
	}
}
