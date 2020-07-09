package com.excilys.cdb.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class ApplicationContextServlet extends ClassPathXmlApplicationContext {

	private static ApplicationContextServlet singleInstance;
	private ApplicationContext context;
	
	private ApplicationContextServlet() {
		 context = new AnnotationConfigApplicationContext(AppConfig.class);
	}
	
	public static ApplicationContextServlet getInstance() {
		if (singleInstance == null) {
			singleInstance = new ApplicationContextServlet();
		}
		return singleInstance;
	}
	
	public ComputerService getComputerService() {
		return context.getBean(ComputerService.class);
	}
	
	public CompanyService getCompanyService() {
		return context.getBean(CompanyService.class);
	}
}
