/*
 * BicubicTest.java
 * compares bilinear & biculic interpolation
 */

package tests.imaging;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class BicubicTest {
    public static void main(String args[]) throws IOException {
        if (args.length == 0) {
            System.err.println(
                    "Provide image name on command line");
            System.exit(-1);
        }
        Image image = ImageIO.read(new File(args[0]));
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage bilinear = new BufferedImage(2*w, 2*h,
                BufferedImage.TYPE_INT_RGB);
        BufferedImage bicubic = new BufferedImage(2*w, 2*h,
                BufferedImage.TYPE_INT_RGB);
        
        Graphics2D bg = bilinear.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        bg.scale(2, 2);
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        
        bg = bicubic.createGraphics();
        bg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bg.scale(2, 2);
        bg.drawImage(image, 0, 0, null);
        bg.dispose();
        
        for(int i=0; i<2*w; i++)
            for(int j=0; j<2*h; j++)
                if (bilinear.getRGB(i, j) != bicubic.getRGB(i, j))
                    System.out.println("Interpolation algo differ");
    }
}