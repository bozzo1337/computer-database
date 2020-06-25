package com.excilys.cdb.service;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.DAOCompany;

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
	
	public Company selectById(Long id) {
		return dao.findById(id);
	}
	
	public Page<Company> selectAll() {
		return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
	}
}
