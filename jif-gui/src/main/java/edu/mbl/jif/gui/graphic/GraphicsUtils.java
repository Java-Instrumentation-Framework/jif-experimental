/*
 * GraphicsUtils.java
 */
package edu.mbl.jif.gui.graphic;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.VolatileImage;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author GBH
 */
public class GraphicsUtils {

	public GraphicsUtils() {
	}

	static {
		System.setProperty("sun.awt.nopixfmt", "true");
	}

	public static void printGraphicDeviceInfo() {
		Rectangle virtualBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		JFrame frame[][] = new JFrame[gs.length][];
		for (int j = 0; j < gs.length; j++) {
			GraphicsDevice gd = gs[j];
			System.out.println("Device " + j + ": " + gd);
			GraphicsConfiguration[] gc = gd.getConfigurations();
			frame[j] = new JFrame[gc.length];

			for (int i = 0; i < gc.length; i++) {
				System.out.println("  Configuration " + i + ": " + gc[i]);
				System.out.println("    Bounds: " + gc[i].getBounds());
				virtualBounds = virtualBounds.union(gc[i].getBounds());
				frame[j][i] = new JFrame("Config: " + i, gc[i]);
				frame[j][i].setBounds(50, 50, 400, 100);
				frame[j][i].setLocation(
						(int) gc[i].getBounds().getX() + 50,
						(int) gc[i].getBounds().getY() + 50);
				frame[j][i].getContentPane().add(new JTextArea("Config:\n" + gc[i]));
				frame[j][i].setVisible(true);
			}
			System.out.println("Overall bounds: " + virtualBounds);
		}
	}

	public static Rectangle getVirtualBounds() {
		Rectangle virtualBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) {
			GraphicsDevice gd = gs[j];
			System.out.println("Device " + j + ": " + gd);
			GraphicsConfiguration[] gc = gd.getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				virtualBounds = virtualBounds.union(gc[i].getBounds());
			}
		}
		return virtualBounds;
	}

	// --- Getting the Number of Screens
	public static int getNumberScreens() {
		int numScreens = 0;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			GraphicsDevice[] gs = ge.getScreenDevices();
			// Get number of screens
			numScreens = gs.length;
		} catch (HeadlessException e) {
			// Is thrown if there are no screen devices
		}
		return numScreens;
	}

// --- Getting Amount of Free Accelerated Image Memory
//      Images in accelerated memory are much faster to draw on the screen.
//      However, accelerated memory is typically limited and it is usually
//      necessary for an application to manage the images residing in this
//      space. This example demonstrates how to determine the amount free
//      accelerated available.
// Note: There appears to be a problem with
//       GraphicsDevice.getAvailableAcceleratedMemory() on some systems.
//       The method returns 0 even if accelerated image memory is available.
//       A workaround is to create a temporary volatile image on the graphics
//       device before calling the method. Once the volatile image is created,
//       the method appears to return the correct value on all subsequent calls.
	public static long getFreeAcceleratedImageMemory() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		int bytes = 0;
		try {
			GraphicsDevice[] gs = ge.getScreenDevices();
			// Get current amount of available memory in bytes for each screen
			for (int i = 0; i < gs.length; i++) {
				// Workaround; see description
				VolatileImage im = gs[i].getDefaultConfiguration().createCompatibleVolatileImage(1,
						1);
				// Retrieve available free accelerated image memory
				bytes = gs[i].getAvailableAcceleratedMemory();
				if (bytes < 0) {
					// Amount of memory is unlimited
				}
				im.flush(); // Release the temporary volatile image
			}
		} catch (HeadlessException e) {
			// Is thrown if there are no screen devices
		}
		return bytes;
	}

      // --- Enabling Full-Screen Mode
      // In full-screen mode, no window can overlap the full-screen window.
      // Also, when in full-screen mode, the display mode typically can be
      // changed if desired (see e605 Setting the Screen Size, Refresh Rate,
      // or Number of Colors).
	public static void enableFullScreenMode() {
		// Determine if full-screen mode is supported directly
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		if (gs.isFullScreenSupported()) {
			// Full-screen mode is supported
		} else {
			// Full-screen mode will be simulated
		}
		// Create a button that leaves full-screen mode
		Button btn = new Button("OK");
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// Return to normal windowed mode
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gs = ge.getDefaultScreenDevice();
				gs.setFullScreenWindow(null);
			}

		});
		// Create a window for full-screen mode; add a button to leave full-screen mode
		Frame frame = new Frame(gs.getDefaultConfiguration());
		Window win = new Window(frame);
		win.add(btn, BorderLayout.CENTER);
		try {
			// Enter full-screen mode
			gs.setFullScreenWindow(win);
			win.validate();
			// ...
		} finally {
			// Exit full-screen mode
			gs.setFullScreenWindow(null);
		}
	}

	public static void setupJvmProperties() {
		// Java2D Properties on Windows
		// The DirectDraw/GDI pipeline is the default pipeline for Windows. Change as follows:

		//System.setProperty("sun.java2d.d3d", "false");
		// -Dsun.java2d.d3d=false/true — Dis/enable the use of Direct3D pipeline. default=true

		// J2D_D3D=false/true — Dis/enable the use of Direct3D pipeline.
		// -Dsun.java2d.ddforcedram=true — Keep volatile images in VRAM; surface punting mechanism

		// -Dsun.java2d.ddblit=false — Disable DirectDraw blit operations; GDI blits used instead.

		//System.setProperty("sun.java2d.noddraw", "true");
		// -Dsun.java2d.noddraw=true/false — Dis/enable DirectDraw pipeline. (else use GDI)
		// To turn off the Java 2D system's use of DirectDraw and Direct3D completely.

		//System.setProperty("sun.java2d.ddoffscreen", "false");
		// To turn off the Java 2D system's use of DirectDraw and Direct3D 
		// for offscreen surfaces such as the Swing back buffer. default=true

		//-Dsun.java2d.ddscale=true
		// Setting this flag to true enables hardware-accelerated scaling; 
		// disabled by default to avoid rendering artifacts in existing applications;
		// has no effect if the OpenGL pipeline is in use.

		//?? System.setProperty("swing.bufferPerWindow", "false");

		// OpenGL, default=false
		// -Dsun.java2d.opengl=true

		//Toolkit.getDefaultToolkit().setDynamicLayout(true);
		System.out.println("awt.dynamicLayoutSupported = "
				+ Toolkit.getDefaultToolkit().getDesktopProperty("awt.dynamicLayoutSupported"));
	}

	public static void main(String[] args) {
		//    setupJvmProperties();
		printGraphicDeviceInfo();
		System.out.println("VirtualBounds: " + getVirtualBounds());
            enableFullScreenMode();
		
            

	}

}