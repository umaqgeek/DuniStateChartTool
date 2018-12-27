/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.BranchBoundAlgo;
import controllers.FloydWarshallAlgo;
import controllers.UMLController;
import helpers.Func;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author umar
 */
public class TestCaseGeneration02Page extends javax.swing.JFrame {
    
    public static int totalVertices = UMLController.dataListStates.size() + 2;
//    public static int totalVertices = 5;
    public static int matrix[][] = new int[totalVertices][totalVertices];
    public static final int posINF = 99999; //Integer.MAX_VALUE;
    public static final int negINF = -99999; //Integer.MIN_VALUE;

    /**
     * Creates new form FloydWarshallAlgo01
     */
    public TestCaseGeneration02Page() {
        initComponents();
        
        initPage();
    }
    
    public static void initPage() {
        
        btnBBAlgo.setEnabled(false);
    }
    
    public static void viewPathMany(boolean isClear, String name, ArrayList<Integer> arr, int totalReduced) {
        
        try {
            
            String outview = "";
            
            String strTotalReduced = (totalReduced >= posINF) ? ("INF") : (totalReduced + "");
            outview += "Path " + name + ": [";
            for (int i = 0; i < arr.size(); i++) {
                outview += arr.get(i);
                if (i != arr.size() - 1) {
                    outview += ", ";
                }
            }
            outview += "]: cost " + strTotalReduced;
            
            if (Func.DEBUG) {
                System.out.println(outview);
            }
            if (isClear) {
                TestCaseGeneration02Page.txtResults.setText(outview);
            } else {
                TestCaseGeneration02Page.txtResults.setText(TestCaseGeneration02Page.txtResults.getText() + "\n" + outview);
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewPath(boolean isClear, String name, ArrayList<Integer> arr, int totalReduced) {
        
        try {
            
            String outview = "";
            
            String strTotalReduced = (totalReduced >= posINF) ? ("INF") : (totalReduced + "");
            outview += "Total Cost: " + strTotalReduced + "\n";
            outview += "Path " + name + ": [";
            for (int i = 0; i < arr.size(); i++) {
                outview += arr.get(i);
                if (i != arr.size() - 1) {
                    outview += ", ";
                }
            }
            outview += "]";
            
            if (Func.DEBUG) {
                System.out.println(outview);
            }
            if (isClear) {
                TestCaseGeneration02Page.txtResults.setText(outview);
            } else {
                TestCaseGeneration02Page.txtResults.setText(TestCaseGeneration02Page.txtResults.getText() + "\n" + outview);
            }
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewMatrix(boolean isClear) {

        try {

            String outview = "";

            outview += "[\n";
            for (int i = 0; i < TestCaseGeneration02Page.totalVertices; i++) {
                outview += "[";
                for (int j = 0; j < TestCaseGeneration02Page.totalVertices; j++) {
                    outview += ((TestCaseGeneration02Page.matrix[i][j] == TestCaseGeneration02Page.posINF 
                            || TestCaseGeneration02Page.matrix[i][j] == TestCaseGeneration02Page.negINF)
                            ? ("INF")
                            : (TestCaseGeneration02Page.matrix[i][j]));
                    if (j != TestCaseGeneration02Page.totalVertices - 1) {
                        outview += ", ";
                    }
                }
                outview += "]";
                if (i != TestCaseGeneration02Page.totalVertices - 1) {
                    outview += ", ";
                }
                outview += "\n";
            }
            outview += "]\n";

            if (Func.DEBUG) {
                System.out.println(outview);
            }
            if (isClear) {
                TestCaseGeneration02Page.txtResults.setText(outview);
            } else {
                TestCaseGeneration02Page.txtResults.setText(TestCaseGeneration02Page.txtResults.getText() + "\n" + outview);
            }

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewMatrix(boolean isClear, int mat[][]) {

        try {

            String outview = "";

            outview += "[\n";
            for (int i = 0; i < TestCaseGeneration02Page.totalVertices; i++) {
                outview += "[";
                for (int j = 0; j < TestCaseGeneration02Page.totalVertices; j++) {
                    outview += ((mat[i][j] == TestCaseGeneration02Page.posINF 
                            || mat[i][j] == TestCaseGeneration02Page.negINF)
                            ? ("INF")
                            : (mat[i][j]));
                    if (j != TestCaseGeneration02Page.totalVertices - 1) {
                        outview += ", ";
                    }
                }
                outview += "]";
                if (i != TestCaseGeneration02Page.totalVertices - 1) {
                    outview += ", ";
                }
                outview += "\n";
            }
            outview += "]\n";

            if (Func.DEBUG) {
                System.out.println(outview);
            }
            if (isClear) {
                TestCaseGeneration02Page.txtResults.setText(outview);
            } else {
                TestCaseGeneration02Page.txtResults.setText(TestCaseGeneration02Page.txtResults.getText() + "\n" + outview);
            }

        } catch (Exception e) {
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMatrix = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResults = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        btnBBAlgo = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNumberPath = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Floyd Warshall Algorithm");
        setAlwaysOnTop(true);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Test Case Generation");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Floyd Warshall Algorithm & Branch and Bound Algorithm");

        txtMatrix.setColumns(20);
        txtMatrix.setRows(5);
        jScrollPane1.setViewportView(txtMatrix);

        jButton1.setText("Run FWA Algo");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Results:");

        txtResults.setColumns(20);
        txtResults.setRows(5);
        jScrollPane2.setViewportView(txtResults);

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnBBAlgo.setText("Run BB Algo");
        btnBBAlgo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBBAlgoActionPerformed(evt);
            }
        });

        btnQuit.setText("Quit");
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });

        jLabel4.setText("No. of path:");

        txtNumberPath.setText("10");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(308, 308, 308)
                                .addComponent(jLabel1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3)))
                        .addGap(0, 285, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(231, 231, 231))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBBAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnQuit, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(txtNumberPath))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBBAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtNumberPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        String matrix = txtMatrix.getText();
        boolean isValidMatrix = new FloydWarshallAlgo().isValidMatrix(matrix);
        if (isValidMatrix) {
            FloydWarshallAlgo.calcFWA01();
            btnBBAlgo.setEnabled(true);
        } else {
            clearPage();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        clearPage();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void clearPage() {
        txtMatrix.setText("");
        txtResults.setText("");
        btnBBAlgo.setEnabled(false);
    }
    
    private void btnBBAlgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBBAlgoActionPerformed
        // TODO add your handling code here:
        
        int numberOfPath = 0;
        try {
            
            numberOfPath = Integer.parseInt(txtNumberPath.getText());
            BranchBoundAlgo.calcBBA01(numberOfPath);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number of path!", "Invalid Number of Path", 0);
        }
    }//GEN-LAST:event_btnBBAlgoActionPerformed

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnQuitActionPerformed

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
            java.util.logging.Logger.getLogger(TestCaseGeneration02Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TestCaseGeneration02Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TestCaseGeneration02Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TestCaseGeneration02Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TestCaseGeneration02Page().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton btnBBAlgo;
    public static javax.swing.JButton btnQuit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTextArea txtMatrix;
    public static javax.swing.JTextField txtNumberPath;
    public static javax.swing.JTextArea txtResults;
    // End of variables declaration//GEN-END:variables
}
