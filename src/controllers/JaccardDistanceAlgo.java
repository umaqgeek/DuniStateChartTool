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
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> jaccard = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    jaccard.add(0.0f);
                }
                jaccards.add(jaccard);
            }
            
            // calculate
            for (int i = 0; i < testCases.size(); i++) {
                for (int j = 0; j < testCases.size(); j++) {
                    if (i != j) {
                        ArrayList<Integer> T1 = testCases.get(i);
                        ArrayList<Integer> T1x = testCases.get(i);
                        ArrayList<Integer> T2 = testCases.get(j);
                        T1x.retainAll(T2);
                        int c = T1x.size();
                        int eT1 = T1.size();
                        int eT2 = T2.size();
                        float d = 1 - (c * 1.0f / ((eT1+eT2)*1.0f/2.0f));
                        System.out.println(i+""+j+":c="+c+",eT1="+eT1+",eT2="+eT2+",d="+d);
                        jaccards.get(i).set(j, d);
                    }
                }
            }
            
            // view matrix
            output += "Jaccard Distance:\n";
            for (int i = 0; i < jaccards.size(); i++) {
                output += "T"+(i+1)+": ";
                for (int j = 0; j < jaccards.get(i).size(); j++) {
                    output += Func.float_df.format(jaccards.get(i).get(j));
                    if (j != jaccards.get(i).size()-1) {
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
