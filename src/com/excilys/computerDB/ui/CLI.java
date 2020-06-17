package com.excilys.computerDB.ui;

import java.io.Console;

import com.excilys.computerDB.mapper.ComputerMapper;
import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.model.RequestResult;
import com.excilys.computerDB.persistence.QueryExecutor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CLI {
	
	private static Console console = System.console();
	private static QueryExecutor qe = QueryExecutor.getInstance();
	private static ComputerMapper cm = ComputerMapper.getInstance();
	private static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public static void main(String[] args) {
		boolean autolog = false; //TODO Dev option ONLY -- TO REMOVE
		if (args.length != 0) {
			switch (args[0]) {
			case "nolaunch": //TODO To Compile with Eclipse without NullPointerE from System.console
				System.out.println("No-Launch activated // Remove arg to disable.");
				System.exit(0);
			case "autolog": //TODO Lazy login
				autolog = !autolog;
				break;
			default:		
			}	
		}
		login(autolog);
		console.printf("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, quit%n>");
		for (;;) {
			nextCommand(console.readLine());
		}
	}
	
	private static void login(boolean autolog) {//TODO Remove arg
		String login = "";
		String password = "";
		int resultConn = -1;
		console.printf("Système de gestion d'ordinateurs.%nBienvenue, veuillez vous identifier.%n");
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
			//TODO DEV ONLY -- TO REMOVE
			if (autolog) {
				login = "admincdb";
				password = "qwerty1234";
			}
		} while ((resultConn = qe.initConn(login, password)) != 0);
		password = "";
		console.printf("Connexion OK%n");
	}

	private static void nextCommand(String command) {
		switch(command) {
		case "help":
			commandHelp();
			break;
		case "computers":
			commandComputers();
			break;
		case "companies":
			commandCompanies();
			break;
		case "computer":
			commandComputer();
			break;
		case "create":
			commandCreate();
			break;
		case "update":
			commandUpdate();
			break;
		case "delete":
			commandDelete();
			break;
		case "quit":
			commandQuit();
			break;
		default:
			console.printf("Commande non reconnue, tapez 'help' pour plus d'informations.%n>");
		}
	}
	
	private static void commandHelp() {
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
	
	private static void commandComputers() {
		console.printf(qe.displayComputers() + ">");
	}
	
	private static void commandCompanies() {
		console.printf(qe.displayCompanies() + ">");
	}
	
	private static Long commandComputer() {
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = null;
		RequestResult rr = new RequestResult();
		try {
			idRead = Long.valueOf(console.readLine());
			rr = qe.findComputer(idRead);
			console.printf(rr.toString() + "%n>");
		} catch (NumberFormatException e) {
			System.err.format("Format d'ID invalide.%n>");
		}
		return idRead = (rr.getStatus() == 0 ? idRead : new Long(0));
	}

	private static void commandCreate() {
		console.printf("Création d'un nouvel ordinateur :%n");
		console.printf("Nom (requis) :%n>");
		String name = console.readLine();
		if (name.trim().isEmpty()) {
			System.err.format("Nom requis ! Retour à l'accueil.%n>");
			return;
		}
		console.printf("LocalDate intro (jj/mm/aaaa) :%n>");
		LocalDate intro = null;
		try {
			intro = LocalDate.parse(console.readLine(), df);
		} catch (DateTimeParseException e) {
			System.err.format("Erreur de format. Valeur par défaut attribuée.%n");
		}
		console.printf("LocalDate disc (>intro) :%n>");
		LocalDate disc = null;
		try {
			disc = LocalDate.parse(console.readLine(), df);
		} catch (DateTimeParseException e) {
			System.err.format("Erreur de format. Valeur par défaut attribuée.%n");
		}
		if (intro != null && disc != null) {
			if (disc.isBefore(intro)) {
				disc = null;
				System.err.format("Erreur de temporalité. Valeur de disc par défaut.%n");
			}
		}
		Long compId = null;
		console.printf("ID de l'entreprise :%n>");
		try {
			compId = Long.valueOf(console.readLine());
		} catch (NumberFormatException e) {
			System.err.format("ID invalide, valeur par défaut attribuée.%n");
		}
		console.printf(qe.createComputer(name, intro, disc, compId).toString() + ">");	
	}
	
	private static void commandUpdate() {
		console.printf("Mise à jour d'un ordinateur :%n");
		Long idRead = commandComputer();
		while (idRead == 0) {
			idRead = commandComputer();
		}
		Computer compToUpdate = cm.mapComputer(idRead);
		String field = "";
		String newName = compToUpdate.getName();
		LocalDate newDate = null;
		do {
			console.printf("Quel champ modifier ? Entrez 'ok' pour terminer.%n>");
			field = console.readLine();
			switch (field) {
			case "id":
				System.err.format("Impossible de modifier ce champ !%n");
				break;
			case "name":
				console.printf("Nouveau nom :%n>");
				newName = console.readLine();
				if (!newName.trim().isEmpty()) {
					compToUpdate.setName(newName);
				} else {
					System.err.format("Nom vide impossible !%n");
				}
				break;
			case "introduced":
				console.printf("Nouvelle date de mise en service (jj/mm/aaaa) :%n>");
				try {
					newDate = LocalDate.parse(console.readLine(), df);
				} catch (DateTimeParseException e) {
					System.err.format("Erreur de format.%n");
					break;
				}
				if (compToUpdate.getDiscontinued() != null && newDate.isAfter(compToUpdate.getDiscontinued())) {
					System.err.format("Erreur : Problème de temporalité.%n");
				} else {
					compToUpdate.setIntroduced(newDate);
				}
				break;
			case "discontinued":
				console.printf("Nouvelle date de mise hors service (jj/mm/aaaa) :%n>");			
				try {
					newDate = LocalDate.parse(console.readLine(), df);
				} catch (DateTimeParseException e) {
					System.err.format("Erreur de format.%n");
					break;
				}
				if (compToUpdate.getIntroduced() != null && newDate.isBefore(compToUpdate.getIntroduced())) {
					System.err.format("Erreur : Problème de temporalité.%n");
				} else {
					compToUpdate.setDiscontinued(newDate);
				}
				break;
			case "company_id":
				console.printf("Nouvel identifiant d'entreprise :%n>");
				try {
					compToUpdate.setCompanyId(Long.valueOf(console.readLine()));
				} catch (NumberFormatException e) {
					console.printf("ID invalide.%n");
				}
				break;
			case "ok":
				console.printf("Modification(s) terminée(s).%n");
				break;
			default:
				console.printf("Champ non reconnu.%n");
			}
			console.printf(compToUpdate.toString() + "%n");
		} while (!field.equals("ok"));
		console.printf(cm.updateComputer(compToUpdate).toString() + ">");
		
	}
	
	private static void commandDelete() {
		console.printf("Suppression d'un ordinateur :%n");
		console.printf("id :%n>");
		String id = console.readLine();
		if(qe.deleteComputer(id) == 0) {
			console.printf("Suppression réussie.%n>");
		} else {
			console.printf("Echec de la suppression.%n>");
		}
	}
	
	private static void commandQuit() {
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
