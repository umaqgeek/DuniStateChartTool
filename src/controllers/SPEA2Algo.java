/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import views.TestSuite01;

/**
 *
 * @author umar
 */
public class SPEA2Algo {

    public static int m[][] = new int[][]{
        {0, 1, -1},
        {1, 1, -1},
        {2, -1, 1},
        {3, -1, 1}
    };
    public static ArrayList<ArrayList<ArrayList<Object>>> store = new ArrayList<ArrayList<ArrayList<Object>>>();

    public ArrayList<ArrayList<Object>> sortObjectFunctions(ArrayList<ArrayList<Object>> sp) {
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
    
    public static ArrayList<ArrayList<Object>> setNeighbourDistance(ArrayList<ArrayList<Object>> sp) {
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
            double minD = Double.MAX_VALUE;
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
                    minKnn = d;
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
//        sp = sortE(sp);
        return sp;
    }
}
