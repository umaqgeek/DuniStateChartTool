/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.NSGA2Algo;
import controllers.PSOAlgo;
import controllers.PureRandomAlgo;
import controllers.SPEA2Algo;
import controllers.TestSuiteController;
import helpers.Func;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author umar
 */
public class TestSuite02 extends javax.swing.JFrame {

    /**
     * Creates new form TestSuite02
     */
    public TestSuite02() {
        initComponents();
        
        String output = "";
        
        final int numberAlgo = 3;
        final int numberDoors = 5;
        final int numberWindowsPerDoor = 50;
        int numberLoop = numberDoors * numberWindowsPerDoor / numberAlgo + 1;
        ArrayList<ArrayList<Object>> slidingWindows = new ArrayList<ArrayList<Object>>();
        
        output += "FIR value:\n";
        
        for (int w = 0; w < numberLoop; w++) {
            
            TestSuiteController.clearSimpleParents();

            ArrayList<Properties> arrProps = TestSuiteController.parents;
            for (int i = 0; i < arrProps.size(); i++) {

                ArrayList<Object> singleParent = new ArrayList<Object>();

                String strNumberOfPath = "0.00";
                try {
                    float numberOfPath = Float.parseFloat((String) arrProps.get(i).get(Func.TOTAL_NUMBER_PATH));
                    strNumberOfPath = Func.df.format(numberOfPath);
                } catch (Exception e) {
                }

                String strExecTime = "0.00";
                try {
                    float execTime = Float.parseFloat((String) arrProps.get(i).get(Func.TOTAL_TIME_EXECUTION));
                    strExecTime = Func.df.format(execTime);
                } catch (Exception e) {
                }

                String strTransCoverage = "0.00";
                try {
                    float transCoverage = Float.parseFloat((String) arrProps.get(i).get(Func.TOTAL_TRANS_COVERAGE));
                    strTransCoverage = Func.df.format(transCoverage);
                } catch (Exception e) {
                }

                String strTransPairCoverage = "0.00";
                try {
                    float transPairCoverage = Float.parseFloat((String) arrProps.get(i).get(Func.TOTAL_TRANS_PAIR_COVERAGE));
                    strTransPairCoverage = Func.df.format(transPairCoverage);
                } catch (Exception e) {
                }

                singleParent.add(strNumberOfPath);
                singleParent.add(strExecTime);
                singleParent.add(strTransCoverage);
                singleParent.add(strTransPairCoverage);

                singleParent.add(arrProps.get(i).get(Func.ARR_PATHS));

                singleParent.add(Integer.MAX_VALUE);

                TestSuiteController.simpleParents.add(singleParent);
            }

            for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
                ArrayList<Object> singleParent = TestSuiteController.simpleParents.get(i);

                ArrayList<Object> singleParent2 = new ArrayList<Object>();
                singleParent2.addAll(singleParent);
                ArrayList<ArrayList<Integer>> oldArr2 = (ArrayList<ArrayList<Integer>>) singleParent2.get(4);
                ArrayList<ArrayList<Integer>> newArr2 = new ArrayList<ArrayList<Integer>>();
                for (int j = 0; j < oldArr2.size(); j++) {
                    ArrayList<Integer> newArrDetail = new ArrayList<Integer>();
                    for (int k = 0; k < oldArr2.get(j).size(); k++) {
                        newArrDetail.add(oldArr2.get(j).get(k));
                    }
                    newArr2.add(newArrDetail);
                }
                singleParent2.set(4, newArr2);
                TestSuiteController.simpleParents2.add(singleParent2);

                ArrayList<Object> singleParent3 = new ArrayList<Object>();
                singleParent3.addAll(singleParent);
                ArrayList<ArrayList<Integer>> oldArr3 = (ArrayList<ArrayList<Integer>>) singleParent3.get(4);
                ArrayList<ArrayList<Integer>> newArr3 = new ArrayList<ArrayList<Integer>>();
                for (int j = 0; j < oldArr3.size(); j++) {
                    ArrayList<Integer> newArrDetail = new ArrayList<Integer>();
                    for (int k = 0; k < oldArr3.get(j).size(); k++) {
                        newArrDetail.add(oldArr3.get(j).get(k));
                    }
                    newArr3.add(newArrDetail);
                }
                singleParent3.set(4, newArr3);
                TestSuiteController.simpleParents3.add(singleParent3);
            }

            // run NSGA2 process
            long timeNSGA = NSGA2Algo.mainProcess();

            // run SPEA2 process
            long timeSPEA = SPEA2Algo.mainProcess();

            // run PSO process
            long timePSO = PSOAlgo.mainProcess();
            
            output += "\nLoop #"+(w+1)+"\n";
            output += "NSGA2: (" + NSGA2Algo.numPathParents + " - " + NSGA2Algo.numPathOffsprings + ") / " + NSGA2Algo.numPathParents + " = " + NSGA2Algo.valueFIR + " [" + (timeNSGA * 1.0 / 1000) + "s]\n";
            output += "SPEA2: (" + SPEA2Algo.numPathParents + " - " + SPEA2Algo.numPathOffsprings + ") / " + SPEA2Algo.numPathParents + " = " + SPEA2Algo.valueFIR + " [" + (timeSPEA * 1.0 / 1000) + "s]\n";
            output += "PSO  : (" + PSOAlgo.numPathParents + " - " + PSOAlgo.numPathOffsprings + ") / " + PSOAlgo.numPathParents + " = " + PSOAlgo.valueFIR + " [" + (timePSO * 1.0 / 1000) + "s]\n";
            
            ArrayList<Object> slidingWindowNSGA2 = new ArrayList<Object>();
            slidingWindowNSGA2.add("NSGA2 ("+(w+1)+")"); // 0
            slidingWindowNSGA2.add(NSGA2Algo.valueFIR); // 1
            slidingWindowNSGA2.add(timeNSGA*1.0/1000); // 2
            ArrayList<Object> so = new ArrayList<Object>();
            so.addAll(NSGA2Algo.bestTestSuiteOffspring);
            slidingWindowNSGA2.add(so); // 3
            slidingWindowNSGA2.add("|"); // 4
            ArrayList<Object> sp = new ArrayList<Object>();
            sp.addAll(NSGA2Algo.bestTestSuiteParent);
            slidingWindowNSGA2.add(sp); // 5
            slidingWindows.add(slidingWindowNSGA2);
            
            ArrayList<Object> slidingWindowSPEA2 = new ArrayList<Object>();
            slidingWindowSPEA2.add("SPEA2 ("+(w+1)+")");
            slidingWindowSPEA2.add(SPEA2Algo.valueFIR);
            slidingWindowSPEA2.add(timeSPEA*1.0/1000);
            ArrayList<Object> so2 = new ArrayList<Object>();
            so2.addAll(SPEA2Algo.bestTestSuiteOffspring);
            slidingWindowSPEA2.add(so2);
            slidingWindowSPEA2.add("|");
            ArrayList<Object> sp2 = new ArrayList<Object>();
            sp2.addAll(SPEA2Algo.bestTestSuiteParent);
            slidingWindowSPEA2.add(sp2);
            slidingWindows.add(slidingWindowSPEA2);
            
            ArrayList<Object> slidingWindowPSO = new ArrayList<Object>();
            slidingWindowPSO.add("PSO ("+(w+1)+")");
            slidingWindowPSO.add(PSOAlgo.valueFIR);
            slidingWindowPSO.add(timePSO*1.0/1000);
            ArrayList<Object> so3 = new ArrayList<Object>();
            so3.addAll(PSOAlgo.bestTestSuiteOffspring);
            slidingWindowPSO.add(so3);
            slidingWindowPSO.add("|");
            ArrayList<Object> sp3 = new ArrayList<Object>();
            sp3.addAll(PSOAlgo.bestTestSuiteParent);
            slidingWindowPSO.add(sp3);
            slidingWindows.add(slidingWindowPSO);
        }
        
        // sort sliding windows from fastest to slowest.
        slidingWindows = TestSuite02.sortSlidingWindows(slidingWindows);
        
        ArrayList<ArrayList<Object>> rewardsAll = new ArrayList<ArrayList<Object>>();
        
        ArrayList<Object> bestNSGA2 = new ArrayList<Object>();
        ArrayList<Object> bestSPEA2 = new ArrayList<Object>();
        ArrayList<Object> bestPSO = new ArrayList<Object>();
        
        System.out.println("\nSliding windows:");
        output += "\n\nSliding windows:";
        for (int j = 0; j < numberDoors; j++) {
            System.out.println("\nSliding window #"+(j+1)+":");
            output += "\n\nSliding window #"+(j+1)+":";
            
            ArrayList<Object> rewardsPerDoor = new ArrayList<Object>();
            float rewardsNSGA = 0.00f;
            float numNSGA = 0;
            float timeNSGA = 0.00f;
            float rewardsSPEA = 0.00f;
            float numSPEA = 0;
            float timeSPEA = 0.00f;
            float rewardsPSO = 0.00f;
            float numPSO = 0;
            float timePSO = 0.00f;
            
            for (int i = 0+(numberWindowsPerDoor*j); i < slidingWindows.size() && i < numberWindowsPerDoor*(j+1); i++) {
                System.out.println((i + 1) + ": " + slidingWindows.get(i).get(0) + ", FIR: " + slidingWindows.get(i).get(1) + " [" + slidingWindows.get(i).get(2) + "s]");
                output += "\n" + (i + 1) + ": " + slidingWindows.get(i).get(0) + ", FIR: " + slidingWindows.get(i).get(1) + " [" + slidingWindows.get(i).get(2) + "s]";
                
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("nsga2")) {
                    rewardsNSGA += Float.parseFloat(slidingWindows.get(i).get(1).toString()); // SUM of all FIR
                    timeNSGA += Float.parseFloat(slidingWindows.get(i).get(2).toString()); // SUM of all Time
                    numNSGA += 1; // All counts
                    if (bestNSGA2.isEmpty()) {
                        bestNSGA2.addAll(slidingWindows.get(i));
                    }
                }
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("spea2")) {
                    rewardsSPEA += Float.parseFloat(slidingWindows.get(i).get(1).toString());
                    timeSPEA += Float.parseFloat(slidingWindows.get(i).get(2).toString());
                    numSPEA += 1;
                    if (bestSPEA2.isEmpty()) {
                        bestSPEA2.addAll(slidingWindows.get(i));
                    }
                }
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("pso")) {
                    rewardsPSO += Float.parseFloat(slidingWindows.get(i).get(1).toString());
                    timePSO += Float.parseFloat(slidingWindows.get(i).get(2).toString());
                    numPSO += 1;
                    if (bestPSO.isEmpty()) {
                        bestPSO.addAll(slidingWindows.get(i));
                    }
                }
            }
            
            rewardsPerDoor.add(rewardsNSGA); // 0
            rewardsPerDoor.add(numNSGA); // 1
            rewardsPerDoor.add(rewardsSPEA); // 2
            rewardsPerDoor.add(numSPEA); // 3
            rewardsPerDoor.add(rewardsPSO); // 4
            rewardsPerDoor.add(numPSO); // 5
            rewardsPerDoor.add(slidingWindows.get(0+(numberWindowsPerDoor*j)).get(0)); // 6 - algo name
            rewardsPerDoor.add(slidingWindows.get(0+(numberWindowsPerDoor*j)).get(3)); // 7 - best test suite
            rewardsPerDoor.add(slidingWindows.get(0+(numberWindowsPerDoor*j)).get(2)); // 8 - time
            rewardsPerDoor.add(timeNSGA); // 9 - time nsga
            rewardsPerDoor.add(timeSPEA); // 10 - time spea
            rewardsPerDoor.add(timePSO); // 11 - time pso
            
            rewardsAll.add(rewardsPerDoor);
        }
        
        System.out.println("\nMapped Matrix:");
        for (int i = 0; i < PureRandomAlgo.matrix.length; i++) {
            System.out.print("[");
            for (int j = 0; j < PureRandomAlgo.matrix[i].length; j++) {
                String cell = PureRandomAlgo.matrix[i][j] > 10000 ? "INF" : PureRandomAlgo.matrix[i][j]+"";
                System.out.print(cell + ", ");
            }
            System.out.println("], ");
        }
        
        float NSGAFIRAll = 0.00f;
        float SPEAFIRAll = 0.00f;
        float PSOFIRAll = 0.00f;
        
        float NSGACountAll = 0.0f;
        float SPEACountAll = 0.0f;
        float PSOCountAll = 0.0f;
        
        float NSGATimeAll = 0.00f;
        float SPEATimeAll = 0.00f;
        float PSOTimeAll = 0.00f;
        
        String output2 = "";
        
        System.out.println("\n----------------------");
        System.out.println("Rewards:");
        output2 += "Rewards:";
        for (int i = 0; i < rewardsAll.size(); i++) {
            
            NSGAFIRAll += Float.parseFloat(rewardsAll.get(i).get(0).toString());
            NSGACountAll += Float.parseFloat(rewardsAll.get(i).get(1).toString());
            NSGATimeAll += Float.parseFloat(rewardsAll.get(i).get(9).toString());
            
            SPEAFIRAll += Float.parseFloat(rewardsAll.get(i).get(2).toString());
            SPEACountAll += Float.parseFloat(rewardsAll.get(i).get(3).toString());
            SPEATimeAll += Float.parseFloat(rewardsAll.get(i).get(10).toString());
            
            PSOFIRAll += Float.parseFloat(rewardsAll.get(i).get(4).toString());
            PSOCountAll += Float.parseFloat(rewardsAll.get(i).get(5).toString());
            PSOTimeAll += Float.parseFloat(rewardsAll.get(i).get(11).toString());
            
            System.out.println("\nWindow #"+(i+1));

            ArrayList<Object> testSuite = (ArrayList<Object>) rewardsAll.get(i).get(7);
            System.out.println("best TS#: "+testSuite);
            ArrayList<ArrayList<Integer>> bestTestSuite = (ArrayList<ArrayList<Integer>>) testSuite.get(4);
            
            // define which one the best test path.
            ArrayList<Integer> bestTestPath = new ArrayList<Integer>();
            int lowestCost = Integer.MAX_VALUE;
            for (int j = 0; j < bestTestSuite.size(); j++) {
                int cost = calcPathCost(PureRandomAlgo.matrix, bestTestSuite.get(j));
                if (cost < lowestCost) {
                    lowestCost = cost;
                    bestTestPath.addAll(bestTestSuite.get(j));
                }
            }
            
            System.out.println("1. Best test suite       : " + bestTestSuite);
            System.out.println("2. Best test path        : " + bestTestPath + ", cost = " + lowestCost);
            System.out.println("3. Size of test suite    : " + bestTestSuite.size());
            System.out.println("4. Time execution        : " + rewardsAll.get(i).get(8) + "s");
            System.out.println("5. Best running heuristic: " + rewardsAll.get(i).get(6).toString().split(" ")[0]);
            
            output2 += "\n\nWindow #" + (i+1);
            output2 += "\n1. Best test suite       : " + bestTestSuite;
            output2 += "\n2. Best test path        : " + bestTestPath + ", cost = " + lowestCost;
            output2 += "\n3. Size of test suite    : " + bestTestSuite.size();
            output2 += "\n4. Time execution        : " + rewardsAll.get(i).get(8) + "s";
            output2 += "\n5. Best running heuristic: " + rewardsAll.get(i).get(6).toString().split(" ")[0];
        }
        
        float alpha = 0.99f;
        
        /**
         * START Calculate F1
         */
        
        String outputF1 = "";
        
        float nsgaTime = Float.parseFloat(bestNSGA2.get(2).toString());
        nsgaTime = nsgaTime <= 0.000f ? 0.001f : nsgaTime;
        ArrayList<Object> nsgaParent = (ArrayList<Object>) bestNSGA2.get(3);
        ArrayList<Object> nsgaOffspring = (ArrayList<Object>) bestNSGA2.get(5);
        nsgaParent = Func.reCalculateFitness(nsgaParent, nsgaTime);
        nsgaOffspring = Func.reCalculateFitness(nsgaOffspring, nsgaTime);
        bestNSGA2.set(3, nsgaParent);
        bestNSGA2.set(5, nsgaOffspring);
        
        float speaTime = Float.parseFloat(bestSPEA2.get(2).toString());
        speaTime = speaTime <= 0.000f ? 0.001f : speaTime;
        ArrayList<Object> speaParent = (ArrayList<Object>) bestSPEA2.get(3);
        ArrayList<Object> speaOffspring = (ArrayList<Object>) bestSPEA2.get(5);
        speaParent = Func.reCalculateFitness(speaParent, speaTime);
        speaOffspring = Func.reCalculateFitness(speaOffspring, speaTime);
        bestSPEA2.set(3, speaParent);
        bestSPEA2.set(5, speaOffspring);
        
        float psoTime = Float.parseFloat(bestPSO.get(2).toString());
        psoTime = psoTime <= 0.000f ? 0.001f : psoTime;
        ArrayList<Object> psoParent = (ArrayList<Object>) bestPSO.get(3);
        ArrayList<Object> psoOffspring = (ArrayList<Object>) bestPSO.get(5);
        psoParent = Func.reCalculateFitness(psoParent, psoTime);
        psoOffspring = Func.reCalculateFitness(psoOffspring, psoTime);
        bestPSO.set(3, psoParent);
        bestPSO.set(5, psoOffspring);
        
        int NSGAPathSizeParent = (int) Float.parseFloat(nsgaParent.get(0).toString());
        int NSGAPathSizeOffspring = (int) Float.parseFloat(nsgaOffspring.get(0).toString());
        float NSGAAlphaPath = (NSGAPathSizeParent - NSGAPathSizeOffspring) >= 0 ? alpha : alpha - 0.01f;
        float NSGAF1PathSize = Math.abs(NSGAPathSizeParent - NSGAPathSizeOffspring) / nsgaTime * NSGAAlphaPath;
        float NSGAtransCoverParent = Float.parseFloat(nsgaParent.get(2).toString());
        float NSGAtransCoverOffspring = Float.parseFloat(nsgaOffspring.get(2).toString());
        float NSGAAlphaTransCover = (NSGAtransCoverParent - NSGAtransCoverOffspring) >= 0 ? alpha : alpha - 0.01f;
        float NSGAF1TransCover = Math.abs(NSGAtransCoverParent - NSGAtransCoverOffspring) / nsgaTime * NSGAAlphaTransCover;
        float NSGAtransCoverPairParent = Float.parseFloat(nsgaParent.get(3).toString());
        float NSGAtransCoverPairOffspring = Float.parseFloat(nsgaOffspring.get(3).toString());
        float NSGAAlphaTransPairCover = (NSGAtransCoverPairParent - NSGAtransCoverPairOffspring) >= 0 ? alpha : alpha - 0.01f;
        float NSGAF1TransCoverPairNSGA = Math.abs(NSGAtransCoverPairParent - NSGAtransCoverPairOffspring) / nsgaTime * NSGAAlphaTransPairCover;
        float NSGAF1Avg = (NSGAF1PathSize + NSGAF1TransCover + NSGAF1TransCoverPairNSGA) / 3;
        
        int SPEAPathSizeParent = (int) Float.parseFloat(speaParent.get(0).toString());
        int SPEAPathSizeOffspring = (int) Float.parseFloat(speaOffspring.get(0).toString());
        float SPEAAlphaPath = (SPEAPathSizeParent - SPEAPathSizeOffspring) >= 0 ? alpha : alpha - 0.01f;
        float SPEAF1PathSize = Math.abs(SPEAPathSizeParent - SPEAPathSizeOffspring) / speaTime * SPEAAlphaPath;
        float SPEAtransCoverParent = Float.parseFloat(speaParent.get(2).toString());
        float SPEAtransCoverOffspring = Float.parseFloat(speaOffspring.get(2).toString());
        float SPEAAlphaTransCover = (SPEAtransCoverParent - SPEAtransCoverOffspring) >= 0 ? alpha : alpha - 0.01f;
        float SPEAF1TransCover = Math.abs(SPEAtransCoverParent - SPEAtransCoverOffspring) / speaTime * SPEAAlphaTransCover;
        float SPEAtransCoverPairParent = Float.parseFloat(speaParent.get(3).toString());
        float SPEAtransCoverPairOffspring = Float.parseFloat(speaOffspring.get(3).toString());
        float SPEAAlphaTransPairCover = (SPEAtransCoverPairParent - SPEAtransCoverPairOffspring) >= 0 ? alpha : alpha - 0.01f;
        float SPEAF1TransCoverPairSPEA = Math.abs(SPEAtransCoverPairParent - SPEAtransCoverPairOffspring) / speaTime * SPEAAlphaTransPairCover;
        float SPEAF1Avg = (SPEAF1PathSize + SPEAF1TransCover + SPEAF1TransCoverPairSPEA) / 3;
        
        int PSOPathSizeParent = (int) Float.parseFloat(psoParent.get(0).toString());
        int PSOPathSizeOffspring = (int) Float.parseFloat(psoOffspring.get(0).toString());
        float PSOAlphaPath = (PSOPathSizeParent - PSOPathSizeOffspring) >= 0 ? alpha : alpha - 0.01f;
        float PSOF1PathSize = Math.abs(PSOPathSizeParent - PSOPathSizeOffspring) / psoTime * PSOAlphaPath;
        float PSOtransCoverParent = Float.parseFloat(psoParent.get(2).toString());
        float PSOtransCoverOffspring = Float.parseFloat(psoOffspring.get(2).toString());
        float PSOAlphaTransCover = (PSOtransCoverParent - PSOtransCoverOffspring) >= 0 ? alpha : alpha - 0.01f;
        float PSOF1TransCover = Math.abs(PSOtransCoverParent - PSOtransCoverOffspring) / psoTime * PSOAlphaTransCover;
        float PSOtransCoverPairParent = Float.parseFloat(psoParent.get(3).toString());
        float PSOtransCoverPairOffspring = Float.parseFloat(psoOffspring.get(3).toString());
        float PSOAlphaTransPairCover = (PSOtransCoverPairParent - PSOtransCoverPairOffspring) >= 0 ? alpha : alpha - 0.01f;
        float PSOF1TransCoverPairPSO = Math.abs(PSOtransCoverPairParent - PSOtransCoverPairOffspring) / psoTime * PSOAlphaTransPairCover;
        float PSOF1Avg = (PSOF1PathSize + PSOF1TransCover + PSOF1TransCoverPairPSO) / 3;
        
        System.out.println("\n--------------------------------------------");
        System.out.println("F1 results:");
        System.out.println("Formula: (Parent - Offspring) / Time * Alpha");
        outputF1 += "F1 results:\n";
        outputF1 += "Formula: (Parent - Offspring) / Time * Alpha\n";
        
        System.out.println("\nNSGA:");
        System.out.println("Path Size: (" + NSGAPathSizeParent + " - " + NSGAPathSizeOffspring + ") / " + nsgaTime + " * " + NSGAAlphaPath + " = " + NSGAF1PathSize);
        System.out.println("Transition Coverage: (" + NSGAtransCoverParent + " - " + NSGAtransCoverOffspring + ") / " + nsgaTime + " * " + NSGAAlphaTransCover + " = " + NSGAF1TransCover);
        System.out.println("Transition Pair Coverage: (" + NSGAtransCoverPairParent + " - " + NSGAtransCoverPairOffspring + ") / " + nsgaTime + " * " + NSGAAlphaTransPairCover + " = " + NSGAF1TransCoverPairNSGA);
        System.out.println("TOTAL Average: " + NSGAF1Avg);
        outputF1 += "\nNSGA:\n";
        outputF1 += "Path Size: (" + NSGAPathSizeParent + " - " + NSGAPathSizeOffspring + ") / " + nsgaTime + " * " + NSGAAlphaPath + " = " + NSGAF1PathSize + "\n";
        outputF1 += "Transition Coverage: (" + NSGAtransCoverParent + " - " + NSGAtransCoverOffspring + ") / " + nsgaTime + " * " + NSGAAlphaTransCover + " = " + NSGAF1TransCover + "\n";
        outputF1 += "Transition Pair Coverage: (" + NSGAtransCoverPairParent + " - " + NSGAtransCoverPairOffspring + ") / " + nsgaTime + " * " + NSGAAlphaTransPairCover + " = " + NSGAF1TransCoverPairNSGA + "\n";
        outputF1 += "TOTAL Average: " + NSGAF1Avg + "\n";
        
        System.out.println("\nSPEA:");
        System.out.println("Path Size: (" + SPEAPathSizeParent + " - " + SPEAPathSizeOffspring + ") / " + speaTime + " * " + SPEAAlphaPath + " = " + SPEAF1PathSize);
        System.out.println("Transition Coverage: (" + SPEAtransCoverParent + " - " + SPEAtransCoverOffspring + ") / " + speaTime + " * " + SPEAAlphaTransCover + " = " + SPEAF1TransCover);
        System.out.println("Transition Pair Coverage: (" + SPEAtransCoverPairParent + " - " + SPEAtransCoverPairOffspring + ") / " + speaTime + " * " + SPEAAlphaTransPairCover + " = " + SPEAF1TransCoverPairSPEA);
        System.out.println("TOTAL Average: " + SPEAF1Avg);
        outputF1 += "\nSPEA:\n";
        outputF1 += "Path Size: (" + SPEAPathSizeParent + " - " + SPEAPathSizeOffspring + ") / " + speaTime + " * " + SPEAAlphaPath + " = " + SPEAF1PathSize + "\n";
        outputF1 += "Transition Coverage: (" + SPEAtransCoverParent + " - " + SPEAtransCoverOffspring + ") / " + speaTime + " * " + SPEAAlphaTransCover + " = " + SPEAF1TransCover + "\n";
        outputF1 += "Transition Pair Coverage: (" + SPEAtransCoverPairParent + " - " + SPEAtransCoverPairOffspring + ") / " + speaTime + " * " + SPEAAlphaTransPairCover + " = " + SPEAF1TransCoverPairSPEA + "\n";
        outputF1 += "TOTAL Average: " + SPEAF1Avg + "\n";
        
        System.out.println("\nPSO:");
        System.out.println("Path Size: (" + PSOPathSizeParent + " - " + PSOPathSizeOffspring + ") / " + psoTime + " * " + PSOAlphaPath + " = " + PSOF1PathSize);
        System.out.println("Transition Coverage: (" + PSOtransCoverParent + " - " + PSOtransCoverOffspring + ") / " + psoTime + " * " + PSOAlphaTransCover + " = " + PSOF1TransCover);
        System.out.println("Transition Pair Coverage: (" + PSOtransCoverPairParent + " - " + PSOtransCoverPairOffspring + ") / " + psoTime + " * " + PSOAlphaTransPairCover + " = " + PSOF1TransCoverPairPSO);
        System.out.println("TOTAL Average: " + PSOF1Avg);
        outputF1 += "\nPSO:\n";
        outputF1 += "Path Size: (" + PSOPathSizeParent + " - " + PSOPathSizeOffspring + ") / " + psoTime + " * " + PSOAlphaPath + " = " + PSOF1PathSize + "\n";
        outputF1 += "Transition Coverage: (" + PSOtransCoverParent + " - " + PSOtransCoverOffspring + ") / " + psoTime + " * " + PSOAlphaTransCover + " = " + PSOF1TransCover + "\n";
        outputF1 += "Transition Pair Coverage: (" + PSOtransCoverPairParent + " - " + PSOtransCoverPairOffspring + ") / " + psoTime + " * " + PSOAlphaTransPairCover + " = " + PSOF1TransCoverPairPSO + "\n";
        outputF1 += "TOTAL Average: " + PSOF1Avg + "\n";
        
        /**
         * END Calculate F1
         */
        
        /**
         * START Calculate F2
         */
        
        String outputF2 = "";
        
        float NSGAFIRAvg = NSGAFIRAll / NSGACountAll;
        float NSGATimeAvg = NSGATimeAll / NSGACountAll;
        float NSGAAlpha = NSGAFIRAvg >= 0 ? alpha : alpha - 0.01f;
        float NSGAAvgFIR = NSGAFIRAvg / NSGATimeAvg * NSGAAlpha;
        
        float SPEAFIRAvg = SPEAFIRAll / SPEACountAll;
        float SPEATimeAvg = SPEATimeAll / SPEACountAll;
        float SPEAAlpha = SPEAFIRAvg >= 0 ? alpha : alpha - 0.01f;
        float SPEAAvgFIR = SPEAFIRAvg / SPEATimeAvg * SPEAAlpha;
        
        float PSOFIRAvg = PSOFIRAll / PSOCountAll;
        float PSOTimeAvg = PSOTimeAll / PSOCountAll;
        float PSOAlpha = PSOFIRAvg >= 0 ? alpha : alpha - 0.01f;
        float PSOAvgFIR = PSOFIRAvg / PSOTimeAvg * PSOAlpha;
        
        System.out.println("\n--------------------------------------------");
        System.out.println("F2 results:");
        System.out.println("Formula FIR: Avg_FIR / Avg_Time * Alpha");
        outputF2 += "F2 results:\n";
        outputF2 += "Formula FIR: Avg_FIR / Avg_Time * Alpha\n";
        
        System.out.println("");
        System.out.println("NSGA: " + NSGAFIRAvg + " / " + NSGATimeAvg + " * " + NSGAAlpha + " = " + NSGAAvgFIR);
        System.out.println("SPEA: " + SPEAFIRAvg + " / " + SPEATimeAvg + " * " + SPEAAlpha + " = " + SPEAAvgFIR);
        System.out.println("PSO: " + PSOFIRAvg + " / " + PSOTimeAvg + " * " + PSOAlpha + " = " + PSOAvgFIR);
        outputF2 += "\n";
        outputF2 += "NSGA: " + NSGAFIRAvg + " / " + NSGATimeAvg + " * " + NSGAAlpha + " = " + NSGAAvgFIR + "\n";
        outputF2 += "SPEA: " + SPEAFIRAvg + " / " + SPEATimeAvg + " * " + SPEAAlpha + " = " + SPEAAvgFIR + "\n";
        outputF2 += "PSO: " + PSOFIRAvg + " / " + PSOTimeAvg + " * " + PSOAlpha + " = " + PSOAvgFIR + "\n";
        
        /**
         * END Calculate F2
         */
        
        /**
         * START Calculate F3
         */
        
        String outputF3 = "";
        
        float NSGAF3 = (1 - alpha) * NSGATimeAll;
        float SPEAF3 = (1 - alpha) * SPEATimeAll;
        float PSOF3 = (1 - alpha) * PSOTimeAll;
        
        System.out.println("\n--------------------------------------------");
        System.out.println("F3 results:");
        System.out.println("Formula F3: (1 - alpha) * Time_All");
        outputF3 += "F3 results:\n";
        outputF3 += "Formula F3: (1 - alpha) * Time_All\n";
        
        System.out.println("");
        System.out.println("NSGA: (1 - " + alpha + ") * " + NSGATimeAll + " = " + NSGAF3);
        System.out.println("SPEA: (1 - " + alpha + ") * " + SPEATimeAll + " = " + SPEAF3);
        System.out.println("PSO: (1 - " + alpha + ") * " + PSOTimeAll + " = " + PSOF3);
        outputF3 += "\n";
        outputF3 += "NSGA: (1 - " + alpha + ") * " + NSGATimeAll + " = " + NSGAF3 + "\n";
        outputF3 += "SPEA: (1 - " + alpha + ") * " + SPEATimeAll + " = " + SPEAF3 + "\n";
        outputF3 += "PSO: (1 - " + alpha + ") * " + PSOTimeAll + " = " + PSOF3 + "\n";
        
        /**
         * END Calculate F3
         */
        
        /**
         * START Calculate F All
         */
        
        String outputFAll = "";
        
        System.out.println("\n--------------------------------------------");
        System.out.println("F All results:");
        System.out.println("Formula: F1 + F2 + F3");
        outputFAll += "F All results:\n";
        outputFAll += "Formula: F1 + F2 + F3\n";
        
        System.out.println("");
        System.out.println("NSGA: " + NSGAF1Avg + " + " + NSGAAvgFIR + " + " + NSGAF3 + " = " + NSGAF3);
        System.out.println("SPEA: " + SPEAF1Avg + " + " + SPEAAvgFIR + " + " + SPEAF3 + " = " + SPEAF3);
        System.out.println("PSO: " + PSOF1Avg + " + " + PSOAvgFIR + " + " + PSOF3 + " = " + PSOF3);
        outputFAll += "\n";
        outputFAll += "NSGA: " + NSGAF1Avg + " + " + NSGAAvgFIR + " + " + NSGAF3 + " = " + NSGAF3 + "\n";
        outputFAll += "SPEA: " + SPEAF1Avg + " + " + SPEAAvgFIR + " + " + SPEAF3 + " = " + SPEAF3 + "\n";
        outputFAll += "PSO: " + PSOF1Avg + " + " + PSOAvgFIR + " + " + PSOF3 + " = " + PSOF3 + "\n";
        
        /**
         * END Calculate F All
         */
        
        txtSubScreen.setText(output);
        txtMainScreen.setText(output2);
        
        txtF1Screen.setText(outputF1);
        txtF2Screen.setText(outputF2);
        txtF3Screen.setText(outputF3);
        
        txtFAll.setText(outputFAll);
        
        TestSuite01.btnGenerateParOff.setEnabled(true);
    }
    
    private int calcPathCost(int matrx[][], ArrayList<Integer> pathNodes) {
        int totalCost = 0;
        try {
            for (int index = 0; index < pathNodes.size() - 1; index++) {
                int currNode = pathNodes.get(index);
                int nextNode = pathNodes.get(index + 1);
                totalCost += (matrx[currNode-1][nextNode-1]);
            }
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return totalCost;
    }
    
    private static Float getHeuristicSelectionValue(float FRR, float t) {
        float hs = 0.00f;
        try {
            
            float C = 0.50f;
            float upper = 2 * (float) Math.log(t);
            float lower = t;
            float insideSqrt = upper / lower;
            float sqrt = (float) Math.sqrt(insideSqrt);
            hs = t > 0.000 ? FRR + (C * sqrt) : 0.00f;
            
        } catch (Exception e) {
            hs = 0.00f;
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        return hs;
    }
    
    private static ArrayList<ArrayList<Object>> sortSlidingWindows(ArrayList<ArrayList<Object>> sw) {
        try {
            Collections.sort(sw, new Comparator<ArrayList<Object>>() {
                @Override
                public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                    return Float.parseFloat(o1.get(2).toString()) > Float.parseFloat(o2.get(2).toString()) ? 1 : -1;
                }
            });
        } catch (Exception e) {
            if (Func.DEBUG) {
//                for (int i = 0; i < sw.size(); i++) {
//                    System.out.println("ERR #"+i+": "+sw.get(i));
//                }
//                e.printStackTrace();
                System.out.println("Error Tim: "+e.getLocalizedMessage());
            }
        }
        return sw;
    }
    
    public static void viewText(boolean isClear, String text) {
        try {

            String outview = "";

            outview += text;
            outview += "\n";

            if (Func.DEBUG) {
                System.out.print(outview);
            }
            setBox(isClear, outview);

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void setBox(boolean isClear, String output) {
        if (isClear) {
            txtMainScreen.setText(output);
        } else {
            txtMainScreen.setText(txtMainScreen.getText() + output);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMainScreen = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtSubScreen = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnQuit = new javax.swing.JButton();
        btnQuit1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtF1Screen = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtF2Screen = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtF3Screen = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtFAll = new javax.swing.JTextArea();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Test Suite 02");

        txtMainScreen.setEditable(false);
        txtMainScreen.setColumns(20);
        txtMainScreen.setRows(5);
        jScrollPane1.setViewportView(txtMainScreen);

        txtSubScreen.setEditable(false);
        txtSubScreen.setColumns(20);
        txtSubScreen.setRows(5);
        jScrollPane2.setViewportView(txtSubScreen);

        jLabel1.setText("Detail Windows:");

        jLabel2.setText("Summary Rewards:");

        btnQuit.setText("Quit");
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });

        btnQuit1.setText("Back");
        btnQuit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuit1ActionPerformed(evt);
            }
        });

        jLabel3.setText("F1 results:");

        txtF1Screen.setEditable(false);
        txtF1Screen.setColumns(20);
        txtF1Screen.setRows(5);
        jScrollPane3.setViewportView(txtF1Screen);

        jLabel4.setText("F2 results:");

        txtF2Screen.setEditable(false);
        txtF2Screen.setColumns(20);
        txtF2Screen.setRows(5);
        jScrollPane4.setViewportView(txtF2Screen);

        jLabel5.setText("F3 results:");

        txtF3Screen.setEditable(false);
        txtF3Screen.setColumns(20);
        txtF3Screen.setRows(5);
        jScrollPane5.setViewportView(txtF3Screen);

        txtFAll.setEditable(false);
        txtFAll.setColumns(20);
        txtFAll.setRows(5);
        jScrollPane6.setViewportView(txtFAll);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane5)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(253, 253, 253)
                                        .addComponent(btnQuit1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(24, 24, 24)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addGap(422, 422, 422))
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuit1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // TODO add your handling code here:
        dispose();
        System.exit(1);
    }//GEN-LAST:event_btnQuitActionPerformed

    private void btnQuit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuit1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnQuit1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TestSuite02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestSuite02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestSuite02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestSuite02.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestSuite02().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnQuit;
    public static javax.swing.JButton btnQuit1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    public static javax.swing.JTextArea txtF1Screen;
    public static javax.swing.JTextArea txtF2Screen;
    public static javax.swing.JTextArea txtF3Screen;
    public static javax.swing.JTextArea txtFAll;
    public static javax.swing.JTextArea txtMainScreen;
    public static javax.swing.JTextArea txtSubScreen;
    // End of variables declaration//GEN-END:variables
}
