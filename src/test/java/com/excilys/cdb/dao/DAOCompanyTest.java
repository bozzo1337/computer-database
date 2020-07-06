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
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.excilys.cdb.connector.DBConnector;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.model.Company;

public class DAOCompanyTest {

	@Mock
	private DBConnector dbcMocked;

	static {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static DBConnector dbc = DBConnector.getInstance();
	private static DAOCompany dao;
	private static String url;
	private static String login;
	private static String password;
	private static String driver;

	public DAOCompanyTest() {
		MockitoAnnotations.initMocks(this);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws ClassNotFoundException, DatabaseUnitException, SQLException {
		handleSetUpOperation();
	}

	private static IDataSet getDataSet() throws DataSetException {
		return new FlatXmlDataSetBuilder().build(DAOCompanyTest.class.getResourceAsStream("/dataset.xml"));
	}

	private static void handleSetUpOperation() throws DatabaseUnitException, SQLException, ClassNotFoundException {
		final IDatabaseConnection conn = getConnection();
		final IDataSet data = getDataSet();
		try {
			DatabaseOperation.CLEAN_INSERT.execute(conn, data);
		} finally {
			conn.close();
		}
	}

	private static IDatabaseConnection getConnection()
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
		dao = DAOCompany.getInstance(dbc);
	}

	@Test
	public void dataLoaded() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		double rowCount = dao.count();
		assertEquals(4.0, rowCount, 0.01);
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
		assertEquals(companies, dao.findAll());
	}

	@Test
	public void delete() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		dao.delete(new Long(5));
		assertEquals(3.0, dao.count(), 0.01);
		assertEquals(6.0, DAOComputer.getInstance().count(), 0.01);
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
		assertEquals(companies, dao.findBatch(3, 0));
	}

	@Test(expected = PersistenceException.class)
	public void exceptionCount() throws SQLException, PersistenceException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.count();
	}

	@Test(expected = PersistenceException.class)
	public void exceptionFindById() throws SQLException, PersistenceException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findById(new Long(1L));
	}

	@Test(expected = PersistenceException.class)
	public void exceptionFindAll() throws SQLException, PersistenceException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findAll();
	}

	@Test(expected = PersistenceException.class)
	public void exceptionFindBatch() throws SQLException, PersistenceException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.findBatch(2, 0);
	}
	
	@Test(expected = PersistenceException.class)
	public void exceptionDelete() throws PersistenceException, SQLException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn()).thenThrow(new SQLException("Mock"));
		dao.delete(new Long(2L));
	}
	
	@Test(expected = PersistenceException.class)
	public void exceptionTransactionDelete() throws PersistenceException, SQLException {
		dao = DAOCompany.getInstance(dbcMocked);
		Mockito.when(dbcMocked.getConn().prepareStatement(SQLRequest.DELETE_COMPUTERS_OF_COMPANY.toString())).thenThrow(new SQLException("Mock"));
		dao.delete(new Long(2L));
	}
}
