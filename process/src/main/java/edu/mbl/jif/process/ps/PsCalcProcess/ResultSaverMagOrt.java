package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.dataset.MMgrDatasetAccessor;
import edu.mbl.jif.imaging.dataset.MMgrDatasetGenerator;
import edu.mbl.jif.imaging.dataset.linked.DerivedFrom;
import edu.mbl.jif.imaging.dataset.linked.PathUtils;
import edu.mbl.jif.imaging.dataset.linked.Transform;
import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.MMImageCache;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.IProcess;
import edu.mbl.jif.process.parallel.OrderedResult;
import edu.mbl.jif.process.parallel.ResultProcessor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Saves the resulting images... 
 * 
 * May add Frame-level DerivedFrom metadata
 *
 * @author GBH
 */
public class ResultSaverMagOrt implements ResultProcessor {

    private MMgrDatasetGenerator mdg;
    private MMgrDatasetAccessor mda;
    //JSONObject sumMD;
    private boolean derivedFromByFrame = false;

    public ResultSaverMagOrt(MMgrDatasetAccessor mda, MMgrDatasetGenerator mdg) {
        this(mda, mdg, false);
    }

    public ResultSaverMagOrt(MMgrDatasetAccessor mda, MMgrDatasetGenerator mdg,
            boolean derivedFromByFrame) {
        this.mda = mda;
//        this.sumMD = mda.getImageCache().getSummaryMetadata();
        this.mdg = mdg;
        this.derivedFromByFrame = derivedFromByFrame;
    }

    public ResultSaverMagOrt(MMImageCache imgCacheIn, MMgrDatasetGenerator mdg) {
        this.mdg = mdg;
//        this.sumMD = imgCacheIn.getSummaryMetadata();
    }

    @Override
    public void processResult(OrderedResult result) {
        //System.out.println("processResult: " + result.getSeq());
        processResult(result.getProcess());
        System.out.println("Saved: " + result.getSeq());
    }

    public void processResult(IProcess proc) {
        DerivedFrom df = createDerivedFrom(proc);
        TaggedImage magImg = (TaggedImage) proc.getOutputData().get("ImgMag");
        this.saveToDataset(0, "Mag", magImg, df);
        TaggedImage ortImg = (TaggedImage) proc.getOutputData().get("ImgOrt");
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

    // Adds Frame-level DerivedFrom metadata
    private DerivedFrom createDerivedFrom(IProcess proc) {
        if (!derivedFromByFrame) {
            return null;
        } else {
            Transform xform = new Transform(
                    proc.getName(),
                    proc.getInputParameters());
            //
            String antecentPath = mda.getImageCache().getDiskLocation();
            String resultPath = mdg.getImageCache().getDiskLocation();
            String relPath = PathUtils.getRelativePath(antecentPath, resultPath);
            DerivedFrom df = DerivedFrom.createDerivedFrom(
                    relPath,
                    (List<TaggedImage>) proc.getInputData().get("sampleImages"),
                    xform);
            return df;
        }
    }

    @Override
    public void complete() {
        mda.close();
        mdg.stop();
    }

    @Override
    public void cancel() {
        mda.close();
        mdg.cancel();
    }
}