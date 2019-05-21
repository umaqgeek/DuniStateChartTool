/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.NSGA2Algo;
import controllers.PSOAlgo;
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
        
        int numberLoop = 4;
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
            slidingWindowNSGA2.add("NSGA2");
            slidingWindowNSGA2.add(NSGA2Algo.valueFIR);
            slidingWindows.add(slidingWindowNSGA2);
            
            ArrayList<Object> slidingWindowSPEA2 = new ArrayList<Object>();
            slidingWindowSPEA2.add("SPEA2");
            slidingWindowSPEA2.add(SPEA2Algo.valueFIR);
            slidingWindows.add(slidingWindowSPEA2);
            
            ArrayList<Object> slidingWindowPSO = new ArrayList<Object>();
            slidingWindowPSO.add("PSO");
            slidingWindowPSO.add(PSOAlgo.valueFIR);
            slidingWindows.add(slidingWindowPSO);
        }
        
        // sort sliding windows from small to big.
        slidingWindows = TestSuite02.sortSlidingWindows(slidingWindows);
        
        System.out.println("\nSliding windows:");
        for (int i = 0; i < slidingWindows.size(); i++) {
            System.out.println(slidingWindows.get(i));
        }
        
        txtMainScreen.setText(output);
    }
    
    private static ArrayList<ArrayList<Object>> sortSlidingWindows(ArrayList<ArrayList<Object>> sw) {
        Collections.sort(sw, new Comparator<ArrayList<Object>>() {
            @Override
            public int compare(ArrayList<Object> o1, ArrayList<Object> o2) {
                return Float.parseFloat(o1.get(1).toString()) > Float.parseFloat(o2.get(1).toString()) ? 1 : -1;
            }
        });
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
