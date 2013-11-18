package edu.mbl.jif.camera;

import edu.mbl.jif.utils.analytic.FitLine;


/**
 * <p>Title: </p>
 * ((Unused))
 */
public class ExposureAdjuster
{
   public ExposureAdjuster () {
   }


   public static double measure (double exposure) {
      int z = 6;
      double f = 1.244;
      return exposure * f + z;
   }


   public static void main (String[] args) {

      double zeroIntensity = 6;

      double intensity = 0;
      double exposure = 0;
      //add a pref getInt("startingDecade", 1);
      //add a pref getInt("decades", 1) ;
      int decades = 5;
      int numMeasurements = 0;
      double[] y = new double[decades];
      double[] x = new double[decades];
      //Measure Intensity for x = 10^1thru 10^6 microseconds (1 second)
      System.out.println("Exposure  =>  Intensity");
      for (int i = 0; i < decades; i++) {
         exposure = Math.pow(10, i);
         intensity = measure(exposure);
         if(intensity >= 255) break;
         numMeasurements++;
         x[i] = exposure;
         y[i] = intensity - zeroIntensity;
         System.out.println(x[i] + "  =>  " + y[i]);
      }

      // Fit to a line
      double[] parameters = {0, 0, 0, 0};
      FitLine.fit(parameters, x, y, null, null, numMeasurements);
      System.out.println("Intercept: " + parameters[0]);
      System.out.println("Slope: " + parameters[1]);
      System.out.println("Error Intercept: " + parameters[2]);
      System.out.println("Error Slope: " + parameters[3]);
      double intercept = parameters[0];
      double slope = parameters[1];
      // y = a + mx; x = (y - a) / m
      exposure = (255  - intercept) / slope;
      System.out.println("Saturation Exposure = " + exposure);
      exposure = (127  - intercept) / slope;
      System.out.println("Half Sat. Exposure = " + exposure);
   }
}
