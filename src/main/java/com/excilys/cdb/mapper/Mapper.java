package com.excilys.cdb.mapper;

import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;

public abstract class Mapper<T> {
	
	public abstract T map(Object source) throws NullMappingSourceException, UnknownMappingSourceException;
	
}
