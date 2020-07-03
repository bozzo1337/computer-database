package com.excilys.cdb.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.model.Computer;

public class DTOComputerMapper extends Mapper<DTOComputer> {
	
	private static DTOComputerMapper singleInstance = null;
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	private DTOComputerMapper() {
		
	}
	
	public static DTOComputerMapper getInstance() {
		if (singleInstance == null) {
			singleInstance = new DTOComputerMapper();
		}
		return singleInstance;
	}
	
	@Override
	public DTOComputer map(Object source) throws Exception {
		// TODO Auto-generated method stub
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
