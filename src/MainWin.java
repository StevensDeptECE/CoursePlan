import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import processing.core.PApplet;

public class MainWin extends JFrame {
	public MainWin() {
		super("Course Planner");
		
		setSize(500,400);
		Container c = getContentPane();
		
		Button startButton = new Button("Start Navigation");
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
		
		c.add(BorderLayout.WEST, p);
		
		setVisible(true);
	}

}
