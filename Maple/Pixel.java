public class Pixel {
	private int xCoordinate;
	private int yCoordinate;
	
	public Pixel(int xCoord, int yCoord) {
		xCoordinate = xCoord;
		yCoordinate = yCoord;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
}
