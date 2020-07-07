package com.excilys.cdb.servlet;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class ApplicationContextServlet extends ClassPathXmlApplicationContext {

	private static ApplicationContextServlet singleInstance;
	private ClassPathXmlApplicationContext context;
	
	private ApplicationContextServlet() {
		 context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
	}
	
	public static ApplicationContextServlet getInstance() {
		if (singleInstance == null) {
			singleInstance = new ApplicationContextServlet();
		}
		return singleInstance;
	}
	
	public ComputerService getComputerService() {
		return (ComputerService) context.getBean("computerService");
	}
	
	public CompanyService getCompanyService() {
		return (CompanyService) context.getBean("companyService");
	}
}
