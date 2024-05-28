import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JList;

public class Main extends JPanel implements ActionListener, KeyListener {
    private static boolean swi = false;
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
    private static ArrayList<String> entered = new ArrayList<String>();
    private static ArrayList<Double> distances = new ArrayList<Double>();
    private ArrayList<String> sameInput = new ArrayList<String>();
    private static ArrayList<String> co;
    private static int score = 100000;

    private static boolean win = false;

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

    public static void readInput() {
        try {

            Scanner scanner = new Scanner(new File("Centroids.csv"));

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            country = new HashMap<String, Coordinate>();
            co = new ArrayList<String>();

            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(",");
                for (int i = 2; i < data.length; i++) {
                    System.out.println(data[i]);
                    i += 2;
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
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Main() {
        JFrame world = new JFrame("World");
        JPanel legend = new DrawLegend();
        JPanel main = new JPanel();
        JPanel inputRight = new JPanel();
        JPanel graphics = new DrawPane();
        JPanel inputBL = new JPanel();
        world.setPreferredSize(new Dimension(1920, 1080));
        graphics.setPreferredSize(new Dimension(width - 20, height));
        legend.setPreferredSize(new Dimension(1920, 500));
        legend.setBounds(0, 0, 10000, 10000);
        ;
        world.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputRight.setLayout(new FlowLayout());
        inputRight.setPreferredSize(new Dimension(350, height));
        inputBL.setLayout(new FlowLayout());
        inputBL.setPreferredSize(new Dimension(350, 500));
        answers = new JList(new DefaultListModel<>());
        JScrollPane scrollPane = new JScrollPane(answers);
        scrollPane.setPreferredSize(new Dimension(350, height - 185));
        inputRight.add(scrollPane);

        textField = new JTextField("Enter Country");
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addKeyListener(this);
        textField.setPreferredSize(new Dimension(100, 20));
        inputRight.add(textField);

        button = new JButton("Submit");
        button.addActionListener(this);
        button.addKeyListener(this);
        world.addKeyListener(this);

        JTextPane textArea = new JTextPane();
        textArea.setContentType("text/html");
        textArea.setEditable(false);
        textArea.setBackground(null);
        textArea.setBorder(null);
        textArea.setText("List of countries" + "<html><a href=''>" + "https://shorturl.at/PJ5sp" + "</a></html>");
        inputBL.add(textArea);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(backgroundImage.getImage(), new Point(0, 0),
                "custom cursor"));
        setCursor(
                Toolkit.getDefaultToolkit().createCustomCursor(winImage.getImage(), new Point(0, 0), "custom cursor"));

        main.add(graphics);
        main.add(inputRight);
        main.add(inputBL);
        main.add(legend);
        world.setContentPane(main);
        Timer tim = new Timer(16, this);
        tim.start();
        world.pack();

        world.setVisible(true);

    }

    class DrawLegend extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(14474460));
            g.fillRect(0, 0, 10000, 10000);
            Font font = new Font("Kita", Font.BOLD, 25);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawRect(30, 30, 350, 300);
            g.drawString("Legend", 160, 60);
            g.setColor(Color.GREEN);
            g.fillOval(50, 100, 10, 10);
            g.drawString("= right answer", 70, 115);
            g.setColor(Color.RED);
            g.fillOval(50, 140, 10, 10);
            g.drawString("= within 500 miles", 70, 155);
            g.setColor(Color.ORANGE);
            g.fillOval(50, 180, 10, 10);
            g.drawString("= 500 - 1,000 miles", 70, 195);
            g.setColor(Color.YELLOW);
            g.fillOval(50, 220, 10, 10);
            g.drawString("= 1,000 - 5,000 miles", 70, 235);
            g.setColor(Color.PINK);
            g.fillOval(50, 260, 10, 10);
            g.drawString("= 5000 - 10,000 miles", 70, 275);
            g.setColor(Color.WHITE);
            g.fillOval(50, 300, 10, 10);
            g.drawString("= more than 10,000 miles", 70, 315);
        }
    }

    class DrawPane extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Font font = new Font("Kita", Font.BOLD, 25);
            g.setFont(font);
            background.paint(g);
            g.drawString("Score: " + score, 20, 300);
            if (color == 0) {
                g.setColor(Color.GREEN);
                win = true;
            } else if (color == 1) {
                g.setColor(Color.RED);
            } else if (color == 2) {
                g.setColor(Color.ORANGE);
            } else if (color == 3) {
                g.setColor(Color.yellow);
            } else if (color == 4) {
                g.setColor(Color.PINK);
            } else {
                g.setColor(Color.WHITE);
            }
            if (in != null) {
                Pixel curr = (Pixel) pixelCoords.get(in);

                g.fillOval(curr.getxCoordinate() - 10, curr.getyCoordinate() - 10, 10, 10);

                g.setColor(Color.black);

                g.drawString("Score: " + score, 20, 300);
                if (win) {
                    won.paint(g);
                    Font winf = new Font("Comic Sans", Font.BOLD, 30);
                    g.setFont(winf);
                    g.drawString("The answer was " + answer + "! Press Space to reset your score.", 50, 100);
                    win = false;

                }
            }
        }
    }

    public static String randomCountry() {
        int index = (int) ((Math.random() * co.size() - 2) + 1);

        return co.get(index);
    }

    public double distance(Coordinate c) {
        Coordinate a = (Coordinate) country.get(answer);
        double lat1 = Math.toRadians(c.getLatitude()), lat2 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(c.getLongitude()), lon2 = Math.toRadians(a.getLongitude());
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double x = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double y = 2 * Math.asin(Math.sqrt(x));
        double r = 3956;
        return (round((y * r), 2));
    }

    public static double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void bubbleSort(ArrayList<Double> distances, ArrayList<String> text, int n) {
        int i, j;
        double temp;
        String temp2;
        boolean swapped;
        for (i = 0; i < n - 1; i++) {
            swapped = false;
            for (j = 0; j < n - i - 1; j++) {
                if (distances.get(j) > distances.get(j + 1)) {
                    temp = distances.get(j);
                    temp2 = text.get(j);
                    distances.set(j, distances.get(j + 1));
                    text.set(j, text.get(j + 1));
                    distances.set(j + 1, temp);
                    text.set(j + 1, temp2);
                    swapped = true;
                }
            }
            if (swapped == false)
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button && (country.get(textField.getText()) != null)
                && sameInput.indexOf(textField.getText() + ", distance to answer: "
                        + distance((Coordinate) country.get(textField.getText())) + " mi") == -1) {
            swi = true;
            double dist = distance((Coordinate) country.get(textField.getText()));
            distances.add(dist);
            if (dist == 0) {
                color = 0;
            } else if (dist < 500) {
                color = 1;
                score -= 2000;
            } else if (dist < 1000) {
                color = 2;
                score -= 2000;
            } else if (dist < 5000) {
                color = 3;
                score -= 2000;
            } else if (dist < 10000) {
                color = 4;
                score -= 2000;
            } else {
                color = 5;
                score -= 2000;
            }
            String display = textField.getText() + ", distance to answer: " + dist + " mi";
            entered.add(display);
            in = textField.getText();
            System.out.println(display);
            sameInput.add(display);
            bubbleSort(distances, entered, distances.size());
            answers.setModel(new DefaultListModel());
            for (int i = 0; i < entered.size(); i++) {
                ((DefaultListModel) answers.getModel()).addElement(entered.get(i));
            }
        } 
        else if (e.getSource() == button && (country.get(textField.getText()) != null)
                && sameInput.indexOf(textField.getText()) != -1) {
            System.out.println("Country has already been guessed!");
        }
        // Reset the game when space key is pressed
        else if (e.getActionCommand().equals("Space")) {
            // Clear the lists of entered countries and distances
            entered.clear();
            distances.clear();

            // Clear the JTextField and set the answer to a new random country
            textField.setText("");
            answer = randomCountry();
            System.out.println(answer);

            // Clear the JList
            answers.setModel(new DefaultListModel());

            // Reset the in variable to null and reset the score
            in = null;
            score = 100000;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && (country.get(textField.getText()) != null)
                && sameInput.indexOf(textField.getText() + ", distance to answer: "
                        + distance((Coordinate) country.get(textField.getText())) + " mi") == -1) {
            swi = true;
            double dist = distance((Coordinate) country.get(textField.getText()));
            distances.add(dist);
            if (dist == 0) {
                color = 0;
            } else if (dist < 500) {
                color = 1;
                score -= 2000;
            } else if (dist < 1000) {
                color = 2;
                score -= 2000;
            } else if (dist < 5000) {
                color = 3;
                score -= 2000;
            } else if (dist < 10000) {
                color = 4;
                score -= 2000;
            } else {
                color = 5;
                score -= 2000;
            }
            String display = textField.getText() + ", distance to answer: " + dist +  " mi";
    		((DefaultListModel) answers.getModel()).addElement(display);
    		entered.add(textField.getText());
            in = textField.getText();
            System.out.println(display);
            sameInput.add(display);
            
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            answers.setModel(new DefaultListModel());
            in = null;
            score = 100000;
            answer = randomCountry();
            System.out.println(answer);

        }
        if(win) {
        	if (e.getKeyCode() == KeyEvent.VK_ENTER && (country.get(textField.getText()) != null)
                && sameInput.indexOf(textField.getText()) != -1) {
        		System.out.println("Country has already been guessed or is invalid!");
        	}
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
