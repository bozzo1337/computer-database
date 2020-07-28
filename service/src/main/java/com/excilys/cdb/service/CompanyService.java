package com.excilys.cdb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.dao.DAOCompany;
import com.excilys.cdb.dao.mapper.CompanyMapper;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@Service
public class CompanyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyService.class);
	private Page<Company> pageCompany;
	private Page<DTOCompany> pageCompanyDTO;
	private DAOCompany dao;
	private String pageHeader = "ID | Name\n";

	@Autowired
	public CompanyService(DAOCompany dao) {
		this.dao = dao;
		pageCompany = new Page<Company>(pageHeader);
		pageCompanyDTO = new Page<DTOCompany>(pageHeader);
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

	public Page<DTOCompany> selectAll() {
		dao.findAll(pageCompany);
		pageCompany.getEntities().forEach(company -> pageCompanyDTO.getEntities().add(dao.mapToDTO(company)));
		return pageCompanyDTO;
	}

	public DTOCompany selectById(Long id) {
		DTOCompany companyDTO = new DTOCompany("");
		companyDTO = dao.mapToDTO(dao.findById(id));
		return companyDTO;
	}

	public Page<DTOCompany> selectPage() {
		dao.findBatch(pageCompany);
		pageCompany.getEntities().forEach(company -> pageCompanyDTO.getEntities().add(dao.mapToDTO(company)));
		return pageCompanyDTO;
	}

	public void delete(DTOCompany companyDTO) {
		try {
			dao.delete(CompanyMapper.map(companyDTO));
		} catch (MappingException e) {
			LOGGER.error("Error during delete company", e);
		}
	}
}
