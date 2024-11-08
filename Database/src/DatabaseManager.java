import java.sql.*;

/**
 * DatabaseManager
 * @author florian
 * @since 2024-11-08
 */
public class DatabaseManager {
	private static Connection c = null;

	public static void initialize(Connection c) throws SQLException {
		Statement fahrzeugtypen = c.createStatement();
		String fahrzeugtypenCreate = """
			CREATE TABLE fahrzeugtypen(
				fnr INT PRIMARY KEY,
			    fname VARCHAR(4) NOT NULL,
			    fliege BOOLEAN NOT NULL,
			    fstuhl INT NOT NULL,
			    fgehend INT NOT NULL,
			    fmawk INT NOT NULL,
			    fmaximalpassagiere INT NOT NULL
			)""";
		fahrzeugtypen.execute(fahrzeugtypenCreate);

		Statement transport = c.createStatement();
		String transportCreate = """
			CREATE TABLE transport(
					tnr VARCHAR(10) PRIMARY KEY,
				    tdatum DATE NOT NULL,
				    tstart TIME NOT NULL,
				    tende TIME NOT NULL,
				    tvonort VARCHAR(100) NOT NULL,
				    tvonstrasse VARCHAR(100) NOT NULL,
				    tbisort VARCHAR(100) NOT NULL,
				    tbisstrasse VARCHAR(100) NOT NULL,
				    tart ENUM("KANN GEHEN", "STUHL", "LIEGE", "KEIN PATIENT", "EIGENER ROLLSTUHL") NOT NULL,
				    tbezugnr VARCHAR(10),
				    tkmtotale INT NOT NULL,
				    fnr INT NOT NULL,
				    tsektionsoirt VARCHAR(100),
				    FOREIGN KEY (fnr) REFERENCES fahrzeugtypen(fnr)
			    	ON UPDATE CASCADE ON DELETE RESTRICT
			)""";
		transport.execute(transportCreate);
	}

	public static Connection connect(String url, String username, String password) throws SQLException {
		if (c == null)
			c = DriverManager.getConnection(url, username, password);
		return c;
	}

	public static ResultSet getEveryTransport(Connection c) throws SQLException {
		return c.createStatement().executeQuery("SELECT * FROM transport");
	}
}
