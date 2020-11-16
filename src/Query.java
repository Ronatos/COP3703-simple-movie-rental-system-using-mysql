import java.sql.*;

public class Query {

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
			return Query_Utils.isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
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
			return Query_Utils.isExistingUser("SELECT Username, Password FROM Customers", username, password, connection);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * isExistingCustomer determines whether the provided customerID references an existing Customer in the database.
	 * @param connection The connection to the database
	 * @param customerID The customer
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingCustomer(Connection connection, int customerID) throws SQLException {
		String query = "SELECT CustomerID FROM Customers";
		
		try {
			return Query_Utils.isExisting(connection, query);
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
			return Query_Utils.isExisting(connection, query);
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
			return Query_Utils.isExistingUser("SELECT Username, Password FROM Employees", username, password, connection);
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
			return Query_Utils.isExisting(connection, query);
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
			return Query_Utils.isExisting(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}

	// Get ----------------------------------------------------------------------------------------
	
	/**
	 * getActorByID prints out the details of the actor with relevant ID.
	 * @param connection The database connection object
	 * @param actorID The actor to search for
	 * @throws SQLException
	 */
	public static void getActorByID(Connection connection, int actorID) throws SQLException {
		String query = "SELECT * FROM Actors WHERE ActorID = " + actorID;
		try {
			Query_Utils.getActor(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getActorByFirstName prints out the details of the actor with relevant first name
	 * @param connection The database connection object
	 * @param firstName The actor's first name to search for
	 * @throws SQLException
	 */
	public static void getActorByFirstName(Connection connection, String firstName) throws SQLException  {
		String query = "SELECT * FROM Actors WHERE FirstName = \"" + firstName + "\"";
		try {
			Query_Utils.getActor(connection, query);
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
			Query_Utils.getActor(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getActorsByMovie prints out the details of all actors who play in the relevant movie.
	 * @param connection The database connection object
	 * @param movieID The movie with actors to print
	 * @throws SQLException
	 */
	public static void getActorsByMovie(Connection connection, int movieID) throws SQLException {
		String query = "SELECT ActorID FROM Movie_Actors WHERE MovieID = " + movieID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getActorByID(connection, result.getInt("ActorID"));
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
	 * getCustomerByID prints out the details of the customer with the relevant ID
	 * @param connection The database connection object
	 * @param customerID The customer to search for
	 * @throws SQLException
	 */
	public static void getCustomerByID(Connection connection, int customerID) throws SQLException {
		String query = "SELECT * FROM Customers WHERE CustomerID = " + customerID;
		try {
			Query_Utils.getCustomer(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getCustomerByUsername prints out the details of the customer with the relevant Username
	 * @param connection The database connection object
	 * @param username The customer's username to search for
	 * @throws SQLException
	 */
	public static void getCustomerByUsername(Connection connection, String username) throws SQLException {

		String query = "SELECT * FROM Customers WHERE Username = \"" + username + "\"";
		try {
			Query_Utils.getCustomer(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getCustomerByReview prints out all the details of the customer who wrote the relevant review
	 * @param connection The database connection object
	 * @param reviewID The relevant review whose author will be printed
	 * @throws SQLException
	 */
	public static void getCustomerByReview(Connection connection, int reviewID) throws SQLException {
		String query = "SELECT CustomerID FROM Reviews WHERE ReviewID = " + reviewID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getCustomerByID(connection, result.getInt("CustomerID"));
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
	 * getCustomerByTransaction prints the customer's data who made the relevant transaction
	 * @param connection The database connection object
	 * @param transactionID The transaction from which to pull the customer data
	 * @throws SQLException
	 */
	public static void getCustomerByTransaction(Connection connection, int transactionID) throws SQLException {
		String query = "SELECT CustomerID FROM Transactions WHERE TransactionID = " + transactionID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getCustomerByID(connection, result.getInt("CustomerID"));
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
	 * Retrieves transaction data, rental data, prints them, and returns the customer's total owed
	 * from unpaid late fees 
	 * @param connection The database connection object
	 * @param customerID The customer to search for
	 * @return
	 * @throws SQLException
	 */
	public static double getCustomerBalance(Connection connection, int customerID) throws SQLException {
		double customerBalance = 0.00;
		String getTransactionsQuery = "SELECT * FROM Transactions WHERE CustomerID = " + customerID;
		Statement statement = null;
		ResultSet transactionQueryResult = null;
		
		try {
			statement = connection.createStatement();
			transactionQueryResult = statement.executeQuery(getTransactionsQuery);
			while (transactionQueryResult.next()) {
				if (transactionQueryResult.getBoolean("isRental")) {
					System.out.println("Transaction {");
					System.out.println("    TransactionID: " + transactionQueryResult.getInt("TransactionID") + ",");
					System.out.println("    CustomerID: " + transactionQueryResult.getInt("CustomerID") + ",");
					System.out.println("    MovieID: " + transactionQueryResult.getString("MovieID") + ",");
					System.out.println("    TransactionDate: " + transactionQueryResult.getString("TransactionDate") + ", ");
					System.out.println("    isRental: " + transactionQueryResult.getBoolean("isRental"));
					System.out.println("}");
					
					String getRentalQuery = "SELECT * FROM Rentals WHERE TransactionID = " + transactionQueryResult.getInt("TransactionID");
					
					customerBalance += Query_Utils.getCustomerBalanceFromRental(connection, getRentalQuery);
				}
			}
			return customerBalance;
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			transactionQueryResult.close();
			statement.close();
		}
	}
	
	/**
	 * getDirectorsByMovie prints out the details of all directors who directed the relevant movie.
	 * @param connection The database connection object
	 * @param movieID The movie with directors to print
	 * @throws SQLException
	 */
	public static void getDirectorsByMovie(Connection connection, int movieID) throws SQLException {
		String query = "SELECT DirectorID FROM Movie_Directors WHERE MovieID = " + movieID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getDirectorByID(connection, result.getInt("DirectorID"));
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
	 * getGenreByID prints out the details of the genre with the relevant ID
	 * @param connection The database connection object
	 * @param genreID The genre's ID to search for
	 * @throws SQLException
	 */
	public static void getGenreByID(Connection connection, int genreID)throws SQLException {
		String query = "SELECT * FROM Genres WHERE GenreID = " + genreID;
		try {
			Query_Utils.getGenre(connection, query);
		}
		catch (SQLException error){
			throw error;
		}
	}
	
	/**
	 * getGenreByType prints out the details of the genre with the relevant type
	 * @param connection The database connection object
	 * @param genreType The genre's type to search for
	 * @throws SQLException
	 */
	public static void getGenreByType(Connection connection, String genreType)throws SQLException {
		String query = "SELECT * FROM Genres WHERE GenreType = \"" + genreType + "\"";
		try {
			Query_Utils.getGenre(connection, query);
		}
		catch (SQLException error){
			throw error;
		}
	}
	
	/**
<<<<<<< HEAD
	 * getDirectorByID prints out the details of the director with the relevant ID
	 * @param connection The database connection object
	 * @param directorID The director's ID to search for
	 * @throws SQLException
	 */
	public static void getDirectorByID(Connection connection, int directorID)throws SQLException {
		String query = "SELECT * FROM Directors WHERE DirectorID = " + directorID;
		try {
			Query_Utils.getDirector(connection, query);
		}
		catch(SQLException error) {
			throw error;
		}
	}
	
	
	
	/**
	 * getDirectorByFirstName prints out the details of the director with relevant first name
	 * @param connection The database connection object
	 * @param firstName The directors first name to search for
	 * @throws SQLException
	 */
	public static void getDirectorByFirstName(Connection connection, String firstName)throws SQLException {
		String query = "SELECT * FROM Directors WHERE FirstName = \"" + firstName + "\"";
		try {
			Query_Utils.getDirector(connection, query);
		}
		catch(SQLException error) {
			throw error;
		}
	}
	
	
	/**
	 * getDirectorByLastName prints out the details of the director with relevant last name
	 * @param connection The database connection object
	 * @param lastName The directors last name to search for
	 * @throws SQLException
	 */
	public static void getDirectorByLastName(Connection connection, String lastName)throws SQLException {
		String query = "SELECT * FROM Directors WHERE LastName = \"" + lastName + "\"";
		try {
			Query_Utils.getDirector(connection, query);
		}
		catch(SQLException error) {
			throw error;
		}
	}

	
	/**
	 * getGenreByMovie prints out the genre following the given movieID
	 * @param connection The database connection object
	 * @param movieID The movie with genres to print
	 * @throws SQLException
	 */
	public static void getGenresByMovie(Connection connection, int movieID) throws SQLException {
		String query = "SELECT GenreID FROM Movie_Genres WHERE MovieID = " + movieID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getGenreByID(connection, result.getInt("GenreID"));
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
	 * getMovieByID prints out the details of the movie with relevant ID
	 * @param connection The connection object
	 * @param movieID The movie to search for
	 * @throws SQLException
	 */
	public static void getMovieByID(Connection connection, int movieID) throws SQLException {
		String query = "SELECT * FROM Movies WHERE MovieID = " + movieID;
		try {
			Query_Utils.getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMovieByCertificateRating prints out the details of the movie with relevant Certificate Rating
	 * @param connection The database connection object
	 * @param certificateRating The relevant certificate rating ("PG-13", "R", etc.)
	 * @throws SQLException
	 */
	public static void getMovieByCertificateRating(Connection connection, String certificateRating) throws SQLException {
		String query = "SELECT * FROM Movies WHERE CertificateRating = \"" + certificateRating + "\"";
		try {
			Query_Utils.getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMovieByReleaseDate prints out the details of the movie with relevant Release Date
	 * @param connection The database connection object
	 * @param releaseDate The relevant release date
	 * @throws SQLException
	 */
	public static void getMovieByReleaseDate(Connection connection, String releaseDate) throws SQLException {
		String query = "SELECT * FROM Movies WHERE ReleaseDate = \"" + releaseDate + "\"";
		try {
			Query_Utils.getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMovieByReview prints data about the movie reviewed in the relevant reviewID
	 * @param connection The database connection object
	 * @param reviewID The review ID of the movie to print
	 * @throws SQLException
	 */
	public static void getMovieByReview(Connection connection, int reviewID) throws SQLException {
		String query = "SELECT MovieID FROM Reviews WHERE ReviewID = " + reviewID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while(result.next()) {
				System.out.println("{");
				System.out.println("    GenreID: " + result.getInt("GenreID") + ",");
				System.out.println("    GenreType: " + result.getString("GenreType") + ",");
				System.out.println("}");
				
				while (result.next()) {
				Query.getMovieByID(connection, result.getInt("MovieID"));
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
			Query_Utils.getMovie(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMovieByTransaction prints out the movie which was involved in the relevant transaction.
	 * @param connection The database connection object
	 * @param transactionID The transaction which contains the movie to print
	 * @throws SQLException
	 */
	public static void getMovieByTransaction(Connection connection, int transactionID) throws SQLException {
		String query = "SELECT MovieID FROM Transactions WHERE TransactionID = " + transactionID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getMovieByID(connection, result.getInt("MovieID"));
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
	 * getMoviesByActor prints out the details of all movies that contain the relevant actor.
	 * @param connection The database connection object
	 * @param actorID The actor in the relevant movies to print
	 * @throws SQLException
	 */
	public static void getMoviesByActor(Connection connection, int actorID) throws SQLException {

		String query = "SELECT MovieID FROM Movie_Actors WHERE ActorID = " + actorID;
		try {
			Query_Utils.getMoviesByEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMoviesByDirector prints out the details of all movies that contain the relevant director.
	 * @param connection The database connection object
	 * @param directorID The director of the relevant movies to print
	 * @throws SQLException
	 */
	public static void getMoviesByDirector(Connection connection, int directorID) throws SQLException {
		String query = "SELECT MovieID FROM Movie_Directors WHERE DirectorID = " + directorID;
		try {
			Query_Utils.getMoviesByEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getMoviesByGenre prints out the details of all movies that contain the relevant genre.
	 * @param connection The database connection object
	 * @param genreID The genre of the relevant movies to print
	 * @throws SQLException
	 */
	public static void getMoviesByGenre(Connection connection, int genreID) throws SQLException {
		String query = "SELECT MovieID FROM Movie_Genres WHERE GenreID = " + genreID;
		try {
			Query_Utils.getMoviesByEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getRentalByTransactionID prints the Rental details of the relevant transaction.
	 * Should only be used if you are sure the transaction has a rental.
	 * @param connection The database connection object
	 * @param transactionID The ID of the transaction with rental details
	 * @throws SQLException
	 */
	public static void getRentalByTransactionID(Connection connection, int transactionID) throws SQLException {
		String query = "SELECT * FROM Rentals WHERE TransactionID = " + transactionID;
		try {
			Query_Utils.getRental(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * getReviewByID prints out the details of the review with relevant ID
	 * @param connection The database connection object
	 * @param reviewID The ID of the review to print
	 * @throws SQLException
	 */
	public static void getReviewByID(Connection connection, int reviewID) throws SQLException {
		String query = "SELECT * FROM Reviews WHERE ReviewID = " + reviewID;
		try {
			Query_Utils.getReview(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getReviewsByCustomer prints out the details of all reviews that were written by the relevant customer.
	 * @param connection The database connection object
	 * @param customerID The customer who wrote the reviews
	 * @throws SQLException
	 */
	public static void getReviewsByCustomer(Connection connection, int customerID) throws SQLException {
		String query = "SELECT ReviewID FROM Reviews WHERE CustomerID = " + customerID;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getReviewByID(connection, result.getInt("ReviewID"));
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
	 * getTransactionByID prints out the details of the transaction with relevant ID.
	 * Also prints relevant rental data if one exists.
	 * @param connection The database connection object
	 * @param transactionID The ID of the transaction to print
	 * @throws SQLException
	 */
	public static void getTransactionByID(Connection connection, int transactionID) throws SQLException {
		String query = "SELECT * FROM Transactions WHERE TransactionID = " + transactionID;
		try {
			Query_Utils.getTransaction(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getTransactionByTransactionDate prints out the details of transactions made on the relevant date.
	 * @param connection The database connection object
	 * @param transactionDate The date the transaction was made
	 * @throws SQLException
	 */
	public static void getTransactionByTransactionDate(Connection connection, String transactionDate) throws SQLException {
		String query = "SELECT * FROM Transactions WHERE TransactionDate = \"" + transactionDate + "\"";
		try {
			Query_Utils.getTransaction(connection, query);
		}
		catch(SQLException error){
			throw error;
		}
	}
	
	/**
	 * getTransactionsByCustomer prints out the details of all transactions made by the relevant customer.
	 * @param connection The database connection object
	 * @param customerID The customer who made the transactions
	 * @throws SQLException
	 */
	public static void getTransactionsByCustomer(Connection connection, int customerID) throws SQLException {
		
		String query = "SELECT TransactionID FROM Transactions WHERE CustomerID = " + customerID;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getTransactionByID(connection, result.getInt("TransactionID"));
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
	 * getTransactionsByGenre prints out the details of all transactions involving movies of the respective genre ID.
	 * @param connection The database connection object
	 * @param genreID The genre to search for
	 * @throws SQLException
	 */
	public static void getTransactionsByGenre(Connection connection, int genreID) throws SQLException {
		
		String query = "SELECT Transactions.TransactionID, Movie_Genres.GenreID FROM Transactions, Movie_Genres WHERE GenreID = " + genreID;
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getTransactionByID(connection, result.getInt("TransactionID"));
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
	 * getTransactionsByMovie prints out the details of all transactions containing the relevant movie.
	 * @param connection The database connection object
	 * @param movieID The movie with transactions to print
	 * @throws SQLException
	 */
	public static void getTransactionsByMovie(Connection connection, int movieID) throws SQLException {
		String query = "SELECT TransactionID FROM Transactions WHERE MovieID = " + movieID;
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query.getTransactionByID(connection, result.getInt("TransactionID"));
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

	// Set ----------------------------------------------------------------------------------------
	
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
		String query = "UPDATE Directors SET FirstName = \"" + newDirectorFirstName + "\" WHERE DirectorID = " + directorID;
		
		try {
			Query_Utils.updateTable(connection, query);
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
		String query = "UPDATE Directors SET LastName = \"" + newDirectorLastName + "\" WHERE DirectorID = " + directorID;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Sets a new GenreType value for the specified GenreID
	 * @param connection The database connection object
	 * @param newGenreType The new genre type
	 * @param genreID The genre to update
	 * @throws SQLException
	 */
	public static void setGenreType(Connection connection, String newGenreType, int genreID) throws SQLException {
		String query = "UPDATE Genres SET GenreType = \"" + newGenreType + "\" WHERE GenreID = " + genreID;
		
		try {
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
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
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	public static void setConfigNewReleaseRentalRate(Connection connection, double newReleaseRentalRate) throws SQLException {
		String query = "UPDATE Configurations SET NewReleaseRate = " + newReleaseRentalRate;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	public static void setConfigNonNewReleaseRentalRate(Connection connection, double nonNewReleaseRentalRate) throws SQLException {
		String query = "UPDATE Configurations SET NonNewReleaseRate = " + nonNewReleaseRentalRate;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	public static void setConfigNewReleaseRentalPeriod(Connection connection, int newReleaseRentalPeriod) throws SQLException {
		String query = "UPDATE Configurations SET NewReleasePeriod = " + newReleaseRentalPeriod;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	public static void setConfigNonNewReleaseRentalPeriod(Connection connection, int nonNewReleaseRentalPeriod) throws SQLException {
		String query = "UPDATE Configurations SET NonNewReleaseRate = " + nonNewReleaseRentalPeriod;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	public static void setConfigLateFeePerDay(Connection connection, double newLateFeePerDay) throws SQLException {
		String query = "UPDATE Configurations SET LateFeePerDay = " + newLateFeePerDay;
		
		try {
			Query_Utils.updateTable(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	// Insert -------------------------------------------------------------------------------------
	
	/**
	 * Adds a new actor to the database with relevant information
	 * @param dbConnection The database connection object
	 * @param firstName The first name of the actor
	 * @param lastName The last name of the actor
	 * @throws SQLException
	 * @throws LogicException 
	 */
	public static void insertActor(Connection connection, String firstName, String lastName) throws SQLException, LogicException {
	
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
	 * Adds a new director to the database with relevant information
	 * @param dbConnection The database connection object
	 * @param firstName The first name of the director
	 * @param lastName The last name of the director
	 * @throws SQLException
	 */
	public static void insertDirector(Connection connection, String firstName, String lastName) throws SQLException {
		Statement statement = null;
		
		String addNewDirector = "INSERT INTO Directors (FirstName, LastName) VALUES (\"" + firstName + "\", \"" + lastName + "\")";
		
		try {
			statement = connection.createStatement();
			statement.executeUpdate(addNewDirector);
		}
		catch (SQLException error) {
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
	public static void insertMovie(Connection connection, String movieTitle, String movieReleaseDate, String movieCertificateRating, double movieBusinessCost, double movieCustomerPurchaseCost, double movieCustomerRentCost, String format) throws SQLException {
		Statement statement = null;
		
		String movieYear = movieReleaseDate.split("-")[0];

		String addNewMovie = "INSERT INTO Movies (MovieTitle, MovieYear, CertificateRating, RentPrice, BuyPrice, MovieValue, ReleaseDate, Format) VALUE (\"" + movieTitle + "\", \"" + movieYear + "\", \"" + movieCertificateRating + "\", \"" + movieCustomerRentCost + "\", \"" + movieCustomerPurchaseCost + "\", \"" + movieBusinessCost + "\", \"" + movieReleaseDate + "\", \"" + format + "\")";
		
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
	 * Links a movie with an actor
	 * @param connection The database connection object
	 * @param movieID The movie to link with the actor
	 * @param actorID The actor to link with the movie
	 * @throws SQLException
	 */
	public static void insertMovieActor(Connection connection, int movieID, int actorID) throws SQLException {
		String query = "INSERT INTO movie_actors (movieID, actorID) VALUES (" + movieID +" , " + actorID+")";
		try {
			Query_Utils.insertEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Links a movie with a director
	 * @param connection The database connection object
	 * @param movieID The movie to link with the director
	 * @param directorID The director to link with the movie
	 * @throws SQLException
	 */
	public static void insertMovieDirector(Connection connection, int movieID, int directorID) throws SQLException {
		String query = "INSERT INTO movie_directors (movieID, directorID) VALUES (" + movieID +" , " + directorID+")";
		try {
			Query_Utils.insertEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * Links a movie with a genre
	 * @param connection The database connection object
	 * @param movieID The movie to link with the genre
	 * @param actorID The genre to link with the movie
	 * @throws SQLException
	 */
	public static void insertMovieGenre(Connection connection, int movieID, int genreID) throws SQLException {
		String query = "INSERT INTO movie_genres (movieID, genreID) VALUES (" + movieID +" , " + genreID+")";
		try {
			Query_Utils.insertEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	// Miscellaneous (for now) --------------------------------------------------------------------
	
	public static void applyLateFee(Connection connection, int transactionID, double customerBalanceAddition) throws SQLException {
		String query = "UPDATE Rentals SET LateFee = LateFee + " + customerBalanceAddition + " WHERE TransactionID = " + transactionID;
		try {
			Query_Utils.updateTable(connection, query);
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
	
	public static void printWeeklyGenreRevenueReport(Connection connection, int genreID) throws SQLException {
		String query = "SELECT Transactions.TransactionID, Movie_Genres.GenreID FROM Transactions, Movie_Genres WHERE GenreID = " + genreID + " AND DATEDIFF(Transactions.TransactionDate, CURDATE()) >= -7";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getTransactionByID(connection, result.getInt("TransactionID"));
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
	
	public static void printMonthlyGenreRevenueReport(Connection connection, int genreID) throws SQLException {
		String query = "SELECT Transactions.TransactionID, Movie_Genres.GenreID FROM Transactions, Movie_Genres WHERE GenreID = " + genreID + " AND DATEDIFF(Transactions.TransactionDate, CURDATE()) >= -31";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getTransactionByID(connection, result.getInt("TransactionID"));
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
	
	public static void printYearlyGenreRevenueReport(Connection connection, int genreID) throws SQLException {
		String query = "SELECT Transactions.TransactionID, Movie_Genres.GenreID FROM Transactions, Movie_Genres WHERE GenreID = " + genreID + " AND DATEDIFF(Transactions.TransactionDate, CURDATE()) >= -365";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				getTransactionByID(connection, result.getInt("TransactionID"));
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
	
	//lets review
	public static void getMoviesOfTheMonth(Connection connection) throws SQLException {
		String query = "SELECT * FROM Movies WHERE ReleaseData = MONTH(getData())";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while(result.next()) {
				Query_Utils.getMovie(connection, query);
			}
		}
		catch(SQLException error) {
			throw error;
		}
		finally {
			statement.close();
			result.close();
		}	
	}

<<<<<<< HEAD
	
	/**
	 * Finds and returns the highest rated movies
	 * @param connection the connection to the database
	 * @param OverallReviewRating a movie's review rating
	 * @throws SQLException
	 */
	public static void getHighestRatedMovies(Connection connection, double OverallReviewRating) throws SQLException {
=======
	// Derrick
	// Not to sure if i did this correct, lets review.
	//
	// You set it so we would only pull the OverallReviewRating column, which wouldn't print out all the movie details 
	// also, you had it so we would pass in the rating needed to qualify as "highest". I switched it to pull
	// every movie and order them by the best rating. I don't think we'll ever have enough movies in here
	// that it matters, so we may as well get them all. - Alex
	public static void getHighestRatedMovies(Connection connection) throws SQLException {
>>>>>>> 75c42871fe4d331388296e7a84cbf7d4c374af42
		String query = "SELECT * FROM Movies ORDER BY OverallReviewRating DESC";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				Query_Utils.getMovie(connection, query);
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
<<<<<<< HEAD
	
	public static void getConfigNewReleaseRentalRate(Connection dbConnection) throws SQLException {
		String query = "SELECT NewReleaseRentalRate FROM Configurations";
=======

	public static void getConfigNewReleaseRentalRate(Connection connection) throws SQLException {
		String query = "SELECT NewReleaseRate FROM Configurations";

		Statement statement = null;
		ResultSet result = null;
		
>>>>>>> 75c42871fe4d331388296e7a84cbf7d4c374af42
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
		
			System.out.println("New Release Rental Rate: " + result.getDouble("NewReleaseRate"));
		}
		catch(SQLException error){
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}		
		
	public static void getConfigNonNewReleaseRentalRate(Connection connection) throws SQLException {
		String query = "SELECT NonNewReleaseRate FROM Configurations";

		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
		
			System.out.println("Non-New Release Rental Rate: " + result.getDouble("NonNewReleaseRate"));
		}
		catch(SQLException error){
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}		

	public static void getConfigNewReleaseRentalPeriod(Connection connection) throws SQLException {		
		String query = "SELECT NewReleasePeriod FROM Configurations";

		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
		
			System.out.println("New Release Rental Period: " + result.getDouble("NewReleasePeriod"));
		}
		catch(SQLException error){
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	public static void getConfigNonNewReleaseRentalPeriod(Connection connection) throws SQLException {
		String query = "SELECT NonNewReleasePeriod FROM Configurations";
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
		
			System.out.println("Non-New Release Rental Period: " + result.getDouble("NonNewReleasePeriod"));
		}
		catch(SQLException error){
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	public static void getConfigLateFeePerDay(Connection connection) throws SQLException {
		String query = "SELECT LateFeePerDay FROM Configurations";

		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
		
			System.out.println("Late Fee Added Per Day Rental is Late: " + result.getDouble("LateFeePerDay"));
		}
		catch(SQLException error){
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}

	public static boolean customerCanAffordPurchase(Connection connection, String username, int movieID) throws SQLException {
		String query = "SELECT Customers.CustomerBalance FROM Customers WHERE Customers.Username = \"" + username + "\"";
		double custBal = 0;
		
		Statement statement = null;
		ResultSet result = null;
		
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
			
			custBal = result.getDouble("CustomerBalance") - getMovieBuyPriceByID(connection, movieID);
			
			if (custBal >= 0) {
				return true;
			}
			else {
				return false;
			}
		}
		catch(SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	public static double getMovieBuyPriceByID(Connection connection, int movieID) throws SQLException {
		String query = "SELECT Movies.BuyPrice FROM Movies WHERE MovieID = " + movieID;
		
		Statement statement = null;
		ResultSet result = null;
		
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
			
			return result.getDouble("BuyPrice");
		}
		catch(SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	public static void purchaseMovie(Connection connection, int customerID, int movieID) throws SQLException {
		String query = 
	}

	public static void insertTransaction(Connection connection, String username, int movieID, boolean b) throws SQLException {
		// TODO Auto-generated method stub
		int customerID = getCustomerIDFromUsername(connection, username);
		double movieBuyPrice = getMovieBuyPriceByID(connection, movieID);
		String query = "INSERT INTO Transactions ('CustomerID', `MovieID`, `TransactionDate`, `UpFrontTransactionCost`, `isRental`) VALUES ('" + customerID +"', '" + movieID + "', CURDATE(), " + movieBuyPrice + ", " + b + ")";
		try {
			Query_Utils.insertEntity(connection, query);
		}
		catch (SQLException error) {
			throw error;
		}
		
		if (b) {
			insertRental(connection, getLatestTransactionID(connection));
		}
	}
	
	public static void insertRental(Connection connection, int transactionID) throws SQLException {
		String query = "INSERT INTO Rentals (TransactionID, ExpirationDate) VALUES (" + transactionID + ", CURDATE() + )";
	}
	
	public static void getLatestTransactionID(Connection connection) {
		String query = "SELECT Transactions.TransactionID FROM Transactions ORDER BY ASC";
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
			
			return result.getINT("TransactionID");
		}
		catch(SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
<<<<<<< HEAD
	
	
	
	
	
	
	
	
=======
	public static int getCustomerIDFromUsername(Connection connection, String username) throws SQLException {
		String query = "SELECT CustomerID FROM Customers WHERE Customers.Username = '" + username + "'";
		
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			result.next();
			
			return result.getInt("CustomerID");
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
>>>>>>> 75c42871fe4d331388296e7a84cbf7d4c374af42
}