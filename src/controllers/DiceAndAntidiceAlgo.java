/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import views.DissimilarityPage;

/**
 *
 * @author umar
 */
public class DiceAndAntidiceAlgo {
    
    private static String FILE_LOCAL_DICE = "APFD/dice_local.txt";
    private static String FILE_GLOBAL_DICE = "APFD/dice_global.txt";
    private static String FILE_LOCAL_ANTIDICE = "APFD/antidice_local.txt";
    private static String FILE_GLOBAL_ANTIDICE = "APFD/antidice_global.txt";
    
    public static String getResult(boolean isLocal, ArrayList<ArrayList<Integer>> testCases, boolean isSaved, boolean isCompared) {
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
                } else if (priorDice.size() <= (testCases.size() - 1)) {
                    for (int i = 0; i < testCases.size(); i++) {
                        if (!priorDice.contains(i)) {
                            priorDice.add(i);
                            break;
                        }
                    }
                }
            }
            System.out.println("\nLocal Prior Dice List: " + priorDice);
            
            // process to local prior antidice list
            ArrayList<Integer> priorAntiDice = new ArrayList<Integer>();
            for (int t = 0; t < testCases.size(); t++) {
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
                } else if (priorAntiDice.size() <= (testCases.size() - 1)) {
                    for (int i = 0; i < testCases.size(); i++) {
                        if (!priorAntiDice.contains(i)) {
                            priorAntiDice.add(i);
                            break;
                        }
                    }
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
            
            String outfileLocalDice = "";
            String outfileGlobalDice = "";
            String outfileLocalAntiDice = "";
            String outfileGlobalAntiDice = "";
            
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
            outfileLocalDice += "# Dice Local Priority Path List:\n";
            for (int i = 0; i < priorDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((priorDice.get(i)+1)+"", 2)+": ";
                outfileLocalDice += priorDice.get(i) + ":";
                for (int j = 0; j < testCases.get(priorDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(priorDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileLocalDice += testCases.get(priorDice.get(i)).get(j);
                    if (j != testCases.get(priorDice.get(i)).size()-1) {
                        output += ", ";
                        outfileLocalDice += "-";
                    }
                }
                output += "\n";
                outfileLocalDice += "\n";
            }
            output += "\nDice Global Priority Path List:\n";
            outfileGlobalDice += "# Dice Glocal Priority Path List:\n";
            for (int i = 0; i < gpriorDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((gpriorDice.get(i)+1)+"", 2)+": ";
                outfileGlobalDice += gpriorDice.get(i) + ":";
                for (int j = 0; j < testCases.get(gpriorDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(gpriorDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileGlobalDice += testCases.get(gpriorDice.get(i)).get(j);
                    if (j != testCases.get(gpriorDice.get(i)).size()-1) {
                        output += ", ";
                        outfileGlobalDice += "-";
                    }
                }
                output += "\n";
                outfileGlobalDice += "\n";
            }
            output += "\nAnti-Dice Local Priority Path List:\n";
            outfileLocalAntiDice += "# Anti-Dice Local Priority Path List:\n";
            for (int i = 0; i < priorAntiDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((priorAntiDice.get(i)+1)+"", 2)+": ";
                outfileLocalAntiDice += priorAntiDice.get(i) + ":";
                for (int j = 0; j < testCases.get(priorAntiDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(priorAntiDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileLocalAntiDice += testCases.get(priorAntiDice.get(i)).get(j);
                    if (j != testCases.get(priorAntiDice.get(i)).size()-1) {
                        output += ", ";
                        outfileLocalAntiDice += "-";
                    }
                }
                output += "\n";
                outfileLocalAntiDice += "\n";
            }
            output += "\nAnti-Dice Global Priority Path List:\n";
            outfileGlobalAntiDice += "# Anti-Dice Glocal Priority Path List:\n";
            for (int i = 0; i < gpriorAntiDice.size(); i++) {
                output += "TP" + Func.getFormatInteger((gpriorAntiDice.get(i)+1)+"", 2)+": ";
                outfileGlobalAntiDice += gpriorAntiDice.get(i) + ":";
                for (int j = 0; j < testCases.get(gpriorAntiDice.get(i)).size(); j++) {
                    String name = UMLController.getStateName("s"+testCases.get(gpriorAntiDice.get(i)).get(j));
//                    String name = testCases.get(i).get(j).toString();
                    output += name;
                    outfileGlobalAntiDice += testCases.get(gpriorAntiDice.get(i)).get(j);
                    if (j != testCases.get(gpriorAntiDice.get(i)).size()-1) {
                        output += ", ";
                        outfileGlobalAntiDice += "-";
                    }
                }
                output += "\n";
                outfileGlobalAntiDice += "\n";
            }
            
            if (isSaved) {
                if (isLocal) {
                    Func.saveToTxt(DiceAndAntidiceAlgo.FILE_LOCAL_DICE, outfileLocalDice, false);
                    Func.saveToTxt(DiceAndAntidiceAlgo.FILE_LOCAL_ANTIDICE, outfileLocalAntiDice, false);
                } else {
                    Func.saveToTxt(DiceAndAntidiceAlgo.FILE_GLOBAL_DICE, outfileGlobalDice, false);
                    Func.saveToTxt(DiceAndAntidiceAlgo.FILE_GLOBAL_ANTIDICE, outfileGlobalAntiDice, false);
                }
            }
            
            int priorDiceSize = isLocal ? priorDice.size() : gpriorDice.size();
            int priorAntiDiceSize = isLocal ? priorAntiDice.size() : gpriorAntiDice.size();
            ArrayList<Integer> chosenPriorDice = new ArrayList<Integer>();
            ArrayList<Integer> chosenPriorAntiDice = new ArrayList<Integer>();
            if (isLocal) {
                chosenPriorDice.addAll(priorDice);
                chosenPriorAntiDice.addAll(priorAntiDice);
            } else {
                chosenPriorDice.addAll(gpriorDice);
                chosenPriorAntiDice.addAll(gpriorAntiDice);
            }
            
            if (isCompared) {
                
                String txtAPFD = "";
                
                // DICE
                System.out.println("\nDice current testCases:");
                for (int i = 0; i < testCases.size(); i++) {
                    System.out.print(i + ":");
                    for (int j = 0; j < testCases.get(i).size(); j++) {
                        System.out.print(testCases.get(i).get(j));
                        if (j != testCases.get(i).size() - 1) {
                            System.out.print("-");
                        }
                    }
                    System.out.println("");
                }
                Func funcDice = new Func();
                ArrayList<ArrayList<Integer>> oldTestCasesDice = funcDice.getPathsFromTxt(DiceAndAntidiceAlgo.FILE_LOCAL_DICE);
                if (oldTestCasesDice.size() <= 0) {
                    return output;
                }
                System.out.println("\nDice old testCases:");
                for (int i = 0; i < oldTestCasesDice.size(); i++) {
                    System.out.print(oldTestCasesDice.get(i).get(oldTestCasesDice.get(i).size() - 1) + ":");
                    for (int j = 0; j < oldTestCasesDice.get(i).size() - 1; j++) {
                        System.out.print(oldTestCasesDice.get(i).get(j));
                        if (j != oldTestCasesDice.get(i).size() - 2) {
                            System.out.print("-");
                        }
                    }
                    System.out.println("");
                }

                // init fault matrix.
                ArrayList<ArrayList<Boolean>> matrixFaultsDice = new ArrayList<ArrayList<Boolean>>();
                for (int i = 0; i < priorDiceSize && i < oldTestCasesDice.size(); i++) {
                    ArrayList<Boolean> mf = new ArrayList<Boolean>();
                    for (int j = 0; j < 10; j++) {
                        mf.add(false);
                    }
                    matrixFaultsDice.add(mf);
                }

                // calculate fault in each test path.
                for (int i = 0; i < priorDiceSize && i < oldTestCasesDice.size(); i++) {
                    ArrayList<Integer> T1 = new ArrayList<Integer>();
                    T1.addAll(testCases.get(i));
                    ArrayList<Integer> T1x = new ArrayList<Integer>();
                    T1x.addAll(testCases.get(i));
                    ArrayList<Integer> T2 = new ArrayList<Integer>();
                    T2.addAll(oldTestCasesDice.get(i));
                    T1x.retainAll(T2);
                    DiceAndAntidiceAlgo dada = new DiceAndAntidiceAlgo();
                    ArrayList<Integer> T2x = dada.getUnion(T1, T2);
                    int c_sama = T1x.size();
                    int c_union = T2x.size();
                    int fault = (int) Math.ceil((1.0 - (c_sama * 1.0 / c_union)) * 10);
                    fault = (fault - 1) < 1 ? 1 : fault;
                    fault = fault > matrixFaultsDice.get(0).size() ? matrixFaultsDice.get(0).size() : fault;
                    matrixFaultsDice.get(i).set((fault - 1), true);
                }

                System.out.println("\nDice Fault Matrix:");
                System.out.println("---------------------------------------------");
                System.out.println("    | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |10 |");
                System.out.println("---------------------------------------------");
                for (int i = 0; i < matrixFaultsDice.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + "|");
                    for (int j = 0; j < matrixFaultsDice.get(i).size(); j++) {
                        char star = matrixFaultsDice.get(i).get(j) ? '*' : ' ';
                        System.out.print(" " + star + " |");
                    }
                    System.out.println("");
                }
                System.out.println("---------------------------------------------");

                int nDice = priorDiceSize < oldTestCasesDice.size() ? priorDiceSize : oldTestCasesDice.size();
                int mDice = 0;
                ArrayList<Integer> foundTPDice = new ArrayList<Integer>();
                for (int i = 0; i < matrixFaultsDice.get(0).size(); i++) {
                    for (int j = 0; j < nDice; j++) {
                        if (matrixFaultsDice.get(j).get(i)) {
                            foundTPDice.add(j);
                            mDice += 1;
                            break;
                        }
                    }
                }
                int upDice = 0;
                for (int i = 0; i < foundTPDice.size(); i++) {
                    upDice += (chosenPriorDice.indexOf(foundTPDice.get(i)) + 1);
                }
                float APFDDice = 1.0f - ((upDice * 1.0f / (mDice * nDice)) + (1.0f / (2.0f * nDice)));

                System.out.println("\nDice APFD data:");
                System.out.println("n = " + nDice);
                System.out.println("m = " + mDice);
                System.out.println("found index: " + foundTPDice);
                System.out.println("up = " + upDice);
                System.out.println("APFD = 1 - ((up / (m * n)) + (1 / (2 * n)))");
                System.out.println("APFD = 1 - ((" + upDice + " / (" + mDice + " * " + nDice + ")) + (1 / (2 * " + nDice + ")))");
                System.out.println("APFD = " + APFDDice);
                
                String txtIsLocalDice = isLocal ? "Local" : "Global";
                txtAPFD += "Dice - " + txtIsLocalDice + " Distance\n";
                txtAPFD += "APFD = " + Func.float_df.format(APFDDice) + "\n";
                
                txtAPFD += "\n";
                
                // ANTI-DICE
                System.out.println("\nAnti-Dice current testCases:");
                for (int i = 0; i < testCases.size(); i++) {
                    System.out.print(i + ":");
                    for (int j = 0; j < testCases.get(i).size(); j++) {
                        System.out.print(testCases.get(i).get(j));
                        if (j != testCases.get(i).size() - 1) {
                            System.out.print("-");
                        }
                    }
                    System.out.println("");
                }
                Func funcAntiDice = new Func();
                ArrayList<ArrayList<Integer>> oldTestCasesAntiDice = funcAntiDice.getPathsFromTxt(DiceAndAntidiceAlgo.FILE_LOCAL_ANTIDICE);
                if (oldTestCasesAntiDice.size() <= 0) {
                    return output;
                }
                System.out.println("\nAnti-Dice old testCases:");
                for (int i = 0; i < oldTestCasesAntiDice.size(); i++) {
                    System.out.print(oldTestCasesAntiDice.get(i).get(oldTestCasesAntiDice.get(i).size() - 1) + ":");
                    for (int j = 0; j < oldTestCasesAntiDice.get(i).size() - 1; j++) {
                        System.out.print(oldTestCasesAntiDice.get(i).get(j));
                        if (j != oldTestCasesAntiDice.get(i).size() - 2) {
                            System.out.print("-");
                        }
                    }
                    System.out.println("");
                }

                // init fault matrix.
                ArrayList<ArrayList<Boolean>> matrixFaultsAntiDice = new ArrayList<ArrayList<Boolean>>();
                for (int i = 0; i < priorAntiDiceSize && i < oldTestCasesAntiDice.size(); i++) {
                    ArrayList<Boolean> mf = new ArrayList<Boolean>();
                    for (int j = 0; j < 10; j++) {
                        mf.add(false);
                    }
                    matrixFaultsAntiDice.add(mf);
                }

                // calculate fault in each test path.
                for (int i = 0; i < priorAntiDiceSize && i < oldTestCasesAntiDice.size(); i++) {
                    ArrayList<Integer> T1 = new ArrayList<Integer>();
                    T1.addAll(testCases.get(i));
                    ArrayList<Integer> T1x = new ArrayList<Integer>();
                    T1x.addAll(testCases.get(i));
                    ArrayList<Integer> T2 = new ArrayList<Integer>();
                    T2.addAll(oldTestCasesAntiDice.get(i));
                    T1x.retainAll(T2);
                    DiceAndAntidiceAlgo dada = new DiceAndAntidiceAlgo();
                    ArrayList<Integer> T2x = dada.getUnion(T1, T2);
                    int c_sama = T1x.size();
                    int c_union = T2x.size();
                    int fault = (int) Math.ceil((1.0 - (c_sama * 1.0 / c_union)) * 10);
                    fault = (fault - 1) < 1 ? 1 : fault;
                    fault = fault > matrixFaultsAntiDice.get(0).size() ? matrixFaultsAntiDice.get(0).size() : fault;
                    matrixFaultsAntiDice.get(i).set((fault - 1), true);
                }

                System.out.println("\nAnti-Dice Fault Matrix:");
                System.out.println("---------------------------------------------");
                System.out.println("    | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |10 |");
                System.out.println("---------------------------------------------");
                for (int i = 0; i < matrixFaultsAntiDice.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + "|");
                    for (int j = 0; j < matrixFaultsAntiDice.get(i).size(); j++) {
                        char star = matrixFaultsAntiDice.get(i).get(j) ? '*' : ' ';
                        System.out.print(" " + star + " |");
                    }
                    System.out.println("");
                }
                System.out.println("---------------------------------------------");

                int nAntiDice = priorAntiDiceSize < oldTestCasesAntiDice.size() ? priorAntiDiceSize : oldTestCasesAntiDice.size();
                int mAntiDice = 0;
                ArrayList<Integer> foundTPAntiDice = new ArrayList<Integer>();
                for (int i = 0; i < matrixFaultsAntiDice.get(0).size(); i++) {
                    for (int j = 0; j < nAntiDice; j++) {
                        if (matrixFaultsAntiDice.get(j).get(i)) {
                            foundTPAntiDice.add(j);
                            mAntiDice += 1;
                            break;
                        }
                    }
                }
                int upAntiDice = 0;
                for (int i = 0; i < foundTPAntiDice.size(); i++) {
                    upAntiDice += (chosenPriorAntiDice.indexOf(foundTPAntiDice.get(i)) + 1);
                }
                float APFDAntiDice = 1.0f - ((upAntiDice * 1.0f / (mAntiDice * nAntiDice)) + (1.0f / (2.0f * nAntiDice)));

                System.out.println("\nAPFD data:");
                System.out.println("n = " + nAntiDice);
                System.out.println("m = " + mAntiDice);
                System.out.println("found index: " + foundTPAntiDice);
                System.out.println("up = " + upAntiDice);
                System.out.println("APFD = 1 - ((up / (m * n)) + (1 / (2 * n)))");
                System.out.println("APFD = 1 - ((" + upAntiDice + " / (" + mAntiDice + " * " + nAntiDice + ")) + (1 / (2 * " + nAntiDice + ")))");
                System.out.println("APFD = " + APFDAntiDice);
                
                String txtIsLocalAntiDice = isLocal ? "Local" : "Global";
                txtAPFD += "Anti-Dice - " + txtIsLocalAntiDice + " Distance\n";
                txtAPFD += "APFD = " + Func.float_df.format(APFDAntiDice) + "\n";

                DissimilarityPage.txtAPFD.setText(txtAPFD);
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return output;
    }
    
    private ArrayList<Integer> getUnion(ArrayList<Integer> t1, ArrayList<Integer> t2) {
        ArrayList<Integer> p = new ArrayList<Integer>();
        try {
            for (int i = 0; i < t1.size(); i++) {
                p.add(t1.get(i));
            }
            for (int i = 0; i < t2.size(); i++) {
                boolean isFound = false;
                for (int j = 0; j < t1.size(); j++) {
                    if (t2.get(i) == t1.get(j)) {
                        isFound = true;
                        break;
                    }
                }
                if (isFound == false) {
                    p.add(t2.get(i));
                }
            }
        } catch (Exception e) {
            p.removeAll(p);
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return p;
    }
}
