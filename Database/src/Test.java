import java.sql.Connection;
import java.util.List;

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
			List<Transport> carpoolMatches = DatabaseManager.findCarpoolMatches(c, "T102", 2000);
			System.out.println(carpoolMatches.toString());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
