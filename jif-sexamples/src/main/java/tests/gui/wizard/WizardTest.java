package tests.gui.wizard;

import edu.mbl.jif.gui.wizard.Wizard;
import edu.mbl.jif.gui.wizard.WizardListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


/** Do the Wizard-thing
 * Using Christopher Brind's Java Wizard Framework
 */
public class WizardTest implements WizardListener {
  private static JFrame frame;

  /** The entry point in to the application. */
  public static void main(String[] args) {
    frame = new JFrame("~~The Wizard");
    frame.addWindowListener(createAppCloser());
    Wizard wizard = new Wizard();
    wizard.addWizardListener(new WizardTest());
    frame.setContentPane(wizard);
    frame.pack();
    frame.setVisible(true);
    wizard.start(new WelcomeWizardPanel());
  }


  private static WindowListener createAppCloser() {
    return new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
          System.out.println("~~Wizard Closed");
          System.exit(0);
        }
      };
  }


  /** Called when the wizard finishes.
   * @param wizard the wizard that finished.
   */
  public void wizardFinished(Wizard wizard) {
    System.out.println("~~Wizard Finished");
    System.exit(0);
  }


  /** Called when the wizard is cancelled.
   * @param wizard the wizard that was cancelled.
   */
  public void wizardCancelled(Wizard wizard) {
    System.out.println("~~Wizard Cancelled");
    System.exit(0);
  }


  /** Called when a new panel has been displayed in the wizard.
   * @param wizard the wizard that was updated
   */
  public void wizardPanelChanged(Wizard wizard) {
    System.out.println("~~Wizard Panel Changed");
  }
}
