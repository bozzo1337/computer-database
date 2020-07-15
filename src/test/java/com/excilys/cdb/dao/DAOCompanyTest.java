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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.AppConfig;
import com.excilys.cdb.exception.PersistenceException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Page;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class, loader = AnnotationConfigContextLoader.class)
public class DAOCompanyTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DAOCompanyTest.class);
	@Autowired
	private DAOCompany dao;
	@Autowired
	private DAOComputer daoComputer;
	private Page<Company> page;
	private String url;
	private String login;
	private String password;
	private String driver;
	private JdbcTemplate jdbcTemplate;

	public DAOCompanyTest() {
		MockitoAnnotations.initMocks(this);
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

	private IDatabaseConnection getConnection() throws ClassNotFoundException, SQLException, DatabaseUnitException {
		Class.forName(driver);
		return new DatabaseConnection(DriverManager.getConnection(url, login, password));
	}

	private QueryDataSet getDatabaseDataSet() throws Exception {
		QueryDataSet loadedDataSet = new QueryDataSet(getConnection());
		loadedDataSet.addTable("company");
		return loadedDataSet;
	}

	@Before
	public void setUp() throws ClassNotFoundException, DatabaseUnitException, SQLException {
		handleSetUpOperation();
		jdbcTemplate = Mockito.spy(dao.getJdbcTemplate());
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

	@Test(expected = PersistenceException.class)
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
		dao.delete(new Long(1));
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

	@Test
	public void breakTransaction() throws Exception {
		IDataSet dataSet = getDatabaseDataSet();
		assertNotNull(dataSet);
		double initialCount = daoComputer.count();
		Mockito.doThrow(new RuntimeException()).when(jdbcTemplate).update(SQLRequest.DELETE_COMPANY.toString(), new Long(5L));
		try {
			dao.delete(new Long(5L));
		} catch (RuntimeException e) {
			LOGGER.debug("Runtime caught during breakTransaction Test");
		} finally {
			assertEquals(4.0, dao.count(), 0.01);
			assertEquals(initialCount, daoComputer.count(), 0.01);
		}
	}
}
