// import processing.core.*;

// public class CourseWin extends PApplet {
	
// 	public void settings() {
// 		size(1000,800);
// 	}
	
// 	public void draw() {
// 		line(0,0, 500,500);
// 	}
// 	public void mousePressed() {
		
// 	}
// 	public static void main(String[] args) {
// 		MainWin app = new MainWin();
// 		PApplet.main("CourseWin");
// 	}
// }

import processing.core.*; 
import java.util.ArrayList; 

public class CourseWin extends PApplet {

PImage img;

float ratio;
float mapW;
float mapH;

Point center;

final static double R = 6371.009f; // km
double mapR;

ArrayList<Point> waypoints = new ArrayList<Point>();

ArrayList<Double> distances = new ArrayList<Double>();
double distanceSum = 0;

public void setup() {
  
  img = loadImage("Mercator_projection_SW.jpg");
  
  ratio = Math.min(1.0f * height / img.height, 0.8f * width / img.width);
  mapW = img.width * ratio;
  mapH = img.height * ratio;
  
  center = new Point(mapW / 2, mapH / 2);
  
  mapR = mapW / (2 * Math.PI);
  
}

public void draw() {
  image(img, 0, 0, mapW, mapH);

  for (int i = 0; i < waypoints.size(); i++) {
    Point curr = waypoints.get(i);

    fill(255);
    stroke(0);
    strokeWeight(1);

    ellipse((float) curr.x, (float) curr.y, 10, 10);

    if (i > 0) {
      Point prev = waypoints.get(i - 1);
      line((float) prev.x, (float) prev.y, (float) curr.x, (float) curr.y);
      text(nfc(distances.get(i - 1).floatValue(), 2), (float) (prev.x + curr.x) / 2.0f, (float) (prev.y + curr.y) / 2.0f);
    }
  }

  fill(0);
  text("Distance Sum: " + nfc((float) distanceSum, 4), 20, height - 20);
  text("Unit: km", 20, height - 5);
  
}

public void mousePressed() {
	
	//double lat = ....
	//double lon = ....
	// main.coordinates.add(lat, lon);
  waypoints.add(new Point(mouseX, mouseY));

  if (waypoints.size() > 1) {
    Point prev = waypoints.get(waypoints.size() - 2);
    Point curr = waypoints.get(waypoints.size() - 1);

    distances.add(prev.getDist(curr));
    distanceSum += distances.get(waypoints.size() - 2);
    
  }
  
}
class LatLon {  
  double lat;
  double lon;
  
  // Mercator projection - Gudermannian function
  LatLon(Point p) {
    Point absoluteP = getAbsolutePos(p);
    
    lat = Math.atan(Math.sinh(absoluteP.y / mapR));
    lon = 0 + absoluteP.x / mapR;
  }
  
  public Point getAbsolutePos(Point p) {
    return new Point(p.x - center.x, center.y - p.y);
  }

}
class Point {
  double x;
  double y;
  
  Point(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  // https://en.wikipedia.org/wiki/Great-circle_distance
  public double getDist(Point p) {
    LatLon ll1 = new LatLon(this);
    LatLon ll2 = new LatLon(p);
    
    double deltaLon = Math.abs(ll1.lon - ll2.lon);
    double centralAngle = Math.acos(Math.sin(ll1.lat) * Math.sin(ll2.lat) + Math.cos(ll1.lat) * Math.cos(ll2.lat) * Math.cos(deltaLon)); 
    
    return R * centralAngle;
  }

}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
//    String[] appletArgs = new String[] { "CoursePlotter" };
//    if (passedArgs != null) {
//      PApplet.main(concat(appletArgs, passedArgs));
//    } else {
//      PApplet.main(appletArgs);
//    }
    
    new MainWin();
  }
}
