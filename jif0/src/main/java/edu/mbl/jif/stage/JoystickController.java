package edu.mbl.jif.stage;

/**
 *
 * @author GBH
 */
public class JoystickController {

    static int axisValue = 0;
    static double sensitivity = 0.1;
    private static boolean moving;
    static double baseVelocity = 0.1;
    static double maxVelocity = 3.0;
    static double velocityNow = 0.5;
    static double velocityBandWidth = 0.1;
    static int threshold = 100;
    static int maxDelta = 5000;
    static int rangeDelta = maxDelta - threshold;
    static double rangeVelocity = maxVelocity - baseVelocity;
    static int numberOfBands = (int) Math.round(rangeVelocity / velocityBandWidth);
    static double factorVelocityToDelta = rangeVelocity / rangeDelta;
    static int deltaBandWidth = (int) (rangeDelta / numberOfBands);
    //(int) Math.round(velocityBandWidth / factorVelocityToDelta);
    static int band = 0;
    
    
    public static double deltaToVel(int delta) {
        double vel = (delta - threshold) * factorVelocityToDelta;
        System.out.println(delta + " : " + vel);
        return vel;
    }

    public static void main(String[] args) {
        System.out.println("baseVelocity           " + baseVelocity);
        System.out.println("maxVelocity            " + maxVelocity);
        System.out.println("velocityNow            " + velocityNow);
        System.out.println("velocityBandWidth      " + velocityBandWidth);
        System.out.println("threshold              " + threshold);
        System.out.println("maxDelta               " + maxDelta);
        System.out.println("rangeDelta             " + rangeDelta);
        System.out.println("rangeVelocity;         " + rangeVelocity);
        System.out.println("numberOfBands          " + numberOfBands);
        System.out.println("factorVelocityToDelta  " + factorVelocityToDelta);
        System.out.println("deltaBandWidth          " + deltaBandWidth);
        deltaToVel(210);
        deltaToVel(1298);
        deltaToVel(3400);

//        if (!moving) {
//            if (Math.abs(axisValue) > threshold) {
//                // setVelocity(baseVelocity);
//                if (axisValue > 0) {
//                    // moveTo(axisPositiveMax);
//                } else {
//                    // moveTo(axisPositiveMax);
//                }
//
//            }
//        } else { // moving
//            if (Math.abs(axisValue) <= threshold) {
//                // stop();
//                // restore default velocity
//                return;
//            }
//            if (Math.abs(axisValue) > threshold) {
//                int delta = (Math.abs(axisValue) - threshold);
//                double x = (double) delta / factorDeltaToVelocity;
//
//                if (axisValue > 0) {
//                    // moveTo(axisPositiveMax);
//                } else {
//                    // moveTo(axisPositiveMax);
//                }
//
//            }
//        }
    }

}
