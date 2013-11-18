package edu.mbl.jif.laser;

import edu.mbl.jif.stage.*;
import edu.mbl.jif.gui.spatial.Path;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class SurgeonTaskFrame
        extends JFrame
        implements ActionListener {

    private final GridBagConstraints constraints;
    private final JTextField xText,  yText;
    private final Border border = BorderFactory.createLoweredBevelBorder();
    private final JButton startButton,  stopButton;
    private FlipTask flipTask;    // timeInterval in milliseconds
    StageXYController xyCtrl;
    Iterator<Point2D> points;
    Path path;
    long timeInterval;
    Operation op;

    public SurgeonTaskFrame(StageXYController xyCtrl, final Path path, long timeInterval, Operation op) {
        super("Surgeon");
        this.xyCtrl = xyCtrl;
        this.path = path;
        this.timeInterval = timeInterval;
        this.op = op;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Make text boxes
        getContentPane().setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 10, 3, 10);
        xText = makeText();
        yText = makeText();
        //Make buttons
        startButton = makeButton("Start");
        stopButton = makeButton("Cancel");
        stopButton.setEnabled(false);
        //Display the window.
        pack();
        setVisible(true);
    }

    private JTextField makeText() {
        JTextField t = new JTextField(10);
        t.setEditable(false);
        t.setHorizontalAlignment(JTextField.RIGHT);
        t.setBorder(border);
        getContentPane().add(t, constraints);
        return t;
    }

    private JButton makeButton(String caption) {
        JButton b = new JButton(caption);
        b.setActionCommand(caption);
        b.addActionListener(this);
        getContentPane().add(b, constraints);
        return b;
    }

    private static class XYCoord {

        private final double xCoord,  yCoord;

        XYCoord(double xCoord, double yCoord) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }

    }

    private class FlipTask
            extends SwingWorker<Void, XYCoord> {

        @Override
        protected Void doInBackground() {
            double xCoord = 0;
            double yCoord = 0;
            points = path.getPoints();
            while (!isCancelled() && points.hasNext()) {
                Point2D element = points.next();
                xCoord = element.getX();
                yCoord = element.getY();
                publish(new XYCoord(xCoord, yCoord));
                //System.out.printf("%.4f %.4f \n", element.getX(), element.getY());
                xyCtrl.moveToConstantVelocity(element.getX(), element.getY());
                if (op != null) {
                    op.perform();
                }
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }

        @Override
        protected void process(List<XYCoord> coord) {
            XYCoord pair = coord.get(coord.size() - 1);
            xText.setText(String.format("%g", pair.xCoord));
            yText.setText(String.format("%g", pair.yCoord));

        }

        @Override
        protected void done() {
            try {
                get();
                System.out.println("All Done");
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            } catch (CancellationException e) {
                System.out.println("Cancelled");
                return;
            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        if ("Start" == e.getActionCommand()) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            (flipTask = new FlipTask()).execute();
        } else if ("Cancel" == e.getActionCommand()) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            flipTask.cancel(true);
            flipTask = null;
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                //new SurgeonTaskFrame();
            }

        });
    }

}
