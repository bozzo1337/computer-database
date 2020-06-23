package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.model.Company;

public class CompanyMapperTest {
	
	private CompanyMapper mapper;
	
	@Mock
	private ResultSet resultSet;
	
	public CompanyMapperTest() {
		mapper = CompanyMapper.getInstance();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void mapResultSetNull() {
		assertNull("Mapping null ResultSet", mapper.map(null));
	}
	
	@Test
	public void mapResultSetEmpty() throws SQLException {
		Mockito.when(resultSet.next()).thenReturn(false);
		assertNull("Mapping empty ResultSet", mapper.map(resultSet));
	}
	
	@Test
	public void mapResultSetOneRow() throws SQLException {
		Mockito.when(resultSet.next()).thenReturn(true, false);
		Mockito.when(resultSet.getLong("id")).thenReturn(new Long(1L));
		Mockito.when(resultSet.getString("name")).thenReturn("Company1");
		Company company = new Company(new Long(1L), "Company1");
		assertEquals(company, mapper.map(resultSet));
	}
	
	@Test
	public void mapResultSetMultipleRows() throws SQLException {
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false);
		Mockito.when(resultSet.getLong("id")).thenReturn(new Long(3L), new Long(5L), new Long(6L));
		Mockito.when(resultSet.getString("name")).thenReturn("Company3", "Company5", "Company6");
		ArrayList<Company> compList = new ArrayList<Company>();
		compList.add(new Company(new Long(3L), "Company3"));
		compList.add(new Company(new Long(5L), "Company5"));
		compList.add(new Company(new Long(6L), "Company6"));
		assertEquals(compList, mapper.mapBatch(resultSet));
	}
}
