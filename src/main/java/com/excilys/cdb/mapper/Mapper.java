package com.excilys.cdb.mapper;

public abstract class Mapper<T> {
	
	public abstract T map(Object source) throws Exception;
	
}
