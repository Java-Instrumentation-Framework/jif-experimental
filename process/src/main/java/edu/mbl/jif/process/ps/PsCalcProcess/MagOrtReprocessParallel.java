package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.dataset.linked.Antecedent;
import edu.mbl.jif.imaging.dataset.linked.DerivedFrom;
import edu.mbl.jif.imaging.dataset.linked.PathUtils;
import edu.mbl.jif.imaging.dataset.linked.Transform;
import edu.mbl.jif.imaging.dataset.metadata.SumMetadata;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.ChannelSetProcessor;
import edu.mbl.jif.process.DatasetIterator;
import edu.mbl.jif.process.parallel.OrderedParallelProcessManager;
import edu.mbl.jif.process.parallel.ResultProcessor;
import edu.mbl.jif.process.parallel.listen.CompletionListener;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author GBH
 */
public class MagOrtReprocessParallel {

   DatasetIterator dsIter;
   OrderedParallelProcessManager oppMgr;

   public void runTest(CompletionListener listener) {

      String dir = "C:/MicroManagerData/TestDatasetGen";
      String prefixIn = "pssynth_100";
      String prefixOut = "magort";
      String bkgdFile = "pssynthBG";
      run(dir, prefixIn, prefixOut, bkgdFile, listener);

   }

   public void run(String dir, String prefixIn, String prefixOut, String bkgdFile) {
      run(dir, prefixIn, prefixOut, bkgdFile, null);
   }

   public void run(String dir, String prefixIn, String prefixOut, String bkgdFile, CompletionListener listener) {

      try {
         MMgrDatasetAccessor dsBkgd = new MMgrDatasetAccessor(dir, bkgdFile, false, false);
         //
         MMgrDatasetAccessor dsIn = new MMgrDatasetAccessor(dir, prefixIn, false, false);
         JSONObject sumMD_In = dsIn.getImageCache().getSummaryMetadata();
         // 
         JSONObject sumMDOut = SumMetadata.newCopyOfSummaryMetadata(sumMD_In, dir, prefixOut, true,
                 "MagOrtReprocParallel");
         MMgrDatasetGenerator dsOut = new MMgrDatasetGenerator(sumMDOut);
         //==============================================================
         // Set the specific ResultProcessor, such as saving to file.
         boolean derivedFromByFrame = false;  // if false, use Summary-level DerivedFrom
         // what to do with in-order result.
         ResultProcessor saver = new ResultSaverMagOrt(dsIn, dsOut, derivedFromByFrame);
         // =============================================================
         oppMgr = new OrderedParallelProcessManager(saver);
         if (listener != null) {
            oppMgr.addCompletionListener(listener);
         }
         //==============================================================
         // Specific ChannelSetProcessor set here:
         ChannelSetProcessor channelSetProcessor = new MagOrtParallelChannelSetProcessor(dsIn,
                 oppMgr);
         // Set parameters that will not change
         channelSetProcessor.addParameter("wavelength", 546.0f);
         channelSetProcessor.addParameter("swingFraction", 0.3f);
         channelSetProcessor.addParameter("retCeiling", 100.0f);
         channelSetProcessor.addParameter("azimuthRef", 90f);
         channelSetProcessor.addParameter("zeroIntensity", 0f);
         channelSetProcessor.addParameter("doBkgdCorrect", true);
         channelSetProcessor.addParameter("algorithm", 5);
         //
         // pass the summary metadata as data
         channelSetProcessor.addData("summaryMetadata", dsOut.getImageCache().getSummaryMetadata());
         //==============================================================
         // Load the Background Polstack
         int bkdgChannels = 5;
         List<TaggedImage> bkgdImgs = new ArrayList<TaggedImage>();
         for (int i = 0; i < bkdgChannels; i++) {
            TaggedImage img = dsBkgd.getImageCache().getImage(i, 0, 0, 0);
            if (img == null) {
               System.out.println("**** " + "img==null");
            }
            bkgdImgs.add(img);
         }

         // Get a relative path for the antecedent images
         String antecentPath = dsBkgd.getImageCache().getDiskLocation();
         // TODO +++ this isn't really the result path....
         String resultPath = dsOut.getImageCache().getDiskLocation();
         String relPath = PathUtils.getRelativePath(antecentPath, resultPath);
         //
         Antecedent[] antsBG;
         if (derivedFromByFrame) {
            antsBG = Antecedent.createAntecedentsFor(relPath, bkgdImgs);
         } else {
            antsBG = Antecedent.createAntecedentsForSummary(
                    dsBkgd, dsOut, bkgdImgs, Antecedent.DIMENSION_CHANNEL);
         }
         // Add parameter to carry Background images as Antecedents
         channelSetProcessor.addParameter("BackgroundImages", antsBG);
         //
         //====================================================================
         // create Background Polstack and pre-calc intermediate arrays.
         PolStack bkgdPS = new PolStack(bkgdImgs);
         PolCalculator calc = new PolCalculator();
         calc.calcBkgdArrays(bkgdPS);
         // pass BkgdArrays as data
         channelSetProcessor.addData("bkgdPolStack", bkgdPS);
         //===================================================================
         if (derivedFromByFrame) {
            // Frame-level DerivedFrom will be added by ResultProcessor
         } else {
            // Summary-Level 
            // when summary-level, the indices array starts with the dimension flag,
            // followed by the indices of that dimension, eg. the channels of the images... 
            int[] indices = new int[]{Antecedent.DIMENSION_CHANNEL, 0, 1, 2, 3, 4};
            Antecedent[] antecedents = Antecedent.createAntecedentsForSummary(
                    dsIn, dsOut, indices);
            Transform xform = new Transform("edu.mbl.jif.ps.PsCalcProcess.ProcessMagOrt",
                    channelSetProcessor.parameters);
            DerivedFrom dfSrc = new DerivedFrom(antecedents, xform);
            // add to dsOut SummayMetadata
            dfSrc.addToMetadata(dsOut.getImageCache().getSummaryMetadata());
         }
         //===================================================================
         dsIter = new DatasetIterator(
                 SumMetadata.getDimensionalExtents(
                         dsIn.getImageCache().getSummaryMetadata()));
         dsIter.setChannelSetProcessor(channelSetProcessor);
         // ======================================
         // alternately, an eachChannelProcessor could be set.
         // dsIter.setEachChannelProcessor(...);
         // or feed this from the Acquistion...ImageCacheListener
         // accummulate the images [5]
         // create an OrderedResult
         // submitter.submit(...);

         // run the process on all the dimensions.
         dsIter.iterateAll();
         // Complete when all tasks submitted
         oppMgr.submissionsComplete();
         // If cancelled...
         if (dsIter.isCancelled()) {
            dsOut.cancel();
         }
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   public void cancel() {
      oppMgr.cancelAll();
      dsIter.setCancelled(true);
   }
   /*
    *  SummaryMetadate thus generated...
    * 
    {"Slices":10,
    "UUID":"77808bb2-2493-4f32-971e-7c7a8bd17033",
    "Binning":1,"UserName":"GBH","Depth":1,"PixelType":"GRAY8",

    "DerivedFrom":{
    "Transform":
    {"transformName":"edu.mbl.jif.ps.PsCalcProcess.ProcessMagOrt",
    "parameters":{"wavelength":546,"azimuthRef":90,
    "BackgroundImages":[{"indices":[0,0,0,0],"uuid":"0fe6f635-f083-4baf-bd37-b56a716fe518","uri":"file:/pssynthBG/pssynthBG_MMImages.ome.tif"},{"indices":[1,0,0,0],"uuid":"cc738f59-1060-44e3-90e3-1ba385f5b666","uri":"file:/pssynthBG/pssynthBG_MMImages.ome.tif"},{"indices":[2,0,0,0],"uuid":"f354b8e8-7a0c-4171-89f7-59fa0a160430","uri":"file:/pssynthBG/pssynthBG_MMImages.ome.tif"},{"indices":[3,0,0,0],"uuid":"37d1d677-7d5a-4ab3-b869-ea3676eb9a45","uri":"file:/pssynthBG/pssynthBG_MMImages.ome.tif"},{"indices":[4,0,0,0],"uuid":"514cbf55-0d7c-4064-89d9-f561eee9be83","uri":"file:/pssynthBG/pssynthBG_MMImages.ome.tif"}],
    "retCeiling":100,"doBkgdCorrect":true,"zeroIntensity":0,"swingFraction":0.3,"algorithm":5}
    },
    "Antecedent":[{"indices":[3,0,1,2,3,4],
    "uuid":"c2a740db-3d20-4315-9e1e-13624af2622b",
    "uri":"pssynth_100/pssynth_100_MMImages.ome.tif"}]},
		
    "Time":"Tue Mar 05 14:13:31 EST 2013","Date":"2013-03-05","MetadataVersion":10,"SlicesFirst":true,"Width":201,"ChContrastMin":[0,0],"PixelAspect":1,"MicroManagerVersion":"1.4","ChNames":["Mag","Ort"],"IJType":0,"Height":181,"Frames":10,"Prefix":"magort","PixelSize_um":0,"BitDepth":8,"Source":"undefined","ComputerName":"GBH-VAIO","Channels":2,"TimeFirst":false,"ChColors":[-1,-1],"ChContrastMax":[255,255],"Positions":1,"Directory":"C:/MicroManagerData/TestDatasetGen"
    }
    */
}
