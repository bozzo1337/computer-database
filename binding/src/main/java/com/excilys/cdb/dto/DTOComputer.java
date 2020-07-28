package com.excilys.cdb.dto;

public class DTOComputer {

	private String id;
	private String name;
	private String introduced;
	private String discontinued;
	private DTOCompany companyDTO;

	public static class Builder {
		private String id;
		private String name;
		private String introduced;
		private String discontinued;
		private DTOCompany companyDTO;

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

		public Builder withCompanyDTO(DTOCompany companyDTO) {
			this.companyDTO = companyDTO;
			return this;
		}

		public DTOComputer build() {
			DTOComputer computerDTO = new DTOComputer();
			computerDTO.id = this.id;
			computerDTO.name = this.name;
			computerDTO.introduced = this.introduced;
			computerDTO.discontinued = this.discontinued;
			computerDTO.companyDTO = this.companyDTO;
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

	public String getId() {
		return id;
	}

	public DTOCompany getCompanyDTO() {
		return companyDTO;
	}

	@Override
	public String toString() {
		return id + " | " + name + " | " + introduced + " | " + discontinued + " | " + companyDTO.getId() + " | " + companyDTO.getName();
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public void setCompanyName(DTOCompany companyDTO) {
		this.companyDTO = companyDTO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyDTO == null) ? 0 : companyDTO.hashCode());
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTOComputer other = (DTOComputer) obj;
		if (companyDTO == null) {
			if (other.companyDTO != null)
				return false;
		} else if (!companyDTO.equals(other.companyDTO))
			return false;
		if (discontinued == null) {
			if (other.discontinued != null)
				return false;
		} else if (!discontinued.equals(other.discontinued))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (introduced == null) {
			if (other.introduced != null)
				return false;
		} else if (!introduced.equals(other.introduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
