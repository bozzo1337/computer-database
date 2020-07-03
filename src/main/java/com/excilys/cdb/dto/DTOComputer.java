package com.excilys.cdb.dto;

public class DTOComputer {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyId;
	private String companyName;
	
	public static class Builder {
		private String id;
		private String name;
		private String introduced;
		private String discontinued;
		private String companyId;
		private String companyName;
		
		public Builder withId(String id) {
			this.id = id;
			return this;
		}
		
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder withIntroDate(String introduced) {
			this.introduced = introduced;
			return this;
		}
		
		public Builder withDiscDate(String discontinued) {
			this.discontinued = discontinued;
			return this;
		}
		
		public Builder withCompanyId(String companyId) {
			this.companyId = companyId;
			return this;
		}
		
		public Builder withCompanyName(String companyName) {
			this.companyName = companyName;
			return this;
		}
		
		public DTOComputer build() {
			DTOComputer computerDTO = new DTOComputer();
			computerDTO.id = this.id;
			computerDTO.name = this.name;
			computerDTO.introduced = this.introduced;
			computerDTO.discontinued = this.discontinued;
			computerDTO.companyId = this.companyId;
			computerDTO.companyName = this.companyName;
			return computerDTO;
		}
	}
	
	private DTOComputer() {
		
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
