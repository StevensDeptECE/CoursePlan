import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.table.*;

public class LatLonTable extends AbstractTableModel{
    private static String columnNames[] = {"Lat", "Lon"};
        
    public static ArrayList<Point> points = new ArrayList<Point>();
    public static double distanceSum;
    
    private JComponent parentPanel = null;
    
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
    
    public void setParentPanle(JComponent parentPanel) {
        this.parentPanel = parentPanel;
    }
    
    public void add(Point p) {
        points.add(p);
        fireTableRowsInserted(points.size() - 1, points.size() - 1);
    }
    
    public void remove(int index) {
        points.remove(index);
        fireTableRowsDeleted(index, index);
    }
    
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return points.get(row).getLatString("table");
        } else {
            return points.get(row).getLonString("table");
        }
    }
    
    public void setValueAt(Object value, int row, int col) {
        double origin = getInputData((String) getValueAt(row, col), col);
        
        double data = getInputData((String) value, col);
        if (data == 999) {
            data = origin;
        }
        
        Point p = points.get(row);
        
        if (col == 0) {
            p.updateLat(data);
            
        } else {
            p.updateLon(data);
        }
        
        fireTableCellUpdated(row, col);
        
        updateDistance();
        MainWin.updateMainWin();
    }
    
    public double getInputData(String s, int col) {
        // two legal formats: 23.123456°N / -23.123456
        try {
            double data = 0.0;
            
            String posSign;
            String negSign;
            
            if (col == 0) {
                posSign = "°N";
                negSign = "°S";
                
            } else {
                posSign = "°E";
                negSign = "°W";
            }
            
            int indexOfPostfix = s.lastIndexOf(posSign);
            int flag = 1;
            
            if (indexOfPostfix == -1) {
                indexOfPostfix = s.lastIndexOf(negSign);
                flag = -1;
            }
            
            if (indexOfPostfix != -1) {
                data = Double.parseDouble(s.substring(0, indexOfPostfix));
                data *= flag;
                
            } else {
                data = Double.parseDouble(s);
            }
            
            return data;
            
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(parentPanel, MainWin.texts.getString("pNumberInfo"), MainWin.texts.getString("pNumber"), JOptionPane.WARNING_MESSAGE);
            return 999;
        }
    }
        
    public static void updateDistance() {
        distanceSum = 0.0;
        
        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            
            distanceSum += prev.getDist(curr);
        }       
    }
    
    public String getDistanceSum() {
        return String.format("%.4f", distanceSum);
    }
    
    public boolean writeToFile(String fileName) {
        try {           
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            
            for (int i = 0; i < points.size(); i++) {
                bw.write(points.get(i).getLatString("write") + "\t" + points.get(i).getLonString("write") + "\n");
            }
            
            bw.flush();
            bw.close();
            
            return true;
            
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void readFromFile(String fileName) {
        try {
            ArrayList<Point> loadedPoints = new ArrayList<Point>();
            
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] latlon = line.trim().split("\\s+");
                loadedPoints.add(new Point(Double.parseDouble(latlon[0]), Double.parseDouble(latlon[1]), "degree"));
            }
            
            br.close();
            
            loadPointsToTable(loadedPoints);
            
            return;
            
        } catch(FileNotFoundException e) {
            JOptionPane.showMessageDialog(parentPanel, MainWin.texts.getString("pFile"), MainWin.texts.getString("pError"), JOptionPane.WARNING_MESSAGE);
            return;
            
        } catch(IOException e) {
            JOptionPane.showMessageDialog(parentPanel, MainWin.texts.getString("pErrorInfo"), MainWin.texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
            return;
            
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(parentPanel, MainWin.texts.getString("pRead"), MainWin.texts.getString("pError"), JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
    
    private void loadPointsToTable(ArrayList<Point> loadedPoints) {
        points.clear();
        
        for (Point p : loadedPoints) {
            points.add(p);
        }
    }
    
}
