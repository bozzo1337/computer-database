package persistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CLI {
	
	Connection conn = null;

	public CLI() {	
	}
	
	public int initConn(String login, String password) {
		try {
			conn = DBConnector.getInstance().getConn(login, password);
			return 0;
		} catch (SQLException e) {
			return 1;
		}
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
		String query = "SELECT * FROM computer;";
		ResultSet results;
		Statement stmt;
		String resultsStr = "";
		
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur lors de la création de la requête !";
		}
		try {
			results = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur lors de l'exécution de la requête !";
		}
		
		try {
			Long id;
			String name;
			Date introduced;
			Date discontinued;
			Long comp_id;
			resultsStr = "ID | Name | Date intro | Date disc | Comp ID%n";
			while (results.next()) {
				id = results.getLong("id");
				name = results.getString("name");
				introduced = results.getDate("introduced");
				discontinued = results.getDate("discontinued");
				comp_id = results.getLong("company_id");
				resultsStr += id + " | " + name + " | " + introduced + " | " + discontinued + " | " + comp_id + "%n";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "Erreur de parcours du ResultSet !";
		}
		
		return resultsStr;
	}
	
	public String displayCompanies() {
		return "ll";
	}
	
	public String findComputer(String arg1) {
		return "cc";
	}
	
	public int createComputer(String arg1, String arg2, String arg3) {
		return 1;
	}
	
	public int updateComputer(String arg1, String arg2, String arg3) {
		return 1;
	}
	
	public int deleteComputer(String arg1) {
		return 1;
	}
	
}
