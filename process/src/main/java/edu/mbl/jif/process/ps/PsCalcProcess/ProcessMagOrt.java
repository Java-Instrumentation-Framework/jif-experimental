package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.dataset.linked.Antecedent;
import edu.mbl.jif.imaging.dataset.linked.Xform;
import edu.mbl.jif.imaging.dataset.metadata.FrameEvent;
import edu.mbl.jif.imaging.dataset.metadata.FrameMetadata;
import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.AbstractProcess;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This does the calculation/processing
 *
 * @author GBH
 */
public class ProcessMagOrt extends AbstractProcess implements Xform {

    public ProcessMagOrt() {
    }
    // "edu.mbl.jif.ps.PsCalcProcess.ProcessMagOrt"

    public ProcessMagOrt(Map inputParameters, Map inputData) {
        super(inputParameters, inputData);
    }

    @Override
    public String getName() {
        return this.getClass().getCanonicalName();
    }

    // XForm parameter specification...
    // ++ How about TypeMap ??
    @Override
    public Map<String, Object> getParameterMap() {
        Map<String, Object> map = new ConcurrentHashMap<String, Object>();
        map.put("wavelength", 546.0f);
        map.put("swingFraction", 0.3f);
        map.put("retCeiling", 100.0f);
        map.put("azimuthRef", 90f);
        map.put("zeroIntensity", 0f);
        map.put("doBkgdCorrect", true);
        map.put("algorithm", 5);
        Antecedent[] ants = new Antecedent[5];
        map.put("BackgroundImages", ants);
        return map;
    }

    @Override
    public void cancel() {
        // Help, how do I Stop ??!!
    }

    @Override
    public void process() {
        createMagAndOrientImages();
    }

    //...
    private void createMagAndOrientImages() {
        List<TaggedImage> inImgs = (List<TaggedImage>) getInputDatum("sampleImages");
        PolStack bkgd = (PolStack) getInputDatum("bkgdPolStack");
        try {
            if (inImgs == null) {
                System.err.println("(inImgs==null)");
                return;
            }
            if (inImgs.get(0) == null) {
                System.out.println("inImgs.get(0)");
            } else {
                PolStack ps = new PolStack(inImgs);
                ps.bg21 = bkgd.bg21;
                ps.bg31 = bkgd.bg31;
                ps.bg21_5frame = bkgd.bg21_5frame;
                ps.bg21_5frame = bkgd.bg31_5frame;
                PolCalculator calc = new PolCalculator();
                calc.setParameters(this.getInputParameters());
//                calc.setParameters((Float) getInputParameter("wavelength"),
//                        (Float) getInputParameter("swingFraction"),
//                        (Float) getInputParameter("retCeiling"),
//                        (Float) getInputParameter("azimuthRef"),
//                        (Float) getInputParameter("zeroIntensity"),
//                        (Boolean) getInputParameter("doBkgdCorrect"),
//                        (Integer) getInputParameter("algorithm"));

                calc.calcRetardanceImages(ps, true, true);

                // do some test work...
//			try {
//				String str = "";
//				Thread.sleep(100);
//				// loop that just concatenate a str to simulate
//				// work on the result form remote call
//				for (int i = 0; i < 20000; i++) {
//					str = str + 't';
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
                // Results are placed in outImages...
                List<TaggedImage> outImages = new ArrayList<TaggedImage>();
                addTaggedImageToOutput("ImgMag", 0, ps.imgMagnitude, inImgs.get(0).tags);
                addTaggedImageToOutput("ImgOrt", 1, ps.imgAzimuth, inImgs.get(0).tags);
            }
        } catch (Exception e) {
            System.err.println("Exeception in createMagAndOrientImages");
            e.printStackTrace();
        }
    }

    void addTaggedImageToOutput(String name, int channel, Object pix, JSONObject tags) {
        JSONObject frameMD = createJsonFrameMD(name, channel, tags);
        TaggedImage image = new TaggedImage(pix, frameMD);
        this.addOutputData(name, image);
    }

    // TODO This is not efficient... 
    // only changing the channel?
    JSONObject createJsonFrameMD(String name, int channel, JSONObject tags) {
        JSONObject frameMD = null;
        try {
            FrameMetadata meta = new FrameMetadata();
            FrameEvent evt = new FrameEvent();
            evt.frame = MDUtils.getFrameIndex(tags);
            evt.frameIndex = MDUtils.getFrameIndex(tags);
            evt.channel = name;
            evt.channelIndex = channel;
            evt.slice = MDUtils.getSliceIndex(tags);;
            evt.sliceIndex = MDUtils.getSliceIndex(tags);
            evt.positionIndex = MDUtils.getPositionIndex(tags);
            frameMD = FrameMetadata.generateFrameMetadata(
                    (JSONObject) getInputDatum("summaryMetadata"), evt);
        } catch (JSONException ex) {
            Logger.getLogger(ProcessMagOrt.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return frameMD;
    }
    /*
          for (int chan = 0; chan < numChannels; chan++) {
         JSONObject frameMD_NxN = imgNxNChannel[chan].tags;
         // create Frame metadata
         // Copy frameMD_NxN
         * 
         FrameEvent evt = new FrameEvent();
         evt.frame = frame;
         evt.frameIndex = frame;
         evt.channelIndex = chan;
         // evt.channel = name;
         evt.slice = slice;
         evt.sliceIndex = slice;
         evt.positionIndex = pos;
         JSONObject frameMD = FrameMetadata.modifiedFrameMetadata(frameMD_NxN, evt);
         ImageProcessor ip = stackOfChannelsForSlice.getProcessor(chan + 1);
         try {
            // new UUID
            // else?
            MDUtils.setWidth(frameMD, ip.getWidth());
            MDUtils.setHeight(frameMD, ip.getHeight());
         } catch (JSONException ex) {
            Logger.getLogger(ConvertMultiFocusToZSections.class.getName()).log(Level.SEVERE, null, ex);
         }
         dsg.putImage(ip.getPixels(), frameMD);
      }
      */
}
