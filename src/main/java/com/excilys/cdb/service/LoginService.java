package com.excilys.cdb.service;

import com.excilys.cdb.persistence.DBConnector;

public class LoginService {
	
	private static LoginService singleInstance = null;
	
	private LoginService() {
	}
	
	public static LoginService getInstance() {
		if (singleInstance == null) {
			singleInstance = new LoginService();
		}
		return singleInstance;
	}
	
	public boolean login() {
		return DBConnector.getInstance().initConn();
	}
	
	public boolean quit() {
		return DBConnector.getInstance().closeConn();
	}
}
