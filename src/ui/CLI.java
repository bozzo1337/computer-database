package ui;

import java.io.Console;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

import persistence.QueryExecutor;

public class CLI {
	
	private static Console console = System.console();
	private static QueryExecutor cli = new QueryExecutor();
	
	public static void main(String[] args) {
		boolean autolog = false; //Dev option ONLY -- TO REMOVE
		if (args.length != 0) {
			switch (args[0]) {
			case "nolaunch": //To Compile with Eclipse without NullPointerE from System.console
				System.out.println("No-Launch activated // Remove arg to disable.");
				System.exit(0);
			case "autolog": //Lazy login
				autolog = !autolog;
				break;
			default:		
			}
			
		}
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
			//DEV ONLY -- TO REMOVE
			if (autolog) {
				login = "admincdb";
				password = "qwerty1234";
			}
		} while ((resultConn = cli.initConn(login, password)) != 0);
		console.printf("Connexion OK%n");
		console.printf("Commandes disponibles :%nhelp, computers, companies, computer, create, update, delete, quit%n>");
		for (;;) {
			nextCommand(console.readLine());
		}
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
		console.printf(cli.displayComputers() + ">");
	}
	
	private static void commandCompanies() {
		console.printf(cli.displayCompanies() + ">");
	}
	
	private static void commandComputer() {
		console.printf("Sélection d'un ordinateur :%nID :%n>");
		try {
			Long idRead = Long.valueOf(console.readLine());
			console.printf(cli.findComputer(idRead));
		} catch (NumberFormatException e) {
			System.err.format("Format d'ID invalide, retour à l'accueil.%n>");
		}
	}
	
	private static int commandCreate() {
		console.printf("Création d'un nouvel ordinateur :%n");
		console.printf("Nom (requis) :%n>");
		String name = console.readLine();
		if (name.trim().isEmpty()) {
			System.err.format("Nom requis ! Retour à l'accueil.%n>");
			return 1;
		}
		console.printf("Date intro (jj/mm/aaaa) :%n>");
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date intro = null;
		try {
			intro = new Date(df.parse(console.readLine()).getTime());
		} catch (ParseException e) {
			System.err.format("Erreur de format. Valeur par défaut attribuée.%n");
		}
		console.printf("Date disc (>intro) :%n>");
		Date disc = null;
		try {
			disc = new Date(df.parse(console.readLine()).getTime());
		} catch (ParseException e) {
			System.err.format("Erreur de format. Valeur par défaut attribuée.%n");
		}
		Long compId = null;
		console.printf("ID de l'entreprise :%n>");
		try {
			compId = Long.valueOf(console.readLine());
		} catch (NumberFormatException e) {
			System.err.format("ID invalide, valeur par défaut attribuée.%n");
		}
		if(cli.createComputer(name, intro, disc, compId) == 0) {
			console.printf("Création réussie.%n>");
			return 0;
		} else {
			System.err.format("Echec de la création.%n>");
			return 1;
		}
	}
	
	private static void commandUpdate() {
		console.printf("Mise à jour d'un ordinateur :%n");
		console.printf("arg1 :%n>");
		String arg4 = console.readLine();
		console.printf("arg2 :%n>");
		String arg5 = console.readLine();
		console.printf("arg3 :%n>");
		String arg6 = console.readLine();
		if(cli.updateComputer(arg4, arg5, arg6) == 0) {
			console.printf("Mise à jour réussie.%n>");
		} else {
			console.printf("Echec de la mise à jour.%n>");
		}
	}
	
	private static void commandDelete() {
		console.printf("Suppression d'un ordinateur :%n");
		console.printf("id :%n>");
		String id = console.readLine();
		if(cli.deleteComputer(id) == 0) {
			console.printf("Suppression réussie.%n>");
		} else {
			console.printf("Echec de la suppression.%n>");
		}
	}
	
	private static void commandQuit() {
		console.printf("Fermeture de la connexion...%n");
		if (cli.closeConn() == 0) {
			console.printf("Fin de connexion OK, au revoir !%n");
			System.exit(0);
		} else {
			console.printf("Echec de la fermeture, au revoir quand même !%n");
			System.exit(1);
		}
	}
}
