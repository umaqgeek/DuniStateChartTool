/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import java.util.Random;
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
    
    public static void calcBBA01(int numberOfPath) {
        
        initMatrix();
        
        try {
            
            int matrixTemp[][] = new int[totalVertices][totalVertices];
            
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
            
            // total reduced
            int oriTotalReduced = totalRowMin + totalColumnMin;
            
            ArrayList<Integer> storage = new ArrayList<Integer>();
            storage.add(1);
            
            BranchBoundAlgo bba = new BranchBoundAlgo();
            int newTotalReduced = bba.getCost(matrixTemp, oriTotalReduced, storage);
            
            TestCaseGeneration02Page.viewPath(false, "[The Best BBA]", storage, newTotalReduced);
            
            TestCaseGeneration02Page.viewMatrix(false, matrixTemp);
            
            // generate number random path from node 1 to node n.
            bba.generateRandomPath(numberOfPath, matrixTemp, newTotalReduced);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generateRandomPath(int numPath, int matrx[][], int startReduced) {
        
        int startNode = 1;
        int endNode = totalVertices;
        
        for (int index = 0; index < numPath; index++) {
            
            ArrayList<Integer> paths = new ArrayList<Integer>();
            paths.add(startNode);
            
            BranchBoundAlgo bba = new BranchBoundAlgo();
            paths = bba.searchDeep(matrx, paths, endNode);
            
            int totalCost = bba.calcPathCost(matrx, paths);
            
            TestCaseGeneration02Page.viewPathMany(false, "#"+Func.getFormatInteger((index+1)+"", (numPath+"").length()), paths, totalCost);
        }
    }
    
    public int calcPathCost(int matrx[][], ArrayList<Integer> pathNodes) {
        int totalCost = 0;
        try {
            for (int index = 0; index < pathNodes.size() - 1; index++) {
                int currNode = pathNodes.get(index);
                int nextNode = pathNodes.get(index + 1);
                totalCost += (matrx[currNode-1][nextNode-1]);
            }
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return totalCost;
    }
    
    public ArrayList<Integer> searchDeep(int matrx[][], ArrayList<Integer> pathNodes, int endNode) {
        
        Random rand = new Random();
        int randomNode;
        int countStop = 100;
        do {
            randomNode = rand.nextInt(totalVertices) + 1;
            if (pathNodes.contains(randomNode)) {
                continue;
            } else if (randomNode == endNode) {
                pathNodes.add(randomNode);
                break;
            } else if (countStop <= 0) {
                break;
            } else if (matrix[pathNodes.get(pathNodes.size()-1)-1][randomNode-1] == posINF) {
                countStop--;
                continue;
            } else {
                pathNodes.add(randomNode);
                break;
            }
        } while (true);
        if (countStop <= 0 || randomNode == endNode) {
            return pathNodes;
        } else {
            BranchBoundAlgo bba = new BranchBoundAlgo();
            pathNodes = bba.searchDeep(matrx, pathNodes, endNode);
        }
        return pathNodes;
    }
    
    public int getCost(int box[][], int reducedCost, ArrayList<Integer> storage) {
        
        int matrixTemp[][] = new int[totalVertices][totalVertices];
        int theBestMatrix[][] = new int[totalVertices][totalVertices];
        int theBestCost = posINF;
        int theBestNode = storage.get(storage.size()-1)-1;
        for (int i = 0; i < totalVertices; i++) {
            
            // ignore the node if already gone through it.
            if (storage.contains(i+1)) {
                continue;
            }
            
            // copy matrix into the new one.
            for (int j = 0; j < totalVertices; j++) {
                for (int k = 0; k < totalVertices; k++) {
                    matrixTemp[j][k] = box[j][k];
                }
            }
            
            // make all cells of row of last storage index into INF.
            int lastNode = storage.get(storage.size()-1)-1;
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
                for (int j = 0; j < totalVertices; j++) {
                    for (int k = 0; k < totalVertices; k++) {
                        theBestMatrix[j][k] = matrixTemp[j][k];
                    }
                }
            }
        }
        
        storage.add(theBestNode+1);
        
        // to stop the recursive loop if already go through all nodes.
        if (storage.size() == totalVertices) {
            return theBestCost;
        }
        
        // to stop the recursive loop if looping on the same node.
        if (storage.size() > 1) {
            if (storage.get(storage.size()-1) == storage.get(storage.size()-2)) {
                theBestCost = reducedCost;
                storage.remove(storage.size()-1);
                return theBestCost;
            }
        }
        
        TestCaseGeneration02Page.viewMatrix(true, matrixTemp);
        
        BranchBoundAlgo bba = new BranchBoundAlgo();
        theBestCost = bba.getCost(theBestMatrix, theBestCost, storage);
        
        return theBestCost;
    }
}
