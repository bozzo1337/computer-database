package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.service.LoginService;
import com.excilys.cdb.validation.Validator;

public class CLI {
	
	private LoginService ls = LoginService.getInstance();
	private ComputerService cs = ComputerService.getInstance();
	private CompanyService cas = CompanyService.getInstance();
	private Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		CLI cli = new CLI();
		System.out.format("Système de gestion d'ordinateurs.%nBienvenue, veuillez vous identifier.%n");
		while (!cli.login()) {
			System.err.format("Erreur de connexion, veuillez réessayer...%n");
		}
		System.out.format("Connexion OK%n");
		System.out.format("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, deleteCompany, quit%n>");
		while (true) {
			cli.nextCommand(cli.in.next());
		}
	}

	private void nextCommand(String command) {
		switch(command) {
		case "help":
			commandHelp();
			System.out.format("%n>");
			break;
		case "computers":
			commandComputers();
			System.out.format("%n>");
			break;
		case "companies":
			commandCompanies();
			System.out.format("%n>");
			break;
		case "computer":
			commandComputer();
			System.out.format("%n>");
			break;
		case "create":
			commandCreate();
			System.out.format("%n>");
			break;
		case "update":
			commandUpdate();
			System.out.format("%n>");
			break;
		case "delete":
			commandDelete();
			System.out.format("%n>");
			break;
		case "deleteCompany":
			commandDeleteCompany();
			System.out.format("%n>");
			break;
		case "quit":
			commandQuit();
			break;
		default:
			System.out.format("Commande non reconnue, tapez 'help' pour plus d'informations.%n>");
		}
	}
	
	private boolean login() {
		System.out.format("Connexion...%n");
		return ls.login();
	}
	
	private void commandHelp() {
		System.out.format("Commandes disponibles : %n");
		System.out.format("help : Affiche la liste des commandes détaillées.%n");
		System.out.format("computers : Affiche la liste des ordinateurs.%n");
		System.out.format("companies : Affiche la liste des entreprises.%n");
		System.out.format("computer : Affiche les détails de l'ordinateur demandé.%n");
		System.out.format("create : Crée l'ordinateur indiqué.%n");
		System.out.format("update : Met à jour l'ordinateur ciblé.%n");
		System.out.format("delete : Supprime l'ordinateur ciblé.%n");
		System.out.format("deleteCompany : Supprime l'entreprise ainsi que tous les ordinateurs liés.%n");
		System.out.format("quit : Quitte le programme.");
	}

	private void commandComputers() {
		String input;
		cs.resetPages(null);
		System.out.println(cs.selectAll().toString());
		System.out.format("Page suivante : n, Page précédente : p, Quitter : q%n>");
		while (!(input = in.next()).equals("q")) {
			switch (input) {
			case "n":
				cs.nextPage();
				System.out.format(cs.selectAll().toString());
				break;
			case "p":
				cs.previousPage();
				System.out.format(cs.selectAll().toString());
				break;
			default:
				System.out.format("Commande non reconnue.%n");
			}
			System.out.format("Page suivante : n, Page précédente : p, Quitter : q%n>");
		}
	}
	
	private void commandCompanies() {
		cas.resetPages();
		System.out.format(cas.selectPage().toString());
	}
	
	private void commandComputer() {
		System.out.format("Sélection d'un ordinateur par ID :%n>");
		Long idRead = Validator.validateID(in.next());
		Computer comp;
		if (idRead != null && (comp = cs.selectById(idRead)) != null) {
			System.out.format(comp.toString());
		} else {
			System.out.format("Aucun résultat.");
		}
	}
	
	private void commandCreate() {
		System.out.format("Création d'un nouvel ordinateur :%n");
		System.out.format("Nom (requis) :%n>");
		String name = in.next();
		if (!Validator.validateName(name)) {
			System.err.format("Nom requis ! Retour à l'accueil.");
			return;
		}
		System.out.format("LocalDate intro (jj/mm/aaaa) :%n>");
		LocalDate intro = Validator.validateDate(in.next());
		System.out.format("LocalDate disc (>= intro) :%n>");
		LocalDate disc = Validator.validateDate(in.next());
		if (!Validator.validateTemporality(intro, disc)) {
			System.err.format("Erreur de temporalité ! Retour à l'accueil.");
			return;
		}
		System.out.format("ID de l'entreprise :%n>");
		Long compId = Validator.validateID(in.next());
		Computer newComp = new Computer(name, intro, disc, compId, null);
		cs.create(newComp);
	}
	
	private void commandUpdate() {
		Computer compToUpdate = new Computer();
		System.out.format("Mise à jour d'un ordinateur :%n");
		System.out.format("Sélection d'un ordinateur par ID :%n>");
		Long idRead = Validator.validateID(in.next());
		if (idRead != null) {
			compToUpdate = cs.selectById(idRead);
		} else {
			System.out.format("Aucun résultat.");
			return;
		}
		String field = "";
		String newName = compToUpdate.getName();
		do {
			LocalDate newDate = null;
			System.out.format("Quel champ modifier ? Entrez 'ok' pour terminer.%n>");
			field = in.next();
			switch (field) {
			case "id":
				System.err.format("Impossible de modifier ce champ !%n");
				break;
			case "name":
				System.out.format("Nouveau nom :%n>");
				newName = in.next();
				if (Validator.validateName(newName)) {
					compToUpdate.setName(newName);
				} else {
					System.err.format("Nom vide impossible !%n");
				}
				break;
			case "introduced":
				System.out.format("Nouvelle date de mise en service (jj/mm/aaaa) :%n>");
				newDate = Validator.validateDate(in.next());
				if (newDate != null && Validator.validateTemporality(newDate, compToUpdate.getDiscontinued())) {
					compToUpdate.setIntroduced(newDate);
				}
				break;
			case "discontinued":
				System.out.format("Nouvelle date de mise hors service (jj/mm/aaaa) :%n>");			
				newDate = Validator.validateDate(in.next());
				if (newDate != null && Validator.validateTemporality(compToUpdate.getIntroduced(), newDate)) {
					compToUpdate.setDiscontinued(newDate);
				}
				break;
			case "company_id":
				System.out.format("Nouvel identifiant d'entreprise :%n>");
				Long newCompId = Validator.validateID(in.next());
				compToUpdate.setCompanyId(newCompId);
				break;
			case "ok":
				System.out.format("Modification(s) terminée(s).%n");
				break;
			default:
				System.out.format("Champ non reconnu.%n");
			}
			System.out.format(compToUpdate.toString() + "%n");
		} while (!field.equals("ok"));
		cs.update(compToUpdate);
	}
	
	private void commandDelete() {
		System.out.format("Suppression d'un ordinateur :%n");
		System.out.format("Sélection d'un ordinateur par ID :%n>");
		Long idRead = Validator.validateID(in.next());
		if (idRead != null) {
			Computer compToDelete = cs.selectById(idRead);
			System.out.format("Ordinateur sélectionné :%n");
			System.out.format(compToDelete.toString());
			System.out.format("%nConfirmation de la suppression ? (y/N)%n>");
			if (in.next().equals("y")) {
				cs.delete(compToDelete);
			}
		} else {
			System.out.format("Aucun résultat.");
			return;
		}
	}
	
	private void commandDeleteCompany() {
		System.out.format("Suppression d'une entreprise :%n");
		System.out.format("Sélection d'une entreprise par ID :%n>");
		Long idRead = Validator.validateID(in.next());
		if (idRead != null) {
			Company compToDelete = cas.selectById(idRead);
			System.out.format("Entreprise sélectionné :%n");
			System.out.format(compToDelete.toString());
			System.out.format("%nConfirmation de la suppression ? (y/N)%n>");
			if (in.next().equals("y")) {
				cas.delete(compToDelete);
			}
		} else {
			System.out.format("Aucun résultat.");
			return;
		}
	}
	
	private void commandQuit() {
		System.out.format("Fermeture de la connexion...%n");
		in.close();
		if (ls.quit()) {
			System.out.format("Fin de connexion OK, au revoir !%n");
			System.exit(0);
		} else {
			System.out.format("Echec de la fermeture, au revoir quand même !%n");
			System.exit(1);
		}
		ls.quit();
	}
}
