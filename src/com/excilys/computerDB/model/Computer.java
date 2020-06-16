package com.excilys.computerDB.model;

import java.sql.Date;

public class Computer {
	
	private Long id;
	private String name;
	private Date introduced;
	private Date discontinued;
	private Long companyId;
	
	public Computer() {
		id = null;
		name = null;
		introduced = null;
		discontinued = null;
		companyId = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIntroduced() {
		return introduced;
	}

	public void setIntroduced(Date introduced) {
		this.introduced = introduced;
	}

	public Date getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(Date discontinued) {
		this.discontinued = discontinued;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
