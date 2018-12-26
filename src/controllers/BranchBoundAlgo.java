/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import views.TestCaseGeneration02Page;

/**
 *
 * @author umar
 */
public class BranchBoundAlgo {
    
    public static int totalVertices = UMLController.dataListStates.size() + 2;
    public static int matrix[][] = new int[totalVertices][totalVertices];
    public static int posINF = 99999; //Integer.MAX_VALUE;
    public static int negINF = -99999; //Integer.MIN_VALUE;
    
    private static void initMatrix() {
        
        totalVertices = TestCaseGeneration02Page.totalVertices;
        matrix = TestCaseGeneration02Page.matrix;
        posINF = TestCaseGeneration02Page.posINF;
        negINF = TestCaseGeneration02Page.negINF;
        
        for (int i = 0; i < totalVertices; i++) {
            matrix[i][i] = posINF;
        }
    }
    
    public static void calcBBA01() {
        
        initMatrix();
        
        try {
            
            int matrixTemp[][] = new int[totalVertices][totalVertices];
            
            TestCaseGeneration02Page.viewMatrix();
            
            // reduce row
            int totalRowMin = 0;
            for (int i = 0; i < totalVertices; i++) {
                int min = Func.getMinimumInteger(matrix[i]);
                if (min != 0 && min != posINF) {
                    for (int j = 0; j < totalVertices; j++) {
                        if (matrix[i][j] != posINF) {
                            matrixTemp[i][j] = matrix[i][j] - min;
                        } else {
                            matrixTemp[i][j] = matrix[i][j];
                        }
                    }
                } else if (min == posINF) {
                    for (int j = 0; j < totalVertices; j++) {
                        matrixTemp[i][j] = matrix[i][j];
                    }
                }
                if (min == posINF) {
                    min = 0;
                }
                totalRowMin += min;
            }
            
            TestCaseGeneration02Page.viewMatrix(matrixTemp);
            
            // reduce column
            int totalColumnMin = 0;
            for (int j = 0; j < totalVertices; j++) {
                int cols[] = new int[totalVertices];
                for (int i = 0; i < totalVertices; i++) {
                    cols[i] = matrixTemp[i][j];
                }
                int min = Func.getMinimumInteger(cols);
                if (min != 0 && min != posINF) {
                    for (int i = 0; i < totalVertices; i++) {
                        if (matrixTemp[i][j] != posINF) {
                            matrixTemp[i][j] -= min;
                        }
                    }
                }
                if (min == posINF) {
                    min = 0;
                }
                totalColumnMin += min;
            }
            
            TestCaseGeneration02Page.viewMatrix(matrixTemp);
            
            // total reduced
            int totalReduced = totalRowMin + totalColumnMin;
            
            ArrayList<Integer> storage = new ArrayList<Integer>();
            storage.add(0);
            
            BranchBoundAlgo bba = new BranchBoundAlgo();
            bba.getCost(matrixTemp, totalReduced, storage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getCost(int box[][], int reducedCost, ArrayList<Integer> storage) {
        
        int matrixTemp[][] = new int[totalVertices][totalVertices];
        int theBestCost = posINF;
        int theBestNode = storage.get(storage.size()-1);
        for (int i = 0; i < totalVertices; i++) {
            
            if (storage.contains(i)) {
                continue;
            }
            
            // copy matrix into the new one.
            for (int j = 0; j < totalVertices; j++) {
                for (int k = 0; k < totalVertices; k++) {
                    matrixTemp[j][k] = box[j][k];
                }
            }
            
            // make all cells of row of last storage index into INF.
            int lastNode = storage.get(storage.size()-1);
            for (int j = 0; j < totalVertices; j++) {
                matrixTemp[lastNode][j] = posINF;
            }
            
            // make all cells of column of i index into INF.
            for (int j = 0; j < totalVertices; j++) {
                matrixTemp[j][i] = posINF;
            }
            
            // reduced row
            int totalRowMin = 0;
            for (int j = 0; j < totalVertices; j++) {
                int min = Func.getMinimumInteger(matrixTemp[j]);
                if (min != 0 && min != posINF) {
                    for (int k = 0; k < totalVertices; k++) {
                        if (matrixTemp[j][k] != posINF) {
                            matrixTemp[j][k] -= min;
                        }
                    }
                }
                if (min == posINF) {
                    min = 0;
                }
                totalRowMin += min;
            }
            
            // reduced column
            int totalColumnMin = 0;
            for (int k = 0; k < totalVertices; k++) {
                int cols[] = new int[totalVertices];
                for (int j = 0; j < totalVertices; j++) {
                    cols[j] = matrixTemp[j][k];
                }
                int min = Func.getMinimumInteger(cols);
                if (min != 0 && min != posINF) {
                    for (int j = 0; j < totalVertices; j++) {
                        if (matrixTemp[j][k] != posINF) {
                            matrixTemp[j][k] -= min;
                        }
                    }
                }
                if (min == posINF) {
                    min = 0;
                }
                totalColumnMin += min;
            }
            
            // total reduced
            int totalReduced = reducedCost + (totalRowMin + totalColumnMin) + box[lastNode][i];
            totalReduced = (totalReduced > posINF) ? (posINF) : (totalReduced);
            
            if (totalReduced < theBestCost) {
                theBestCost = totalReduced;
                theBestNode = i;
            }
        }
        
        storage.add(theBestNode);
        BranchBoundAlgo bba = new BranchBoundAlgo();
        theBestCost = bba.getCost(matrixTemp, theBestCost, storage);
        
        return theBestCost;
    }
}
