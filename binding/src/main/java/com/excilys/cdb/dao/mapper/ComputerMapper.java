package com.excilys.cdb.dao.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

@Component
public class ComputerMapper {

	private static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class);

	private ComputerMapper() {
	}

	public static Computer map(Object source) throws MappingException {
		Computer computer;
		if (source == null) {
			LOGGER.error("Null source while mapping Computer");
			throw new NullMappingSourceException();
		}
		if (source instanceof DTOComputer) {
			computer = mapFromDTO((DTOComputer) source);
		} else {
			LOGGER.error("Unknown source while mapping Computer");
			throw new UnknownMappingSourceException();
		}
		return computer;
	}

	private static Computer mapFromDTO(DTOComputer computerDTO) throws MappingException {
		Long id;
		if (computerDTO.getId() == null || computerDTO.getId().isEmpty()) {
			id = null;
		} else {
			id = Long.valueOf(computerDTO.getId());
		}
		LocalDate intro;
		if (computerDTO.getIntroduced() == null || computerDTO.getIntroduced().isEmpty()) {
			intro = null;
		} else {
			intro = LocalDate.parse(computerDTO.getIntroduced(), df);
		}
		LocalDate disc;
		if (computerDTO.getDiscontinued() == null || computerDTO.getDiscontinued().isEmpty()) {
			disc = null;
		} else {
			disc = LocalDate.parse(computerDTO.getDiscontinued(), df);
		}
		Computer computer = new Computer.Builder().withId(id).withName(computerDTO.getName()).withIntroDate(intro)
				.withDiscDate(disc).withCompany(CompanyMapper.map(computerDTO.getCompanyDTO())).build();
		return computer;
	}
}
