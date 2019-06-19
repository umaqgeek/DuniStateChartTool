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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import views.TestSuite01;

/**
 *
 * @author umar
 */
public class NSGA2Algo {
    
    private static Properties Fprops = new Properties();
    private static Float fmx[] = new Float[]{0.00f, Float.MAX_VALUE};
    private static Float fmy[] = new Float[]{0.00f, Float.MAX_VALUE};
    private static Float fmz1[] = new Float[]{0.00f, Float.MAX_VALUE};
    private static Float fmz2[] = new Float[]{0.00f, Float.MAX_VALUE};
    
    public static float numPathParents = 0.0f;
    public static float numPathOffsprings = 0.0f;
    public static float valueFIR = 0.0f;
    public static ArrayList<Object> bestTestSuiteParent = new ArrayList<Object>();
    public static ArrayList<Object> bestTestSuiteOffspring = new ArrayList<Object>();
    
    private static void setMaxMinM(ArrayList<Object> sp) {
        fmx[0] = Float.parseFloat((String) sp.get(0)) > fmx[0] ? Float.parseFloat((String) sp.get(0)) : fmx[0];
        fmx[1] = Float.parseFloat((String) sp.get(0)) < fmx[1] ? Float.parseFloat((String) sp.get(0)) : fmx[1];
        fmy[0] = Float.parseFloat((String) sp.get(1)) > fmy[0] ? Float.parseFloat((String) sp.get(1)) : fmy[0];
        fmy[1] = Float.parseFloat((String) sp.get(1)) < fmy[1] ? Float.parseFloat((String) sp.get(1)) : fmy[1];
        fmz1[0] = Float.parseFloat((String) sp.get(2)) > fmz1[0] ? Float.parseFloat((String) sp.get(2)) : fmz1[0];
        fmz1[1] = Float.parseFloat((String) sp.get(2)) < fmz1[1] ? Float.parseFloat((String) sp.get(2)) : fmz1[1];
        fmz2[0] = Float.parseFloat((String) sp.get(3)) > fmz2[0] ? Float.parseFloat((String) sp.get(3)) : fmz2[0];
        fmz2[1] = Float.parseFloat((String) sp.get(3)) < fmz2[1] ? Float.parseFloat((String) sp.get(3)) : fmz2[1];
    }
    
    private static void viewAllMaxMinM() {
        System.out.print("fmx: ");
        for (int i = 0; i < fmx.length; i++) {
            System.out.print(fmx[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmy: ");
        for (int i = 0; i < fmy.length; i++) {
            System.out.print(fmy[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmz1: ");
        for (int i = 0; i < fmz1.length; i++) {
            System.out.print(fmz1[i] + ", ");
        }
        System.out.println("");
        System.out.print("fmz2: ");
        for (int i = 0; i < fmz2.length; i++) {
            System.out.print(fmz2[i] + ", ");
        }
        System.out.println("");
    }
    
    private static boolean isNominated(ArrayList<Object> p, ArrayList<Object> q) {
        float px = Float.parseFloat((String) p.get(0));
        float py = Float.parseFloat((String) p.get(1));
        float pz1 = Float.parseFloat((String) p.get(2));
        float pz2 = Float.parseFloat((String) p.get(3));

        float qx = Float.parseFloat((String) q.get(0));
        float qy = Float.parseFloat((String) q.get(1));
        float qz1 = Float.parseFloat((String) q.get(2));
        float qz2 = Float.parseFloat((String) q.get(3));
        
        return ((px<=qx && py<=qy && pz1>=qz1 && pz2>=qz2) && (px<qx || py<qy || pz1>qz1 || pz2>qz2));
    }
    
    private static void setRanks() {
        
        ArrayList<ArrayList<Object>> arrAll = TestSuiteController.simpleParents;
        
        for (int i = 0; i < arrAll.size(); i++) {
            
            ArrayList<Object> p = arrAll.get(i);
            ArrayList<Integer> Sp = new ArrayList<Integer>();
            int np = 0;
            
            for (int j = 0; j < arrAll.size(); j++) {
                if (i != j) {
                    try {
                        
                        ArrayList<Object> q = arrAll.get(j);
                        boolean isNominate = NSGA2Algo.isNominated(p, q);
                        boolean isNominatedBy = NSGA2Algo.isNominated(q, p);
                        
                        if (isNominate) {
                            Sp.add(j+1);
                        } else if (isNominatedBy) {
                            np += 1;
                        }
                        
                    } catch (Exception e) {
                        if (Func.DEBUG) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            
            TestSuiteController.simpleParents.get(i).add(Sp);
            TestSuiteController.simpleParents.get(i).add(np);
            
            if (np == 0) {
                TestSuiteController.simpleParents.get(i).set(5, 1);
            }
        }
        
        // for every F1
        ArrayList<ArrayList<Object>> F1 = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {   
            ArrayList<Object> p = TestSuiteController.simpleParents.get(i);
            if (p.get(5).equals(1)) {
                F1.add(p);
            }
        }
        
        int currentRank = 1;
        
        Fprops.put(Func.KEY_F + currentRank, F1);
        
        int nextRank = currentRank + 1;
        setRank(F1, nextRank);
    }
    
    private static void setRank(ArrayList<ArrayList<Object>> Fn, int nextRank) {
        ArrayList<ArrayList<Object>> Fnext = new ArrayList<ArrayList<Object>>();
        // for every F1, modify the rest to F2
        for (int i = 0; i < Fn.size(); i++) {
            ArrayList<Object> p = Fn.get(i);
            try {
                ArrayList<Integer> qIndexes = (ArrayList<Integer>) p.get(6);
                for (int j = 0; j < qIndexes.size(); j++) {
                    int nomination = (int) TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).get(7);
                    nomination -= 1;
                    TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).set(7, nomination);
                    if (nomination <= 0) {
                        // if nomination if zero, set to rank n+1
                        TestSuiteController.simpleParents.get(qIndexes.get(j) - 1).set(5, nextRank);
                        Fnext.add(TestSuiteController.simpleParents.get(qIndexes.get(j) - 1));
                    }
                }
            } catch (Exception e) {
                if (Func.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
        
        Fprops.put(Func.KEY_F + nextRank, Fnext);
        nextRank += 1;
        
        if (!NSGA2Algo.isAllRanked()) {
            setRank(Fnext, nextRank);
        }
    }
    
    private static boolean isAllRanked() {
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            int rank = (int) TestSuiteController.simpleParents.get(i).get(5);
            if (rank == Integer.MAX_VALUE) {
                return false;
            }
        }
        return true;
    }
    
    private static void setCrowds() {
        for (int i = 1; ; i++) {
            try {
                ArrayList<ArrayList<Object>> F = (ArrayList<ArrayList<Object>>) Fprops.get(Func.KEY_F + i);
                if (F == null) {
                    break;
                }
                // init set all distances = 0.
                for (int j = 0; j < F.size(); j++) {
                    F.get(j).add(0);
                }
                // rules of objective function, m.
                int m[][] = new int[][]{
                    {0, 1, -1},
                    {1, 1, -1},
                    {2, -1, 1},
                    {3, -1, 1}
                };
                // sort column 1.
                F = sortF(F, m[0]);
                
                /**
                 * TODO:
                 * 1. Separate into each cluster based on same column 1.
                 * 2. Sort column 2.
                 * 3. Combine all clusters in one list.
                 * 4. Separate into each cluster based on same column 1 and 2.
                 * 5. Sort column 3.
                 * 6. Combine all clusters in one list.
                 * 7. Separate into each cluster based on same column 1, 2, and 3.
                 * 8. Sort column 4.
                 * 9. Combine all clusters in one list.
                 */
                
                // TODO 1-3
                F = multipleSoftF(F, 1, m);
                // TODO 4-6
                F = multipleSoftF(F, 2, m);
                // TODO 7-9
                F = multipleSoftF(F, 3, m);
                
                if (F.size() > 0 && F.size() <= 2) {
                    for (int j = 0; j < F.size(); j++) {
                        F.get(j).set(8, Float.MAX_VALUE);
                    }
                } else if (F.size() > 2) {
                    F.get(0).set(8, Float.MAX_VALUE);
                    F.get(F.size()-1).set(8, Float.MAX_VALUE);
                    for (int j = 1; j < F.size()-1; j++) {
                        float dist = getDistance(F, j);
                        F.get(j).set(8, dist);
                    }
                }
                
                // put it back into paretos.
                Fprops.put(Func.KEY_F + i, F);
                
            } catch (Exception e) {
                if (Func.DEBUG) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    
    private static ArrayList<ArrayList<Object>> multipleSoftF(ArrayList<ArrayList<Object>> F, int level, int[][] m) {
        // TODO 1/4/7.
        ArrayList<ArrayList<ArrayList<Object>>> clustersLevel1 = getClusters(F, level);
        // TODO 2/5/8.
        for (int j = 0; j < clustersLevel1.size(); j++) {
            ArrayList<ArrayList<Object>> Fcluster = clustersLevel1.get(j);
            Fcluster = sortF(Fcluster, m[level]);
        }
        // TODO 3/6/9.
        F.clear();
        for (int j = 0; j < clustersLevel1.size(); j++) {
            for (int k = 0; k < clustersLevel1.get(j).size(); k++) {
                F.add(clustersLevel1.get(j).get(k));
            }
        }
        return F;
    }
    
    private static ArrayList<ArrayList<ArrayList<Object>>> getClusters(ArrayList<ArrayList<Object>> F, int level) {
        ArrayList<ArrayList<ArrayList<Object>>> clusters = new ArrayList<ArrayList<ArrayList<Object>>>();
        ArrayList<ArrayList<Object>> Ftemp = new ArrayList<ArrayList<Object>>();
        for (int j = 0; j < F.size(); j++) {
            if (j == 0) {
                Ftemp.add(F.get(j));
            } else {
                if (
                        (
                            level == 1 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                        )
                        ||
                        (
                            level == 2 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                                            && (Float.parseFloat((String) F.get(j).get(1))) == (Float.parseFloat((String) F.get(j - 1).get(1))) 
                        )
                        ||
                        (
                            level == 3 && (Float.parseFloat((String) F.get(j).get(0))) == (Float.parseFloat((String) F.get(j - 1).get(0))) 
                                            && (Float.parseFloat((String) F.get(j).get(1))) == (Float.parseFloat((String) F.get(j - 1).get(1))) 
                                            && (Float.parseFloat((String) F.get(j).get(2))) == (Float.parseFloat((String) F.get(j - 1).get(2))) 
                        )
                   ) {
                    Ftemp.add(F.get(j));
                } else {
                    ArrayList<ArrayList<Object>> Ftemp2 = new ArrayList<ArrayList<Object>>();
                    Ftemp2.addAll(Ftemp);
                    clusters.add(Ftemp2);
                    Ftemp.clear();
                    Ftemp.add(F.get(j));
                }
            }
            if (j == F.size() - 1) {
                clusters.add(Ftemp);
            }
        }
        return clusters;
    }
    
    private static ArrayList<ArrayList<Object>> sortF(ArrayList<ArrayList<Object>> Ftemp, int[] m) {
        Collections.sort(Ftemp, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return Float.parseFloat(o1.get(m[0]).toString()) > Float.parseFloat(o2.get(m[0]).toString()) ? m[1] : m[2];
            }
        });
        return Ftemp;
    }
    
    private static float getDistance(ArrayList<ArrayList<Object>> F, int currentIndex) {
        float calc = calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(0)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(0)), 
                fmx[0], 
                fmx[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(1)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(1)), 
                fmy[0], 
                fmy[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(2)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(2)), 
                fmz1[0], 
                fmz1[1]);
        calc += calcDistancePerM(
                Float.parseFloat((String) F.get(currentIndex-1).get(3)), 
                Float.parseFloat((String) F.get(currentIndex+1).get(3)), 
                fmz2[0], 
                fmz2[1]);
        return calc;
    }
    
    private static float calcDistancePerM(float yMax, float yMin, float xMax, float xMin) {
        float up = yMax - yMin;
        up = up < 0 ? up * -1.0f : up;
        float down = xMax - xMin;
        down = down < 0 ? down * -1.0f : down;
        return up * 1.0f / down;
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
    
    public static long mainProcess() {
        long start_time = System.currentTimeMillis();
        /**
         * START NSGA2
         */
        
        System.out.println("\nNSGA2 Algorithm Process\n--------------------");
        
        NSGA2Algo.numPathParents = NSGA2Algo.calcNumberPaths(TestSuiteController.simpleParents);

        NSGA2Algo.setRanks();

        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            ArrayList<Object> singleParent = TestSuiteController.simpleParents.get(i);
            NSGA2Algo.setMaxMinM(singleParent);
        }

        NSGA2Algo.viewAllMaxMinM();

        NSGA2Algo.setCrowds();

        for (int i = 0; i < NSGA2Algo.Fprops.size(); i++) {
            System.out.println("F #" + (i + 1) + ":");
            ArrayList<ArrayList<Object>> F = (ArrayList<ArrayList<Object>>) NSGA2Algo.Fprops.get(Func.KEY_F + (i + 1));
            for (int j = 0; j < F.size(); j++) {
                System.out.println(j + ": " + F.get(j));
            }
        }

        System.out.println("\nParents by Ranks and Crowd Distances:");
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            ArrayList<Object> singleParent = TestSuiteController.simpleParents.get(i);
            System.out.println("TS #" + (i + 1) + ": (R=" + singleParent.get(5) + "), (CD=" + singleParent.get(8) + "), " + singleParent);

            ArrayList<ArrayList<Integer>> parentPaths = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleParents.get(i).get(4);
            for (int j = 0; j < parentPaths.size(); j++) {
                TestSuiteController.possibleMutationPaths1.add(parentPaths.get(j));
            }
        }

        // view all possible path for mutation.
        // sorting the list.
        Collections.sort(TestSuiteController.possibleMutationPaths1, new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                return o1.size() > o2.size() ? 1 : -1;
            }
        });
        // remove duplicates in the list.
        TestSuiteController.possibleMutationPaths1 = Func.removeDuplicates(TestSuiteController.possibleMutationPaths1);
        // view all possible path for mutation.
        System.out.println("\nView all possible path for mutation");
        for (int i = 0; i < TestSuiteController.possibleMutationPaths1.size(); i++) {
            System.out.println(TestSuiteController.possibleMutationPaths1.get(i));
        }

        // generate offspring.
        // tournament selection.
        System.out.println("\nTournament selection");
        int rawParentSize = TestSuiteController.simpleParents.size();
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
            ArrayList<Object> parent1 = TestSuiteController.simpleParents.get(indexParent1);
            ArrayList<Object> parent2 = TestSuiteController.simpleParents.get(indexParent2);

            System.out.println("P #" + (i + 1) + ":");
            System.out.println("parent1: index " + indexParent1 + " - " + parent1);
            System.out.println("vs");
            System.out.println("parent2: index " + indexParent2 + " - " + parent2);

            ArrayList<Object> selectedParent = new ArrayList<Object>();
            int selectedIndexParent = -1;
            if (((int) parent1.get(5)) < ((int) parent2.get(5))) {
                selectedParent.addAll(parent1);
                selectedIndexParent = indexParent1;
            } else if (((int) parent1.get(5)) == ((int) parent2.get(5))) {
                if (((float) parent1.get(8)) > ((float) parent2.get(8))) {
                    selectedParent.addAll(parent1);
                    selectedIndexParent = indexParent1;
                } else if (((float) parent1.get(8)) == ((float) parent2.get(8))) {
                    selectedParent.addAll(parent1);
                    selectedIndexParent = indexParent1;
                } else {
                    selectedParent.addAll(parent2);
                    selectedIndexParent = indexParent2;
                }
            } else {
                selectedParent.addAll(parent2);
                selectedIndexParent = indexParent2;
            }

            // reset offsprings attributes.
//            for (int j = 0; j < selectedParent.size(); j++) {
//                if (j != 4) {
//                    selectedParent.set(j, 0.00);
//                }
//            }
            TestSuiteController.simpleOffsprings.add(selectedParent);

            System.out.println("win parent: index " + selectedIndexParent + " - " + selectedParent + "\n");
        }

        // before crossover.
        System.out.println("Offsprings before crossover.");
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            System.out.println("Offspring #" + (i + 1) + ": " + TestSuiteController.simpleOffsprings.get(i));
        }

        // crossover.
        float crossoverChances = TestSuite01.sliCrossover.getValue() * 1.0f / 100;
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i += 2) {

            int inext = (i + 1) >= TestSuiteController.simpleOffsprings.size() ? 0 : (i + 1);

            // takeout genes from chromosome parent 1.
            ArrayList<ArrayList<Integer>> geneP1 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings.get(i).get(4);
            int numGene1 = (int) (crossoverChances * geneP1.size());
            ArrayList<ArrayList<Integer>> holdGene1 = new ArrayList<ArrayList<Integer>>();
            for (int j = geneP1.size() - 1; j >= 0 && numGene1 > 0; j--, numGene1--) {
                holdGene1.add(geneP1.get(j));
                geneP1.remove(j);
            }

            // takeout genes from chromosome parent 2.
            ArrayList<ArrayList<Integer>> geneP2 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings.get(inext).get(4);
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
            TestSuiteController.simpleOffsprings.get(i).set(4, geneP1);
            TestSuiteController.simpleOffsprings.get(inext).set(4, geneP2);
        }

        // after crossover and before mutation.
        System.out.println("\nOffsprings after crossover and before mutation.");
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            System.out.println("Offspring #" + (i + 1) + ": " + TestSuiteController.simpleOffsprings.get(i));
        }

        // mutation.
        float mutationChances = TestSuite01.sliMutation.getValue() * 1.0f / 100;
        System.out.println("\nOffsprings after mutation");
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            ArrayList<ArrayList<Integer>> oldOffspringPaths = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings.get(i).get(4);

            System.out.println("#" + (i + 1) + ":");
            System.out.println("oldOffspringPaths: " + oldOffspringPaths);

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

                int randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths1.size());
                ArrayList<Integer> newGene = TestSuiteController.possibleMutationPaths1.get(randMutateIndex);
//                do {
//                    if (!oldGene.equals(newGene)) {
//                        break;
//                    }
//                    randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths1.size());
//                    newGene = TestSuiteController.possibleMutationPaths1.get(randMutateIndex);
//                } while (true);

                oldOffspringPaths.set(randIndex, newGene);
            }

            System.out.println("newOffspringPaths: " + oldOffspringPaths);
            TestSuiteController.simpleOffsprings.get(i).set(4, oldOffspringPaths);
        }

        // after crossover and before mutation.
        System.out.println("\nOffsprings after mutation.");
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            System.out.println("Offspring #" + (i + 1) + ": " + TestSuiteController.simpleOffsprings.get(i));
        }
        
        // calculate FIR
        NSGA2Algo.numPathOffsprings = NSGA2Algo.calcNumberPaths(TestSuiteController.simpleOffsprings);
        NSGA2Algo.valueFIR = (NSGA2Algo.numPathParents - NSGA2Algo.numPathOffsprings) * 1.0f / NSGA2Algo.numPathParents;
        
        // find best offspring among offsprings.
        NSGA2Algo.bestTestSuiteOffspring.removeAll(NSGA2Algo.bestTestSuiteOffspring);
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            if (i == 0) {
                NSGA2Algo.bestTestSuiteOffspring.addAll(TestSuiteController.simpleOffsprings.get(i));
            } else {
                int currRank = Integer.parseInt(TestSuiteController.simpleOffsprings.get(i).get(5).toString());
                float currCrowd = Float.parseFloat(TestSuiteController.simpleOffsprings.get(i).get(8).toString());
                int bestRank = Integer.parseInt(NSGA2Algo.bestTestSuiteOffspring.get(5).toString());
                float bestCrowd = Float.parseFloat(NSGA2Algo.bestTestSuiteOffspring.get(8).toString());
                if (currRank < bestRank || (currRank == bestRank && currCrowd < bestCrowd)) {
                    NSGA2Algo.bestTestSuiteOffspring.removeAll(NSGA2Algo.bestTestSuiteOffspring);
                    NSGA2Algo.bestTestSuiteOffspring.addAll(TestSuiteController.simpleOffsprings.get(i));
                }
            }
        }
        
        // find best parent among parents.
        NSGA2Algo.bestTestSuiteParent.removeAll(NSGA2Algo.bestTestSuiteParent);
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            if (i == 0) {
                NSGA2Algo.bestTestSuiteParent.addAll(TestSuiteController.simpleParents.get(i));
            } else {
                int currRank = Integer.parseInt(TestSuiteController.simpleParents.get(i).get(5).toString());
                float currCrowd = Float.parseFloat(TestSuiteController.simpleParents.get(i).get(8).toString());
                int bestRank = Integer.parseInt(NSGA2Algo.bestTestSuiteParent.get(5).toString());
                float bestCrowd = Float.parseFloat(NSGA2Algo.bestTestSuiteParent.get(8).toString());
                if (currRank < bestRank || (currRank == bestRank && currCrowd < bestCrowd)) {
                    NSGA2Algo.bestTestSuiteParent.removeAll(NSGA2Algo.bestTestSuiteParent);
                    NSGA2Algo.bestTestSuiteParent.addAll(TestSuiteController.simpleParents.get(i));
                }
            }
        }

        /**
         * END NSGA2
         */
        long end_time = System.currentTimeMillis();
        return (end_time - start_time);
    }
}
