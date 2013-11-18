package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.gui.imaging.FrameImageDisplayTabbed;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.linked.Antecedent;
import edu.mbl.jif.imaging.dataset.linked.AntecedentRetriever;
import edu.mbl.jif.imaging.dataset.linked.DerivedFrom;
import edu.mbl.jif.imaging.dataset.linked.Transform;
import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Testing of DerivedFrom...
 *
 * @author GBH
 */
public class TestDerivedFromRead {

   public static void dump(DerivedFrom df) {
      Transform xform = df.getTransform();
      Antecedent[] ants = df.getAntecedents();
      for (Antecedent ant : ants) {
         System.out.println(ant.getUri());
         System.out.println(ant.getUuid());
         for (int i = 0; i < ant.getIndices().length; i++) {
            System.out.print(ant.getIndices()[i] + "  ");
         }
         System.out.println("");
      }
   }

   public static void main(String[] args) {

      String dir = "C:/MicroManagerData/TestDatasetGen";
      String prefixIn = "magort_27";
      MMgrDatasetAccessor mda = null;
      try {
         mda = new MMgrDatasetAccessor(dir, prefixIn, false, false);
      } catch (Exception ex) {
         Logger.getLogger(TestDerivedFromRead.class.getName()).log(Level.SEVERE, null, ex);
      }
      String bkgdFile = "pssynthBG";
      try {
         MMgrDatasetAccessor bkgdDS = new MMgrDatasetAccessor(dir, bkgdFile, false, false);
      } catch (Exception ex) {
         Logger.getLogger(TestDerivedFromRead.class.getName()).log(Level.SEVERE, null, ex);
      }
      //===================================================
      // Get DerivedFromn from Summary metadata
      JSONObject sumMDj = mda.getImageCache().getSummaryMetadata();
      try {
         System.out.println(sumMDj.toString(2));
      } catch (JSONException ex) {
         ex.printStackTrace();
      }
      try {
         DerivedFrom dfOut = DerivedFrom.getDerivedFrom(sumMDj);
         String transformNameOut = dfOut.getTransform().getTransformName();
         Map<String, Object> parametersOut = dfOut.getTransform().getParameters();
         Antecedent[] antsOut = dfOut.getAntecedents();
         //
         AntecedentRetriever ar = new AntecedentRetriever();
         String relPath = dir;
         boolean summaryLevel = true;
         TaggedImage imgIn = mda.getImageCache().getImage(0, 0, 0, 0);
         int c = MDUtils.getChannelIndex(imgIn.tags);
         int z = MDUtils.getSliceIndex(imgIn.tags);
         int t = MDUtils.getFrameIndex(imgIn.tags);
         int p = MDUtils.getPositionIndex(imgIn.tags);
         int[] dimIndices = new int[]{c, z, t, p};
         final TaggedImage[] imgs = ar.getAntecedentImagesRelative(antsOut, relPath, dimIndices);
         try {
            int w = MDUtils.getWidth(imgs[0].tags);
            int h = MDUtils.getHeight(imgs[0].tags);
            int d = MDUtils.getBitDepth(imgs[0].tags);
            final ArrayList<BufferedImage> buffImgs = new ArrayList<BufferedImage>();
            //BufferedImage[] buffImgs = new BufferedImage[imgs.length];
            for (int i = 0; i < imgs.length; i++) {
               buffImgs.add(ImageFactoryGrayScale.createImage(w, h, d, (byte[]) imgs[i].pix));
            }
            // Display them...
            StaticSwingUtils.dispatchToEDT(new Runnable() {
               public void run() {
                  FrameImageDisplayTabbed fid = new FrameImageDisplayTabbed(buffImgs);
                  fid.setVisible(true);
               }
            });
         } catch (JSONException ex) {
            Logger.getLogger(TestDerivedFromRead.class.getName()).log(Level.SEVERE, null, ex);
         }

         //SeriesOfImagesChannelSet series = new SeriesOfImagesChannelSet(buffImgs);
         //ImageArrayDisplayPanel.openInFrame(series);
         // 
         System.out.println("");

         // TODO
         // Test Parameter Anticedents

         // ImageRetriever ir = new ImageRetriever();
         // TaggedImage[] imgs2 = ir.getAntecedentImages(df);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
      if (true) {
         return;
      }
      //===================================================
      // Get DerivedFromn from Frame metadata
      TaggedImage img = mda.getImageCache().getImage(0, 0, 0, 0);
      JSONObject frameMDj = img.tags;
      try {
         System.out.println(frameMDj.toString(2));
      } catch (JSONException ex) {
         ex.printStackTrace();
      }
      try {
         DerivedFrom dfOut = DerivedFrom.getDerivedFrom(frameMDj);
         String transformNameOut = dfOut.getTransform().getTransformName();
         Map<String, Object> parametersOut = dfOut.getTransform().getParameters();
         Antecedent[] antsOut = dfOut.getAntecedents();
         AntecedentRetriever ar = new AntecedentRetriever();
         String relPath = dir;
         boolean summaryLevel = true;
         TaggedImage[] imgs = ar.getAntecedentImages(antsOut, relPath, summaryLevel);
         // 
         System.out.println("");
         // ImageRetriever ir = new ImageRetriever();
         // TaggedImage[] imgs2 = ir.getAntecedentImages(df);
      } catch (JSONException ex) {
         ex.printStackTrace();
      }
   }
}
