import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainWin extends JFrame {
	public MainWin() {
		super("Course Planner");
		setSize(500,400);
		Container c = getContentPane();
		JPanel p = new JPanel();
		p.setBackground(Color.RED);
		p.setLayout(new GridLayout(6,1,5,5));
		p.add(new JButton("Draw"));
		c.add(BorderLayout.WEST, p);
		
		setVisible(true);
		
		
		
	}

}
