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
    
    public static String getResult(ArrayList<ArrayList<Integer>> testCases) {
        String output = "";
        try {
            
            // init and reset.
            ArrayList<ArrayList<Float>> jaccards = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> jaccardsTemp = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> jaccard = new ArrayList<Float>();
                ArrayList<Float> jaccardTemp = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    jaccard.add(0.0f);
                    jaccardTemp.add(0.0f);
                }
                jaccards.add(jaccard);
                jaccardsTemp.add(jaccardTemp);
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
            
            // process to prior list
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
                                jaccardsTemp.get(i).set(j, 0.00f);
                                jaccardsTemp.get(j).set(i, 0.00f);
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    prior.add(best1);
                    if (prior.size() < testCases.size()) {
                        prior.add(best2);
                    }
                }
                System.out.println("\nProcess #"+(t+1));
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
            System.out.println("\nPrior List: " + prior);
            
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
            output += "\nPriority Path List:\n";
            for (int i = 0; i < prior.size(); i++) {
                output += "TP" + Func.getFormatInteger((prior.get(i)+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(prior.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(prior.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(prior.get(i)).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    } 
}
