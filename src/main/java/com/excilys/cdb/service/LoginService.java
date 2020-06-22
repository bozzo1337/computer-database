package com.excilys.computerDB.service;

import com.excilys.computerDB.persistence.DBConnector;

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
	
	public boolean login(String login, String password) {
		return DBConnector.getInstance().initConn(login, password);
	}
	
	public boolean quit() {
		return DBConnector.getInstance().closeConn();
	}
}
