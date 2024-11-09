import java.sql.*;

/**
 * Test
 * @author florian
 * @since 2024-11-08
 */
public class Test {
	public static void main(String[] args) {
		try {
			Connection c = DatabaseManager.connect("jdbc:mysql://localhost/hackathon", "root", "");
			//DatabaseManager.initialize(c);
			ResultSet rs = DatabaseManager.getDuplicateTragetAndTime(c);
			rs.next();

		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
