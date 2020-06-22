package com.excilys.computerDB.mapper;

import java.sql.ResultSet;
import java.util.List;

public abstract class Mapper<T> {

	public abstract T map(ResultSet results);
	public abstract List<T> mapBatch(ResultSet results);
	
}
