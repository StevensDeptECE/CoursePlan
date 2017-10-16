import edu.stevens.nav.LatLon;

/**
 * 
 * @author dkruger
 * Represent a point (x,y) on the map in CourseWin
 * all methods to convert to/from (lat,lon) spherical coordinates 
 * are in here
 */

public class Point2 {
	private final static int R = 10;
	private final static int RSQ = R*R;
	
	private double mapW = CourseWin.mapW;
	private double mapH = CourseWin.mapH;
	private double mapR = mapW / (2 * Math.PI);
	
	public double x;
	public double y;
  
	Point2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	

	// Mercator projection - Gudermannian function - reverse
	Point2(LatLon latlon) {
		double absX = mapR * latlon.lon;
		double absY = mapR * Math.log(Math.tan(Math.PI / 4 + latlon.lat / 2));
		x = absX + mapW / 2;
		y = mapH / 2 - absY;
	}

	// Mercator projection - Gudermannian function	
	void convertTo(LatLon latlon) {
		double absX = x - mapW / 2;
		double absY = mapH / 2 - y;
		latlon.lat = Math.atan(Math.sinh(absY / mapR));
		latlon.lon = absX / mapR;
	}
	
	public boolean approxEqual(Point2 p2) {
		double dx = x-p2.x, dy = y-p2.y;
		return dx*dx + dy*dy <= RSQ; // if distance <= R returns true
	}

}