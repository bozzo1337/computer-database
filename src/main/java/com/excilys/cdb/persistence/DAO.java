package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.excilys.cdb.mapper.Mapper;

public abstract class DAO<T> {
	
	protected static final DBConnector DBC = DBConnector.getInstance();
	protected Mapper<T> mapper = null;
	
	public abstract T findById(Long id);
	public abstract List<T> findBatch(int batchSize, int index);
	public abstract void create(T pojo);
	public abstract void update(T pojo);
	public abstract void delete(T pojo);
	public abstract double getCount();
	public void doRollBack() {
		try (Connection conn = DBC.getConn()){
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
