package ui;

import java.io.Console;

import persistence.CLI;

public class LauncherCLI {
	
	private static Console console = System.console();
	private static CLI cli = new CLI();
	
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
				console.printf("Erreur de connexion, veuillez réessayer...%n>");
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
			console.printf("Commandes disponibles : %n");
			console.printf("help : Affiche la liste des commandes détaillées.%n");
			console.printf("computers : Affiche la liste des ordinateurs.%n");
			console.printf("companies : Affiche la liste des entreprises.%n");
			console.printf("computer : Affiche les détails de l'ordinateur demandé.%n");
			console.printf("create : Crée l'ordinateur indiqué.%n");
			console.printf("update : Met à jour l'ordinateur ciblé.%n");
			console.printf("delete : Supprime l'ordinateur ciblé.%n");
			console.printf("quit : Quitte le programme.%n>");
			break;
		case "computers":
			console.printf(cli.displayComputers() + ">");
			break;
		case "companies":
			console.printf(cli.displayCompanies() + ">");
			break;
		case "computer":
			console.printf("Sélection d'un ordinateur :%nID :%n>");
			try {
				Long idRead = Long.valueOf(console.readLine());
				console.printf(cli.findComputer(idRead));
			} catch (NumberFormatException e) {
				console.printf("Format d'ID invalide, retour à l'accueil.%n>");
			}
			break;
		case "create":
			console.printf("Création d'un nouvel ordinateur :%n");
			console.printf("arg1 :%n>");
			String arg1 = console.readLine();
			console.printf("arg2 :%n>");
			String arg2 = console.readLine();
			console.printf("arg3 :%n>");
			String arg3 = console.readLine();
			if(cli.createComputer(arg1, arg2, arg3) == 0) {
				console.printf("Création réussie.%n>");
			} else {
				console.printf("Echec de la création.%n>");
			}
			break;
		case "update":
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
			break;
		case "delete":
			console.printf("Suppression d'un ordinateur :%n");
			console.printf("id :%n>");
			String id = console.readLine();
			if(cli.deleteComputer(id) == 0) {
				console.printf("Suppression réussie.%n>");
			} else {
				console.printf("Echec de la suppression.%n>");
			}
			break;
		case "quit":
			console.printf("Fermeture de la connexion...%n");
			if (cli.closeConn() == 0) {
				console.printf("Fin de connexion OK, au revoir !%n");
				System.exit(0);
			} else {
				console.printf("Echec de la fermeture, au revoir quand même !%n");
				System.exit(1);
			}
			break;
		default:
			console.printf("Commande non reconnue, tapez 'help' pour plus d'informations.%n>");
		}
	}
}
