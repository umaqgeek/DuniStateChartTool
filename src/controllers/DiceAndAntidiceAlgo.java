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
            ArrayList<ArrayList<Float>> dicesTemp = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> dice = new ArrayList<Float>();
                ArrayList<Float> diceTemp = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    dice.add(0.0f);
                    diceTemp.add(0.0f);
                }
                dices.add(dice);
                dicesTemp.add(diceTemp);
            }
            ArrayList<ArrayList<Float>> antidices = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> antidicesTemp = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> antidice = new ArrayList<Float>();
                ArrayList<Float> antidiceTemp = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    antidice.add(0.0f);
                    antidiceTemp.add(0.0f);
                }
                antidices.add(antidice);
                antidicesTemp.add(antidiceTemp);
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
                        dicesTemp.get(i).set(j, dice);
                        float antidice = 1 - (ttn * 1.0f / (ttn + (w_antidice * (ttu - ttn))));
                        antidices.get(i).set(j, antidice);
                        antidicesTemp.get(i).set(j, antidice);
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
            
            // process to prior dice list
            ArrayList<Integer> priorDice = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < dicesTemp.size(); i++) {
                    for (int j = i; j < dicesTemp.get(i).size(); j++) {
                        if (dicesTemp.get(i).get(j) > maxLocal) {
                            if (!priorDice.contains(i) && !priorDice.contains(j)) {
                                maxLocal = dicesTemp.get(i).get(j);
                                best1 = i;
                                best2 = j;
                                dicesTemp.get(i).set(j, 0.00f);
                                dicesTemp.get(j).set(i, 0.00f);
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    priorDice.add(best1);
                    if (priorDice.size() < testCases.size()) {
                        priorDice.add(best2);
                    }
                }
                System.out.println("\nProcess #"+(t+1));
                for (int i = 0; i < dicesTemp.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + ": ");
                    for (int j = 0; j < dicesTemp.get(i).size(); j++) {
                        System.out.print(Func.float_df.format(dicesTemp.get(i).get(j)));
                        if (j != dicesTemp.get(i).size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println("");
                }
            }
            System.out.println("\nPrior Dice List: " + priorDice);
            
            // process to prior antidice list
            ArrayList<Integer> priorAntiDice = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < antidicesTemp.size(); i++) {
                    for (int j = i; j < antidicesTemp.get(i).size(); j++) {
                        if (antidicesTemp.get(i).get(j) > maxLocal) {
                            if (!priorAntiDice.contains(i) && !priorAntiDice.contains(j)) {
                                maxLocal = antidicesTemp.get(i).get(j);
                                best1 = i;
                                best2 = j;
                                antidicesTemp.get(i).set(j, 0.00f);
                                antidicesTemp.get(j).set(i, 0.00f);
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    priorAntiDice.add(best1);
                    if (priorAntiDice.size() < testCases.size()) {
                        priorAntiDice.add(best2);
                    }
                }
                System.out.println("\nProcess #"+(t+1));
                for (int i = 0; i < antidicesTemp.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + ": ");
                    for (int j = 0; j < antidicesTemp.get(i).size(); j++) {
                        System.out.print(Func.float_df.format(antidicesTemp.get(i).get(j)));
                        if (j != antidicesTemp.get(i).size() - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.println("");
                }
            }
            System.out.println("\nPrior Anti-Dice List: " + priorAntiDice);
            
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
            output += "\nDice Priority Path List:\n";
            for (int i = 0; i < priorDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((priorDice.get(i)+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(priorDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(priorDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(priorDice.get(i)).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            output += "\nAnti-Dice Priority Path List:\n";
            for (int i = 0; i < priorAntiDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((priorAntiDice.get(i)+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(priorAntiDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(priorAntiDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(priorAntiDice.get(i)).size()-1) {
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
