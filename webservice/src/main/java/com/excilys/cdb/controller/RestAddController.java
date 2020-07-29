package com.excilys.cdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.controller.attributes.AddAttributes;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.validation.ValidationException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

@RestController
@RequestMapping("/create")
public class RestAddController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestAddController.class);
	private ComputerService computerService;
	private CompanyService companyService;
	private AddAttributes addAttributes;

	@Autowired
	public RestAddController(ComputerService computerService, CompanyService companyService) {
		this.computerService = computerService;
		this.companyService = companyService;
		addAttributes = new AddAttributes();
		LOGGER.info("AddController instantiated");
	}

	@GetMapping
	public String loadForm(Model model, @RequestParam String firstCallCreate) {
		addAttributes.setListCompanies(companyService.selectAll().getEntities());
		boolean firstCallCreateBool = Boolean.parseBoolean(firstCallCreate);
		addAttributes.setFirstCallCreate(firstCallCreateBool);
		model.addAttribute(addAttributes);
		return "create";
	}

	@PostMapping
	public String addComputer(Model model, @RequestParam String computerNameInput,
			@RequestParam(required = false) String introduced, @RequestParam(required = false) String discontinued,
			@RequestParam(required = false) String companyId) {
		boolean creationOK = false;
		boolean validDTO = true;
		DTOComputer computerDTO = new DTOComputer.Builder().withName(computerNameInput).withIntroDate(introduced)
				.withDiscDate(discontinued).withCompanyDTO(new DTOCompany(companyId)).build();
		String errMessage = null;
		try {
			Validator.validateDTO(computerDTO);
		} catch (ValidationException e) {
			validDTO = false;
			errMessage = e.getMessage();
		}
		if (validDTO) {
			computerService.create(computerDTO);
			creationOK = true;
		}
		addAttributes.setCreationOK(creationOK);
		addAttributes.setErrMessage(errMessage);
		return loadForm(model, "false");
	}
}
