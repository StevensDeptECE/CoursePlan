package edu.stevens.nav;

public class TestLatLonNav {
	public static void distAndCourse(double lat1, double lon1, double lat2, double lon2) {
		LatLon p1 = LatLon.degrees(lat1,lon1);
		LatLon p2 = LatLon.degrees(lat2,lon2);
		System.out.print(p1 + "\t" +
						   p2 + "\t");
		System.out.printf("%8.6f\t%6.2f\n",p1.dist(p2), LatLon.RAD2DEG*p1.course(p2));		
	}
	public static void main(String[] args) {
		System.out.println("point 1\t\t\tpoint 2\t\t\tDistance\tCourse");
		distAndCourse(0, 0, 0, 1);
		distAndCourse(1, 0, 1, -1);
		distAndCourse(40, -74, 40, -70);
		distAndCourse(40, -74, 45, -74);
		distAndCourse(40, -74, 41, -73);
		distAndCourse(0, -15, 1, -14);
		distAndCourse(0, -15, 1, -16);
		distAndCourse(0, -15, -1, -14);
		distAndCourse(0, -15, -1, -16);
	}
}
