import java.io.File;
import java.util.HashMap;
import java.util.Scanner;


public class Country {
	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(new File("Centroids.csv"));
			
			if (scanner.hasNextLine()) {
                scanner.nextLine(); // Skip the first line
            }
			
			HashMap country = new HashMap<String, Coordinate>();
			
			 while (scanner.hasNextLine()) {
	    			String[] data = scanner.nextLine().split(",");
	                for(int i = 2; i < data.length; i++) {
	                	System.out.println(data[i]);
	                	i+=2;
	                }
	                double latitude = Double.parseDouble(data[0]);
	                double longitude = Double.parseDouble(data[1]);
	                String c = data[2];
	                country.put(c, new Coordinate(latitude, longitude));
	            }
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
}
