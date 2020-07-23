package com.excilys.cdb.dao.mapper;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.config.CoreConfig;
import com.excilys.cdb.config.BindingConfig;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.mapping.MappingException;
import com.excilys.cdb.exception.mapping.NullMappingSourceException;
import com.excilys.cdb.exception.mapping.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfig.class, BindingConfig.class})
public class ComputerMapperTest {

	@Mock
	private ResultSet resultSet;
	@Autowired
	private ComputerMapper mapper;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected = NullMappingSourceException.class)
	public void mapNullSource() throws MappingException {
		mapper.map(null);
	}
	
	@Test(expected = UnknownMappingSourceException.class)
	public void mapUnknownSource() throws MappingException {
		mapper.map(new DiagnosticCollector<List<Exception>>());
	}

	@Test(expected = UnknownMappingSourceException.class)
	public void mapResultSetEmpty() throws SQLException, MappingException {
		assertEquals(new Computer.Builder().build(),
				mapper.map(resultSet));
	}

	@Test(expected = UnknownMappingSourceException.class)
	public void mapResultSetOneRow() throws SQLException, MappingException {
		when(resultSet.getLong("computer.id")).thenReturn(new Long(1L));
		when(resultSet.getString("computer.name")).thenReturn("Computer1");
		when(resultSet.getDate("computer.introduced")).thenReturn(new Date(1592900853L));
		when(resultSet.getDate("computer.discontinued")).thenReturn(new Date(1592900862L));
		when(resultSet.getLong("computer.company_id")).thenReturn(new Long(1L));
		Computer computer = new Computer.Builder().withId(new Long(1L)).withName("Computer1")
				.withIntroDate(new Date(1592900853L).toLocalDate()).withDiscDate(new Date(1592900862L).toLocalDate())
				.withCompanyId(new Long(1L)).build();
		assertEquals(computer, mapper.map(resultSet));
	}

	@Test(expected = UnknownMappingSourceException.class)
	public void mapResultSetMultipleRows()
			throws SQLException, MappingException {
		when(resultSet.next()).thenReturn(true, true, true, false);
		when(resultSet.getLong("computer.id")).thenReturn(new Long(15L), new Long(12L), new Long(955L));
		when(resultSet.getString("computer.name")).thenReturn("Computer15", "Computer12", "Computer955");
		when(resultSet.getDate("computer.introduced")).thenReturn(new Date(1592666666L), new Date(99993L),
				new Date(1L));
		when(resultSet.getDate("computer.discontinued")).thenReturn(new Date(1592666677L), new Date(1111111111L),
				new Date(2L));
		when(resultSet.getLong("computer.company_id")).thenReturn(new Long(45L), new Long(45L), new Long(3L));
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
		ArrayList<Computer> compListResult = new ArrayList<Computer>();
		while (resultSet.next()) {
			compListResult.add(mapper.map(resultSet));
		}
		assertEquals(compList, compListResult);
	}

	@Test
	public void mapFromDTO() throws MappingException {
		DTOComputer computerDTO = new DTOComputer.Builder().withId("3").withName("Computer12")
				.withIntroDate("12/12/2012").build();
		Computer compExpected = new Computer.Builder().withId(new Long(3L)).withName("Computer12")
				.withIntroDate(LocalDate.of(2012, 12, 12)).build();
		assertEquals(compExpected, mapper.map(computerDTO));
	}
}
