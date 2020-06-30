package com.excilys.cdb.dto;

public class DTOComputer {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;
	private String companyName;
	
	public DTOComputer(String name, String introduced, String discontinued, String companyId) {
		this.name = name.trim();
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}
	
	public DTOComputer(String id, String name, String introduced, String discontinued, String companyId, String companyName) {
		this.id = id;
		this.name = name.trim();
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.companyName = companyName;
	}
	
	public DTOComputer(String id, String name, String introduced, String discontinued, String companyId) {
		this.id = id;
		this.name = name.trim();
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
	}
	
	public String getName() {
		return name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public String getCompanyId() {
		return companyId;
	}

	public String getId() {
		return id;
	}
	
	public String getCompanyName() {
		return companyName;
	}
}
