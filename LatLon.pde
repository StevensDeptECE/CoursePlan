class LatLon {  
  double lat;
  double lon;
  
  // Mercator projection - Gudermannian function
  LatLon(Point p) {
    Point absoluteP = getAbsolutePos(p);
    
    lat = Math.atan(Math.sinh(absoluteP.y / mapR));
    lon = 0 + absoluteP.x / mapR;
  }
  
  Point getAbsolutePos(Point p) {
    return new Point(p.x - center.x, center.y - p.y);
  }

}