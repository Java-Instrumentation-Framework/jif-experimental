package edu.mbl.jif.varilc;

/**
 *
 * @author GBH
 */
public class MockRetarder {

    public static MockRetarder[] create(int numIDs) {
        double parameterA = 0.00002;
        double minXA = 2000;
        double minimumA = 8;
        double parameterB = 0.00003;
        double minXB = 2300;
        double minimumB = 11;
        MockRetarder[] rets = new MockRetarder[numIDs];
        for (int i = 0; i < numIDs; i++) {
            rets[i] = new MockRetarder(
                    twiddle(parameterA, 0.1),
                    twiddle(minXA, 0.9),
                    twiddle(minimumA, 0.3),
                    twiddle(parameterB, 0.1),
                    twiddle(minXB, 0.9),
                    twiddle(minimumB,0.3));
            rets[i].dumpParms();
        }

        return rets;
    }

    public static double twiddle(double x, double f) {
        return x + x * ((Math.random() - 0.5) * f);

    }
    double parameterA;
    double minXA;
    private double minimumA;
    private double parameterB;
    private double minXB;
    private double minimumB;
    private double max = 254;

    public MockRetarder(double parameterA, double minXA, double minimumA,
            double parameterB, double minXB, double minimumB) {
        this.parameterA = parameterA;
        this.minXA = minXA;
        this.minimumA = minimumA;
        this.parameterB = parameterB;
        this.minXB = minXB;
        this.minimumB = minimumB;
    }

    public void dumpParms() {
        System.out.println(
                parameterA + ", " +
                minXA + ", " +
                minimumA + ", " +
                parameterB + ", " +
                minXB + ", " +
                minimumB);
    }

    
    double Awas = 0;
    double Bwas = 0;

    
//  public double calc(double a, double b) {
//      double combined = calc(a) + calc(b);
//        if (combined < max) {
//            return combined;
//        } else {
//            return 254;
//        }
//    }
  
    public double calc(double a, double b) {
        double yA = parameterA * Math.pow(a - minXA, 2.0) + minimumA;
        double yB = parameterB * Math.pow(b - minXB, 2.0) + minimumB;
        double result = yA + yB;
        if (result < max) {
            return result;
        } else {
            return 254;
        }        
    }
    public double calc(double x) {
        double yA = parameterA * Math.pow(x - minXA, 2.0) + minimumA;
        double yB = parameterB * Math.pow(x - minXB, 2.0) + minimumB;
        double result = yA + yB;
        if (result < max) {
            return result;
        } else {
            return 254;
        }
    }
    public double calcA(double x) {
      Awas = x;
        double yA = parameterA * Math.pow(x - minXA, 2.0) + minimumA;
        double yB = parameterB * Math.pow(Bwas - minXB, 2.0) + minimumB;
        double result = yA + yB;
        if (result < max) {
            return result;
        } else {
            return 254;
        }
    }    
    public double calcB(double x) {
      Bwas = x;
        double yA = parameterA * Math.pow(Awas - minXA, 2.0) + minimumA;
        double yB = parameterB * Math.pow(x - minXB, 2.0) + minimumB;
        double result = yA + yB;
        if (result < max) {
            return result;
        } else {
            return 254;
        }
    }     
  
    
    private void test() {
        int n = 50;
        double a = .25;
        double b = .5;
        for (int i = 0; i < n; i++) {
            a = (double) i /n;
            System.out.println(a + ", " + b + "->" + calc(a, b));
        }
        a = .25;
        for (int i = 0; i < n; i++) {
            b = (double) i /n;
            System.out.println(a + ", " + b + "->" + calc(a, b));
        }    }

    public static void main(String[] args) {
        double parameterA = 400;
        double minXA = .25;
        double minimumA = 7;
        double parameterB = 200;
        double minXB = .5;
        double minimumB = 8;
        MockRetarder mockRet = new MockRetarder(
                parameterA,
                minXA,
                minimumA,
                parameterB,
                minXB,
                minimumB);

            mockRet.test();

        


    }
}
