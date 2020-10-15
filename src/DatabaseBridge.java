import java.sql.*;
import java.util.Scanner;

public class DatabaseBridge {
	
	public static Connection connection = null;
	
	public DatabaseBridge() throws SQLException {
		String url = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3308/group2";
		
		do {
			// Acquire administrator credentials to establish initial database connection
			System.out.println("Establishing database connection...");
			Scanner scanner = new Scanner(System.in);
	        System.out.print("Admin Username: ");
	        String username = scanner.nextLine();
	        System.out.print("Admin Password: ");
	        String password = scanner.nextLine();
	        
	        // Attempt to establish initial database connection with provided administrator credentials
	        try {
	        	connection = DriverManager.getConnection(url, username, password);
	            System.out.println("Database connection established.");
	            scanner.close();
	        }
	        catch (SQLException error) {
	        	System.out.println("Unable to establish database connection. " +
	        		"Please try again or contact your system administrator.");
	            System.out.print("Try again? (y/n): ");
	            String response = scanner.nextLine();
	            if (!response.equals("y")) {
	            	scanner.close();
	            	throw error;
	            }
	        }
		} while (connection == null);
	}
	
	/*
	public static void query() {
		try {
            Connection conn = getConnection();
			Statement st = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM Customers");
			while (rs.next()) {
				System.out.println(rs.getString("FirstName") + " " + rs.getString("LastName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	*/
}
