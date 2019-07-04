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
public class DiceAndAntidiceAlgo {
    
    public static String getResult(ArrayList<ArrayList<Integer>> testCases) {
        String output = "";
        try {
            
            // init and reset.
            ArrayList<ArrayList<Float>> dices = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> dice = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    dice.add(0.0f);
                }
                dices.add(dice);
            }
            ArrayList<ArrayList<Float>> antidices = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> antidice = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    antidice.add(0.0f);
                }
                antidices.add(antidice);
            }
            
            // calculate
            float w_dice = 0.5f;
            float w_antidice = 2.0f;
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
                        int ttn = T1x.size();
                        int ttu = T1.size() + T2.size();
                        float dice = 1 - (ttn * 1.0f / (ttn + (w_dice * (ttu - ttn))));
                        dices.get(i).set(j, dice);
                        float antidice = 1 - (ttn * 1.0f / (ttn + (w_antidice * (ttu - ttn))));
                        antidices.get(i).set(j, antidice);
                    }
                }
            }
            
            // view matrix
            output += "Dice:\n";
            for (int i = 0; i < dices.size(); i++) {
                output += "TP"+Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < dices.get(i).size(); j++) {
                    output += Func.float_df.format(dices.get(i).get(j));
                    if (j != dices.get(i).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            output += "Anti-Dice:\n";
            for (int i = 0; i < antidices.size(); i++) {
                output += "TP"+Func.getFormatInteger((i+1)+"", 2)+": ";
                for (int j = 0; j < antidices.get(i).size(); j++) {
                    output += Func.float_df.format(antidices.get(i).get(j));
                    if (j != antidices.get(i).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            
            // view path
            output += "\nPaths:\n";
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
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    }
}
