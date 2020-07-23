package com.excilys.cdb.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompanyTest {

	@Test
	public void companyConstructorTest() {
		Long id = new Long(3L);
		String name = "Company3";
		Company company = new Company(id, name);
		assertEquals(id, company.getId());
		assertEquals(name, company.getName());
	}
}
