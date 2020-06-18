package com.excilys.computerDB.ui;

import java.io.Console;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.service.CompanyService;
import com.excilys.computerDB.service.ComputerService;
import com.excilys.computerDB.service.LoginService;

public class CLI {
	
	private Console console = System.console();
	private LoginService ls = new LoginService();
	private ComputerService cs = new ComputerService();
	private CompanyService cas = new CompanyService();
	
	public static void main(String[] args) {
		CLI cli = new CLI();
		
		cli.console.printf("Système de gestion d'ordinateurs.%nBienvenue, veuillez vous identifier.%n");
		ls.login();
		String login = "";
		String password = "";
		boolean connSuccess = false;
		while (!connSuccess) {
			console.printf("Login :%n>");
			login = console.readLine();
			console.printf("Password :%n>");
			password = new String(console.readPassword());
			console.printf("Connexion à la base...%n");
			connSuccess = qe.initConn(login, password);
			if (!connSuccess) {
				System.err.format("Erreur de connexion, veuillez réessayer...%n>");
			}
		}
		password = "";
		console.printf("Connexion OK%n");
		cli.console.printf("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, quit%n>");
		while (true) {
			cli.nextCommand(cs, cas, ls, cli.console.readLine());
		}
	}

	private void nextCommand(ComputerService cs, CompanyService cas, LoginService ls, String command) {
		switch(command) {
		case "help":
			commandHelp();
			break;
		case "computers":
			String input = "";
			pageComp.init();
			do {
				pageComp.fill();
				console.printf("Page n°" + (pageComp.getIdxPage() + 1) + "/" + (pageComp.getIdxMaxPage() + 1) + "%n");
				console.printf(pageComp.toString());
				console.printf("Page suivante : n, Page précédente : p, Quitter : q%n>");
				switch (input = console.readLine()) {
				case "n":
					pageComp.nextPage();
					break;
				case "p":
					pageComp.previousPage();
					break;
				case "q":
					break;
				default:
					console.printf("Commande non reconnue.%n");
				}
			} while (!input.equals("q"));
			console.printf(">");
			cs.selectAll();
			break;
		case "companies":
			cas.selectAll();
			break;
		case "computer":
			console.printf("Sélection d'un ordinateur par ID :%n>");
			Long idRead = null;
			try {
				idRead = Long.valueOf(console.readLine());
			} catch (NumberFormatException e) {
				System.err.format("Format d'ID invalide.%n>");
				return;
			}
			
			cs.selectById();
			break;
		case "create":
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
			console.printf("LocalDate disc (>= intro) :%n>");
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
			cs.create();
			break;
		case "update":
			console.printf("Mise à jour d'un ordinateur :%n");
			Long idRead = null;
			do {
				console.printf("Sélection d'un ordinateur par ID :%n>");
				try {
					idRead = Long.valueOf(console.readLine());
				} catch (NumberFormatException e) {
					System.err.format("Format d'ID invalide.%n>");
				}
			} while ();
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
			cs.update();
			break;
		case "delete":
			console.printf("Suppression d'un ordinateur :%n");
			console.printf("Sélection d'un ordinateur par ID :%n>");
			Long idRead = null;
			try {
				idRead = Long.valueOf(console.readLine());
			} catch (NumberFormatException e) {
				System.err.format("Format d'ID invalide.%n>");
				return;
			}
			console.printf("Ordinateur sélectionné :%n");
			console.printf("Confirmation de la suppression ? (y/n)%n>");
			cs.delete();
			break;
		case "quit":
			console.printf("Fermeture de la connexion...%n");
			if (qe.closeConn()) {
				console.printf("Fin de connexion OK, au revoir !%n");
				System.exit(0);
			} else {
				console.printf("Echec de la fermeture, au revoir quand même !%n");
				System.exit(1);
			}
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
