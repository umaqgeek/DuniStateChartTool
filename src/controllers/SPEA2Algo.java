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
public class SPEA2Algo {
    
    public static float numPathParents = 0.0f;
    public static float numPathOffsprings = 0.0f;
    public static float valueFIR = 0.0f;

    private static int m[][] = new int[][]{
        {0, 1, -1},
        {1, 1, -1},
        {2, -1, 1},
        {3, -1, 1}
    };
    private static ArrayList<ArrayList<ArrayList<Object>>> store = new ArrayList<ArrayList<ArrayList<Object>>>();

    private ArrayList<ArrayList<Object>> sortObjectFunctions(ArrayList<ArrayList<Object>> sp) {
        store = new ArrayList<ArrayList<ArrayList<Object>>>();
        for (int i = 0; i < m.length; i++) {
            ArrayList<ArrayList<Object>> storeDetail = new ArrayList<ArrayList<Object>>();
            for (int j = 0; j < sp.size(); j++) {
                storeDetail.add(sp.get(j));
            }
            storeDetail = SPEA2Algo.sortF(storeDetail, m[i]);
            store.add(storeDetail);
        }
        ArrayList<Integer> allowedIndexes = new ArrayList<Integer>();
        for (int i = 0; i < sp.size(); i++) {
            ArrayList<Integer> data7 = (ArrayList<Integer>) sp.get(i).get(7);
            int iIndex = (int) sp.get(i).get(6);
            allowedIndexes.add(iIndex);
            for (int j = 0; j < data7.size(); j++) {
                int currCount = data7.get(j);
                for (int k = 0; k < store.size(); k++) {
                    int kIndex = (int) store.get(k).get(j).get(6);
                    if (iIndex == kIndex) {
                        currCount += 1;
                    }
                }
                data7.set(j, currCount);
            }
            sp.get(i).set(7, data7);
        }
        sp = sort7(sp, 0);
        return sp;
    }
    
    private static ArrayList<ArrayList<Object>> sort7(ArrayList<ArrayList<Object>> Ftemp, int c) {
        if (Ftemp.size() <= 1) {
            return (ArrayList<ArrayList<Object>>) Ftemp.clone();
        }
        if (c+1 >= ((ArrayList<Integer>) Ftemp.get(0).get(7)).size()) {
            return (ArrayList<ArrayList<Object>>) Ftemp.clone();
        }
        // sort
        Collections.sort(Ftemp, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return ((ArrayList<Integer>) o1.get(7)).get(c) > ((ArrayList<Integer>) o2.get(7)).get(c) ? -1 : 1;
            }
        });
        // clustering
        ArrayList<ArrayList<ArrayList<Object>>> cluster = new ArrayList<ArrayList<ArrayList<Object>>>();
        ArrayList<ArrayList<Object>> innerCluster = new ArrayList<ArrayList<Object>>();
        innerCluster.add(Ftemp.get(0));
        for (int i = 1; i < Ftemp.size(); i++) {
            int prevCount = ((ArrayList<Integer>) Ftemp.get(i-1).get(7)).get(c);
            int currCount = ((ArrayList<Integer>) Ftemp.get(i).get(7)).get(c);
            if (prevCount == currCount) {
                innerCluster.add(Ftemp.get(i));
            } else {
                innerCluster = sort7(innerCluster, c+1);
                ArrayList<ArrayList<Object>> innerClusterCopy = new ArrayList<ArrayList<Object>>();
                innerClusterCopy.addAll(innerCluster);
                cluster.add(innerClusterCopy);
                innerCluster.clear();
                innerCluster.add(Ftemp.get(i));
            }
        }
        innerCluster = sort7(innerCluster, c+1);
        ArrayList<ArrayList<Object>> innerClusterCopy = new ArrayList<ArrayList<Object>>();
        innerClusterCopy.addAll(innerCluster);
        cluster.add(innerClusterCopy);
        innerCluster.clear();
        ArrayList<ArrayList<Object>> Fout = new ArrayList<ArrayList<Object>>();
        for (int i = 0; i < cluster.size(); i++) {
            Fout.addAll(cluster.get(i));
        }
        return Fout;
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
    
    private static ArrayList<ArrayList<Object>> sortE(ArrayList<ArrayList<Object>> Ftemp) {
        Collections.sort(Ftemp, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return Float.parseFloat(o1.get(9).toString()) > Float.parseFloat(o2.get(9).toString()) ? 1 : -1;
            }
        });
        return Ftemp;
    }
    
    private static ArrayList<ArrayList<Object>> setNeighbourDistance(ArrayList<ArrayList<Object>> sp) {
        // set array of 8th and 9th
        int totalObj = 4;
        double minKnn = 3.00;
        int group = 1;
        try {
            minKnn = Double.parseDouble(TestSuite01.txtMinKnn.getText());
        } catch (Exception e) {
        }
        sp.get(0).add(0); // 8th
        sp.get(0).add(group); // 9th
        for (int i = 1; i < sp.size(); i++) {
            int bestGroup = -1;
            for (int k = i-1; k >= 0; k--) {
                float total = 0.0f;
                for (int j = 0; j < totalObj; j++) {
                    float prev = Float.parseFloat(sp.get(k).get(j).toString());
                    float curr = Float.parseFloat(sp.get(i).get(j).toString());
                    total += Math.pow((curr - prev), 2);
                }
                double d = Math.sqrt(total);
                int currGroup = Integer.parseInt(sp.get(k).get(9).toString());
                if (d <= minKnn) {
                    bestGroup = currGroup;
                    break;
                }
            }
            if (bestGroup == -1) {
                group += 1;
                bestGroup = group;
            }
            sp.get(i).add(0);
            sp.get(i).add(bestGroup);
        }
        sp = sortE(sp);
        return sp;
    }
    
    private static ArrayList<ArrayList<Object>> getEliminatedArr(ArrayList<ArrayList<Object>> sp, int minSize) {
        System.out.println("before eliminated");
        for (int i = 0; i < sp.size(); i++) {
            int iIndex = (int) sp.get(i).get(6);
            System.out.println("par #"+iIndex+": ["+//TestSuiteController.simpleParents2.get(i));
                    sp.get(i).get(9)+", "+
                    sp.get(i).get(0)+", "+
                    sp.get(i).get(1)+", "+
                    sp.get(i).get(2)+", "+
                    sp.get(i).get(3)+"]");
        }
        while (sp.size() > minSize) {
            boolean isNotSame = true;
            for (int i = 0; i < sp.size()-1; i++) {
                if (sp.get(i).get(9) == sp.get(i+1).get(9)) {
                    sp.remove(i+1);
                    isNotSame = false;
                    break;
                }
            }
            if (isNotSame) {
                for (int i = sp.size()-1; i >= 0; i--) {
                    sp.remove(i);
                    break;
                }
            }
        }
        System.out.println("after eliminated");
        for (int i = 0; i < sp.size(); i++) {
            int iIndex = (int) sp.get(i).get(6);
            System.out.println("par #"+iIndex+": ["+//TestSuiteController.simpleParents2.get(i));
                    sp.get(i).get(9)+", "+
                    sp.get(i).get(0)+", "+
                    sp.get(i).get(1)+", "+
                    sp.get(i).get(2)+", "+
                    sp.get(i).get(3)+"]");
        }
        return sp;
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
         * START SPEA
         */
        
        System.out.println("\nSPEA2 Algorithm Process\n--------------------");
        
        SPEA2Algo.numPathParents = SPEA2Algo.calcNumberPaths(TestSuiteController.simpleParents2);

        SPEA2Algo spalgo = new SPEA2Algo();
        int gen = 2;
        ArrayList<ArrayList<Object>> arrArch = new ArrayList<ArrayList<Object>>();
        
        int pop = 5;
        int arc = 5;
        try {
            pop = Integer.parseInt(TestSuite01.txtPopSize.getText());
            arc = Integer.parseInt(TestSuite01.txtArchSize.getText());
            gen = Integer.parseInt(TestSuite01.txtNoLoop.getText());
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
            ArrayList<Integer> data7 = new ArrayList<Integer>();
            for (int j = 0; j < TestSuiteController.simpleParents2.size(); j++) {
                data7.add(0);
            }
            TestSuiteController.simpleParents2.get(i).add(i);
            TestSuiteController.simpleParents2.get(i).add(data7);
        }
        
        for (int p = 0; p < gen; p++) {
            
            TestSuiteController.simpleOffsprings2.removeAll(TestSuiteController.simpleOffsprings2);
            TestSuiteController.possibleMutationPaths2.removeAll(TestSuiteController.possibleMutationPaths2);
            
            System.out.println("\nParents before loop.");
            for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleParents2.get(i).get(6);
                System.out.println("Parent #" + iIndex + ": " + TestSuiteController.simpleParents2.get(i));
            }
            
            int par = TestSuiteController.simpleParents2.size();
            int np = par; // 10
            int arx = arrArch.size(); // 0
            arrArch.removeAll(arrArch);
            System.out.println("\nspea");
            System.out.println("before sort");
            for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
                ArrayList<Integer> data7 = new ArrayList<Integer>();
                for (int j = 0; j < TestSuiteController.simpleParents2.size(); j++) {
                    data7.add(0);
                }
                TestSuiteController.simpleParents2.get(i).set(7, data7);
                System.out.println("par #" + (i + 1) + ": " + TestSuiteController.simpleParents2.get(i));
            }
            System.out.println("after sort");
            TestSuiteController.simpleParents2 = spalgo.sortObjectFunctions(TestSuiteController.simpleParents2);
            for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleParents2.get(i).get(6);
                System.out.println("par #" + iIndex + ": " + TestSuiteController.simpleParents2.get(i));
            }
            float rawFitness = par + arx; // 10 + 0 = 5
            float density = (float) Math.sqrt(par + arx); // sqrt(10 + 0) = 3.16
            float fitness = rawFitness + density; // 10 + 3.16 = 13.16

            System.out.println("par before distance:");
            for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleParents2.get(i).get(6);
                System.out.println("par #" + iIndex + ": " + TestSuiteController.simpleParents2.get(i));
            }
            TestSuiteController.simpleParents2 = SPEA2Algo.setNeighbourDistance(TestSuiteController.simpleParents2);
            System.out.println("par after kNN:");
            for (int i = 0; i < TestSuiteController.simpleParents2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleParents2.get(i).get(6);
                System.out.println("par #" + iIndex + ": ["
                        +//TestSuiteController.simpleParents2.get(i));
                        TestSuiteController.simpleParents2.get(i).get(9) + ", "
                        + TestSuiteController.simpleParents2.get(i).get(0) + ", "
                        + TestSuiteController.simpleParents2.get(i).get(1) + ", "
                        + TestSuiteController.simpleParents2.get(i).get(2) + ", "
                        + TestSuiteController.simpleParents2.get(i).get(3) + "]");
            }

            // copy to new sp array.
            ArrayList<ArrayList<Object>> beforeArcArr = new ArrayList<ArrayList<Object>>();
            beforeArcArr.addAll(TestSuiteController.simpleParents2);

            if (par >= arc) { // 10 >= 5

                // eliminate 2nd array smpai sama size dngn arc.
                beforeArcArr = SPEA2Algo.getEliminatedArr(beforeArcArr, arc);

                // copy eliminated array masuk dlm arc.
                for (int i = 0; i < beforeArcArr.size() && i < arc; i++) {
                    arrArch.add(beforeArcArr.get(i));
                }
            } else {
                for (int i = 0, ix = 0; i < arc; i++, ix++) {
                    ix = ix >= par ? 0 : ix;
                    arrArch.add(TestSuiteController.simpleParents2.get(ix));
                }
            }
            System.out.println("arc:");
            for (int i = 0; i < arrArch.size(); i++) {
                int iIndex = (int) arrArch.get(i).get(6);
                System.out.println("arc #" + iIndex + ": " + arrArch.get(i));
            }

            // Mating Genetic Algo.
            System.out.println("parent paths:");
            for (int i = 0; i < beforeArcArr.size(); i++) {
                int iIndex = (int) beforeArcArr.get(i).get(6);
                System.out.println("par #" + iIndex + ": " + beforeArcArr.get(i));
            }
            System.out.println("archive paths:");
            for (int i = 0; i < arrArch.size(); i++) {
                int iIndex = (int) arrArch.get(i).get(6);
                System.out.println("arc #" + iIndex + ": " + arrArch.get(i));
            }

            System.out.println("\nTournament selection:");
            int sizeMatingParents = beforeArcArr.size();
            for (int j = 0; j < sizeMatingParents; j++) {
                // Random pick parent.
                ArrayList<Object> pickedParent = new ArrayList<Object>();
                int randParentIndex = Func.rand.nextInt(beforeArcArr.size());
                pickedParent.addAll(beforeArcArr.get(randParentIndex));
                System.out.println("Chosen parent:\npar #" + (pickedParent.get(6)) + ": " + pickedParent);

                // Random pick archive.
                ArrayList<Object> pickedArchive = new ArrayList<Object>();
                int randArchiveIndex = Func.rand.nextInt(arrArch.size());
                pickedArchive.addAll(arrArch.get(randArchiveIndex));
                System.out.println("Chosen archive:\narc #" + (pickedArchive.get(6)) + ": " + pickedArchive);

                // set who win
                int countWinParent = 0;
                int countWinArchive = 0;
                for (int i = 0; i < 4; i++) {
                    float ofp = Float.parseFloat(pickedParent.get(i).toString());
                    float ofa = Float.parseFloat(pickedArchive.get(i).toString());
                    countWinParent = ofp > ofa ? countWinParent + 1 : countWinParent;
                    countWinArchive = ofa > ofp ? countWinArchive + 1 : countWinArchive;
                }
                ArrayList<Object> pickedMating = new ArrayList<Object>();
                if (countWinParent >= countWinArchive) {
                    pickedMating.addAll(pickedParent);
                } else {
                    pickedMating.addAll(pickedArchive);
                }
                System.out.println("Winner:\nmating #" + (pickedMating.get(6)) + ": " + pickedMating);

                // insert into mating pools
                TestSuiteController.simpleOffsprings2.add(pickedMating);
            }

            // collect list of permutations for mutation process.
            for (int i = 0; i < beforeArcArr.size(); i++) {
                TestSuiteController.possibleMutationPaths2.addAll((ArrayList<ArrayList<Integer>>) TestSuiteController.simpleParents2.get(i).get(4));
            }
            // view all possible path for mutation.
            // sorting the list.
            Collections.sort(TestSuiteController.possibleMutationPaths2, new Comparator<ArrayList<Integer>>() {
                @Override
                public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2) {
                    return o1.size() > o2.size() ? 1 : -1;
                }
            });
            // remove duplicates in the list.
            TestSuiteController.possibleMutationPaths2 = Func.removeDuplicates(TestSuiteController.possibleMutationPaths2);
            // view all possible path for mutation.
            System.out.println("\nView all possible path for mutation");
            for (int i = 0; i < TestSuiteController.possibleMutationPaths2.size(); i++) {
                System.out.println(TestSuiteController.possibleMutationPaths2.get(i));
            }

            // before crossover.
            System.out.println("\nOffsprings before crossover.");
            for (int i = 0; i < TestSuiteController.simpleOffsprings2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleOffsprings2.get(i).get(6);
                System.out.println("Offspring #" + iIndex + ": " + TestSuiteController.simpleOffsprings2.get(i));
            }

            // crossover.
            float crossoverChances = TestSuite01.sliCrossover.getValue() * 1.0f / 100;
            for (int i = 0; i < TestSuiteController.simpleOffsprings2.size(); i += 2) {

                int inext = (i + 1) >= TestSuiteController.simpleOffsprings2.size() ? 0 : (i + 1);

                // takeout genes from chromosome parent 1.
                ArrayList<ArrayList<Integer>> geneP1 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings2.get(i).get(4);
                int numGene1 = (int) (crossoverChances * geneP1.size());
                ArrayList<ArrayList<Integer>> holdGene1 = new ArrayList<ArrayList<Integer>>();
                for (int j = geneP1.size() - 1; j >= 0 && numGene1 > 0; j--, numGene1--) {
                    holdGene1.add(geneP1.get(j));
                    geneP1.remove(j);
                }

                // takeout genes from chromosome parent 2.
                ArrayList<ArrayList<Integer>> geneP2 = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings2.get(inext).get(4);
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
                TestSuiteController.simpleOffsprings2.get(i).set(4, geneP1);
                TestSuiteController.simpleOffsprings2.get(inext).set(4, geneP2);
            }

            // after crossover and before mutation.
            System.out.println("\nOffsprings after crossover and before mutation.");
            for (int i = 0; i < TestSuiteController.simpleOffsprings2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleOffsprings2.get(i).get(6);
                System.out.println("Offspring #" + iIndex + ": " + TestSuiteController.simpleOffsprings2.get(i));
            }

            // mutation.
            float mutationChances = TestSuite01.sliMutation.getValue() * 1.0f / 100;
            for (int i = 0; i < TestSuiteController.simpleOffsprings2.size(); i++) {
                ArrayList<ArrayList<Integer>> oldOffspringPaths = (ArrayList<ArrayList<Integer>>) TestSuiteController.simpleOffsprings2.get(i).get(4);

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

                    int randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths2.size());
                    ArrayList<Integer> newGene = TestSuiteController.possibleMutationPaths2.get(randMutateIndex);
                    do {
                        if (!oldGene.equals(newGene)) {
                            break;
                        }
                        randMutateIndex = Func.rand.nextInt(TestSuiteController.possibleMutationPaths2.size());
                        newGene = TestSuiteController.possibleMutationPaths2.get(randMutateIndex);
                    } while (true);

                    oldOffspringPaths.set(randIndex, newGene);
                }

                TestSuiteController.simpleOffsprings2.get(i).set(4, oldOffspringPaths);
            }

            // after mutation.
            System.out.println("\nOffsprings after mutation.");
            for (int i = 0; i < TestSuiteController.simpleOffsprings2.size(); i++) {
                int iIndex = (int) TestSuiteController.simpleOffsprings2.get(i).get(6);
                System.out.println("Offspring #" + iIndex + ": " + TestSuiteController.simpleOffsprings2.get(i));
            }
            
            // override new parents with new offsprings
            TestSuiteController.simpleParents2.removeAll(TestSuiteController.simpleParents2);
            TestSuiteController.simpleParents2.addAll(TestSuiteController.simpleOffsprings2);
        }
        
        // calculate FIR
        SPEA2Algo.numPathOffsprings = SPEA2Algo.calcNumberPaths(TestSuiteController.simpleOffsprings2);
        SPEA2Algo.valueFIR = (SPEA2Algo.numPathParents - SPEA2Algo.numPathOffsprings) * 1.0f / SPEA2Algo.numPathParents;

        /**
         * END SPEA
         */
    }
}
