package edu.stevens.nav;
/**
 * 
 * @author dkruger
 * Represent a single coordinate on the globe 
 * (lat = [-90,+90] and lat=[-180, +180]) 
 * but stored internally in radians not degrees
 * Only text output is in degrees.
 * 
 * Numbers are stored in double precision to ensure at least 8 digits
 * (1m accuracy) with 7 digits remaining for roundoff and higher accuracy
 * in the future.
 * 
 * 
 * All conversion to/from the rectangular coordinates on the map 
 * are in the class Point2.
 */
public class LatLon {
	private final static double R = 6371009; // m
	public final static double RAD2DEG = 180/Math.PI; // TODO: Nicer in utility class
	public final static double DEG2RAD = Math.PI/180;
	
	public double lat;	//internal representation is radians
	public double lon;

	// initialize lat/lon to radians
	public LatLon(double lat, double lon) {
		this.lat = lat; this.lon = lon;
	}

	// read in degrees lat/lon and create a LatLon object in radians
	public static LatLon degrees(double lat, double lon) {
		return new LatLon(lat*DEG2RAD, lon*DEG2RAD);
	}
	// display in degrees
	public String toString() {
		return String.format("%8.6f, %8.6f", getLatDeg(), getLonDeg());
	}
	public double getLatDeg() { return RAD2DEG * lat; }
	public double getLonDeg() { return RAD2DEG * lon; }

	// Great Circle Distance (in meters)
	public double dist(LatLon p2) {
		double deltaLon = Math.abs(lon - p2.lon);
		double centralAngle = Math.acos(Math.sin(lat) * Math.sin(p2.lat) +
				Math.cos(lat) * Math.cos(p2.lat) * Math.cos(deltaLon));    
		return R * centralAngle;
	}

	/*
	 * Returns initial course heading for great circle to point p2
	 * Angle convention is mathematical (not navigation) where 0 is due East
	 * positive counterclockwise	
	 */
	public double course(LatLon p2) {
		double dLon = p2.lon - lon;
		double tana = Math.sin(dLon) / (Math.cos(lat)*Math.tan(p2.lat) - Math.sin(lat)*Math.cos(dLon));
		return Math.atan(tana);
	}
}
