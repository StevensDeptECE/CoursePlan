public class Point {
	final static double R = 6371.009f; // km
	
	double mapW = CourseWin.mapW;
	double mapH = CourseWin.mapH;
	double mapR = mapW / (2 * Math.PI);
	
	double x;
	double y;
	
	double lat;
	double lon;
  
	Point(double x, double y) {
		this.x = x;
		this.y = y;
		
		lat = getLatLon()[0];
		lon = getLatLon()[1];
		
	}
	
	Point(String dLatStr, String dLonStr) {
		lat = getRadValue(Double.parseDouble(dLatStr));
		lon = getRadValue(Double.parseDouble(dLonStr));
		
		x = getXY()[0];
		y = getXY()[1];
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
	
	public void updateLat(String dLatStr) {
		this.lat = getRadValue(Double.parseDouble(dLatStr));
		
		x = getXY()[0];
		y = getXY()[1];
	}
	
	public void updateLon(String dLonStr) {
		this.lon = getRadValue(Double.parseDouble(dLonStr));
		
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
		
		if (distanceToCenter <= 10) {
			return true;
		}
		
		return false;
	}
	
	public String getLatString() {
		int dl = (int) getDegreeValue(lat);
		
		String latStr = "" + Math.abs(dl) + "°";
		
		if (dl > 0) {
			latStr += "N";
		} else if (dl < 0) {
			latStr += "S";
		}
		
		return latStr;
	}
	
	public String getLatString(int flag) {
		if (flag == 2) {
			double dl = Math.abs(getDegreeValue(lat));
			
			String latStr = "";
			
			int latInt = (int) (dl * 100 / 100);
			int latDec = (int) (dl * 100 % 100);
			
			latStr += latInt + "°";
			latStr += latDec + "′";
			
			if (latInt != 0 && latDec != 0) {
				if (lat > 0) {
					latStr += "N";
				} else if (lat < 0) {
					latStr += "S";
				}
			}
			
			return latStr;	
			
		} else {
			return "error";
		}
	}
	
	public String getLonString() {
		int l = (int) getDegreeValue(lon);
		
		String lonStr = "" + Math.abs(l) + "°";
		
		if (l > 0) {
			lonStr += "E";
		} else if (l < 0) {
			lonStr += "W";
		}
		
		return lonStr;
	}
	
	public String getLonString(int flag) {
		if (flag == 2) {
			double dl = Math.abs(getDegreeValue(lon));
			
			String lonStr = "";
			
			int lonInt = (int) (dl * 100 / 100);
			int lonDec = (int) (dl * 100 % 100);
			
			lonStr += lonInt + "°";
			lonStr += lonDec + "′";
			
			if (lonInt != 0 && lonDec != 0) {
				if (lon > 0) {
					lonStr += "E";
				} else if (lon < 0) {
					lonStr += "W";
				}
			}
			
			return lonStr;
			
		} else {
			return "error";
		}
	}

}