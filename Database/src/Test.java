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
			Transport t = new Transport("2","2010-01-01", "02:45:00", "03:05:00", "Nals",
				"Andreas-Hofer-Straße 7", "Meran", "Meranrgasse 8", Transport.TransportArt.STUHL ,null, 30,
				2, "Meran");
			DatabaseManager.insert(t.generateInsertQuery());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
