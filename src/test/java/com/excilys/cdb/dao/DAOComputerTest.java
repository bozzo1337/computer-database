package com.excilys.cdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;

public class DAOComputerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOComputerTest.class);
	@Mock
	private DBConnector dbcMocked;
	private DBConnector dbc;
	private DAOComputer dao;
	private String url;
	private String login;
	private String password;
	private String driver;

	public DAOComputerTest(DBConnector dbc, DAOComputer dao) {
		this.dbc = dbc;
		this.dao = dao;
		MockitoAnnotations.initMocks(this);
		InputStream inputStream = null;
		try {
			inputStream = DAOCompanyTest.class.getResourceAsStream("/config.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			url = properties.getProperty("url");
			login = properties.getProperty("login");
			password = properties.getProperty("password");
			driver = properties.getProperty("driver");
		} catch (IOException e) {
			LOGGER.error("Error while accessing config.properties", e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				LOGGER.error("Error while closing config.properties", e);
			}
		}
	}

	@Before
	public void setUp() throws ClassNotFoundException, DatabaseUnitException, SQLException {
		handleSetUpOperation();
		dao.setDBC(dbc);
	}

	private IDataSet getDataSet() throws DataSetException {
		return new FlatXmlDataSetBuilder().build(DAOCompanyTest.class.getResourceAsStream("/dataset.xml"));
	}

	private void handleSetUpOperation() throws DatabaseUnitException, SQLException, ClassNotFoundException {
		final IDatabaseConnection conn = getConnection();
		final IDataSet data = getDataSet();
		try {
			DatabaseOperation.CLEAN_INSERT.execute(conn, data);
		} finally {
			conn.close();
		}
	}

	private IDatabaseConnection getConnection() throws ClassNotFoundException, SQLException, DatabaseUnitException {
		Class.forName(driver);
		return new DatabaseConnection(DriverManager.getConnection(url, login, password));
	}

	private QueryDataSet getDatabaseDataSet() throws Exception {
		QueryDataSet loadedDataSet = new QueryDataSet(getConnection());
		loadedDataSet.addTable("company");
		return loadedDataSet;
	}

	@Test
	public void dataLoaded() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
	}

	@Test
	public void findByIdCorrect() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		assertEquals("Computer3", dao.findById(new Long(3L)).getName());
	}

	@Test
	public void findByIdIncorrect() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		assertNull(dao.findById(new Long(20L)).getName());
	}

	@Test
	public void findBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		List<Computer> computers = new ArrayList<Computer>();
		computersName.add("Computer44");
		computersName.add("Computer15");
		computersName.add("Computer999");
		computers = dao.findBatch(3, 1);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : computers) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}
	
	@Test
	public void searchBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		List<Computer> computers = new ArrayList<Computer>();
		computersName.add("Computer15");
		computersName.add("Computer999");
		computers = dao.searchBatch("Company5", 3, 0);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : computers) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}
	
	@Test
	public void orderBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		List<Computer> computers = new ArrayList<Computer>();
		computersName.add("Computer22");
		computersName.add("Computer3");
		computersName.add("Computer42");
		computers = dao.orderBatch("computer", 3, 1);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : computers) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}
	
	@Test
	public void orderedSearch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		List<Computer> computers = new ArrayList<Computer>();
		computersName.add("Computer44");
		computersName.add("Computer42");
		computers = dao.orderedSearch("Computer4", "companydesc", 2, 0);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : computers) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}
	
	@Test
	public void create() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		Computer comp = new Computer.Builder().withName("computerTest").withCompanyId(new Long(4L)).build();
		dao.create(comp);
		assertEquals(comp.getName(), dao.searchBatch("computerTest", 1, 0).get(0).getName());
	}

	@Test
	public void delete() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		dao.delete(new Long(999));
		dao.delete(new Long(44));
		assertEquals(6.0, dao.count(), 0.01);
	}

	@Test
	public void count() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		assertEquals(8.0, dao.count(), 0.01);
	}

	@Test
	public void mapToDto() throws NullMappingSourceException, UnknownMappingSourceException {
		DTOComputer compDTO = new DTOComputer.Builder().withName("ComputerAA").withIntroDate("").withDiscDate("")
				.withCompanyName("").withCompanyId("4").withId("10").build();
		Computer comp = new Computer.Builder().withName("ComputerAA").withCompanyId(new Long(4L)).withId(new Long(10L))
				.build();
		assertEquals(compDTO, dao.mapToDTO(comp));
	}

	@Test(expected = PersistenceException.class)
	public void exceptionCount() throws SQLException, PersistenceException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.count();
	}

	@Test(expected = PersistenceException.class)
	public void exceptionFindById() throws SQLException, PersistenceException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findById(new Long(1L));
	}

	@Test(expected = PersistenceException.class)
	public void exceptionFindBatch() throws SQLException, PersistenceException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findBatch(2, 0);
	}

	@Test(expected = PersistenceException.class)
	public void exceptionDelete() throws PersistenceException, SQLException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.delete(new Long(2L));
	}
}
