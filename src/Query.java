import java.sql.*;

public class Query {

	// This method is currently being tested, and should be adjusted to verify username and password eventually
	/**
	 * isExistingCustomer is currently being tested, and should be adjusted to verify username and password eventually.
	 * @param connection The connection to the database.
	 * @param FirstName
	 * @param LastName
	 * @return
	 * @throws SQLException
	 */
	public static boolean isExistingCustomer(Connection connection, String FirstName, String LastName) throws SQLException {
		// Set up query environment
		Statement statement = null;
		ResultSet result = null;
		boolean matchFound = false;
		
		try {
			statement = connection.createStatement();
			// result = statement.executeQuery("SELECT Username, Password FROM Customers"); // the real query
			result = statement.executeQuery("SELECT FirstName, LastName FROM Customers");
			while (result.next()) {
				if (result.getString("FirstName").equals(FirstName) &&
					result.getString("LastName").equals(LastName)) {
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
