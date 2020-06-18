package com.excilys.computerDB.ui;

import java.io.Console;

import com.excilys.computerDB.service.CompanyService;
import com.excilys.computerDB.service.ComputerService;
import com.excilys.computerDB.service.LoginService;

public class CLI {
	
	private Console console = System.console();
	
	public static void main(String[] args) {
		CLI cli = new CLI();
		LoginService ls = new LoginService(cli.console);
		ComputerService cs = new ComputerService(cli.console);
		CompanyService cas = new CompanyService(cli.console);
		cli.console.printf("Système de gestion d'ordinateurs.%nBienvenue, veuillez vous identifier.%n");
		ls.login();
		cli.console.printf("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, quit%n>");
		for (;;) {
			cli.nextCommand(cs, cas, ls, cli.console.readLine());
		}
	}

	private void nextCommand(ComputerService cs, CompanyService cas, LoginService ls, String command) {
		switch(command) {
		case "help":
			commandHelp();
			break;
		case "computers":
			cs.selectAll();
			break;
		case "companies":
			cas.selectAll();
			break;
		case "computer":
			cs.selectById();
			break;
		case "create":
			cs.create();
			break;
		case "update":
			cs.update();
			break;
		case "delete":
			cs.delete();
			break;
		case "quit":
			ls.quit();
			break;
		default:
			console.printf("Commande non reconnue, tapez 'help' pour plus d'informations.%n>");
		}
	}
	
	private void commandHelp() {
		console.printf("Commandes disponibles : %n");
		console.printf("help : Affiche la liste des commandes détaillées.%n");
		console.printf("computers : Affiche la liste des ordinateurs.%n");
		console.printf("companies : Affiche la liste des entreprises.%n");
		console.printf("computer : Affiche les détails de l'ordinateur demandé.%n");
		console.printf("create : Crée l'ordinateur indiqué.%n");
		console.printf("update : Met à jour l'ordinateur ciblé.%n");
		console.printf("delete : Supprime l'ordinateur ciblé.%n");
		console.printf("quit : Quitte le programme.%n>");
	}

	
}
