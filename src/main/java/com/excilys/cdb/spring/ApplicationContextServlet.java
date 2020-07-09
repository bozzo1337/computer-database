package com.excilys.cdb.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

public class ApplicationContextServlet extends ClassPathXmlApplicationContext {

	private static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	public static ComputerService getComputerService() {
		return context.getBean(ComputerService.class);
	}
	
	public static CompanyService getCompanyService() {
		return context.getBean(CompanyService.class);
	}
}
