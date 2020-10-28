import java.sql.*;

public class Query {

	/**
	 * isExistingCustomer determines whether the provided username and password reference an existing Customer in the database.
	 * @param connection The connection to the database
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingCustomer(Connection connection, String username, String password) throws SQLException {
		try {
			return isExistingUser("SELECT Username, Password FROM Customers", username, password, connection);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExistingEmployee determines whether the provided username and password reference an existing Employee in the database.
	 * @param connection
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingEmployee(Connection connection, String username, String password) throws SQLException {
		try {
			return isExistingUser("SELECT Username, Password FROM Employees", username, password, connection);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Attempts to add a new customer to the database with provided customer information
	 * only after verifying that the provided username is unique, and that the provided
	 * referencedBy user exists. 
	 * @param connection The database connection object
	 * @param username The new customer's chosen username
	 * @param password The new customer's chosen password
	 * @param firstName The new customer's first name
	 * @param lastName The new customer's last name
	 * @param referencedBy The customer's username that referenced this new customer.
	 * @throws SQLException A fatal error encountered while communicating with the database
	 * @throws LogicException A logical error encountered that can be corrected - such as a username already being taken.
	 */
	public static void createNewCustomer(Connection connection, String username, String password, String firstName, String lastName, String referredBy) throws SQLException, LogicException {
		// Set up query environment
		Statement statement = null;
		ResultSet result = null;
		String verificationQuery = "SELECT Username FROM Customers";
		String addNewUser = null;
		if (referredBy.equals("")) {
			addNewUser = "INSERT INTO Customers (FirstName, LastName, Username, Password) VALUES (\"" + firstName + "\", \"" + lastName + "\", \"" + username + "\", \"" + password + "\")";
		}
		else {
			addNewUser = "INSERT INTO Customers (FirstName, LastName, Username, Password, ReferredBy) VALUES (\"" + firstName + "\", \"" + lastName + "\", \"" + username + "\", \"" + password + "\", \"" + referredBy + "\")";
		}
		String currentUser = null;
		boolean referringCustomerExists = false;
		
		// Make sure user can be created as specified, and do so if possible
		// 1. Usernames must be unique
		// 2. Referring customer must exist
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(verificationQuery);
			while (result.next()) {
				currentUser = result.getString("Username");
				if (currentUser.equals(username)) {
					throw new LogicException("Username already in use. Please try again.");
				}
				if (currentUser.equals(referredBy)) {
					referringCustomerExists = true;
				}
			}
			if (referringCustomerExists == false && !referredBy.equals("")) {
				throw new LogicException("Referenced customer does not exist. Please try again.");
			}
			result.close();
			
			statement.executeUpdate(addNewUser);
			result.close();
		}
		catch (SQLException error) {
			throw error;
		}
		catch (LogicException error) {
			throw error;
		}
		finally {
			statement.close();
		}
	}
	
	/**
	 * getMovieByID prints out the details of the movie with relevant ID
	 * @param connection The connection object
	 * @param movieID The movie to search for
	 * @throws SQLException
	 */
	public static void getMovieByID(Connection connection, int movieID) throws SQLException {
		String query = "SELECT * FROM Movies WHERE MovieID = " + movieID;
		try {
			getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMovieByTitle prints out the details of the movie with relevant Title.
	 * @param connection The connection object
	 * @param movieTitle The movie to search for
	 * @throws SQLException
	 */
	public static void getMovieByTitle(Connection connection, String movieTitle) throws SQLException {
		String query = "SELECT * FROM Movies WHERE MovieTitle = \"" + movieTitle + "\"";
		try {
			getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * A helper function to be used by various getMovieByX() functions which accepts a query to search for a
	 * movie by a particular value, and prints out ALL details of the relevant movie.
	 * Note: The customer should never see all these details.
	 * @param connection The connection object
	 * @param query Must be of the form SELECT * FROM Movies BY {value}
	 * @throws SQLException
	 */
	private static void getMovie(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("{");
				System.out.println("    MovieID: " + result.getInt("MovieID") + ",");
				System.out.println("    MovieTitle: " + result.getString("MovieTitle") + ",");
				System.out.println("    MovieYear: " + result.getString("MovieYear") + ",");
				System.out.println("    CertificateRating: " + result.getString("CertificateRating") + ",");
				System.out.println("    RentPrice: " + result.getDouble("RentPrice") + ",");
				System.out.println("    BuyPrice: " + result.getDouble("BuyPrice") + ",");
				System.out.println("    MovieValue: " + result.getDouble("MovieValue") + ",");
				System.out.println("    Stock: " + result.getInt("Stock") + ",");
				System.out.println("    ReleaseDate: " + result.getString("ReleaseDate") + ",");
				System.out.println("    OverallReviewRating: " + result.getDouble("OverallReviewRating"));
				System.out.println("}");
			}
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	/**
	 * isExistingUser is a utility function to be used in
	 * conjunction with isExistingEmployee() & isExistingCustomer()
	 * @param query A SQL query of the form SELECT Username, Password FROM Table, where Table is either Employees or Customers
	 * @param username The username of the user to find
	 * @param password The password of the user to find
	 * @param connection The database connection object
	 * @return
	 * @throws SQLException
	 */
	private static boolean isExistingUser(String query, String username, String password, Connection connection) throws SQLException {
		// Set up query environment
		Statement statement = null;
		ResultSet result = null;
		boolean matchFound = false;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			while (result.next()) {
				if (result.getString("Username").equals(username) &&
					result.getString("Password").equals(password)) {
					matchFound = true;
					break;
				}
			}
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
		return matchFound;
	}

	/**
	 * Adds a new movie to the database with provided details.
	 * @param connection The database connection object
	 * @param movieTitle The title of the movie to add
	 * @param movieReleaseDate The release date of the movie to add
	 * @param movieCertificateRating The certificate rating of the movie to add
	 * @param movieBusinessCost The cost the business pays for each of this movie
	 * @param movieCustomerPurchaseCost The cost the customer pays to purchase this movie
	 * @param movieCustomerRentCost The cost the customer pays to rent this movie
	 * @throws SQLException
	 */
	public static void insertMovie(Connection connection, String movieTitle, String movieReleaseDate, String movieCertificateRating, double movieBusinessCost, double movieCustomerPurchaseCost, double movieCustomerRentCost) throws SQLException {
		Statement statement = null;
		
		String movieYear = movieReleaseDate.split("-")[0];

		String addNewMovie = "INSERT INTO Movies (MovieTitle, MovieYear, CertificateRating, RentPrice, BuyPrice, MovieValue, ReleaseDate) VALUE (\"" + movieTitle + "\", \"" + movieYear + "\", \"" + movieCertificateRating + "\", \"" + movieCustomerRentCost + "\", \"" + movieCustomerPurchaseCost + "\", \"" + movieBusinessCost + "\", \"" + movieReleaseDate + "\")";
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(addNewMovie);
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			statement.close();
		}
	}

public static void insertActor(Connection dbConnection, String firstName, String lastName) {
	// TODO Auto-generated method stub
	
}

public static void insertGenre(Connection dbConnection, String genre) {
	// TODO Auto-generated method stub
	
}

public static void insertDirector(Connection dbConnection, String firstName, String lastName) {
	// TODO Auto-generated method stub
	
}
}