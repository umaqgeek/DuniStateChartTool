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
public class JaccardDistanceAlgo {
    
    private static String FILE_LOCAL = "APFD/jaccard_local.txt";
    private static String FILE_GLOBAL = "APFD/jaccard_global.txt";
    
    public static String getResult(boolean isLocal, ArrayList<ArrayList<Integer>> testCases, boolean isSaved, boolean isCompared) {
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
            if (isLocal) {
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
            } else {
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
            }
            
            if (isSaved) {
                if (isLocal) {
                    Func.saveToTxt(JaccardDistanceAlgo.FILE_LOCAL, outfileLocal, false);
                } else {
                    Func.saveToTxt(JaccardDistanceAlgo.FILE_GLOBAL, outfileGlobal, false);
                }
            }
            
            int priorSize = isLocal ? prior.size() : gprior.size();
            ArrayList<Integer> chosenPrior = new ArrayList<Integer>();
            if (isLocal) {
                chosenPrior.addAll(prior);
            } else {
                chosenPrior.addAll(gprior);
            }
            
            if (isCompared) {
                System.out.println("\ncurrent testCases:");
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
                Func func = new Func();
                ArrayList<ArrayList<Integer>> oldTestCases = func.getPathsFromTxt(JaccardDistanceAlgo.FILE_LOCAL);
                if (oldTestCases.size() <= 0) {
                    return output;
                }
                System.out.println("\nold testCases:");
                for (int i = 0; i < oldTestCases.size(); i++) {
                    System.out.print(oldTestCases.get(i).get(oldTestCases.get(i).size() - 1) + ":");
                    for (int j = 0; j < oldTestCases.get(i).size() - 1; j++) {
                        System.out.print(oldTestCases.get(i).get(j));
                        if (j != oldTestCases.get(i).size() - 2) {
                            System.out.print("-");
                        }
                    }
                    System.out.println("");
                }

                // init fault matrix.
                ArrayList<ArrayList<Boolean>> matrixFaults = new ArrayList<ArrayList<Boolean>>();
                for (int i = 0; i < priorSize && i < oldTestCases.size(); i++) {
                    ArrayList<Boolean> mf = new ArrayList<Boolean>();
                    for (int j = 0; j < 10; j++) {
                        mf.add(false);
                    }
                    matrixFaults.add(mf);
                }

                // calculate fault in each test path.
                for (int i = 0; i < priorSize && i < oldTestCases.size(); i++) {
                    ArrayList<Integer> T1 = new ArrayList<Integer>();
                    T1.addAll(testCases.get(i));
                    ArrayList<Integer> T1x = new ArrayList<Integer>();
                    T1x.addAll(testCases.get(i));
                    ArrayList<Integer> T2 = new ArrayList<Integer>();
                    T2.addAll(oldTestCases.get(i));
                    T1x.retainAll(T2);
                    JaccardDistanceAlgo jda = new JaccardDistanceAlgo();
                    ArrayList<Integer> T2x = jda.getUnion(T1, T2);
                    int c_sama = T1x.size();
                    int c_union = T2x.size();
                    int fault = (int) Math.ceil((1.0 - (c_sama * 1.0 / c_union)) * 10);
                    fault = (fault - 1) < 1 ? 1 : fault;
                    fault = fault > matrixFaults.get(0).size() ? matrixFaults.get(0).size() : fault;
                    matrixFaults.get(i).set((fault - 1), true);
                }

                System.out.println("\nFault Matrix:");
                System.out.println("---------------------------------------------");
                System.out.println("    | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |10 |");
                System.out.println("---------------------------------------------");
                for (int i = 0; i < matrixFaults.size(); i++) {
                    System.out.print("TP" + Func.getFormatInteger((i + 1) + "", 2) + "|");
                    for (int j = 0; j < matrixFaults.get(i).size(); j++) {
                        char star = matrixFaults.get(i).get(j) ? '*' : ' ';
                        System.out.print(" " + star + " |");
                    }
                    System.out.println("");
                }
                System.out.println("---------------------------------------------");

                int n = priorSize;
                int m = 0;
                ArrayList<Integer> foundTP = new ArrayList<Integer>();
                for (int i = 0; i < matrixFaults.get(0).size(); i++) {
                    for (int j = 0; j < n; j++) {
                        if (matrixFaults.get(j).get(i)) {
                            foundTP.add(j);
                            m += 1;
                            break;
                        }
                    }
                }
                int up = 0;
                for (int i = 0; i < foundTP.size(); i++) {
                    up += (chosenPrior.indexOf(foundTP.get(i)) + 1);
                }
                float APFD = 1.0f - ((up * 1.0f / (m * n)) + (1.0f / (2.0f * n)));

                System.out.println("\nAPFD data:");
                System.out.println("n = " + n);
                System.out.println("m = " + m);
                System.out.println("found index: " + foundTP);
                System.out.println("up = " + up);
                System.out.println("APFD = 1 - ((up / (m * n)) + (1 / (2 * n)))");
                System.out.println("APFD = 1 - ((" + up + " / (" + m + " * " + n + ")) + (1 / (2 * " + n + ")))");
                System.out.println("APFD = " + APFD);

                DissimilarityPage.txtAPFD.setText("APFD = " + APFD);
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
