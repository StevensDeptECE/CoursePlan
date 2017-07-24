import processing.core.*;

public class CourseWin extends PApplet {
	
	public void settings() {
		size(1000,800);
	}
	
	public void draw() {
		line(0,0, 500,500);
	}
	public void mousePressed() {
		
	}
	public static void main(String[] args) {
		MainWin app = new MainWin();
		PApplet.main("CourseWin");
	}
}
