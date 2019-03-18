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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 *
 * @author umar
 */
public class NSGA2Algo {
    
    public static Properties Fprops = new Properties();
    public static Float fmx[] = new Float[]{0.00f, Float.MAX_VALUE};
    public static Float fmy[] = new Float[]{0.00f, Float.MAX_VALUE};
    public static Float fmz1[] = new Float[]{0.00f, Float.MAX_VALUE};
    public static Float fmz2[] = new Float[]{0.00f, Float.MAX_VALUE};
    
    public static void setMaxMinM(ArrayList<Object> sp) {
        fmx[0] = Float.parseFloat((String) sp.get(0)) > fmx[0] ? Float.parseFloat((String) sp.get(0)) : fmx[0];
        fmx[1] = Float.parseFloat((String) sp.get(0)) < fmx[1] ? Float.parseFloat((String) sp.get(0)) : fmx[1];
        fmy[0] = Float.parseFloat((String) sp.get(1)) > fmy[0] ? Float.parseFloat((String) sp.get(1)) : fmy[0];
        fmy[1] = Float.parseFloat((String) sp.get(1)) < fmy[1] ? Float.parseFloat((String) sp.get(1)) : fmy[1];
        fmz1[0] = Float.parseFloat((String) sp.get(2)) > fmz1[0] ? Float.parseFloat((String) sp.get(2)) : fmz1[0];
        fmz1[1] = Float.parseFloat((String) sp.get(2)) < fmz1[1] ? Float.parseFloat((String) sp.get(2)) : fmz1[1];
        fmz2[0] = Float.parseFloat((String) sp.get(3)) > fmz2[0] ? Float.parseFloat((String) sp.get(3)) : fmz2[0];
        fmz2[1] = Float.parseFloat((String) sp.get(3)) < fmz2[1] ? Float.parseFloat((String) sp.get(3)) : fmz2[1];
    }
    
    public static void viewAllMaxMinM() {
        System.out.print("fmx: ");
        for (int i = 0; i < fmx.length; i++) {
            System.out.print(fmx[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmy: ");
        for (int i = 0; i < fmy.length; i++) {
            System.out.print(fmy[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmz1: ");
        for (int i = 0; i < fmz1.length; i++) {
            System.out.print(fmz1[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmz2: ");
        for (int i = 0; i < fmz2.length; i++) {
            System.out.print(fmz2[i] + ", ");
        }
        System.out.println("");
    }
    
    public static boolean isNominated(ArrayList<Object> p, ArrayList<Object> q) {
        float px = Float.parseFloat((String) p.get(0));
        float py = Float.parseFloat((String) p.get(1));
        float pz1 = Float.parseFloat((String) p.get(2));
        float pz2 = Float.parseFloat((String) p.get(3));

        float qx = Float.parseFloat((String) q.get(0));
        float qy = Float.parseFloat((String) q.get(1));
        float qz1 = Float.parseFloat((String) q.get(2));
        float qz2 = Float.parseFloat((String) q.get(3));
        
        return ((px<=qx && py<=qy && pz1>=qz1 && pz2>=qz2) && (px<qx || py<qy || pz1>qz1 || pz2>qz2));
    }
    
    public static void setRanks() {
        
        ArrayList<ArrayList<Object>> arrAll = TestSuiteController.simpleParents;
        
        for (int i = 0; i < arrAll.size(); i++) {
            
            ArrayList<Object> p = arrAll.get(i);
            ArrayList<Integer> Sp = new ArrayList<Integer>();
            int np = 0;
            
            for (int j = 0; j < arrAll.size(); j++) {
                if (i != j) {
                    try {
                        
                        ArrayList<Object> q = arrAll.get(j);
                        boolean isNominate = NSGA2Algo.isNominated(p, q);
                        boolean isNominatedBy = NSGA2Algo.isNominated(q, p);
                        
                        if (isNominate) {
                            Sp.add(j+1);
                        } else if (isNominatedBy) {
                            np += 1;
                        }
                        
                    } catch (Exception e) {
                        if (Func.DEBUG) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            TestSuiteController.simpleParents.get(i).add(Sp);
            TestSuiteController.simpleParents.get(i).add(np);
            
            if (np == 0) {
                TestSuiteController.simpleParents.get(i).set(5, 1);
            }
        }
        
        // for every F1
        ArrayList<ArrayList<Object>> F1 = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {   
            ArrayList<Object> p = TestSuiteController.simpleParents.get(i);
            if (p.get(5).equals(1)) {
                F1.add(p);
            }
        }
        
        int currentRank = 1;
        
        Fprops.put(Func.KEY_F + currentRank, F1);
        
        int nextRank = currentRank + 1;
        setRank(F1, nextRank);
    }
    
    private static void setRank(ArrayList<ArrayList<Object>> Fn, int nextRank) {
        ArrayList<ArrayList<Object>> Fnext = new ArrayList<ArrayList<Object>>();
        // for every F1, modify the rest to F2
        for (int i = 0; i < Fn.size(); i++) {
            ArrayList<Object> p = Fn.get(i);
            try {
                ArrayList<Integer> qIndexes = (ArrayList<Integer>) p.get(6);
                for (int j = 0; j < qIndexes.size(); j++) {
                    int nomination = (int) TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).get(7);
                    nomination -= 1;
                    TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).set(7, nomination);
                    if (nomination <= 0) {
                        // if nomination if zero, set to rank n+1
                        TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).set(5, nextRank);
                        Fnext.add(TestSuiteController.simpleParents.get(qIndexes.get(j) - 1));
                    }
                }
            } catch (Exception e) {
                if (Func.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        
        Fprops.put(Func.KEY_F + nextRank, Fnext);
        nextRank += 1;
        
        if (!NSGA2Algo.isAllRanked()) {
            setRank(Fnext, nextRank);
        }
    }
    
    private static boolean isAllRanked() {
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            int rank = (int) TestSuiteController.simpleParents.get(i).get(5);
            if (rank == Integer.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }
    
    public static void setCrowds() {
        for (int i = 1; ; i++) {
            try {
                ArrayList<ArrayList<Object>> F = (ArrayList<ArrayList<Object>>) Fprops.get(Func.KEY_F + i);
                if (F == null) {
                    break;
                }
                // init set all distances = 0.
                for (int j = 0; j < F.size(); j++) {
                    F.get(j).add(0);
                }
                // rules of objective function, m.
                int m[][] = new int[][]{
                    {0, 1, -1},
                    {1, 1, -1},
                    {2, -1, 1},
                    {3, -1, 1}
                };
                // sort column 1.
                F = sortF(F, m[0]);
                
                /**
                 * TODO:
                 * 1. Separate into each cluster based on same column 1.
                 * 2. Sort column 2.
                 * 3. Combine all clusters in one list.
                 * 4. Separate into each cluster based on same column 1 and 2.
                 * 5. Sort column 3.
                 * 6. Combine all clusters in one list.
                 * 7. Separate into each cluster based on same column 1, 2, and 3.
                 * 8. Sort column 4.
                 * 9. Combine all clusters in one list.
                 */
                
                // TODO 1-3
                F = multipleSoftF(F, 1, m);
                // TODO 4-6
                F = multipleSoftF(F, 2, m);
                // TODO 7-9
                F = multipleSoftF(F, 3, m);
                
                if (F.size() > 0 && F.size() <= 2) {
                    for (int j = 0; j < F.size(); j++) {
                        F.get(j).set(8, Float.MAX_VALUE);
                    }
                } else if (F.size() > 2) {
                    F.get(0).set(8, Float.MAX_VALUE);
                    F.get(F.size()-1).set(8, Float.MAX_VALUE);
                    for (int j = 1; j < F.size()-1; j++) {
                        float dist = getDistance(F, j);
                        F.get(j).set(8, dist);
                    }
                }
                
                // put it back into paretos.
                Fprops.put(Func.KEY_F + i, F);
                
            } catch (Exception e) {
                if (Func.DEBUG) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    private static ArrayList<ArrayList<Object>> multipleSoftF(ArrayList<ArrayList<Object>> F, int level, int[][] m) {
        // TODO 1/4/7.
        ArrayList<ArrayList<ArrayList<Object>>> clustersLevel1 = getClusters(F, level);
        // TODO 2/5/8.
        for (int j = 0; j < clustersLevel1.size(); j++) {
            ArrayList<ArrayList<Object>> Fcluster = clustersLevel1.get(j);
            Fcluster = sortF(Fcluster, m[level]);
        }
        // TODO 3/6/9.
        F.clear();
        for (int j = 0; j < clustersLevel1.size(); j++) {
            for (int k = 0; k < clustersLevel1.get(j).size(); k++) {
                F.add(clustersLevel1.get(j).get(k));
            }
        }
        return F;
    }
    
    private static ArrayList<ArrayList<ArrayList<Object>>> getClusters(ArrayList<ArrayList<Object>> F, int level) {
        ArrayList<ArrayList<ArrayList<Object>>> clusters = new ArrayList<ArrayList<ArrayList<Object>>>();
        ArrayList<ArrayList<Object>> Ftemp = new ArrayList<ArrayList<Object>>();
        for (int j = 0; j < F.size(); j++) {
            if (j == 0) {
                Ftemp.add(F.get(j));
            } else {
                if (
                        (
                            level == 1 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                        )
                        ||
                        (
                            level == 2 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                                            && (Float.parseFloat((String) F.get(j).get(1))) == (Float.parseFloat((String) F.get(j - 1).get(1))) 
                        )
                        ||
                        (
                            level == 3 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                                            && (Float.parseFloat((String) F.get(j).get(1))) == (Float.parseFloat((String) F.get(j - 1).get(1))) 
                                            && (Float.parseFloat((String) F.get(j).get(2))) == (Float.parseFloat((String) F.get(j - 1).get(2))) 
                        )
                   ) {
                    Ftemp.add(F.get(j));
                } else {
                    ArrayList<ArrayList<Object>> Ftemp2 = new ArrayList<ArrayList<Object>>();
                    Ftemp2.addAll(Ftemp);
                    clusters.add(Ftemp2);
                    Ftemp.clear();
                    Ftemp.add(F.get(j));
                }
            }
            if (j == F.size() - 1) {
                clusters.add(Ftemp);
            }
        }
        return clusters;
    }
    
    public static ArrayList<ArrayList<Object>> sortF(ArrayList<ArrayList<Object>> Ftemp, int[] m) {
        Collections.sort(Ftemp, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return Float.parseFloat(o1.get(m[0]).toString()) > Float.parseFloat(o2.get(m[0]).toString()) ? m[1] : m[2];
            }
        });
        return Ftemp;
    }
    
    private static float getDistance(ArrayList<ArrayList<Object>> F, int currentIndex) {
        float calc = calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(0)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(0)), 
                fmx[0], 
                fmx[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(1)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(1)), 
                fmy[0], 
                fmy[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(2)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(2)), 
                fmz1[0], 
                fmz1[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(3)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(3)), 
                fmz2[0], 
                fmz2[1]);
        return calc;
    }
    
    private static float calcDistancePerM(float yMax, float yMin, float xMax, float xMin) {
        float up = yMax - yMin;
        up = up < 0 ? up * -1.0f : up;
        float down = xMax - xMin;
        down = down < 0 ? down * -1.0f : down;
        return up * 1.0f / down;
    }
}
