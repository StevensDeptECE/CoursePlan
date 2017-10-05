import processing.core.*;

import java.awt.Image;
import java.util.*;

public class CourseWin extends PApplet {  
  public static float mapW;
  public static float mapH;
  
  private PImage img;
  private float ratio;
  
  private ArrayList<Point> waypoints = LatLonTable.points;
  
  // for drag point - start
  private boolean isOverPoint = false;
  private boolean isLockPoint = false;
  
  private Point choosedPoint = null;
  private double[] offsets = new double[2];
  // for drag point - end
  
  public void setup() {
    addImage();

    int screenWidth = width;
    
    int winW = (int) mapW + 1;
    int winH = (int) mapH + 1;
        
    surface.setSize(winW, winH);
    surface.setLocation(screenWidth - winW, 0); 
  }
  
  public void addImage() {
    img = loadImage("images/Mercator_projection_SW.jpg");
    
    ratio = (float) Math.min(1.0f * displayHeight / img.height, 0.8f * displayWidth / img.width);
    
    mapW = img.width * ratio;
    mapH = img.height * ratio;
  }
    
  public void draw() {
    image(img, 0, 0, mapW, mapH);
    
    isOverPoint = false;
    
    stroke(0);
      strokeWeight(1);
        
    for (int i = 0; i < waypoints.size(); i++) {
      Point curr = waypoints.get(i);
    
      if (curr.isSamePoint(mouseX, mouseY)) { // isOver a point
        isOverPoint = true;
        choosedPoint = curr;
        fill(150);
      } else {
        fill(255);
      }
    
        ellipse((float) curr.x, (float) curr.y, 10, 10);
        
        fill(255);
        text(curr.getLatString("map"), (float) curr.x - 40, (float) curr.y - 5);
        text(curr.getLonString("map"), (float) curr.x - 40, (float) curr.y + 10);
        
        if (i > 0) {
          Point prev = waypoints.get(i - 1);
          drawDistLine(prev, curr);         
        }
    }
    
    fill(0);
    text("Distance Sum: " + nfc((float) LatLonTable.distanceSum, 2), 20, height - 80);
    text("Unit: km", 20, height - 60);
  }
  
  public void drawDistLine(Point prev, Point curr) {
    fill(255);
    
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
  
  public void mouseClicked() {
    boolean isExist = false;
    
    for (int i = waypoints.size() - 1; i >= 0; i--) {
      Point curr = waypoints.get(i);
      
      if (curr.isSamePoint(mouseX, mouseY)) {
        MainWin.table.remove(i);
        isExist = true;
        
        break;
      }
    }
    
    if (!isExist) {
      if (mouseX >= 0 && mouseX <= mapW && mouseY >= 0 && mouseY <= mapH) {
        Point newPoint = new Point(mouseX, mouseY);       
        MainWin.table.add(newPoint);
      }
    }
    
    LatLonTable.updateDistance();
    MainWin.updateMainWin();
  }
  
  public void mousePressed() {
    if (waypoints.size() > 0) {
      if (isOverPoint) {
        isLockPoint = true;
        
        offsets[0] = mouseX - choosedPoint.x;
        offsets[1] = mouseY - choosedPoint.y;
        
      } else {
        isLockPoint = false;
      }
    }
  }
  
  public void mouseDragged() {
    if (isLockPoint) {
      cursor(HAND);
      choosedPoint.updateXY(mouseX - offsets[0], mouseY - offsets[1]);
    }
  }
  
  public void mouseReleased() {
    isLockPoint = false;
    cursor(ARROW);
    
    LatLonTable.updateDistance();
    MainWin.updateMainWin();
  }

  public void settings() { size(displayWidth, displayHeight); }

  static public void main(String[] passedArgs) {
    new MainWin(Locale.ENGLISH, true);
  }

}
