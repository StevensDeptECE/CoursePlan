import java.io.*;
import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import processing.core.PApplet;

public class MainWin extends JFrame {	
	LatLonTable table;
	
	JTable jtable;
	
	JPanel mainPanel;
	
	JButton startButton, quitButton, saveButton, updateButton, addButton, deleteButton, resetButton;
	
	JLabel distLabel;
	
	JTextField textLat, textLon;
	
	private boolean isStart = false;
	private int printTimes = 0;
	
	public MainWin(LatLonTable table) {
		super("Course Planner");
		
		this.table = table;
		
		int[] size = calculateSize();
		setSize(size[0], size[1]);
		
		setLocation(0, 0);
		setResizable(true);
		
		// mainPanel
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		// ActionListener for buttons
		ListenForButtons alForButtons = new ListenForButtons();
		
		// part_1: controlPanel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(2, 1));
		
		// part_1_1: welcomeLabel
		addLabel(controlPanel, "Welcome! Let's Navigation!", 21.0f, JLabel.CENTER);
		
		// part_1_2: controlButtonsPanel - controlButtons (start, quit & save)
		JPanel controlButtonsPanel = new JPanel();
		controlButtonsPanel.setLayout(new GridLayout(1, 3));
			
		startButton = addButton(controlButtonsPanel, "Start", alForButtons);
		quitButton = addButton(controlButtonsPanel, "Quit", alForButtons);
		saveButton = addButton(controlButtonsPanel, "Save", alForButtons);
		
		controlPanel.add(controlButtonsPanel);
		
		addComp(controlPanel, 0, 0, 1, 1, this.getWidth(), this.getHeight() / 7);
				
		// part_2: tablePanel
		jtable = new JTable(table);
		jtable.setToolTipText("Click and revise values here!");
		
        JScrollPane tablePanel = new JScrollPane(jtable);
        
        table.parentPanel = tablePanel;
        
		addComp(tablePanel, 0, 1, 1, 1, this.getWidth(), this.getHeight() / 3 * 2);

		// part_3: distPanel
		JPanel distPanel = new JPanel();
		distPanel.setToolTipText("Click \"Update\" button to update!");
		
		distLabel = addLabel(distPanel, "Distance Sum: " + table.getDistanceSum() + " (km)");
		updateButton = addButton(distPanel, "Update", alForButtons);
		
		addComp(distPanel, 0, 2, 1, 1, this.getWidth(), this.getHeight() / 9);
	
		// part_4: operatePanel
		JPanel operatePanel = new JPanel();
		operatePanel.setLayout(new GridLayout(3, 1));
		
		// part_4_1: inputPanel
		JPanel inputPanel = new JPanel();
		
		addLabel(inputPanel, "Input Pos: ", 14.0f, JLabel.LEFT);
		
		textLat = addTextField(inputPanel, "Latitude", 8);
		textLon = addTextField(inputPanel, "textLon", 8);
		
		operatePanel.add(inputPanel);
		
		// part_4_2: remindLabel
		addLabel(operatePanel, "[e.g., 23=23°N, -23=23°S, 67=67°E, -67=67°W]", 12.0f, JLabel.CENTER);
		
		// part_4_3: operateButtonsPanel - operateButtons (add, delete & reset)
		JPanel operateButtonsPanel = new JPanel();
		operateButtonsPanel.setLayout(new GridLayout(1, 3));
		
		addButton = addButton(operateButtonsPanel, "Add", alForButtons);
		deleteButton = addButton(operateButtonsPanel, "Delete", alForButtons);
		resetButton = addButton(operateButtonsPanel, "Reset", alForButtons);
		
		operatePanel.add(operateButtonsPanel);
		
		addComp(operatePanel, 0, 3, 1, 1, this.getWidth(), this.getHeight() / 8);
		
		// add thePanel				
		this.add(mainPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}

	private JButton addButton(JPanel panel, String text, ActionListener al) {
		JButton button = new JButton(text);
		button.addActionListener(al);
		panel.add(button);
		
		return button;
	}
	
	private void addLabel(JPanel panel, String text, float fontSize, int horizontalAlignment) {
		JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(fontSize));
		label.setHorizontalAlignment(horizontalAlignment);
		
		panel.add(label);
	}
	
	private JLabel addLabel(JPanel panel, String text) {
		JLabel label = new JLabel(text);
		panel.add(label);
		
		return label;
	}
	
	private JTextField addTextField(JPanel panel, String text, int columns) {
		JTextField tf = new JTextField(text, columns);
		panel.add(tf);
		
		return tf;
	}
	
	private void addComp(JComponent comp, int gx, int gy, int gw, int gh, int ipadX, int ipadY) {
		GridBagConstraints gridConstraints = new GridBagConstraints();
		
		gridConstraints.gridx = gx;
		gridConstraints.gridy = gy;
		
		gridConstraints.gridwidth = gw;
		gridConstraints.gridheight = gh;
		
		gridConstraints.ipadx = ipadX;
		gridConstraints.ipady = ipadY;
		
		gridConstraints.weightx = 100;
		gridConstraints.weighty = 100;
		
		gridConstraints.fill = GridBagConstraints.VERTICAL;
		gridConstraints.anchor = GridBagConstraints.CENTER;
		gridConstraints.insets = new Insets(10, 15, 10, 15);
		
		mainPanel.add(comp, gridConstraints); 
	}
	
	public int[] calculateSize() {
		int[] size = new int[2];
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("Mercator_projection_SW.jpg")); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dm = tk.getScreenSize();
		
		double ratio = Math.min(1.0f * dm.getHeight() / img.getHeight(), 0.8f * dm.getWidth() / img.getWidth());
		
		size[0] = (int) (dm.getWidth() - img.getWidth() * ratio);
		size[1] = (int) (img.getHeight() * ratio) + 1;
				
		return size;
	}
	
	private class ListenForButtons implements ActionListener {
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
			
			if (e.getSource() == saveButton) {
				String fileName = table.saveToFile(++printTimes);
				
				if (fileName == null) {
					JOptionPane.showMessageDialog(MainWin.this, "Opps...Something wrong!", "Error", JOptionPane.ERROR_MESSAGE);
					
				} else {
					JOptionPane.showMessageDialog(MainWin.this, "Success!\nPlease check \"" + fileName + "\"!", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
			
			if (e.getSource() == updateButton) {
				table.updateDistance();
				distLabel.setText("Distance Sum: " + table.getDistanceSum() + " (km)");
			}
			
			if (e.getSource() == addButton) {
				try {
					Integer.parseInt(textLat.getText());
					
					try {
						Integer.parseInt(textLon.getText());
						table.add(new Point(textLat.getText(), textLon.getText()));
						
					} catch(NumberFormatException exp) {
						JOptionPane.showMessageDialog(MainWin.this, "Opps...Lon Input Wrong!\nPlease try an INTEGER!", "Input Wrong", JOptionPane.WARNING_MESSAGE);
						textLon.requestFocus();
					}
					
				} catch(NumberFormatException exp) {
					JOptionPane.showMessageDialog(MainWin.this, "Opps...Lat Input Wrong!\nPlease try an INTEGER!", "Input Wrong", JOptionPane.WARNING_MESSAGE);
					textLat.requestFocus();
				}
				
			}
			
			if (e.getSource() == deleteButton) {
				int[] rows = jtable.getSelectedRows();
				
				for (int i = rows.length - 1; i >= 0; i--) {
					table.remove(rows[i]);
				}
			}
			
			if (e.getSource() == resetButton) {
				textLat.setText("Latitude");
				textLon.setText("Longitude");
			}
		}
	}

}
