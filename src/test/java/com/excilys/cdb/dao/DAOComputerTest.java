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
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.AppConfig;
import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.NullMappingSourceException;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.exception.UnknownMappingSourceException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
public class DAOComputerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOComputerTest.class);
	@Mock
	private DBConnector dbcMocked;
	@Autowired
	private DBConnector dbc;
	@Autowired
	private DAOComputer dao;
	private Page<Computer> page;
	private String url;
	private String login;
	private String password;
	private String driver;

	public DAOComputerTest() {
		this.page = new Page<Computer>("");
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
		computersName.add("Computer44");
		computersName.add("Computer15");
		computersName.add("Computer999");
		page.setEntitiesPerPage(3);
		page.setIdxCurrentPage(1);
		dao.findBatch(page);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : page.getEntities()) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}

	@Test
	public void searchBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		computersName.add("Computer1");
		computersName.add("Computer18");
		computersName.add("Computer15");
		page.setEntitiesPerPage(3);
		page.setIdxCurrentPage(0);
		page.setSearch("Computer1");
		dao.searchBatch(page);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : page.getEntities()) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}

	@Test
	public void orderBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		computersName.add("Computer22");
		computersName.add("Computer3");
		computersName.add("Computer42");
		page.setEntitiesPerPage(3);
		page.setIdxCurrentPage(1);
		page.setOrder("computer");
		dao.orderBatch(page);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : page.getEntities()) {
			computersNameResult.add(comp.getName());
		}
		assertEquals(computersName, computersNameResult);
	}

	@Test
	public void orderedSearch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<String> computersName = new ArrayList<String>();
		computersName.add("Computer44");
		computersName.add("Computer42");
		page.setEntitiesPerPage(2);
		page.setIdxCurrentPage(0);
		page.setOrder("companydesc");
		page.setSearch("Computer4");
		dao.orderedSearch(page);
		List<String> computersNameResult = new ArrayList<String>();
		for (Computer comp : page.getEntities()) {
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
		page.setSearch("ComputerTest");
		dao.searchBatch(page);
		assertEquals(comp.getName(), page.getEntities().get(0).getName());
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
		dao.findBatch(page);
	}

	@Test(expected = PersistenceException.class)
	public void exceptionDelete() throws PersistenceException, SQLException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.delete(new Long(2L));
	}
}
