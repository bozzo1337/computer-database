package com.excilys.cdb.model;

import java.time.LocalDate;

public class Computer {
	
	private Long id;
	private String name;
	private LocalDate introduced;
	private LocalDate discontinued;
	private Long companyId;
	private Company company;
	
	public Computer() {
	}
	
	public Computer(String name, LocalDate introduced, LocalDate discontinued, Long companyId) {
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.company = null;
	}
	
	public Computer(String name, LocalDate introduced, LocalDate discontinued, Long companyId, Company company) {
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.company = company;
	}
	
	public Computer(Long id, String name, LocalDate introduced, LocalDate discontinued, Long companyId) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}

	public Computer(Long id, String name, LocalDate introduced, LocalDate discontinued, Long companyId, Company company) {
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.company = company;
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

	public LocalDate getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}

	public LocalDate getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		return id + " | " + name + " | " + introduced + " | " +
				discontinued + " | " + companyId;
	}
	
	@Override
	public boolean equals(Object otherComp) {
		if (otherComp == this) {
			return true;
		}
		if (otherComp == null || otherComp.getClass() != this.getClass()) {
			return false;
		}
		Computer otherComputer = (Computer) otherComp;
		return otherComputer.getId().equals(this.id) &&
				otherComputer.getName().equals(this.name) &&
				otherComputer.getIntroduced().equals(this.introduced) &&
				otherComputer.getDiscontinued().equals(this.discontinued) &&
				otherComputer.getCompanyId().equals(this.companyId);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
