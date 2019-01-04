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
import javax.swing.JOptionPane;
import views.TestCaseGeneration02Page;

/**
 *
 * @author umar
 */
public class PureRandomAlgo {
    
    public static int totalVertices = UMLController.dataListStates.size();
    public static int matrix[][] = new int[totalVertices][totalVertices];
    public static int posINF = 99999; //Integer.MAX_VALUE;
    public static int negINF = -99999; //Integer.MIN_VALUE;
    
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

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public ArrayList<Integer> searchDeep(int matrx[][], ArrayList<Integer> pathNodes, int endNode) {
        
        Random rand = new Random();
        int randomNode;
        int countStop = 100;
        do {
            randomNode = rand.nextInt(totalVertices) + 1;
            
            if (pathNodes.contains(randomNode)) {
                continue;
            } else if (countStop <= 0) {
                break;
//***            } else if (matrix[pathNodes.get(pathNodes.size()-1)-1][randomNode-1] == posINF) {
//                countStop--;
//                continue;
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
            PureRandomAlgo pra = new PureRandomAlgo();
            pathNodes = pra.searchDeep(matrx, pathNodes, endNode);
        }
        return pathNodes;
    }
    
    public void generateRandomPath(int numPath, int matrx[][]) {
        
        int startNode = 1;
        int endNode = totalVertices;
        
        ArrayList<ArrayList<Integer>> allPaths = new ArrayList<ArrayList<Integer>>();
        allPaths.removeAll(allPaths);
        
        PureRandomAlgo pra = new PureRandomAlgo();
        
        for (int index = 0; index < numPath; index++) {
            
            ArrayList<Integer> paths = new ArrayList<Integer>();
            
            do {
                paths.removeAll(paths);
                
                paths.add(startNode);
                paths = pra.searchDeep(matrx, paths, endNode);
                
                boolean isMatch = pra.matchPaths(allPaths, paths);
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
        
        TestCaseGeneration02Page.viewTitle(3, true, "Generate Random Path Before Floyd Warshall");
        
        for (int index = 0; index < allPaths.size(); index++) {
            int totalCost = pra.calcPathCost(matrx, allPaths.get(index));
            TestCaseGeneration02Page.viewPathMany(3, false, "#"+Func.getFormatInteger((index+1)+"", (numPath+"").length()), allPaths.get(index), totalCost);
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
    
    public static void calcPRA01(int numberOfPath) {
        
        PureRandomAlgo pra = new PureRandomAlgo();
        pra.generateRandomPath(numberOfPath, matrix);
    }
}
