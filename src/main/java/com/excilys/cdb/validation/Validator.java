package com.excilys.cdb.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.IncorrectDiscDateException;
import com.excilys.cdb.exception.IncorrectIDException;
import com.excilys.cdb.exception.IncorrectIntroDateException;
import com.excilys.cdb.exception.IncorrectNameException;
import com.excilys.cdb.exception.IncorrectTemporalityException;

public class Validator {

	private static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public static LocalDate validateDate(String input) throws DateTimeParseException {
		LocalDate dateValid = null;
		dateValid = LocalDate.parse(input, df);
		return dateValid;
	}
	
	public static Long validateID(String input) throws NumberFormatException {
		Long validId = null;
		validId = Long.valueOf(input);
		return validId;
	}
	
	public static boolean validateTemporality(LocalDate intro, LocalDate disc) {
		boolean valid = true;
		if (intro != null && disc != null) {
			valid = intro.isBefore(disc);
		}
		return valid;
	}
	
	public static boolean validateName(String input) {
		return input != null && !input.isEmpty();
	}
	
	public static void validateDTO(DTOComputer computerDTO) throws IncorrectNameException, IncorrectIntroDateException, 
	IncorrectDiscDateException, IncorrectIDException, IncorrectTemporalityException {
		LocalDate intro = null;
		LocalDate disc = null;
		if (!validateName(computerDTO.getName())) {
			throw new IncorrectNameException("Empty name");
		}
		if (!computerDTO.getIntroduced().equals("")) {
			try {
				intro = validateDate(computerDTO.getIntroduced());
			} catch (DateTimeParseException e) {
				throw new IncorrectIntroDateException("Incorrect intro date format", e);
			}
		}
		if (!computerDTO.getDiscontinued().equals("")) {
			try {
				disc = validateDate(computerDTO.getDiscontinued());
			} catch (DateTimeParseException e) {
				throw new IncorrectDiscDateException("Incorrect disc date format", e);
			}
		}
		if (!computerDTO.getCompanyId().equals("0")) {
			try {
				validateID(computerDTO.getCompanyId());
			} catch (NumberFormatException e) {
				throw new IncorrectIDException("Incorrect ID format", e);
			}
		}
		if (intro != null && disc != null && !validateTemporality(intro, disc)) {
			throw new IncorrectTemporalityException("Temporality error");
		}
	}
}
