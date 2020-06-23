package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	
	private static DBConnector singleInstance = null;
	private Connection conn = null;
	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?serverTimezone=UTC";
	private String login;
	private String password;
	
	public static DBConnector getInstance() {
		if (singleInstance == null) {
			singleInstance = new DBConnector();
		}
		return singleInstance;
	}
	
	public Connection getConn(){
		try {
			if (conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection(url, login, password);
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		return conn;
	}
	
	public boolean initConn(String login, String password) {
		try {
			this.login = login;
			this.password = password;
			conn = DriverManager.getConnection(url, login, password);
			conn.setAutoCommit(false);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public boolean closeConn() {
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}
}
