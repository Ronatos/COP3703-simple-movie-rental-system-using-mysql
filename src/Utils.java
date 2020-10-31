import java.util.Scanner;

public class Utils {

	/**
	 * Forces the user to enter an integer.
	 * @param scanner The scanner object
	 * @return A user-selected integer
	 */
	public static int getUserSelection(Scanner scanner) {
		do {
			try {
				return Integer.parseInt(scanner.nextLine());
			}
			catch (NumberFormatException error) {
				System.out.println("Invalid selection. Please try again.");
			}
		} while (true);
	}
	
	public static void printDatabaseError(Exception error) {
		error.printStackTrace();
		System.out.println("A database error was encountered. " +
			"Please try again or contact your system administrator.");
	}
	
}
