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
        
        String output = "FIR value:\n";
        
        final int numberAlgo = 3;
        final int numberDoors = 5;
        final int numberWindowsPerDoor = 50;
        int numberLoop = numberDoors * numberWindowsPerDoor / numberAlgo + 1;
        ArrayList<ArrayList<Object>> slidingWindows = new ArrayList<ArrayList<Object>>();
        
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
            slidingWindowNSGA2.add("NSGA2 ("+(w+1)+")");
            slidingWindowNSGA2.add(NSGA2Algo.valueFIR);
            slidingWindowNSGA2.add(timeNSGA*1.0/1000);
            ArrayList<Object> so = new ArrayList<Object>();
            so.addAll(NSGA2Algo.bestTestSuite);
            slidingWindowNSGA2.add(so);
            slidingWindows.add(slidingWindowNSGA2);
            
            ArrayList<Object> slidingWindowSPEA2 = new ArrayList<Object>();
            slidingWindowSPEA2.add("SPEA2 ("+(w+1)+")");
            slidingWindowSPEA2.add(SPEA2Algo.valueFIR);
            slidingWindowSPEA2.add(timeSPEA*1.0/1000);
            ArrayList<Object> so2 = new ArrayList<Object>();
            so2.addAll(SPEA2Algo.bestTestSuite);
            slidingWindowSPEA2.add(so2);
            slidingWindows.add(slidingWindowSPEA2);
            
            ArrayList<Object> slidingWindowPSO = new ArrayList<Object>();
            slidingWindowPSO.add("PSO ("+(w+1)+")");
            slidingWindowPSO.add(PSOAlgo.valueFIR);
            slidingWindowPSO.add(timePSO*1.0/1000);
            ArrayList<Object> so3 = new ArrayList<Object>();
            so3.addAll(PSOAlgo.bestTestSuite);
            slidingWindowPSO.add(so3);
            slidingWindows.add(slidingWindowPSO);
        }
        
        // sort sliding windows from fastest to slowest.
        slidingWindows = TestSuite02.sortSlidingWindows(slidingWindows);
        
        ArrayList<ArrayList<Object>> rewardsAll = new ArrayList<ArrayList<Object>>();
        
        System.out.println("\nSliding windows:");
        output += "\n\nSliding windows:";
        for (int j = 0; j < numberDoors; j++) {
            System.out.println("\nSliding window #"+(j+1)+":");
            output += "\n\nSliding window #"+(j+1)+":";
            
            ArrayList<Object> rewardsPerDoor = new ArrayList<Object>();
            float rewardsNSGA = 0.00f;
            float numNSGA = 0;
            float rewardsSPEA = 0.00f;
            float numSPEA = 0;
            float rewardsPSO = 0.00f;
            float numPSO = 0;
            
            for (int i = 0+(numberWindowsPerDoor*j); i < slidingWindows.size() && i < numberWindowsPerDoor*(j+1); i++) {
                System.out.print((i + 1) + ": " + slidingWindows.get(i).get(0) + ", FIR: " + slidingWindows.get(i).get(1) + " [" + slidingWindows.get(i).get(2) + "s]");
                output += "\n" + (i + 1) + ": " + slidingWindows.get(i).get(0) + ", FIR: " + slidingWindows.get(i).get(1) + " [" + slidingWindows.get(i).get(2) + "s]";
                
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("nsga2")) {
                    rewardsNSGA += Float.parseFloat(slidingWindows.get(i).get(1).toString());
                    numNSGA += 1;
                }
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("spea2")) {
                    rewardsSPEA += Float.parseFloat(slidingWindows.get(i).get(1).toString());
                    numSPEA += 1;
                }
                if (slidingWindows.get(i).get(0).toString().toLowerCase().contains("pso")) {
                    rewardsPSO += Float.parseFloat(slidingWindows.get(i).get(1).toString());
                    numPSO += 1;
                }
                
                ArrayList<Object> bestTestSuite = (ArrayList<Object>) slidingWindows.get(i).get(3);
                System.out.println(" | " + bestTestSuite.get(4));
                output += " | " + bestTestSuite.get(4);
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
        
        System.out.println("\nRewards:");
        output += "\n\nRewards:";
        for (int i = 0; i < rewardsAll.size(); i++) {
            float SUM = 0.0f;
            for (int j = 0; j < rewardsAll.get(i).size() && j < 6; j+=2) {
                SUM += Float.parseFloat(rewardsAll.get(i).get(j).toString());
            }
            
            float FRR_NSGA2 = Float.parseFloat(rewardsAll.get(i).get(0).toString()) / SUM;
            float tNSGA2 = Float.parseFloat(rewardsAll.get(i).get(1).toString());
            float hsNSGA2 = TestSuite02.getHeuristicSelectionValue(FRR_NSGA2, tNSGA2);
            
            float FRR_SPEA2 = Float.parseFloat(rewardsAll.get(i).get(2).toString()) / SUM;
            float tSPEA2 = Float.parseFloat(rewardsAll.get(i).get(3).toString());
            float hsSPEA2 = TestSuite02.getHeuristicSelectionValue(FRR_SPEA2, tSPEA2);
            
            float FRR_PSO = Float.parseFloat(rewardsAll.get(i).get(4).toString()) / SUM;
            float tPSO = Float.parseFloat(rewardsAll.get(i).get(5).toString());
            float hsPSO = TestSuite02.getHeuristicSelectionValue(FRR_PSO, tPSO);
            
            System.out.println("\nWindow #"+(i+1));
//            System.out.println("NSGA2, FIR: "+rewardsAll.get(i).get(0)+", FRR: "+FRR_NSGA2+", Appear: "+tNSGA2+" times, HSV: "+hsNSGA2);
//            System.out.println("SPEA2, FIR: "+rewardsAll.get(i).get(2)+", FRR: "+FRR_SPEA2+", Appear: "+tSPEA2+" times, HSV: "+hsSPEA2);
//            System.out.println("PSO, FIR: "+rewardsAll.get(i).get(4)+", FRR: "+FRR_PSO+", Appear: "+tPSO+" times, HSV: "+hsPSO);

            ArrayList<Object> testSuite = (ArrayList<Object>) rewardsAll.get(i).get(7);
            System.out.println("best TS#: "+testSuite);
            ArrayList<ArrayList<Integer>> bestTestSuite = (ArrayList<ArrayList<Integer>>) testSuite.get(4);
            
            // TODO: define which one the best test path.
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
            
            output += "\n\nWindow #" + (i+1);
//            output += "\nNSGA2, FIR: " + rewardsAll.get(i).get(0) + ", FRR: " + FRR_NSGA2 + ", Appear: " + tNSGA2 + " times, HSV: " + hsNSGA2;
//            output += "\nSPEA2, FIR: " + rewardsAll.get(i).get(2) + ", FRR: " + FRR_SPEA2 + ", Appear: " + tSPEA2 + " times, HSV: " + hsSPEA2;
//            output += "\nPSO, FIR: " + rewardsAll.get(i).get(4) + ", FRR: " + FRR_PSO + ", Appear: " + tPSO + " times, HSV: " + hsPSO;
            output += "\n1. Best test suite       : " + bestTestSuite;
            output += "\n2. Best test path        : " + bestTestPath + ", cost = " + lowestCost;
            output += "\n3. Size of test suite    : " + bestTestSuite.size();
            output += "\n4. Time execution        : " + rewardsAll.get(i).get(8) + "s";
            output += "\n5. Best running heuristic: " + rewardsAll.get(i).get(6).toString().split(" ")[0];
        }
        
        txtMainScreen.setText(output);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        txtMainScreen = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Test Suite 02");
        setAlwaysOnTop(true);

        txtMainScreen.setEditable(false);
        txtMainScreen.setColumns(20);
        txtMainScreen.setRows(5);
        jScrollPane1.setViewportView(txtMainScreen);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 553, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(240, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea txtMainScreen;
    // End of variables declaration//GEN-END:variables
}
