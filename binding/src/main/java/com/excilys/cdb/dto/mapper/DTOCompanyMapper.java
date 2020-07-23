package com.excilys.cdb.dto.mapper;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

public class DTOCompanyMapper {

	private DTOCompanyMapper() {
	}
	
	public static DTOCompany map(Object source) throws NullMappingSourceException, UnknownMappingSourceException {
		DTOCompany companyDTO;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source instanceof Company) {
			companyDTO = mapFromCompany((Company) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return companyDTO;
	}

	private static DTOCompany mapFromCompany(Company company) {
		return new DTOCompany(company.getId().toString(), company.getName());
	}
}
