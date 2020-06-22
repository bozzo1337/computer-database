package com.excilys.computerDB.ui;

import java.io.Console;
import java.time.LocalDate;

import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.service.CompanyService;
import com.excilys.computerDB.service.ComputerService;
import com.excilys.computerDB.service.LoginService;

public class CLI {
	
	private Console console = System.console();
	private LoginService ls = LoginService.getInstance();
	private ComputerService cs = ComputerService.getInstance();
	private CompanyService cas = CompanyService.getInstance();
	private Validator validator = new Validator();
	
	public static void main(String[] args) {
		CLI cli = new CLI();
		cli.console.printf("Système de gestion d'ordinateurs.%nBienvenue, veuillez vous identifier.%n");
		while (!cli.login()) {
			System.err.format("Erreur de connexion, veuillez réessayer...%n>");
		}
		cli.console.printf("Connexion OK%n");
		cli.console.printf("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, quit%n>");
		while (true) {
			cli.nextCommand(cli.console.readLine());
		}
	}

	private void nextCommand(String command) {
		switch(command) {
		case "help":
			commandHelp();
			console.printf("%n>");
			break;
		case "computers":
			commandComputers();
			console.printf("%n>");
			break;
		case "companies":
			commandCompanies();
			console.printf("%n>");
			break;
		case "computer":
			commandComputer();
			console.printf("%n>");
			break;
		case "create":
			commandCreate();
			console.printf("%n>");
			break;
		case "update":
			commandUpdate();
			console.printf("%n>");
			break;
		case "delete":
			commandDelete();
			console.printf("%n>");
			break;
		case "quit":
			commandQuit();
			break;
		default:
			console.printf("Commande non reconnue, tapez 'help' pour plus d'informations.%n>");
		}
	}
	
	private boolean login() {
		String login = null;
		String password = null;
		console.printf("Login :%n>");
		login = console.readLine();
		console.printf("Password:%n>");
		password = new String(console.readPassword());
		console.printf("Connexion...%n");
		return ls.login(login, password);
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
		console.printf("quit : Quitte le programme.");
	}

	private void commandComputers() {
		String input;
		cs.resetPages();
		console.printf(cs.selectAll().toString());
		console.printf("Page suivante : n, Page précédente : p, Quitter : q%n>");
		while (!(input = console.readLine()).equals("q")) {
			switch (input) {
			case "n":
				console.printf(cs.getNextPage().toString());
				break;
			case "p":
				console.printf(cs.getPreviousPage().toString());
				break;
			default:
				console.printf("Commande non reconnue.%n");
			}
			console.printf("Page suivante : n, Page précédente : p, Quitter : q%n>");
		}
	}
	
	private void commandCompanies() {
		cas.resetPages();
		console.printf(cas.selectAll().toString());
	}
	
	private void commandComputer() {
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = validator.validateID(console.readLine());
		Computer comp;
		if (idRead != null && (comp = cs.selectById(idRead)) != null) {
			console.printf(comp.toString());
		} else {
			console.printf("Aucun résultat.");
		}
	}
	
	private void commandCreate() {
		console.printf("Création d'un nouvel ordinateur :%n");
		console.printf("Nom (requis) :%n>");
		String name = console.readLine();
		if (!validator.validateName(name)) {
			System.err.format("Nom requis ! Retour à l'accueil.");
			return;
		}
		console.printf("LocalDate intro (jj/mm/aaaa) :%n>");
		LocalDate intro = validator.validateDate(console.readLine());
		console.printf("LocalDate disc (>= intro) :%n>");
		LocalDate disc = validator.validateDate(console.readLine());
		if (!validator.validateTemporality(intro, disc)) {
			System.err.format("Erreur de temporalité ! Retour à l'accueil.");
			return;
		}
		console.printf("ID de l'entreprise :%n>");
		Long compId = validator.validateID(console.readLine());
		Computer newComp = new Computer(name, intro, disc, compId);
		cs.create(newComp);
	}
	
	private void commandUpdate() {
		Computer compToUpdate = new Computer();
		console.printf("Mise à jour d'un ordinateur :%n");
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = validator.validateID(console.readLine());
		if (idRead != null) {
			compToUpdate = cs.selectById(idRead);
		} else {
			console.printf("Aucun résultat.");
			return;
		}
		String field = "";
		String newName = compToUpdate.getName();
		do {
			LocalDate newDate = null;
			console.printf("Quel champ modifier ? Entrez 'ok' pour terminer.%n>");
			field = console.readLine();
			switch (field) {
			case "id":
				System.err.format("Impossible de modifier ce champ !%n");
				break;
			case "name":
				console.printf("Nouveau nom :%n>");
				newName = console.readLine();
				if (validator.validateName(newName)) {
					compToUpdate.setName(newName);
				} else {
					System.err.format("Nom vide impossible !%n");
				}
				break;
			case "introduced":
				console.printf("Nouvelle date de mise en service (jj/mm/aaaa) :%n>");
				newDate = validator.validateDate(console.readLine());
				if (newDate != null && validator.validateTemporality(newDate, compToUpdate.getDiscontinued())) {
					compToUpdate.setIntroduced(newDate);
				}
				break;
			case "discontinued":
				console.printf("Nouvelle date de mise hors service (jj/mm/aaaa) :%n>");			
				newDate = validator.validateDate(console.readLine());
				if (newDate != null && validator.validateTemporality(compToUpdate.getIntroduced(), newDate)) {
					compToUpdate.setDiscontinued(newDate);
				}
				break;
			case "company_id":
				console.printf("Nouvel identifiant d'entreprise :%n>");
				Long newCompId = validator.validateID(console.readLine());
				compToUpdate.setCompanyId(newCompId);
				break;
			case "ok":
				console.printf("Modification(s) terminée(s).%n");
				break;
			default:
				console.printf("Champ non reconnu.%n");
			}
			console.printf(compToUpdate.toString() + "%n");
		} while (!field.equals("ok"));
		cs.update(compToUpdate);
	}
	
	private void commandDelete() {
		console.printf("Suppression d'un ordinateur :%n");
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = validator.validateID(console.readLine());
		if (idRead != null) {
			Computer compToDelete = cs.selectById(idRead);
			console.printf("Ordinateur sélectionné :%n");
			console.printf(compToDelete.toString());
			console.printf("%nConfirmation de la suppression ? (y/N)%n>");
			if (console.readLine().equals("y")) {
				cs.delete(compToDelete);
			}
		} else {
			console.printf("Aucun résultat.");
			return;
		}
	}
	
	private void commandQuit() {
		console.printf("Fermeture de la connexion...%n");
		if (ls.quit()) {
			console.printf("Fin de connexion OK, au revoir !%n");
			System.exit(0);
		} else {
			console.printf("Echec de la fermeture, au revoir quand même !%n");
			System.exit(1);
		}
		ls.quit();
	}
}
