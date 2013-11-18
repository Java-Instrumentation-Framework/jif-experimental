package tests.gui.icewalker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;
//import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.border.*;


public class BackgroundBorder implements Border {

	private BufferedImage image;
	public static int BOTTOM_LEFT = 0, CENTER = 1, TOP_RIGHT = 2, LEFT = 3, RIGHT = 4, TOP_LEFT = 5,
			REPEAT = 6, REPEAT_X = 7, REPEAT_Y = 8;
			
	public int LOCATION = -1;
	private boolean tileImage = false;
	private int repeat = REPEAT;

	public BackgroundBorder() {

	}

	public BackgroundBorder(int imageLocation) {
		setImageLocation(imageLocation);
	}

	public BackgroundBorder(BufferedImage image) {
		this.image = image;
	}

	public BackgroundBorder(File iconfile) {
		setImageFile(iconfile);
		setImageLocation(CENTER);
	}

	public BackgroundBorder(String fileLocation) {
		this( new File(fileLocation) );
	}

	public BackgroundBorder(String fileLocation, int imageLocation) {
		this( new File(fileLocation), imageLocation );
	}

	public BackgroundBorder(File iconfile, int imageLocation) {
		this(iconfile);
		setImageLocation(imageLocation);
	}

	public void setImageFile(File iconFile) {
		if(iconFile == null) return;

		try {
			this.image = ImageIO.read(iconFile);
		} catch(Exception ioe) {
			System.out.println("Background Border error: \n" + ioe + "\n");
		}
	}

	public void setImageLocation(int location) {
		this.LOCATION = location;
	}

	public int getImageLocation() {
		return LOCATION;
	}

	public void clearImage() {
		image = null;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int imgLoc = getImageLocation();

		if(image == null) {
			return;
		}

		if(imgLoc == CENTER) {
			x += (width - image.getWidth(c))/2;
			y += (height - image.getHeight(c))/2;
		} else if(imgLoc == BOTTOM_LEFT) {
			y += height - image.getHeight(c);
		} else if(imgLoc == TOP_RIGHT) {
			x += width - image.getWidth(c);
		} else if(imgLoc == LEFT) {
			y += (height-image.getHeight(c))/2;
		} else if(imgLoc == RIGHT) {
			x += width - image.getWidth(c);
			y += (height-image.getHeight(c))/2;
		} else if(imgLoc == TOP_LEFT) {
			x = 0;
			y = 0;
		}

		if( getTileImage() ) {
			if(getBackgroundRepeat() == REPEAT) {
				for(x = 0, y = 0; y < height;  ) {
					((Graphics2D) g).drawRenderedImage(image, AffineTransform.getTranslateInstance(x,y));
	
					x += image.getWidth(c);
	
					if(x > width) {
						x = 0;
						y += image.getHeight(c);
					}
				}	
			} else if(getBackgroundRepeat() == REPEAT_Y) {
				for(x = 0, y = 0; y < height;  ) {
					((Graphics2D) g).drawRenderedImage(image, AffineTransform.getTranslateInstance(x,y));
	
					y += image.getHeight(c);
	
					//if(y > height) {
					//	x = 0;
					//	y += image.getHeight(c);
					//}
				}
			} else if( getBackgroundRepeat() == REPEAT_X ) {
				for(x = 0, y = 0; x < width;  ) {
					((Graphics2D) g).drawRenderedImage(image, AffineTransform.getTranslateInstance(x,y));
	
					x += image.getHeight(c);
				}
			}
			
		} else {
			((Graphics2D) g).drawRenderedImage(image, AffineTransform.getTranslateInstance(x,y));
		}



	}

	public boolean getTileImage() {
		return tileImage;
	}

	public void setTileImage(boolean b) {
		tileImage = b;
		setImageLocation(TOP_LEFT);
	}
	
	public void setBackgroundRepeat(int repeat) {
		this.repeat = repeat;
		setTileImage(true);
	}
	
	public int getBackgroundRepeat() {
		return repeat;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(0,0,0,0);
	}

	public boolean isBorderOpaque() {
		return true;
	}
}
