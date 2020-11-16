import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
 * To do
 * 1. Remove all error.printStackTrace() calls after application is fully tested.
 *    The user doesn't need to see these.
 * 2. Add a way for the employees who have just searched for a movie to update that movie right there instead of needing to
 * 	  navigate through the tree.
 * 3. Add a way for employees to look up the ID of an item, since updating them requires the employee to know the ID
 * 4. Add user friendly way of knowing where they are in the tree (home/employee dashboard/add new item/) for example
 */

public class Main {
	
	static Scanner scanner = new Scanner(System.in);
	static Connection dbConnection = null;
	
	public static void main(String[] args) {
		dbConnection = getDatabaseConnection();
		displayRootMenu();
	}
	
	private static Connection getDatabaseConnection() {
		String dbURL = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3308/group2";
		
		do {
			System.out.println("Establishing database connection...");
			
	        System.out.print("Admin Username: ");
	        String username = scanner.nextLine();
	        
	        System.out.print("Admin Password: ");
	        String password = scanner.nextLine();
	        
	        try {
	        	return DriverManager.getConnection(dbURL, username, password);
	        }
	        catch (SQLException error) {
	        	Utils.printDatabaseError(error);
	            System.out.print("Try again? (y/n): ");
	            
	            String response = scanner.nextLine();
	            if (!response.equals("y")) {
	    			System.out.println("Goodbye!");
	    			scanner.close();
	    			System.exit(0);
	            }
	        }
		} while (true);
	}

	/**
	 * Complete and tested.
	 * displayRootMenu is the root of the UI. This menu provides options to log in,
	 * create a new account, or exit the application gracefully.
	 */
	private static void displayRootMenu() {
		do {
			String username;
			String password;
			
			System.out.println("----------");
			System.out.println("Home");
			System.out.println("Greetings, and welcome to UNFMovies!");
			System.out.println("----------");
			System.out.println("1. Log in");
			System.out.println("2. Create new account");
			System.out.println("3. Quit");
			
			int selection = Utils.getUserSelection(scanner);
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
						Utils.printDatabaseError(error);
						break; // back to the root menu
					}
						
					if (userIsCustomer && userIsEmployee) {
						displayDashboardDecisionMenu(username);
					}
					else if (userIsCustomer) {
						displayCustomerDashboard(username);
					}
					else if (userIsEmployee) {
						displayEmployeeDashboard();
					}
					else {
						System.out.println("Incorrect username or password.");
					}
					
					break; // back to the root menu
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
						Utils.printDatabaseError(error);
						break; // take me back to the login menu
					}
					catch (LogicException error) {
						System.out.println(error.getMessage());
					}
					
					break; // take me back to the login menu
				case 3: // 3. Quit
					System.out.println("Goodbye!");
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
	
	/**
	 * Complete and tested.
	 * displayDashboardDecisionMenu is displayed after logging in to the application
	 * if the user exists in both the Employees and Customers tables.
	 */
	private static void displayDashboardDecisionMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Login");
			System.out.println("Which dashboard would you like to use?");
			System.out.println("----------");
			System.out.println("1. Employee dashboard");
			System.out.println("2. Customer dashboard");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1:
					displayEmployeeDashboard();
					return; // back to the root menu
				case 2:
					displayCustomerDashboard(username);
					return; // back to the root menu
			}
		} while (true);
	}
	
	// Customer -----------------------------------------------------------------------------------

	public static void displayCustomerDashboard(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard");
			System.out.println("What would you like to do, " + username + "?");
			System.out.println("----------");
			System.out.println("1. Search");
			System.out.println("2. My Rentals");
			System.out.println("3. Account Management"); // don't forget to add a way to return purchases. see #10 for details
			System.out.println("4. Log out");
			
			int selection = Utils.getUserSelection(scanner);
			System.out.println(selection);
			switch (selection) {
			case 1: //1. Find a movie
				displayCustomerSearchMenu(username);
				break;
			case 2: //2. Rental return
				// displayCustomerMyRentalsMenu();
				break;
			case 3: //3. Account Management
				// displayCustomerAccountManagementMenu();
				break;
			case 4:
				return;	
			}
		} while (true);
	}
	
	// Customer - Purchase ------------------------------------------------------------------------
	
	private static void displayCustomerPurchaseMenu(String username) {
		System.out.println("----------");
		System.out.println("Home / Customer Dashboard / Purchase");
		System.out.println("Please enter the ID of the movie you would like to purchase.");
		System.out.println("----------");
		System.out.print("Movie ID: ");
		
		int movieID = Utils.getUserSelection(scanner);
		try { // customerCanAffordPurchase just returns a boolean if the customer's balance is >= movie purchase cost
			if (Query.isExistingMovie(dbConnection, movieID) && Query.customerCanAffordPurchase(dbConnection, username, movieID)) {
				// connection, customerId, movieID, isRental <- these are passed in.
				// upfrontcost and transactiondate are retrieved from SQL code
				Query.insertTransaction(dbConnection, username, movieID, false); // GO BACK AND ACCOUNT FOR NEW OR NON-NEW MOVIES
				// all this needs to do is subtract the movie purchase cost from the customer balance
				Query.purchaseMovie(dbConnection, username, movieID); // GO BACK AND ACCOUNT FOR NEW OR NON-NEW MOVIES
			}
			else {
				break;
			}
		}
		catch (SQLException error) {
			Utils.printDatabaseError(error);
		}
	}
	
	// Customer - Rental --------------------------------------------------------------------------
	
	private static void displayCustomerRentalMenu(String username) {
		System.out.println("----------");
		System.out.println("Home / Customer Dashboard / Rent");
		System.out.println("Please enter the ID of the movie you would like to rent.");
		System.out.println("----------");
		System.out.print("Movie ID: ");
		
		int selection = Utils.getUserSelection(scanner);
		// check if movie exists
		// check if the customer
		// 1. has the balance required for the transaction
		// 2. will not surpass 2 current rentals
		// 3. 
		// subtract the balance and add an entry to the transaction table and rental table
	}
	
	// Customer - Search --------------------------------------------------------------------------
	
	// Complete
	private static void displayCustomerSearchMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard / Search");
			System.out.println("How would you like to search for a movie?");
			System.out.println("----------");
			System.out.println("1. Recommended");
			System.out.println("2. Custom");
			System.out.println("3. Back to dashboard");
			
			int selection = Utils.getUserSelection(scanner);
			
			switch (selection) {
			case 1:// 1. Recommended
				displayCustomerSearchRecommendedMenu(username);
				break;
			case 2: // 2. Custom
				displayCustomerSearchCustomMovieMenu(username);
			case 3://3. Back to dashboard
				return;
			}
		} while (true);
	}
	
	// Customer - Search - Recommended ------------------------------------------------------------
	
	// Complete
	private static void displayCustomerSearchRecommendedMenu(String username) {
		try {
<<<<<<< HEAD
			Query.getMoviesOfTheMonth(dbConnection);
			Query.getHighestRatedMovies(dbConnection, 0);
=======
			Query.getMoviesOfTheMonth();
			Query.getHighestRatedMovies(dbConnection);
>>>>>>> 75c42871fe4d331388296e7a84cbf7d4c374af42
		}
		catch (SQLException error) {
			Utils.printDatabaseError(error);
		}
		
		System.out.println("----------");
		System.out.println("Home / Customer Dashboard / Search / Recommended");
		System.out.println("Would you like to purchase or rent one of these movies?");
		System.out.println("----------");
		System.out.println("1. Purchase");
		System.out.println("2. Rent");
		System.out.println("3. Back");
		
		int selection = Utils.getUserSelection(scanner);
		switch (selection) {
		case 1:
			displayCustomerPurchaseMenu(username);
		case 2:
			displayCustomerRentalMenu(username);
		case 3:
			return; // back to customer search
		}
	}
	
	// Customer - Search - Custom -----------------------------------------------------------------
	
	// TO DO: This needs to transition from a search to a purchase via another menu
	
	// Query.getMovieByID should be replaced by something that doesn't display business data like getMovieByIDRestricted
	// getMovieByTitle, getMovieByCertificateRating, and getMovieByReleaseDate are the same deal
	private static void displayCustomerSearchCustomMovieMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard / Search / Custom / Movie");
			System.out.println("How would you like to search for a movie?");
			System.out.println("----------");
			System.out.println("1. Movie ID");
			System.out.println("   Searching for a movie by ID provides data about\n" +
				"   the movie, all actors that played in it, all directors that directed it,\n" +
				"   and all genres it is classified as.");
			System.out.println("2. Actor");
			System.out.println("3. Director");
			System.out.println("4. Genre");
			System.out.println("5. Movie Title");
			System.out.println("6. Certificate Rating");
			System.out.println("7. Release Date");
			System.out.println("8. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Movie ID
					System.out.print("Movie ID: ");
					
					try {
						int movieID = Utils.getUserSelection(scanner);
						Query.getMovieByID(dbConnection, movieID);
						Query.getActorsByMovie(dbConnection, movieID);
						Query.getDirectorsByMovie(dbConnection, movieID);
						Query.getGenresByMovie(dbConnection, movieID);
						
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Actor
					displayCustomerSearchCustomActorMenu(username);
					break;
				case 3: // 3. Director
					displayCustomerSearchCustomDirectorMenu(username);
					break;
				case 4: // 4. Genre
					displayCustomerSearchCustomGenreMenu(username);
					break;
				case 5: // 5. Movie Title
					System.out.print("Movie Title: ");
					
					String movieTitle = scanner.nextLine();
					try {
						Query.getMovieByTitle(dbConnection, movieTitle);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 6: // 6. Certificate Rating
					System.out.print("Certificate Rating: ");
					
					String certificateRating = scanner.nextLine();
					try {
						Query.getMovieByCertificateRating(dbConnection, certificateRating);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 7: // 7. Release Date
					System.out.print("Release Date: ");
					
					String releaseDate = scanner.nextLine();
					try {
						Query.getMovieByReleaseDate(dbConnection, releaseDate);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 8: // 8. Back
					return;
			}
		} while (true);
	}
	
	// getMoviesByActor pritns business movie data
	private static void displayCustomerSearchCustomActorMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard / Search / Custom / Actor");
			System.out.println("How would you like to search for an actor?");
			System.out.println("----------");
			System.out.println("1. Actor ID");
			System.out.println("   Searching for an actor by ID provides data\n" +
				"   about the actor and all movies they have played in.");
			System.out.println("2. Movie");
			System.out.println("3. First name");
			System.out.println("4. Last name");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Actor ID
					System.out.print("Actor ID: ");
					
					try {
						int actorID = Utils.getUserSelection(scanner);
						Query.getActorByID(dbConnection, actorID);
						Query.getMoviesByActor(dbConnection, actorID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayCustomerSearchCustomMovieMenu(username);
					break;
				case 3: // 3. First Name
					System.out.print("First name: ");
					
					String firstName = scanner.nextLine();
					try {
						Query.getActorByFirstName(dbConnection, firstName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Last Name
					System.out.print("Last name: ");
					
					String lastName = scanner.nextLine();
					try {
						Query.getActorByLastName(dbConnection, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// getMoviesByDirector prints business movie data
	private static void displayCustomerSearchCustomDirectorMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard / Search / Custom / Director");
			System.out.println("How would you like to search for a director?");
			System.out.println("----------");
			System.out.println("1. Director ID");
			System.out.println("   Searching for a director by ID provides data about\n" +
				"   the director and all movies they have directed.");
			System.out.println("2. Movie");
			System.out.println("3. First Name");
			System.out.println("4. Last Name");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Director ID
					System.out.print("Director ID: ");
					
					try {
						int directorID = Utils.getUserSelection(scanner);
						Query.getDirectorByID(dbConnection, directorID);
						Query.getMoviesByDirector(dbConnection, directorID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayCustomerSearchCustomMovieMenu(username);
					break;
				case 3: // 3. First Name
					System.out.print("First Name: ");
					
					String firstName = scanner.nextLine();
					try {
						Query.getDirectorByFirstName(dbConnection, firstName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Last Name
					System.out.print("Last Name: ");
					
					String lastName = scanner.nextLine();
					try {
						Query.getDirectorByLastName(dbConnection, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// getMoviesByGenre provides too much business data
	private static void displayCustomerSearchCustomGenreMenu(String username) {
		do {
			System.out.println("----------");
			System.out.println("Home / Customer Dashboard / Search / Custom / Genre");
			System.out.println("How would you like to search for a genre?");
			System.out.println("----------");
			System.out.println("1. Genre ID");
			System.out.println("   Searching for a genre by ID provides data about\n" +
				"   the genre and all movies in the genre.");
			System.out.println("2. Movie");
			System.out.println("3. Genre Type");
			System.out.println("4. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Genre ID
					System.out.print("Genre ID: ");
					
					try {
						int genreID = Utils.getUserSelection(scanner);
						Query.getGenreByID(dbConnection, genreID);
						Query.getMoviesByGenre(dbConnection, genreID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayCustomerSearchCustomMovieMenu(username);
					break;
				case 3: // 3. Genre Type
					System.out.print("Genre: ");
					
					String genre = scanner.nextLine();
					try {
						Query.getGenreByType(dbConnection, genre);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Back
					return;
			}
		} while (true);
	}
	
	// Employee -----------------------------------------------------------------------------------
	
	/**
	 * Complete and untested.
	 * displayEmployeeDashboard is a sub-menu of displayUserLoginMenu.
	 * This menu provides options to search, update existing inventory,
	 * manage a customer's account, generate a report, or log out of the application.
	 */
	private static void displayEmployeeDashboard() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard");
			System.out.println("What would you like to do?");
			System.out.println("----------");
			System.out.println("1. Search");
			System.out.println("2. Add");
			System.out.println("3. Update");
			System.out.println("4. Link");
			System.out.println("5. Business Management");
			System.out.println("6. Customer Management");
			System.out.println("7. Reports");
			System.out.println("8. Log out");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Search
					displayEmployeeSearchMenu();
					break;
				case 2: // 2. Add
					displayEmployeeAddMenu();
					break;
				case 3: // 3. Update
					displayEmployeeUpdateMenu();
					break;
				case 4:
					displayEmployeeLinkMenu();
					break;
				case 5:
					displayEmployeeBusinessManagementMenu();
					break;
				case 6:
					System.out.print("Customer ID: ");
					int customerID = Utils.getUserSelection(scanner);
					
					try {
						if (!Query.isExistingCustomer(dbConnection, customerID)) {
							System.out.println("Customer ID " + customerID + " does not reference an existing customer.");
							break;
						}
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					displayEmployeeCustomerManagement(customerID);
					break;
				case 7:
					displayEmployeeReportsMenu();
					break;
				case 8:
					return; // back to the root menu
			}
		} while (true);
	}
	
	// Employee - Search --------------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchMenu is a sub-menu of displayEmployeeDashboard.
	 * This menu allows the user to select an entity in the database to search for.
	 */
	private static void displayEmployeeSearchMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search");
			System.out.println("Which entity would you like to search for?");
			System.out.println("----------");
			System.out.println("1. Actor");
			System.out.println("2. Customer");
			System.out.println("3. Director");
			System.out.println("4. Genre");
			System.out.println("5. Movie");
			System.out.println("6. Review");
			System.out.println("7. Transaction");
			System.out.println("8. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Actor
					displayEmployeeSearchActorMenu();
					break;
				case 2: // 2. Customer
					displayEmployeeSearchCustomerMenu();
					break;
				case 3: // 3. Director
					displayEmployeeSearchDirectorMenu();
					break;
				case 4: // 4. Genre
					displayEmployeeSearchGenreMenu();
					break;
				case 5: // 5. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 6: // 6. Review
					displayEmployeeSearchReviewMenu();
					break;
				case 7: // 7. Transaction
					displayEmployeeSearchTransactionMenu();
					break;
				case 8: // 8. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Actor ------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchActorMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for an actor entity using various attributes.
	 */
	private static void displayEmployeeSearchActorMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Actor");
			System.out.println("How would you like to search for an actor?");
			System.out.println("----------");
			System.out.println("1. Actor ID");
			System.out.println("   Searching for an actor by ID provides data\n" +
				"   about the actor and all movies they have played in.");
			System.out.println("2. Movie");
			System.out.println("3. First name");
			System.out.println("4. Last name");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Actor ID
					System.out.print("Actor ID: ");
					
					try {
						int actorID = Utils.getUserSelection(scanner);
						Query.getActorByID(dbConnection, actorID);
						Query.getMoviesByActor(dbConnection, actorID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 3: // 3. First Name
					System.out.print("First name: ");
					
					String firstName = scanner.nextLine();
					try {
						Query.getActorByFirstName(dbConnection, firstName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Last Name
					System.out.print("Last name: ");
					
					String lastName = scanner.nextLine();
					try {
						Query.getActorByLastName(dbConnection, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Customer ---------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchCustomerMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a customer entity using various attributes.
	 */
	private static void displayEmployeeSearchCustomerMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Customer");
			System.out.println("How would you like to search for a customer?");
			System.out.println("----------");
			System.out.println("1. Customer ID");
			System.out.println("   Searching for a customer by ID provides data\n" +
				"   about the customer, all transactions they have made,\n" +
				"   all rentals they have had, and all reviews they have written.");
			System.out.println("2. Transaction");
			System.out.println("3. Review");
			System.out.println("4. Username");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Customer ID
					System.out.print("Customer ID: ");
					
					try {
						int customerID = Utils.getUserSelection(scanner);
						Query.getCustomerByID(dbConnection, customerID);
						Query.getTransactionsByCustomer(dbConnection, customerID);
						Query.getReviewsByCustomer(dbConnection, customerID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Transaction
					displayEmployeeSearchTransactionMenu();
					break;
				case 3: // 3. Review
					displayEmployeeSearchReviewMenu();
					break;
				case 4: // 4. Username
					System.out.print("Username: ");
					
					String username = scanner.nextLine();
					try {
						Query.getCustomerByUsername(dbConnection, username);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Director ---------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchDirectorMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a director entity using various attributes.
	 */
	private static void displayEmployeeSearchDirectorMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Director");
			System.out.println("How would you like to search for a director?");
			System.out.println("----------");
			System.out.println("1. Director ID");
			System.out.println("   Searching for a director by ID provides data about\n" +
				"   the director and all movies they have directed.");
			System.out.println("2. Movie");
			System.out.println("3. First Name");
			System.out.println("4. Last Name");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Director ID
					System.out.print("Director ID: ");
					
					try {
						int directorID = Utils.getUserSelection(scanner);
						Query.getDirectorByID(dbConnection, directorID);
						Query.getMoviesByDirector(dbConnection, directorID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 3: // 3. First Name
					System.out.print("First Name: ");
					
					String firstName = scanner.nextLine();
					try {
						Query.getDirectorByFirstName(dbConnection, firstName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Last Name
					System.out.print("Last Name: ");
					
					String lastName = scanner.nextLine();
					try {
						Query.getDirectorByLastName(dbConnection, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Genre ------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchGenreMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a genre entity using various attributes.
	 */
	private static void displayEmployeeSearchGenreMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Genre");
			System.out.println("How would you like to search for a genre?");
			System.out.println("----------");
			System.out.println("1. Genre ID");
			System.out.println("   Searching for a genre by ID provides data about\n" +
				"   the genre and all movies in the genre.");
			System.out.println("2. Movie");
			System.out.println("3. Genre Type");
			System.out.println("4. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Genre ID
					System.out.print("Genre ID: ");
					
					try {
						int genreID = Utils.getUserSelection(scanner);
						Query.getGenreByID(dbConnection, genreID);
						Query.getMoviesByGenre(dbConnection, genreID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 3: // 3. Genre Type
					System.out.print("Genre: ");
					
					String genre = scanner.nextLine();
					try {
						Query.getGenreByType(dbConnection, genre);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 4: // 4. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Movie ------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchMovieMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a movie entity using various attributes.
	 */
	private static void displayEmployeeSearchMovieMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Movie");
			System.out.println("How would you like to search for a movie?");
			System.out.println("----------");
			System.out.println("1. Movie ID");
			System.out.println("   Searching for a movie by ID provides data about\n" +
				"   the movie, all actors that played in it, all directors that directed it,\n" +
				"   all genres it is classified as, and all transactions including the movie.");
			System.out.println("2. Actor");
			System.out.println("3. Director");
			System.out.println("4. Genre");
			System.out.println("5. Movie Title");
			System.out.println("6. Certificate Rating");
			System.out.println("7. Release Date");
			System.out.println("8. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Movie ID
					System.out.print("Movie ID: ");
					
					try {
						int movieID = Utils.getUserSelection(scanner);
						Query.getMovieByID(dbConnection, movieID);
						Query.getActorsByMovie(dbConnection, movieID);
						Query.getDirectorsByMovie(dbConnection, movieID);
						Query.getGenresByMovie(dbConnection, movieID);
						Query.getTransactionsByMovie(dbConnection, movieID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Actor
					displayEmployeeSearchActorMenu();
					break;
				case 3: // 3. Director
					displayEmployeeSearchDirectorMenu();
					break;
				case 4: // 4. Genre
					displayEmployeeSearchGenreMenu();
					break;
				case 5: // 5. Movie Title
					System.out.print("Movie Title: ");
					
					String movieTitle = scanner.nextLine();
					try {
						Query.getMovieByTitle(dbConnection, movieTitle);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 6: // 6. Certificate Rating
					System.out.print("Certificate Rating: ");
					
					String certificateRating = scanner.nextLine();
					try {
						Query.getMovieByCertificateRating(dbConnection, certificateRating);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 7: // 7. Release Date
					System.out.print("Release Date: ");
					
					String releaseDate = scanner.nextLine();
					try {
						Query.getMovieByReleaseDate(dbConnection, releaseDate);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 8: // 8. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Review -----------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchReviewMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a review entity using various attributes.
	 */
	private static void displayEmployeeSearchReviewMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Review");
			System.out.println("How would you like to search for a review?");
			System.out.println("----------");
			System.out.println("1. Review ID");
			System.out.println("   Searching for a review by ID provides data\n" +
				"   about the review, the customer who wrote it, and the movie\n" +
				"   it was written about.");
			System.out.println("2. Customer");
			System.out.println("3. Movie");
			System.out.println("4. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Review ID
					System.out.print("Review ID: ");
					
					try {
						int reviewID = Utils.getUserSelection(scanner);
						Query.getReviewByID(dbConnection, reviewID);
						Query.getCustomerByReview(dbConnection, reviewID);
						Query.getMovieByReview(dbConnection, reviewID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break;
					}
					break;
				case 2: // 2. Customer
					displayEmployeeSearchCustomerMenu();
					break;
				case 3: // 3. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 4: // 4. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Search - Transaction ------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeSearchTransactionMenu is a sub-menu of displayEmployeeSearchMenu.
	 * This menu allows the user to search for a transaction entity using various attributes.
	 */
	private static void displayEmployeeSearchTransactionMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Search / Transaction");
			System.out.println("How would you like to search for a transaction?");
			System.out.println("----------");
			System.out.println("1. Transaction ID");
			System.out.println("   Searching for a transaction by ID provides data about\n" +
				"   the customer, the transaction, and any data about a rental if there was one.");
			System.out.println("2. Customer");
			System.out.println("3. Movie");
			System.out.println("4. Transaction Date");
			System.out.println("5. Back");
			
			int	selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Transaction ID
					System.out.print("Transaction ID: ");
					
					try {
						int transactionID = Utils.getUserSelection(scanner);
						Query.getCustomerByTransaction(dbConnection, transactionID);
						Query.getTransactionByID(dbConnection, transactionID);
						Query.getMovieByTransaction(dbConnection, transactionID);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					break;
				case 2: // 2. Customer
					displayEmployeeSearchCustomerMenu();
					break;
				case 3: // 3. Movie
					displayEmployeeSearchMovieMenu();
					break;
				case 4: // 4. Transaction Date
					System.out.print("Transaction date (yyyy-mm-dd): ");
					
					String transactionDate = scanner.nextLine();
					try {
						Query.getTransactionByTransactionDate(dbConnection, transactionDate);
					}
					catch (InputMismatchException error) {
						System.out.println("Not a valid ID. Please try again.");
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					break;
				case 5: // 5. Back
					return;
			}
		} while (true);
	}
	
	// Employee - Business Management -------------------------------------------------------------
	
	private static void displayEmployeeBusinessManagementMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Business Management");
			System.out.println("What would you like to change?");
			System.out.println("----------");
			System.out.println("1. New Release Rental Rate");
			System.out.println("2. Non-New Release Rental Rate");
			System.out.println("3. New Release Rental Period");
			System.out.println("4. Non-New Release Rental Period");
			System.out.println("5. Late Fee Added Per Late Rental Day");
			System.out.println("6. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
			case 1:
				try {
					Query.getConfigNewReleaseRentalRate(dbConnection);
					System.out.print("New Value: ");
					double newReleaseRentalRate = Double.parseDouble(scanner.nextLine());
					Query.setConfigNewReleaseRentalRate(dbConnection, newReleaseRentalRate);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				break;
			case 2:
				try {
					Query.getConfigNonNewReleaseRentalRate(dbConnection);
					System.out.print("New Value: ");
					double nonNewReleaseRentalRate = Double.parseDouble(scanner.nextLine());
					Query.setConfigNonNewReleaseRentalRate(dbConnection, nonNewReleaseRentalRate);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				break;
			case 3:
				try {
					Query.getConfigNewReleaseRentalPeriod(dbConnection);
					System.out.print("New Value: ");
					int newReleaseRentalPeriod = Integer.parseInt(scanner.nextLine());
					Query.setConfigNewReleaseRentalPeriod(dbConnection, newReleaseRentalPeriod);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				break;
			case 4:
				try {
					Query.getConfigNonNewReleaseRentalPeriod(dbConnection);
					System.out.print("New Value: ");
					int nonNewReleaseRentalPeriod = Integer.parseInt(scanner.nextLine());
					Query.setConfigNonNewReleaseRentalPeriod(dbConnection, nonNewReleaseRentalPeriod);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				break;
			case 5:
				try {
					Query.getConfigLateFeePerDay(dbConnection);
					System.out.print("New Value: ");
					double newLateFeePerDay = Double.parseDouble(scanner.nextLine());
					Query.setConfigLateFeePerDay(dbConnection, newLateFeePerDay);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				break;
			case 6:
				return; // back to employee dashboard
			}
		} while(true);
	}
	
	// Employee - Add -----------------------------------------------------------------------------
	
	/**
	 * Complete and tested
	 * displayEmployeeAddMenu is a sub-menu of displayEmployeeDashboard.
	 * This menu provides options to add a movie, add an actor, add a genre,
	 * add a director, or return to the Employee Dashboard.
	 */
	public static void displayEmployeeAddMenu() {
		do {
			String firstName;
			String lastName;
			
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Add");
			System.out.println("What would you like to add?");
			System.out.println("----------");
			System.out.println("1. New Actor");
			System.out.println("2. New Director");
			System.out.println("3. New Genre");
			System.out.println("4. New Movie");
			System.out.println("5. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. New Actor
					System.out.print("Actor first name: ");
					firstName = scanner.nextLine();
					
					System.out.print("Actor last name: ");
					lastName = scanner.nextLine();
					
					try {
						Query.insertActor(
								dbConnection, firstName, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					catch (LogicException error) {
						System.out.println(error.getMessage());
					}
					
					break;
				case 2: // 2. New Director
					System.out.print("Director first name: ");
					firstName = scanner.nextLine();
					
					System.out.print("Director last name: ");
					lastName = scanner.nextLine();
					
					try {
						Query.insertDirector(
								dbConnection, firstName, lastName);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					break;
				case 3: // 3. New Genre
					System.out.print("Genre: ");
					String genre = scanner.nextLine();
					
					try {
						Query.insertGenre(
								dbConnection, genre);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					break;
				case 4: // 4. New Movie
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
					
					System.out.print("Is this movie digital or physical? (d/p): ");
					scanner.nextLine();
					String response = scanner.nextLine();
					String format;
					
					if (response.equals("d")) {
						format = "Digital";
					}
					else if (response.equals("p")) {
						format = "Physical";
					}
					else {
						System.out.println("Please enter the requested value.");
						break;
					}
					
					try {
						Query.insertMovie(
								dbConnection,
								movieTitle,
								movieReleaseDate,
								movieCertificateRating,
								movieBusinessCost,
								movieCustomerPurchaseCost,
								movieCustomerRentCost,
								format);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}

					break;
				case 5: // 5. Back
					return; // back to the employee dashboard
			}
		} while (true);
	}
	
	// Employee - Update --------------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * displayEmployeeUpdateMenu is a sub-menu of displayEmployeeDashboard.
	 * This menu provides options to update a movie, actor, director, genre.
	 */
	public static void displayEmployeeUpdateMenu() {
		do {
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Update");
			System.out.println("What would you like to update?");
			System.out.println("----------");
			System.out.println("1. Update actor");
			System.out.println("2. Update director");
			System.out.println("3. Update genre");
			System.out.println("4. Update movie");
			System.out.println("5. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Update Actor
					System.out.print("Actor ID: ");
					int actorID = Utils.getUserSelection(scanner);
					
					try {
						if (Query.isExistingActor(dbConnection, actorID)) {
							displayEmployeeUpdateActorMenu(actorID);
						}
						else {
							System.out.println("Actor with ID " + actorID + " not found. Please try again.");
						}
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					
					break;
				case 2: // 2. Update Director
					System.out.print("Director ID: ");
					int directorID = Utils.getUserSelection(scanner);
					
					try {
						if (Query.isExistingDirector(dbConnection, directorID)) {
							displayEmployeeUpdateDirectorMenu(directorID);
						}
						else {
							System.out.println("Director with ID " + directorID + " not found. Please try again.");
						}
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					
					break;
				case 3: // 3. Update Genre
					System.out.print("Genre ID: ");
					int genreID = Utils.getUserSelection(scanner);
					
					try {
						if (Query.isExistingGenre(dbConnection, genreID)) {
							displayEmployeeUpdateGenreMenu(genreID);
						}
						else {
							System.out.println("Genre with ID " + genreID + " not found. Please try again.");
						}
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					
					break;
				case 4: // 4. Update movie
					System.out.print("Movie ID: ");
					int movieID = Utils.getUserSelection(scanner);
					
					try {
						if (Query.isExistingMovie(dbConnection, movieID)) {
							displayEmployeeUpdateMovieMenu(movieID);
						}
						else {
							System.out.println("Movie with ID " + movieID + " not found. Please try again.");
						}
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
					}
					
					break;
				case 5:
					return; // back to the Employee Dashboard
			}
		} while (true);
	}
	
	// Employee - Update - Actor ------------------------------------------------------------------
	
	/**
	 * Might like to update this a little.
	 * Finished and tested.
	 * displayEmployeeUpdateActorMenu is a sub-menu of displayEmployeeUpdateMenu which handles
	 * updating an actor of specific actorID.
	 * @param actorID The actor to update
	 */
	public static void displayEmployeeUpdateActorMenu(int actorID) {
		do {
			try {
				Query.getActorByID(dbConnection, actorID);
			}
			catch (SQLException error) {
				Utils.printDatabaseError(error);
				return; // back to the displayEmployeeUpdateMenu
			}
			
			System.out.println("What would you like to update about this actor?");
			System.out.println("1. First name");
			System.out.println("2. Last name");
			System.out.println("3. Finish updating this actor");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. First Name
					System.out.print("New First Name: ");
					
					String newActorFirstName = scanner.nextLine();
					try {
						Query.setActorFirstName(dbConnection, newActorFirstName, actorID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateActorMenu
					}
					break; // take me back to the displayUpdateActorMenu
				case 2: // 2. Last Name
					System.out.print("New Last Name: ");
					
					String newActorLastName = scanner.nextLine();
					try {
						Query.setActorLastName(dbConnection, newActorLastName, actorID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateActorMenu
					}
					break; // take me back to the displayUpdateActorMenu
				case 3: // 3. Finish updating this actor
					return; // take me back to the displayUpdateItemMenu
			}
		} while (true);
	}
	
	// Employee - Update - Director ---------------------------------------------------------------
	
	/**
	 * Might like to update this a little
	 * Complete and tested.
	 * displayUpdateDirectorMenu is a sub-menu of displayUpdateItemMenu which handles
	 * updating an director of specific directorID.
	 * @param directorID The director to update
	 */
	public static void displayEmployeeUpdateDirectorMenu(int directorID) {
		do {
			try {
				Query.getDirectorByID(dbConnection, directorID);
			}
			catch (SQLException error) {
				error.printStackTrace();
				System.out.println("A database error was encountered. " +
					"Please try again or contact your system administrator.");
				return; // take me back to the displayUpdateItemMenu
			}
			
			System.out.println("What would you like to update about this actor?");
			System.out.println("1. First name");
			System.out.println("2. Last name");
			System.out.println("3. Finish updating this director");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. First Name
					System.out.print("New First Name: ");
					
					String newDirectorFirstName = scanner.nextLine();
					try {
						Query.setDirectorFirstName(dbConnection, newDirectorFirstName, directorID);
					}
					catch (SQLException error) {
						Utils.printDatabaseError(error);
						break; // take me back to the displayUpdateDirectorMenu
					}
					break; // take me back to the displayUpdateDirectorMenu
				case 2: // 2. Last Name
					System.out.print("New Last Name: ");
					
					String newDirectorLastName = scanner.nextLine();
					try {
						Query.setDirectorLastName(dbConnection, newDirectorLastName, directorID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateDirectorMenu
					}
					break; // take me back to the displayUpdateDirectorMenu
				case 3: // 3. Finish updating this director
					return; // take me back to the displayUpdateItemMenu
			}
		} while (true);
	}
	
	// Employee - Update - Genre ------------------------------------------------------------------
	
	/**
	 * Might like to update this a little
	 * Complete and tested.
	 * displayUpdateGenreMenu is a sub-menu of displayUpdateItemMenu which handles
	 * updating a genre of specific genreID.
	 * @param genreID The director to update
	 */
	public static void displayEmployeeUpdateGenreMenu(int genreID) {
		do {
			try {
				Query.getGenreByID(dbConnection, genreID);
			}
			catch (SQLException error) {
				error.printStackTrace();
				System.out.println("A database error was encountered. " +
					"Please try again or contact your system administrator.");
				return; // take me back to the displayUpdateItemMenu
			}
			
			System.out.println("What would you like to update about this genre?");
			System.out.println("1. Genre type");
			System.out.println("2. Finish updating this genre");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. First Name
					System.out.print("New Genre Type: ");
					
					String newGenreType = scanner.nextLine();
					try {
						Query.setGenreType(dbConnection, newGenreType, genreID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateDirectorMenu
					}
					break; // take me back to the displayUpdateDirectorMenu
				case 2: // 2. Finish updating this genre
					return; // take me back to the displayUpdateItemMenu
			}
		} while (true);
	}
	
	// Employee - Update - Movie ------------------------------------------------------------------
	
	/**
	 * Might like to update this a little
	 * Finished and tested.
	 * displayUpdateMovieMenu is a sub-menu of displayUpdateItemMenu which handles
	 * updating a movie of specific movieID.
	 * @param movieID The movie to update
	 */
	public static void displayEmployeeUpdateMovieMenu(int movieID) {
		do {
			try {
				Query.getMovieByID(dbConnection, movieID);
			}
			catch (SQLException error) {
				error.printStackTrace();
				System.out.println("A database error was encountered. " +
					"Please try again or contact your system administrator.");
				return; // take me back to the displayUpdateItemMenu
			}
			
			System.out.println("What would you like to update about this movie?");
			System.out.println("1. Title");
			System.out.println("2. Release date");
			System.out.println("3. Certificate Rating");
			System.out.println("4. Business cost per item");
			System.out.println("5. Customer rental cost");
			System.out.println("6. Customer purchase cost");
			System.out.println("7. Stock");
			System.out.println("8. Finish updating this movie");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
				case 1: // 1. Title
					System.out.print("New Title: ");
					
					String newMovieTitle = scanner.nextLine();
					try {
						Query.setMovieTitle(dbConnection, newMovieTitle, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 2: // 2. Release date
					System.out.print("New Release Date (yyyy-mm-dd): ");
					
					String newReleaseDate = scanner.nextLine();
					try {
						Query.setMovieReleaseDate(dbConnection, newReleaseDate, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 3: // 3. Certificate Rating
					System.out.print("New Certificate Rating: ");
					
					String newCertificateRating = scanner.nextLine();
					try {
						Query.setMovieCertificateRating(dbConnection, newCertificateRating, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 4: // 4. Business cost per item
					System.out.print("New Business Cost per Item: ");
					
					Double newBusinessCost = scanner.nextDouble();
					try {
						Query.setMovieBusinessCost(dbConnection, newBusinessCost, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 5: // 5. Customer rental cost
					System.out.print("New Customer Rental Cost: ");
					
					Double newRentalCost = scanner.nextDouble();
					try {
						Query.setMovieRentalCost(dbConnection, newRentalCost, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 6: // 6. Customer purchase cost
					System.out.print("New Customer Purchase Cost: ");
					
					Double newPurchaseCost = scanner.nextDouble();
					try {
						Query.setMoviePurchaseCost(dbConnection, newPurchaseCost, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 7: // 7. Stock
					System.out.print("New Stock: ");
					
					int newStock = Utils.getUserSelection(scanner);
					try {
						Query.setMovieStock(dbConnection, newStock, movieID);
					}
					catch (SQLException error) {
						error.printStackTrace();
						System.out.println("A database error was encountered. " +
							"Please try again or contact your system administrator.");
						break; // take me back to the displayUpdateMovieMenu
					}
					break; // take me back to the displayUpdateMovieMenu
				case 8: // 8. Finish updating this movie
					return; // take me back to the displayUpdateItemMenu
			}
		} while (true);
	}
	
	// Employee - Link ----------------------------------------------------------------------------
	
	/**
	 * Complete and tested.
	 * diplsayEmployeeLinkMenu is a sub-menu of the employee dashboard.
	 * It exists to allow an employee to create a relationship between
	 * an actor, director, or genre with a movie.
	 */
	private static void displayEmployeeLinkMenu() {
		do {
			int movieID;
			
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Link");
			System.out.println("What would you like to link together?");
			System.out.println("----------");
			System.out.println("1. Add an actor to a movie");
			System.out.println("2. Add a director to a movie");
			System.out.println("3. Add a genre to a movie");
			System.out.println("4. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
			case 1: // 1. Add an actor to a movie
				System.out.print("Movie ID: ");
				movieID = Utils.getUserSelection(scanner);
				
				System.out.print("Actor ID: ");
				int actorID = Utils.getUserSelection(scanner);
				
				try {
					if (!Query.isExistingMovie(dbConnection, movieID)) {
						System.out.println("Movie with ID " + movieID + " does not exist.");
						break;
					}
					if (!Query.isExistingActor(dbConnection, actorID)) {
						System.out.println("Actor with ID " + actorID + " does not exist.");
						break;
					}
					
					Query.getMovieByID(dbConnection, movieID);
					Query.getActorByID(dbConnection, actorID);
					
					System.out.print("Are you sure you want to add this actor to this movie? (y/n):");
					String response = scanner.nextLine();
					
					if (response.equals("y")) {
						Query.insertMovieActor(dbConnection, movieID, actorID);
					}
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				
				break;
			case 2: // 2. Add a director to a movie
				System.out.print("Movie ID: ");
				movieID = Utils.getUserSelection(scanner);
				
				System.out.print("Director ID: ");
				int directorID = Utils.getUserSelection(scanner);
				
				try {
					if (!Query.isExistingMovie(dbConnection, movieID)) {
						System.out.println("Movie with ID " + movieID + " does not exist.");
						break;
					}
					if (!Query.isExistingDirector(dbConnection, directorID)) {
						System.out.println("Director with ID " + directorID + " does not exist.");
						break;
					}
					
					Query.getMovieByID(dbConnection, movieID);
					Query.getDirectorByID(dbConnection, directorID);
					
					System.out.print("Are you sure you want to add this director to this movie? (y/n):");
					String response = scanner.nextLine();
					
					if (response.equals("y")) {
						Query.insertMovieDirector(dbConnection, movieID, directorID);
					}
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				
				break;
			case 3: // 3. Add a genre to a movie
				System.out.print("Movie ID: ");
				movieID = Utils.getUserSelection(scanner);
				
				System.out.print("Genre ID: ");
				int genreID = Utils.getUserSelection(scanner);
				
				try {
					if (!Query.isExistingMovie(dbConnection, movieID)) {
						System.out.println("Movie with ID " + movieID + " does not exist.");
						break;
					}
					if (!Query.isExistingGenre(dbConnection, genreID)) {
						System.out.println("Genre with ID " + genreID + " does not exist.");
						break;
					}
					
					Query.getMovieByID(dbConnection, movieID);
					Query.getGenreByID(dbConnection, genreID);
					
					System.out.print("Are you sure you want to add this genre to this movie? (y/n):");
					String response = scanner.nextLine();
					
					if (response.equals("y")) {
						Query.insertMovieGenre(dbConnection, movieID, genreID);
					}
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				
				break;
			case 4:
				return; // back to employee dashboard
			}
		} while(true);
	}
	
	// Employee - Customer Management -------------------------------------------------------------
	
	private static void displayEmployeeCustomerManagement(int customerID) {
		do {
			try {
				Query.getCustomerByID(dbConnection, customerID);
			}
			catch (SQLException error) {
				Utils.printDatabaseError(error);
				return; // back to the employee dashboard
			}
			
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Customer Management");
			System.out.println("How would you like to manage this customer?");
			System.out.println("----------");
			System.out.println("1. Check balance");
			System.out.println("2. Apply late fee");
			System.out.println("3. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
			case 1: // 1. Check Balance
				try {
					System.out.println("Customer balance: " + Query.getCustomerBalance(dbConnection, customerID));
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 2: // 2. Apply late fees
				System.out.print("Transaction ID: ");
				int transactionID = Utils.getUserSelection(scanner);
				
				System.out.print("Addition to current customer balance: ");
				try {
					double customerBalanceAddition = Double.parseDouble(scanner.nextLine());
					Query.applyLateFee(dbConnection, transactionID, customerBalanceAddition);
					System.out.println("Late fee successfully applied.");
				}
				catch (NumberFormatException error) {
					System.out.println("Invalid selection. Please try again.");
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 3:
				return; // back to the employee dashboard
			}
		} while(true);
	}
	
	private static void displayEmployeeReportsMenu() {
		do {
			int genreID;
			
			System.out.println("----------");
			System.out.println("Home / Employee Dashboard / Reports");
			System.out.println("Which report would you like to run?");
			System.out.println("----------");
			System.out.println("1. Generate revenue report by movie for sales and rentals");
			System.out.println("2. Generate revenue report by genre for sales and rentals");
			System.out.println("3. Generate weekly revenue report by genre for sales and rentals");
			System.out.println("4. Generate monthly revenue report by genre for sales and rentals");
			System.out.println("5. Generate yearly revenue report by genre for sales and rentals");
			System.out.println("6. Back");
			
			int selection = Utils.getUserSelection(scanner);
			switch (selection) {
			case 1:
				System.out.print("Movie ID: ");
				int movieID = Utils.getUserSelection(scanner);
				
				try {
					Query.getTransactionsByMovie(dbConnection, movieID);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 2:
				System.out.print("Genre ID: ");
				genreID = Utils.getUserSelection(scanner);
				
				try {
					Query.getGenreByID(dbConnection, genreID);
					Query.getTransactionsByGenre(dbConnection, genreID);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 3:
				System.out.print("Genre ID: ");
				genreID = Utils.getUserSelection(scanner);
				
				try {
					Query.getGenreByID(dbConnection, genreID);
					Query.printWeeklyGenreRevenueReport(dbConnection, genreID);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 4:
				System.out.print("Genre ID: ");
				genreID = Utils.getUserSelection(scanner);
				
				try {
					Query.printMonthlyGenreRevenueReport(dbConnection, genreID);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 5:
				System.out.print("Genre ID: ");
				genreID = Utils.getUserSelection(scanner);
				
				try {
					Query.printYearlyGenreRevenueReport(dbConnection, genreID);
				}
				catch (SQLException error) {
					Utils.printDatabaseError(error);
				}
				break;
			case 6:
				return; // back to the employee dashboard
			}
		} while (true);
	}
}
