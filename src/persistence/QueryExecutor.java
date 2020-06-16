package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
	
	Connection conn = null;

	public QueryExecutor() {	
	}
	
	public int initConn(String login, String password) {
		try {
			conn = DBConnector.getInstance().getConn(login, password);
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
	
	public String displayComputers() {
		String resultsStr = "";
		String query = "SELECT * FROM computer;";
		try {
			ResultSet results = executeQueryCLI(query);
			resultsStr = "ID | Name | Date intro | Date disc | Comp ID";
			while (results.next()) {
				resultsStr += results.getLong("id") + " | " + results.getString("name")
					+ " | " + results.getDate("introduced") + " | " + results.getDate("discontinued")
					+ " | " + results.getLong("company_id") + "%n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur lors de la requête !";
		}
		
		return resultsStr;
	}
	
	public String displayCompanies() {
		String query = "SELECT * FROM company;";
		String resultsStr = "";
		
		try {
			ResultSet results = executeQueryCLI(query);
			resultsStr = "ID | Name";
			while (results.next()) {
				resultsStr += results.getLong("id") + " | " + results.getString("name") + "%n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur lors de la requête !";
		}
		
		return resultsStr;
	}
	
	public String findComputer(Long id) {
		String query = "SELECT * FROM computer WHERE id=" + id + ";";
		String resultsStr = "Aucun résultat.";
		
		try {
			ResultSet results = executeQueryCLI(query);
			if (results.next()) {
				resultsStr = "ID | Name | Date intro | Date disc | Comp ID%n";
				resultsStr += results.getLong("id") + " | " + results.getString("name")
				+ " | " + results.getDate("introduced") + " | " + results.getDate("discontinued")
				+ " | " + results.getLong("company_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur lors de la requête !%n>";
		}
		return resultsStr + "%n>";
	}
	
	public int createComputer(String name, Date intro, Date disc, Long compId) {
		String query = "INSERT INTO computer('name', 'introduced', 'discontinued', 'company_id')";
		query += " VALUES('?', '?', '?', '?');";
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, name);
			ps.setDate(2, intro);
			ps.setDate(3, disc);
			ps.setLong(4, compId);
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		return 1;
	}
	
	public int updateComputer(String arg1, String arg2, String arg3) {
		return 1;
	}
	
	public int deleteComputer(String arg1) {
		return 1;
	}
	
}
