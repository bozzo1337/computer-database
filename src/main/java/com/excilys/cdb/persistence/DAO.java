package com.excilys.cdb.persistence;

import java.util.List;

import com.excilys.cdb.mapper.Mapper;

public abstract class DAO<T> {
	
	protected static final QueryExecutor QE = QueryExecutor.getInstance();
	protected Mapper<T> mapper = null;
	
	public abstract T findById(Long id);
	public abstract List<T> findBatch(int batchSize, int index);
	public abstract void create(T pojo);
	public abstract void update(T pojo);
	public abstract void delete(T pojo);
	public abstract double getCount();
	
}
