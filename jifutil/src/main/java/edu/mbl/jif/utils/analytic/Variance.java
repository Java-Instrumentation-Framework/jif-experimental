package edu.mbl.jif.utils.analytic;

public class Variance {

  public static double variance(double[] samples) {
    double mean = 0;
    int n = samples.length;
    for (int i = 0; i < n; i++) {
      mean += samples[i];
    }
    mean = mean / n;
    double sumOfSquares = 0;
    for (int i = 0; i < n; i++) {
      sumOfSquares += Math.pow(samples[i] - mean, 2);
    }
    double v = Math.sqrt(sumOfSquares / n);
    return v;
  }

  public static double variance(float[] samples) {
    double mean = 0;
    int n = samples.length;
    for (int i = 0; i < n; i++) {
      mean += samples[i];
    }
    mean = mean / n;
    double sumOfSquares = 0;
    for (int i = 0; i < n; i++) {
      sumOfSquares += Math.pow(samples[i] - mean, 2);
    }
    double v = Math.sqrt(sumOfSquares / n);
    return v;
  }

  public static double variance(float[] samples, double mean) {
    mean = 0;
    int n = samples.length;
    for (int i = 0; i < n; i++) {
      mean += samples[i];
    }
    mean = mean / n;
    double sumOfSquares = 0;
    for (int i = 0; i < n; i++) {
      sumOfSquares += Math.pow(samples[i] - mean, 2);
    }
    double v = Math.sqrt(sumOfSquares / n);
    return v;
  }



  public static void main(String[] args) {
     double[] intensD = {1,2,3,4,5};
    System.out.println("Variance: " + variance(intensD));
    float[] intens = {15f, 67.5f, 72.3f, 68.4f, 92.3f};
    System.out.println("Variance: " + variance(intens));
    float[] intens2 = {67.5f, 72.3f, 68.4f, 70.3f};
    System.out.println("Variance: " + variance(intens2));
    float[] intens3 = {67.5f, 72.3f, 68.4f, 92.3f};
    double m = 0;
    System.out.println("Variance: " + variance(intens3, m));
    System.out.println("Mean: " + m);
  }
}
