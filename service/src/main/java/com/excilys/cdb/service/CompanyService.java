package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Service
public class CompanyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);
	private Page<Company> pageCompany;
	private DAOCompany dao;
	private String pageHeader = "ID | Name\n";
	
	@Autowired
	public CompanyService(DAOCompany dao) {
		this.dao = dao;
		pageCompany = new Page<Company>(pageHeader);
		LOGGER.info("CompanyService instantiated");
	}
	
	public void resetPages() {
		pageCompany.init(dao.count());
	}
	
	public void nextPage() {
		pageCompany.nextPage();
	}
	
	public void previousPage() {
		pageCompany.previousPage();
	}
	
	public Page<Company> selectAll() {
		dao.findAll(pageCompany);
		return pageCompany;
	}
	
	public Company selectById(Long id) {
		Company company = new Company();
		company = dao.findById(id);
		return company;
	}
	
	public Page<Company> selectPage() {
		dao.findBatch(pageCompany);
		return pageCompany;
	}
	
	public void delete(Long id) {
		dao.delete(id);
	}
}
