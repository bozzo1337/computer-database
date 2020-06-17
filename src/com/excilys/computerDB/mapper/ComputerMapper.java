package com.excilys.computerDB.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.model.RequestResult;
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
					Date intro = results.getDate("introduced");
					if (intro != null)
						computer.setIntroduced(intro.toLocalDate());
					Date disc = results.getDate("discontinued");
					if (disc != null)
						computer.setDiscontinued(disc.toLocalDate());
					Long companyId = results.getLong("company_id");
					if (companyId == 0)
						computer.setCompanyId(null);
					else
						computer.setCompanyId(companyId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return computer;
	}
	
	public RequestResult updateComputer(Computer comp) {
		return qe.updateComputer(comp.getId(), comp.getName(), comp.getIntroduced(),
				comp.getDiscontinued(), comp.getCompanyId());
	}
}
