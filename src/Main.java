import java.sql.*;
import java.util.Scanner;

public class Main {
	
	static Scanner scanner = new Scanner(System.in);
	static Connection dbConnection = null;
	
	public static void main(String[] args) {
		
		// Get database connection
		String dbURL = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3308/group2";
		
		do {
			// Acquire administrator credentials to establish initial database connection
			System.out.println("Establishing database connection...");
			
	        System.out.print("Admin Username: ");
	        String username = scanner.nextLine();
	        
	        System.out.print("Admin Password: ");
	        String password = scanner.nextLine();
	        
	        // Attempt to establish initial database connection with provided administrator
	        // credentials
	        try {
	        	dbConnection = DriverManager.getConnection(dbURL, username, password);
	            System.out.println("Database connection established.");
	        }
	        catch (SQLException error) {
	        	System.out.println("Unable to establish database connection. " +
	        		"Please try again or contact your system administrator.");
	            System.out.print("Try again? (y/n): ");
	            
	            String response = scanner.nextLine();
	            if (!response.equals("y")) {
	            	error.printStackTrace();
	    			System.out.println("Goodbye!");
	    			scanner.close();
	    			System.exit(0);
	            }
	        }
		} while (dbConnection == null);
		
		// Display UI root
		displayUserLoginMenu();
	}

	/**
	 * Complete, but untested.
	 * displayUserLoginMenu is the root of the UI. This menu provides options to log in,
	 * create a new account, or exit the application gracefully.
	 * 
	 * Log in: Used by both employees and customers, group membership is handled,
	 * and the appropriate dashboard is displayed.
	 * Create New Account: Used to create a new account, this gathers relevant account info,
	 * and passes it to the database.
	 * Quit: The only way the application should end.
	 */
	public static void displayUserLoginMenu() {
		do {
			String username;
			String password;
			
			System.out.println("Greetings, and welcome to UNFMovies!");
			System.out.println("1. Log in"); // Takes you to a sub-menu
			System.out.println("2. Create new account"); // Performs a function
			System.out.println("3. Quit"); // Ends the application
			
			int selection = getUserSelection();
			switch (selection) {
				case 1: // 1. Log in
					boolean userIsCustomer = false;
					boolean userIsEmployee = false;
					
					System.out.print("Username: ");
					username = scanner.nextLine();
					
					System.out.print("Password: ");
					password = scanner.nextLine();
					
					try {
						userIsCustomer = Query.isExistingCustomer(
							dbConnection, username, password);
						userIsEmployee = Query.isExistingEmployee(
							dbConnection, username, password);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the login menu
					}
						
					if (userIsCustomer && userIsEmployee) {	
						System.out.println("1. Employee dashboard");
						System.out.println("2. Customer dashboard");
						
						selection = getUserSelection();
						switch (selection) {
							case 1:
								displayEmployeeDashboard();
								break; // take me back to the login menu
							case 2:
								displayCustomerDashboard();
								break; // take me back to the login menu
						}
					}
					else if (userIsCustomer) {
						displayCustomerDashboard();
					}
					else if (userIsEmployee) {
						displayEmployeeDashboard();
					}
					else {
						System.out.println("Incorrect username or password.");
					}
					
					break; // take me back to the login menu
				case 2: // 2. Create new account
					String referredBy = "";
					
					System.out.print("Username: ");
					username = scanner.nextLine();
					
					System.out.print("Password: ");
					password = scanner.nextLine();
					
					System.out.print("First name: ");
					String firstName = scanner.nextLine();
					
					System.out.print("Last name: ");
					String lastName = scanner.nextLine();
					
					System.out.print("Were you referenced by an existing customer? (y/n): ");
					String referenced = scanner.nextLine();
					
					if (referenced.equals("y")) {
						System.out.println("Existing customer username: ");
						referredBy = scanner.nextLine();
					}
					
					try {
						Query.createNewCustomer(
							dbConnection, username, password, firstName, lastName, referredBy);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the login menu
					}
					catch (LogicException error) {
						System.out.println(error.getMessage());
					}
					
					break; // take me back to the login menu
				case 3: // 3. Quit
					scanner.close();
					try {
						dbConnection.close();
					}
					catch (SQLException error) {
						error.printStackTrace();
					}
					System.exit(0);
			}
		} while (true);
	}
	
	// Incomplete
	public static void displayCustomerDashboard() {
		do {
			System.out.println("1. Find a movie");
			System.out.println("2. Rental return");
			System.out.println("3. Account Management");
			System.out.println("4. Log out");
			
			int selection = getUserSelection();
		} while (true);
	}
	
	/**
	 * Incomplete and untested.
	 * displayEmployeeDashboard is a sub-menu of displayUserLoginMenu.
	 * This menu provides options to locate a movie, update existing inventory,
	 * manage a customer's account, generate a report, or log out of the application.
	 * 
	 * Locate a movie: This allows the employee to search for a movie by its MovieTitle or
	 * its MovieID properties.
	 * Update Inventory: Displays the update inventory menu.
	 * Customer Management: Incomplete
	 * Reports: Incomplete
	 * Log out: Log out and return to the login menu.
	 */
	public static void displayEmployeeDashboard() {
		do {
			System.out.println("1. Locate a movie"); // Performs a function
			System.out.println("2. Update inventory"); // Takes you to a sub-menu
			System.out.println("3. Customer Management");
			System.out.println("4. Reports");
			System.out.println("5. Log out");
			
			int selection = getUserSelection();
			switch (selection) {
				case 1: // 1. Locate a movie
					System.out.println("Select a movie property to search by.");
					System.out.println("1. Movie ID");
					System.out.println("2. Movie Title");
					
					selection = getUserSelection();
					switch (selection) {
						case 1: // 1. Movie ID
							System.out.print("Movie ID: ");
							
							int movieID = getUserSelection();
							try {
								Query.getMovieByID(dbConnection, movieID);
							}
							catch (SQLException error) {
								error.printStackTrace();
								System.out.println("A database error was encountered. " +
									"Please try again or contact your system administrator.");
								break; // take me back to the employee dashboard
							}
							
							break; // take me back to the employee dashboard
						case 2: // 2. Movie Title
							System.out.print("Movie Title: ");
							
							String movieTitle = scanner.nextLine();
							try {
								Query.getMovieByTitle(dbConnection, movieTitle);
							}
							catch (SQLException error) {
								error.printStackTrace();
								System.out.println("A database error was encountered. " +
									"Please try again or contact your system administrator.");
								break; // take me back to the employee dashboard
							}
							
							break; // take me back to the employee dashboard
					}
					
					break; // take me back to the employee dashboard
				case 2:
					displayUpdateInventoryMenu();
					break; // take me back to the employee dashboard
				case 3:
					System.out.println("Menu option has not yet been implemented. Check back later.");
					break;
				case 4:
					System.out.println("Menu option has not yet been implemented. Check back later.");
					break;
				case 5:
					return; // Take me back to the log in menu
			}
		} while (true);
	}
	
	/**
	 * Incomplete and untested.
	 * displayUpdateInventoryMenu is a sub-menu of displayEmployeeDashboard.
	 * This menu provides options to add a new item, update an existing item,
	 * delete an item, or return to the employee dashboard.
	 * 
	 * Add a new item: Takes you to the add new item sub-menu.
	 * Update existing item: Incomplete
	 * Delete item: Incomplete
	 * Back: Returns you to the employee dashboard.
	 */
	public static void displayUpdateInventoryMenu() {
		do {
			System.out.println("1. Add new item"); // Takes you to a sub-menu
			System.out.println("2. Update existing item");
			System.out.println("3. Delete item");
			System.out.println("4. Back");
			
			int selection = getUserSelection();
			switch (selection) {
				case 1:
					displayAddNewItemMenu();
					break; // take me back to the update inventory menu
				case 2:
					System.out.println("Menu option has not yet been implemented. Check back later.");
					break; // take me back to the update inventory menu
				case 3:
					System.out.println("Menu option has not yet been implemented. Check back later.");
					break; // take me back to the update inventory menu
				case 4:
					return; // take me back to the employee dashboard
			}
		} while (true);
	}
	
	/**
	 * Incomplete and untested.
	 * displayAddNewItemMenu is a sub-menu of displayUpdateInventoryMenu.
	 * This menu provides options to add a movie, add an actor, add a genre,
	 * add a director, add an employee, or return to the update inventory menu.
	 * 
	 * Add Movie: Gathers relevant movie data and passes it to the database to attempt to add it.
	 * Add Actor: Incomplete
	 * Add Genre: Incomplete
	 * Add Director: Incomplete
	 * Add Employee Incomplete
	 * Back: Returns you to the update inventory menu.
	 */
	public static void displayAddNewItemMenu() {
		do {
			System.out.println("1. Add Movie"); // Performs a function
			System.out.println("2. Add Actor");
			System.out.println("3. Add Genre");
			System.out.println("4. Add Director");
			System.out.println("5. Add Employee");
			System.out.println("6. Back");
			
			int selection = getUserSelection();
			switch (selection) {
				case 1: // 1. Add Movie
					System.out.print("Movie title: ");
					String movieTitle = scanner.nextLine();
					
					System.out.print("Movie release date (yyyy-mm-dd): ");
					String movieReleaseDate = scanner.nextLine();
					
					System.out.print("Movie certificate rating: ");
					String movieCertificateRating = scanner.nextLine();
					
					System.out.print("Movie business cost per item: ");
					double movieBusinessCost = scanner.nextDouble();
					
					System.out.print("Movie customer purchase cost: ");
					double movieCustomerPurchaseCost = scanner.nextDouble();
					
					System.out.print("Movie customer rent cost: ");
					double movieCustomerRentCost = scanner.nextDouble();
					
					try {
						Query.insertMovie(
								movieTitle,
								movieReleaseDate,
								movieCertificateRating,
								movieBusinessCost,
								movieCustomerPurchaseCost,
								movieCustomerRentCost);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the add new item menu
					}
					catch (LogicException error) {
						System.out.println(error.getMessage());
					}
					
					break; // take me back to the add new item menu
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					return; // take me back to the update inventory menu
			}
		} while (true);
	}
	
	/**
	 * Forces the user to enter an integer.
	 * @param scanner The scanner object
	 * @return A user-selected integer
	 */
	public static int getUserSelection() {
		do {
			try {
				return Integer.parseInt(scanner.nextLine());
			}
			catch (NumberFormatException error) {
				System.out.println("Please select one of the options provided.");
			}
		} while (true);
	}
}
