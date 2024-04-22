
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main extends JPanel implements ActionListener, MouseListener, KeyListener {
    
    // sprites/object creating
    
    Map background = new Map();
    double maxLong = 180;
    double minLong = -180;
    double maxLat = 85.05112878;
    double minLat = -85.05112878;
    int width = 1280;
    int height = 641;
    
    double xScale = width/(maxLong - minLong);
    double yScale = height/(maxLat - minLat);
    
    //sets background
    private ImageIcon backgroundImage = new ImageIcon("Map.jpg");

    public static void main(String[] arg) {
        new Main();
    }
    
    //frame constructor
    public Main() {
        JFrame f = new JFrame("Map");
        f.setLayout(new BorderLayout());
        f.setSize(new Dimension(width, height));
        f.setResizable(false);
        f.addMouseListener(this);
        f.addKeyListener(this);
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));

        Timer t = new Timer(16, this);
        t.start();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        
    	super.paintComponent(g);
        // Call the paint method of the Map object to draw the background
        background.paint(g);
        g.setColor(Color.red);
        
        double[] paraguay = convert(-32.815428, -56.094636);
        
        g.fillOval((int)paraguay[0], (int)paraguay[1], 10, 10);
        g.fillOval((int)((1.601554 - minLong)*xScale-75), (int)(-(42.546245 + minLat)*yScale), 10, 10); //Andora
        g.fillOval((int)((25.48583 - minLong)*xScale-75), (int)(-(42.733883 + minLat)*yScale), 10, 10); //Bulgaria
        g.fillOval((int)((-95.712891 - minLong)*xScale-75), (int)(-(37.09024 + minLat)*yScale), 10, 10); //United States
        g.fillOval((int)((-3.74922 - minLong)*xScale-75), (int)(-(40.463667 + minLat)*yScale), 10, 10); //Spain
        g.fillOval((int)((-3.435973	 - minLong)*xScale-75), (int)(-(55.378051 + minLat)*yScale), 10, 10); //United Kingdom
        g.fillOval((int)((28.233608 - minLong)*xScale-75), (int)(-(-29.609988	 + minLat)*yScale), 10, 10); //Lesotho
        g.fillOval((int)((	-51.92528 - minLong)*xScale-75), (int)(-(-14.235004	 + minLat)*yScale), 10, 10); //Brazil
        g.fillOval((int)((28.233608 + 180) * (width / 360)), ((int)((-29.609988 * -1) + 90) * (height / 180)), 10, 10);

    }
    
    public double[] convert(double lon, double lat){
        double x = ((lon + 180.0) * (width / 360.0));
        double y = (((lat * -1.0) + 90.0) * (height / 180.0));
        
        
        double[] res = {x,y};
        return res;
    }
    
    public double distance(double lat1, double lon1, double lat2, double lon2) {
    	// The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        // Haversine formula 
        double dlon = lon2 - lon1; 
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);
             
        double c = 2 * Math.asin(Math.sqrt(a));
 
        // Radius of earth in kilometers. Use 3956 
        // for miles
        double r = 6371;
 
        // calculate the result
        return(c * r);
    } 
 

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent m) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    }

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
