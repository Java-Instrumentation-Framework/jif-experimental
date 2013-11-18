package edu.mbl.jif.workframe.test;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;


public class TestPresentation extends PresentationModel {

      // SelectionInList's holds the bean's list model plus a selection

      // --- numberSelect
      private final SelectionInList selectionInNumberSelectList;

      // --- valueSelect
      private final SelectionInList selectionInValueSelectList;

      // --- stringSelect
      private final SelectionInList selectionInStringSelectList;

    /**
     * Creates a new instance of TestPresentation
     */
    public TestPresentation (TestModel testModel) {
         super(testModel);

         // --- numberSelect
         selectionInNumberSelectList = new SelectionInList(testModel.getNumberSelectListModel(),
               getModel(TestModel.PROPERTYNAME_NUMBERSELECT));
         // --- valueSelect
         selectionInValueSelectList = new SelectionInList(testModel.getValueSelectListModel(),
               getModel(TestModel.PROPERTYNAME_VALUESELECT));
         // --- stringSelect
         selectionInStringSelectList = new SelectionInList(testModel.getStringSelectListModel(),
               getModel(TestModel.PROPERTYNAME_STRINGSELECT));
    }


    // --- numberSelect
    public SelectionInList getSelectionInNumberSelectList () {
         return selectionInNumberSelectList;
    }

    // --- valueSelect
    public SelectionInList getSelectionInValueSelectList () {
         return selectionInValueSelectList;
    }

    // --- stringSelect
    public SelectionInList getSelectionInStringSelectList () {
         return selectionInStringSelectList;
    }

}