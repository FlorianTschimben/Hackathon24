import java.sql.*;

/**
 * InitializeDatabase
 * @author florian
 * @since 2024-11-08
 */
public class InitializeDatabase {
	public static void initialize(Connection c) throws SQLException {
		Statement fahrzeugtypen = c.createStatement();
		String fahrzeugtypenCreate = """
			CREATE TABLE fahrzeugtypen(
			\tfnr INT PRIMARY KEY,
			    fname VARCHAR(4) NOT NULL,
			    fliege BOOLEAN NOT NULL,
			    fstuhl INT NOT NULL,
			    fgehend INT NOT NULL,
			    fmawk INT NOT NULL,
			    fmaximalpassagiere INT NOT NULL
			)""";
		fahrzeugtypen.executeQuery(fahrzeugtypenCreate);

		Statement transport = c.createStatement();
		String transportCreate = """
			CREATE TABLE transport(
			\ttnr VARCHAR(10) PRIMARY KEY,
			    tdatum DATE NOT NULL,
			    tstart TIME NOT NULL,
			    tende TIME NOT NULL,
			    tvonort VARCHAR(100) NOT NULL,
			    tvonstrasse VARCHAR(100) NOT NULL,
			    tbisort VARCHAR(100) NOT NULL,
			    tbisstrasse VARCHAR(100) NOT NULL,
			    tbezugnr VARCHAR(10),
			    tkmtotale INT NOT NULL,
			    fnr INT NOT NULL,
			    tsektionsoirt VARCHAR(100),
			    FOREIGN KEY (fnr) REFERENCES fahrzeugtypen(fnr)
			    \tON UPDATE CASCADE ON DELETE RESTRICT
			)""";
		transport.executeQuery(transportCreate);
	}
}
