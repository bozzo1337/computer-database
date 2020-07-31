package com.excilys.cdb.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.register(CoreConfig.class, BindingConfig.class, PersistenceConfig.class, ServiceConfig.class,
				WebConfig.class, HibernateConfig.class, RestConfig.class, SecurityConfig.class,
				SecurityInitializer.class);
		ac.setServletContext(servletContext);
		servletContext.addListener(new ContextLoaderListener(ac));

		DispatcherServlet servlet = new DispatcherServlet(ac);
		ServletRegistration.Dynamic registration = servletContext.addServlet("dashboard", servlet);
		registration.setLoadOnStartup(1);
		registration.addMapping("/");
	}
}
