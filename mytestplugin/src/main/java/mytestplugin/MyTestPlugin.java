package mytestplugin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import mmcorej.CMMCore;
import static org.micromanager.MMStudioMainFrame.MM_DATA_SET;
import org.micromanager.api.MMPlugin;
import org.micromanager.api.ScriptInterface;
import org.micromanager.utils.FileDialogs;
import org.micromanager.utils.MMScriptException;
import org.micromanager.utils.ReportingUtils;

public class MyTestPlugin implements MMPlugin {
   // Micro-Manager Plugin template

   public static final String menuName = "ZZ_Test";
   public static final String tooltipDescription = "_Description of the plugin_";
   //-----------------------------------------------------------
   static ScriptInterface app;
   static CMMCore core;

   @Override
   public void setApp(ScriptInterface si) {
      app = si;
      core = app.getMMCore();
   }

   @Override
   public void show() {
      JFrame f = new JFrame();
      JButton button = new JButton("Go");
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            openAcquisitionData(false);
            //ImagePlus imp = ij.IJ.getImage();
            //draw(imp.getCanvas());
         }
      });
      f.getContentPane().add(BorderLayout.CENTER, button);
      f.setBounds(100, 100, 300, 100);
      f.setVisible(true);
   }

//   public void draw(ImageCanvas ic) {
//      Graphics g = ic.getGraphics();
//      Color color = Color.BLUE;
//      g.setColor(color);
//
//      double mag = ic.getMagnification();
//      int x = 20;
//      int y = 20;
//      for (int i = 0; i < 10; i++) {
//         int width = 20;
//         int height = 10;
//         x = x + 10 * i;
//         y = y + 10 * i;
//         int sw = (int) (width * mag);
//         int sh = (int) (height * mag);
//         int sx1 = ic.screenX((int) (x * mag));
//         int sy1 = ic.screenY((int) (y * mag));
//
//         Graphics2D g2d = (Graphics2D) g;
//         g.drawRect(sx1, sy1, sw, sh);
//      }
//
//   }
   //===========================================
   AcquisitionManager acqMgr_ = new AcquisitionManager();
   String openAcqDirectory_;

   public void openAcquisitionData(boolean inRAM) {

      // choose the directory
      // --------------------
      File f = FileDialogs.openDir(null, "Please select an image data set", MM_DATA_SET);
      if (f != null) {
         if (f.isDirectory()) {
            openAcqDirectory_ = f.getAbsolutePath();
         } else {
            openAcqDirectory_ = f.getParent();
         }
         String acq = null;
         try {
            acq = openAcquisitionData(openAcqDirectory_, inRAM);
         } catch (MMScriptException ex) {
            ReportingUtils.showError(ex);
         } finally {
            try {
               acqMgr_.closeAcquisition(acq);
            } catch (MMScriptException ex) {
               ReportingUtils.logError(ex);
            }
         }

      }
   }

   public String openAcquisitionData(String dir, boolean inRAM, boolean show)
           throws MMScriptException {
      String rootDir = new File(dir).getAbsolutePath();
      String name = new File(dir).getName();
      rootDir = rootDir.substring(0, rootDir.length() - (name.length() + 1));
      acqMgr_.openAcquisition(name, rootDir, show, !inRAM, true);
      try {
         acqMgr_.getAcquisition(name).initialize();
      } catch (MMScriptException mex) {
         acqMgr_.closeAcquisition(name);
         throw (mex);
      }
      return name;
   }

   /**
    * Opens an existing data set. Shows the acquisition in a window.
    *
    * @return The acquisition object.
    */
   public String openAcquisitionData(String dir, boolean inRam) throws MMScriptException {
      return openAcquisitionData(dir, inRam, true);
   }

   //===========================================================
   @Override
   public void configurationChanged() {
   }

   @Override
   public String getDescription() {
      return "";
   }

   @Override
   public String getInfo() {
      return "_Plug info_";
   }

   @Override
   public String getVersion() {
      return "1.0";
   }

   @Override
   public String getCopyright() {
      return "2012";
   }

   @Override
   public void dispose() {
   }
   public static void main(String[] args) {
      new MyTestPlugin().openAcquisitionData(false);
   }
}
