
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.awt.Image;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private int width = 1280;
	private int height = 641;
	private JButton button;
	private JTextField textField;
	private JLabel map;
	private JList<String> answers;
    
    private static ArrayList<String> co;
    
    private double xScale = width/(maxLong - minLong);
    private double yScale = height/(maxLat - minLat);
    
    boolean joever = false;
    private ImageIcon backgroundImage = new ImageIcon("Map.jpg");
    private static Coordinate inputted;
    private static String in;
    private static HashMap country;
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

		}
		catch (Exception e){
			System.out.println(e);
		}
    }
    
    class DrawPane extends JPanel {
        protected void paintComponent(Graphics g) {
        	super.paintComponent(g);
        	background.paint(g);
            g.setColor(Color.red);
        	g.fillOval(500, 500, 10, 10);
            if(in != null) {
            	Coordinate curr = (Coordinate)country.get(in);
            	g.drawOval((int)curr.getLatitude(), (int)curr.getLongitude(), 10, 10);
            }
        }
   }
    public Main() {
    	System.out.print(generateRandomCountry());
//    	System.out.println(generateRandomCountry());
        JFrame input = new JFrame("Input");
        JFrame world = new JFrame("World");
        world.setContentPane(new DrawPane());
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        world.setSize(new Dimension(width, height));
        world.setVisible(true); 
        
        JPanel panel = new JPanel();
        
        answers = new JList(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(answers);
        scrollPane.setBounds(10, 300, 200, 300);
        
        textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBounds(0, 0, (int) (width), 50);
        textField.addKeyListener(this);
        
//        button = new JButton("Enter");
//        button.addActionListener(this);
//        button.addKeyListener(this);
//        input.addMouseListener(this);

        input.setLayout(new BorderLayout());
        input.setResizable(false);
        input.addKeyListener(this);
        
//	    map = new JLabel("");
//	    Image img = new ImageIcon(this.getClass().getResource("Map.jpg")).getImage();
//	    map.setIcon(new ImageIcon(img));
        
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));

        panel.add(textField);
//        panel.add(button);
//      input.getContentPane().add(scrollPane);
        Timer tim = new Timer(16, this);
        tim.start();
        input.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        input.add(panel);

        input.setLocationRelativeTo(null);
        input.setVisible(true);
        input.pack();
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
    		((DefaultListModel) answers.getModel()).addElement(display);
            in = textField.getText();
            joever = true;
            System.out.println(answer);
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (e.getSource() == button && (country.get(textField.getText()) != null)) {
    		String display = "";
    		double dist = distance((Coordinate)country.get(textField.getText()));
    		display = textField.getText() + ", distance to answer: " + dist +  " mi";
    		((DefaultListModel) answers.getModel()).addElement(display);
            in = textField.getText();
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
