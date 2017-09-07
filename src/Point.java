public class Point {
	private final double R = 6371.009f; // km
	private final int pointR = 10;
	
	private double mapW = CourseWin.mapW;
	private double mapH = CourseWin.mapH;
	private double mapR = mapW / (2 * Math.PI);
	
	public double x;
	public double y;
	
	public double lat;
	public double lon;
  
	Point(double x, double y) {
		this.x = x;
		this.y = y;
		
		lat = getLatLon()[0];
		lon = getLatLon()[1];
	}
	
	Point(double dlat, double dlon, String flag) {
		if (flag.equals("degree")) {
			lat = getRadValue(dlat);
			lon = getRadValue(dlon);
			
			x = getXY()[0];
			y = getXY()[1];
		}
	}
	
	// Mercator projection - Gudermannian function	
	public double[] getLatLon() {
		double[] latlon = new double[2];
		
		double absX = this.x - mapW / 2;
		double absY = mapH / 2 - this.y;
		
		latlon[0] = Math.atan(Math.sinh(absY / mapR));
		latlon[1] = 0 + absX / mapR;
				
		return latlon;
	}
	
	// Mercator projection - Gudermannian function - reverse
	public double[] getXY() {
		double[] xy = new double[2];
		
		double absX = mapR * (lon - 0);
		double absY = mapR * Math.log(Math.tan(Math.PI / 4 + this.lat / 2));
		
		xy[0] = absX + mapW / 2;
		xy[1] = mapH / 2 - absY;
		
		return xy;
	}
	
	public void updateXY(double newX, double newY) {
		this.x = newX;
		this.y = newY;
		
		lat = getLatLon()[0];
		lon = getLatLon()[1];
	}	
	
	public void updateLat(double dLat) {
		this.lat = getRadValue(dLat);
		
		x = getXY()[0];
		y = getXY()[1];
	}
	
	public void updateLon(double dLon) {
		this.lon = getRadValue(dLon);
		
		x = getXY()[0];
		y = getXY()[1];
	}
	
	public double getDegreeValue(double r) {
		return r / Math.PI * 180;
	}
	
	public double getRadValue(double d) {
		return d * Math.PI / 180;
	}
	
	// Great Circle Distance
	public double getDist(Point p) {
		double deltaLon = Math.abs(this.lon - p.lon);
		double centralAngle = Math.acos(Math.sin(this.lat) * Math.sin(p.lat) + Math.cos(this.lat) * Math.cos(p.lat) * Math.cos(deltaLon)); 
    
		return R * centralAngle;
	}
	
	public boolean isSamePoint(int mx, int my) {
		double distanceToCenter = Math.sqrt(Math.pow((mx - x), 2) + Math.pow((my - y), 2));
		
		if (distanceToCenter <= pointR) {
			return true;
		}
		
		return false;
	}
	
	public String getLatString(String flag) {
		double dlat = getDegreeValue(lat);
		
		String latStr = "";
		
		boolean isPostfix = true;
		
		if (flag.equals("map")) {
			latStr += Math.abs((int) dlat);
			
		} else if (flag.equals("table")) {
			latStr += String.format("%.6f", Math.abs(dlat));
			
		} else if (flag.equals("write")) {
			latStr += String.format("%.6f", dlat);
			isPostfix = false;
		}
		
		if (isPostfix) {
			latStr += "°";
			
			if (dlat > 0) {
				latStr += "N";
			} else if (dlat < 0) {
				latStr += "S";
			}
		}
		
		return latStr;
	}
	
	public String getLonString(String flag) {
		double dlon = getDegreeValue(lon);
		
		String lonStr = "";
		
		boolean isPostfix = true;
		
		if (flag.equals("map")) {
			lonStr += Math.abs((int) dlon);
			
		} else if (flag.equals("table")) {
			lonStr += String.format("%.6f", Math.abs(dlon));
			
		} else if (flag.equals("write")) {
			lonStr += String.format("%.6f", dlon);
			isPostfix = false;
		}
		
		if (isPostfix) {
			lonStr += "°";
			
			if (dlon > 0) {
				lonStr += "E";
			} else if (dlon < 0) {
				lonStr += "W";
			}
		}
		
		return lonStr;
	}

}