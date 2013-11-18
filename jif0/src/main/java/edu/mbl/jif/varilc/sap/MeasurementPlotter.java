package edu.mbl.jif.varilc.sap;


import edu.mbl.jif.io.csv.CSVFileWrite;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.TracePoint2D;
import info.monitorenter.gui.chart.ZoomableChart;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.computing.Trace2DArithmeticMean;
import info.monitorenter.gui.chart.traces.painters.TracePainterDisc;
import info.monitorenter.gui.chart.traces.painters.TracePainterLine;
import info.monitorenter.gui.chart.views.ChartPanel;
import info.monitorenter.util.Range;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author GBH
 */
public class MeasurementPlotter {

    Trace2DSimple[] trace;
    ITrace2D[] traceArithmeticMean;
    String title;
    int numIDs;
    int currentPoint = 0;
    String filename = "";
    CSVFileWrite dataFile;
    private boolean addMean = false;


    public MeasurementPlotter(final int numIDs, String filename) {
        this.numIDs = numIDs;
        currentPoint = 0;
        System.out.println("datFile: " + filename);
        if (filename != null) {
            dataFile = new CSVFileWrite(filename);
        }
    }


    public MeasurementPlotter(final String title, final int numIDs,
                               String filename, Range range, Rectangle bounds) {
        this.title = title;
        this.numIDs = numIDs;
        trace = new Trace2DSimple[numIDs];
        traceArithmeticMean = new Trace2DArithmeticMean[numIDs];
        currentPoint = 0;
        System.out.println("datFile: " + filename);
        if (filename != null) {
            dataFile = new CSVFileWrite(filename);
        }
        openChartFrame(range, bounds);
    }


    public void openChartFrame(final Range range, final Rectangle bounds) {
        // Chart2D chart = new Chart2D();
        ZoomableChart chart = new ZoomableChart();
        chart.setPaintLabels(false);
        //chart.getAxisY().setRange(new Range(0.0, 1.0));
        chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(range));
        //chart.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0, 256)));
        // Note that dynamic charts need limited amount of values!!!
        for (int i = 0; i < numIDs; i++) {
            trace[i] = new Trace2DSimple();
            trace[i].setColor(Color.getHSBColor((float) i / numIDs,
                    0.8f,
                    0.8f));
            trace[i].setTracePainter(new TracePainterDisc());
            ///trace[i].setTracePainter(new TracePainterLine());
            //trace[i].setTracePainter(new TracePainterDisc());
            chart.addTrace(trace[i]);
            if (addMean) {
                // Create an arithmetic mean trace.
                traceArithmeticMean[i] = new Trace2DArithmeticMean(4);
                // hook it to the initial trace:
                trace[i].addComputingTrace(traceArithmeticMean[i]);
                // Add the arithmetic mean trace to the chart too:
                chart.addTrace(traceArithmeticMean[i]);
            }

        }

        JFrame frame = new JFrame(title + " - intensity (ROI avg.)");
        try {
            frame.setIconImage((new javax.swing.ImageIcon(getClass().getResource("/edu/mbl/jif/camera/icons/plot.png"))).getImage());
        } catch (Exception ex) {
        }
        ChartPanel chartPanel = new ChartPanel(chart);
        frame.getContentPane().add(chartPanel);
        frame.setBounds(bounds);
        //frame.setSize(400, 300);
        // Enable the termination button [cross on the upper right edge]:
        frame.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        trace = null;
                    }


                });
        frame.setLocation(500, 20);
        frame.setVisible(true);
    }


    public void recordData(int id, int point,
                            double setting, double measurement) {
        currentPoint++;
        // if (trace[id] != null) {
        //System.out.println("in recordData: " + setting + ", " + measurement);
        TracePoint2D tp = new TracePoint2D(setting, measurement);

        trace[id].addPoint(tp);
        //}
        if (dataFile != null) {
            Object[] d = new Object[]{id, point, (int) setting, measurement};
            dataFile.writeRow(d);
        }

    }


    public void recordData(int id, int iteration, double measurement) {
        currentPoint++;
        if (trace[id] != null) {
            trace[id].addPoint(new TracePoint2D(currentPoint, measurement));
        }

        if (dataFile != null) {
            Object[] d = new Object[]{iteration, id, currentPoint, (int) measurement};
            dataFile.writeRow(d);
        }

    }


    public void closeTextFile() {
        if (dataFile != null) {
            dataFile.close();
        }

    }


}
