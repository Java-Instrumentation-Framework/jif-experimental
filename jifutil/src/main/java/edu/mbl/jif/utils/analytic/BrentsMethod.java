package edu.mbl.jif.utils.analytic;


/* Brent's Method, Golden-Section Minization
 * Multi-threaded version... using CountDownLatch
 * GBH
 */
import java.util.logging.Logger;

public class BrentsMethod {

    final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /*
     * @todo if hit maxIters, throw exception ?
     */
    /*************************************************************************
    Minimization by the Brent method
    Input parameters:
    id - identifier for multiple threading...
    a - left boundary of an interval to search minimum in.
    b - right boundary of an interval to search minimum in.
    epsilon � absolute error of the value of the function minimum.
    For min. of a function, epsilon = 1.2e-16; // assuming 1.1102e-16 is machine precision
    maxIters - maximum number of iterations - prevents un-ending process
     * 
    Output parameters:
    XMin - point of minimum.
    The result:m  function value at the point of minimum.
     *************************************************************************/
    private double xmin;
    // function to minimize, passes int (index to identifier) and double
    private Function function;


    public BrentsMethod(Function function) {
        this.function = function;
    }


    public double minimize(
            double a,
            double b,
            double epsilon, int maxIters) {

        double result = 0;
        double ia = 0;
        double ib = 0;
        double bx = 0;
        double d = 0;
        double e = 0;
        double etemp = 0;
        double fu = 0;
        double fv = 0;
        double fw = 0;
        double fx = 0;
        int iter = 0;
        double p = 0;
        double q = 0;
        double r = 0;
        double u = 0;
        double v = 0;
        double w = 0;
        double x = 0;
        double xm = 0;
        double cGold = 0;

        cGold = 0.3819660;
        bx = 0.5 * (a + b);
        if (a < b) {
            ia = a;
        } else {
            ia = b;
        }
        if (a > b) {
            ib = a;
        } else {
            ib = b;
        }
        v = bx;
        w = v;
        x = v;
        e = 0.0;
        fx = function.f(x);  //  <<< ============= Function
        fv = fx;
        fw = fx;
        for (iter = 1; iter <= maxIters; iter++) {
            xm = 0.5 * (ia + ib);
//            if (Math.abs(x - xm) <= epsilon * 2 - 0.5 * (ib - ia)) {
//                break;
//            }
            if (Math.abs(x - xm) <= 2 * epsilon - 0.5 * (ib - ia)) {
                break;
            }
            if (Math.abs(e) > epsilon) {
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2 * (q - r);
                if (q > 0) {
                    p = -p;
                }
                q = Math.abs(q);
                etemp = e;
                e = d;
                if (!(Math.abs(p) >= Math.abs(0.5 * q * etemp) | p <= q * (ia - x) | p >= q * (ib - x))) {
                    d = p / q;
                    u = x + d;
                    if (u - ia < epsilon * 2 | ib - u < epsilon * 2) {
                        d = mysign(epsilon, xm - x);
                    }
                } else {
                    if (x >= xm) {
                        e = ia - x;
                    } else {
                        e = ib - x;
                    }
                    d = cGold * e;
                }
            } else {
                if (x >= xm) {
                    e = ia - x;
                } else {
                    e = ib - x;
                }
                d = cGold * e;
            }
            if (Math.abs(d) >= epsilon) {
                u = x + d;
            } else {
                u = x + mysign(epsilon, d);
            }
            fu = function.f(u);   //  <<< ============= Function
            if (fu <= fx) {
                if (u >= x) {
                    ia = x;
                } else {
                    ib = x;
                }
                v = w;
                fv = fw;
                w = x;
                fw = fx;
                x = u;
                fx = fu;
            } else {
                if (u < x) {
                    ia = u;
                } else {
                    ib = u;
                }
                if (fu <= fw | w == x) {
                    v = w;
                    fv = fw;
                    w = u;
                    fw = fu;
                } else {
                    if (fu <= fv | v == x | v == 2) {
                        v = u;
                        fv = fu;
                    }
                }
            }
        }
        xmin = x;
        // for debug...
        //logger.info("stopSignal.getCount() = " + stopSignal.getCount());
        //System.out.println("stopSignal.getCount() = " + stopSignal.getCount() + " for id= " + id);
        result = fx;
        return result;
    }


    private static double mysign(double a, double b) {
        double result = 0;
        if (b > 0) {
            result = Math.abs(a);
        } else {
            result = -Math.abs(a);
        }
        return result;
    }


    public double getMinX() {
        return xmin;
    }


    public static void main(String[] args) {
        final Function function = new Function() {

            public double f(double x) {
                System.out.println(x);
                return 3 * x * x - 2;
            }


        };
        BrentsMethod bm = new BrentsMethod(function);
        double min = bm.minimize(-20, 33, 1.2e-16, 100);
        System.out.println(min + " at " + bm.getMinX());
    }


}
