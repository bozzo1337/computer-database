package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public class CompanyService {
	
	private static CompanyService singleInstance = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);
	private Page<Company> pageComp;
	private DAOCompany dao;
	private String pageHeader = "ID | Name\n";
	
	private CompanyService() {
		dao = DAOCompany.getInstance(DBConnector.getInstance());
		pageComp = new Page<Company>(pageHeader);
	}
	
	public static CompanyService getInstance() {
		if (singleInstance == null) {
			singleInstance = new CompanyService();
			LOGGER.info("CompanyService instancied");
		}
		return singleInstance;
	}
	
	public void resetPages() {
		try {
			pageComp.init(dao.count());
		} catch (PersistenceException e) {
			LOGGER.error("Error during page init", e);
		}
	}
	
	public void nextPage() {
		pageComp.nextPage();
	}
	
	public void previousPage() {
		pageComp.previousPage();
	}
	
	public List<Company> selectAll() {
		List<Company> listCompanies = new ArrayList<Company>();
		try {
			listCompanies = dao.findAll();
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectAll", e);
		}
		return listCompanies;
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
			pageComp.filled(dao.findBatch(pageComp.getEntitiesPerPage(), pageComp.getIdxPage()));
		} catch (PersistenceException e) {
			LOGGER.error("Error during selectPage", e);
		}
		return pageComp;
	}
	
	public void delete(Long id) {
		try {
			dao.delete(id);
		} catch (PersistenceException e) {
			LOGGER.error("Error during delete", e);
		}
	}
}
