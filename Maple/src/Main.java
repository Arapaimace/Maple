
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
    JButton button;
    JTextField textField;
    JLabel map;
    
    double xScale = width/(maxLong - minLong);
    double yScale = height/(maxLat - minLat);
    
    //sets background
    private ImageIcon backgroundImage = new ImageIcon("Map.jpg");
    private static Coordinate inputted;
    private static String in = "Russian Federation";
    public static void main(String[] arg) {
        new Main(); 
        readInput();
    }

    public static void readInput(){
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
//			 while(true) {
//				Scanner input = new Scanner(System.in);
//				System.out.println("input country");
//                in = input.nextLine();
//                inputted = (Coordinate)(country.get(in));
//			}
		}
		catch (Exception e){
			System.out.println(e);
		}
    }
    
    //frame constructor
    public Main() {
        JFrame world = new JFrame("Map");
        JPanel panel = new JPanel();
        textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBounds(0, 0, (int) (width), 50);
        

        button = new JButton("Enter");
        button.addActionListener(this);

        // Make the JFrame visible
        world.setLayout(new BorderLayout());
        world.setSize(new Dimension(width, height));
        world.setResizable(false);
        world.addMouseListener(this);
        world.addKeyListener(this);
        
        String src = new File("").getAbsolutePath() + "/src/";
        map = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("Map.jpg")).getImage();
        map.setIcon(new ImageIcon(img));
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));
        
        panel.add(textField);
        panel.add(button);
        panel.add(map);
        Timer tim = new Timer(16, this);
        tim.start();
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        world.add(panel);
//        world.add(textField);
        world.setLocationRelativeTo(null);
        world.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        
    	super.paintComponent(g);
        // Call the paint method of the Map object to draw the background
        background.paint(g);
        g.setColor(Color.red);
//        g.drawOval((int)inputted.getLatitude(), (int)inputted.getLongitude(), 10, 10);
        double[] paraguay = convert(-32.815428, -56.094636);
    }
    
    public double[] convert(double lon, double lat){
        double x = ((lon + 180.0) * (width / 360.0));
        double y = (((lat * -1.0) + 90.0) * (height / 180.0));
        
        
        double[] res = {x,y};
        return res;
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
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
        	System.out.println(textField.getText());
        }
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

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
