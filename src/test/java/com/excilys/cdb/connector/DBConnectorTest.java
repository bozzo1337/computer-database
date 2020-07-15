package com.excilys.cdb.connector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.excilys.cdb.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
public class DBConnectorTest {

	@Autowired
	private DBConnector dbc;
	
	@Test
	public void getConnNotNull() throws SQLException {
		assertNotNull(dbc.getConn());
	}
	
	@Test
	public void reopenConn() throws SQLException {
		dbc.getConn().close();
		assertFalse(dbc.getConn().isClosed());
	}
}
