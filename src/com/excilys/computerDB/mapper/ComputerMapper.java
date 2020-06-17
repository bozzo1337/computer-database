package com.excilys.computerDB.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.persistence.QueryExecutor;

public class ComputerMapper {
	
	public static ComputerMapper singleInstance = null;
	private QueryExecutor qe;
	
	public static ComputerMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new ComputerMapper();
		}
		return singleInstance;
	}
	
	private ComputerMapper() {
		qe = QueryExecutor.getInstance();
	}
	
	public Computer mapComputer(Long id) {
		Computer computer = new Computer();
		ResultSet results = qe.findComputerToMap(id);
		if (results != null) {
			try {
				if (results.next()) {
					computer.setId(results.getLong("id"));
					computer.setName(results.getString("name"));
					computer.setIntroduced(results.getDate("introduced"));
					computer.setDiscontinued(results.getDate("discontinued"));
					computer.setCompanyId(results.getLong("company_id"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return computer;
	}
}
