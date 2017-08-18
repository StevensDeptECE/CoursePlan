public class Point {
	final static double R = 6371.009f; // km
	double mapR;
			
	double x;
	double y;
	
	double lat;
	double lon;
	
	double latDegree;
	double lonDegree;
	
	String latStr;
	String lonStr;
  
	Point(double x, double y, float mapW, float mapH) {
		this.x = x;
		this.y = y;
		
		double absoluteX = this.x - mapW / 2;
		double absoluteY = mapH / 2 - this.y;
		
		mapR = mapW / (2 * Math.PI);
		
		// Mercator projection - Gudermannian function	
		lat = Math.atan(Math.sinh(absoluteY / mapR));
		lon = 0 + absoluteX / mapR;
		
		latDegree = getDegreeValue(lat);
		lonDegree = getDegreeValue(lon);
		
		latStr = getLatString();
		lonStr = getLonString();
		
	}
	
	// Great Circle Distance
	public double getDist(Point p) {
		double deltaLon = Math.abs(this.lon - p.lon);
		double centralAngle = Math.acos(Math.sin(this.lat) * Math.sin(p.lat) + Math.cos(this.lat) * Math.cos(p.lat) * Math.cos(deltaLon)); 
    
		return R * centralAngle;
	}
	
	public boolean isSamePoint(int mx, int my) {
		double distanceToCenter = Math.sqrt((mx - x) * (mx - x) + (my - y) * (my - y));
		
		if (distanceToCenter <= 10) {
			return true;
		}
		
		return false;
	}
	
	public double getDegreeValue(double l) {
		return l / Math.PI * 180;
	}
	
	public String getLatString() {				
//		String latStr = "";
//		
//		int latInt = (int) (latDegree * 100) / 100;
//		int latDec = (int) (latDegree * 100) % 100;
//		
//		latStr += latInt + "°";
//		latStr += latDec + "′";
		
		int l = (int) latDegree;
		
		String latStr = "" + Math.abs(l) + "°";
		
		if (l > 0) {
			latStr += "N";
		} else if (l < 0) {
			latStr += "S";
		}
		
		return latStr;
	}
	
	public String getLonString() {		
//		String lonStr = "";
//		
//		int latInt = (int) (lonDegree * 100) / 100;
//		int latDec = (int) (lonDegree * 100) % 100;
//		
//		lonStr += latInt + "°";
//		lonStr += latDec + "′";
		
		int l = (int) lonDegree;
		
		String lonStr = "" + Math.abs(l) + "°";
		
		if (l > 0) {
			lonStr += "E";
		} else if (l < 0) {
			lonStr += "W";
		}
		
		return lonStr;
	}

}