package com.excilys.cdb.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.serializer.SerializerComputer;
import com.excilys.cdb.service.ComputerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

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

	@GetMapping("")
	public ResponseEntity<String> getAll() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addSerializer(new SerializerComputer());
		mapper.registerModule(module);
		String computers = "";
		try {
			computers = computerService.getPageComp().getEntities().stream().map(t -> {
				try {
					return mapper.writeValueAsString(t);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.joining());
		} catch (Exception e) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>(computers, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DTOComputer> getOne(@PathVariable Long id) {
		DTOComputer computer = null;
		try {
			computer = computerService.selectById(id);
		} catch (PersistenceException e) {
			return new ResponseEntity<DTOComputer>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<DTOComputer>(computer, HttpStatus.OK);
	}

	@PutMapping(value = { "/add/{name}/intro={intro}/disc={disc}/companyId={companyId}" })
	public ResponseEntity<DTOComputer> add(@PathVariable String name,
			@PathVariable(required = false) Optional<String> intro,
			@PathVariable(required = false) Optional<String> disc,
			@PathVariable(required = false) Optional<String> companyId) {
		DTOComputer newComputer = new DTOComputer.Builder().withName(name).withIntroDate(intro.orElse(""))
				.withDiscDate(disc.orElse("")).withCompanyDTO(new DTOCompany(companyId.orElse(""))).build();
		try {
			computerService.create(newComputer);
		} catch (PersistenceException e) {
			return new ResponseEntity<DTOComputer>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<DTOComputer>(newComputer, HttpStatus.CREATED);
	}
}
