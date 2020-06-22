package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;


public class QueryExecutor {
	
	private static QueryExecutor singleInstance = null;
	private static Connection conn;
	private String query;

	public static QueryExecutor getInstance() {
		if (singleInstance == null) {
			singleInstance = new QueryExecutor();
		}
		return singleInstance;
	}
	
	private QueryExecutor() {
	}
	
	public static void setConn(Connection conn) {
		QueryExecutor.conn = conn;
	}
	
	private ResultSet simpleQuery(String query){
		ResultSet results = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(query);
			results = ps.executeQuery();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
		return results;
	}
	
	private void queryWithParams(PreparedStatement ps) {
		try {
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			doRollBack();
		}
	}
	
	private void doRollBack() {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet findComputerById(Long id) {
		query = "SELECT * FROM computer WHERE id=" + id + ";";
		return simpleQuery(query);
	}
	
	public ResultSet findCompanyById(Long id) {
		query = "SELECT * FROM company WHERE id=" + id + ";";
		return simpleQuery(query);
	}
	
	public void createComputer(String name, LocalDate intro, LocalDate disc, Long compId) {
		query = "INSERT INTO computer(name, introduced, discontinued, company_id)";
		query += " VALUES(?, ?, ?, ?);";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			if (intro != null)
				ps.setDate(2, Date.valueOf(intro));
			else
				ps.setNull(2, java.sql.Types.DATE);
			if (disc != null)
				ps.setDate(3, Date.valueOf(disc));
			else
				ps.setNull(3, java.sql.Types.DATE);
			if (compId != null)
				ps.setLong(4, compId);
			else
				ps.setNull(4, java.sql.Types.BIGINT);
			queryWithParams(ps);
		} catch (SQLException e) {
			//TODO
		}
	}
	
	public void createCompany(String name) {
		query = "INSERT INTO company(name) VALUES(" + name + ");";
		simpleQuery(query);
	}
	
	public void updateComputer(Long id, String name, LocalDate intro, LocalDate disc, Long compId) {
		query = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			if (intro != null)
				ps.setDate(2, Date.valueOf(intro));
			else
				ps.setNull(2, java.sql.Types.DATE);
			if (disc != null)
				ps.setDate(3, Date.valueOf(disc));
			else
				ps.setNull(3, java.sql.Types.DATE);
			if (compId != null)
				ps.setLong(4, compId);
			else
				ps.setNull(4, java.sql.Types.BIGINT);
			ps.setLong(5, id);
			queryWithParams(ps);
		} catch (SQLException e) {
			//TODO
		}
	}
	
	public void deleteComputer(Long id) {
		query = "DELETE FROM computer WHERE id=" + id + ";";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			queryWithParams(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public ResultSet retrieveComputers(int batchSize, int index) {
		query = "SELECT * FROM computer LIMIT " + index * batchSize + ", " + batchSize +";";
		return simpleQuery(query);
	}
	
	public ResultSet retrieveCompanies(int batchSize, int index) {
		query = "SELECT * FROM company LIMIT " + index * batchSize + ", " + batchSize +";";
		return simpleQuery(query);
	}
	
	public double count(String table) {
		query = "SELECT COUNT(id) AS count FROM " + table + ";";
		ResultSet results = null;
		double compCount = 0;
		try {
			results = simpleQuery(query);
			if (results.next()) {
				compCount = results.getDouble("count");
			}
		} catch (SQLException e) {
			//TODO
		}
		return compCount;
	}
}
