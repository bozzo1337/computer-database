package com.excilys.computerDB.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.excilys.computerDB.model.Company;

public class CompanyMapper extends Mapper<Company>{

private static CompanyMapper singleInstance = null;
	
	private CompanyMapper() {
		
	}
	
	public static CompanyMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new CompanyMapper();
		}
		return singleInstance;
	}
	
	@Override
	public Company map(ResultSet results) {
		Company company = new Company();
		if (results != null) {
			try {
				if (results.next()) {
					company.setId(results.getLong("id"));
					company.setName(results.getString("name"));
				}
			} catch (SQLException e) {
				// TODO
			}
		}
		return company;
	}

	@Override
	public List<Company> mapBatch(ResultSet results) {
		// TODO
		return null;
	}
}
