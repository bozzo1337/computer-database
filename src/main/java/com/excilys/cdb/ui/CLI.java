package com.excilys.cdb.ui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.excilys.cdb.connector.MyDataSource;
import com.excilys.cdb.dto.DTOComputer;
import com.excilys.cdb.exception.validation.IncorrectDiscDateException;
import com.excilys.cdb.exception.validation.IncorrectIDException;
import com.excilys.cdb.exception.validation.IncorrectIntroDateException;
import com.excilys.cdb.exception.validation.IncorrectNameException;
import com.excilys.cdb.exception.validation.IncorrectTemporalityException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;
import com.excilys.cdb.validation.Validator;

public class CLI {

	private static final Logger LOGGER = LoggerFactory.getLogger(CLI.class);
	private MyDataSource DBC;
	private ComputerService cs;
	private CompanyService cas;
	private Scanner in = new Scanner(System.in);

	@Autowired
	public CLI(MyDataSource dbc, CompanyService cas, ComputerService cs) {
		this.cas = cas;
		this.cs = cs;
		this.DBC = dbc;
	}
	
	public void run() {
		System.out.format("Système de gestion d'ordinateurs.%nConnexion...%n");
		try {
			if (DBC.getConnection() == null) {
				System.err.format("Erreur de connexion...%n");
				LOGGER.error("Connection failed");
				return;
			}
		} catch (SQLException e) {
			LOGGER.error("Connection failed due to Exception");
		}
		System.out.format("Connexion OK, Bienvenue !%n");
		System.out.format(
				"Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, deleteCompany, quit%n>");
		while (true) {
			nextCommand(in.next());
		}
	}

	private void nextCommand(String command) {
		switch (command) {
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
				System.out.println(cs.selectAll().toString());
				break;
			case "p":
				cs.previousPage();
				System.out.println(cs.selectAll().toString());
				break;
			default:
				System.out.format("Commande non reconnue.%n");
			}
			System.out.format("Page suivante : n, Page précédente : p, Quitter : q%n>");
		}
	}

	private void commandCompanies() {
		String input;
		cas.resetPages();
		System.out.println(cas.selectPage().toString());
		System.out.format("Page suivante : n, Page précédente : p, Quitter : q%n>");
		while (!(input = in.next()).equals("q")) {
			switch (input) {
			case "n":
				cas.nextPage();
				System.out.println(cas.selectPage().toString());
				break;
			case "p":
				cas.previousPage();
				System.out.println(cas.selectPage().toString());
				break;
			default:
				System.out.format("Commande non reconnue.%n");
			}
			System.out.format("Page suivante : n, Page précédente : p, Quitter : q%n>");
		}
	}

	private void commandComputer() {
		System.out.format("Sélection d'un ordinateur par ID :%n>");
		Long idRead = Validator.validateID(in.next());
		DTOComputer comp;
		if (idRead != null && (comp = cs.selectById(idRead)) != null) {
			System.out.println(comp.toString());
		} else {
			System.out.println("Aucun résultat.");
		}
	}

	private void commandCreate() {
		System.out.format("Création d'un nouvel ordinateur :%n");
		System.out.format("Nom (requis) :%n>");
		String name = in.next();
		DTOComputer.Builder newCompBuilder = new DTOComputer.Builder();
		if (!Validator.validateName(name)) {
			System.err.format("Nom requis ! Retour à l'accueil.");
			return;
		}
		newCompBuilder.withName(name);
		System.out.format("LocalDate intro (jj/mm/aaaa) :%n>");
		LocalDate intro = Validator.validateDate(in.next());
		if (intro != null) {
			newCompBuilder.withIntroDate(intro.toString());
		}
		System.out.format("LocalDate disc (>= intro) :%n>");
		LocalDate disc = Validator.validateDate(in.next());
		if (disc != null) {
			newCompBuilder.withDiscDate(disc.toString());
		}
		if (!Validator.validateTemporality(intro, disc)) {
			System.err.format("Erreur de temporalité ! Retour à l'accueil.");
			return;
		}
		System.out.format("ID de l'entreprise :%n>");
		Long compId = Validator.validateID(in.next());
		if (compId != null) {
			newCompBuilder.withCompanyId(compId.toString());
		}
		cs.create(newCompBuilder.build());
	}

	private void commandUpdate() {
		DTOComputer compToUpdate = null;
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
				if (newDate != null) {
					compToUpdate.setIntroduced(newDate.toString());
					 try {
						Validator.validateDTO(compToUpdate);
					} catch (IncorrectNameException | IncorrectIntroDateException | IncorrectDiscDateException
							| IncorrectIDException | IncorrectTemporalityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case "discontinued":
				System.out.format("Nouvelle date de mise hors service (jj/mm/aaaa) :%n>");
				newDate = Validator.validateDate(in.next());
				if (newDate != null) {
					compToUpdate.setDiscontinued(newDate.toString());
					 try {
						Validator.validateDTO(compToUpdate);
					} catch (IncorrectNameException | IncorrectIntroDateException | IncorrectDiscDateException
							| IncorrectIDException | IncorrectTemporalityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case "company_id":
				System.out.format("Nouvel identifiant d'entreprise :%n>");
				Long newCompId = Validator.validateID(in.next());
				compToUpdate.setCompanyId(newCompId.toString());
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
			DTOComputer compToDelete = cs.selectById(idRead);
			if (compToDelete != null) {
				System.out.format("Ordinateur sélectionné :%n");
				System.out.format(compToDelete.toString());
				System.out.format("%nConfirmation de la suppression ? (y/N)%n>");
				if (in.next().equals("y")) {
					cs.delete(compToDelete);
				}
			} else {
				System.out.format("Aucun résultat.");
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
			if (compToDelete != null) {
				System.out.format("Entreprise sélectionné :%n");
				System.out.format(compToDelete.toString());
				System.out.format("%nConfirmation de la suppression ? (y/N)%n>");
				if (in.next().equals("y")) {
					cas.delete(compToDelete.getId());
				}
			} else {
				System.out.format("Aucun résultat.");
			}
		} else {
			System.out.format("Aucun résultat.");
			return;
		}
	}

	private void commandQuit() {
		System.out.format("Au revoir%n");
		System.exit(0);
	}
}
