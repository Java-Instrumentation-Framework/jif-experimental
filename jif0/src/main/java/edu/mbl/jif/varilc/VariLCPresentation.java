package edu.mbl.jif.varilc;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;


public class VariLCPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

      // --- type
      private final SelectionInList selectionInTypeList;

    /**
     * Creates a new instance of VariLCPresentation
     */
    public VariLCPresentation (VariLCModel variLCModel) {
         super(variLCModel);

         // --- type
         selectionInTypeList = new SelectionInList(variLCModel.getTypeListModel(),
               getModel(VariLCModel.PROPERTYNAME_TYPE));
    }


    // --- type
    public SelectionInList getSelectionInTypeList () {
         return selectionInTypeList;
    }

}