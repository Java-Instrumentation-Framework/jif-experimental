package edu.mbl.jif.varilc;

import edu.mbl.jif.io.csv.CSVFileWrite;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author GBH
 */
public class MeasurementPlotter_VLC {
    String title;
    // plot
    private TimeSeries series;
    int currentPoint = 0;
    // Data file
    CSVFileWrite dataFile;
    String filename = "";
    
    private boolean addMean = false;

    public MeasurementPlotter_VLC(String filename) {
        currentPoint = 0;
        System.out.println("datFile: " + filename);
        if (filename != null) {
            dataFile = new CSVFileWrite(filename);
        }
    }

    public MeasurementPlotter_VLC(final String title,
                                  String filename, double min, double max, Rectangle bounds) {
        this.title = title;
        currentPoint = 0;
        System.out.println("datFile: " + filename);
        if (filename != null) {
            dataFile = new CSVFileWrite(filename);
        }
        this.series = new TimeSeries("Intensity", Millisecond.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final JFreeChart chart = createChart(dataset, min, max);
        final ChartPanel chartPanel = new ChartPanel(chart);
        openChartFrame(bounds, chartPanel);
    }

    private JFreeChart createChart(final XYDataset dataset, final double min, final double max) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Measurement Plotter",
            "Time",
            "Intensity",
            dataset,
            true,
            true,
            false);
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(30000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(min, max);
        return result;
    }

    public void openChartFrame(final Rectangle bounds, final ChartPanel chartPanel) {
//        // Chart2D chart = new Chart2D();
//        ZoomableChart chart = new ZoomableChart();
//        chart.setPaintLabels(false);
//        //chart.getAxisY().setRange(new Range(0.0, 1.0));
//        chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(range));
//        //chart.getAxisY().setRangePolicy(new RangePolicyMinimumViewport(new Range(0, 256)));
//        // Note that dynamic charts need limited amount of values!!!
//
//
//        trace.addTracePainter(new TracePainterDisc());
//
//        trace.addTracePainter(new TracePainterLine());
//        //trace[i].setTracePainter(new TracePainterDisc());
//        chart.addTrace(trace);
//        if (addMean) {
//            // Create an arithmetic mean trace.
//            traceArithmeticMean = new Trace2DArithmeticMean(4);
//            // hook it to the initial trace:
//            trace.addComputingTrace(traceArithmeticMean);
//            // Add the arithmetic mean trace to the chart too:
//            chart.addTrace(traceArithmeticMean);
//        }



        JFrame frame = new JFrame(title + " - intensity (ROI avg.)");
        try {
            frame.setIconImage((new javax.swing.ImageIcon(getClass().getResource(
                "/edu/mbl/jif/camera/icons/plot.png"))).getImage());
        } catch (Exception ex) {
        }

        frame.getContentPane().add(chartPanel);
        frame.setBounds(bounds);
        //frame.setSize(400, 300);
        // Enable the termination button [cross on the upper right edge]:
        frame.addWindowListener(
            new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    series.clear();
                    series = null;
                }

            });
        frame.setLocation(500, 20);
        frame.setVisible(true);
    }

//    public void recordData(int point, double setting, double measurement) {
//        currentPoint++;
//        // if (trace[id] != null) {
//        System.out.println("in recordData: " + setting + ", " + measurement);
//       // TracePoint2D tp = new TracePoint2D(setting, measurement);
//
//        //trace.addPoint(tp);
//        //}
//        if (dataFile != null) {
//            Object[] d = new Object[]{point, (int) setting, measurement};
//            dataFile.writeRow(d);
//        }
//
//    }
    public void recordData(int iteration, double measurement) {
        currentPoint++;
        if (series != null) {
            final Millisecond now = new Millisecond();
            //System.out.println("Now = " + now.toString());
            series.add(now, measurement);
        }
        if (dataFile != null) {
            Object[] d = new Object[]{iteration, currentPoint, (int) measurement};
            dataFile.writeRow(d);
        }

    }

    public void recordData(final double measurement) {
        currentPoint++;
        if (series != null) {
            final Millisecond now = new Millisecond();
            //System.out.println("Now = " + now.toString());
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        series.add(now, measurement);
                    } catch (Exception e) {
                    }
                }

            });
        }
    }

    public void setColor(Color color) {
        //  trace.setColor(color);
    }

    public void closeTextFile() {
        if (dataFile != null) {
            dataFile.close();
        }

    }

}
