package application;

import javax.swing.UIManager;
import java.awt.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.SwingUtilities;
import edu.mbl.jif.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AppTest {
    boolean packFrame = false;
    
    //Construct the application
    public AppTest() {
        AppTestFrame frame = new AppTestFrame();
        //Validate frames that have preset sizes
        //Pack frames that have useful preferred size info, e.g. from their layout
        if (packFrame) {
            frame.pack();
        } else {
            frame.validate();
        }
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }
    
    //Main method
    public static void main(String[] args) {
        //new javax.swing.plaf.metal.MetalLookAndFeel();
        //    FileInputStream in = null;
        //    try {
        //      in = new FileInputStream("gray.theme");
        //    } catch (IOException e) {
        //      System.out.println(e);
        //    }
        //    System.out.println("ThemeIn="+in);
        //    lookFeel.setCurrentTheme(new CustomTheme(in));
//    MetalLookAndFeel lookFeel =
//      new com.incors.plaf.kunststoff.KunststoffLookAndFeel();
        //new com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel();
        //   try {
        // MetalTheme theme = new psj.Utils.WhiteSatinTheme();
        //MetalTheme theme = new psj.Utils.MoodyBlueTheme();
        //lookFeel.setCurrentTheme(theme);
        //    UIManager.setLookAndFeel(lookFeel);
        //SwingUtilities.updateComponentTreeUI();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
        
        //edu.mbl.jif.gui.lookfeel.LooksAndFeels.setSynthetica();
        new AppTest();
    }
}
