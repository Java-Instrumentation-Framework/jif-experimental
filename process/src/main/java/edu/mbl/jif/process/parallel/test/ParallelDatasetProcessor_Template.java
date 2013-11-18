package edu.mbl.jif.process.parallel.test;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.dataset.metadata.DimensionalExtents;
import edu.mbl.jif.imaging.dataset.metadata.SumMetadata;
import edu.mbl.jif.process.ChannelSetProcessor;
import edu.mbl.jif.process.DatasetIterator;
import edu.mbl.jif.process.parallel.ResultProcessor;
import edu.mbl.jif.process.parallel.OrderedParallelProcessManager;
import edu.mbl.jif.process.ps.PsCalcProcess.MagOrtParallelChannelSetProcessor;
import edu.mbl.jif.process.ps.PsCalcProcess.ResultSaverMagOrt;
import org.json.JSONObject;

/**
 *
 * @author GBH
 */
public class ParallelDatasetProcessor_Template {

   public void runTest(String[] args) {
      try {
         String dir = "C:/MicroManagerData/TestDatasetGen";
         String prefixIn = "pssynth";
         String prefixOut = "magort";
         //
         MMgrDatasetAccessor mda = new MMgrDatasetAccessor(dir, prefixIn, false, false);
         JSONObject sumMD_In = mda.getImageCache().getSummaryMetadata();
         //
         // Create new metadata based on the input dataset...
         JSONObject sumMDOut = SumMetadata.newCopyOfSummaryMetadata(sumMD_In, dir, prefixOut, true, "Extraction");
         
         // TODO: Add displayAndComments... but need to change for only 2 channels
         JSONObject dispMD = mda.getImageCache().getDisplayAndComments();
         //
         // Change /add metadata...
         String[] channelNames = new String[]{"Mag", "Ort"};  // define channels
         
         MMgrDatasetGenerator mdg = new MMgrDatasetGenerator(sumMDOut);
         DimensionalExtents dsdIn = SumMetadata.getDimensionalExtents(sumMD_In);
         
         //=============================
         // Specific ResultProcessor set here:
         ResultProcessor saver = new ResultSaverMagOrt(mda, mdg); // what to do with in-order result.
         OrderedParallelProcessManager oppm = new OrderedParallelProcessManager(saver);
         //======================================
         // Specific ChannelSetProcessor set here:
         ChannelSetProcessor setProcessor = new MagOrtParallelChannelSetProcessor(mda, oppm);
         setProcessor.addParameter("Swing", 0.03);
         setProcessor.addParameter("BkgdCorrected", true);

         // create Dataset walking processor...
         DatasetIterator mdp = new DatasetIterator(SumMetadata.getDimensionalExtents(sumMD_In));
         mdp.setChannelSetProcessor(setProcessor);
         // alternately, an eachChannelProcessor could be set.
         // mdp.setEachChannelProcessor(...);
         // run the process on all the dimensions.
         mdp.iterateAll();
         // When complete (all tasks submitted)
         oppm.submissionsComplete();
      } catch (Exception ex) {
      }
   }
}
