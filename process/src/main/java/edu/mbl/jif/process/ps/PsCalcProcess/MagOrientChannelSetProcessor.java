package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.dataset.linked.DerivedFrom;
import edu.mbl.jif.imaging.dataset.linked.PathUtils;
import edu.mbl.jif.imaging.dataset.linked.Transform;
import edu.mbl.jif.imaging.dataset.metadata.DimensionalExtents;
import edu.mbl.jif.process.DatasetIterator;
import edu.mbl.jif.imaging.dataset.metadata.SumMetadata;
import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.MMImageCache;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.ChannelSetProcessor;
import edu.mbl.jif.process.IProcess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Accesses a PolStack dataset (assuming channels mag, ort, 5 samples) and extracts the mag & ort
 * images to a new dataset with DerivedFrom metadata.
 *
 * * * * Non-Parallel Version * * *
 *
 * @author GBH
 */
public class MagOrientChannelSetProcessor extends ChannelSetProcessor {

   private final MMgrDatasetAccessor mda;
   private final MMgrDatasetGenerator mdg;
   MMImageCache in;
   int numSamples = 5;

   public MagOrientChannelSetProcessor(MMgrDatasetAccessor mda, MMgrDatasetGenerator mdg) {
      this.mda = mda;
      this.mdg = mdg;
      in = mda.getImageCache();
   }

   @Override
   public void process(int z, int t, int p) {
      // called for each z,t,p by DatasetIterator.iterateAll();
      System.out.println("MagOrientProcessor.process(" + z + "," + t + "," + p + ")");
      ArrayList<TaggedImage> sampleImgs = new ArrayList<TaggedImage>();
      for (int i = 0; i < numSamples; i++) {
         TaggedImage img = mda.getImageCache().getImage(i + 2, z, t, p);
         if (img == null) {
            System.out.println("**** " + "img==null at submit  i: " + i + "  z: " + z + "  t: " + t + "  p: " + p);
         }
         sampleImgs.add(img);
      }

      // Assuming channels : {mag,ort,0,1,2,3,4}
//		TaggedImage magImage = in.getImage(0, z, t, p);
//		TaggedImage ortImage = in.getImage(1, z, t, p);


      // Set Parameters...
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("Swing", 0.03);
      parameters.put("BkgdCorrected", true);
      parameters.put("summaryMetadata", mda.getImageCache().getSummaryMetadata());
      parameters.put("sampleImages", sampleImgs);
      //proc.setInputParameters(parameters);
      IProcess proc = new ProcessMagOrt(this.parameters, this.data);
      proc.process();
      processResult(proc);
   }

   public void processResult(IProcess proc) {
      DerivedFrom df = null; // createDerivedFrom(proc);
      TaggedImage magImg = (TaggedImage) proc.getOutputParameters().get("ImgMag");
      TaggedImage ortImg = (TaggedImage) proc.getOutputParameters().get("ImgOrt");
      this.saveToDataset(0, "Mag", magImg, df);
      this.saveToDataset(1, "Ort", ortImg, df);
   }

   void saveToDataset(int channel, String label, TaggedImage img, DerivedFrom df) {
      try {
         int z = MDUtils.getSliceIndex(img.tags);
         int t = MDUtils.getFrameIndex(img.tags);
         int p = MDUtils.getPositionIndex(img.tags);
         mdg.putImageToChannel(channel, label, z, t, p, img.pix, df);
      } catch (JSONException ex) {
         Logger.getLogger(ResultSaverMagOrt.class.getName()).log(Level.SEVERE, null, ex);
      }
   }

   private DerivedFrom createDerivedFrom(IProcess proc) {
      Transform xform = new Transform(
              proc.getName(),
              proc.getInputParameters());
      String antecentPath = mda.getImageCache().getDiskLocation();
      String resultPath = mdg.getImageCache().getDiskLocation();
      String relPath = PathUtils.getRelativePath(antecentPath, resultPath);
      DerivedFrom df = DerivedFrom.createDerivedFrom(relPath,
              (List<TaggedImage>) proc.getInputParameters().get("sampleImages"),
              xform);
      return df;
   }

   // Test Main =============================================================================
   public static void main(String[] args) {
      try {
         String dir = "C:/MicroManagerData/TestDatasetGen";
         String prefixIn = "pssynth_100";
         String prefixOut = "processed";
         //
         MMgrDatasetAccessor mda = new MMgrDatasetAccessor(dir, prefixIn, false, false);
         JSONObject sumMD_In = mda.getImageCache().getSummaryMetadata();
         // Create new metadata based on the input dataset...
         JSONObject sumMDOut = SumMetadata.newCopyOfSummaryMetadata(
                 sumMD_In, dir, prefixOut, true, "Extraction");
         JSONObject dispMD = mda.getImageCache().getDisplayAndComments();
         //
         // Change /add metadata...
         String[] channelNames = new String[]{"Mag", "Ort"};  // define channels

         MMgrDatasetGenerator mdg = new MMgrDatasetGenerator(sumMDOut);
         DimensionalExtents dsdIn = SumMetadata.getDimensionalExtents(sumMD_In);
         // create processor...
         ChannelSetProcessor magOrtProcessor = new MagOrientChannelSetProcessor(mda, mdg);
         magOrtProcessor.addParameter("Swing", 0.03);
         magOrtProcessor.addParameter("BkgdCorrected", true);
         //
         DatasetIterator dsIter = new DatasetIterator(SumMetadata.getDimensionalExtents(sumMD_In));
         dsIter.setChannelSetProcessor(magOrtProcessor);
         dsIter.iterateAll();  // run the process on all the dimensions
         if (dsIter.isCancelled()) {
            mdg.cancel();
         }
         //
         System.exit(0);
      } catch (Exception ex) {
      }
   }
}
