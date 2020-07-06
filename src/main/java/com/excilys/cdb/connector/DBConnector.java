package com.excilys.cdb.connector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnector {
	
	private static DBConnector singleInstance = null;
	private Connection conn = null;
	private String url;
	private String login;
	private String password;
	private HikariConfig config = new HikariConfig();
	private HikariDataSource dataSource;
	
	private DBConnector() {
		InputStream inputStream = null;
		try {
			inputStream = DBConnector.class.getResourceAsStream("/config.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			url = properties.getProperty("url");
			login = properties.getProperty("login");
			password = properties.getProperty("password");
			config.setUsername(login);
			config.setJdbcUrl(url);
			config.setPassword(password);
			config.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource = new HikariDataSource(config);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static DBConnector getInstance() {
		if (singleInstance == null) {
			singleInstance = new DBConnector();
		}
		return singleInstance;
	}
	
	public Connection getConn(){
		try {
			if (conn == null || conn.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage().toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
