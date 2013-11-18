package binding;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;


public class ClazzPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

      // --- selectableNumber
      private final SelectionInList selectionInSelectableNumberList;

    /**
     * Creates a new instance of ClazzPresentation
     */
    public ClazzPresentation (ClazzModel clazzModel) {
         super(clazzModel);

         // --- selectableNumber
         selectionInSelectableNumberList = new SelectionInList(clazzModel.getSelectableNumberListModel(),
               getModel(ClazzModel.PROPERTYNAME_SELECTABLENUMBER));
    }


    // --- selectableNumber
    public SelectionInList getSelectionInSelectableNumberList () {
         return selectionInSelectableNumberList;
    }

}