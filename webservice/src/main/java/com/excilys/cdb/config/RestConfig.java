package com.excilys.cdb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(basePackages = {"com.excilys.cdb.rest"})
public class RestConfig {
	public static final String COMPUTERS_ENDPOINT = "/computers";
	public static final String COMPANIES_ENDPOINT = "/companies";
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
