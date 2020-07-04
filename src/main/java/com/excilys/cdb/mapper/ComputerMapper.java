package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerMapper extends Mapper<Computer> {

	private static ComputerMapper singleInstance = null;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private ComputerMapper() {

	}

	public static ComputerMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new ComputerMapper();
		}
		return singleInstance;
	}

	@Override
	public Computer map(Object source) throws NullMappingSourceException, UnknownMappingSourceException {
		Computer computer;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source.getClass() == ResultSet.class) {
			computer = mapFromResultSet((ResultSet) source);
		} else if (source.getClass() == DTOComputer.class) {
			computer = mapFromDTO((DTOComputer) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return computer;
	}

	private Computer mapFromResultSet(ResultSet results) {
		Computer computer = null;
		try {
			Computer.Builder computerBuilder = new Computer.Builder();
			computerBuilder.withId(results.getLong("computer.id")).withName(results.getString("computer.name"));
			Date intro = results.getDate("computer.introduced");
			if (intro != null) {
				computerBuilder.withIntroDate(intro.toLocalDate());
			}
			Date disc = results.getDate("computer.discontinued");
			if (disc != null) {
				computerBuilder.withDiscDate(disc.toLocalDate());
			}
			Long companyId = results.getLong("computer.company_id");
			computerBuilder.withCompanyId(companyId == 0 ? null : companyId);
			Company company = CompanyMapper.getInstance().map(results);
			computerBuilder.withCompany(company);
			computer = computerBuilder.build();
		} catch (SQLException | NullMappingSourceException | UnknownMappingSourceException e) {
			// TODO treat exception
			e.printStackTrace();
		}
		return computer;
	}

	private Computer mapFromDTO(DTOComputer computerDTO) {
		Long id = !(computerDTO.getId() == null) ? Long.valueOf(computerDTO.getId()) : null;
		LocalDate intro = !computerDTO.getIntroduced().isEmpty() ? LocalDate.parse(computerDTO.getIntroduced(), df)
				: null;
		LocalDate disc = !computerDTO.getDiscontinued().isEmpty() ? LocalDate.parse(computerDTO.getDiscontinued(), df)
				: null;
		Long compId = !computerDTO.getCompanyId().toString().equals("0") ? Long.valueOf(computerDTO.getCompanyId())
				: null;
		Computer computer = new Computer.Builder().withId(id).withName(computerDTO.getName()).withIntroDate(intro)
				.withDiscDate(disc).withCompanyId(compId).build();
		return computer;
	}
}
