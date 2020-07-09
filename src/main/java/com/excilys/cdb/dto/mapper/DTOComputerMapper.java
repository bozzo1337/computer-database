package com.excilys.cdb.dto.mapper;

import java.time.format.DateTimeFormatter;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

public class DTOComputerMapper {
	
	private static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private DTOComputerMapper() {
	}
	
	public static DTOComputer map(Object source) throws NullMappingSourceException, UnknownMappingSourceException {
		DTOComputer computerDTO;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source instanceof Computer) {
			computerDTO = mapFromComputer((Computer) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return computerDTO;
	}

	private static DTOComputer mapFromComputer(Computer computer) {
		DTOComputer.Builder builderDTO = new DTOComputer.Builder();
		builderDTO.withId(computer.getId().toString());
		builderDTO.withName(computer.getName());
		builderDTO.withIntroDate(computer.getIntroduced() != null ? computer.getIntroduced().format(df) : "");
		builderDTO.withDiscDate(computer.getDiscontinued() != null ? computer.getDiscontinued().format(df) : "");
		builderDTO.withCompanyId(computer.getCompanyId() != null ? computer.getCompanyId().toString() : "0");
		builderDTO.withCompanyName(computer.getCompany() != null ? computer.getCompany().getName() : "");
		return builderDTO.build();
	}
}
