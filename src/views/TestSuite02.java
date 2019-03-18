/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.NSGA2Algo;
import controllers.TestSuiteController;
import helpers.Func;
import java.util.ArrayList;
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
        
        NSGA2Algo.setRanks();
        
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            ArrayList<Object> singleParent = TestSuiteController.simpleParents.get(i);
            NSGA2Algo.setMaxMinM(singleParent);
        }
        
        NSGA2Algo.viewAllMaxMinM();
        
        NSGA2Algo.setCrowds();
        
        for (int i = 0; i < NSGA2Algo.Fprops.size(); i++) {
            System.out.println("F #"+(i+1)+":");
            ArrayList<ArrayList<Object>> F = (ArrayList<ArrayList<Object>>) NSGA2Algo.Fprops.get(Func.KEY_F + (i+1));
            for (int j = 0; j < F.size(); j++) {
                System.out.println(j + ": " + F.get(j));
            }
        }
        
        viewText(true, "Parents by Ranks and Crowd Distances:");
        for (int i = 0; i < TestSuiteController.simpleParents.size(); i++) {
            ArrayList<Object> singleParent = TestSuiteController.simpleParents.get(i);
            viewText(false, "TS #" + (i+1) + ": (R="+singleParent.get(5)+"), (CD="+singleParent.get(8)+"), " + singleParent);
        }
        
        // generate offspring.
        // tournament selection.
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
            } while(true);
            ArrayList<Object> parent1 = TestSuiteController.simpleParents.get(indexParent1);
            ArrayList<Object> parent2 = TestSuiteController.simpleParents.get(indexParent2);
            System.out.println("P #"+(i+1)+":");
            System.out.println("parent1: index "+indexParent1+" - "+parent1);
            System.out.println("vs");
            System.out.println("parent2: index "+indexParent2+" - "+parent2);
            ArrayList<Object> selectedParent = new ArrayList<Object>();
            int selectedIndexParent = -1;
            if ( ((int) parent1.get(5)) < ((int) parent2.get(5)) ) {
                selectedParent.addAll(parent1);
                selectedIndexParent = indexParent1;
            } else if (((int) parent1.get(5)) == ((int) parent2.get(5))) {
                if ( ((float) parent1.get(8)) > ((float) parent2.get(8)) ) {
                    selectedParent.addAll(parent1);
                    selectedIndexParent = indexParent1;
                } else if ( ((float) parent1.get(8)) == ((float) parent2.get(8)) ) {
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
            TestSuiteController.simpleOffsprings.add(selectedParent);
            System.out.println("win parent: index "+selectedIndexParent+" - "+selectedParent+"\n");
        }
        
        // crossover.
        for (int i = 0; i < TestSuiteController.simpleOffsprings.size(); i++) {
            System.out.println("Offspring #"+(i+1)+": "+TestSuiteController.simpleOffsprings.get(i));
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
