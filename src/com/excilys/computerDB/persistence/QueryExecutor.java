package com.excilys.computerDB.persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import java.time.LocalDate;

import com.excilys.computerDB.model.RequestResult;

public class QueryExecutor {
	
	private static QueryExecutor singleInstance = null;
	private Connection conn;
	private RequestResult rr;
	private String query;

	public static QueryExecutor getInstance() {
		if (singleInstance == null) {
			singleInstance = new QueryExecutor();
		}
		return singleInstance;
	}
	
	private QueryExecutor() {
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
	
	public RequestResult displayComputers() {
		rr.reset();
		query = "SELECT * FROM computer;";
		try {
			ResultSet results = simpleQuery(query);
			rr.setResult("ID | Name | LocalDate intro | LocalDate disc | Comp ID%n");
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
			ResultSet results = simpleQuery(query);
			rr.setResult("ID | Name%n");
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
			ResultSet results = simpleQuery(query);
			if (results.next()) {
				rr.setStatus(0);
				rr.appendResult("id | name | introduced | discontinued | company_id%n");
				rr.appendResult(results.getLong("id") + " | " + results.getString("name")
				+ " | " + results.getDate("introduced") + " | " + results.getDate("discontinued")
				+ " | " + results.getLong("company_id") + "%n");
			} else {
				rr.setStatus(1);
				rr.setResult("Aucun résultat.%n");
			}
		} catch (SQLException e) {
			rr.setStatus(1);
			e.printStackTrace();
		}
		return rr;
	}
	
	public ResultSet findComputerToMap(Long id) {
		query = "SELECT * FROM computer WHERE id=" + id + ";";
		ResultSet results = null;
		try {
			results = simpleQuery(query);
		} catch (SQLException e) {
			System.err.format("Erreur requête mapping.%n");
		}
		return results;
	}
	
	public RequestResult createComputer(String name, LocalDate intro, LocalDate disc, Long compId) {
		rr.reset();
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
			rr.setStatus(0);
			rr.setResult("Création réussie.%n");
		} catch (SQLException e) {
			rr.setResult("Echec de la création.%n");
			if (e instanceof SQLIntegrityConstraintViolationException) {
				rr.setStatus(2);
				rr.setResult("ID de l'entreprise invalide.%n");
			}
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				rr.appendResult("Echec du rollback.%n");
			}
		}
		return rr;
	}
	
	public RequestResult updateComputer(Long id, String name, LocalDate intro, LocalDate disc, Long compId) {
		rr.reset();
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
			rr.setStatus(0);
			rr.setResult("Mise à jour réussie.%n");
		} catch (SQLException e) {
			rr.setResult("Echec de la mise à jour.%n");
			if (e instanceof SQLIntegrityConstraintViolationException) {
				rr.setStatus(2);
				rr.setResult("ID d'entreprise invalide.%n");
			}
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				rr.appendResult("Echec du rollback.%n");
			}
		}
		return rr;
	}
	
	public RequestResult deleteComputer(Long id) {
		rr.reset();
		query = "DELETE FROM computer WHERE id=" + id + ";";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
			conn.commit();
			rr.setStatus(0);
			rr.setResult("Suppression validée.%n");
		} catch (SQLException e) {
			rr.setResult("Echec de la suppression.%n");
			rr.setStatus(1);
			try {
				conn.rollback();
			} catch (SQLException e1) {
				rr.setStatus(3);
				rr.appendResult("Echec du rollback.%n");
			}
		}
		return rr;
	}
	
	public ResultSet retrieveComputers(int batchSize, int idxNum) {
		query = "SELECT * FROM computer LIMIT " + batchSize + ", " + idxNum * batchSize +";";
		ResultSet results = null;
		try {
			Statement stmt = conn.prepareCall(query);
			results = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public ResultSet computerCount() {
		query = "SELECT COUNT(*) AS compcount FROM computer;";
		ResultSet results = null;
		try {
			Statement stmt = conn.prepareCall(query);
			results = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
}
