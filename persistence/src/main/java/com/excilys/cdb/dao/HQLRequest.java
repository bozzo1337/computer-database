package com.excilys.cdb.dao;

public enum HQLRequest {

	SELECT_ONE("FROM Computer WHERE id = :id"),
	SELECT_BATCH("FROM Computer"),
	SEARCH_BATCH("FROM Computer as computer LEFT JOIN FETCH computer.company "
			+ "WHERE computer.name LIKE :search OR computer.company.name LIKE :search ORDER BY CASE "
			+ "WHEN computer.name LIKE :exact OR computer.company.name LIKE :exact THEN 0 "
			+ "WHEN computer.name LIKE :start OR computer.company.name LIKE :start THEN 1 "
			+ "WHEN computer.name LIKE :search OR computer.company.name LIKE :search THEN 3 "
			+ "WHEN computer.name LIKE :end OR computer.company.name LIKE :end THEN 2 " + "ELSE 4 end"),
	COUNT_COMPUTER("SELECT COUNT(id) FROM Computer"),
	COUNT_SEARCH(
			"SELECT COUNT(computer.id) FROM Computer as computer WHERE computer.name LIKE :search OR computer.company.name LIKE :search"),
	ORDER("FROM Computer as computer LEFT JOIN FETCH computer.company ORDER BY %s"),
	SEARCH_ORDER(
			"FROM Computer as computer LEFT JOIN FETCH computer.company WHERE computer.name LIKE :search OR computer.company.name LIKE :search ORDER BY %s"),
	SELECT_COMPANIES("FROM Company ORDER BY name"),
	SELECT_ONE_COMPANY("FROM Company WHERE id = :id"),
	SELECT_BATCH_COMPANY("FROM Company"),
	COUNT_COMPANY("SELECT COUNT(id) FROM Company"),
	SELECT_COMPUTERS_IN_COMPANY(
			"FROM Computer as computer LEFT JOIN FETCH computer.company WHERE computer.company.id = :companyId");

	private String request;

	private HQLRequest(String request) {
		this.request = request;
	}

	@Override
	public String toString() {
		return request;
	}

}
