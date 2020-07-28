package com.excilys.cdb.dao.mapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMapper.class);
	
	private CompanyMapper() {
	}
	
	public static Company map(Object source) throws MappingException {
		Company company;
		if (source == null) {
			LOGGER.error("Null mapping source while mapping Company");
			throw new NullMappingSourceException();
		}if (source instanceof DTOCompany) {
			company = mapFromDTO((DTOCompany) source);
		} else {
			LOGGER.error("Unknown mapping source while mapping Company");
			throw new UnknownMappingSourceException();
		}
		return company;
	}

	private static Company mapFromDTO(DTOCompany companyDTO) {
		return new Company(Long.valueOf(companyDTO.getId()), companyDTO.getName());
	}
}
