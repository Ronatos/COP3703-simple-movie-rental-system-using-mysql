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
		
		// Display UI root
		displayUserLoginMenu(db.connection, scanner);
	}

	/**
	 * Complete, but untested awaiting database updates.
	 * displayUserLoginMenu is the root of the UI. This provides options such as Log In (for both customers and employees),
	 * Sign Up (for customers only), and Quit - the only way the application should end.
	 * @param connection The connection object
	 * @param scanner The scanner object
	 */
	public static void displayUserLoginMenu(Connection connection, Scanner scanner) {
		do {
			String username = null;
			String password = null;
			int selection = 0;
			
			System.out.println("Greetings, and welcome to UNFMovies!");
			System.out.println("1. Log in");
			System.out.println("2. Sign up");
			System.out.println("3. Quit");
			
			selection = getUserSelection(scanner);
			
			switch (selection) {
				case 1:
					System.out.print("Username: ");
					username = scanner.nextLine();
					System.out.print("Password: ");
					password = scanner.nextLine();
					try {
						boolean userIsCustomer = Query.isExistingCustomer(connection, username, password);
						boolean userIsEmployee = Query.isExistingEmployee(connection, username, password);
						if (userIsCustomer && userIsEmployee) {
							System.out.println("1. Employee login");
							System.out.println("2. Customer login");
							selection = getUserSelection(scanner);
							switch (selection) {
								case 1:
									displayEmployeeDashboard(connection, scanner);
									break;
								case 2:
									displayCustomerDashboard(connection, scanner);
									break;
							}
						}
						else if (userIsCustomer) {
							displayCustomerDashboard(connection, scanner);
						}
						else if (userIsEmployee) {
							displayEmployeeDashboard(connection, scanner);
						}
						else {
							System.out.println("Incorrect username or password."); // Vagueness is on purpose (security)
						}
					}
					catch (SQLException error) {
						System.out.println("Critical failure encountered during database operation. Aborting application...");
						exit(scanner, connection, 0);
					}
					break;
				case 2:
					String firstName = null;
					String lastName = null;
					String referenced = null;
					String referredBy = "";
					
					System.out.print("Username: ");
					username = scanner.nextLine();
					System.out.print("Password: ");
					password = scanner.nextLine();
					System.out.print("First name: ");
					firstName = scanner.nextLine();
					System.out.print("Last name: ");
					lastName = scanner.nextLine();
					System.out.print("Were you referenced by an existing customer? (y/n): ");
					referenced = scanner.nextLine();
					if (referenced.equals("y")) {
						System.out.println("Existing customer username: ");
						referredBy = scanner.nextLine();
					}
					
					try {
						Query.createNewCustomer(connection, username, password, firstName, lastName, referredBy);
					}
					catch (SQLException error) {
						System.out.println("Critical failure encountered during database operation. Aborting application...");
						exit(scanner, connection, 0);
					}
					catch (LogicException error) {
						System.out.println(error.getMessage());
					}
					break;
				case 3:
					exit(scanner, connection, 0);
			}
		} while (true);
	}
	
	// Incomplete
	public static void displayCustomerDashboard(Connection connection, Scanner scanner) {
		do {
			int selection = 0;
			
			System.out.println("1. Find a movie");
			System.out.println("2. Rental return");
			System.out.println("3. Account Management");
			System.out.println("4. Log out");
			
			selection = getUserSelection(scanner);
		} while (true);
	}
	
	// Incomplete
	public static void displayEmployeeDashboard(Connection connection, Scanner scanner) {
		do {
			int selection = 0;
			
			System.out.println("1. Locate a movie");
			System.out.println("2. Update inventory");
			System.out.println("3. Customer Management");
			System.out.println("4. Reports");
			System.out.println("5. Log out");
			
			selection = getUserSelection(scanner);
			switch (selection) {
			case 1:
				System.out.println("Select a movie property to search by.");
				System.out.println("1. Movie ID");
				System.out.println("2. Movie Title");
				selection = getUserSelection(scanner);
				
				switch (selection) {
					case 1:
						int movieID = 0;
						
						System.out.print("Movie ID: ");
						movieID = getUserSelection(scanner);
						try {
							Query.getMovieByID(connection, movieID);
						}
						catch (SQLException error) {
							System.out.println("Critical failure encountered during database operation. Aborting application...");
							exit(scanner, connection, 0);
						}
						break;
					case 2:
						String movieTitle = null;
						
						System.out.print("Movie Title: ");
						movieTitle = scanner.nextLine();
						try {
							Query.getMovieByTitle(connection, movieTitle);
						}
						catch (SQLException error) {
							System.out.println("Critical failure encountered during database operation. Aborting application...");
							exit(scanner, connection, 0);
						}
						break;
				}
				break;
			case 2:
				System.out.println("Menu has not yet been implemented. Check back later.");
				break;
			case 3:
				System.out.println("Menu has not yet been implemented. Check back later.");
				break;
			case 4:
				return;
			}
			System.out.println("Menu has not yet been implemented. Check back later.");
		} while (true);
	}
	
	/**
	 * Forces the user to enter an integer.
	 * @param scanner The scanner object
	 * @return A user-selected integer
	 */
	public static int getUserSelection(Scanner scanner) {
		do {
			try {
				return Integer.parseInt(scanner.nextLine());
			}
			catch (NumberFormatException error) {
				System.out.println("Please select one of the options provided.");
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
