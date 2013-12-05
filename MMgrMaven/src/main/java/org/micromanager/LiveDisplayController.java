/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micromanager;

//import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import mmcorej.TaggedImage;
import org.json.JSONObject;
//import org.micromanager.acquisition.AcquisitionWrapperEngine;
//import org.micromanager.acquisition.DefaultTaggedImagePipeline;
//import org.micromanager.acquisition.LiveAcq;
//import org.micromanager.acquisition.MMAcquisition;
//import org.micromanager.acquisition.ProcessorStack;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.api.Autofocus;
//import org.micromanager.acquisition.TaggedImageQueue;
//import org.micromanager.api.AcquisitionEngine;
//import org.micromanager.api.DataProcessor;
import org.micromanager.api.IAcquisitionEngine2010;
import org.micromanager.api.PositionList;
//import org.micromanager.utils.ReportingUtils;

/**
 *
 * @author GBH
 */
public class LiveDisplayController {

	IAcquisitionEngine2010 acqEng = new AcqEng();

	protected String runAcquisition(SequenceSettings acquisitionSettings) {
		/*
		 * 
		try {
		 // ======
		 AcquisitionEngine wrapper = MMStudioMainFrame.getInstance().getAcquisitionEngine();
		 // Start up the acquisition engine
		 BlockingQueue<TaggedImage> taggedImageQueue = acqEng.run(null);
		 JSONObject summaryMetadata_ = acqEng.getSummaryMetadata();
		 final List<DataProcessor<TaggedImage>> imageProcessors = wrapper.getImageProcessors();
		 // Set up the DataProcessor<TaggedImage> sequence
		 BlockingQueue<TaggedImage> taggedImageQueue2 = ProcessorStack.run(taggedImageQueue, imageProcessors);

		 // Create the default display
		 acqName_ = gui.createAcquisition(summaryMetadata_, diskCached);
		 MMAcquisition acq = gui.getAcquisition(acqName_);
		 display_ = acq.getAcquisitionWindow();
		 imageCache_ = acq.getImageCache();

		 // Start pumping images into the ImageCache
		 LiveAcq liveAcq = new LiveAcq(taggedImageQueue2, imageCache_);
		 liveAcq.start();
		 // =======
				
		 summaryMetadata_ = taggedImagePipeline.summaryMetadata_;
		 imageCache_ = taggedImagePipeline.imageCache_;
		 return taggedImagePipeline.acqName_;
		 } catch (Throwable ex) {
		 ReportingUtils.showError(ex);
		 return null;
		 }
		 */
		return "none";
	}

	/*
	 * Shows images as they appear in the default display window. Uses
	 * the default processor stack to process images as they arrive on
	 * the rawImageQueue.
	 */
	public void runDisplayThread(BlockingQueue rawImageQueue, final MMStudioMainFrame.DisplayImageRoutine displayImageRoutine) {
		/*
		 * final BlockingQueue processedImageQueue = ProcessorStack.run(rawImageQueue,
		 getAcquisitionEngine().getTaggedImageProcessors());
		 // will these have been initialized if an acquisition has not been done before this?
		 new Thread("Snap thread") {
		 public void run() {
		 while (true) {
		 try {
		 final TaggedImage image = (TaggedImage) processedImageQueue.take();
		 if (image == TaggedImageQueue.POISON) {
		 break;
		 }
		 displayImageRoutine.show(image);
		 } catch (InterruptedException ex) {
		 ReportingUtils.logError(ex);
		 }

		 }
		 }

		 }.start();
		 */
	}

	public interface DisplayImageRoutine {

		public void show(TaggedImage image);

	}

	class AcqEng implements IAcquisitionEngine2010 {

		public BlockingQueue<TaggedImage> run(SequenceSettings sequenceSettings) {
			return run(sequenceSettings, false);
		}

		//@Override
		public BlockingQueue<TaggedImage> run(SequenceSettings sequenceSettings, boolean cleanup) {
			return new LinkedBlockingDeque<TaggedImage>();
		}

		@Override
		public JSONObject getSummaryMetadata() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void pause() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void resume() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void stop() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean isRunning() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean isPaused() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean isFinished() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public boolean stopHasBeenRequested() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public long nextWakeTime() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		
		public void acquireSingle() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void attachRunnable(int frame, int position, int channel, int slice, Runnable runnable) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		@Override
		public void clearRunnables() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

      @Override
      public BlockingQueue<TaggedImage> run(org.micromanager.api.SequenceSettings sequenceSettings) {
         throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public BlockingQueue<TaggedImage> run(org.micromanager.api.SequenceSettings sequenceSettings, boolean cleanup, PositionList positionList, Autofocus device) {
         throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public BlockingQueue<TaggedImage> run(org.micromanager.api.SequenceSettings sequenceSettings, boolean cleanup) {
         throw new UnsupportedOperationException("Not supported yet.");
      }

	}
}