package com.excilys.computerDB.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
	
	private static DBConnector singleInstance = null;
	private String url = "jdbc:mysql://127.0.0.1:3306/computer-database-db?serverTimezone=UTC";
	
	public static DBConnector getInstance() {
		if (singleInstance == null) {
			singleInstance = new DBConnector();
		}
		return singleInstance;
	}
	
	public Connection getConn(String login, String password) throws SQLException {
		return DriverManager.getConnection(url, login, password);
	}
}
