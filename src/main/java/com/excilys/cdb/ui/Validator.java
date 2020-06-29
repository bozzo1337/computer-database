package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public LocalDate validateDate(String input) throws DateTimeParseException {
		LocalDate dateValid = null;
		dateValid = LocalDate.parse(input, df);
		return dateValid;
	}
	
	public Long validateID(String input) throws NumberFormatException {
		Long validId = null;
		validId = Long.valueOf(input);
		return validId;
	}
	
	public boolean validateTemporality(LocalDate intro, LocalDate disc) {
		boolean valid = true;
		if (intro != null && disc != null) {
			valid = intro.isBefore(disc);
		}
		return valid;
	}
	
	public boolean validateName(String input) {
		return !input.isEmpty();
	}
}
