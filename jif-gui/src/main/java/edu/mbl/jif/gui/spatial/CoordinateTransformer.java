package edu.mbl.jif.gui.spatial;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class CoordinateTransformer {

//    double translationX = 1;
//    double translationY = 2;
//    double rotation = 1;
//    double scalingX = 2.0;
//    double scalingY = 2.0;
    AffineTransform transformer;

    public CoordinateTransformer() {
    }

    ;

    public CoordinateTransformer(double translationX,
                                 double translationY,
                                 double rotation,
                                 double scalingX,
                                 double scalingY) {
        transformer = new AffineTransform();
        transformer.translate(translationX, translationY);
        transformer.scale(scalingX, scalingY);
        transformer.rotate(Math.toRadians(rotation));
    }
    
    // fromCoord is the screen, toCoord is the stage
    
    public static CoordinateTransformer createCoordinateTranformer(double[][] fromCoord,
                                                                   double[][] toCoord) {
        double[][] matrix;
        CoordinateTransformer xform = new CoordinateTransformer();
        matrix = xform.getTransformationMatrix(fromCoord, toCoord);

        /*
        matrix[0][0],matrix[0][1],matrix[0][2]
        matrix[1][0],matrix[1][1],matrix[1][2]
        
        [ x']   [  m00  m01  m02  ] [ x ]   [ m00x + m01y + m02 ]
        [ y'] = [  m10  m11  m12  ] [ y ] = [ m10x + m11y + m12 ]
        [ 1 ]   [   0    0    1   ] [ 1 ]   [         1         ]
        
        AffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) */
        
        xform.transformer = new AffineTransform(
            matrix[0][1], matrix[0][2],
            matrix[1][1], matrix[1][2],
            matrix[0][0], matrix[1][0]);
        return xform;
    }
    
    
    public void showMatrix() {
        double[] matrix = new double[6];
        transformer.getMatrix(matrix);
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("xform Matrix:   " + matrix[i] );
            
        }
    }



    public Point2D toStage(Point2D point) {
        Point2D newPoint = null;
        try {
            //other.rotation
            newPoint = transformer.transform(point, null);
        } catch (Exception ex) {
            Logger.getLogger(CoordinateTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newPoint;
    }
    
    public Point2D toScreen(Point2D point) {
        Point2D newPoint = null;
        try {
            //other.rotation
            newPoint = transformer.inverseTransform(point, null);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(CoordinateTransformer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return newPoint;
    }

    /*------------------------------------------------------------------*/
    // getTransformationMatrix using two point from each coordinate system
    // This algorithm came from TurboReg....
    private double[][] getTransformationMatrix(
        final double[][] fromCoord,
        final double[][] toCoord) {
        double[][] matrix = null;
        double[][] a = null;
        double[] v = null;
        /*********************************************************************
        A pair of points determines the combination of a translation, of
        a rotation, and of an isotropic scaling. Angles are conserved. A
        scaled rotation is determined by 4 parameters.
         ********************************************************************/
        //public static final int SCALED_ROTATION = 4;
        matrix = new double[2][3];
        a = new double[3][3];
        v = new double[3];
        a[0][0] = 1.0;
        a[0][1] = fromCoord[0][0];
        a[0][2] = fromCoord[0][1];
        a[1][0] = 1.0;
        a[1][1] = fromCoord[1][0];
        a[1][2] = fromCoord[1][1];
        a[2][0] = 1.0;
        a[2][1] = fromCoord[0][1] - fromCoord[1][1] + fromCoord[1][0];
        a[2][2] = fromCoord[1][0] + fromCoord[1][1] - fromCoord[0][0];
        invertGauss(a);
        v[0] = toCoord[0][0];
        v[1] = toCoord[1][0];
        v[2] = toCoord[0][1] - toCoord[1][1] + toCoord[1][0];
        for (int i = 0; (i < 3); i++) {
            matrix[0][i] = 0.0;
            for (int j = 0; (j < 3); j++) {
                matrix[0][i] += a[i][j] * v[j];
            }
        }
        v[0] = toCoord[0][1];
        v[1] = toCoord[1][1];
        v[2] = toCoord[1][0] + toCoord[1][1] - toCoord[0][0];
        for (int i = 0; (i < 3); i++) {
            matrix[1][i] = 0.0;
            for (int j = 0; (j < 3); j++) {
                matrix[1][i] += a[i][j] * v[j];
            }
        }

        return (matrix);
    } /* end getTransformationMatrix */


    private void invertGauss(
        final double[][] matrix) {
        final int n = matrix.length;
        final double[][] inverse = new double[n][n];
        for (int i = 0; (i < n); i++) {
            double max = matrix[i][0];
            double absMax = Math.abs(max);
            for (int j = 0; (j < n); j++) {
                inverse[i][j] = 0.0;
                if (absMax < Math.abs(matrix[i][j])) {
                    max = matrix[i][j];
                    absMax = Math.abs(max);
                }
            }
            inverse[i][i] = 1.0 / max;
            for (int j = 0; (j < n); j++) {
                matrix[i][j] /= max;
            }
        }
        for (int j = 0; (j < n); j++) {
            double max = matrix[j][j];
            double absMax = Math.abs(max);
            int k = j;
            for (int i = j + 1; (i < n); i++) {
                if (absMax < Math.abs(matrix[i][j])) {
                    max = matrix[i][j];
                    absMax = Math.abs(max);
                    k = i;
                }
            }
            if (k != j) {
                final double[] partialLine = new double[n - j];
                final double[] fullLine = new double[n];
                System.arraycopy(matrix[j], j, partialLine, 0, n - j);
                System.arraycopy(matrix[k], j, matrix[j], j, n - j);
                System.arraycopy(partialLine, 0, matrix[k], j, n - j);
                System.arraycopy(inverse[j], 0, fullLine, 0, n);
                System.arraycopy(inverse[k], 0, inverse[j], 0, n);
                System.arraycopy(fullLine, 0, inverse[k], 0, n);
            }
            for (k = 0; (k <= j); k++) {
                inverse[j][k] /= max;
            }
            for (k = j + 1; (k < n); k++) {
                matrix[j][k] /= max;
                inverse[j][k] /= max;
            }
            for (int i = j + 1; (i < n); i++) {
                for (k = 0; (k <= j); k++) {
                    inverse[i][k] -= matrix[i][j] * inverse[j][k];
                }
                for (k = j + 1; (k < n); k++) {
                    matrix[i][k] -= matrix[i][j] * matrix[j][k];
                    inverse[i][k] -= matrix[i][j] * inverse[j][k];
                }
            }
        }
        for (int j = n - 1; (1 <= j); j--) {
            for (int i = j - 1; (0 <= i); i--) {
                for (int k = 0; (k <= j); k++) {
                    inverse[i][k] -= matrix[i][j] * inverse[j][k];
                }
                for (int k = j + 1; (k < n); k++) {
                    matrix[i][k] -= matrix[i][j] * matrix[j][k];
                    inverse[i][k] -= matrix[i][j] * inverse[j][k];
                }
            }
        }
        for (int i = 0; (i < n); i++) {
            System.arraycopy(inverse[i], 0, matrix[i], 0, n);
        }
    } /* end invertGauss */

    
    
    public static void main(String[] args) {
        testGetTransformationMatrix();
        // testUsingSpecifiedParameters();
    }
    
    public static void testGetTransformationMatrix() {
        double[][] fromCoord = new double[][]{{0, 0}, {1, 1}};
        double[][] toCoord = new double[][]{{0, 0}, {5, 4}};
        CoordinateTransformer transformer = CoordinateTransformer.createCoordinateTranformer(fromCoord, toCoord);

//        System.out.println(
//            " " + matrix[0][0] + ", " + matrix[0][1] + ", " + matrix[0][2] + "\n " + matrix[1][0] + ", " + matrix[1][1] + ", " + matrix[1][2]);
//        System.out.println("scaleX " + at.getScaleX());
//        System.out.println("scaleY " + at.getScaleY());
//        System.out.println("translateX " + at.getTranslateX());
//        System.out.println("translateY " + at.getTranslateY());
        
        Point2D ptSrc = new Point2D.Double(1, 1);
        Point2D ptDst = transformer.toStage(ptSrc);
        System.out.println("ptDst (1,1): " + ptDst);
         ptSrc = new Point2D.Double(0, 0);
         ptDst = transformer.toStage(ptSrc);
        System.out.println("ptDst (0,0): " + ptDst);
    }

    
    public static void testUsingSpecifiedParameters() {
        CoordinateTransformer xform = new CoordinateTransformer(
            1, 2, 45, .9, .9);
        Point2D p1 = xform.toStage(new Point2D.Double(1, 1));
        System.out.println(p1);
        Point2D p2 = xform.toScreen(p1);
        System.out.println(p2);
    }

}
