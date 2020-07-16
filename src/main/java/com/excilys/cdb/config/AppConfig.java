package com.excilys.cdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.excilys.cdb.connector.MyDataSource;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.dao", "com.excilys.cdb.service", "com.excilys.cdb.connector",
		"com.excilys.cdb.config", "com.excilys.cdb.controller", "com.excilys.cdb.controller.attributes" })
public class AppConfig {

	@Autowired
	MyDataSource dataSource;

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public PlatformTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
