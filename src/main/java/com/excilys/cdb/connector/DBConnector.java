package com.excilys.cdb.connector;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

@Component
public class DBConnector {

	private static Logger LOGGER = LoggerFactory.getLogger(DBConnector.class);
	private Connection conn = null;
	
	private HikariDataSource dataSource;

	@Autowired
	public DBConnector(MyDataSource dataSource) {
		this.dataSource = dataSource;
		LOGGER.info("DBConnector instantiated");
	}

	public Connection getConn() throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
		}
		return conn;
	}
}
