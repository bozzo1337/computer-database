package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.model.Company;

public class CompanyMapper extends Mapper<Company> {

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
		try {
			if (results != null && !results.isClosed()) {
				return mapOne(results);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Company> mapBatch(ResultSet results) {
		ArrayList<Company> companies = new ArrayList<Company>();
		try {
			while (results != null && !results.isClosed()) {
				companies.add(mapOne(results));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companies;
	}

	private Company mapOne(ResultSet results) throws SQLException {
		Company company = new Company();
		company.setId(results.getLong("company.id"));
		company.setName(results.getString("company.name"));
		return company;
	}
}
