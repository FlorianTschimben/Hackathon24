import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	public static ResultSet getDuplicateTragetAndTime(Connection c, String tnr) throws SQLException {
		String query = """
        SELECT t1.tnr
        FROM transport t1, transport t2
        WHERE t1.tbisort = t2.tbisort
        AND t1.tbisstrasse = t2.tbisstrasse
        AND t1.tnr = ?
        AND t1.tnr <> t2.tnr
        AND ABS(TIME_TO_SEC(TIMEDIFF(t1.tende, t2.tende)) / 60) <= 15;
        """;

		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, tnr); // Setze den Parameter tnr in die Abfrage ein
		return pst.executeQuery();
	}

	public static void insert(String query) throws SQLException {
		c.createStatement().execute(query);
	}

	public static Transport get(Connection c, String nr) throws SQLException {
		ResultSet rs = c.createStatement().executeQuery("SELECT * FROM transport WHERE tnr = "+nr);
		rs.next();
		return new Transport( rs.getString("tnr"),
			rs.getDate("tdatum").toString(),
			rs.getTime("tstart").toString(),
			rs.getTime("tende").toString(),
			rs.getString("tvonort"),
			rs.getString("tvonstrasse"),
			rs.getString("tbisort"),
			rs.getString("tbisstrasse"),
			Transport.TransportArt.valueOf(rs.getString("tart")),
			rs.getString("tbezugnr"),
			rs.getInt("tkmtotale"),
			rs.getInt("fnr"),
			rs.getString("tsektionsort"));
	}

	public static List<Transport> findCarpoolMatches(Connection c, String tnr, int timeWindowMinutes) throws SQLException {
		List<Transport> matches = new ArrayList<>();

		// Debug-Anzeige
		System.out.println("findCarpoolMatches aufgerufen mit tnr=" + tnr + ", timeWindowMinutes=" + timeWindowMinutes);

		// Angepasste SQL-Abfrage mit Datum
		String query = """
        SELECT t2.*
        FROM transport t1
        JOIN transport t2 ON t1.tbisort = t2.tbisort
            AND t1.tbisstrasse = t2.tbisstrasse
            AND t1.tdatum = t2.tdatum
        JOIN fahrzeugtypen f ON t2.fnr = f.fnr
        WHERE t1.tnr = ?
            AND t1.tnr <> t2.tnr
            AND ABS(TIME_TO_SEC(TIMEDIFF(t1.tende, t2.tende)) / 60) <= ?
    """;

		PreparedStatement pst = c.prepareStatement(query);
		pst.setString(1, tnr);
		pst.setInt(2, timeWindowMinutes);

		System.out.println(pst.toString());

		ResultSet rs = pst.executeQuery();

		// Schleife durch die ResultSet-Ergebnisse
		while (rs.next()) {
			String tnrResult = rs.getString("tnr");
			String tdatum = rs.getDate("tdatum").toString();
			String tstart = rs.getTime("tstart").toString();
			String tende = rs.getTime("tende").toString();
			String tvonort = rs.getString("tvonort");
			String tvonstrasse = rs.getString("tvonstrasse");
			String tbisort = rs.getString("tbisort");
			String tbisstrasse = rs.getString("tbisstrasse");

			Transport.TransportArt tart;
			try {
				tart = Transport.TransportArt.valueOf(rs.getString("tart"));
			} catch (IllegalArgumentException e) {
				tart = Transport.TransportArt.KANN_GEHEN; // Standardwert bei Fehler
				System.out.println("Unbekannter TransportArt-Wert: " + rs.getString("tart"));
			}

			String tbezugnr = rs.getString("tbezugnr");
			int tkmtotale = rs.getInt("tkmtotale");
			int fnr = rs.getInt("fnr");
			String tsektionsort = rs.getString("tsektionsort");

			// Debug-Ausgabe der gefundenen Übereinstimmungen
			System.out.println("Match gefunden: tnr=" + tnrResult);

			// Transport-Objekt hinzufügen
			matches.add(new Transport(tnrResult, tdatum, tstart, tende, tvonort, tvonstrasse, tbisort, tbisstrasse, tart, tbezugnr, tkmtotale, fnr, tsektionsort));
		}

		return matches;
	}


}
