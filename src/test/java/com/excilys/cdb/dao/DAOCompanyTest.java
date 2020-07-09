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
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

public class DAOCompanyTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOCompanyTest.class);
	@Mock
	private DBConnector dbcMocked;
	private DBConnector dbc;
	private DAOCompany dao;
	private Page<Company> page;
	private String url;
	private String login;
	private String password;
	private String driver;

	public DAOCompanyTest() {
		MockitoAnnotations.initMocks(this);
		this.dbc = new DBConnector();
		this.dao = new DAOCompany(dbc);
		this.page = new Page<Company>("");
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

	private IDatabaseConnection getConnection()
			throws ClassNotFoundException, SQLException, DatabaseUnitException {
		Class.forName(driver);
		return new DatabaseConnection(DriverManager.getConnection(url, login, password));
	}

	private QueryDataSet getDatabaseDataSet() throws Exception {
		QueryDataSet loadedDataSet = new QueryDataSet(getConnection());
		loadedDataSet.addTable("company");
		return loadedDataSet;
	}

	@Before
	public void setUp() {
		try {
			handleSetUpOperation();
		} catch (ClassNotFoundException | DatabaseUnitException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dao.setDBC(dbc);;
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
		assertEquals("Company2", dao.findById(new Long(2L)).getName());
	}

	@Test
	public void findByIdIncorrect() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		assertNull(dao.findById(new Long(20L)).getName());
	}

	@Test
	public void findAll() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<Company> companies = new ArrayList<Company>();
		companies.add(new Company(new Long(1), "Company1"));
		companies.add(new Company(new Long(2), "Company2"));
		companies.add(new Company(new Long(4), "Company4"));
		companies.add(new Company(new Long(5), "Company5"));
		dao.findAll(page);
		assertEquals(companies, page.getEntities());
	}

	@Test
	public void delete() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		dao.delete(new Long(5));
		assertEquals(3.0, dao.count(), 0.01);
	}

	@Test
	public void count() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		assertEquals(4.0, dao.count(), 0.01);
	}

	@Test
	public void findBatch() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		List<Company> companies = new ArrayList<Company>();
		companies.add(new Company(new Long(1), "Company1"));
		companies.add(new Company(new Long(2), "Company2"));
		companies.add(new Company(new Long(4), "Company4"));
		page.setEntitiesPerPage(3);
		page.setIdxCurrentPage(0);
		dao.findBatch(page);
		assertEquals(companies, page.getEntities());
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
	public void exceptionFindAll() throws SQLException, PersistenceException {
		dao.setDBC(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findAll(page);
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
