/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import helpers.Func;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import views.ProgressBar;
import views.TestSuite01;
import views.TestSuite02;

/**
 *
 * @author umar
 */
public class ThreadController implements Runnable {
    
    private Thread t;
    private String status = "99";
    
    public ThreadController() {
        this.status = "99";
    }
    
    public ThreadController(String stat) {
        this.status = stat;
    }

    @Override
    public void run() {
        if (status == "99") {
            progressBar();
        } else if (status == "ts1") {
            TestSuite01_1();
        }
    }
    
    private void progressBar() {
        for (int i = 0; i < 100; i++) {
            TestSuite01.pb.setValue(i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void TestSuite01_1() {
        TestSuiteController.clearParents();
        int numberOfTestSuite = 0;
        try {

            numberOfTestSuite = Integer.parseInt(TestSuite01.txtNoTestSuite.getText());

            TestSuite01.viewTitle(true, "Generating Test Suites / Parents");

            for (int i = 0; i < numberOfTestSuite; i++) {

                TestSuite01.viewText(false, "Test Suite #" + (i + 1));

                int numberOfPath = 0;
                try {

                    String matrix = TestSuite01.prevMatrix;
                    boolean isValidMatrix = TestSuiteController.isValidMatrix(matrix);
                    if (isValidMatrix) {

                        Random rand = new Random();
                        int from = Integer.parseInt(TestSuite01.txtNoTestCaseFrom.getText());
                        int to = Integer.parseInt(TestSuite01.txtNoTestCaseTo.getText());

                        numberOfPath = rand.nextInt(to - from + 1) + from;

                        if (numberOfPath <= 0 || numberOfPath >= TestSuite01.totalMaximumPath) {
                            throw new Exception();
                        } else {

                            long startTime = System.currentTimeMillis();

                            Properties props = TestSuiteController.calcPRA01(numberOfPath);

                            if (TestSuite01.cbTotalPath.isSelected()) {
                                TestSuite01.viewText(false, "Total Paths: " + props.getProperty(Func.TOTAL_NUMBER_PATH));
                            }

                            long endTime = System.currentTimeMillis();
                            long diffTime = endTime - startTime;
//                            lblTimeExec.setText("Exection Time: " + diffTime + " ms");

                            if (TestSuite01.cbTimeExec.isSelected()) {
                                props.setProperty(Func.TOTAL_TIME_EXECUTION, "" + diffTime);

                                TestSuite01.viewText(false, "Total Exec. Time: " + diffTime + " ms");
                            }

                            if (TestSuite01.cbTransCoverage.isSelected()) {
                                float A1 = Float.parseFloat((String) props.getProperty(Func.TOTAL_ALL_TRANSITIONS));
                                float A2 = Float.parseFloat((String) props.getProperty(Func.TOTAL_TRANSITIONS_PATH));
                                float X = (A2 > 0.0f && A1 > 0.0f) ? (A2 * 1.0f / A1) * 100.0f : 0.0f;

                                props.setProperty(Func.TOTAL_TRANS_COVERAGE, "" + X);

                                TestSuite01.viewText(false, "Total All Transitions: " + props.getProperty(Func.TOTAL_ALL_TRANSITIONS));
                                TestSuite01.viewText(false, "Total Transitions in Path: " + props.getProperty(Func.TOTAL_TRANSITIONS_PATH));
                                TestSuite01.viewText(false, "Transition Coverage: " + Func.df.format(X) + " %");
                            }

                            if (TestSuite01.cbTransPairCoverage.isSelected()) {
                                float B1 = Float.parseFloat((String) props.getProperty(Func.TOTAL_ALL_PAIRS));
                                float B2 = Float.parseFloat((String) props.getProperty(Func.TOTAL_PAIRS_PATH));
                                float Y = (B2 > 0.0f && B1 > 0.0f) ? (B2 * 1.0f / B1) * 100.0f : 0.0f;

                                props.setProperty(Func.TOTAL_TRANS_PAIR_COVERAGE, "" + Y);

                                TestSuite01.viewText(false, "Total All Pairs: " + props.getProperty(Func.TOTAL_ALL_PAIRS));
                                TestSuite01.viewText(false, "Total Pairs in Path: " + props.getProperty(Func.TOTAL_PAIRS_PATH));
                                TestSuite01.viewText(false, "Transition Pair Coverage: " + Func.df.format(Y) + " %");
                            }

                            // only take parent that at least have one path.
                            if (Integer.parseInt((String) props.getProperty(Func.TOTAL_NUMBER_PATH)) > 0) {
                                if (!TestSuite01.cbTotalPath.isSelected()) {
                                    props.remove(Func.TOTAL_NUMBER_PATH);
                                }
                                TestSuiteController.setParents(props);
                            }

                            TestSuite01.viewText(false, "\n");
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

        if (TestSuiteController.parents.size() > 0) {
            new TestSuite02().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Generate parents first!", "Parent Not Found", 0);
        }
    }
    
    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }    
}
