package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.process.parallel.listen.CompletionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestingApplication implements CompletionListener {

    boolean packFrame = true;
final JButton cancelButton = new JButton("Cancel");
    /**
     * Construct the application
     */
    public TestingApplication() {
       
        final JFrame frame = new JFrame();
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        //frame.setPreferredSize(new Dimension(300, 400));

        final MagOrtReprocessParallel proc = new MagOrtReprocessParallel();
        //final MagOrtReprocessParallel proc = new MagOrtReprocessParallel();
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    // Add Cancellability to process
                    
                    cancelButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            proc.cancel();
                        }
                    });
                    frame.add(cancelButton);
                    //
                    if (packFrame) {
                        frame.pack();
                    } else {
                        frame.validate();
                    }
                    frame.setVisible(true);
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(TestingApplication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(TestingApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        proc.runTest(this);
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        new TestingApplication();
    }

   public void handleCompletionEvent(EventObject e) {
      this.cancelButton.setEnabled(false);
      this.cancelButton.setText("Done.");
   }
}
