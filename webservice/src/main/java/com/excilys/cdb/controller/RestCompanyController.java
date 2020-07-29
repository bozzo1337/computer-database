package com.excilys.cdb.controller;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class RestCompanyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestCompanyController.class);
	private CompanyService companyService;

	@Autowired
	public RestCompanyController(CompanyService companyService) {
		this.companyService = companyService;
		LOGGER.info("RestCompanyController instantiated");
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DTOCompany> returnCompany(@PathVariable Long id) {
		DTOCompany company = null;
		try {
			company = companyService.selectById(id);
		} catch (PersistenceException e) {
			return new ResponseEntity<DTOCompany>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<DTOCompany>(company, HttpStatus.OK);
	}

}
