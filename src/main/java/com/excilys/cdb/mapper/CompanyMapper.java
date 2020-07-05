package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
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
	public Company map(Object source) throws NullMappingSourceException, UnknownMappingSourceException {
		Company company;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source instanceof ResultSet) {
			company = mapFromResultSet((ResultSet) source);
		} else if (source instanceof DTOCompany) {
			company = mapFromDTO((DTOCompany) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return company;
	}

	private Company mapFromResultSet(ResultSet results) {
		Company company = null;
		try {
			company = new Company(results.getLong("company.id"), results.getString("company.name"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return company;
	}

	private Company mapFromDTO(DTOCompany companyDTO) {
		return new Company(Long.valueOf(companyDTO.getId()), companyDTO.getName());
	}
}
