package com.excilys.cdb.dao;

public enum HQLRequest {

	SELECT_ONE(
			"SELECT new Computer(computer.id, computer.name, computer.introduced, computer.discontinued, computer.companyId, "
					+ "company.name) FROM Computer as computer LEFT JOIN Company as company ON "
					+ "computer.companyId = company.id WHERE computer.id = :id"),
	SELECT_BATCH("SELECT new Computer(computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.companyId, company.name) FROM Computer as computer LEFT JOIN Company as company ON "
			+ "computer.companyId = company.id"),
	SEARCH_BATCH("SELECT new Computer(computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.companyId, company.name) FROM Computer as computer LEFT JOIN Company as company ON "
			+ "computer.companyId = company.id WHERE computer.name LIKE :search OR company.name LIKE :search ORDER BY CASE "
			+ "WHEN computer.name LIKE :exact OR company.name LIKE :exact THEN 0 "
			+ "WHEN computer.name LIKE :start OR company.name LIKE :start THEN 1 "
			+ "WHEN computer.name LIKE :search OR company.name LIKE :search THEN 3 "
			+ "WHEN computer.name LIKE :end OR company.name LIKE :end THEN 2 " + "ELSE 4 end"),
	INSERT_COMPUTER("INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);"),
	UPDATE_COMPUTER("UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;"),
	DELETE_COMPUTER("DELETE FROM computer WHERE id=?;"),
	COUNT_COMPUTER("SELECT COUNT(id) AS count FROM computer;"),
	COUNT_SEARCH("SELECT COUNT(computer.id) AS count FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? ;"),
	ORDER("SELECT new Computer(computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.companyId, company.name) FROM Computer as computer LEFT JOIN Company as company ON "
			+ "computer.companyId = company.id ORDER BY %s"),
	SEARCH_ORDER("SELECT new Computer(computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.companyId, company.name) FROM Computer as computer LEFT JOIN Company as company ON "
			+ "computer.companyId = company.id WHERE computer.name LIKE :search OR company.name LIKE :search "
			+ "ORDER BY %s"),
	DELETE_COMPUTERS_OF_COMPANY("DELETE FROM computer WHERE company_id=?;"),
	SELECT_COMPANIES("SELECT company.id, company.name FROM company ORDER BY company.name;"),
	SELECT_ONE_COMPANY("SELECT * FROM company WHERE id=?;"),
	SELECT_BATCH_COMPANY("SELECT * FROM company LIMIT ?, ?;"),
	COUNT_COMPANY("SELECT COUNT(id) AS count FROM company;"),
	SELECT_COMPUTERS_IN_COMPANY("SELECT computer.id FROM computer WHERE computer.company_id=?;"),
	DELETE_COMPANY("DELETE FROM company WHERE id=?;");

	private String request;

	private HQLRequest(String request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return request;
	}

}
