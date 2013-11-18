package edu.mbl.jif.lightfield;


import java.awt.image.ImageConsumer;
import java.awt.image.ColorModel;
import java.util.Hashtable;
import java.awt.Rectangle;
import java.awt.image.ImageFilter;

/**
 * An ImageFilter class for extracting a sub-image from an image (like cropping).
 */
public class LightfieldPerspectiveFilter extends ImageFilter {
 LFModel lfModel;
    int width;
    int height;
    /**
     * Constructs a CropImageFilter that extracts the absolute rectangular
     * region of pixels from its source Image as specified by the x, y,
     * w, and h parameters.
     * @param x the x location of the top of the rectangle to be extracted
     * @param y the y location of the top of the rectangle to be extracted
     * @param w the width of the rectangle to be extracted
     * @param h the height of the rectangle to be extracted
     */
    public LightfieldPerspectiveFilter(LFModel lfModel) {
	this.lfModel = lfModel;
        // addPropertyChangeListener...
    }

    /**
     * Passes along  the properties from the source object after adding a
     * property indicating the cropped region.
     * This method invokes <code>super.setProperties</code>,
     * which might result in additional properties being added.
     * <p>
     * Note: This method is intended to be called by the 
     * <code>ImageProducer</code> of the <code>Image</code> whose pixels 
     * are being filtered. Developers using
     * this class to filter pixels from an image should avoid calling
     * this method directly since that operation could interfere
     * with the filtering operation.
     */
    public void setProperties(Hashtable<?,?> props) {
	Hashtable<Object,Object> p = (Hashtable<Object,Object>)props.clone();
	p.put("lfView", new Integer(lfModel.getPitchInt()));
	super.setProperties(p);
    }

    /**
     * Override the source image's dimensions and pass the dimensions
     * of the rectangular cropped region to the ImageConsumer.
     * <p>
     * Note: This method is intended to be called by the 
     * <code>ImageProducer</code> of the <code>Image</code> whose 
     * pixels are being filtered. Developers using
     * this class to filter pixels from an image should avoid calling
     * this method directly since that operation could interfere
     * with the filtering operation.
     * @see ImageConsumer
     */
    public void setDimensions(int w, int h) {
        width = w/lfModel.getPitchInt();
        height = h / lfModel.getPitchInt();
	consumer.setDimensions(width, height);
    }
   

    public void setPixels(int x, int y, int w, int h,
			  ColorModel model, byte pixels[], int off,
			  int scansize) {
                byte view[] = new byte[width * height];
        int xV = 0;
        int yV = 0;
        //dx = wS / nx;
        //dy = hS / ny;
        for (int yS = lfModel.getOffsetY() + lfModel.getDisplacementY(); yS < h; yS = yS + lfModel.getPitchInt()) {
            xV = 0;
            for (int xS = lfModel.getOffsetX() + lfModel.getDisplacementX(); xS < w; xS = xS + lfModel.getPitchInt()) {
                //this.getRaster().setPixel(xV, yV, source[xS + (yS)]);
                //int p = source[xS + (yS * wS)] & 0xff;
                
                int indexV = xV + (yV * width);
                int indexS = xS + (yS * w);
                if ((indexV < view.length) && (indexS < pixels.length)) {
                    view[indexV] = pixels[indexS];
                }
                xV++;
            }
            yV++;
        }
	consumer.setPixels(0, 0, width, height, 
			   model, view,
			   0, width);
    }
    
    /**
     * Determine if the delivered int pixels intersect the region to
     * be extracted and pass through only that subset of pixels that
     * appear in the output region.
     * <p>
     * Note: This method is intended to be called by the 
     * <code>ImageProducer</code> of the <code>Image</code> whose 
     * pixels are being filtered. Developers using
     * this class to filter pixels from an image should avoid calling
     * this method directly since that operation could interfere
     * with the filtering operation.
     */
    public void setPixels(int x, int y, int w, int h,
			  ColorModel model, int pixels[], int off,
			  int scansize) {
    }

   
}
