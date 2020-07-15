package com.excilys.cdb.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.dto.DTOCompany;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.MappingResultSetException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper implements RowMapper<Company> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyMapper.class);
	
	private CompanyMapper() {
	}
	
	public Company map(Object source) throws MappingException {
		Company company;
		if (source == null) {
			throw new NullMappingSourceException();
		}
		if (source instanceof ResultSet) {
			company = mapFromResultSet((ResultSet) source);
		} else if (source instanceof DTOCompany) {
			company = mapFromDTO((DTOCompany) source);
		} else {
			throw new UnknownMappingSourceException();
		}
		return company;
	}

	private Company mapFromResultSet(ResultSet results) throws MappingResultSetException {
		Company company = new Company();
		try {
			Long id = results.getLong("company.id");
			company = new Company(id != 0 ? id : null, results.getString("company.name"));
		} catch (SQLException e) {
			LOGGER.error("Error while mapping Computer from ResultSet", e);
			throw new MappingResultSetException();
		}
		return company;
	}

	private Company mapFromDTO(DTOCompany companyDTO) {
		return new Company(Long.valueOf(companyDTO.getId()), companyDTO.getName());
	}

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		Company company = new Company();
		try {
			company = map(rs);
		} catch (MappingException e) {
			LOGGER.debug("Exception during mapRow Company", e);
		}
		return company;
	}
}
