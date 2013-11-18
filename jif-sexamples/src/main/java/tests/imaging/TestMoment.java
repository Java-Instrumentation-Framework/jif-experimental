package tests.imaging;

import javax.media.jai.PlanarImage;
import javax.media.jai.JAI;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.Histogram;
import javax.media.jai.RenderedOp;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TestMoment {
  public TestMoment() {
  }


  public static double[] getMoment(PlanarImage image) {
    // set up the histogram
    int[] bins = {
        256};
    double[] low = {
        0.0D};
    double[] high = {
        256.0D};
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(null);
    pb.add(1);
    pb.add(1);
    pb.add(bins);
    pb.add(low);
    pb.add(high);
    RenderedOp op = JAI.create("histogram", pb, null);
    Histogram histogram = (Histogram) op.getProperty("histogram");
    // Moment, Absolute, Central
    double[] moment = (double[]) (histogram.getMoment(2, true, true));
    for (int i = 0; i < moment.length; i++) {
      System.out.println("moment " + i + " = " + moment[i]);
    }
    return moment;
  }

}
