package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.dao", "com.excilys.cdb.service",
		"com.excilys.cdb.config", "com.excilys.cdb.controller", "com.excilys.cdb.controller.attributes" })
public class AppConfig {

}
