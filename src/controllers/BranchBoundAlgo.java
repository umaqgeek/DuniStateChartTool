/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import views.TestCaseGeneration02Page;

/**
 *
 * @author umar
 */
public class BranchBoundAlgo {
    
    public static int totalVertices = UMLController.dataListStates.size();
    public static int matrix[][] = new int[totalVertices][totalVertices];
    public static int posINF = 99999; //Integer.MAX_VALUE;
    public static int negINF = -99999; //Integer.MIN_VALUE;
    
    private static void initMatrix() {
        
        totalVertices = TestCaseGeneration02Page.totalVertices;
        posINF = TestCaseGeneration02Page.posINF;
        negINF = TestCaseGeneration02Page.negINF;
        
        for (int i = 0; i < TestCaseGeneration02Page.matrix.length; i++) {
            for (int j = 0; j < TestCaseGeneration02Page.matrix[i].length; j++) {
                if (i == j) {
                    matrix[i][j] = posINF;
                } else {
                    matrix[i][j] = TestCaseGeneration02Page.matrix[i][j];
                }
            }
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
            storage = bba.getCost(matrixTemp, oriTotalReduced, storage); //*** 0 = oriTotalReduced
            int newTotalReduced = bba.calcPathCost(TestCaseGeneration02Page.matrix, storage);
            
            TestCaseGeneration02Page.viewPath(2, true, "After Branch Bound Algo", "[The Best BBA]", storage, newTotalReduced);
            
            // generate number random path from node 1 to node n.
            bba.generateRandomPath(numberOfPath, TestCaseGeneration02Page.matrix);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void generateRandomPath(int numPath, int matrx[][]) {
        
        int startNode = 1;
        int endNode = totalVertices;
        
        ArrayList<ArrayList<Integer>> allPaths = new ArrayList<ArrayList<Integer>>();
        allPaths.removeAll(allPaths);
        
        BranchBoundAlgo bba = new BranchBoundAlgo();
        
        for (int index = 0; index < numPath; index++) {
            
            ArrayList<Integer> paths = new ArrayList<Integer>();
            
            do {
                paths.removeAll(paths);

                paths.add(startNode);
                paths = bba.searchDeep(matrx, paths, endNode);
                
                boolean isMatch = bba.matchPaths(allPaths, paths);
                if (!isMatch) {
                    break;
                }
            } while (true);
            
            allPaths.add(paths);
        }
        
        Collections.sort(allPaths, new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> one, ArrayList<Integer> two) {
                return (one.size() - two.size());
            }
        });
        
        for (int index = 0; index < allPaths.size(); index++) {
            int totalCost = bba.calcPathCost(TestCaseGeneration02Page.matrix, allPaths.get(index));
            TestCaseGeneration02Page.viewPathMany(2, false, "#"+Func.getFormatInteger((index+1)+"", (numPath+"").length()), allPaths.get(index), totalCost);
        }
    }
    
    public boolean matchPaths(ArrayList<ArrayList<Integer>> allP, ArrayList<Integer> p) {
        boolean status = false;
        try {
            
            for (int i = 0; i < allP.size(); i++) {
                int numTrue = 0;
                for (int j = 0; j < allP.get(i).size() && j < p.size(); j++) {
                    if (allP.get(i).get(j) == p.get(j)) {
                        numTrue += 1;
                    }
                }
                if (numTrue == p.size()) {
                    status = true;
                    break;
                }
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return status;
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
    
    public int getColumnOfRow(int matrx[]) {
        int found = 0;
//        for (int i = 0; i < 10; i++) {
//            if (matrx[i] )
//        }
        return found;
    }
    
    public ArrayList<Integer> searchDeep(int matrx[][], ArrayList<Integer> pathNodes, int endNode) {
        
        Random rand = new Random();
        int randomNode;
        int countStop = 100;
        do {
            randomNode = rand.nextInt(totalVertices) + 1;
//            BranchBoundAlgo bba = new BranchBoundAlgo();
//            randomNode = bba.getColumnOfRow(matrix[pathNodes.get(pathNodes.size()-1)-1]);
            
            if (pathNodes.contains(randomNode)) {
                continue;
            } else if (countStop <= 0) {
                break;
            } else if (matrix[pathNodes.get(pathNodes.size()-1)-1][randomNode-1] == posINF) {
                countStop--;
                continue;
            } else if (randomNode == endNode) {
                pathNodes.add(randomNode);
                break;
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
    
    public ArrayList<Integer> getCost(int box[][], int reducedCost, ArrayList<Integer> storage) {
        
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
        
        // to stop the recursive loop if looping on the same node.
        if (storage.size() > 1) {
            if (storage.get(storage.size()-1) == storage.get(storage.size()-2)) {
                theBestCost = reducedCost;
                storage.remove(storage.size()-1);
                return storage;
            }
        }
        
        // to stop the recursive loop if already go through all nodes.
        if (storage.size() == totalVertices) {
            return storage;
        }
        
        BranchBoundAlgo bba = new BranchBoundAlgo();
        storage = bba.getCost(theBestMatrix, theBestCost, storage);
        
        return storage;
    }
}
