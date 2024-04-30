
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
    JList<String> answers;
    
    double xScale = width/(maxLong - minLong);
    double yScale = height/(maxLat - minLat);
    
    private ImageIcon backgroundImage = new ImageIcon("Map.jpg");
    private static Coordinate inputted;
    private static String in;
    private static HashMap country;
    public static void main(String[] arg) {
        new Main(); 
        readInput();
    }

    public static void readInput(){
        try {
        	
			Scanner scanner = new Scanner(new File("Centroids.csv"));
			
			if (scanner.hasNextLine()) {
                scanner.nextLine();             }
			
			country = new HashMap<String, Coordinate>();
			
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
    
    public Main() {
        JFrame world = new JFrame("Map");
        JPanel panel = new JPanel();
        
        answers = new JList(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(answers);
        scrollPane.setBounds(10, 300, 200, 300);
        
        textField = new JTextField(20);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBounds(0, 0, (int) (width), 50);
        
        button = new JButton("Enter");
        button.addActionListener(this);
        button.addKeyListener(this);

        world.setLayout(new BorderLayout());
        world.setSize(new Dimension(width, height));
        world.setResizable(false);
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
        world.getContentPane().add(scrollPane);
        Timer tim = new Timer(16, this);
        tim.start();
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        world.add(panel);

        world.setLocationRelativeTo(null);
        world.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        
    	super.paintComponent(g);
        background.paint(g);
        g.setColor(Color.red);
        if(in != null) {
        	Coordinate curr = (Coordinate)country.get(in);
        	g.drawOval((int)curr.getLatitude(), (int)curr.getLongitude(), 10, 10);
        	g.drawOval(500, 500, 100, 100);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == button) {
        	System.out.println(textField.getText());
        	((DefaultListModel) answers.getModel()).addElement(textField.getText());
        	in = textField.getText();

        	
        }
        
    }
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    	System.out.println(textField.getText());
	        ((DefaultListModel) answers.getModel()).addElement(textField.getText());
	        in = textField.getText();
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
