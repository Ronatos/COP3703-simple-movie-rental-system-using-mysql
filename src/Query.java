import java.sql.*;

/*
 * x = completed
 * 
 * isExistingGenre(x)
 * isExistingDirector(x)
 * getActorByFirstName(x)
 * getActorByLastName(x)
 * getGenreByID(x)
 * getGenreByType(x)
 * getGenre(x) <-- helper function?
 * getDirectorByID()
 * getDirectorByFirstName()
 * getDirectorByLastName()
 * getDirector() <-- helper function?
 * setGenreType()
 * setDirectorFirstName()
 * setDirectorLastName()
 * deleteMovieByID()
 * deleteActorByID()
 * deleteGenreByID()
 * deleteDirectorByID()
 * deleteItem() <-- helper function?
 */

public class Query {

	// isExisting ---------------------------------------------------------------------------------
	
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
	 * isExistingMovie determines whether the provided movieID references an existing movie in the database.
	 * @param connection The database connection object
	 * @param movieID The MovieID to search
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingMovie(Connection connection, int movieID) throws SQLException {
		String query = "SELECT * FROM Movies WHERE MovieID = " + movieID;
		try {
			return isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExistingActor determines whether the provided actorID references an existing actor in the database.
	 * @param connection The database connection object
	 * @param actorID The actor to search for
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingActor(Connection connection, int actorID) throws SQLException {
		String query = "SELECT * FROM Actors WHERE ActorID = " + actorID;
		try {
			return isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExsistingGenre determines whether the provided genreID references an existing genre in the database
	 * @param connection The database connection object
	 * @param genreID The genre to search for
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingGenre(Connection connection, int genreID) throws SQLException {
		String query = "SELECT * FROM Genres WHERE GenreID = " + genreID;
		try {
			return isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExistingDirector determines whether the provided director references an existing director in the database
	 * @param connection The database connection object
	 * @param directorID The director to search for
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingDirector(Connection connection, int directorID)throws SQLException {
		String query = "SELECT * FROM Directors WHERE DirectorID = " + directorID;
		try {
			return isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExisting is a utility function to be used in
	 * conjuction with isExisting functions to determine
	 * if a given query returned any results.
	 * @param connection The database connection object
	 * @param query Query should be of the form SELECT {values} FROM {tables}
	 * @return
	 * @throws SQLException
	 */
	private static boolean isExisting(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			if (result.next()) {
				return true;
			}
			else {
				return false;
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
	
	// --------------------------------------------------------------------------------------------
	
	// Getters ------------------------------------------------------------------------------------
	
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
	 * getActorByID prints out the details of the actor with relevant ID.
	 * @param connection The database connection object
	 * @param actorID The actor to search for
	 * @throws SQLException
	 */
	public static void getActorByID(Connection connection, int actorID) throws SQLException {
		String query = "SELECT * FROM Actors WHERE ActorID = " + actorID;
		try {
			getActor(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**\
	 * getActorByFirstName prints out the details of the actor with relevant first name
	 * @param connection The database connection object
	 * @param firstName The actor's first name to search for
	 * @throws SQLException
	 */
	public static void getActorByFirstName(Connection connection, String firstName) throws SQLException  {
		String query = "SELECT * FROM Actors WHERE FirstName = \"" + firstName + "\"";
		try {
			getActor(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getActorByLastName prints out the details of the actor with the relevant last name
	 * @param connection The database connection object
	 * @param lastName The actor's last name to search for
	 * @throws SQLException
	 */
	public static void getActorByLastName(Connection connection, String lastName) throws SQLException {
		String query = "SELECT * FROM Actors WHERE LastName = \"" + lastName + "\"";
		try {
			getActor(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getDirectorByID prints out the details of the director with relevant ID.
	 * @param connection The database connection object
	 * @param actorID The actor to search for
	 * @throws SQLException
	 */
	public static void getDirectorByID(Connection connection, int DirectorID) throws SQLException {
		String query = "SELECT * FROM Directors WHERE DirectorID = " + DirectorID;
		try {
			getDirector(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getGenreByID prints out the details of the genre with the relevant ID
	 * @param connection The database connection object
	 * @param genreID The genre's ID to search for
	 * @throws SQLException
	 */
	public static void getGenreByID(Connection connection, int genreID)throws SQLException {
		String query = "SELECT * FROM Genres WHERE GenreID = " + genreID;
		try {
			getGenre(connection, query);
		}
		catch (SQLException error){
			throw error;
		}
	}
	
	
	
	/**
	 * getGenreByType prints out the details of the genre with the relevant type
	 * @param connection The database connection object
	 * @param genreID The genre's type to search for
	 * @throws SQLException
	 */
	public static void getGenreByType(Connection connection, String genreType)throws SQLException {
		String query = "SELECT * FROM Genres WHERE GenreType = \"" + genreType + "\"";
		try {
			getGenre(connection, query);
		}
		catch (SQLException error){
			throw error;
		}
	}
	
	
	/**
	 * A helper function to be used by various getActorByX() functions which accepts a query to search for an
	 * actor by a particular value, and prints out ALL details of the relevant actor.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Actors BY {value}
	 * @throws SQLException
	 */
	private static void getActor(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("{");
				System.out.println("    ActorID: " + result.getInt("ActorID") + ",");
				System.out.println("    FirstName: " + result.getString("FirstName") + ",");
				System.out.println("    LastName: " + result.getString("LastName") + ",");
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
	 * A helper function to be used by various getDirectorByX() functions which accepts a query to search for a
	 * Director by a particular value, and prints out ALL details of the relevant Director.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Director BY {value}
	 * @throws SQLException
	 */
	private static void getDirector(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("{");
				System.out.println("    DirectorID: " + result.getInt("DirectorID") + ",");
				System.out.println("    FirstName: " + result.getString("FirstName") + ",");
				System.out.println("    LastName: " + result.getString("LastName") + ",");
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
	 * A helper function to be used by various getGenreByX() functions which accepts a query to search for an
	 * genre by a particular value, and prints out ALL details of the relevant genre.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Genres BY {value}
	 * @throws SQLException
	 */
	private static void getGenre(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while(result.next()) {
				System.out.println("{");
				System.out.println("    Genre: " + result.getInt("GenreID") + ",");
				System.out.println("    GenreType: " + result.getString("GenreType") + ",");
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
	
	// --------------------------------------------------------------------------------------------
	
	// Setters ------------------------------------------------------------------------------------
	
	/**
	 * Sets a new FirstName value for the specified ActorID
	 * @param connection The database connection object
	 * @param newActorFirstName The actor's new first name
	 * @param actorID The actor to update
	 * @throws SQLException
	 */
	public static void setActorFirstName(Connection connection, String newActorFirstName, int actorID) throws SQLException {
		String query = "UPDATE Actors SET FirstName = \"" + newActorFirstName + "\" WHERE ActorID = " + actorID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets a new LastName value for the specified ActorID
	 * @param connection The database connection object
	 * @param newActorLastName The actor's new first name
	 * @param actorID The actor to update
	 * @throws SQLException
	 */
	public static void setActorLastName(Connection connection, String newActorLastName, int actorID) throws SQLException {
		String query = "UPDATE Actors SET LastName = \"" + newActorLastName + "\" WHERE ActorID = " + actorID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets a new FirstName value for the specified DirectorID
	 * @param connection The database connection object
	 * @param newDirectorFirstName The director's new first name
	 * @param directorID The director to update
	 * @throws SQLException
	 */
	public static void setDirectorFirstName(Connection connection, String newDirectorFirstName, int directorID) throws SQLException {
		String query = "UPDATE Director SET FirstName = \"" + newDirectorFirstName + "\" WHERE DirectorID = " + directorID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets a new LastName value for the specified DirectorID
	 * @param connection The database connection object
	 * @param newDirectorLastName The director's new last name
	 * @param directorID The director to update
	 * @throws SQLException
	 */
	public static void setDirectorLastName(Connection connection, String newDirectorLastName, int directorID) throws SQLException {
		String query = "UPDATE Director SET LastName = \"" + newDirectorLastName + "\" WHERE DirectorID = " + directorID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets a new MovieTitle value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieTitle The new MovieTitle value
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieTitle(Connection connection, String newMovieTitle, int movieID) throws SQLException {
		String query = "UPDATE Movies SET MovieTitle = \"" + newMovieTitle + "\" WHERE MovieID = " + movieID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new MovieYear and ReleaseDate values for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieReleaseDate The new ReleaseDate value
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieReleaseDate(Connection connection, String newMovieReleaseDate, int movieID) throws SQLException {
		String newMovieYear = newMovieReleaseDate.split("-")[0];
		String query = "UPDATE Movies SET ReleaseDate = \"" + newMovieReleaseDate + "\", MovieYear = \"" + newMovieYear + "\" WHERE MovieID = " + movieID;
		
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new CertificateRating value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieCertificateRating The new CertificateRating value
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieCertificateRating(Connection connection, String newMovieCertificateRating, int movieID) throws SQLException {
		String query = "UPDATE Movies SET CertificateRating = \"" + newMovieCertificateRating + "\" WHERE MovieID = " + movieID;
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new MovieValue value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieBusinessCost The new price the business pays for this item
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieBusinessCost(Connection connection, Double newMovieBusinessCost, int movieID) throws SQLException {
		String query = "UPDATE Movies SET MovieValue = \"" + newMovieBusinessCost + "\" WHERE MovieID = " + movieID;
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new RentPrice value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieRentalCost The new price the customer pays to rent this item
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieRentalCost(Connection connection, Double newMovieRentalCost, int movieID) throws SQLException {
		String query = "UPDATE Movies SET RentPrice = \"" + newMovieRentalCost + "\" WHERE MovieID = " + movieID;
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new BuyPrice value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMoviePurchaseCost The new price the customer pays to buy this item
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMoviePurchaseCost(Connection connection, Double newMoviePurchaseCost, int movieID) throws SQLException {
		String query = "UPDATE Movies SET BuyPrice = \"" + newMoviePurchaseCost + "\" WHERE MovieID = " + movieID;
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets new Stock value for the specified MovieID
	 * @param connection The database connection object
	 * @param newMovieStock The new number of this item the business as in stock
	 * @param movieID The movie to update
	 * @throws SQLException
	 */
	public static void setMovieStock(Connection connection, int newMovieStock, int movieID) throws SQLException {
		String query = "UPDATE Movies SET Stock = \"" + newMovieStock + "\" WHERE MovieID = " + movieID;
		try {
			updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	// --------------------------------------------------------------------------------------------
	
	// Miscellaneous (for now) --------------------------------------------------------------------
	
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

	/**
	 * Adds a new actor to the database with relevant information
	 * @param dbConnection The database connection object
	 * @param firstName The first name of the actor
	 * @param lastName The last name of the actor
	 * @throws SQLException
	 * @throws LogicException 
	 */
	public static void insertActor(Connection connection, String firstName, String lastName) throws SQLException, LogicException {
	// TODO Auto-generated method stub
	
	Statement statement = null;
	ResultSet result = null;
	String verificationQuery = "SELECT First Name, Last Name FROM Actors";
	String addNewActor = "INSERT INTO Actors (FirstName, LastName) VALUE (\"" + firstName + "\", \"" + lastName + "\")";
	
	String exsistingActor = null;
	
	try {
		statement = connection.createStatement();
		result = statement.executeQuery(verificationQuery);
		while (result.next()) {
			exsistingActor = result.getString("FirstName, LastName");
			if (exsistingActor.equals(firstName) && exsistingActor.equals(lastName)) {
				throw new LogicException("Actor already exsist. Please try again.");
			}
		}
		result.close();
		
		statement.executeUpdate(addNewActor);
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
	 * Adds a new genre to the database with relevant information
	 * @param dbConnection The database connection object
	 * @param genre The genre to add
	 * @throws SQLException
	 */
	public static void insertGenre(Connection connection, String genre) throws SQLException {
		Statement statement = null;
		
		String addNewGenre = "INSERT INTO Genres (GenreType) VALUES (\"" + genre + "\")";
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(addNewGenre);
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			statement.close();
		}
	}

	/**
	 * Adds a new director to the database with relevant information
	 * @param dbConnection The database connection object
	 * @param firstName The first name of the director
	 * @param lastName The last name of the director
	 * @throws SQLException
	 */
	public static void insertDirector(Connection connection, String firstName, String lastName) throws SQLException {
		Statement statement = null;
		
		String addNewActor = "INSERT INTO Directors (FirstName, LastName) VALUES (\"" + firstName + "\", \"" + lastName + "\")";
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(addNewActor);
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			statement.close();
		}
	}
	
	private static void updateTable(Connection connection, String query) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			statement.close();
		}
	}
}