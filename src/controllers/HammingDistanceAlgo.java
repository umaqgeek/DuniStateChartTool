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
public class HammingDistanceAlgo {
    
    public ArrayList<Integer> getNotSame(ArrayList<Integer> T1, ArrayList<Integer> T2) {
        ArrayList<Integer> output = new ArrayList<Integer>();
        try {
            
            for (int i = 0; i < T1.size(); i++) {
                boolean notFound = true;
                for (int j = 0; j < T2.size(); j++) {
                    if (T1.get(i) == T2.get(j)) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    output.add(T1.get(i));
                }
            }
            
            for (int i = 0; i < T2.size(); i++) {
                boolean notFound = true;
                for (int j = 0; j < T1.size(); j++) {
                    if (T2.get(i) == T1.get(j)) {
                        notFound = false;
                        break;
                    }
                }
                if (notFound) {
                    output.add(T2.get(i));
                }
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    }
    
    public static String getResult(ArrayList<ArrayList<Integer>> testCases) {
        String output = "";
        try {
            
            // init and reset.
            ArrayList<ArrayList<Float>> hammings = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> hammingsTemp = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> hamming = new ArrayList<Float>();
                ArrayList<Float> hammingTemp = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    hamming.add(0.0f);
                    hammingTemp.add(0.0f);
                }
                hammings.add(hamming);
                hammingsTemp.add(hammingTemp);
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
                        HammingDistanceAlgo hma = new HammingDistanceAlgo();
                        ArrayList<Integer> T2x = hma.getNotSame(T1, T2);
                        int cTotalF = T1.size() + T2.size();
                        int c_sama = T1x.size();
                        int c_tak_sama = T2x.size();
                        float h = 1 - ((c_sama + c_tak_sama) * 1.0f / cTotalF);
                        hammings.get(i).set(j, h);
                        hammingsTemp.get(i).set(j, h);
                    }
                }
            }
            
            // view matrix
            output += "Hamming Distance:\n";
            for (int i = 0; i < hammings.size(); i++) {
                output += "TP"+Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < hammings.get(i).size(); j++) {
                    output += Func.float_df.format(hammings.get(i).get(j));
                    if (j != hammings.get(i).size()-1) {
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
                for (int i = 0; i < hammingsTemp.size(); i++) {
                    for (int j = i; j < hammingsTemp.get(i).size(); j++) {
                        if (hammingsTemp.get(i).get(j) > maxLocal) {
                            if (!prior.contains(i) && !prior.contains(j)) {
                                maxLocal = hammingsTemp.get(i).get(j);
                                best1 = i;
                                best2 = j;
                                hammingsTemp.get(i).set(j, 0.00f);
                                hammingsTemp.get(j).set(i, 0.00f);
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
                for (int i = 0; i < hammingsTemp.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + ": ");
                    for (int j = 0; j < hammingsTemp.get(i).size(); j++) {
                        System.out.print(Func.float_df.format(hammingsTemp.get(i).get(j)));
                        if (j != hammingsTemp.get(i).size() - 1) {
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
