package edu.mbl.jif.process.ps.PsCalcProcess;


import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.mmtiff.MMImageCache;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.ChannelSetProcessor;
import edu.mbl.jif.process.IProcess;
import edu.mbl.jif.process.parallel.OrderedParallelProcessManager;
import edu.mbl.jif.process.parallel.OrderedResult;
import java.util.ArrayList;
import java.util.List;


/**
 * Accesses a PolStack dataset (assuming channels mag, ort, 5 samples) and
 * extracts the mag & ort images to a new dataset with DerivedFrom metadata.
 *
 * @author GBH
 */
public class MagOrtParallelChannelSetProcessor extends ChannelSetProcessor {

    private final MMgrDatasetAccessor mda;
    private OrderedParallelProcessManager submitter;
    MMImageCache in;
    private int seq = 0;
    int numSamples = 5;

    public MagOrtParallelChannelSetProcessor(MMgrDatasetAccessor mda,
            OrderedParallelProcessManager submitter) {
        this.mda = mda;
        this.submitter = submitter;
        in = mda.getImageCache();
    }

    // ChannelSetProcessor implementation...
    @Override
    public void process(int z, int t, int p) {
        // called for each z,t,p by DatasetProcessor.processAll();
        //System.out.println("MagOrientProcessor.process(" + z + "," + t + "," + p + ")");
        List<TaggedImage> sampleImgs = new ArrayList<TaggedImage>();
        for (int i = 0; i < numSamples; i++) {
            TaggedImage img = mda.getImageCache().getImage(i + 2, z, t, p);
            if (img == null) {
                System.out.println("**** " + "img==null at submit seq: " + seq
                        + "   i: " + i + "  z: " + z + "  t: " + t + "  p: " + p);
            }
            sampleImgs.add(img);
        }
        // Set Parameters...
        this.addData("sampleImages", sampleImgs);
        IProcess proc = new ProcessMagOrt(this.parameters, this.data);
        OrderedResult result = new OrderedResult(seq, proc);
        submitter.submit(result);
        seq++;
    }
}
