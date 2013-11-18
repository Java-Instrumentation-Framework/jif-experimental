package edu.mbl.jif.stateset;

import java.util.ArrayList;
import javax.swing.JTabbedPane;

/**
 *
 * @author GBH
 */
public class SavedStateSet {

    ArrayList<SavedState> savedStates = new ArrayList<SavedState>();
    protected String name;
    SavedStateMapSet savedStateMapSet;

    public SavedStateSet(String name)
      {
        this.name = name;
      }

    public SavedStateSet(SavedStateMapSet savedStateMapSet)
      {
        this.savedStateMapSet = savedStateMapSet;
        for (SavedStateMap map : savedStateMapSet) {
            savedStates.add(new SavedState(map));
        }
      }

    public String getName()
      {
        return name;
      }

    public void setName(String name)
      {
        this.name = name;
      }

    public void addState(SavedState state)
      {
        savedStates.add(state);
      }

    public void removeSet(SavedState state)
      {
        savedStates.remove(state);
      }

    public void enter()
      {
        for (SavedState state : savedStates) {
            state.syncToModel();
        }
      }

    public void exit()
      {
      }

    public void adoptCurrentSettings()
      {
        for (SavedState state : savedStates) {
            state.syncFromModel();
        }
      }

    protected String filename;

    public String getFilename()
      {
        return filename;
      }

    public void setFilename(String filename)
      {
        this.filename = filename;
      }

    public SavedStateMapSet createSavedStateMapSet()
      {
        SavedStateMapSet ssMap = new SavedStateMapSet(getName());
        for (SavedState state : savedStates) {
            ssMap.add(state.savedStateMap);
        }
        return ssMap;
      }

    public JTabbedPane getPropertyEditorPanel()
      {
        JTabbedPane pane = new JTabbedPane();
        for (SavedState state : savedStates) {
            final PropertySheetPage sheet = new PropertySheetPage(state.stateBean);
            pane.addTab(state.displayName, sheet);
        }
        return pane;
      }

}
