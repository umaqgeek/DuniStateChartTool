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
public class JaccardDistanceAlgo {
    
    public static String getResult(ArrayList<ArrayList<Integer>> testCases, boolean isSaved) {
        String output = "";
        try {
            
            // init and reset.
            ArrayList<ArrayList<Float>> jaccards = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> jaccardsTemp = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> jaccardsTempGlobal = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> jaccard = new ArrayList<Float>();
                ArrayList<Float> jaccardTemp = new ArrayList<Float>();
                ArrayList<Float> jaccardTempGlobal = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    jaccard.add(0.0f);
                    jaccardTemp.add(0.0f);
                    jaccardTempGlobal.add(0.0f);
                }
                jaccards.add(jaccard);
                jaccardsTemp.add(jaccardTemp);
                jaccardsTempGlobal.add(jaccardTempGlobal);
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
                        int c = T1x.size();
                        int eT1 = T1.size();
                        int eT2 = T2.size();
                        float d = 1 - (c * 1.0f / ((eT1+eT2)*1.0f/2.0f));
                        jaccards.get(i).set(j, d);
                        jaccardsTemp.get(i).set(j, d);
                        jaccardsTempGlobal.get(i).set(j, d);
                    }
                }
            }
            
            // view matrix
            output += "Jaccard Distance:\n";
            for (int i = 0; i < jaccards.size(); i++) {
                output += "TP"+Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < jaccards.get(i).size(); j++) {
                    output += Func.float_df.format(jaccards.get(i).get(j));
                    if (j != jaccards.get(i).size()-1) {
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
                for (int i = 0; i < jaccardsTemp.size(); i++) {
                    for (int j = i; j < jaccardsTemp.get(i).size(); j++) {
                        if (jaccardsTemp.get(i).get(j) > maxLocal) {
                            if (!prior.contains(i) && !prior.contains(j)) {
                                maxLocal = jaccardsTemp.get(i).get(j);
                                best1 = i;
                                best2 = j;
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    prior.add(best1);
                    for (int i = 0; i < testCases.size(); i++) {
                        jaccardsTemp.get(best1).set(i, 0.00f);
                        jaccardsTemp.get(i).set(best1, 0.00f);
                    }
                    if (prior.size() < testCases.size()) {
                        prior.add(best2);
                        for (int i = 0; i < testCases.size(); i++) {
                            jaccardsTemp.get(best2).set(i, 0.00f);
                            jaccardsTemp.get(i).set(best2, 0.00f);
                        }
                    }
                }
                System.out.println("\nProcess Prior Local #"+(t+1));
                for (int i = 0; i < jaccardsTemp.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + ": ");
                    for (int j = 0; j < jaccardsTemp.get(i).size(); j++) {
                        System.out.print(Func.float_df.format(jaccardsTemp.get(i).get(j)));
                        if (j != jaccardsTemp.get(i).size() - 1) {
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
                for (int i = 0; i < jaccardsTempGlobal.size(); i++) {
                    for (int j = i; j < jaccardsTempGlobal.get(i).size(); j++) {
                        if (jaccardsTempGlobal.get(i).get(j) > maxLocal) {
                            if (!gprior.contains(i) && !gprior.contains(j)) {
                                maxLocal = jaccardsTempGlobal.get(i).get(j);
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
                        jaccardsTempGlobal.get(best1).set(best2, 0.00f);
                        if (gprior.size() < testCases.size()) {
                            gprior.add(best2);
                            gpriornot.remove((Object) best2);
                            jaccardsTempGlobal.get(best2).set(best1, 0.00f);
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
                                total += jaccardsTempGlobal.get(x).get(y);
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
                Func.saveToTxt("jaccard_local.txt", outfileLocal, false);
                Func.saveToTxt("jaccard_global.txt", outfileGlobal, false);
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    } 
}
