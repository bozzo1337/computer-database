package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.exception.PersistenceException;
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
		try {
			pageCompany.init(dao.count());
		} catch (PersistenceException e) {
			LOGGER.error("Error during page init", e);
		}
	}
	
	public void nextPage() {
		pageCompany.nextPage();
	}
	
	public void previousPage() {
		pageCompany.previousPage();
	}
	
	public Page<Company> selectAll() {
		try {
			dao.findAll(pageCompany);
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll", e);
		}
		return pageCompany;
	}
	
	public Company selectById(Long id) {
		Company company = new Company();
		try {
			company = dao.findById(id);
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectById", e);
		}
		return company;
	}
	
	public Page<Company> selectPage() {
		try {
			dao.findBatch(pageCompany);
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectPage", e);
		}
		return pageCompany;
	}
	
	public void delete(Long id) {
		try {
			dao.delete(id);
		} catch (PersistenceException e) {
			LOGGER.error("Error during delete", e);
		}
	}
}
