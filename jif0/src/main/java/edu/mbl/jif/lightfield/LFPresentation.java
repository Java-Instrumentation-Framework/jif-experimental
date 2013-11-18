package edu.mbl.jif.lightfield;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;


public class LFPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

    /**
     * Creates a new instance of LFPresentation
     */
    public LFPresentation (LFModel lFModel) {
         super(lFModel);

    }


}