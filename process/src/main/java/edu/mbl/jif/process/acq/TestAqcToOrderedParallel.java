/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.process.acq;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.mmtiff.MMImageCache;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;

import edu.mbl.jif.process.parallel.ResultProcessor;
import edu.mbl.jif.process.ps.PsCalcProcess.ResultSaverMagOrt;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

/**
 *
 * @author GBH
 */
public class TestAqcToOrderedParallel {

   public void test() {


      String dir = "C:/MicroManagerData/TestDatasetGen";
      String prefixIn = "pssynth_100";
      String prefixOut = "magort";
      String bkgdFile = "pssynthBG";

      runTest(dir, prefixIn, prefixOut, bkgdFile);
   }

   public void runTest(String dir, String prefixIn, String prefixOut, String bkgdFile) {

      MMgrDatasetAccessor dsIn;
      try {
         dsIn = new MMgrDatasetAccessor(dir, prefixIn, false, false);
         dsIn.getImageCache();
      } catch (Exception ex) {
         Logger.getLogger(TestAqcToOrderedParallel.class.getName()).log(Level.SEVERE, null, ex);
      }

      MMImageCache imgCacheIn = null;
      MMgrDatasetGenerator mdg = null;
      // BkdgDataset
      //
      String procClassName = "edu.mbl.jif.imaging.dataset.magort.ProcessMagOrt";
      int numSamples = 3;
      Map inParameters = new HashMap();
      inParameters.put("Swing", 0.03);
      inParameters.put("BkgdCorrected", true);
      //
      ResultProcessor saver = new ResultSaverMagOrt(imgCacheIn, mdg);
      //
      AqcToOrderedParallel acqProc = new AqcToOrderedParallel(
              imgCacheIn,
              mdg,
              procClassName,
              numSamples,
              inParameters,
              saver);
      
      // imgCacheIn.addImageCacheListener(acqProc);

      byte[] pix = new byte[1024];
      TaggedImage img = new TaggedImage(pix, new JSONObject());
      acqProc.imageReceivedTest(img);
      acqProc.imageReceivedTest(img);
      acqProc.imageReceivedTest(img);
   }

   public static void main(String[] args) {
      new TestAqcToOrderedParallel().test();

   }
}
