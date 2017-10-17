package edu.stevens.nav;
/**
 * 
 * @author dkruger
 * Represent a single coordinate on the globe 
 * (lat = [-90,+90] and lat=[-180, +180])
 * Numbers are stored in double precision to ensure at least 8 digits
 * (1m accuracy) with 7 digits remaining for roundoff and higher accuracy
 * in the future.
 * 
 * All conversion to/from the rectangular coordinates on the map 
 * are in the class Point2.
 */
public class LatLon {
	private final static double R = 6371.009; // km
	public final static double RAD2DEG = 180/Math.PI;
	public final static double DEG2RAD = Math.PI/180;
	
	public double lat;	//internal representation is radians
	public double lon;

	public LatLon(double lat, double lon) {
		this.lat = lat; this.lon = lon;
	}

	public static LatLon degrees(double lat, double lon) {
		return new LatLon(lat*DEG2RAD, lon*DEG2RAD);
	}
	// display in degrees
	public String toString() {
		return String.format("%8.6f, %8.6f", getLatDeg(), getLonDeg());
	}
	public double getLatDeg() { return RAD2DEG * lat; }
	public double getLonDeg() { return RAD2DEG * lon; }

	// Great Circle Distance
	public double dist(LatLon p2) {
		double deltaLon = Math.abs(lon - p2.lon);
		double centralAngle = Math.acos(Math.sin(lat) * Math.sin(p2.lat) +
				Math.cos(lat) * Math.cos(p2.lat) * Math.cos(deltaLon));    
		return R * centralAngle;
	}

	//course heading to go great circle to point p2	
	public double course(LatLon p2) {
		double dLon = p2.lon - lon;
		double tana = Math.sin(dLon) / (Math.cos(lat)*Math.tan(p2.lat) - Math.sin(lat)*Math.cos(dLon));
		return Math.atan(tana);
	}
}
