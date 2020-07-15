package com.excilys.cdb.exception.mapping;

public class MappingResultSetException extends MappingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MappingResultSetException() {
		super("Error during ResultSet manipulation in Mapper");
	}

}
