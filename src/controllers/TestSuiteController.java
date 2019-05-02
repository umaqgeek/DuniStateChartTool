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
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Random;
import javax.swing.JOptionPane;
import views.TestCaseGeneration02Page;
import views.TestSuite01;

/**
 *
 * @author umar
 */
public class TestSuiteController {
    
    public static int totalVertices = UMLController.dataListStates.size();
    public static int totalMaximumPath = 1;
    public static int posINF = 99999; //Integer.MAX_VALUE;
    public static int negINF = -99999; //Integer.MIN_VALUE;
    public static int matrix[][] = new int[totalVertices][totalVertices];
    
    public static String prevMatrix;
    public static int totalTransitions = UMLController.dataListTransitions.size();
    public static int totalPairs = 1;
    
    public static ArrayList<Properties> parents = new ArrayList<Properties>();
    public static ArrayList<ArrayList<Object>> simpleParents = new ArrayList<ArrayList<Object>>();
    public static ArrayList<ArrayList<Object>> simpleParents2 = new ArrayList<ArrayList<Object>>();
    public static ArrayList<ArrayList<Object>> simpleParents3 = new ArrayList<ArrayList<Object>>();
    public static ArrayList<ArrayList<Object>> simpleOffsprings = new ArrayList<ArrayList<Object>>();
    public static ArrayList<ArrayList<Object>> simpleOffsprings2 = new ArrayList<ArrayList<Object>>();
    public static ArrayList<ArrayList<Integer>> possibleMutationPaths1 = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> possibleMutationPaths2 = new ArrayList<ArrayList<Integer>>();
    
    public static void clearSimpleParents() {
        TestSuiteController.simpleParents.clear();
        TestSuiteController.simpleParents2.clear();
        TestSuiteController.simpleParents3.clear();
        TestSuiteController.simpleOffsprings.clear();
        TestSuiteController.simpleOffsprings2.clear();
        TestSuiteController.possibleMutationPaths1.clear();
        TestSuiteController.possibleMutationPaths2.clear();
        
        TestSuiteController.simpleParents.removeAll(TestSuiteController.simpleParents);
        TestSuiteController.simpleParents2.removeAll(TestSuiteController.simpleParents2);
        TestSuiteController.simpleParents3.removeAll(TestSuiteController.simpleParents3);
        TestSuiteController.simpleOffsprings.removeAll(TestSuiteController.simpleOffsprings);
        TestSuiteController.simpleOffsprings2.removeAll(TestSuiteController.simpleOffsprings2);
        TestSuiteController.possibleMutationPaths1.removeAll(TestSuiteController.possibleMutationPaths1);
        TestSuiteController.possibleMutationPaths2.removeAll(TestSuiteController.possibleMutationPaths2);
    }
    
    public static void clearParents() {
        TestSuiteController.parents.clear();
        clearSimpleParents();
    }
    
    public static void setParents(Properties parent) {
        TestSuiteController.parents.add(parent);
    }
    
    public TestSuiteController(String matrix) {
        prevMatrix = matrix;
    }
    
    public static int getTotalMaximumPath() {
        long maxTemp = 1;
        int maxPath = 1;
        if (totalVertices > 1) {
            for (int i = 2; i <= totalVertices-2; i++) {
                maxTemp = maxTemp * i;
                if (maxTemp >= Integer.MAX_VALUE) {
                    maxPath = Integer.MAX_VALUE;
                    break;
                } else {
                    maxPath = (int) maxTemp;
                }
            }
        }
        return maxPath;
    }
    
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
            if (Func.DEBUG) {
                e.printStackTrace();
            }
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
            
            totalPairs = 0;
            for (int i = 0; i < matrix.length; i++) {
                int pairs = 0;
                for (int j = 0; j < matrix[i].length; j++) {
                    if (matrix[i][j] != posINF && matrix[i][j] != negINF && i != j) {
                        pairs += 1;
                    }
                }
                if (pairs >= 2) {
                    totalPairs += pairs;
                }
            }

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public Properties generateRandomPath(int numPath, int matrx[][]) {
        
        int startNode = 1;
        int endNode = totalVertices;
        
        ArrayList<ArrayList<Integer>> allPaths = new ArrayList<ArrayList<Integer>>();
        allPaths.removeAll(allPaths);
        
        TestSuiteController ts = new TestSuiteController(prevMatrix);
        
        for (int index = 0; index < numPath; index++) {
            
            ArrayList<Integer> paths = new ArrayList<Integer>();
            
            boolean isMatch = false; 
            int limit = 100;
            do {
                paths.removeAll(paths);
                
                paths.add(startNode);
                paths = ts.searchDeep(matrx, paths, endNode);
                
                isMatch = ts.matchPaths(allPaths, paths);
                if (!isMatch) {
                    break;
                }
            } while (true && limit-- > 0);

            if (!isMatch) {
                if (TestSuite01.cbWithoutFinal.isSelected()) {
                    allPaths.add(paths);
                } else {
                    if (paths.get(paths.size()-1) == endNode) {
                        allPaths.add(paths);
                    }
                }
            }
        }
        
        Collections.sort(allPaths, new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> one, ArrayList<Integer> two) {
                return (one.size() - two.size());
            }
        });
        
        ArrayList<String> trans = new ArrayList<String>();
        
        for (int index = 0; index < allPaths.size(); index++) {
            
            int totalCost = ts.calcPathCost(matrx, allPaths.get(index));
            
            TestSuite01.viewPathMany(false, 
                    "#"+Func.getFormatInteger((index+1)+"", (numPath+"").length()), 
                    allPaths.get(index), 
                    totalCost);
            
            // collect all transitions in each path.
            for (int i = 0; i < allPaths.get(index).size()-1; i++) {
                String temp = allPaths.get(index).get(i)+","+allPaths.get(index).get(i+1);
                trans.add(temp);
            }
        }
        
        // remove redundant transition.
        ArrayList<String> transA2 = new ArrayList<String>(new LinkedHashSet<String>(trans));
        
        int pairsInPath = 0;
        for (int i = startNode; i <= endNode; i++) {
            int countTemp = 0;
            for (int j = startNode; j <= endNode; j++) {
                if (i != j) {
                    String temp = i + "," + j;
                    if (transA2.contains(temp)) {
                        countTemp += 1;
                    }
                }
            }
            if (countTemp >= 2) {
                pairsInPath += countTemp;
            }
        }
        
        Properties props = new Properties();
        props.setProperty(Func.TOTAL_NUMBER_PATH, ""+allPaths.size());
        props.setProperty(Func.TOTAL_ALL_TRANSITIONS, ""+totalTransitions);
        props.setProperty(Func.TOTAL_TRANSITIONS_PATH, ""+transA2.size());
        props.setProperty(Func.TOTAL_ALL_PAIRS, ""+totalPairs);
        props.setProperty(Func.TOTAL_PAIRS_PATH, ""+pairsInPath);
        props.put(Func.ARR_PATHS, allPaths);
        
        return props;
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
    
    public ArrayList<Integer> searchDeep(int matrx[][], ArrayList<Integer> pathNodes, int endNode) {
        
        Random rand = new Random();
        int randomNode;
        int countStop = 100;
        do {
            randomNode = rand.nextInt(totalVertices) + 1;
            
            if (pathNodes.contains(randomNode) && Collections.frequency(pathNodes, randomNode) > 1) {
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
            TestSuiteController ts = new TestSuiteController(prevMatrix);
            pathNodes = ts.searchDeep(matrx, pathNodes, endNode);
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
    
    public static Properties calcPRA01(int numberOfPath) {
        
        TestSuiteController ts = new TestSuiteController(prevMatrix);
        Properties props = ts.generateRandomPath(numberOfPath, matrix);
        
//        ArrayList<Integer> storage = new ArrayList<Integer>();
//        storage.add(1);
//        storage = ts.getCost(matrix, negINF, storage);
//        System.out.println(storage);
        
        return props;
    }
}
