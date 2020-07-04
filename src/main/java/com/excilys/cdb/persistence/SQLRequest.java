package com.excilys.cdb.persistence;

public enum SQLRequest {

	
	SELECT_ONE("SELECT (computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.company_id, company.id, company.name) FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id WHERE computer.id = ?;"),
	SELECT_BATCH("SELECT (computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.company_id, company.id, company.name) FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id LIMIT ?, ?;"),
	SEARCH_BATCH("SELECT (computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.company_id, company.id, company.name) FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? OR company.name LIKE ? ORDER BY CASE "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 0 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 1 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 3 "
			+ "WHEN computer.name LIKE ? OR company.name LIKE ? THEN 2 "
			+ "ELSE 4 "
			+ "END LIMIT ?, ? ;"),
	INSERT_COMPUTER("INSERT INTO computer(name, introduced, discontinued, company_id) VALUES(?, ?, ?, ?);"),
	UPDATE_COMPUTER("UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?;"),
	DELETE_COMPUTER("DELETE FROM computer WHERE id=?;"),
	COUNT_COMPUTER("SELECT COUNT(id) AS count FROM computer;"),
	COUNT_SEARCH("SELECT COUNT(computer.id) AS count FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? ;"),
	ORDER("SELECT (computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.company_id, company.id, company.name) FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id ORDER BY %s LIMIT ?, ?;"),
	SEARCH_ORDER("SELECT (computer.id, computer.name, computer.introduced, computer.discontinued, "
			+ "computer.company_id, company.id, company.name) FROM computer LEFT JOIN company ON "
			+ "computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ? "
			+ "ORDER BY %s LIMIT ?, ?;"),
	DELETE_COMPUTERS_OF_COMPANY("DELETE FROM computer WHERE company_id=?;"),
	SELECT_COMPANIES("SELECT (company.id, company.name) FROM company;"),
	SELECT_ONE_COMPANY("SELECT * FROM company WHERE id=?;"),
	SELECT_BATCH_COMPANY("SELECT * FROM company LIMIT ?, ?;"),
	COUNT_COMPANY("SELECT COUNT(id) AS count FROM company;"),
	SELECT_COMPUTERS_IN_COMPANY("SELECT computer.id FROM computer WHERE computer.company_id=?;"),
	DELETE_COMPANY("DELETE FROM company WHERE id=?;");
		
	private String request;
	
	private SQLRequest(String request) {
		this.request = request;
	}
	
	@Override
	public String toString() {
		return request;
	}

}
