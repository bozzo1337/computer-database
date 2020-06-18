package com.excilys.computerDB.service;

import java.io.Console;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.excilys.computerDB.mapper.ComputerMapper;
import com.excilys.computerDB.model.Computer;
import com.excilys.computerDB.model.Page;
import com.excilys.computerDB.model.RequestResult;
import com.excilys.computerDB.persistence.QueryExecutor;

public class ComputerService {
	
	private Console console;
	private Page pageComp = Page.getInstance();
	private QueryExecutor qe = QueryExecutor.getInstance();
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private ComputerMapper cm = ComputerMapper.getInstance();
	
	public ComputerService(Console console) {
		this.console = console;
	}
	
	public void selectAll() {
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
	}
	
	public void selectById() {
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = null;
		RequestResult rr = new RequestResult();
		try {
			idRead = Long.valueOf(console.readLine());
		} catch (NumberFormatException e) {
			System.err.format("Format d'ID invalide.%n>");
			return;
		}
		rr = qe.findComputer(idRead);
		console.printf(rr.toString() + ">");
	}
	
	public void create() {
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
		console.printf(qe.createComputer(name, intro, disc, compId).toString() + ">");	
	}
	
	public void update() {
		console.printf("Mise à jour d'un ordinateur :%n");
		Long idRead = null;
		RequestResult rr = new RequestResult();
		do {
			console.printf("Sélection d'un ordinateur par ID :%n>");
			try {
				idRead = Long.valueOf(console.readLine());
				rr = qe.findComputer(idRead);
				console.printf(rr.toString() + "%n>");
			} catch (NumberFormatException e) {
				System.err.format("Format d'ID invalide.%n>");
			}
		} while (rr.getStatus() != 0);
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
	
	public void delete() {
		console.printf("Suppression d'un ordinateur :%n");
		console.printf("Sélection d'un ordinateur par ID :%n>");
		Long idRead = null;
		RequestResult rr = new RequestResult();
		try {
			idRead = Long.valueOf(console.readLine());
		} catch (NumberFormatException e) {
			System.err.format("Format d'ID invalide.%n>");
			return;
		}
		rr = qe.findComputer(idRead);
		if (rr.getStatus() != 0) {
			console.printf(rr.toString());
			return;
		}
		console.printf("Ordinateur sélectionné :%n");
		console.printf(rr.toString());
		console.printf("Confirmation de la suppression ? (y/n)%n>");
		if (console.readLine().contentEquals("y")) {
			console.printf(qe.deleteComputer(idRead).toString() + ">");
		} else {
			console.printf("Suppression annulée.%n>");
		}
	}
}
