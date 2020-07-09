package com.excilys.cdb.connector;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class DBConnector {

	private static Logger LOGGER = LoggerFactory.getLogger(DBConnector.class);
	private Connection conn = null;
	private String url;
	private String login;
	private String password;
	private String driver;
	private HikariConfig config = new HikariConfig();
	private HikariDataSource dataSource;

	public DBConnector() {
		InputStream inputStream = null;
		try {
			inputStream = DBConnector.class.getResourceAsStream("/config.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			url = properties.getProperty("url");
			login = properties.getProperty("login");
			password = properties.getProperty("password");
			driver = properties.getProperty("driver");
			config.setUsername(login);
			config.setJdbcUrl(url);
			config.setPassword(password);
			config.setDriverClassName(driver);
			dataSource = new HikariDataSource(config);
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
		LOGGER.info("DBConnector instantiated");
	}

	public Connection getConn() throws SQLException {
		try {
			if (conn == null || conn.isClosed()) {
				Class.forName(driver);
				conn = dataSource.getConnection();
				conn.setAutoCommit(false);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
}
