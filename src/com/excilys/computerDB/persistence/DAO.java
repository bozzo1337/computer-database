package com.excilys.computerDB.persistence;

import java.util.List;

import com.excilys.computerDB.mapper.Mapper;

public abstract class DAO<T> {
	
	public static final QueryExecutor QE = QueryExecutor.getInstance();
	public Mapper<T> mapper = null;
	public DAO<T> singleInstance = null;
	public String header = null;
	
	public abstract DAO<T> getInstance();
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
