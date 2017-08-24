import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import processing.core.PApplet;

public class MainWin extends JFrame {	
	LatLonTable table;
	JTable jtable;
	
	JLabel distLabel;
	
	JTextField textLat, textLon;
	
	JButton startButton, quitButton, updateButton, addButton, deleteButton, resetButton;
	
	boolean isStart = false;
			
	public MainWin(LatLonTable table) {
		super("Course Planner");
		
		this.table = table;
		
		int[] size = calculateSize();
		this.setSize(size[0], size[1]);
		
		this.setLocation(0, 0);
		this.setResizable(true);
		
		// thePanel
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new GridBagLayout());
		
		ListenForButton lb = new ListenForButton();
		
		// panel1 - welcome
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2, 1));
		
		// panel1 - comp1 - welcome label
		JLabel welcomeLabel = new JLabel("Welcome! Let's Navigation!");
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont(22.0f));
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// panel1 - comp2 - button1Panel - start & quit
		JPanel button1Panel = new JPanel();
		button1Panel.setLayout(new GridLayout(1, 2));
		
		startButton = new JButton("Start");		
		quitButton = new JButton("Quit");
		
		startButton.addActionListener(lb);
		quitButton.addActionListener(lb);
		
		button1Panel.add(startButton);
		button1Panel.add(quitButton);
		
		// panel1 - add
		panel1.add(welcomeLabel);
		panel1.add(button1Panel);
		
		addComp(thePanel, panel1, 0, 0, 1, 1, this.getWidth(), this.getHeight() / 7);
				
		// panel2 - table
		jtable = new JTable(table);
        JScrollPane panel2 = new JScrollPane(jtable);
        
		addComp(thePanel, panel2, 0, 1, 1, 1, this.getWidth(), this.getHeight() / 3 * 2);

		// panel3 - distanceSum
		JPanel panel3 = new JPanel();
		panel3.setToolTipText("Click \"update\" button to update!");
		
		distLabel = new JLabel("Distance Sum: " + table.getDistanceSum() + " (km)");
		
		updateButton = new JButton("Update");
		updateButton.addActionListener(lb);
		
		panel3.add(distLabel);
		panel3.add(updateButton);
		
		addComp(thePanel, panel3, 0, 2, 1, 1, this.getWidth(), this.getHeight() / 9);
	
		// panel4 - input part
		JPanel panel4 = new JPanel();
		panel4.setLayout(new GridLayout(3, 1));
		
		// panel4 - comp1 - input area
		JPanel inputPanel = new JPanel();
		
		JLabel inputLabel = new JLabel("Input Pos: ");
		inputLabel.setFont(inputLabel.getFont().deriveFont(14.0f));
		inputLabel.setHorizontalAlignment(JLabel.LEFT);
		
		textLat = new JTextField("Latitude", 8);
		textLon = new JTextField("Longitude", 8);
				
		inputPanel.add(inputLabel);
		inputPanel.add(textLat);
		inputPanel.add(textLon);
		
		// panel4 - comp2 - remind label
		JLabel remindLabel = new JLabel("* Input Format: 23.5 (23°5′N), -67.86 (67°86′W)");
		remindLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// panel4 - comp3 - button2Panel - add & delete & reset
		JPanel button2Panel = new JPanel();
		button2Panel.setLayout(new GridLayout(1, 3));
		
		addButton = new JButton("Add");
		deleteButton = new JButton("Delete");
		resetButton = new JButton("Reset");
		
		addButton.addActionListener(lb);
		deleteButton.addActionListener(lb);
		resetButton.addActionListener(lb);
		
		button2Panel.add(addButton);
		button2Panel.add(deleteButton);
		button2Panel.add(resetButton);
		
		// panel4 - add
		panel4.add(inputPanel);
		panel4.add(remindLabel);
		panel4.add(button2Panel);
		
		addComp(thePanel, panel4, 0, 3, 1, 1, this.getWidth(), this.getHeight() / 8);
		
		// add thePanel				
		this.add(thePanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
	
	private class ListenForButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == startButton) {
				if (isStart) {
					table.points.clear();
					
					jtable.requestFocus();
					updateButton.doClick();
					
					textLat.setText("Latitude");
					textLon.setText("Longitude");
					
				} else {
					PApplet.main("CourseWin");
					
					isStart = true;
					startButton.setText("Restart");
				}
			}
			
			if (e.getSource() == quitButton) {
				System.exit(0);
			}
			
			if (e.getSource() == updateButton) {
				table.updateDistance();
				distLabel.setText("Distance Sum: " + table.getDistanceSum() + " (km)");
			}
			
			if (e.getSource() == addButton) {
				table.add(new Point(textLat.getText(), textLon.getText()));
			}
			
			if (e.getSource() == deleteButton) {
				
			}
			
			if (e.getSource() == resetButton) {
				textLat.setText("Latitude");
				textLon.setText("Longitude");
			}
		}
	}
	
	private void addComp(JPanel thePanel, JComponent comp, int gx, int gy, int gw, int gh, int ipadX, int ipadY) {
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = gx;
		gridConstraints.gridy = gy;
		
		gridConstraints.gridwidth = gw;
		gridConstraints.gridheight = gh;
		
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		
		gridConstraints.ipadx = ipadX;
		gridConstraints.ipady = ipadY;
		
		gridConstraints.fill = GridBagConstraints.VERTICAL;
		gridConstraints.anchor = GridBagConstraints.CENTER;
		gridConstraints.insets = new Insets(10, 15, 10, 15);
		
		thePanel.add(comp, gridConstraints); 
	}
	
	public int[] calculateSize() {
		int[] size = new int[2];
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("Mercator_projection_SW.jpg"));
		} catch (IOException e) {
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		
		double ratio = Math.min(1.0f * dm.getHeight() / img.getHeight(), 0.8f * dm.getWidth() / img.getWidth());
		
		size[0] = (int) (dm.getWidth() - img.getWidth() * ratio);
		size[1] = (int) (img.getHeight() * ratio) + 1;
				
		return size;
	}

}
