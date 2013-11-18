package edu.mbl.jif.joystick;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.SwingUtilities;

public class AxisListener {

    de.hardcode.jxinput.Axis axis;
    double lastValue = 0;
    double accumValue = 0;
    DIAxisTransform axisTransform;
    String axisName;
    ArrayList xyInputListeners = new ArrayList<XYInputListener>();

    public AxisListener(de.hardcode.jxinput.Axis a) {
        super();
        axis = a;
        axisTransform = new DefaultAxisTransform();
        lastValue = axisTransform.valueOf(axis.getValue());
        axisName = a.getName();
        System.out.println("AxisListener: " + axisName);
    }

    public void update() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                double currentValue = axisTransform.valueOf(axis.getValue());
                double deltaValue = currentValue - lastValue;
                if (deltaValue > 100000.0 || deltaValue < -100000) {
                } else {
                    accumValue = accumValue + deltaValue;
                    for (Iterator<XYInputListener> xyL = xyInputListeners.iterator(); xyL.hasNext();) {
                        xyL.next().updateAxis(axisName, deltaValue,
                            accumValue);
                    }
                }
                lastValue = currentValue;
            }

        });

    }

    public void addListener(XYInputListener xyInputListener) {
        xyInputListeners.add(xyInputListener);
    }

    /** The null axis transform. */
    class DefaultAxisTransform implements DIAxisTransform {

        public double valueOf(double x) {
            return x;
        }

    }
}
