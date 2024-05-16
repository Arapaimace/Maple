
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.Image;

import javax.swing.AbstractButton;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JList;

public class Main extends JPanel implements ActionListener, KeyListener {
        
	private Map background = new Map();
	private double maxLong = 180;
	private double minLong = -180;
	private double maxLat = 85.05112878;
	private double minLat = -85.05112878;
	private int width = 1175;
	private int height = 690;
	private JButton button;
	private JTextField textField;
	private JLabel map;
	private JList<String> answers;
    
    private static ArrayList<String> co;
    
    private double xScale = width/(maxLong - minLong);
    private double yScale = height/(maxLat - minLat);
    
    boolean joever = false;
    private ImageIcon backgroundImage = new ImageIcon("EquiMap.png");
    private static Coordinate inputted;
    private static String in;
    private static HashMap country;
    private static HashMap pixelCoords;
    public static String answer;
    
    public static void main(String[] arg) {
        Main m = new Main(); 

        readInput();
        
        answer = randomCountry();

    }
    public static void readInput(){
        try {
        	
			Scanner scanner = new Scanner(new File("Centroids.csv"));
			
			if (scanner.hasNextLine()) {
                scanner.nextLine();             }
			
			country = new HashMap<String, Coordinate>();
			co = new ArrayList<String>();
			
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
	                co.add(c);
	        }
			scanner.close();
			
			Scanner scanner2 = new Scanner(new File("Final_Coordinates - Sheet1.csv"));

			if (scanner2.hasNextLine()) {
                scanner2.nextLine();             
            }

			pixelCoords = new HashMap<String, Coordinate>();			
			while (scanner2.hasNextLine()) {
	    			String[] data = scanner2.nextLine().split(",");
	                int xPos = Integer.parseInt(data[1]);
	                int yPos = Integer.parseInt(data[2]);
	                String c = data[0];
	                pixelCoords.put(c, new Pixel(xPos, yPos));
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
    }
    
    public Main() {
        JFrame world = new JFrame("World");
        JPanel main = new JPanel();
        JPanel inputRight = new JPanel();
        JPanel graphics = new DrawPane();
        graphics.setPreferredSize(new Dimension(width, height));
        
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        inputRight.setLayout(new FlowLayout());
        inputRight.setPreferredSize(new Dimension(100, height));
        
        answers = new JList(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(answers);
        scrollPane.setPreferredSize(new Dimension(100, height-62));
        inputRight.add(scrollPane);
        
        textField = new JTextField("Input State");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addKeyListener(this);
        textField.setPreferredSize(new Dimension(100, 20));
        inputRight.add(textField);
        
        button = new JButton("Enter");
        button.addActionListener(this);
        button.addKeyListener(this);
        inputRight.add(button);

        world.addKeyListener(this);
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));
        
        main.add(graphics);
        main.add(inputRight);
        
        world.setContentPane(main);
        world.pack();
     // Call this *before* 'setVisible(true)'.
        world.setVisible(true); 
        
        Timer tim = new Timer(16, this);
        tim.start();
    }
    
    class DrawPane extends JPanel {
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	background.paint(g);
            g.setColor(Color.red);
            if(in != null) {
            	Pixel curr = (Pixel)pixelCoords.get(in);
            	g.fillOval(curr.getxCoordinate(), curr.getyCoordinate(), 5, 5);
            }
        }
   }
    
    public static String randomCountry() {
    	int index = (int)((Math.random()* co.size()-2)+1);
    	
    	return co.get(index);
    }
    
    public double distance(Coordinate c) {

    	Coordinate a = (Coordinate) country.get(answer);
    	
    	
    	double lat1 = Math.toRadians(c.getLatitude()), lat2 = Math.toRadians(a.getLatitude());  
    	double lon1 = Math.toRadians(c.getLongitude()), lon2 = Math.toRadians(a.getLongitude());

    	
    	double dlon = lon2 - lon1; 
        double dlat = lat2 - lat1;
        double x = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);
             
        double y = 2 * Math.asin(Math.sqrt(x));
 
        // Radius of earth in kilometers. Use 3956 
        // for miles
        double r = 3956;
 
        // calculate the result
        return(round((y * r),2));

    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == button && (country.get(textField.getText()) != null)) {
    		String display = "";
    		double dist = distance((Coordinate)country.get(textField.getText()));
    		display = textField.getText() + ", distance to answer: " + dist +  " mi";
    		((DefaultListModel) answers.getModel()).addElement(textField.getText());
            in = textField.getText();
            joever = true;
            System.out.println(answer);
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_ENTER && (country.get(textField.getText()) != null)) {
    		((DefaultListModel) answers.getModel()).addElement(textField.getText());
    		String display = "";
    		double dist = distance((Coordinate)country.get(textField.getText()));
    		display = textField.getText() + ", distance to answer: " + dist +  " mi";
            in = textField.getText();
    		System.out.print(in);
            joever = true;
            repaint();
        }
    }


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
