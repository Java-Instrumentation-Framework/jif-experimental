/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.plot;

/*
 *  AdvancedStaticChart  jchart2d
 *  Copyright (C) Achim Westermann, created on 10.12.2004, 13:48:55
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 * 
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 *  If you modify or optimize the code in a useful way please let me know.
 *  Achim.Westermann@gmx.de
 *
 */

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.axis.AAxis;
import info.monitorenter.gui.chart.axis.AxisLinear;
import info.monitorenter.gui.chart.labelformatters.LabelFormatterDate;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DSimple;
import info.monitorenter.gui.chart.traces.painters.TracePainterVerticalBar;
import info.monitorenter.gui.chart.views.ChartPanel;
import info.monitorenter.util.Range;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Demonstrates advanced features of static charts in jchart2d.
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * 
 */
public final class AdvancedStaticChart {

  /**
   * Application startup hook.
   * <p>
   * 
   * @param args
   *          ignored
   * 
   * @throws ParseException
   *           if sth. goes wrong.
   * 
   */
  public static void main(final String[] args) throws ParseException {
    // Create a chart:
    Chart2D chart = new Chart2D();
    // Create an ITrace:
    ITrace2D trace = new Trace2DSimple();
    trace.setTracePainter(new TracePainterVerticalBar(2, chart));
    AAxis yAxis = new AxisLinear();
    Font titleFont = UIManager.getDefaults().getFont("Label.font").deriveFont(14f).deriveFont(
        Font.BOLD);
    //yAxis.getTitlePainter().setTitleFont(titleFont);
    yAxis.setTitle("hoppelhase");
    chart.setAxisY(yAxis);
    yAxis.setFormatter(new LabelFormatterDate(new SimpleDateFormat()));
    IAxis xAxis = chart.getAxisX();
    xAxis.setTitle("emil");
    //xAxis.getTitlePainter().setTitleFont(titleFont);
    xAxis.setRangePolicy(new RangePolicyFixedViewport(new Range(0, 220)));
    // Add all points, as it is static:
    double high = System.currentTimeMillis();
    for (double i = 0; i < 200; i++) {
      trace.addPoint(i, high);
      high += 1000 * 50;

    }
    // Add the trace to the chart:
    chart.addTrace(trace);

    // Make it visible:
    // Create a frame.
    JFrame frame = new JFrame("AdvancedStaticChart");
    // add the chart to the frame:
    frame.getContentPane().add(new ChartPanel(chart));
    frame.setSize(600, 600);
    // Enable the termination button [cross on the upper right edge]:
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(final WindowEvent e) {
        System.exit(0);
      }
    });
    frame.setVisible(true);
  }

  /**
   * Utility constructor.
   * <p>
   */
  private AdvancedStaticChart() {
    super();
  }
}
