
package edu.mbl.jif.process.ps.PsCalcProcess;

import edu.mbl.jif.imaging.mmtiff.TaggedImage;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author GBH
 */
public class PSInputs {

	Map inputs = new HashMap();
	Map sampleImages = new HashMap();

	public void test() {
		
		TaggedImage img = null;
		sampleImages.put("ps.ext", img);
		sampleImages.put("ps.set1", img);
		sampleImages.put("ps.set2", img);
		inputs.put("sampleImages", sampleImages);
	}

}
