package com.excilys.cdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.excilys.cdb.connector.MyDataSource;

@Configuration
@ComponentScan(basePackages = "com.excilys.cdb.connector")
public class HibernateConfig {

	@Autowired
	MyDataSource dataSource;
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.excilys.cdb.model");
		return sessionFactory;
	}
}
