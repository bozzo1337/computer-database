package com.excilys.cdb.connector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class MyDataSource extends HikariDataSource {

	private static String url;
	private static String login;
	private static String password;
	private static String driver;
	private static HikariConfig config = new HikariConfig();
	
	static {
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
	
	public MyDataSource() {
		super(config);
	}
	
}
