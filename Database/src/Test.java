import java.sql.*;
import java.util.HashMap;

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
			ResultSet rs = DatabaseManager.getDuplicateTragetAndTime(c, "1");
			HashMap<String, Double> duplicateMap= new HashMap<>();
			while (rs.next()){
				duplicateMap.put(rs.getInt(5) + ", " + rs.getInt(6),
						RouteTimeFinder.routeTime(rs.getInt(5) + ", " + rs.getInt(6),
								rs.getInt(7) + ", " + rs.getInt(8)));
			}
			duplicateMap.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
