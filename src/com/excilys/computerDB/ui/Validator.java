package com.excilys.computerDB.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	protected LocalDate validateDate(String input) {
		LocalDate dateValid = null;
		try {
			dateValid = LocalDate.parse(input, df);
		} catch (DateTimeParseException e) {
			//TODO
		}
		return dateValid;
	}
	
	protected Long validateID(String input) {
		Long validId = null;
		try {
			validId = Long.valueOf(input);
		} catch (NumberFormatException e) {
			//TODO
		}
		return validId;
	}
	
	protected boolean validateTemporality(LocalDate intro, LocalDate disc) {
		boolean valid = true;
		if (intro != null && disc != null) {
			valid = intro.isBefore(disc);
		}
		return valid;
	}
	
	protected boolean validateName(String input) {
		return !input.trim().isEmpty();
	}
}
