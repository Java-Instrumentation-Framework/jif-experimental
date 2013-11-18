package edu.mbl.jif.process.parallel.test;

import edu.mbl.jif.imaging.dataset.metadata.FrameEvent;
import edu.mbl.jif.imaging.dataset.metadata.FrameMetadata;
import edu.mbl.jif.imaging.mmtiff.MDUtils;
import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import edu.mbl.jif.process.AbstractProcess;
import edu.mbl.jif.process.ps.PsCalcProcess.ProcessMagOrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This does the calculation/processing
 *
 * @author GBH
 */
public class TestProcess extends AbstractProcess {

	public TestProcess(Map inputParameters, Map inputData) {
		super(inputParameters, inputData);
	}

	@Override
	public String getName() {
		return "edu.mbl.ps.magort";
	}

	@Override
	public void cancel() {
		// Help, how do I Stop ??!!
	}

	@Override
	public void process() {
		processImages();
	}

	//...
	private void processImages() {
		System.out.println(printMap(this.getInputParameters()));
		List<TaggedImage> inImgs =
				(List<TaggedImage>) this.getInputParameters().get("sampleImages");
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
		Object newImage = new Object();
		addTaggedImage("imageId", 0, newImage, inImgs.get(0).tags);
	}

	void addTaggedImage(String name, int channel, Object pix, JSONObject tags) {
		JSONObject frameMD = createJsonFrameMD(name, 0, tags);
		TaggedImage image = new TaggedImage(pix, frameMD);
		this.addOutputData(name, image);
	}

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
			frameMD = FrameMetadata.generateFrameMetadata((JSONObject) getInputParameter("summaryMetadata"), evt);
		} catch (JSONException ex) {
			Logger.getLogger(ProcessMagOrt.class
					.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		return frameMD;
	}

	public static String printMap(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("{");
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 1) {
				sb.append(", ");
			}
			sb.append(entry.getKey()).append("=").append(entry.getValue());
		}
		sb.append("}");
		return sb.toString();
	}

	public static void dumpMapOfMap(Map map) {
		dumpMapOfMap(map, 0);
	}

	public static void dumpMapOfMap(Map map, int depth) {
		Set s = map.entrySet();
		Iterator sit = s.iterator();
		boolean isFirst = true;

		while (sit.hasNext()) {
			Map.Entry elem = (Map.Entry) sit.next();
			String key = (String) elem.getKey();
			Object value = elem.getValue();

			if (value instanceof String) {
				// recursivity stop condition
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < depth; i++) {
					sb.append("   ");
				}
				System.out.print(sb.toString());
				System.out.print(key);
				System.out.print(" : ");
				System.out.println(value);
			} else {
				if (!isFirst) {
					System.out.println("");
				} else {
					isFirst = false;
				}
				System.out.println(key);
				Map valueMap = (Map) elem.getValue();
				dumpMapOfMap(valueMap, depth++);
			}
		}
	}

	public static void main(String[] args) {
		Map map = new HashMap();
		Map map2 = new HashMap();
		map2.put("This", "that");
		map2.put("Foo", "Bar");
		map.put("First", "thing");
		map.put("Map2", map2);
		dumpMapOfMap(map);
	}

}
