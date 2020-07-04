package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

public class DTOCompanyMapper extends Mapper<DTOCompany> {

	private static DTOCompanyMapper singleInstance = null;
	
	private DTOCompanyMapper() {
		
	}
	
	public static DTOCompanyMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new DTOCompanyMapper();
		}
		return singleInstance;
	}
	
	@Override
	public DTOCompany map(Object source) throws NullMappingSourceException, UnknownMappingSourceException {
		DTOCompany companyDTO;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source.getClass() == Company.class) {
			companyDTO = mapFromCompany((Company) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return companyDTO;
	}

	private DTOCompany mapFromCompany(Company company) {
		return new DTOCompany(company.getId().toString(), company.getName());
	}
}
