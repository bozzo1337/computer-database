package com.excilys.computerDB.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	
	private static DBConnector singleInstance = null;
	private Connection conn = null;
	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?serverTimezone=UTC";
	
	public static DBConnector getInstance() {
		if (singleInstance == null) {
			singleInstance = new DBConnector();
		}
		return singleInstance;
	}
	
	public Connection getConn(){
		return conn;
	}
	
	public boolean initConn(String login, String password) {
		try {
			conn = DriverManager.getConnection(url, login, password);
			conn.setAutoCommit(false);
			QueryExecutor.setConn(conn);
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
