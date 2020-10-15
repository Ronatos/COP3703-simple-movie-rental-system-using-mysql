import java.sql.*;
import java.util.Scanner;

public class DatabaseBridge {
	
	public Connection connection = null;
	
	public DatabaseBridge(Scanner scanner) throws SQLException {
		String url = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3308/group2";
		
		do {
			// Acquire administrator credentials to establish initial database connection
			System.out.println("Establishing database connection...");
	        System.out.print("Admin Username: ");
	        String username = scanner.nextLine();
	        System.out.print("Admin Password: ");
	        String password = scanner.nextLine();
	        
	        // Attempt to establish initial database connection with provided administrator credentials
	        try {
	        	connection = DriverManager.getConnection(url, username, password);
	            System.out.println("Database connection established.");
	        }
	        catch (SQLException error) {
	        	System.out.println("Unable to establish database connection. " +
	        		"Please try again or contact your system administrator.");
	            System.out.print("Try again? (y/n): ");
	            String response = scanner.nextLine();
	            if (!response.equals("y")) {
	            	throw error;
	            }
	        }
		} while (connection == null);
	}
}
