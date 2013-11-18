/*
 * MinimalDynamicChart.java
 *
 * Created on October 11, 2006, 1:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.plot;

//import info.monitorenter.gui.chart.Chart2D;
//import info.monitorenter.gui.chart.ITrace2D;
//import info.monitorenter.gui.chart.traces.Trace2DLtd;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;


public class MinimalDynamicChart {
  private MinimalDynamicChart() {
    super();
  }
  
  public static void main(String[]args){
    // Create a chart:  
//    Chart2D chart = new Chart2D();
//    // Create an ITrace: 
//    // Note that dynamic charts need limited amount of values!!! 
//    ITrace2D trace = new Trace2DLtd(200); 
//    trace.setColor(Color.RED);
// 
//    // Add the trace to the chart: 
//    chart.addTrace(trace);
//    
//    // Make it visible:
//    // Create a frame. 
//    JFrame frame = new JFrame("MinimalDynamicChart");
//    // add the chart to the frame: 
//    frame.getContentPane().add(chart);
//    frame.setSize(400,300);
//    // Enable the termination button [cross on the upper right edge]: 
//    frame.addWindowListener(
//        new WindowAdapter(){
//          public void windowClosing(WindowEvent e){
//              System.exit(0);
//          }
//        }
//      );
//    frame.show(); 
//    // Every 50 milliseconds a new value is collected. 
//    //AbstractDataCollector collector = new RandomDataCollector(trace,50);
//    // Start a Thread that adds the values: 
//    //new Thread(collector).start();
  }
}