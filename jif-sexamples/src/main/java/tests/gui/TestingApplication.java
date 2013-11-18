package tests.gui;

import java.awt.Dimension;
import javax.swing.UIManager;

public class TestingApplication {
    boolean packFrame = true;

    /**Construct the application*/
    public TestingApplication() {
        TestingFrame frame = new TestingFrame();
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        //frame.setPreferredSize(new Dimension(300, 400));
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }

        frame.setVisible(true);
    }

    /**Main method*/
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new TestingApplication();
    }
}
