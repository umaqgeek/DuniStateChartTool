/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.BranchBoundAlgo;
import controllers.FloydWarshallAlgo;
import controllers.PureRandomAlgo;
import controllers.UMLController;
import helpers.Func;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author umar
 */
public class TestCaseGeneration02Page extends javax.swing.JFrame {
    
    public static int totalVertices = UMLController.dataListStates.size();
    public static int totalMaximumPath = 1;
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
        
        totalVertices = UMLController.dataListStates.size();
        matrix = new int[totalVertices][totalVertices];
        btnBBAlgo.setEnabled(false);
        resetPreMatrix();
        totalMaximumPath = getTotalMaximumPath();
        
        int initDefaultPath = 10;
        int defaultPath = (totalMaximumPath > initDefaultPath) ? (initDefaultPath) : (totalMaximumPath - 1);
        txtNumberPath.setText(defaultPath+"");
    }
    
    public static int getTotalMaximumPath() {
        long maxTemp = 1;
        int maxPath = 1;
        if (totalVertices > 1) {
            for (int i = 2; i <= totalVertices-2; i++) {
                maxTemp = maxTemp * i;
                if (maxTemp >= Integer.MAX_VALUE) {
                    maxPath = Integer.MAX_VALUE;
                    break;
                } else {
                    maxPath = (int) maxTemp;
                }
            }
        }
        return maxPath;
    }
    
    public static void resetPreMatrix() {
        String preMatrix = "";
        for (int i = 0; i < UMLController.preMatrix.size(); i++) {
            for (int j = 0; j < UMLController.preMatrix.get(i).size(); j++) {
                preMatrix += UMLController.preMatrix.get(i).get(j);
                if (j != (UMLController.preMatrix.get(i).size() - 1)) {
                    preMatrix += ", ";
                }
            }
            if (i != (UMLController.preMatrix.size() - 1)) {
                preMatrix += ";\n";
            }
        }
        txtMatrix.setText(preMatrix);
    }
    
    public static void viewTitle(int box, boolean isClear, String title) {
        try {

            String outview = "";

            outview += title;
            outview += "\n------------------";

            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(box, isClear, outview);

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewPathMany(int box, boolean isClear, String name, ArrayList<Integer> arr, int totalReduced) {
        
        try {
            
            String outview = "";
            
            String strTotalReduced = (totalReduced >= posINF) ? ("INF") : (totalReduced + "");
            outview += "Path " + name + ": [";
            for (int i = 0; i < arr.size(); i++) {
                String code = "s" + arr.get(i);
                String stateName = UMLController.getStateName(code);
                outview += stateName;
                if (i != arr.size() - 1) {
                    outview += ", ";
                }
            }
            outview += "]: cost " + strTotalReduced;
            
            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(box, isClear, outview);
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewPath(int box, boolean isClear, String title, String name, ArrayList<Integer> arr, int totalReduced) {
        
        try {
            
            String outview = "";
            
            String strTotalReduced = (totalReduced >= posINF) ? ("INF") : (totalReduced + "");
            outview += title;
            outview += "\n------------------\n";
            outview += "Total Cost: " + strTotalReduced + "\n";
            outview += "Path " + name + ": [";
            for (int i = 0; i < arr.size(); i++) {
                String code = "s" + arr.get(i);
                String stateName = UMLController.getStateName(code);
                outview += stateName;
                if (i != arr.size() - 1) {
                    outview += ", ";
                }
            }
            outview += "]\n";
            
            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(box, isClear, outview);
            
        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewMatrix(int box, boolean isClear, String title) {

        try {

            String outview = "";

            outview += title;
            outview += "\n------------------\n";
            outview += "[";
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
                    outview += ", \n";
                }
//                outview += "\n";
            }
            outview += "]\n";

            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(box, isClear, outview);

        } catch (Exception e) {
            if (Func.DEBUG) {
                e.printStackTrace();
            }
        }
    }
    
    public static void viewMatrix(int box, boolean isClear, String title, int mat[][]) {

        try {

            String outview = "";

            outview += title;
            outview += "\n------------------\n";
            outview += "[";
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
                    outview += ", \n";
                }
//                outview += "\n";
            }
            outview += "]\n";

            if (Func.DEBUG) {
                System.out.println(outview);
            }
            setBox(box, isClear, outview);

        } catch (Exception e) {
        }
    }
    
    public static void setBox(int box, boolean isClear, String output) {
        switch (box) {
            case 1:
                if (isClear) {
                    TestCaseGeneration02Page.txtResultsFWA.setText(output);
                } else {
                    TestCaseGeneration02Page.txtResultsFWA.setText(TestCaseGeneration02Page.txtResultsFWA.getText() + "\n" + output);
                }
                break;
            case 2:
                if (isClear) {
                    TestCaseGeneration02Page.txtResultsBBA.setText(output);
                } else {
                    TestCaseGeneration02Page.txtResultsBBA.setText(TestCaseGeneration02Page.txtResultsBBA.getText() + "\n" + output);
                }
                break;
            case 3:
                if (isClear) {
                    TestCaseGeneration02Page.txtResultsBodo.setText(output);
                } else {
                    TestCaseGeneration02Page.txtResultsBodo.setText(TestCaseGeneration02Page.txtResultsBodo.getText() + "\n" + output);
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Cannot decide which result box need to be output!", "Invalid Output Box", 0);
                break;
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

        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMatrix = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResultsFWA = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        btnBBAlgo = new javax.swing.JButton();
        btnQuit = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNumberPath = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtResultsBBA = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtResultsBodo = new javax.swing.JTextArea();
        btnBodoAlgo = new javax.swing.JButton();
        lblET1 = new javax.swing.JLabel();
        lblET2 = new javax.swing.JLabel();
        lblET3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();

        jLabel6.setText("Execution Time: ");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Floyd Warshall Algorithm");

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

        txtResultsFWA.setColumns(20);
        txtResultsFWA.setRows(5);
        jScrollPane2.setViewportView(txtResultsFWA);

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

        jButton3.setText("Reset");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        txtResultsBBA.setColumns(20);
        txtResultsBBA.setRows(5);
        jScrollPane3.setViewportView(txtResultsBBA);

        txtResultsBodo.setColumns(20);
        txtResultsBodo.setRows(5);
        jScrollPane4.setViewportView(txtResultsBodo);

        btnBodoAlgo.setText("Generate Random");
        btnBodoAlgo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBodoAlgoActionPerformed(evt);
            }
        });

        lblET1.setText("Execution Time: ");

        lblET2.setText("Execution Time: ");

        lblET3.setText("Execution Time: ");

        jButton4.setText("Test Suite >>");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(494, 494, 494))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(564, 564, 564))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(46, 46, 46)
                        .addComponent(lblET1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBBAlgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBodoAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNumberPath, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblET2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblET3)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(btnQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnBBAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(btnBodoAlgo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtNumberPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(107, 107, 107)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(lblET1)
                            .addComponent(lblET2)
                            .addComponent(lblET3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        runFWA();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void runFWA() {
        String matrix = txtMatrix.getText();
        boolean isValidMatrix = new FloydWarshallAlgo().isValidMatrix(matrix);
        if (isValidMatrix) {
            
            long startTime = System.currentTimeMillis();
            
            FloydWarshallAlgo.calcFWA01();
            
            long endTime = System.currentTimeMillis();
            long diffTime = endTime - startTime;
            lblET1.setText("Exection Time: " + diffTime + " ms");
            
            btnBBAlgo.setEnabled(true);
        } else {
            clearPage();
        }
    }
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        clearPreMatrix();
        clearPage();
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void clearPreMatrix() {
        txtMatrix.setText("");
    }
    
    public static void clearPage() {
        lblET1.setText("Execution Time: -");
        lblET2.setText("Execution Time: -");
        lblET3.setText("Execution Time: -");
        txtResultsFWA.setText("");
        txtResultsBBA.setText("");
        txtResultsBodo.setText("");
        btnBBAlgo.setEnabled(false);
    }
    
    private void btnBBAlgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBBAlgoActionPerformed
        // TODO add your handling code here:
        
        int numberOfPath = 0;
        try {
            
            // run FWA first
            runFWA();
            
            System.out.println("totalMaximumPath: "+totalMaximumPath);
            
            // then run BBA
            numberOfPath = Integer.parseInt(txtNumberPath.getText());
            if (numberOfPath <= 0 || numberOfPath >= totalMaximumPath) {
                throw new Exception();
            } else {
                
                long startTime = System.currentTimeMillis();

                BranchBoundAlgo.calcBBA01(numberOfPath);
                
                long endTime = System.currentTimeMillis();
                long diffTime = endTime - startTime;
                lblET2.setText("Exection Time: " + diffTime + " ms");
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number of path!", "Invalid Number of Path", 0);
        }
    }//GEN-LAST:event_btnBBAlgoActionPerformed

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnQuitActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        resetPreMatrix();
        clearPage();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnBodoAlgoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBodoAlgoActionPerformed
        // TODO add your handling code here:
        
        int numberOfPath = 0;
        try {
            
            String matrix = txtMatrix.getText();
            PureRandomAlgo.rawMatrix = matrix;
            boolean isValidMatrix = new PureRandomAlgo().isValidMatrix(matrix);
            if (isValidMatrix) {

                numberOfPath = Integer.parseInt(txtNumberPath.getText());
                if (numberOfPath <= 0 || numberOfPath >= totalMaximumPath) {
                    throw new Exception();
                } else {
                    
                    long startTime = System.currentTimeMillis();

                    PureRandomAlgo.calcPRA01(numberOfPath);

                    long endTime = System.currentTimeMillis();
                    long diffTime = endTime - startTime;
                    lblET3.setText("Exection Time: " + diffTime + " ms");
                }

            } else {
                clearPage();
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Invalid number of path!", "Invalid Number of Path", 0);
        }
    }//GEN-LAST:event_btnBodoAlgoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String matrix = txtMatrix.getText();
        PureRandomAlgo.rawMatrix = matrix;
        boolean isValidMatrix = new PureRandomAlgo().isValidMatrix(PureRandomAlgo.rawMatrix);
        if (isValidMatrix) {
            new TestSuite01(txtMatrix.getText()).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid matrix!", "Invalid Matrix", 0);
            clearPage();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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
    public static javax.swing.JButton btnBodoAlgo;
    public static javax.swing.JButton btnQuit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JLabel lblET1;
    public static javax.swing.JLabel lblET2;
    public static javax.swing.JLabel lblET3;
    public static javax.swing.JTextArea txtMatrix;
    public static javax.swing.JTextField txtNumberPath;
    public static javax.swing.JTextArea txtResultsBBA;
    public static javax.swing.JTextArea txtResultsBodo;
    public static javax.swing.JTextArea txtResultsFWA;
    // End of variables declaration//GEN-END:variables
}
