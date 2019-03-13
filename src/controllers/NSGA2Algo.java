/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author umar
 */
public class NSGA2Algo {
    
    public static Properties Fprops = new Properties();
    
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
}
