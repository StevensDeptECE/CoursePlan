import java.io.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import processing.core.PApplet;

public class MainWin extends JFrame {
		
	JButton startButton, quitButton;
	JButton addButton, deleteButton, resetButton;
	
	LatLonTable table = CourseWin.table;
	
	public MainWin() {
		super("Course Planner");
		
//		Container c = getContentPane();
//		
//		// panels
//		JPanel panel1 = new JPanel();
//		panel1.setBackground(Color.RED);
//		panel1.setLayout(new GridLayout(6,1,5,5));
//		
//		// buttons
//		ListenForButton lb = new ListenForButton();
//		
//		startButton = new JButton("Start Navigation");
//		startButton.addActionListener(lb);
//		panel1.add(startButton);
//		
//		addButton = new JButton("Add Position");
//		addButton.addActionListener(lb);
//		panel1.add(addButton);
//		
//		// container
//		c.add(BorderLayout.WEST, panel1);
		
		int[] size = calculateSize();
		this.setSize(size[0], size[1]);
		
		this.setLocation(0, 0);
		this.setResizable(true);
		
		// thePanel
		JPanel thePanel = new JPanel();
		thePanel.setLayout(new GridBagLayout());
		
		// panel1 - start & quit
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));
		
		startButton = new JButton("Start Navigation");
		quitButton = new JButton("Quit App");
		
		ListenForButton lb = new ListenForButton();
		startButton.addActionListener(lb);
		
		panel1.add(startButton);
		panel1.add(quitButton);
		
		addComp(thePanel, panel1, 0, 0, 1, 1, this.getWidth(), this.getHeight() / 10, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		
		// panel2 - table
		JPanel panel2 = new JPanel();
		
		JTable jtable = new JTable(table);
		panel2.add(jtable);
//		panel2.setBackground(Color.blue);
		
		addComp(thePanel, panel2, 0, 1, 1, 3, this.getWidth(), this.getHeight() / 10 * 8, GridBagConstraints.CENTER, GridBagConstraints.BOTH);

		// panel3 - start & quit
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(1, 3));
		
		addButton = new JButton("Add");
		deleteButton = new JButton("Delete");
		resetButton = new JButton("Reset");
		
		panel3.add(addButton);
		panel3.add(deleteButton);
		panel3.add(resetButton);
		
		addComp(thePanel, panel3, 0, 4, 1, 1, this.getWidth(), this.getHeight() / 10, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
				
		this.add(thePanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
	
	private class ListenForButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == startButton) {
				PApplet.main("CourseWin");
			}
			
			if (e.getSource() == addButton) {
				
			}
		}
	}
	
	private void addComp(JPanel thePanel, JComponent comp, int xPos, int yPos, int compWidth, int compHeight, int ipadX, int ipadY, int place, int stretch) {
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = xPos;
		gridConstraints.gridy = yPos;
		gridConstraints.gridwidth = compWidth;
		gridConstraints.gridheight = compHeight;
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		gridConstraints.insets = new Insets(10, 10, 10, 10);
		gridConstraints.ipadx = ipadX;
		gridConstraints.ipady = ipadY;
		gridConstraints.anchor = place;
		gridConstraints.fill = stretch;
		
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
