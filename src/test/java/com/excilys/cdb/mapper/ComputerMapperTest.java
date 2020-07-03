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
		Mockito.when(resultSet.getLong("computer.id")).thenReturn(new Long(1L));
		Mockito.when(resultSet.getString("computer.name")).thenReturn("Computer1");
		Mockito.when(resultSet.getDate("computer.introduced")).thenReturn(new Date(1592900853L));
		Mockito.when(resultSet.getDate("computer.discontinued")).thenReturn(new Date(1592900862L));
		Mockito.when(resultSet.getLong("computer.company_id")).thenReturn(new Long(1L));
		Computer computer = new Computer.Builder().withId(new Long(1L)).withName("Computer1")
				.withIntroDate(new Date(1592900853L).toLocalDate()).withDiscDate(new Date(1592900862L).toLocalDate())
				.withCompanyId(new Long(1L)).build();
		assertEquals(computer, mapper.map(resultSet));
	}

	@Test
	public void mapResultSetMultipleRows() throws SQLException {
		Mockito.when(resultSet.next()).thenReturn(true, true, true, false);
		Mockito.when(resultSet.getLong("computer.id")).thenReturn(new Long(15L), new Long(12L), new Long(955L));
		Mockito.when(resultSet.getString("computer.name")).thenReturn("Computer15", "Computer12", "Computer955");
		Mockito.when(resultSet.getDate("computer.introduced")).thenReturn(new Date(1592666666L), new Date(99993L),
				new Date(1L));
		Mockito.when(resultSet.getDate("computer.discontinued")).thenReturn(new Date(1592666677L),
				new Date(1111111111L), new Date(2L));
		Mockito.when(resultSet.getLong("computer.company_id")).thenReturn(new Long(45L), new Long(45L), new Long(3L));
		Computer comp1 = new Computer.Builder().withId(new Long(15L)).withName("Computer15")
				.withIntroDate(new Date(1592666666L).toLocalDate()).withDiscDate(new Date(1592666677L).toLocalDate())
				.withCompanyId(new Long(45L)).build();
		Computer comp2 = new Computer.Builder().withId(new Long(12L)).withName("Computer12")
				.withIntroDate(new Date(99993L).toLocalDate()).withDiscDate(new Date(1111111111L).toLocalDate())
				.withCompanyId(new Long(45L)).build();
		Computer comp3 = new Computer.Builder().withId(new Long(955L)).withName("Computer955")
				.withIntroDate(new Date(1L).toLocalDate()).withDiscDate(new Date(2L).toLocalDate())
				.withCompanyId(new Long(3L)).build();
		ArrayList<Computer> compList = new ArrayList<Computer>();
		compList.add(comp1);
		compList.add(comp2);
		compList.add(comp3);
		assertEquals(compList, mapper.mapBatch(resultSet));
	}
}
