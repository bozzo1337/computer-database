package com.excilys.cdb.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.service.ComputerService;

@RestController
@RequestMapping("/computers")
public class RestComputerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestComputerController.class);
	private ComputerService computerService;

	@Autowired
	public RestComputerController(ComputerService computerService) {
		this.computerService = computerService;
		LOGGER.info("RestComputerController instantiated");
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<DTOComputer>> getAll() {
		return new ResponseEntity<List<DTOComputer>>(computerService.getPageComp().getEntities(), HttpStatus.OK);
	}
}
