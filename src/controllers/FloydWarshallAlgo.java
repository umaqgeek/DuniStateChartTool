/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.swing.JOptionPane;
import views.TestCaseGeneration02Page;

/**
 *
 * @author umar
 */
public class FloydWarshallAlgo {
    
    public static boolean isValidMatrix(String str) {
        
        boolean status = false;
        
        try {
            
            str = str.trim().replaceAll("\\s", "");
            String row[] = str.split(";");
            if (row.length != TestCaseGeneration02Page.totalVertices) {
                JOptionPane.showMessageDialog(null, "Number of rows not equal to the total number of STATES/VERTICES!", "Invalid Rows", 0);
                return false;
            }
            for (int i = 0; i < row.length; i++) {
                String column[] = row[i].split(",");
                if (column.length != TestCaseGeneration02Page.totalVertices) {
                    JOptionPane.showMessageDialog(null, "Number of column not equal to the total number of STATES/VERTICES! for row #" + (i+1), "Invalid Columns", 0);
                    return false;
                }
            }
            
            initMatrix(str);
            status = true;
            
        } catch (Exception e) {
            status = false;
        }
        
        return status;
    }
    
    private static void initMatrix(String str) {
        try {

            str = str.trim().replaceAll("\\s", "");

            String row[] = str.split(";");
            for (int i = 0; i < TestCaseGeneration02Page.totalVertices; i++) {
                String col[] = row[i].split(",");
                for (int j = 0; j < TestCaseGeneration02Page.totalVertices; j++) {
                    try {
                        TestCaseGeneration02Page.matrix[i][j] = Integer.parseInt(col[j]);
                        if (i == j) {
                            TestCaseGeneration02Page.matrix[i][j] = 0;
                        }
                    } catch (Exception e) {
                        TestCaseGeneration02Page.matrix[i][j] = TestCaseGeneration02Page.posINF;
                    }
                }
            }

        } catch (Exception e) {
        }
    }
    
    public static void calcFWA01() {
        
        for (int k = 0; k < TestCaseGeneration02Page.totalVertices; k++) {
            for (int i = 0; i < TestCaseGeneration02Page.totalVertices; i++) {
                for (int j = 0; j < TestCaseGeneration02Page.totalVertices; j++) {
                    if (i != j) {
                        if (TestCaseGeneration02Page.matrix[i][j] > (TestCaseGeneration02Page.matrix[i][k] + TestCaseGeneration02Page.matrix[k][j])) {
                            TestCaseGeneration02Page.matrix[i][j] = (TestCaseGeneration02Page.matrix[i][k] + TestCaseGeneration02Page.matrix[k][j]);
                        }
                    }
                }
            }
        }
        
        TestCaseGeneration02Page.viewMatrix(1, true, "After Warshall Floyd Algo");
    }
}
