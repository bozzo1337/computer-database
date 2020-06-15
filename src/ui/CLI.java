package ui;

import java.sql.Connection;
import java.sql.SQLException;

import persistence.DBConnector;

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
		return "kk";
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
