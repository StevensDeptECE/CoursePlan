import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

public class CoordinateList extends AbstractTableModel {
	static class LL {
		double lat,lon;
		public LL(double lat, double lon) { this.lat = lat; this.lon = lon; }
	}
	
	private ArrayList<LL> coordinates;
	public CoordinateList() { coordinates = new ArrayList<LL>(); }
	private static String columnNames[] = {"lat", "lon"};
	    public String getColumnName(int col) {
	        return columnNames[col];
	    }
	    public int getRowCount() { return coordinates.size(); }
	    public int getColumnCount() { return columnNames.length; }
	    public Object getValueAt(int row, int col) {
	        if (col == 0)
	        	return coordinates.get(row).lat; // Double
	        else
	        	return coordinates.get(row).lon;
	    }
	    public boolean isCellEditable(int row, int col)
	        { return true; }
	    public void setValueAt(Object value, int row, int col) {
	    	//coordinates.get(row)
	        //fireTableCellUpdated(row, col);
	    }
	    public void add(LL L) {
	    	coordinates.add(L);
	    	fireTableRowsInserted(coordinates.size()-1, coordinates.size()-1);
	    }

}
