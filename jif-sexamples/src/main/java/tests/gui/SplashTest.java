/*
 * SplashTest.java
 *
 * Created on April 26, 2007, 12:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.gui;

//import edu.mbl.jif.camacq.CamAcqJ;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SplashTest {
    static Logger logger = Logger.getLogger(SplashTest.class.getSimpleName());
    final SplashScreen splash = SplashScreen.getSplashScreen();
    Rectangle splashBounds;
    Graphics2D splashGraphics;
    
    protected void initSplash() throws Exception {
        if (splash == null) {
            throw new Exception("no splash image specified eg. -splash:mysplash.png");
        }
        splashBounds = splash.getBounds();
        splashGraphics = (Graphics2D) splash.createGraphics();
        if (splashGraphics == null) throw new Exception("no SplashScreen Graphics2D");
        splashGraphics.setColor(Color.YELLOW);
        splashGraphics.drawRect(0, 0, splashBounds.width - 1, splashBounds.height - 1);
    }
    protected void updateSplash(String status, int progress) {
        if (splash == null) return;
        if (splashGraphics == null) return;
        drawSplash(splashGraphics, status, progress);
        splash.update();
    }
    
    protected void drawSplash(Graphics2D splashGraphics, String status, int progress) {
        int barWidth = splashBounds.width*50/100;
        splashGraphics.setComposite(AlphaComposite.Clear);
        splashGraphics.fillRect(1, 10, splashBounds.width - 2, 20);
        splashGraphics.setPaintMode();
        splashGraphics.setColor(Color.GRAY);
        splashGraphics.drawString(status, 10, 20);
        splashGraphics.setColor(Color.BLACK);
        splashGraphics.drawRect(10, 25, barWidth + 2, 10);
        splashGraphics.setColor(Color.YELLOW);
        int width = progress*barWidth/100;
        splashGraphics.fillRect(11, 26, width + 1, 9);
        splashGraphics.setColor(Color.WHITE);
        splashGraphics.fillRect(11 + width + 1, 26, barWidth - width, 9);
    }

    protected void initialiseApplication() {
        try {
            initSplash();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        final String[] stages = {"stage 1", "stage 2", "stage 3"};
        int stage = 0;
        for (int i = 0; i <= 100; i += 5) {
            String status = "Initialising " + stages[stage] + "...";
            if (splash != null) updateSplash(status, i);
            try {
                Thread.sleep(500);
                if (i == 30) stage = 1;
                else if (i == 60) stage = 2;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if (splash != null) splash.close();
        //runApplication();
        //new CamAcqJ().start();
    }
      
    public static void main(String args[]) {
        //new SplashTest().initialiseApplication();
    }

}