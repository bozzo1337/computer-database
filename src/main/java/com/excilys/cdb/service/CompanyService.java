package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public class CompanyService {
	
	private static CompanyService singleInstance = null;
	private Page<Company> pageComp;
	private DAOCompany dao;
	private String pageHeader = "ID | Name\n";
	
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
	
	public void nextPage() {
		pageComp.nextPage();
	}
	
	public void previousPage() {
		pageComp.previousPage();
	}
	
	public List<Company> selectAll() {
		return dao.findAll();
	}
	
	public Company selectById(Long id) {
		return dao.findById(id);
	}
	
	public Page<Company> selectPage() {
		return pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
	}
	
	public void delete(Long id) {
		dao.deleteCompany(id);
	}
}
