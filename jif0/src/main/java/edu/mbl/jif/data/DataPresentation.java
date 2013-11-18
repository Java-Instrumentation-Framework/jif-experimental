package edu.mbl.jif.data;

import edu.mbl.jif.data.DataModel;
import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;


public class DataPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

      // --- imageFileFormat
      private final SelectionInList selectionInImageFileFormatList;

    /**
     * Creates a new instance of DataPresentation
     */
    public DataPresentation (DataModel dataModel) {
         super(dataModel);

         // --- imageFileFormat
         selectionInImageFileFormatList = new SelectionInList(dataModel.getImageFileFormatListModel(),
               getModel(DataModel.PROPERTYNAME_IMAGEFILEFORMAT));
    }


    // --- imageFileFormat
    public SelectionInList getSelectionInImageFileFormatList () {
         return selectionInImageFileFormatList;
    }

}