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