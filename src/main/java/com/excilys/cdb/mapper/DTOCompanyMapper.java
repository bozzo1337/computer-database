package com.excilys.cdb.mapper;

import com.excilys.cdb.dto.DTOCompany;

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
	public DTOCompany map(Object source) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
