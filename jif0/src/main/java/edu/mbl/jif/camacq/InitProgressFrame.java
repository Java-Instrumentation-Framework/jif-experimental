/*
 * InitProgressFrame.java
 * CamAcqJ Startup process and monitor
 *
 */
package edu.mbl.jif.camacq;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
//if JVM < 1.6, import org.jdesktop.swingx.util.SwingWorker;

public class InitProgressFrame
        extends JFrame {

    static JFrame frame;

    public InitProgressFrame(ApplicationFrame mainFrame) throws Exception {

        frame = new JFrame("Initialization");

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Create the Task & ProgressBar
        InitializationTask task = new InitializationTask(textArea, mainFrame);
        final JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        task.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("progress".equals(evt.getPropertyName())) {
                            progressBar.setIndeterminate(false);
                            progressBar.setValue((Integer) evt.getNewValue());
                        }
                    }
                });
        //
        frame.add(progressBar, BorderLayout.NORTH);
        JLabel jLabel1 = new JLabel();
        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camacq/AsterColorSoft.gif"))); // NOI18N
        jLabel1.setPreferredSize(new Dimension(250, 150));
        frame.add(jLabel1, BorderLayout.SOUTH);

        frame.setSize(350, 500);
        frame.setVisible(true);
        // Do it...
        task.execute();

        return;
    }

    /**
     * SwingWorker<T,V>
     * <T> - the result type returned by this SwingWorker's doInBackground and get methods
     * <V> - the type used for carrying out intermediate results by this 
     * SwingWorker's publish and process methods
     */
    public static class InitializationTask
            extends SwingWorker<Integer, String> {

        private final JTextArea textArea;
        private final ApplicationFrame mainFrame;
        InstrumentController instCtrl;

        InitializationTask(JTextArea textArea, ApplicationFrame mainFrame) {
            this.textArea = textArea;
            this.mainFrame = mainFrame;
        }
        int stepsTotal = 18;
        int stepsDone = 0;

        @Override
        public Integer doInBackground() {
            updateProgress("Initializing Instrument Controller...");
            instCtrl = new InstrumentController(mainFrame);
            instCtrl.initialize(this);
            return 0;
        }

        public void updateProgress(String msg) {
          try {
            stepsDone++;
            setProgress(100 * stepsDone / stepsTotal);
            publish(msg);  // publish() calls process()
          } catch (Exception e) {
          }
        }

        @Override
        protected void process(List<String> msgs) {
            for (String msg : msgs) {
                textArea.append(msg + "\n");
            }
        }

        @Override
        protected void done() {
            instCtrl.finish();
            //frame.setVisible(false);
            frame.disable();

        }
    }

    public static void main(String[] args) {
        try {
            new InitProgressFrame(null);
        } catch (Exception ex) {
            Logger.getLogger(InitProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

