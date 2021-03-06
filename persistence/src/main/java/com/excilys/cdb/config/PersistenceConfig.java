package com.excilys.cdb.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.dao", "com.excilys.cdb.connector" })
public class PersistenceConfig {
}
