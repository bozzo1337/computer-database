package com.excilys.computerDB.service;

import java.io.Console;

import com.excilys.computerDB.persistence.QueryExecutor;

public class CompanyService {
	
	private Console console;
	private QueryExecutor qe = QueryExecutor.getInstance();
	
	public CompanyService(Console console) {
		this.console = console;
	}
	
	public void selectAll() {
		console.printf(qe.displayCompanies() + ">");
	}
}
