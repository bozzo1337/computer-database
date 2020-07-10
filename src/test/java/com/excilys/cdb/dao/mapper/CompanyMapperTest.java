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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.AppConfig;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
public class CompanyMapperTest {
		
	@Mock
	private ResultSet resultSet;
	
	public CompanyMapperTest() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = NullMappingSourceException.class)
	public void mapResultSetNull() throws NullMappingSourceException, UnknownMappingSourceException {
		 CompanyMapper.map(null);		
	}
	
	@Test(expected = UnknownMappingSourceException.class)
	public void mapUnknownSource() throws NullMappingSourceException, UnknownMappingSourceException {
		CompanyMapper.map(new DiagnosticCollector<List<Exception>>());
	}
	
	@Test
	public void mapResultSetOneRow() throws SQLException, NullMappingSourceException, UnknownMappingSourceException {
		Mockito.when(resultSet.getLong("company.id")).thenReturn(new Long(1L));
		Mockito.when(resultSet.getString("company.name")).thenReturn("Company1");
		Company company = new Company(new Long(1L), "Company1");
		assertEquals(company, CompanyMapper.map(resultSet));
	}
	
	@Test
	public void mapResultSetMultipleRows() throws SQLException, NullMappingSourceException, UnknownMappingSourceException {
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false);
		Mockito.when(resultSet.getLong("company.id")).thenReturn(new Long(3L), new Long(5L), new Long(6L));
		Mockito.when(resultSet.getString("company.name")).thenReturn("Company3", "Company5", "Company6");
		ArrayList<Company> compList = new ArrayList<Company>();
		compList.add(new Company(new Long(3L), "Company3"));
		compList.add(new Company(new Long(5L), "Company5"));
		compList.add(new Company(new Long(6L), "Company6"));
		ArrayList<Company> compListResult = new ArrayList<Company>();
		while (resultSet.next()) {
			compListResult.add(CompanyMapper.map(resultSet));
		}
		assertEquals(compList, compListResult);
	}
}
