package com.excilys.cdb.persistence;

import org.dbunit.PropertiesBasedJdbcDatabaseTester;

public class DBConnectorTest {

	{
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.mysql.jdbcDriver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:mysql:sample");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "test");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "testpw");
	}
	
	
}
