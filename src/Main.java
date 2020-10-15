import java.sql.*;
import java.util.Scanner;

public class Main {

	public static Connection getConnection() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Establishing database connection...");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        scanner.close();
        
        Connection conn = null;
		String url = "jdbc:mysql://cisvm-winsrv-mysql1.unfcsd.unf.edu:3308/group2";
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
        	System.out.println("Database connection failed to establish. Aborting...");
            e.printStackTrace();
        }
        return conn;
	}
	
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
	
	public Main() {
		getConnection();
	}
	
	public static void main(String[] args) {
		query();
	}
}
