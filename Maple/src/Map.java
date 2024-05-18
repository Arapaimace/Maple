import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.net.URL;
public class Map {
	
	//necessary code to paint the background for the game
	private Image background;
	private int x, y;
	public Map() { 
	    background = getImage("EquiMap.png");
		x = -50;
		y = -50;
	}
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Map.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	public void paint(Graphics g) {
	    Graphics2D g2 = (Graphics2D) g;
	    g2.drawImage(background, x, y, null);
	}
	



}
