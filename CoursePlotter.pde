PImage img;

float ratio;
float mapW;
float mapH;

Point center;

final static double R = 6371.009; // km
double mapR;

ArrayList<Point> waypoints = new ArrayList<Point>();

ArrayList<Double> distances = new ArrayList<Double>();
double distanceSum = 0;

void setup() {
  fullScreen();
  img = loadImage("Mercator_projection_SW.jpg");
  
  ratio = Math.min(1.0 * height / img.height, 0.8 * width / img.width);
  mapW = img.width * ratio;
  mapH = img.height * ratio;
  
  center = new Point(mapW / 2, mapH / 2);
  
  mapR = mapW / (2 * Math.PI);
  
}

void draw() {
  image(img, 0, 0, mapW, mapH);

  for (int i = 0; i < waypoints.size(); i++) {
    Point curr = waypoints.get(i);

    fill(255);
    stroke(0);
    strokeWeight(1);

    ellipse((float) curr.x, (float) curr.y, 10, 10);

    if (i > 0) {
      Point prev = waypoints.get(i - 1);
      line((float) prev.x, (float) prev.y, (float) curr.x, (float) curr.y);
      text(nfc(distances.get(i - 1).floatValue(), 2), (float) (prev.x + curr.x) / 2.0, (float) (prev.y + curr.y) / 2.0);
    }
  }

  fill(0);
  text("Distance Sum: " + nfc((float) distanceSum, 4), 20, height - 20);
  text("Unit: km", 20, height - 5);
  
}

void mousePressed() {
  waypoints.add(new Point(mouseX, mouseY));

  if (waypoints.size() > 1) {
    Point prev = waypoints.get(waypoints.size() - 2);
    Point curr = waypoints.get(waypoints.size() - 1);

    distances.add(prev.getDist(curr));
    distanceSum += distances.get(waypoints.size() - 2);
  }
  
}