package com.excilys.cdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.service.ComputerService;

@RestController
@RequestMapping("/computer/display")
public class RestComputerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestComputerController.class);
	private ComputerService computerService;

	@Autowired
	public RestComputerController(ComputerService computerService) {
		this.computerService = computerService;
		LOGGER.info("RestComputerController instantiated");
	}
}
