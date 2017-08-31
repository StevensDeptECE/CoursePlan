import java.util.*;
import java.io.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.table.*;

public class LatLonTable extends AbstractTableModel{
    JComponent parentPanel = null;
    
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
            return points.get(row).getLatString();
        } else {
            return points.get(row).getLonString();
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
        try {
            int valInt = Integer.parseInt((String) value);
            
            Point p = points.get(row);
            
            if (col == 0) {
                p.updateLat(valInt);
            } else {
                p.updateLon(valInt);
            }
            
            fireTableCellUpdated(row, col);
            
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(parentPanel, "Opps...\nPlease input an INTEGER!", "Retry", JOptionPane.WARNING_MESSAGE);
        }
        
                
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
    
    public String saveToFile(int printTimes) {
        try {
            String fileName = "LatLonInfo_" + DateTimeFormatter.ofPattern("MMddyyyy").format(LocalDate.now()) + "_" + printTimes + ".txt";
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            
            // print file head
            bw.write("The " + printTimes + "th Print:\n\n");            
            bw.write("Positions:\tLat:\tLon:\t\n");
            bw.write("------------------------------\n");
            
            // print table data
            for (int i = 0; i < points.size(); i++) {
                bw.write("Position " + (i + 1) + ":\t" + points.get(i).getLatString() + "\t" + points.get(i).getLonString() + "\n");
            }
            
            // print file end
            bw.write("------------------------------\n");           
            bw.write(new Date() + "\n");
                        
            bw.flush();
            bw.close();
            
            return fileName;
            
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    
    }
    
}
