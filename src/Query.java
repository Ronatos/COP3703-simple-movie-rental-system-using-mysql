import java.sql.*;

public class Query {

	/**
	 * In need of FirstName > Username & LastName > Password adjustments following database updates.
	 * isExistingCustomer determines whether the provided username and password reference an existing Customer in the database.
	 * @param connection The connection to the database
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingCustomer(Connection connection, String username, String password) throws SQLException {
		try {
			return isExistingUser("SELECT FirstName, LastName FROM Customers", username, password, connection);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * In need of FirstName > Username & LastName > Password adjustments following database updates.
	 * isExistingEmployee determines whether the provided username and password reference an existing Employee in the database.
	 * @param connection
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingEmployee(Connection connection, String username, String password) throws SQLException {
		try {
			return isExistingUser("SELECT FirstName, LastName FROM Employees", username, password, connection);
		}
		catch (SQLException error) {
			throw error;
		}
	}
	
	/**
	 * In need of testing following database updates.
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
	public static void createNewCustomer(Connection connection, String username, String password, String firstName, String lastName, String referencedBy) throws SQLException, LogicException {
		// Set up query environment
		Statement statement = null;
		ResultSet result = null;
		String verificationQuery = "SELECT Username FROM Customers";
		String addNewUser = "INSERT INTO Customers (Username, Password, FirstName, LastName, ReferencedBy) VALUES (" + username + ", " + password + ", " + firstName + ", " + ", " + lastName + ", " + referencedBy;
		String currentUser = null;
		boolean referencedCustomerExists = false;
		
		// Make sure user can be created as specified, and do so if possible
		// 1. Usernames must be unique
		// 2. Referenced customer must exist
		try {
			statement = connection.createStatement();
			result = statement.executeQuery(verificationQuery);
			while (result.next()) {
				currentUser = result.getString("Username");
				if (currentUser.equals(username)) {
					throw new LogicException("Username already in use. Please try again.");
				}
				if (currentUser.equals(referencedBy)) {
					referencedCustomerExists = true;
				}
			}
			if (referencedCustomerExists == false) {
				throw new LogicException("Referenced customer does not exist. Please try again.");
			}
			result.close();
			
			result = statement.executeQuery(addNewUser);
		}
		catch (SQLException error) {
			throw error;
		}
		catch (LogicException error) {
			throw error;
		}
		finally {
			result.close();
			statement.close();
		}
	}
	
	/**
	 * In need of FirstName > Username & LastName > Password adjustments following database updates.
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
				if (result.getString("FirstName").equals(username) &&
					result.getString("LastName").equals(password)) {
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
}
