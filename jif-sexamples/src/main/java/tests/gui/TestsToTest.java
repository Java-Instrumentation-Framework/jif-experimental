/*
 * TestsToTest.java
 *
 * Created on May 2, 2006, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.gui;


/**
 *
 * @author GBH
 */
public class TestsToTest {
    TestCommandlButtons panelActionButtons;

    /** Creates a new instance of TestsToTest */
    public TestsToTest(TestCommandlButtons panelActionButtons) {
        this.panelActionButtons = panelActionButtons;
        panelActionButtons.addButton("DoItToIt", this, "doItToIt();");
    }

    public void doItToIt() {
        System.out.println("I've been done.");
    }

    //      public void addButton(String name, Object obj, String method) {
    //      JButton button = new JButton(name);
    //      try {
    //         button.addActionListener(Command.parse(obj, method));
    //      } catch (IOException ex) {
    //         ex.printStackTrace();
    //      }
    //      this.add(button);
    //}
}
