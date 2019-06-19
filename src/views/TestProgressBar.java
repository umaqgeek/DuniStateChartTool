/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author umar
 */
public class TestProgressBar {
    public static JProgressBar pb = new JProgressBar();
    public static void main(String args[]) {
        final int MAX = 100;
        final JFrame frame = new JFrame("Progress Bar");

        // creates progress bar
        pb.setMinimum(0);
        pb.setMaximum(MAX);
        pb.setStringPainted(true);

        // add progress bar
        frame.setLayout(new FlowLayout());
        frame.getContentPane().add(pb);
        frame.setAlwaysOnTop(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 100);
//        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // update progressbar
        final int currentValue = 1;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pb.setValue(currentValue);
            }
        });
    }
}
