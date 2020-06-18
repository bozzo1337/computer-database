package com.excilys.computerDB.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.time.LocalDate;


public class QueryExecutor {
	
	private static QueryExecutor singleInstance = null;
	private Connection conn;
	private String query;

	public static QueryExecutor getInstance() {
		if (singleInstance == null) {
			singleInstance = new QueryExecutor();
		}
		return singleInstance;
	}
	
	private QueryExecutor() {
	}
	
	public int initConn(String login, String password) {
		try {
			conn = DBConnector.getInstance().getConn(login, password);
			conn.setAutoCommit(false);
			return 0;
		} catch (SQLException e) {
			return 1;
		}
	}
	
	private ResultSet simpleQuery(String query) throws SQLException {
		ResultSet results;
		Statement stmt;
		stmt = conn.createStatement();
		results = stmt.executeQuery(query);
		return results;
	}
	
	public int closeConn() {
		try {
			conn.close();
			return 0;
		} catch (SQLException e) {
			return 1;
		}
	}
	
	public ResultSet findComputerById(Long id) {
		query = "SELECT * FROM computer WHERE id=" + id + ";";
		ResultSet results = null;
		try {
			results = simpleQuery(query);
		} catch (SQLException e) {
			//TODO
		}
		return results;
	}
	
	public ResultSet findCompanyById(Long id) {
		query = "SELECT * FROM company WHERE id=" + id + ";";
		ResultSet results = null;
		try {
			results = simpleQuery(query);
		} catch (SQLException e) {
			//TODO
		}
		return results;
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
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//TODO
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//TODO
			}
		}
	}
	
	public void createCompany(String name) {
		query = "INSERT INTO company(name) VALUES(" + name + ");";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			conn.commit();
		} catch (SQLException e) {
			//TODO
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//TODO
			}
		}
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
			ps.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			//TODO
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//TODO
			}
		}
	}
	
	public void deleteComputer(Long id) {
		query = "DELETE FROM computer WHERE id=" + id + ";";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			conn.commit();
		} catch (SQLException e) {
			//TODO
			try {
				conn.rollback();
			} catch (SQLException e1) {
				//TODO
			}
		}
	}
	
	public ResultSet retrieveComputers(int batchSize, int index) {
		query = "SELECT * FROM computer LIMIT " + index * batchSize + ", " + batchSize +";";
		ResultSet results = null;
		try {
			Statement stmt = conn.prepareCall(query);
			results = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public double computerCount() {
		query = "SELECT COUNT(id) AS compcount FROM computer;";
		ResultSet results = null;
		double compCount = 0;
		try {
			Statement stmt = conn.prepareCall(query);
			results = stmt.executeQuery(query);
			if (results.next()) {
				compCount = results.getDouble("compcount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return compCount;
	}
}
