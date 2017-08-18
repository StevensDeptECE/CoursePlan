import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class LatLonTable extends AbstractTableModel{
	
	private static String columnNames[] = {"Lat", "Lon"};
	private ArrayList<Point> points;
	
	public LatLonTable() { 
		points = new ArrayList<Point>(); 
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
        	return points.get(row).latStr;
        } else {
        	return points.get(row).lonStr;
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
    	Point p = points.get(row);
    	
    	if (col == 0) { // lat
    		p.latDegree = (double) value;
    		
    	} else { // lon
    		p.lonDegree = (double) value;
    	}
    	
    	fireTableCellUpdated(row, col);
    }
    
    public void add(Point p) {
    	points.add(p);
    	fireTableRowsInserted(points.size() - 1, points.size() - 1);
    }

}
