/*
 * LightfieldPerspectiveFilteredSource.java
 * * Created on January 24, 2007, 11:02 AM
 */

package edu.mbl.jif.lightfield;

import edu.mbl.jif.camera.CameraModel;
import edu.mbl.jif.camacq.InstrumentController;
import java.awt.image.FilteredImageSource;

/**
 *
 * @author GBH
 */
public class LightfieldPerspectiveFilteredSource extends FilteredImageSource {
    
    /** Creates a new instance of LightfieldPerspectiveFilteredSource */
    public LightfieldPerspectiveFilteredSource(LFModel lFModel, InstrumentController instCtrl) {
        super( ((CameraModel)instCtrl.getModel("camera")).getStreamSource().getImageProducer(),
                new LightfieldPerspectiveFilter(lFModel));
//        super( ((StreamGenerator)instCtrl.getCamera()).getStreamSource().getImageProducer(),
//                new LightfieldPerspectiveFilter(lFModel));
    }
    
}
