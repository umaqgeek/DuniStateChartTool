/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;

/**
 *
 * @author umar
 */
public class JaroWrinklerAlgo {
    
    private int getNumberTranspositions(ArrayList<Integer> t1, ArrayList<Integer> t2) {
        int total = 0;
        try {
            
            for (int i = 0; i < t1.size() && i < t2.size(); i++) {
                if (t1.get(i) != t2.get(i)) {
                    HammingDistanceAlgo hda = new HammingDistanceAlgo();
                    ArrayList<Integer> notSameArr = hda.getNotSame(t1, t2);
                    total += notSameArr.size();
                }
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return total;
    }
    
    public static String getResult(ArrayList<ArrayList<Integer>> testCases, boolean isSaved) {
        String output = "";
        try {
            
            // init and reset.
            ArrayList<ArrayList<Float>> jaros = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> jarosTemp = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> jarosTempGlobal = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> jaro = new ArrayList<Float>();
                ArrayList<Float> jaroTemp = new ArrayList<Float>();
                ArrayList<Float> jaroTempGlobal = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    jaro.add(0.0f);
                    jaroTemp.add(0.0f);
                    jaroTempGlobal.add(0.0f);
                }
                jaros.add(jaro);
                jarosTemp.add(jaroTemp);
                jarosTempGlobal.add(jaroTempGlobal);
            }
            
            // calculate
            for (int i = 0; i < testCases.size(); i++) {
                for (int j = 0; j < testCases.size(); j++) {
                    if (i != j) {
                        ArrayList<Integer> T1 = new ArrayList<Integer>();
                        T1.addAll(testCases.get(i));
                        ArrayList<Integer> T1x = new ArrayList<Integer>();
                        T1x.addAll(testCases.get(i));
                        ArrayList<Integer> T2 = new ArrayList<Integer>();
                        T2.addAll(testCases.get(j));
                        T1x.retainAll(T2);
                        int m = T1x.size();
                        int t1 = T1.size();
                        int t2 = T2.size();
                        JaroWrinklerAlgo jw = new JaroWrinklerAlgo();
                        int t = jw.getNumberTranspositions(T1, T2);
                        float dj = (1.0f/3.0f) * ((m * 1.0f / t1) + (m * 1.0f / t2) + ((m - t) * 1.0f / m));
                        int l = 0;
                        int limitCommon = 4;
                        for (int k = 0; k < limitCommon && k < T1.size() && k < T2.size(); k++) {
                            if (T1.get(k) == T2.get(k)) {
                                l += 1;
                            } else {
                                break;
                            }
                        }
                        float p = 0.1f;
                        float djw = dj + ((l * 1.0f * p) * (1 - dj));
                        float simjw = 1 - djw;
                        jaros.get(i).set(j, simjw);
                        jarosTemp.get(i).set(j, simjw);
                        jarosTempGlobal.get(i).set(j, simjw);
                    }
                }
            }
            
            // view matrix
            output += "Jaro Wrinkler:\n";
            for (int i = 0; i < jaros.size(); i++) {
                output += "TP"+Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < jaros.get(i).size(); j++) {
                    output += Func.float_df.format(jaros.get(i).get(j));
                    if (j != jaros.get(i).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            
            // process to local prior list
            ArrayList<Integer> prior = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < jarosTemp.size(); i++) {
                    for (int j = i; j < jarosTemp.get(i).size(); j++) {
                        if (jarosTemp.get(i).get(j) > maxLocal) {
                            if (!prior.contains(i) && !prior.contains(j)) {
                                maxLocal = jarosTemp.get(i).get(j);
                                best1 = i;
                                best2 = j;
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    prior.add(best1);
                    for (int i = 0; i < testCases.size(); i++) {
                        jarosTemp.get(best1).set(i, 0.00f);
                        jarosTemp.get(i).set(best1, 0.00f);
                    }
                    if (prior.size() < testCases.size()) {
                        prior.add(best2);
                        for (int i = 0; i < testCases.size(); i++) {
                            jarosTemp.get(best2).set(i, 0.00f);
                            jarosTemp.get(i).set(best2, 0.00f);
                        }
                    }
                }
                System.out.println("\nProcess Prior Local #"+(t+1));
                for (int i = 0; i < jarosTemp.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + ": ");
                    for (int j = 0; j < jarosTemp.get(i).size(); j++) {
                        System.out.print(Func.float_df.format(jarosTemp.get(i).get(j)));
                        if (j != jarosTemp.get(i).size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println("");
                }
            }
            System.out.println("\nLocal Prior List: " + prior);
            
            // process to global prior list
            ArrayList<Integer> gpriornot = new ArrayList<Integer>();
            for (int i = 0; i < testCases.size(); i++) {
                gpriornot.add(i);
            }
            ArrayList<Integer> gprior = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < jarosTempGlobal.size(); i++) {
                    for (int j = i; j < jarosTempGlobal.get(i).size(); j++) {
                        if (jarosTempGlobal.get(i).get(j) > maxLocal) {
                            if (!gprior.contains(i) && !gprior.contains(j)) {
                                maxLocal = jarosTempGlobal.get(i).get(j);
                                best1 = i;
                                best2 = j;
                            }
                        }
                    }
                }
                if (t == 0) {
                    if (best1 != -1) {
                        gprior.add(best1);
                        gpriornot.remove((Object) best1);
                        jarosTempGlobal.get(best1).set(best2, 0.00f);
                        if (gprior.size() < testCases.size()) {
                            gprior.add(best2);
                            gpriornot.remove((Object) best2);
                            jarosTempGlobal.get(best2).set(best1, 0.00f);
                        }
                    }
                } else {
                    if (gpriornot.size() > 0) {
                        float maxTotal = 0.00f;
                        int bestGpriornot = gpriornot.get(0);
                        for (int i = 0; i < gpriornot.size(); i++) {
                            float total = 0.00f;
                            for (int j = 0; j < gprior.size(); j++) {
                                int x = gpriornot.get(i);
                                int y = gprior.get(j);
                                total += jarosTempGlobal.get(x).get(y);
                            }
                            if (total > maxTotal) {
                                maxTotal = total;
                                bestGpriornot = gpriornot.get(i);
                            }
                        }
                        gprior.add(bestGpriornot);
                        gpriornot.remove((Object) bestGpriornot);
                    }
                }
            }
            System.out.println("\nGlobal Prior List: " + gprior);
            
            String outfileLocal = "";
            String outfileGlobal = "";
            
            // view path
            output += "\nOriginal Path List:\n";
            for (int i = 0; i < testCases.size(); i++) {
                output += "TP" + Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(i).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(i).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(i).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            output += "\nLocal Priority Path List:\n";
            outfileLocal += "# Local Priority Path List:\n";
            for (int i = 0; i < prior.size(); i++) {
                output += "TP" + Func.getFormatInteger((prior.get(i)+1)+"", 2)+": ";
                outfileLocal += prior.get(i) + ":";
                for (int j = 0; j < testCases.get(prior.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(prior.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileLocal += testCases.get(prior.get(i)).get(j);
                    if (j != testCases.get(prior.get(i)).size()-1) {
                        output += ", ";
                        outfileLocal += "-";
                    }
                }
                output += "\n";
                outfileLocal += "\n";
            }
            output += "\nGlobal Priority Path List:\n";
            outfileGlobal += "# Glocal Priority Path List:\n";
            for (int i = 0; i < gprior.size(); i++) {
                output += "TP" + Func.getFormatInteger((gprior.get(i)+1)+"", 2)+": ";
                outfileGlobal += gprior.get(i) + ":";
                for (int j = 0; j < testCases.get(gprior.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(gprior.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileGlobal += testCases.get(gprior.get(i)).get(j);
                    if (j != testCases.get(gprior.get(i)).size()-1) {
                        output += ", ";
                        outfileGlobal += "-";
                    }
                }
                output += "\n";
                outfileGlobal += "\n";
            }
            
            if (isSaved) {
                Func.saveToTxt("APFD/jarowrinkler_local.txt", outfileLocal, false);
                Func.saveToTxt("APFD/jarowrinkler_global.txt", outfileGlobal, false);
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    }
}
