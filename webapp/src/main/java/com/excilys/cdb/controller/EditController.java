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

import com.excilys.cdb.controller.attributes.EditAttributes;
import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.validation.ValidationException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

@Controller
@RequestMapping("/edit")
public class EditController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EditController.class);
	private ComputerService computerService;
	private CompanyService companyService;
	private EditAttributes editAttributes;

	@Autowired
	public EditController(ComputerService computerService, CompanyService companyService) {
		this.computerService = computerService;
		this.companyService = companyService;
		editAttributes = new EditAttributes();
	}

	@GetMapping
	public String loadForm(Model model, @RequestParam String computerId, @RequestParam String firstCall) {
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
		model.addAttribute(editAttributes);
		return "edit";
	}

	@PostMapping
	public String editComputer(Model model, @RequestParam String computerId,
			@RequestParam String computerNameInput, @RequestParam(required = false) String introduced,
			@RequestParam(required = false) String discontinued, @RequestParam(required = false) String companyId) {
		boolean updateOK = false;
		boolean validDTO = true;
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
		return loadForm(model, computerId, "false");
	}
}
