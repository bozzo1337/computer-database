package com.excilys.cdb.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.ui.Validator;

import exception.IncorrectDiscDateException;
import exception.IncorrectIDException;
import exception.IncorrectIntroDateException;
import exception.IncorrectNameException;
import exception.IncorrectTemporalityException;

public class DTOComputer {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;
	private Validator validator;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public DTOComputer() {
	}
	
	public DTOComputer(Computer computer) {
		this.companyId = computer.getId().toString();
		this.name = computer.getName();
		this.introduced = computer.getIntroduced().format(df);
		this.discontinued = computer.getDiscontinued().format(df);
		this.companyId = computer.getCompanyId().toString();
	}
	
	public DTOComputer(String name, String introduced, String discontinued, String companyId) {
		this.name = name.trim();
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		validator = new Validator();
	}
	
	public void validate() throws IncorrectNameException, IncorrectIntroDateException, 
		IncorrectDiscDateException, IncorrectIDException, IncorrectTemporalityException {
		LocalDate intro = null;
		LocalDate disc = null;
		if (!this.validator.validateName(this.name)) {
			throw new IncorrectNameException("Empty name");
		}
		if (this.introduced != null) {
			try {
				intro = this.validator.validateDate(this.introduced);
			} catch (DateTimeParseException e) {
				throw new IncorrectIntroDateException("Incorrect intro date format", e);
			}
		}
		if (this.discontinued != null) {
			try {
				disc = this.validator.validateDate(this.discontinued);
			} catch (DateTimeParseException e) {
				throw new IncorrectDiscDateException("Incorrect disc date format", e);
			}
		}
		if (this.companyId != null) {
			try {
				this.validator.validateID(this.companyId);
			} catch (NumberFormatException e) {
				throw new IncorrectIDException("Incorrect ID format", e);
			}
		}
		if (intro != null && disc != null && !this.validator.validateTemporality(intro, disc)) {
			throw new IncorrectTemporalityException("Temporality error");
		}
	}
	
	public String getName() {
		return name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getId() {
		return id;
	}
}
