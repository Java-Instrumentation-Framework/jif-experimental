package application;

import javax.swing.UIManager;

public class TestingApplication {
  boolean packFrame = true;

  /**Construct the application*/
  public TestingApplication() {
    TestingFrame frame = new TestingFrame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    frame.setVisible(true);
  }
  /**Main method*/
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    new TestingApplication();
  }
}
