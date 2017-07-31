import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import processing.core.PApplet;

public class MainWin extends JFrame {
	private CoordinateList coordinates;
	
	public CoordinateList getCoordinateList() { return coordinates; }
	public MainWin() {
		super("Course Planner");
		
		setSize(500,400);
		Container c = getContentPane();
		coordinates = new CoordinateList();
		
		JButton startButton = new JButton("Start Navigation");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PApplet.main("CourseWin");
			}
			
		});
		
		JPanel p = new JPanel();
		p.setBackground(Color.RED);
		p.setLayout(new GridLayout(6,1,5,5));
		p.add(startButton);
		
		JButton addPoint = new JButton("Add");
		addPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				coordinates.add(new CoordinateList.LL(0,0));
			}
		});
		p.add(addPoint);
		
		c.add(BorderLayout.WEST, p);
		JTable t = new JTable(coordinates);
		c.add(BorderLayout.CENTER, t);
		setVisible(true);
	}

}
