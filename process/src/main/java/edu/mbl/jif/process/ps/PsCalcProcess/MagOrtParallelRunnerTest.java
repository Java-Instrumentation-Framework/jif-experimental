package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.dataset.metadata.SumMetadata;
import edu.mbl.jif.process.ChannelSetProcessor;
import edu.mbl.jif.process.DatasetIterator;
import edu.mbl.jif.process.parallel.OrderedParallelProcessManager;
import edu.mbl.jif.process.parallel.OrderedResult;
import edu.mbl.jif.process.parallel.ResultProcessor;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/**
 *
 * @author GBH
 */
public class MagOrtParallelRunnerTest {

	public void runTest(String[] args) {
		try {
			String dir = "C:/MicroManagerData/TestDatasetGen";
			String prefixIn = "pssynth";
			String prefixOut = "magort";
			//
			MMgrDatasetAccessor mda = new MMgrDatasetAccessor(dir, prefixIn, false, false);
			JSONObject sumMD_In = mda.getImageCache().getSummaryMetadata();
			//
                  JSONObject sumMD_Out = SumMetadata.newCopyOfSummaryMetadata(sumMD_In, dir, prefixOut, true, "New MarOrt dataset");
                  SumMetadata.setChannelsAndDisplay(sumMD_Out, new String[]{"Mag", "Ort"});
			MMgrDatasetGenerator mdg = new MMgrDatasetGenerator(sumMD_Out);

			//int nrOfProcessors = Runtime.getRuntime().availableProcessors();
			int nrOfProcessors = 1;
			System.out.println("nrOfProcessors = " + nrOfProcessors);
			ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);
			CompletionService< OrderedResult> cservice = 
					new ExecutorCompletionService< OrderedResult>(eservice);
			//
			// Specific ResultProcessor set here:
			ResultProcessor saver = new ResultSaverMagOrt(mda, mdg); // what to do with in-order result.
			// ------------------
			OrderedParallelProcessManager oppm = new OrderedParallelProcessManager(saver);
			
			// create processor...
			DatasetIterator mdp = new DatasetIterator(SumMetadata.getDimensionalExtents(sumMD_In));
			//
			// Specific ChannelSetProcessor set here:
			ChannelSetProcessor setProcessor = new MagOrtParallelChannelSetProcessor(mda, oppm);
			// Add 'Global' parameters...
			setProcessor.addParameter("Swing", 0.03);
			setProcessor.addParameter("BkgdCorrected", true);
			
			mdp.setChannelSetProcessor(setProcessor);
			// run the process on all the dimensions.
			mdp.iterateAll();
			// When complete (all tasks submitted)
			oppm.submissionsComplete();
		} catch (Exception ex) {
		}
	}

}
