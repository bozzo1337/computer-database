package ui;

import java.io.Console;

public class LauncherCLI {
	
	private static Console console = System.console();
	private static CLI cli = new CLI();
	
	public static void main(String[] args) {
		if (args.length != 0) {
			System.out.println("No-GUI activated // Remove arg to disable.");
			System.exit(0);
		}
		String login = "";
		String password = "";
		int resultConn = -1;
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
			console.printf(cli.displayComputers() + "%n>");
			break;
		case "companies":
			console.printf(cli.displayCompanies() + "%n>");
			break;
		case "computer":
			console.printf("arg1 :%n>");
			console.printf(cli.findComputer(console.readLine()) + "%n>");
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
