import java.sql.*;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		// Set up UI environment
		Scanner scanner = new Scanner(System.in);
		
		// Set up database environment
		DatabaseBridge db = null;
		
		// Establish database connection
		try {
			db = new DatabaseBridge(scanner);
		}
		catch (SQLException error) {
			System.out.println("Goodbye!");
			scanner.close();
			System.exit(0);
		}
		
		displayUserLoginMenu(db.connection, scanner);
	}
	
	public static void displayUserLoginMenu(Connection connection, Scanner scanner) {
		do {
			String username = null;
			String password = null;
			int selection = 0;
			
			System.out.println("Greetings, and welcome to UNFMovies!");
			System.out.println("1. Log in");
			System.out.println("2. Sign up");
			System.out.println("3. Quit");
			
			do {
				try {
					selection = Integer.parseInt(scanner.nextLine());
					break;
				}
				catch (NumberFormatException error) {
					System.out.println("Please select one of the options provided.");
				}
			} while (true);
			
			switch (selection) {
				case 1:
					System.out.print("Username: ");
					username = scanner.nextLine();
					System.out.print("Password: ");
					password = scanner.nextLine();
					try {
						if (Query.isExistingCustomer(connection, username, password)) {
							// also need to check if user is an employee
							// return displayUserDashboard();
							System.out.println("Welcome, " + username + "!");
							System.out.println("Your dashboard is coming soon. For now, we must bid you farewell!");
							exit(scanner, connection, 0);
						}
						else {
							System.out.println("The password you entered was incorrect."); // Vagueness is on purpose (security)
						}
					}
					catch (SQLException error) {
						exit(scanner, connection, 0);
					}
					break;
				case 2:
					System.out.println("This function is not yet supported.");
					break;
				case 3:
					exit(scanner, connection, 0);
			}
		} while (true);
	}
	
	/**
	 * Exits the program gracefully.
	 * @param scanner The scanner used to collect user input.
	 * @param connection The connection to the database.
	 * @param status The exit status code.
	 */
	public static void exit(Scanner scanner, Connection connection, int status) {
		System.out.println("Goodbye!");
		scanner.close();
		try {
			connection.close();
		}
		catch (SQLException error) {
			error.printStackTrace();
		}
		System.exit(status);
	}
}
