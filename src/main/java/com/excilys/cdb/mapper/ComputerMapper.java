package com.excilys.computerDB.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.computerDB.model.Computer;

public class ComputerMapper extends Mapper<Computer> {
	
	private static ComputerMapper singleInstance = null;
	
	private ComputerMapper() {
		
	}
	
	public static ComputerMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new ComputerMapper();
		}
		return singleInstance;
	}
	
	@Override
	public Computer map(ResultSet results) {
		try {
			if (results != null && results.next()) {
				return mapOne(results);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Computer> mapBatch(ResultSet results) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try {
			while (results != null && results.next()) {
				computers.add(mapOne(results));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
	private Computer mapOne(ResultSet results) throws SQLException {
		Computer computer = new Computer();
		computer.setId(results.getLong("id"));
		computer.setName(results.getString("name"));
		Date intro = results.getDate("introduced");
		if (intro != null) {
			computer.setIntroduced(intro.toLocalDate());
		}
		Date disc = results.getDate("discontinued");
		if (disc != null) {
			computer.setDiscontinued(disc.toLocalDate());
		}
		Long companyId = results.getLong("company_id");
		if (companyId == 0) {
			computer.setCompanyId(null);
		} else {
			computer.setCompanyId(companyId);
		}
		return computer;
	}
}
