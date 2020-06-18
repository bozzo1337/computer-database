package com.excilys.computerDB.persistence;

import java.util.List;

import com.excilys.computerDB.mapper.Mapper;

public abstract class DAO<T> {
	
	protected static final QueryExecutor QE = QueryExecutor.getInstance();
	protected Mapper<T> mapper = null;
	protected String header = null;
	
	public abstract T findById(Long id);
	public abstract List<T> findBatch(int batchSize, int index);
	public abstract void create(T pojo);
	public abstract void update(T pojo);
	public abstract void delete(T pojo);
	public abstract double getCount();
	public String getHeader() {
		return header;
	}
	
}
