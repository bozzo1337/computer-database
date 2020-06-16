package com.excilys.computerDB.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import com.excilys.computerDB.model.RequestResult;

public class QueryExecutor {
	
	private Connection conn;
	private RequestResult rr;
	private String query;

	public QueryExecutor() {
		conn = null;
		rr = new RequestResult();
		query = "";
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
	
	private ResultSet executeQueryCLI(String query) throws SQLException {
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
	
	public RequestResult displayComputers() {
		rr.reset();
		query = "SELECT * FROM computer;";
		try {
			ResultSet results = executeQueryCLI(query);
			rr.appendResult("ID | Name | Date intro | Date disc | Comp ID");
			while (results.next()) {
				rr.appendResult(results.getLong("id") + " | " + results.getString("name")
					+ " | " + results.getDate("introduced") + " | " + results.getDate("discontinued")
					+ " | " + results.getLong("company_id") + "%n");
			}
			rr.setStatus(0);
		} catch (SQLException e) {
			rr.setStatus(1);
			e.printStackTrace();
		}
		
		return rr;
	}
	
	public RequestResult displayCompanies() {
		rr.reset();
		query = "SELECT * FROM company;";		
		try {
			ResultSet results = executeQueryCLI(query);
			rr.appendResult("ID | Name");
			while (results.next()) {
				rr.appendResult(results.getLong("id") + " | " + results.getString("name") + "%n");
			}
			rr.setStatus(0);
		} catch (SQLException e) {
			rr.setStatus(1);
			e.printStackTrace();
		}
		
		return rr;
	}
	
	public RequestResult findComputer(Long id) {
		rr.reset();
		query = "SELECT * FROM computer WHERE id=" + id + ";";		
		try {
			ResultSet results = executeQueryCLI(query);
			if (results.next()) {
				rr.appendResult("id | name | introduced | discontinued | company_id%n");
				rr.appendResult(results.getLong("id") + " | " + results.getString("name")
				+ " | " + results.getDate("introduced") + " | " + results.getDate("discontinued")
				+ " | " + results.getLong("company_id"));
			}
			rr.setStatus(0);
		} catch (SQLException e) {
			rr.setStatus(1);
			e.printStackTrace();
		}
		return rr;
	}
	
	public RequestResult createComputer(String name, Date intro, Date disc, Long compId) {
		rr.reset();
		query = "INSERT INTO computer(name, introduced, discontinued, company_id)";
		query += " VALUES(?, ?, ?, ?);";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setDate(2, intro);
			ps.setDate(3, disc);
			if (compId != null) {
				ps.setLong(4, compId);
			} else {
				ps.setNull(4, 7);
			}
			ps.executeUpdate();
			conn.commit();
			rr.setStatus(0);
			rr.setResult("Création réussie.%n");
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				rr.setStatus(2);
			}
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				e1.printStackTrace();
			}
		}
		return rr;
	}
	
	public RequestResult updateComputer(Long id, String newName) {
		rr.reset();
		query = "UPDATE computer SET name=? WHERE id=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);	
			ps.setString(1, newName);
			ps.setLong(2, id);
			ps.executeUpdate();
			conn.commit();
			rr.setStatus(0);
			rr.setResult("Mise à jour réussie.%n");
		} catch (SQLException e) {
			rr.setStatus(1);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return rr;
	}
	
	public int updateComputer(Long id, String dateToUpdate, Date value) {
		return 1;
	}
	
	public RequestResult updateComputer(Long id, Long newCompany) {
		rr.reset();
		query = "UPDATE computer SET company_id=? WHERE id=?;";
		try {
			PreparedStatement ps = conn.prepareStatement(query);	
			ps.setLong(1, newCompany);
			ps.setLong(2, id);
			ps.executeUpdate();
			conn.commit();
			rr.setStatus(0);
			rr.setResult("Mise à jour réussie.%n");
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException) {
				rr.setStatus(2);
			}
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				e1.printStackTrace();
			}
		}
		return rr;
	}
	
	public int deleteComputer(String arg1) {
		return 1;
	}
	
}
