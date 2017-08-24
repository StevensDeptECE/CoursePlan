import java.text.DecimalFormat;
import java.util.*;

import javax.swing.table.*;

public class LatLonTable extends AbstractTableModel{
    
    private static String columnNames[] = {"Lat", "Lon"};
    
    public ArrayList<Point> points = new ArrayList<Point>();
    
    public double distanceSum;
    
    public LatLonTable() {
        
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    public int getRowCount() { 
        return points.size(); 
    }
    
    public int getColumnCount() { 
        return columnNames.length; 
    }
    
    public boolean isCellEditable(int row, int col) { 
        return true; 
    }
    
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return points.get(row).getLatString(2);
        } else {
            return points.get(row).getLonString(2);
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
        Point p = points.get(row);
        
        if (col == 0) { // lat
            p.updateLat((String) value);
            
        } else { // lon
            p.updateLon((String) value);
        }
        
        fireTableCellUpdated(row, col);     
    }
    
    public void add(Point p) {
        points.add(p);
        fireTableRowsInserted(points.size() - 1, points.size() - 1);        
    }
    
    public void remove(int index) {
        points.remove(index);
        fireTableRowsDeleted(index, index);     
    }
        
    public void updateDistance() {
        distanceSum = 0.0;
        
        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            
            distanceSum += prev.getDist(curr);
        }
    }
    
    public String getDistanceSum() {
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(distanceSum);
    }
    
}
