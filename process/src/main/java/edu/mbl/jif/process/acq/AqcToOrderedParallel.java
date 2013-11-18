package edu.mbl.jif.process.acq;

import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.mmtiff.ImageCacheListener;
import edu.mbl.jif.imaging.mmtiff.MMImageCache;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;

import edu.mbl.jif.process.IProcess;
import edu.mbl.jif.process.parallel.OrderedParallelProcessManager;
import edu.mbl.jif.process.parallel.OrderedResult;
import edu.mbl.jif.process.parallel.ResultProcessor;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Accumulates numSamples images arriving from an Acquisition and passes them on
 * for processing using OrderedParallelProcess
 *
 * @author GBH
 */
public class AqcToOrderedParallel implements ImageCacheListener {

    private OrderedParallelProcessManager oppm;
    private int seq;
    private MMImageCache imgCacheIn;  // ?? necessary
    private MMgrDatasetGenerator mdg;
    String procClassName;
    private int numSamples;
    private Map inParameters;
    private int numSamplesRecvd = 0;
    private ArrayList<TaggedImage> sampleImgs;

    AqcToOrderedParallel(MMImageCache imgCacheIn, MMgrDatasetGenerator mdg,
            String procClassName, int numSamples, Map inParameters, ResultProcessor saver) {
        this.imgCacheIn = imgCacheIn;
        this.procClassName = procClassName;
        this.numSamples = numSamples;
        this.inParameters = inParameters;
        //
        oppm = new OrderedParallelProcessManager(saver);
    }

    public void imageReceivedTest(TaggedImage taggedImage) {
        imageReceived(taggedImage);
    }

    @Override
    public void imageReceived(TaggedImage taggedImage) {
        // accumulate the sample images
        if (numSamplesRecvd == 0) {
            sampleImgs = new ArrayList<TaggedImage>();
        }
        if (numSamplesRecvd < numSamples) {
            sampleImgs.add(taggedImage);
            numSamplesRecvd++;
        } else { // then submit...
            createProcessAndSubmit(sampleImgs);
            numSamplesRecvd = 0;
        }
    }

    void createProcessAndSubmit(final ArrayList<TaggedImage> sampleImgs) {
        IProcess proc = null; // = new ProcessMagOrt();
        try {
            try {
                proc = (IProcess) Class.forName(procClassName).newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(AqcToOrderedParallel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(AqcToOrderedParallel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AqcToOrderedParallel.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (proc != null) {
            // TODO Add the rest of the parameters...
            inParameters.put("sampleImages", sampleImgs);
            proc.setInputParameters(inParameters);
            OrderedResult result = new OrderedResult(seq, proc);
            oppm.submit(result);
            seq++;
        }

    }

    @Override
    public void imagingFinished(String path) {
        oppm.submissionsComplete();
    }
    /*
     * Constructor<?> c = Class.forName(childClass).getDeclaredConstructor(constructorParam.getClass());
     c.setAccessible(true);
     c.newInstance(new Object[] {constructorParam});
     * 
     * ---------
     
        public static IProcess createProc(Integer i, String s) throws Exception {
         String procClassName = ProcessMagOrt.class.getName();
	 Constructor c = Class.forName(procClassName).getConstructor(new Class[]{Integer.class, String.class});
	 c.setAccessible(true);
	 IProcess c = (IProcess) c.newInstance(new Object[]{i , s}) ; 
	 return instance;
	 }
      */
}
