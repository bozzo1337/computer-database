package com.excilys.cdb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.controller.attributes.EditAttributes;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.validation.ValidationException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

@RestController
@RequestMapping("/edit")
public class RestEditController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestEditController.class);
	private ComputerService computerService;
	private CompanyService companyService;
	private EditAttributes editAttributes;

	@Autowired
	public RestEditController(ComputerService computerService, CompanyService companyService) {
		this.computerService = computerService;
		this.companyService = companyService;
		editAttributes = new EditAttributes();
	}

	@GetMapping
	public ResponseEntity<DTOComputer> loadForm(@RequestParam String computerId, @RequestParam String firstCall) {
		editAttributes.setListCompanies(companyService.selectAll().getEntities());
		Boolean firstCallBool = Boolean.valueOf(firstCall);
		Long compId = null;
		DTOComputer computer = null;
		if (computerId != null) {
			try {
				compId = Long.parseLong(computerId);
			} catch (NumberFormatException e) {
				LOGGER.debug("Wrong ID format");
			}
		}
		if (computerId != null) {
			computer = computerService.selectById(compId);
		}
		editAttributes.setFirstCall(firstCallBool);
		editAttributes.setComputer(computer);
		return new ResponseEntity<DTOComputer>(computer, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<DTOComputer> editComputer(Model model, @RequestParam String computerId,
			@RequestParam String computerNameInput, @RequestParam(required = false) String introduced,
			@RequestParam(required = false) String discontinued, @RequestParam(required = false) String companyId) {
		boolean updateOK = false;
		boolean validDTO = true;
		introduced = introduced != null ? introduced : "";
		discontinued = discontinued != null ? discontinued : "";
		companyId = companyId != null ? companyId : "";
		DTOComputer computerDTO = new DTOComputer.Builder().withId(computerId).withName(computerNameInput)
				.withIntroDate(introduced).withDiscDate(discontinued).withCompanyDTO(new DTOCompany(companyId)).build();
		String errMessage = null;
		try {
			Validator.validateDTO(computerDTO);
		} catch (ValidationException e) {
			validDTO = false;
			errMessage = e.getMessage();
		}
		if (validDTO) {
			computerService.update(computerDTO);
			updateOK = true;
		}
		editAttributes.setUpdateOK(updateOK);
		editAttributes.setErrMessage(errMessage);
		return loadForm(computerId, "false");
	}
}
