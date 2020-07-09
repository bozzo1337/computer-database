package com.excilys.cdb.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.dao.DAOComputer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Configuration
public class AppConfig {

	@Bean
	public DBConnector dbConnector() {
		return new DBConnector();
	}
	
	@Bean
	public DAOComputer daoComputer(DBConnector dbc) {
		return new DAOComputer(dbc);
	}
	
	@Bean
	public DAOCompany daoCompany(DBConnector dbc) {
		return new DAOCompany(dbc);
	}
	
	@Bean
	public ComputerService computerService(DAOComputer dao) {
		return new ComputerService(dao);
	}
	
	@Bean
	public CompanyService companyService(DAOCompany dao) {
		return new CompanyService(dao);
	}
}
