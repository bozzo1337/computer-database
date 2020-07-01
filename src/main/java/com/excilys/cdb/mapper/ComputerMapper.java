package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dto.DTOComputer;
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
	public Computer map(ResultSet results) {
		try {
			if (results != null && results.next()) {
				return mapOne(results);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<DTOComputer> mapListToDTO(List<Computer> listComp) {
		ArrayList<DTOComputer> dtos = new ArrayList<DTOComputer>();
		for (Computer comp : listComp) {
			dtos.add(mapToDTO(comp));
		}
		return dtos;
	}
	
	public DTOComputer mapToDTO(Computer computer) {
		String id = computer.getId().toString();
		String name = computer.getName();
		String introduced = computer.getIntroduced() != null ? computer.getIntroduced().format(df) : null;
		String discontinued = computer.getDiscontinued() != null ? computer.getDiscontinued().format(df) : null;
		String companyId = computer.getCompanyId() != null ? computer.getCompanyId().toString() : null;
		String companyName = computer.getCompany() != null ? computer.getCompany().getName() : null;
		return new DTOComputer(id, name, introduced, discontinued, companyId, companyName);
	}
	
	public Computer mapFromValidDTO(DTOComputer computerDTO) {
		Long id = !computerDTO.getId().isEmpty() ? Long.valueOf(computerDTO.getId()) : null;
		LocalDate intro = !computerDTO.getIntroduced().isEmpty() ? LocalDate.parse(computerDTO.getIntroduced(), df) : null;
		LocalDate disc = !computerDTO.getDiscontinued().isEmpty() ? LocalDate.parse(computerDTO.getDiscontinued(), df) : null;
		Long compId = !computerDTO.getCompanyId().toString().equals("0") ? Long.valueOf(computerDTO.getCompanyId()) : null;
		Computer computer = new Computer(id, computerDTO.getName(), intro, disc, compId);
		return computer;
	}

	@Override
	public List<Computer> mapBatch(ResultSet results) {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		try {
			while (results != null && results.next()) {
				computers.add(mapOne(results));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return computers;
	}
	
	private Computer mapOne(ResultSet results) throws SQLException {
		Computer computer = new Computer();
		computer.setId(results.getLong("computer.id"));
		computer.setName(results.getString("computer.name"));
		Date intro = results.getDate("computer.introduced");
		if (intro != null) {
			computer.setIntroduced(intro.toLocalDate());
		}
		Date disc = results.getDate("computer.discontinued");
		if (disc != null) {
			computer.setDiscontinued(disc.toLocalDate());
		}
		Long companyId = results.getLong("computer.company_id");
		if (companyId == 0) {
			computer.setCompanyId(null);
		} else {
			computer.setCompanyId(companyId);
		}
		Company company = CompanyMapper.getInstance().mapOne(results);
		computer.setCompany(company);
		return computer;
	}
}
