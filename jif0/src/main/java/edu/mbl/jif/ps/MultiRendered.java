/*

 */
package edu.mbl.jif.ps;

import edu.mbl.jif.gui.imaging.ImageDisplayPanel;
import edu.mbl.jif.imaging.api.SeriesOfImages;
import edu.mbl.jif.imaging.series.SeriesOfImagesMultipageTiff;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;

/**
 *
 * Renderer... maps from whatever data type to compatible image.
 *
 *
 * BufferedImage getCompatibleImage(); void update(); // is
 *
 * GrayScale byte, short12, short16, float, double
 *
 * RGB/A int[], byte with indexed with lookup table (LUT)
 *
 * setLUT(LUT)
 *
 * Birefringence (mag + orient)
 *
 * Dichroism (mag + orient)
 *
 * FluorPol (mag? + orient)
 *
 *
 *
 * @author GBH
 */
public class MultiRendered implements DimensionChangeEventListener {

	SeriesOfImages series;
	//
	// access for mag and orient images from file.
	int magSlice = 1;
	int orientSlice = 2;
	int stackSize = 7;
	BufferedImage magImage;
	BufferedImage orientImage;
	BufferedImage orientSineImage;
	BufferedImage orientImageScaled;
	// scaling =  3, 5, or 9;
	int c;
	int z;
	int t;
	int p;
	private Image outImage;
	private ImageDisplayPanel imageDisplayPanel;

	public void open(String file) {
		series = new SeriesOfImagesMultipageTiff(file);
	}

	public void setOutImage(Image outImage) {
		this.outImage = outImage;
	};
	
	
	@Override
	public void onDimensionChangeEvent(DimensionChangeEvent evt) {
		// event recieved from dimensional control(s)
		setCoords(evt.dimIndices);
		loadImages();
		renderImage();
		updateImageDisplay();
	}

	public void setCoords(int[] dimIndices) {
		c = dimIndices[0];
		z = dimIndices[1];
		t = dimIndices[2];
		p = dimIndices[3];
	}

	
	public void loadImages() {
		this.loadMagAndOrient(z, t, p);
		// blocks while loading required images...

	}

	public void renderImage() {
		outImage = null;
	}
	
	public void updateImageDisplay() {
		imageDisplayPanel.showImage((BufferedImage) outImage);
	}

	public void loadMagAndOrient(int z, int t, int pos) {
		// deal with pos
		magImage = series.getImage(0, z, t);
		orientImage = series.getImage(1, z, t);
		int imageType = orientImage.getType();
		int depth = 8;
		if (imageType == BufferedImage.TYPE_BYTE_GRAY) {
			depth = 8;
		}
		if (imageType == BufferedImage.TYPE_USHORT_GRAY) {
			depth = 16;
		}
		PolStack ps = new PolStack(0, series.getImageDimensions().width,
				series.getImageDimensions().height, depth, "temp");
		if (depth == 8) {
			ps.imgMagnitude = ((DataBufferByte) magImage.getData().getDataBuffer()).getData();
			ps.imgAzimuth = ((DataBufferByte) orientImage.getData().getDataBuffer()).getData();
		} else if (depth == 16) {
			ps.imgMagnitude = ((DataBufferUShort) magImage.getData().getDataBuffer()).getData();
			ps.imgAzimuth = ((DataBufferUShort) orientImage.getData().getDataBuffer()).getData();
		} else {
			System.err.println("Bad image data type");
		}

	}

	public BufferedImage createOrientColorMappedImage(PolStack ps) {

		return null; // new BufferedImage()
	}

	public BufferedImage createOrientLinesOverlayImage() {
		return null; // new BufferedImage()
	}

	// makeThumbnail from a Buffered Image
	public static BufferedImage makeScaled(BufferedImage _image, float scale) {
		Interpolation interp = null;
		RenderingHints renderHints = null;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(_image);
		pb.add(scale);
		pb.add(scale);
		pb.add(0.0F);
		pb.add(0.0F);
		if (interp == null) {
			interp = Interpolation.getInstance(Interpolation.INTERP_BICUBIC);
		}
		pb.add(interp);
		return JAI.create("scale", pb, renderHints).getAsBufferedImage();
	}

	

}
