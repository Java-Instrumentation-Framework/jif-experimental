/*
 * TaskTemplate.java
 * SwingWorker task template
 * Created on June 3, 2006, 7:10 PM
 */
package edu.mbl.jif.gui.swingthread;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
//if JVM < 1.6, import org.jdesktop.swingx.util.SwingWorker;
public class TaskProgressFrame
        extends JFrame {

    public TaskProgressFrame(String[] args) throws Exception {
        JFrame frame = new JFrame("Prime Numbers Demo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Create the Task & ProgressBar
        PrimeNumbersTask task = new PrimeNumbersTask(textArea, 10000);
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
        frame.setSize(500, 500);
        frame.setVisible(true);
        // Do it...
        task.execute();

        return;
    }

    /**
     * Finds first N prime numbers.
     * SwingWorker<T,V>
     * <T> - the result type returned by this SwingWorker's doInBackground and get methods
     * <V> - the type used for carrying out intermediate results by this 
     * SwingWorker's publish and process methods
     */
    static class PrimeNumbersTask
            extends SwingWorker<List<Integer>, Integer> {

        final int numbersToFind;
        //sorted list of consequent prime numbers
        private final List<Integer> primeNumbers;
        private final JTextArea textArea;

        PrimeNumbersTask(JTextArea textArea, int numbersToFind) {
            this.textArea = textArea;
            this.numbersToFind = numbersToFind;
            this.primeNumbers = new ArrayList<Integer>(numbersToFind);
        }

        @Override
        public List<Integer> doInBackground() {
            int number = 2;
            while (primeNumbers.size() < numbersToFind && !isCancelled()) {
                if (isPrime(number)) {
                    primeNumbers.add(number);
                    setProgress(100 * primeNumbers.size() / numbersToFind);
                    publish(number);  // publish() calls process()
                }
                number++;
            }
            return primeNumbers;
        }

        @Override
        protected void process(List<Integer> chunks) {
            for (int number : chunks) {
                textArea.append(number + "\n");
            }
        }

        private boolean isPrime(int number) {
            for (int prime : primeNumbers) {
                if (number % prime == 0) {
                    return false;
                }
            }
            return true;
        }

    }
    public static void main(String[] args) {
        try {
            new TaskProgressFrame(null);
        } catch (Exception ex) {
            Logger.getLogger(TaskProgressFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
    MyTask task = new MyTask(textArea, 10000);
    //...
    task.execute();
    
static class MyTask extends SwingWorker {

    final int theNumber;

    MyTask(int theNumber) {
        this.theNumber = theNumber;
    }

    @Override
    public void doInBackground() {
    }
}
 */