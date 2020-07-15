package com.excilys.cdb.dao.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.MappingResultSetException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

@Component
public class ComputerMapper implements RowMapper<Computer> {

	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerMapper.class);

	private ComputerMapper() {
	}

	public Computer map(Object source) throws MappingException {
		Computer computer;
		if (source == null) {
			LOGGER.error("Null source while mapping Computer");
			throw new NullMappingSourceException();
		}
		if (source instanceof ResultSet) {
			computer = mapFromResultSet((ResultSet) source);
		} else if (source instanceof DTOComputer) {
			computer = mapFromDTO((DTOComputer) source);
		} else {
			LOGGER.error("Unknown source while mapping Computer");
			throw new UnknownMappingSourceException();
		}
		return computer;
	}

	private Computer mapFromResultSet(ResultSet results) throws MappingResultSetException {
		Computer computer = new Computer.Builder().build();
		try {
			Computer.Builder computerBuilder = new Computer.Builder();
			Long id = results.getLong("computer.id");
			if (id != 0 ) {
				computerBuilder.withId(id);
			}
			computerBuilder.withName(results.getString("computer.name"));
			Date intro = results.getDate("computer.introduced");
			if (intro != null) {
				computerBuilder.withIntroDate(intro.toLocalDate());
			}
			Date disc = results.getDate("computer.discontinued");
			if (disc != null) {
				computerBuilder.withDiscDate(disc.toLocalDate());
			}
			Long companyId = results.getLong("computer.company_id");
			if (companyId != 0) {
				computerBuilder.withCompanyId(companyId);
				computerBuilder.withCompanyName(results.getString("company.name"));
			}
			computer = computerBuilder.build();
		} catch (SQLException e) {
			LOGGER.error("Error while mapping Computer from ResultSet", e);
			throw new MappingResultSetException();
		}
		return computer;
	}

	private Computer mapFromDTO(DTOComputer computerDTO) {
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
		String compIdString = computerDTO.getCompanyId();
		Long compId;
		if(compIdString == null || "0".equals(computerDTO.getCompanyId())) {
			compId = null;
		} else {
			compId = Long.valueOf(compIdString);
		}
		Computer computer = new Computer.Builder().withId(id).withName(computerDTO.getName()).withIntroDate(intro)
				.withDiscDate(disc).withCompanyId(compId).build();
		return computer;
	}

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		Computer computer = new Computer.Builder().build();
		try {
			computer = map(rs);
		} catch (MappingException e) {
			LOGGER.debug("Exception during mapRow Computer", e);
		}
		return computer;
	}
}
