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
					tnr VARCHAR(20) PRIMARY KEY,
				    tdatum DATE NOT NULL,
				    tstart TIME NOT NULL,
				    tende TIME NOT NULL,
				    tvonort VARCHAR(100) NOT NULL,
				    tvonstrasse VARCHAR(100) NOT NULL,
				    tbisort VARCHAR(100) NOT NULL,
				    tbisstrasse VARCHAR(100) NOT NULL,
				    tart ENUM("KANN GEHEN", "STUHL", "LIEGE", "KEIN PATIENT", "EIGENER ROLLSTUHL") NOT NULL,
				    tbezugnr VARCHAR(20),
				    tkmtotale INT NOT NULL,
				    fnr INT NOT NULL,
				    tsektionsort VARCHAR(100),
				    FOREIGN KEY (fnr) REFERENCES fahrzeugtypen(fnr)
			    	ON UPDATE CASCADE ON DELETE RESTRICT
			)""";
		transport.execute(transportCreate);

		Statement strecken = c.createStatement();
		String streckenCreate = """
			CREATE TABLE strecken(
				snr INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
			    tnr VARCHAR(20) NOT NULL,
			    svonort VARCHAR(100) NOT NULL,
			    sbisort VARCHAR(100) NOT NULL,
			    sstuhl INT NOT NULL,
			    sgehend INT NOT NULL,
			    ssitz INT NOT NULL,
			    sliege BOOLEAN NOT NULL,
			    FOREIGN KEY (tnr) REFERENCES transport(tnr)
			    ON UPDATE CASCADE ON DELETE CASCADE
			)""";
		strecken.execute(streckenCreate);
	}

	public static Connection connect(String url, String username, String password) throws SQLException {
		if (c == null)
			c = DriverManager.getConnection(url, username, password);
		return c;
	}

	public static ResultSet getEveryTransport(Connection c) throws SQLException {
		return c.createStatement().executeQuery("SELECT * FROM transport");
	}

	public static ResultSet getDuplicateTragetAndTime(Connection c) throws SQLException {
		String query = """
			SELECT t1.tnr
				FROM transport t1, transport t2
				WHERE t1.tbisort = t2.tbisort
				AND t1.tbisstrasse = t2.tbisstrasse
				AND t1.tnr <> t2.tnr
				AND	ABS(TIME_TO_SEC(TIMEDIFF(t1.tende, t2.tende)) / 60) <= 15;
			""";
		return c.createStatement().executeQuery(query);
	}

	public static void insert(String query) throws SQLException {
		c.createStatement().execute(query);
	}
}
