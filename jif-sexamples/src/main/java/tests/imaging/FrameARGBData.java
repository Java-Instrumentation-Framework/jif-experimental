
package tests.imaging;

import java.awt.Dimension;

interface FrameARGBData {
	Dimension size();
	int [] getPixels();
	void nextFrame();
}

