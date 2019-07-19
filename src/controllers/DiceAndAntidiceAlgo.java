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
            ArrayList<ArrayList<Float>> dicesTempGlobal = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> dice = new ArrayList<Float>();
                ArrayList<Float> diceTemp = new ArrayList<Float>();
                ArrayList<Float> diceTempGlobal = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    dice.add(0.0f);
                    diceTemp.add(0.0f);
                    diceTempGlobal.add(0.0f);
                }
                dices.add(dice);
                dicesTemp.add(diceTemp);
                dicesTempGlobal.add(diceTempGlobal);
            }
            ArrayList<ArrayList<Float>> antidices = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> antidicesTemp = new ArrayList<ArrayList<Float>>();
            ArrayList<ArrayList<Float>> antidicesTempGlobal = new ArrayList<ArrayList<Float>>();
            for (int i = 0; i < testCases.size(); i++) {
                ArrayList<Float> antidice = new ArrayList<Float>();
                ArrayList<Float> antidiceTemp = new ArrayList<Float>();
                ArrayList<Float> antidiceTempGlobal = new ArrayList<Float>();
                for (int j = 0; j < testCases.size(); j++) {
                    antidice.add(0.0f);
                    antidiceTemp.add(0.0f);
                    antidiceTempGlobal.add(0.0f);
                }
                antidices.add(antidice);
                antidicesTemp.add(antidiceTemp);
                antidicesTempGlobal.add(antidiceTempGlobal);
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
                        dicesTempGlobal.get(i).set(j, dice);
                        float antidice = 1 - (ttn * 1.0f / (ttn + (w_antidice * (ttu - ttn))));
                        antidices.get(i).set(j, antidice);
                        antidicesTemp.get(i).set(j, antidice);
                        antidicesTempGlobal.get(i).set(j, antidice);
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
            
            // process to local prior dice list
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
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    priorDice.add(best1);
                    for (int i = 0; i < testCases.size(); i++) {
                        dicesTemp.get(best1).set(i, 0.00f);
                        dicesTemp.get(i).set(best1, 0.00f);
                    }
                    if (priorDice.size() < testCases.size()) {
                        priorDice.add(best2);
                        for (int i = 0; i < testCases.size(); i++) {
                            dicesTemp.get(best2).set(i, 0.00f);
                            dicesTemp.get(i).set(best2, 0.00f);
                        }
                    }
                }
                System.out.println("\nProcess Local Prior Dice #"+(t+1));
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
            System.out.println("\nLocal Prior Dice List: " + priorDice);
            
            // process to local prior antidice list
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
                            }
                        }
                    }
                }
                if (best1 != -1) {
                    priorAntiDice.add(best1);
                    for (int i = 0; i < testCases.size(); i++) {
                        antidicesTemp.get(best1).set(i, 0.00f);
                        antidicesTemp.get(i).set(best1, 0.00f);
                    }
                    if (priorAntiDice.size() < testCases.size()) {
                        priorAntiDice.add(best2);
                        for (int i = 0; i < testCases.size(); i++) {
                            antidicesTemp.get(best2).set(i, 0.00f);
                            antidicesTemp.get(i).set(best2, 0.00f);
                        }
                    }
                }
                System.out.println("\nProcess Local Prior Anti-Dice #"+(t+1));
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
            System.out.println("\nLocal Prior Anti-Dice List: " + priorAntiDice);
            
            // process to global prior dice list
            ArrayList<Integer> gpriornotDice = new ArrayList<Integer>();
            for (int i = 0; i < testCases.size(); i++) {
                gpriornotDice.add(i);
            }
            ArrayList<Integer> gpriorDice = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < dicesTempGlobal.size(); i++) {
                    for (int j = i; j < dicesTempGlobal.get(i).size(); j++) {
                        if (dicesTempGlobal.get(i).get(j) > maxLocal) {
                            if (!gpriorDice.contains(i) && !gpriorDice.contains(j)) {
                                maxLocal = dicesTempGlobal.get(i).get(j);
                                best1 = i;
                                best2 = j;
                            }
                        }
                    }
                }
                if (t == 0) {
                    if (best1 != -1) {
                        gpriorDice.add(best1);
                        gpriornotDice.remove((Object) best1);
                        dicesTempGlobal.get(best1).set(best2, 0.00f);
                        if (gpriorDice.size() < testCases.size()) {
                            gpriorDice.add(best2);
                            gpriornotDice.remove((Object) best2);
                            dicesTempGlobal.get(best2).set(best1, 0.00f);
                        }
                    }
                } else {
                    if (gpriornotDice.size() > 0) {
                        float maxTotal = 0.00f;
                        int bestGpriornot = gpriornotDice.get(0);
                        for (int i = 0; i < gpriornotDice.size(); i++) {
                            float total = 0.00f;
                            for (int j = 0; j < gpriorDice.size(); j++) {
                                int x = gpriornotDice.get(i);
                                int y = gpriorDice.get(j);
                                total += dicesTempGlobal.get(x).get(y);
                            }
                            if (total > maxTotal) {
                                maxTotal = total;
                                bestGpriornot = gpriornotDice.get(i);
                            }
                        }
                        gpriorDice.add(bestGpriornot);
                        gpriornotDice.remove((Object) bestGpriornot);
                    }
                }
            }
            System.out.println("\nGlobal Prior Dice List: " + gpriorDice);
            
            // process to global prior anti-dice list
            ArrayList<Integer> gpriornotAntiDice = new ArrayList<Integer>();
            for (int i = 0; i < testCases.size(); i++) {
                gpriornotAntiDice.add(i);
            }
            ArrayList<Integer> gpriorAntiDice = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
                int best1 = -1;
                int best2 = -1;
                float maxLocal = 0.00f;
                for (int i = 0; i < antidicesTempGlobal.size(); i++) {
                    for (int j = i; j < antidicesTempGlobal.get(i).size(); j++) {
                        if (antidicesTempGlobal.get(i).get(j) > maxLocal) {
                            if (!gpriorAntiDice.contains(i) && !gpriorAntiDice.contains(j)) {
                                maxLocal = antidicesTempGlobal.get(i).get(j);
                                best1 = i;
                                best2 = j;
                            }
                        }
                    }
                }
                if (t == 0) {
                    if (best1 != -1) {
                        gpriorAntiDice.add(best1);
                        gpriornotAntiDice.remove((Object) best1);
                        antidicesTempGlobal.get(best1).set(best2, 0.00f);
                        if (gpriorAntiDice.size() < testCases.size()) {
                            gpriorAntiDice.add(best2);
                            gpriornotAntiDice.remove((Object) best2);
                            antidicesTempGlobal.get(best2).set(best1, 0.00f);
                        }
                    }
                } else {
                    if (gpriornotAntiDice.size() > 0) {
                        float maxTotal = 0.00f;
                        int bestGpriornot = gpriornotAntiDice.get(0);
                        for (int i = 0; i < gpriornotAntiDice.size(); i++) {
                            float total = 0.00f;
                            for (int j = 0; j < gpriorAntiDice.size(); j++) {
                                int x = gpriornotAntiDice.get(i);
                                int y = gpriorAntiDice.get(j);
                                total += antidicesTempGlobal.get(x).get(y);
                            }
                            if (total > maxTotal) {
                                maxTotal = total;
                                bestGpriornot = gpriornotAntiDice.get(i);
                            }
                        }
                        gpriorAntiDice.add(bestGpriornot);
                        gpriornotAntiDice.remove((Object) bestGpriornot);
                    }
                }
            }
            System.out.println("\nGlobal Prior Anti-Dice List: " + gpriorAntiDice);
            
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
            output += "\nDice Local Priority Path List:\n";
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
            output += "\nDice Global Priority Path List:\n";
            for (int i = 0; i < gpriorDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((gpriorDice.get(i)+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(gpriorDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(gpriorDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(gpriorDice.get(i)).size()-1) {
                        output += ", ";
                    }
                }
                output += "\n";
            }
            output += "\nAnti-Dice Local Priority Path List:\n";
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
            output += "\nAnti-Dice Global Priority Path List:\n";
            for (int i = 0; i < gpriorAntiDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((gpriorAntiDice.get(i)+1)+"", 2)+": ";
                for (int j = 0; j < testCases.get(gpriorAntiDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(gpriorAntiDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    if (j != testCases.get(gpriorAntiDice.get(i)).size()-1) {
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
