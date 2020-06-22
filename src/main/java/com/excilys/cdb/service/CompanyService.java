package com.excilys.computerDB.service;

import com.excilys.computerDB.model.Company;
import com.excilys.computerDB.model.Page;
import com.excilys.computerDB.persistence.DAOCompany;

public class CompanyService {
	
	private static CompanyService singleInstance = null;
	private Page<Company> pageComp;
	private DAOCompany dao;
	private String pageHeader = "ID | Name%n";
	
	private CompanyService() {
		dao = DAOCompany.getInstance();
		pageComp = new Page<Company>(pageHeader);
	}
	
	public static CompanyService getInstance() {
		if (singleInstance == null) {
			singleInstance = new CompanyService();
		}
		return singleInstance;
	}
	
	public void resetPages() {
		pageComp.init(dao.getCount());
	}
	
	public Page<Company> selectAll() {
		return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
	}
}
