import java.sql.*;

public class Query_Utils {
	
	/**
	 * isExisting is a utility function to be used in
	 * conjuction with isExisting functions to determine
	 * if a given query returned any results.
	 * @param connection The database connection object
	 * @param query Query should be of the form SELECT {values} FROM {tables}
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExisting(Connection connection, String query) throws SQLException {
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
	public static boolean isExistingUser(String query, String username, String password, Connection connection) throws SQLException {
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
	 * A helper function to be used by various getActorByX() functions which accepts a query to search for an
	 * actor by a particular value, and prints out ALL details of the relevant actor.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Actors WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getActor(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Actor {");
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
	 * A helper function to be used by various getCustomerByX() functions which accepts a query to search for a
	 * customer by a particular value, and prints out ALL details of the relevant customer.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Customer WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getCustomer(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Customer {");
				System.out.println("    CustomerID: " + result.getInt("CustomerID") + ",");
				System.out.println("    FirstName: " + result.getString("FirstName") + ",");
				System.out.println("    LastName: " + result.getString("LastName") + ",");
				System.out.println("    Username: " + result.getString("Username"));
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
	 * Should only be used by Query.getCustomerBalance
	 * @param connection The database connection object
	 * @param getRentalQuery "SELECT * FROM Rentals WHERE TransactionID = " + transactionQueryResult.getInt("TransactionID")
	 * @return
	 * @throws SQLException
	 */
	public static double getCustomerBalanceFromRental(Connection connection, String getRentalQuery) throws SQLException {
		Statement statement = null;
		ResultSet rentalQueryResult = null;
		
		try {
			statement = connection.createStatement();
			rentalQueryResult = statement.executeQuery(getRentalQuery);
			rentalQueryResult.next();
			
			System.out.println("Rental {");
			System.out.println("    TransactionID: " + rentalQueryResult.getInt("TransactionID") + ",");
			System.out.println("    ExpirationDate: " + rentalQueryResult.getString("ExpirationDate") + ",");
			System.out.println("    ReturnDate: " + rentalQueryResult.getString("ReturnDate") + ",");
			System.out.println("    LateFee: " + rentalQueryResult.getDouble("LateFee") + ", ");
			System.out.println("    LateFeePaid: " + rentalQueryResult.getBoolean("LateFeePaid"));
			System.out.println("}");
			
			if (!rentalQueryResult.getBoolean("LateFeePaid")) {
				return rentalQueryResult.getDouble("LateFee");
			}
			else {
				return 0.00;
			}
		}
		catch (SQLException error) {
			throw error;
		}
		finally {
			rentalQueryResult.close();
			statement.close();
		}
	}
	
	/**
	 * A helper function to be used by various getDirectorByX() functions which accepts a query to search for a
	 * Director by a particular value, and prints out ALL details of the relevant Director.
	 * @param connection The database connection object
	 * @param query Must be of the form SELECT * FROM Director WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getDirector(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Director {");
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
	 * @param query Must be of the form SELECT * FROM Genres WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getGenre(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while(result.next()) {
				System.out.println("Genre {");
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
	 * @param query Must be of the form SELECT * FROM Movies WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getMovie(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Movie {");
				System.out.println("    MovieID: " + result.getInt("MovieID") + ",");
				System.out.println("    MovieTitle: " + result.getString("MovieTitle") + ",");
				System.out.println("    MovieYear: " + result.getString("MovieYear") + ",");
				System.out.println("    CertificateRating: " + result.getString("CertificateRating") + ",");
				System.out.println("    RentPrice: " + result.getDouble("RentPrice") + ",");
				System.out.println("    BuyPrice: " + result.getDouble("BuyPrice") + ",");
				System.out.println("    MovieValue: " + result.getDouble("MovieValue") + ",");
				System.out.println("    Stock: " + result.getInt("Stock") + ",");
				System.out.println("    ReleaseDate: " + result.getString("ReleaseDate") + ",");
				System.out.println("    OverallReviewRating: " + result.getDouble("OverallReviewRating") + ",");
				System.out.println("	Format: " + result.getString("Format"));
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
	 * A helper function to be used by various getRentalByX() functions which accepts a query to search for a
	 * rental by a particular value, and prints out ALL details of the relevant rental.
	 * Note: The customer should never see all these details.
	 * @param connection The connection object
	 * @param query Must be of the form SELECT * FROM Rentalss WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getRental(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Rental {");
				System.out.println("    TransactionID: " + result.getInt("TransactionID") + ",");
				System.out.println("    ExpirationDate: " + result.getString("ExpirationDate") + ",");
				System.out.println("    ReturnDate: " + result.getString("ReturnDate") + ",");
				System.out.println("    LateFee: " + result.getDouble("LateFee") + ", ");
				System.out.println("    LateFeePaid: " + result.getBoolean("LateFeePaid"));
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
	 * A helper function to be used by various getReviewByX() functions which accepts a query to search for a
	 * review by a particular value, and prints out ALL details of the relevant review.
	 * Note: The customer should never see all these details.
	 * @param connection The connection object
	 * @param query Must be of the form SELECT * FROM Reviews WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getReview(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Review {");
				System.out.println("    ReviewID: " + result.getInt("ReviewID") + ",");
				System.out.println("    CustomerID: " + result.getInt("CustomerID") + ",");
				System.out.println("    MovieID: " + result.getInt("MovieID") + ",");
				System.out.println("    Rating: " + result.getInt("Rating") + ", ");
				System.out.println("    Comment: " + result.getString("Comment"));
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
	 * A helper function to be used by various getTransactionByX() functions which accepts a query to search for a
	 * transaction by a particular value, and prints out ALL details of the relevant transaction,
	 * including rental details if there is one involved.
	 * Note: The customer should never see all these details.
	 * @param connection The connection object
	 * @param query Must be of the form SELECT * FROM Transactions WHERE {attribute} = {value}
	 * @throws SQLException
	 */
	public static void getTransaction(Connection connection, String query) throws SQLException {
		Statement statement = null;
		ResultSet result = null;
		
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(query);
			
			while (result.next()) {
				System.out.println("Transaction {");
				System.out.println("    TransactionID: " + result.getInt("TransactionID") + ",");
				System.out.println("    CustomerID: " + result.getInt("CustomerID") + ",");
				System.out.println("    MovieID: " + result.getString("MovieID") + ",");
				System.out.println("    TransactionDate: " + result.getString("TransactionDate") + ", ");
				System.out.println("    isRental: " + result.getBoolean("isRental"));
				System.out.println("}");
				
				if (result.getBoolean("isRental") == true) {
					Query.getRentalByTransactionID(connection, result.getInt("TransactionID"));
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
	 * A helper function to be used by various getMoviesByX() functions which accepts a query
	 * to list all movies which correspond to a relevant entity ID (such as a director), and prints out each movie's
	 * details individually.
	 * @param connection
	 * @param query Must be of the form SELECT MovieID FROM Movie_{Table} WHERE {Table}ID = {value};
	 * @throws SQLException
	 */
	public static void getMoviesByEntity(Connection connection, String query) throws SQLException {
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
	 * A helper function to be used by various insertX() functions which accepts a query
	 * to insert any entity.
	 * @param connection The database connection object
	 * @param query Must be of the form INSERT INTO {table} ({values}) VALUES ({values})
	 * @throws SQLException
	 */
	public static void insertEntity(Connection connection, String query) throws SQLException {
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

	/**
	 * A helper function by be used by various setter functions
	 * @param connection The database connection object
	 * @param query Queries should be SQL UPDATE queries
	 * @throws SQLException
	 */
	public static void updateTable(Connection connection, String query) throws SQLException {
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
