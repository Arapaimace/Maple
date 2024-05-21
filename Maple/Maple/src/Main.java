import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
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

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JList;

public class Main extends JPanel implements ActionListener, KeyListener {
        
	private Map background = new Map();
	private Win won = new Win();
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
	private ArrayList<String> sameInput = new ArrayList<String>();
    private static ArrayList<String> co;
    private static int score = 100000;
    
    boolean joever = false;
    private ImageIcon backgroundImage = new ImageIcon("EquiMap.png");
    private ImageIcon winImage = new ImageIcon("Win.jpg");

    private static Coordinate inputted;
    private static String in;
    private static HashMap country;
    private static HashMap pixelCoords;
    public static String answer;
    private static int color;
    
    public static void main(String[] arg) {
        Main m = new Main(); 

        readInput();
        
        answer = randomCountry();
        System.out.println(answer);

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
	                pixelCoords.put(c, new Pixel(xPos, yPos - 20));
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
        world.setPreferredSize(new Dimension(width+450,height-100));
        graphics.setPreferredSize(new Dimension(width-20, height));
        
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        inputRight.setLayout(new FlowLayout());
        inputRight.setPreferredSize(new Dimension(350, height));
        
        answers = new JList(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(answers);
        scrollPane.setPreferredSize(new Dimension(350, height-185));
        inputRight.add(scrollPane);
        
        textField = new JTextField("Enter Country");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addKeyListener(this);
        textField.setPreferredSize(new Dimension(100, 20));
        inputRight.add(textField);
        
        button = new JButton("Submit");
        button.addActionListener(this);
        button.addKeyListener(this);
        inputRight.add(button);

        world.addKeyListener(this);
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(winImage.getImage(), new Point(0, 0),
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
        @Override
    	protected void paintComponent(Graphics g) {
        	boolean win = false;
        	super.paintComponent(g);
        	background.paint(g);
            //color selection
        	if(color == 0) {
        		g.setColor(Color.GREEN);
        		win=true;

        	}
        	else if(color == 1)  {
        		g.setColor(Color.RED);
        	}
        	else if(color == 2) {
        		g.setColor(Color.ORANGE);
        	}
        	else if(color == 3) {
        		g.setColor(Color.yellow);
        	}
        	else if(color == 4) {
        		g.setColor(Color.PINK);
        	}
        	else {
        		g.setColor(Color.WHITE);
        		
        	}
            if(in != null) {
            	Pixel curr = (Pixel)pixelCoords.get(in);
            	g.fillOval(curr.getxCoordinate(), curr.getyCoordinate(), 10, 10);
            	g.setColor(Color.black);
            	g.drawString("Score: " + score, 20, 300);
            	if(win) {
            		won.paint(g);
            	}
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
    	if (e.getSource() == button && (country.get(textField.getText()) != null) && sameInput.indexOf(textField.getText()) == -1) {
    		double dist = distance((Coordinate)country.get(textField.getText()));
    		if(dist == 0) {
        		color = 0;
        	}
        	else if(dist < 500)  {
        		color = 1;
        		score-= 2000;
        	}
        	else if(dist < 1000) {
        		color = 2;
        		score-= 2000;
        	}
        	else if(dist < 5000) {
        		color = 3;
        		score-= 2000;

        	}
        	else if(dist < 10000) {
        		color = 4;
        		score-= 2000;

        	}
        	else {
        		color = 5;
        		score-= 2000;
        	}    
    		String display = textField.getText() + ", distance to answer: " + dist +  " mi";
    		((DefaultListModel) answers.getModel()).addElement(display);

            in = textField.getText();
            System.out.println(display);
            sameInput.add(display);
            repaint();
        }
    	else if(e.getSource() == button && (country.get(textField.getText()) != null) && sameInput.indexOf(textField.getText()) != -1) {
    		System.out.println("Country has already been guessed!");
    	}
    	repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_ENTER && (country.get(textField.getText()) != null) && sameInput.indexOf(textField.getText()) == -1) {
    		double dist = distance((Coordinate)country.get(textField.getText()));
    		if(dist == 0) {
        		color = 0;
        	}
        	else if(dist < 500)  {
        		color = 1;
        		score-= 2000;
        	}
        	else if(dist < 1000) {
        		color = 2;
        		score-= 2000;
        	}
        	else if(dist < 5000) {
        		color = 3;
        		score-= 2000;

        	}
        	else if(dist < 10000) {
        		color = 4;
        		score-= 2000;

        	}
        	else {
        		color = 5;
        		score-= 2000;
        	}    		
    		String display = textField.getText() + ", distance to answer: " + dist +  " mi";
    		((DefaultListModel) answers.getModel()).addElement(display);

            in = textField.getText();
            System.out.println(display);
            sameInput.add(display);
            repaint();
        }
    	else if(e.getKeyCode() == KeyEvent.VK_ENTER && (country.get(textField.getText()) != null) && sameInput.indexOf(textField.getText()) != -1) {
    		System.out.println("Country has already been guessed or is invalid!");
    	}
    	repaint();
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
