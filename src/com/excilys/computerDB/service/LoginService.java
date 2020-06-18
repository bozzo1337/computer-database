package com.excilys.computerDB.service;

import java.io.Console;

import com.excilys.computerDB.persistence.QueryExecutor;

public class LoginService {
	
	private Console console;
	private QueryExecutor qe = QueryExecutor.getInstance();
	
	public LoginService(Console console) {
		this.console = console;
	}
	
	public void login() {
		String login = "";
		String password = "";
		int resultConn = -1;
		do {
			switch (resultConn) {
			case 1:
				System.err.format("Erreur de connexion, veuillez réessayer...%n>");
				break;
			default:
			}
			console.printf("Login :%n>");
			login = console.readLine();
			console.printf("Password :%n>");
			password = new String(console.readPassword());
			console.printf("Connexion à la base...%n");
		} while ((resultConn = qe.initConn(login, password)) != 0);
		password = "";
		console.printf("Connexion OK%n");
	}
	
	public void quit() {
		console.printf("Fermeture de la connexion...%n");
		if (qe.closeConn() == 0) {
			console.printf("Fin de connexion OK, au revoir !%n");
			System.exit(0);
		} else {
			console.printf("Echec de la fermeture, au revoir quand même !%n");
			System.exit(1);
		}
	}
}
