import processing.core.*; 
import java.util.ArrayList;

public class CourseWin extends PApplet {
  PImage img;
    
  float ratio;
  float mapW;
  float mapH;
  
  ArrayList<Point> waypoints = new ArrayList<Point>();
  
  double distanceSum = 0;

  public void setup() {
    addImage();

    int screenWidth = width;
    
    int winW = (int) mapW + 1;
    int winH = (int) mapH + 1;
        
    surface.setSize(winW, winH);
    surface.setLocation(screenWidth - winW, 0);
    
  }
  
  public void addImage() {
    img = loadImage("Mercator_projection_SW.jpg");
    
    ratio = (float) Math.min(1.0f * displayHeight / img.height, 0.8f * displayWidth / img.width);
    mapW = img.width * ratio;
    mapH = img.height * ratio;
    
  }
    
  public void draw() {
    image(img, 0, 0, mapW, mapH);
        
    for (int i = 0; i < waypoints.size(); i++) {
      Point curr = waypoints.get(i);
    
        fill(255);
        stroke(0);
        strokeWeight(1);
    
        ellipse((float) curr.x, (float) curr.y, 10, 10);
        text(curr.latStr, (float) curr.x - 40, (float) curr.y - 5);
        text(curr.lonStr, (float) curr.x - 40, (float) curr.y + 10);
        
        if (i > 0) {
          Point prev = waypoints.get(i - 1);
          drawDistLine(prev, curr);         
        }
    }
    
    fill(0);
    text("Distance Sum: " + nfc((float) distanceSum, 3), 20, height - 80);
    text("Unit: km", 20, height - 60);
      
  }
  
  public void drawDistLine(Point prev, Point curr) {
    if (Math.abs(prev.lon - curr.lon) <= Math.PI) {
      line((float) prev.x, (float) prev.y, (float) curr.x, (float) curr.y);
        text(nfc((float) prev.getDist(curr), 1), (float) (prev.x + curr.x) / 2.0f, (float) (prev.y + curr.y) / 2.0f);
      
    } else {
      Point left = prev;
      Point right = curr;
      
      if (prev.x > curr.x) {
        left = curr;
        right = prev;
      }
      
      double tan = (right.y - left.y) / (right.x - (left.x + width));
      
      float xr = width;
      float yr = (float) (right.y + (tan * (width - right.x)));
      line((float) right.x, (float) right.y, xr, yr);
      
      float xl = 0;
      float yl = (float) (left.y - (tan * (left.x - 0)));
      line(xl, yl, (float) left.x, (float) left.y);
      
      text(nfc((float) prev.getDist(curr), 1), (float) (left.x + xl) / 2.0f, (float) (left.y + yl) / 2.0f);
    }
  }
  
  public void mousePressed() {
    boolean isExist = false;
    
    for (int i = waypoints.size() - 1; i >= 0; i--) {
      Point curr = waypoints.get(i);
      
      if (curr.isSamePoint(mouseX, mouseY)) {
        waypoints.remove(i);
        isExist = true;
        
        break;
      }
    }
    
    if (!isExist) {
      if (mouseX >= 0 && mouseX <= mapW && mouseY >= 0 && mouseY <= mapH) {
        waypoints.add(new Point(mouseX, mouseY, mapW, mapH));       
      }
    }
    
    // calculate distance
    distanceSum = 0.0;
    for (int i = 0; i < waypoints.size(); i++) {
      Point curr = waypoints.get(i);
      
      if (i > 0) {
        Point prev = waypoints.get(i - 1);
        distanceSum += prev.getDist(curr);
      }
    }
    
  }

  public void settings() { size(displayWidth, displayHeight); }

  static public void main(String[] passedArgs) {
    new MainWin();
  }

}
