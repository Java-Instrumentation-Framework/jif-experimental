package edu.mbl.jif.stateset;

import java.util.ArrayList;
import javax.swing.JTabbedPane;

/**
 *
 * @author GBH
 */
public class SavedStatePrefsSet {

    ArrayList<SavedStatePrefs> savedStatePrefs = new ArrayList<SavedStatePrefs>();
    protected String name;
    SavedStateMapSet savedStateMapSet;

    public SavedStatePrefsSet(String name)
      {
        this.name = name;
      }

//    public SavedStatePrefsSet(SavedStateMapSet savedStateMapSet)
//      {
//        this.savedStateMapSet = savedStateMapSet;
//        for (SavedStateMap map : savedStateMapSet) {
//            savedStatePrefs.add(new SavedStatePrefs(map));
//        }
//      }

    public String getName()
      {
        return name;
      }

    public void setName(String name)
      {
        this.name = name;
      }

    public void addState(SavedStatePrefs state)
      {
        savedStatePrefs.add(state);
      }

    public void removeSet(SavedStatePrefs state)
      {
        savedStatePrefs.remove(state);
      }

    public void enter()
      {
        for (SavedStatePrefs state : savedStatePrefs) {
            state.syncToModel();
        }
      }

    public void exit()
      {
      }

    public void adoptCurrentSettings()
      {
        for (SavedStatePrefs state : savedStatePrefs) {
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

//    public SavedStateMapSet createSavedStateMapSet()
//      {
//        SavedStateMapSet ssMap = new SavedStateMapSet(getName());
//        for (SavedStatePrefs state : savedStatePrefs) {
//            ssMap.add(state.savedStateMap);
//        }
//        return ssMap;
//      }

//    public JTabbedPane getPropertyEditorPanel()
//      {
//        JTabbedPane pane = new JTabbedPane();
//        for (SavedStatePrefs state : savedStatePrefs) {
//            final PropertySheetPage sheet = new PropertySheetPage(state.stateBean);
//            pane.addTab(state.displayName, sheet);
//        }
//        return pane;
//      }

}
