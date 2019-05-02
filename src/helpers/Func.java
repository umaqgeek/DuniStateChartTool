/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Desktop;
import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.ArrayUtils;
import views.MainPage;

/**
 *
 * @author umarmukhtar
 */
public class Func {
    
    private static String FILE_NAME = "exportStateChartTool.xlsx";
    public static boolean DEBUG = true;
    public static Random rand = new Random();
    
    public static final String TOTAL_NUMBER_PATH = "total_number_path";
    public static final String TOTAL_TIME_EXECUTION = "total_time_execution";
    public static final String TOTAL_ALL_TRANSITIONS = "total_all_transitions";
    public static final String TOTAL_TRANSITIONS_PATH = "total_transitions_path";
    public static final String TOTAL_TRANS_COVERAGE = "total_trans_coverage";
    public static final String TOTAL_ALL_PAIRS = "total_all_pairs";
    public static final String TOTAL_PAIRS_PATH = "total_pairs_path";
    public static final String TOTAL_TRANS_PAIR_COVERAGE = "total_trans_pair_coverage";
    public static final String ARR_PATHS = "arr_paths";
    
    public static final String KEY_F = "key_f";
    
    public static DecimalFormat df = new DecimalFormat("#,###.00");
    
    // Function to remove duplicates from an ArrayList 
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        // Create a new ArrayList 
        ArrayList<T> newList = new ArrayList<T>();
        // Traverse through the first list 
        for (T element : list) {
            // If this element is not present in newList 
            // then add it 
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        // return the new list 
        return newList;
    }
    
    public static String getFormatInteger(String number, int numDigit) {
        String outNumber = "";
        try {
            int balanceDigit = (numDigit <= number.length()) ? (0) : (numDigit - number.length());
            for (int i = 0; i < balanceDigit; i++) {
                outNumber += "0";
            }
            outNumber += number;
        } catch (Exception e) {
            outNumber = number;
        }
        return outNumber;
    }
    
    public static void setTable(String[] columns, String[][] data) {
        DefaultTableModel model = new DefaultTableModel();
        for (int index = 0; index < columns.length; index++) {
            model.addColumn(columns[index]);
        }
        for (int index = 0; index < data.length; index++) {
            model.addRow(data[index]);
        }
        MainPage.tblList.setModel(model);
    }
    
    public static void setPrint() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(MainPage.lblTitle.getText());
        
        Object[][] dataPrint = new Object[MainPage.tblList.getRowCount()+1][MainPage.tblList.getColumnCount()];
        for (int index = 0; index < MainPage.tblList.getColumnCount(); index++) {
            dataPrint[0][index] = MainPage.tblList.getColumnName(index);
        }
        for (int index = 1; index < MainPage.tblList.getRowCount()+1; index++) {
            for (int jndex = 0; jndex < MainPage.tblList.getColumnCount(); jndex++) {
                dataPrint[index][jndex] = MainPage.tblList.getValueAt(index-1, jndex);
            }
        }

        int rowNum = 0;
        System.out.println("Creating excel");

        for (Object[] datatype : dataPrint) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found!", "File Not Found", 0);
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IO Error exception!", "IO Error Exception", 0);
            e.printStackTrace();
        }

//        System.out.println("Done");
//        JOptionPane.showMessageDialog(null, "Excel has been exported.", "Success Export", 1);
        
        try {
            File file = new File(FILE_NAME);
            if (!Desktop.isDesktopSupported()) {
                System.out.println("Desktop is not supported");
                JOptionPane.showMessageDialog(null, "Desktop is not supported!", "Error Open File", 0);
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                desktop.open(file);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "File cannot be opened!", "Error Open File", 0);
            e.printStackTrace();
        }
    }
    
    public static int getMinimumInteger(int arr[]) {
        List b = Arrays.asList(ArrayUtils.toObject(arr));
        return (int) Collections.min(b);
    }
    
    public static void debugArrayList(ArrayList<ArrayList<Object>> arrObj) {
        System.out.println("\nDEBUG ARRAYLIST:");
        for (int i = 0; i < arrObj.size(); i++) {
            System.out.println("#"+i+": "+arrObj.get(i));
        }
        System.out.println("\n");
    }
}
