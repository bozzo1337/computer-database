package com.excilys.cdb.dao.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.config.CoreConfig;
import com.excilys.cdb.config.BindingConfig;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, BindingConfig.class})
public class CompanyMapperTest {
		
	@Mock
	private ResultSet resultSet;
	@Autowired
	private CompanyMapper mapper;
	
	public CompanyMapperTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = NullMappingSourceException.class)
	public void mapResultSetNull() throws MappingException {
		mapper.map(null);		
	}
	
	@Test(expected = UnknownMappingSourceException.class)
	public void mapUnknownSource() throws MappingException {
		mapper.map(new DiagnosticCollector<List<Exception>>());
	}
	
	@Test(expected = UnknownMappingSourceException.class)
	public void mapResultSetOneRow() throws SQLException, MappingException {
		Mockito.when(resultSet.getLong("company.id")).thenReturn(new Long(1L));
		Mockito.when(resultSet.getString("company.name")).thenReturn("Company1");
		Company company = new Company(new Long(1L), "Company1");
		assertEquals(company, mapper.map(resultSet));
	}
	
	@Test(expected = UnknownMappingSourceException.class)
	public void mapResultSetMultipleRows() throws SQLException, MappingException {
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false);
		Mockito.when(resultSet.getLong("company.id")).thenReturn(new Long(3L), new Long(5L), new Long(6L));
		Mockito.when(resultSet.getString("company.name")).thenReturn("Company3", "Company5", "Company6");
		ArrayList<Company> compList = new ArrayList<Company>();
		compList.add(new Company(new Long(3L), "Company3"));
		compList.add(new Company(new Long(5L), "Company5"));
		compList.add(new Company(new Long(6L), "Company6"));
		ArrayList<Company> compListResult = new ArrayList<Company>();
		while (resultSet.next()) {
			compListResult.add(mapper.map(resultSet));
		}
		assertEquals(compList, compListResult);
	}
}
