package com.excilys.cdb.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.model.Computer;

public class ComputerMapperTest {

	private ComputerMapper mapper;
	
	@Mock
	private ResultSet resultSet;
	
	@Before
	public void init() {
		mapper = ComputerMapper.getInstance();
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
		Mockito.when(resultSet.getString("name")).thenReturn("Computer1");
		Mockito.when(resultSet.getDate("introduced")).thenReturn(new Date(1592900853L));
		Mockito.when(resultSet.getDate("discontinued")).thenReturn(new Date(1592900862L));
		Mockito.when(resultSet.getLong("company_id")).thenReturn(new Long(1L));
		Computer computer = new Computer(new Long(1L), "Computer1", new Date(1592900853L).toLocalDate(),
				new Date(1592900862L).toLocalDate(), new Long(1L), null);
		assertEquals(computer, mapper.map(resultSet));
	}
	
	@Test
	public void mapResultSetMultipleRows() throws SQLException {
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false);
		Mockito.when(resultSet.getLong("id")).thenReturn(new Long(15L), new Long(12L), new Long(955L));
		Mockito.when(resultSet.getString("name")).thenReturn("Computer15", "Computer12", "Computer955");
		Mockito.when(resultSet.getDate("introduced")).thenReturn(new Date(1592666666L), new Date(99993L), new Date(1L));
		Mockito.when(resultSet.getDate("discontinued")).thenReturn(new Date(1592666677L), new Date(1111111111L), new Date(2L));
		Mockito.when(resultSet.getLong("company_id")).thenReturn(new Long(45L), new Long(45L), new Long(3L));
		ArrayList<Computer> compList = new ArrayList<Computer>();
		compList.add(new Computer(new Long(15L), "Computer15", new Date(1592666666L).toLocalDate(),
				new Date(1592666677L).toLocalDate(), new Long(45L), null));
		compList.add(new Computer(new Long(12L), "Computer12", new Date(99993L).toLocalDate(),
				new Date(1111111111L).toLocalDate(), new Long(45L), null));
		compList.add(new Computer(new Long(955L), "Computer955", new Date(1L).toLocalDate(),
				new Date(2L).toLocalDate(), new Long(3L), null));
		assertEquals(compList, mapper.mapBatch(resultSet));
	}
}
