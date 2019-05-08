/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.ArrayList;
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
    
    private static ArrayList<Float> getRandomPosition() {
        ArrayList<Float> result = new ArrayList<Float>();
        try {
            for (int i = 0; i < MAX_MIN.length; i++) {
                result.add(Func.rand.nextFloat() * (MAX_MIN[i][1] + 1));
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
    
    public static void mainProcess() {
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
        
        for (int g = 0; g < T; g++) {
            ArrayList<Float> randomPosition = PSOAlgo.getRandomPosition();
            float velocity = 0.0f;
            for (int i = 0; i < numberParticle; i++) {
                if (i == 0) {
                    prevPersonalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                    prevPersonalBestRange = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), randomPosition);
                    prevPersonalBestIndex = i;
                    if (g == 0) {
                        prevGlobalBestSolution.addAll(TestSuiteController.simpleParents3.get(i));
                        prevGlobalBestRange = PSOAlgo.getRange(TestSuiteController.simpleParents3.get(i), randomPosition);
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
        
        // calculate FIR
        PSOAlgo.numPathOffsprings = PSOAlgo.calcNumberPaths(TestSuiteController.simpleOffsprings3);
        PSOAlgo.valueFIR = (PSOAlgo.numPathParents - PSOAlgo.numPathOffsprings) * 1.0f / PSOAlgo.numPathParents;
        
        /**
         * END PSO
         */
    }
}
