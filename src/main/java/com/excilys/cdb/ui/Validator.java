package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public LocalDate validateDate(String input) {
		LocalDate dateValid = null;
		try {
			dateValid = LocalDate.parse(input, df);
		} catch (DateTimeParseException e) {
			//TODO
		}
		return dateValid;
	}
	
	public Long validateID(String input) {
		Long validId = null;
		try {
			validId = Long.valueOf(input);
		} catch (NumberFormatException e) {
			//TODO
		}
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
		return !input.trim().isEmpty();
	}
}
