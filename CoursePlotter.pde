PImage img;
void setup() {
   fullScreen();
   img = loadImage("Mercator_projection_SW.jpg");
}

static class LatLon {
  private double lat,lon;
  public LatLon(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }
  public LatLon(int lat, int lon, int w, int h) {
    // first compute fractions from 0 to 1
    double latFrac = (double)lat / h;
    double lonFrac = (double)lon / w;
    
    
  }
  public double dist(LatLon L2) {
    
  }
}

ArrayList<LatLon> waypoints = new ArrayList<LatLon>();

void draw() {
   image(img,0,0, width, height);
   if (waypoints.size() == 0)
     return;
   LatLon last = waypoints.get(0);
   for (LatLon latlon : waypoints) {
     ellipse((float)latlon.lon, (float)latlon.lat, 10, 10);
     line((float)last.lon, (float)last.lat, (float)latlon.lon, (float)latlon.lat);
     last = latlon;
   }
}

void mousePressed() {
  waypoints.add(new LatLon(mouseY, mouseX));
}