/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import views.TestSuite01;

/**
 *
 * @author umar
 */
public class PSOAlgo {
    
    private static float MAX_MIN[][] = {
        {Float.MAX_VALUE, 0.0f}, // init min, init max
        {Float.MAX_VALUE, 0.0f},
        {Float.MAX_VALUE, 0.0f},
        {Float.MAX_VALUE, 0.0f}
    };
    
    public static float numPathParents = 0.0f;
    public static float numPathOffsprings = 0.0f;
    public static float valueFIR = 0.0f;
    
    private static void setMaxMin() {
        for (int i = 0; i < TestSuiteController.simpleParents3.size(); i++) {
            for (int j = 0; j < MAX_MIN.length; j++) {
                float cell = Float.parseFloat(TestSuiteController.simpleParents3.get(i).get(j).toString());
                MAX_MIN[j][0] = cell <= MAX_MIN[j][0] ? cell : MAX_MIN[j][0];
                MAX_MIN[j][1] = cell >= MAX_MIN[j][1] ? cell : MAX_MIN[j][1];
            }
        }
    }
    
    private static ArrayList<Float> getRandomPosition(float v) {
        ArrayList<Float> result = new ArrayList<Float>();
        try {
            for (int i = 0; i < MAX_MIN.length; i++) {
                result.add(Func.rand.nextFloat() * (MAX_MIN[i][1] + 1) + v);
            }
        } catch (Exception e) {
            result.removeAll(result);
        }
        return result;
    }
    
    private static float getRange(ArrayList<Object> sp, ArrayList<Float> target) {
        float result = 0.0f;
        try {
            for (int j = 0; j < target.size(); j++) {
                float particle = Float.parseFloat(sp.get(j).toString());
                float food = Float.parseFloat(target.get(j).toString());
                result += Math.pow((particle - food), 2);
            }
            result = (float) Math.sqrt(result);
        } catch (Exception e) {
            result = 0.0f;
        }
        return result;
    }
    
    private static int calcNumberPaths(ArrayList<ArrayList<Object>> sp) {
        int total = 0;
        for (int i = 0; i < sp.size(); i++) {
            ArrayList<ArrayList<Integer>> paths = (ArrayList<ArrayList<Integer>>) sp.get(i).get(4);
            for (int j = 0; j < paths.size(); j++) {
                total += 1;
            }
        }
        return total;
    }
    
    private static ArrayList<ArrayList<Object>> sortDistance(ArrayList<ArrayList<Object>> Ftemp) {
        Collections.sort(Ftemp, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return Float.parseFloat(o1.get(7).toString()) > Float.parseFloat(o2.get(7).toString()) ? 1 : -1;
            }
        });
        return Ftemp;
    }
    
    public static long mainProcess() {
        long start_time = System.currentTimeMillis();
        /**
         * START PSO
         */
        
        System.out.println("\nPSO Algorithm Process\n--------------------");
        
        PSOAlgo.numPathParents = PSOAlgo.calcNumberPaths(TestSuiteController.simpleParents3);
        
        PSOAlgo.setMaxMin();
        
        int numberParticle = TestSuiteController.simpleParents3.size();
        int T = Integer.parseInt(TestSuite01.txtNoIteration.getText().toString());
        
        ArrayList<Object> prevPersonalBestSolution = new ArrayList<Object>();
        float prevPersonalBestRange = Float.MAX_VALUE;
        int prevPersonalBestIndex = 0;
        
        float thresholdGlobalMinimum = 0.25f;
        
        ArrayList<Object> prevGlobalBestSolution = new ArrayList<Object>();
        float prevGlobalBestRange = Float.MAX_VALUE;
        int prevGlobalGlobalBestIndex = 0;
        int prevGlobalPersonalBestIndex = 0;
        
        ArrayList<Float> worstRandomPosition =  new ArrayList<Float>();
        float personalWorstRange = 0.00f;
        for (int g = 0; g < T; g++) {
            
            float velocity = 0.0f;
            
            for (int i = 0; i < numberParticle; i++) {
                
                ArrayList<Float> randomPosition = PSOAlgo.getRandomPosition(velocity);
                System.out.println("\nRandom Position g" + g + ": " + randomPosition);
                
                if (i == 0) {
                    prevPersonalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                    prevPersonalBestRange = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), randomPosition);
                    prevPersonalBestIndex = i;
                    if (g == 0) {
                        prevGlobalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                        prevGlobalBestRange = prevPersonalBestRange;
                        prevGlobalGlobalBestIndex = g;
                        prevGlobalPersonalBestIndex = i;
                    } else {
                        if (prevPersonalBestRange < prevGlobalBestRange) {
                            prevGlobalBestSolution.removeAll(prevGlobalBestSolution);
                            prevGlobalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                            prevGlobalBestRange = prevPersonalBestRange;
                            prevGlobalGlobalBestIndex = g;
                            prevGlobalPersonalBestIndex = i;
                        }
                    }
                } else {
                    float distance = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), randomPosition);
                    if (distance < prevPersonalBestRange) {
                        prevPersonalBestSolution.removeAll(prevPersonalBestSolution);
                        prevPersonalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                        prevPersonalBestRange = distance;
                        prevPersonalBestIndex = i;
                        if (distance < prevGlobalBestRange) {
                            prevGlobalBestSolution.removeAll(prevGlobalBestSolution);
                            prevGlobalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                            prevGlobalBestRange = distance;
                            prevGlobalGlobalBestIndex = g;
                            prevGlobalPersonalBestIndex = i;
                        }
                    }
                }
                float distance = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), randomPosition);
                if (distance >= personalWorstRange && distance != Float.POSITIVE_INFINITY) {
                    personalWorstRange = distance;
                    worstRandomPosition.removeAll(worstRandomPosition);
                    worstRandomPosition.addAll(randomPosition);
                }
                
                // adjust velocity
                float inertiaWeight = 0.3f;
                float learningVector = 2.0f;
                float random = Func.rand.nextFloat();
                velocity = (inertiaWeight * velocity) + (learningVector * random * prevPersonalBestRange) * (learningVector * random * prevGlobalBestRange);
            }
            
            System.out.println("\nPersonal Best Solution:");
            System.out.println("Sol g"+g+" #"+prevPersonalBestIndex+": "+prevPersonalBestSolution);
            System.out.println("R   g"+g+" #"+prevPersonalBestIndex+": "+prevPersonalBestRange);
            
            if (prevGlobalBestRange <= thresholdGlobalMinimum) {
                break;
            }
        }
        
        System.out.println("\nGlobal Best Solution:");
        System.out.println("Sol g"+prevGlobalGlobalBestIndex+" #"+prevGlobalPersonalBestIndex+": "+prevGlobalBestSolution);
        System.out.println("R   g"+prevGlobalGlobalBestIndex+" #"+prevGlobalPersonalBestIndex+": "+prevGlobalBestRange);
        
        System.out.println("\nWorst Range: "+personalWorstRange);
        System.out.println("Worst Position: "+worstRandomPosition);
        
        System.out.println("\nBefore sort by distance:");
        for (int i = 0; i < TestSuiteController.simpleParents3.size(); i++) {
            float distance = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), worstRandomPosition);
            TestSuiteController.simpleParents3.get(i).add(i);
            TestSuiteController.simpleParents3.get(i).add(distance);
            System.out.println(i+": "+TestSuiteController.simpleParents3.get(i));
        }
        
        TestSuiteController.simpleParents3 = PSOAlgo.sortDistance(TestSuiteController.simpleParents3);
        
        System.out.println("\nAfter sort by distance:");
        for (int i = 0; i < TestSuiteController.simpleParents3.size(); i++) {
            int index = Integer.parseInt(TestSuiteController.simpleParents3.get(i).get(6).toString());
            System.out.println(index+": "+TestSuiteController.simpleParents3.get(i));
        }
        
        // generate offspring.
        // tournament selection.
        System.out.println("\nTournament selection");
        int rawParentSize = TestSuiteController.simpleParents3.size();
        for (int i = 0; i < rawParentSize; i++) {
            int indexParent1 = Func.rand.nextInt(rawParentSize);
            int indexParent2 = Func.rand.nextInt(rawParentSize);
            do {
                if (indexParent2 == indexParent1) {
                    indexParent2 = Func.rand.nextInt(rawParentSize);
                } else {
                    break;
                }
            } while (true);
            ArrayList<Object> parent1 = TestSuiteController.simpleParents3.get(indexParent1);
            ArrayList<Object> parent2 = TestSuiteController.simpleParents3.get(indexParent2);

            System.out.println("P #" + (i + 1) + ":");
            System.out.println("parent1: index " + indexParent1 + " - " + parent1);
            System.out.println("vs");
            System.out.println("parent2: index " + indexParent2 + " - " + parent2);
            
            // set who win
            int countWinParent1 = 0;
            int countWinParent2 = 0;
            for (int j = 0; j < 4; j++) {
                float op1 = Float.parseFloat(parent1.get(j).toString());
                float op2 = Float.parseFloat(parent2.get(j).toString());
                countWinParent1 = op1 > op2 ? countWinParent1 + 1 : countWinParent1;
                countWinParent2 = op2 > op1 ? countWinParent2 + 1 : countWinParent2;
            }
            ArrayList<Object> pickedMating = new ArrayList<Object>();
            if (countWinParent1 >= countWinParent2) {
                pickedMating.addAll(parent1);
            } else {
                pickedMating.addAll(parent2);
            }
            System.out.println("Winner:\nmating #" + (pickedMating.get(6)) + ": " + pickedMating);

            // insert into mating pools
            TestSuiteController.simpleOffsprings3.add(pickedMating);
        }
        
        System.out.println("\nAfter Tournament Selection & before crossover:");
        for (int i = 0; i < TestSuiteController.simpleOffsprings3.size(); i++) {
            System.out.println(i+": "+TestSuiteController.simpleOffsprings3.get(i));
        }
        
        // collect list of permutations for mutation process.
        for (int i = 0; i < TestSuiteController.simpleParents3.size(); i++) {
            TestSuiteController.possibleMutationPaths3.addAll((ArrayList<ArrayList<Integer>>) TestSuiteController.simpleParents3.get(i).get(4));
        }
        // view all possible path for mutation.
        // sorting the list.
        Collections.sort(TestSuiteController.possibleMutationPaths3, new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                return o1.size() > o2.size() ? 1 : -1;
            }
        });
        // remove duplicates in the list.
        TestSuiteController.possibleMutationPaths3 = Func.removeDuplicates(TestSuiteController.possibleMutationPaths3);
        // view all possible path for mutation.
        System.out.println("\nView all possible path for mutation");
        for (int i = 0; i < TestSuiteController.possibleMutationPaths3.size(); i++) {
            System.out.println(TestSuiteController.possibleMutationPaths3.get(i));
        }
        
        // crossover.
        float crossoverChances = TestSuite01.sliCrossover.getValue() * 1.0f / 100;
        for (int i = 0; i < TestSuiteController.simpleOffsprings3.size(); i += 2) {

            int inext = (i + 1) >= TestSuiteController.simpleOffsprings3.size() ? 0 : (i + 1);

            // takeout genes from chromosome parent 1.
            ArrayList<ArrayList<Integer>> geneP1 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings3.get(i).get(4);
            int numGene1 = (int) (crossoverChances * geneP1.size());
            ArrayList<ArrayList<Integer>> holdGene1 = new ArrayList<ArrayList<Integer>>();
            for (int j = geneP1.size() - 1; j >= 0 && numGene1 > 0; j--, numGene1--) {
                holdGene1.add(geneP1.get(j));
                geneP1.remove(j);
            }

            // takeout genes from chromosome parent 2.
            ArrayList<ArrayList<Integer>> geneP2 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings3.get(inext).get(4);
            int numGene2 = (int) (crossoverChances * geneP2.size());
            ArrayList<ArrayList<Integer>> holdGene2 = new ArrayList<ArrayList<Integer>>();
            for (int j = geneP2.size() - 1; j >= 0 && numGene2 > 0; j--, numGene2--) {
                holdGene2.add(geneP2.get(j));
                geneP2.remove(j);
            }

            // insert the takeout genes from parent 1 into parent 2.
            for (int j = 0; j < holdGene1.size(); j++) {
                geneP2.add(holdGene1.get(j));
            }

            // insert the takeout genes from parent 2 into parent 1.
            for (int j = 0; j < holdGene2.size(); j++) {
                geneP1.add(holdGene2.get(j));
            }

            // refresh after swapping genes parent 1 and parent 2.
            TestSuiteController.simpleOffsprings3.get(i).set(4, geneP1);
            TestSuiteController.simpleOffsprings3.get(inext).set(4, geneP2);
        }
        
        System.out.println("\nAfter crossover & before mutation:");
        for (int i = 0; i < TestSuiteController.simpleOffsprings3.size(); i++) {
            System.out.println(i+": "+TestSuiteController.simpleOffsprings3.get(i));
        }
        
        // mutation.
        float mutationChances = TestSuite01.sliMutation.getValue() * 1.0f / 100;
        for (int i = 0; i < TestSuiteController.simpleOffsprings3.size(); i++) {
            ArrayList<ArrayList<Integer>> oldOffspringPaths = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings3.get(i).get(4);

            int numMutatedGene = (int) (mutationChances * oldOffspringPaths.size());
            ArrayList<Integer> chosenList = new ArrayList<Integer>();
            while (numMutatedGene-- > 0) {
                int randIndex = Func.rand.nextInt(oldOffspringPaths.size());
                do {
                    if (chosenList.contains(randIndex)) {
                        randIndex = Func.rand.nextInt(oldOffspringPaths.size());
                    } else {
                        chosenList.add(randIndex);
                        break;
                    }
                } while (true);

                ArrayList<Integer> oldGene = oldOffspringPaths.get(randIndex);

                int randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths3.size());
                ArrayList<Integer> newGene = TestSuiteController.possibleMutationPaths3.get(randMutateIndex);
//                do {
//                    if (!oldGene.equals(newGene)) {
//                        break;
//                    }
//                    randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths3.size());
//                    newGene = TestSuiteController.possibleMutationPaths3.get(randMutateIndex);
//                } while (true);

                oldOffspringPaths.set(randIndex, newGene);
            }

            TestSuiteController.simpleOffsprings3.get(i).set(4, oldOffspringPaths);
        }
        
        // calculate FIR
        PSOAlgo.numPathOffsprings = PSOAlgo.calcNumberPaths(TestSuiteController.simpleOffsprings3);
        PSOAlgo.valueFIR = (PSOAlgo.numPathParents - PSOAlgo.numPathOffsprings) * 1.0f / PSOAlgo.numPathParents;
        
        /**
         * END PSO
         */
        long end_time = System.currentTimeMillis();
        return (end_time - start_time);
    }
}
