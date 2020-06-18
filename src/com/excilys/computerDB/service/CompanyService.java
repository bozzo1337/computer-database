package com.excilys.computerDB.service;

import java.util.List;

import com.excilys.computerDB.model.Company;
import com.excilys.computerDB.model.Page;
import com.excilys.computerDB.persistence.DAOCompany;

public class CompanyService {
	
	private static CompanyService singleInstance = null;
	private Page<Company> pageComp;
	private DAOCompany dao;
	
	private CompanyService() {
		dao = DAOCompany.getInstance();
		pageComp = new Page<Company>();
	}
	
	public static CompanyService getInstance() {
		if (singleInstance == null) {
			singleInstance = new CompanyService();
		}
		return singleInstance;
	}
	
	public List<Company> selectAll() {
		return dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage());
	}
}
