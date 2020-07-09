package com.excilys.cdb.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.excilys.cdb.dao", "com.excilys.cdb.service", "com.excilys.cdb.connector"})
public class AppConfig {
}
