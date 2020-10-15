import java.sql.*;

public class Main {
	
	public static void main(String[] args) {
		// Establish database connection
		try {
			DatabaseBridge db = new DatabaseBridge();
		}
		catch (SQLException error) {
			System.out.println("Goodbye!");
			System.exit(0);
		}
	}
	
}
