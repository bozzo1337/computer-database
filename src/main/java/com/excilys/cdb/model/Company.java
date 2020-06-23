package com.excilys.cdb.model;

public class Company {
	
	private Long id;
	private String name;
	
	public Company() {
	}
	
	public Company(Long id, String name) {
		this.id = id;
		this.name = name;
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
	
	@Override
	public String toString() {
		return this.id + " | " + this.name;
	}
	
	@Override
	public boolean equals(Object otherComp) {
		if (otherComp == this) {
			return true;
		}
		if (otherComp == null || otherComp.getClass() != this.getClass()) {
			return false;
		}
		Company otherCompany = (Company) otherComp;
		return otherCompany.getId().equals(this.id) && otherCompany.getName().equals(this.name);
	}
}
