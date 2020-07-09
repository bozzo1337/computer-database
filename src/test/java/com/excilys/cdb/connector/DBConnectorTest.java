package com.excilys.cdb.connector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.Test;

public class DBConnectorTest {

	private DBConnector dbc;
	
	public DBConnectorTest() {
		this.dbc = new DBConnector();
	}
	
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
