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
			Connection c = DatabaseManager.connect("jdbc:mysql://localhost/hackathon", "root", "masterkey");
			//DatabaseManager.initialize(c);
			List<Transport> carpoolMatches = DatabaseManager.findCarpoolMatches(c, "T102", 2000);
			System.out.println(carpoolMatches.toString());
			for(Transport t:carpoolMatches){
				System.out.println(t.getTbisstrasse());
			}
			String[] arr = new String[carpoolMatches.size() + 1];
			System.out.println(carpoolMatches.size() + 1);
			int index = 0;
			arr[index] = carpoolMatches.get(0).getTvonstrasse() + ", " + carpoolMatches.get(0).getTvonort();
			index++;
			for(Transport t:carpoolMatches){
				arr[index] = t.getTbisstrasse() + ", " + t.getTbisort();
				index++;
			}
			String[] arr2 = new	String[carpoolMatches.size() + 1];
			int index2 = 0;


		}
		catch (Exception e) {
			//throw new RuntimeException(e);
		}
	}
}
