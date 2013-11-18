package edu.mbl.jif.stateset;

import java.util.ArrayList;

/**
 *
 * @author GBH
 */
public class SavedStateMapSet extends ArrayList<SavedStateMap>  {
    
    String setName;

    public SavedStateMapSet(String setName)
      {
        this.setName = setName;
      }
    
    

    public String getSetName()
      {
        return setName;
      }

    public void setSetName(String setName)
      {
        this.setName = setName;
      }
    
    
    public void addStateMap(SavedStateMap map)
      {
        this.add(map);
      }
    public void removeStateMap(SavedStateMap map)
      {
        this.remove(map);
      }
    
}
