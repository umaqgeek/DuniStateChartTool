/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.TestSuiteController;
import controllers.UMLController;
import helpers.Func;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Properties;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author umar
 */
public class TestSuite01 extends javax.swing.JFrame {
    
    public static int totalVertices = UMLController.dataListStates.size();
    public static String prevMatrix;
    public static int totalMaximumPath = 1;
    public static int posINF = 99999; //Integer.MAX_VALUE;
    public static int negINF = -99999; //Integer.MIN_VALUE;
    public static int matrix[][] = new int[totalVertices][totalVertices];

    /**
     * Creates new form TestSuite01
     */
    public TestSuite01() {
        initComponents();
    }
    
    public TestSuite01(String matrix) {
        initComponents();
        
        totalMaximumPath = TestSuiteController.getTotalMaximumPath();

        prevMatrix = matrix;
        txtBoxOutput.setText(prevMatrix);
    }
    
    public static void viewTitle(boolean isClear, String title) {
        try {

            String outview = "";

            outview += title + "\n";
            outview += "------------------\n";

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
            txtResults.setText(output);
        } else {
            txtResults.setText(txtResults.getText() + output);
        }
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
    
    public static void viewPathMany(boolean isClear, String name, ArrayList<Integer> arr, int totalReduced) {
        
        try {
            
            String outview = "";
            
            String strTotalReduced = (totalReduced >= posINF) ? ("INF") : (totalReduced + "");
            outview += "Path " + name + ": [";
            for (int i = 0; i < arr.size(); i++) {
                String code = "s" + arr.get(i);
                String stateName = UMLController.getStateName(code);
                if (Func.DEBUG) {
                    outview += code;
                } else {
                    outview += stateName;
                }
                if (i != arr.size() - 1) {
                    outview += ", ";
                }
            }
            outview += "]: cost " + strTotalReduced + "\n";
            
            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(isClear, outview);
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNoTestSuite = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtNoTestCaseFrom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNoTestCaseTo = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtBoxOutput = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResults = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cbTotalPath = new javax.swing.JCheckBox();
        cbWithoutFinal = new javax.swing.JCheckBox();
        cbTimeExec = new javax.swing.JCheckBox();
        cbTransCoverage = new javax.swing.JCheckBox();
        cbTransPairCoverage = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Test Suite");
        setAlwaysOnTop(true);

        jLabel1.setText("No. of Test Suite : ");

        txtNoTestSuite.setText("10");

        jLabel2.setText("Range of Test Case per suite : ");

        txtNoTestCaseFrom.setText("8");

        jLabel3.setText("to");

        txtNoTestCaseTo.setText("12");

        jButton1.setText("Generate Parents");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnQuit.setText("Quit");
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });

        txtBoxOutput.setEditable(false);
        txtBoxOutput.setColumns(20);
        txtBoxOutput.setRows(5);
        jScrollPane1.setViewportView(txtBoxOutput);

        jLabel4.setText("Previous Matrix : ");

        txtResults.setEditable(false);
        txtResults.setColumns(20);
        txtResults.setRows(5);
        jScrollPane2.setViewportView(txtResults);

        jLabel5.setText("Results :");

        cbTotalPath.setSelected(true);
        cbTotalPath.setText("Include Total Paths.");

        cbWithoutFinal.setText("Include Without Final in Path.");

        cbTimeExec.setSelected(true);
        cbTimeExec.setText("Include Time Execution.");

        cbTransCoverage.setSelected(true);
        cbTransCoverage.setText("Transitions Coverage.");

        cbTransPairCoverage.setSelected(true);
        cbTransPairCoverage.setText("Transitions Pair Coverage.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbWithoutFinal)
                            .addComponent(cbTotalPath)
                            .addComponent(cbTimeExec))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTransPairCoverage)
                            .addComponent(cbTransCoverage))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbWithoutFinal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTotalPath)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTimeExec)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTransCoverage)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTransPairCoverage)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jButton2.setText("Generate Offsprings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 166, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoTestSuite, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoTestCaseFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNoTestCaseTo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txtNoTestSuite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNoTestCaseFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtNoTestCaseTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
                .addGap(84, 84, 84))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // TODO add your handling code here:
        dispose();
        System.exit(1);
    }//GEN-LAST:event_btnQuitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        TestSuiteController.clearParents();
        int numberOfTestSuite = 0;
        try {
            
            numberOfTestSuite = Integer.parseInt(txtNoTestSuite.getText());
            
            viewTitle(true, "Generating Test Suites / Parents");
            
            for (int i = 0; i < numberOfTestSuite; i++) {
                
                viewText(false, "Test Suite #" + (i+1));
                
                int numberOfPath = 0;
                try {

                    String matrix = prevMatrix;
                    boolean isValidMatrix = TestSuiteController.isValidMatrix(matrix);
                    if (isValidMatrix) {

                        Random rand = new Random();
                        int from = Integer.parseInt(txtNoTestCaseFrom.getText());
                        int to = Integer.parseInt(txtNoTestCaseTo.getText());

                        numberOfPath = rand.nextInt(to - from + 1) + from;

                        if (numberOfPath <= 0 || numberOfPath >= totalMaximumPath) {
                            throw new Exception();
                        } else {

                            long startTime = System.currentTimeMillis();

                            Properties props = TestSuiteController.calcPRA01(numberOfPath);
                            
                            if (TestSuite01.cbTotalPath.isSelected()) {
                                viewText(false, "Total Paths: " + props.getProperty(Func.TOTAL_NUMBER_PATH));
                            } else {
                                props.remove(Func.TOTAL_NUMBER_PATH);
                            }

                            long endTime = System.currentTimeMillis();
                            long diffTime = endTime - startTime;
//                            lblTimeExec.setText("Exection Time: " + diffTime + " ms");

                            if (TestSuite01.cbTimeExec.isSelected()) {
                                props.setProperty(Func.TOTAL_TIME_EXECUTION, ""+diffTime);
                                
                                viewText(false, "Total Exec. Time: " + diffTime + " ms");
                            }
                            
                            if (TestSuite01.cbTransCoverage.isSelected()) {
                                float A1 = Float.parseFloat((String) props.getProperty(Func.TOTAL_ALL_TRANSITIONS));
                                float A2 = Float.parseFloat((String) props.getProperty(Func.TOTAL_TRANSITIONS_PATH));
                                float X = (A2 > 0.0f && A1 > 0.0f) ? (A2 * 1.0f / A1) * 100.0f : 0.0f;
                                
                                props.setProperty(Func.TOTAL_TRANS_COVERAGE, ""+X);

                                viewText(false, "Total All Transitions: " + props.getProperty(Func.TOTAL_ALL_TRANSITIONS));
                                viewText(false, "Total Transitions in Path: " + props.getProperty(Func.TOTAL_TRANSITIONS_PATH));
                                viewText(false, "Transition Coverage: " + Func.df.format(X) + " %");
                            }
                            
                            if (TestSuite01.cbTransPairCoverage.isSelected()) {
                                float B1 = Float.parseFloat((String) props.getProperty(Func.TOTAL_ALL_PAIRS));
                                float B2 = Float.parseFloat((String) props.getProperty(Func.TOTAL_PAIRS_PATH));
                                float Y = (B2 > 0.0f && B1 > 0.0f) ? (B2 * 1.0f / B1) * 100.0f : 0.0f;
                                
                                props.setProperty(Func.TOTAL_TRANS_PAIR_COVERAGE, ""+Y);

                                viewText(false, "Total All Pairs: " + props.getProperty(Func.TOTAL_ALL_PAIRS));
                                viewText(false, "Total Pairs in Path: " + props.getProperty(Func.TOTAL_PAIRS_PATH));
                                viewText(false, "Transition Pair Coverage: " + Func.df.format(Y) + " %");
                            }
                            
                            // only take parent that at least have one path.
                            if (Integer.parseInt((String) props.getProperty(Func.TOTAL_NUMBER_PATH)) > 0) {
                                TestSuiteController.setParents(props);
                            }
                           
                            viewText(false, "\n");
                        }

                    }

                } catch (Exception e) {
                    if (Func.DEBUG) {
                        e.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "Invalid number of path!", "Invalid Number of Path", 0);
                }
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Invalid number of test suite!", "Invalid Number of Test Suite", 0);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (TestSuiteController.parents.size() > 0) {
            
            new TestSuite02().setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(null, "Generate parents first!", "Parent Not Found", 0);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(TestSuite01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestSuite01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestSuite01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestSuite01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestSuite01(prevMatrix).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnQuit;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    public static javax.swing.JCheckBox cbTimeExec;
    public static javax.swing.JCheckBox cbTotalPath;
    public static javax.swing.JCheckBox cbTransCoverage;
    public static javax.swing.JCheckBox cbTransPairCoverage;
    public static javax.swing.JCheckBox cbWithoutFinal;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTextArea txtBoxOutput;
    public static javax.swing.JTextField txtNoTestCaseFrom;
    public static javax.swing.JTextField txtNoTestCaseTo;
    public static javax.swing.JTextField txtNoTestSuite;
    public static javax.swing.JTextArea txtResults;
    // End of variables declaration//GEN-END:variables
}
