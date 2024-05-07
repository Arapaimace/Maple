
public class Coordinate {
	private double latitude;
	private double longitude;
	private int x;
	private int y;
	
	public Coordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	//to be implemented once we add all the x/y-values to the csv
	public Coordinate(double latitude, double longitude, int x, int y){
		this.latitude = latitude;
		this.longitude = longitude;
		this.x = x;
		this.y = y;
	}

	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public String toString() {
		return "latitude: " + latitude + " longitude: " + longitude;
	}
}
