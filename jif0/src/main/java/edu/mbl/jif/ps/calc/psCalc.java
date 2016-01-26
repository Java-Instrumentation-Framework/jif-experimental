/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.ps.calc;

/**
 *
 * @author GBH
 */
public class psCalc {

    double s = 0; //tanSwingAngleDiv2;


    public double retardance(double a, double b, double s) {
        return Math.atan(s * Math.sqrt(a * a + b * b));
    }


    double ref = 0; //azimRef;


    public double orientation(double a, double b, double ref) {
        return (ref + 90 + 90 * Math.atan2(a, b) / Math.PI) % 180;
    }

    // test
    static int n = 100;


    public static void main(String[] args) {

//        for (int i = 0; i < n; i++) {
//             // -2pi to +2pi
//            double range = 4*Math.PI;
//            double x = range / n * (double) (i - n / 2);
//            System.out.println(x);
//        }
       
        double range = Math.PI / 2;
        for (int i = 0; i < n; i++) {
            // -2pi to +2pi
            double x = range / n * (double) (i - n / 2);
            double math = Math.atan(x);
            double fast = atan_66(x);
            System.out.println(x + ": error= " + (math - fast));
        }

        long start = 0;

        start = System.nanoTime();
        for (int j = 0; j < 1000000; j++) {
            for (int i = 0; i < n; i++) {
                // -2pi to +2pi
                double x = range / n * (double) (i - n / 2);
                double math = Math.atan(x);
            }
        }
        long mathatantime = System.nanoTime() - start;
        System.out.println("Math.atan:            " + mathatantime);
        
        start = System.nanoTime();
        for (int j = 0; j < 1000000; j++) {
            for (int i = 0; i < n; i++) {
                // -2pi to +2pi
                double x = range / n * (double) (i - n / 2);
                double math = atan_66(x);
            }
        }
        long atan_66time = System.nanoTime() - start;
        System.out.println("atan_66:              " + atan_66time);
        
       System.out.println("Ratio: " + (float) atan_66time/ (float)mathatantime );
    }

    // <editor-fold defaultstate="collapsed" desc=">>>--- Fast Math Functions  ---<<<" >
    
    static final double pi = 3.1415926535897932384626433;	// pi
    static final double twopi = 2.0 * pi;			// pi times 2
    static final double two_over_pi = 2.0 / pi;                 // 2/pi
    static final double halfpi = pi / 2.0;			// pi divided by 2
    static final double threehalfpi = 3.0 * pi / 2.0;  		// pi times 3/2, used in tan routines
    static final double four_over_pi = 4.0 / pi;		// 4/pi, used in tan routines
    static final double qtrpi = pi / 4.0;			// pi/4.0, used in tan routines
    static final double sixthpi = pi / 6.0;			// pi/6.0, used in atan routines
    static final double tansixthpi = Math.tan(sixthpi);		// tan(pi/6), used in atan routines
    static final double twelfthpi = pi / 12.0;			// pi/12.0, used in atan routines
    static final double tantwelfthpi = Math.tan(twelfthpi);	// tan(pi/12), used in atan routines


    static double add1(double x) {
       double b = 1.0;
       return x + b;
    }
    
    static double atan_66(double x) {
        double y; // return from atan__s function
        boolean complement = false; // true if arg was >1
        boolean region = false; // true depending on region arg is in
        boolean sign = false; // true if arg was < 0
        if (x < 0) {
            x = -x;
            sign = true; // arctan(-x)=-arctan(x)
        }
        if (x > 1.0) {
            x = 1.0 / x; // keep arg between 0 and 1
            complement = true;
        }
        if (x > tantwelfthpi) {
            x = (x - tansixthpi) / (1 + tansixthpi * x); // reduce arg to under tan(pi/12)
            region = true;
        }
        y = atan_66s(x); // run the approximation
        if (region) {
            y += sixthpi;
        } // correct for region we're in
        if (complement) {
            y = halfpi - y;
        } // correct for 1/x if we did that
        if (sign) {
            y = -y;
        } // correct for negative arg
        return (y);
    }

    // atan_66s computes atan(x)
    // Accurate to about 6.6 decimal digits over the range [0, pi/12].
    static final double c1 = 1.6867629106;
    static final double c2 = 0.4378497304;
    static final double c3 = 1.6867633134;


    public static double atan_66s(double x) {
        double x2 = x * x;
        return (x * (c1 + x2 * c2) / (c3 + x2));
    }


    //===============================================================================
    /*
     * "The atan2 C++ function calculates the arctangent of the two variables y and x. 
    It is similar to calculating the arctangent of y/x, except that the signs of 
    both arguments are used to determine the quadrant of the result."
    Effectively, this means that atan2 finds the counterclockwise angle in 
    radians between the x-axis and the vector <x,y>
    This produces results in the range [-?,?], which can be mapped to [0,2?]
    by adding 2? to the negative values.
    As defined above, and traditionally, atan2(0,0) is undefined. 
     */
    public static double aTan2(double y, double x) {
        double coeff_1 = Math.PI / 4d;
        double coeff_2 = 3d * coeff_1;
        double abs_y = Math.abs(y);
        double angle;
        if (x >= 0d) {
            double r = (x - abs_y) / (x + abs_y);
            angle = coeff_1 - coeff_1 * r;
        } else {
            double r = (x + abs_y) / (abs_y - x);
            angle = coeff_2 - coeff_1 * r;
        }
        return y < 0d ? -angle : angle;
    }

// </editor-fold>
}
