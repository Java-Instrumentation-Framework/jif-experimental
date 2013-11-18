package edu.mbl.jif.stateset;

import java.util.HashMap;

/**
 *
 * @author GBH
 */
public class SavedStateMap {
    public String displayName;
    public String associatedModel;
    public HashMap propertiesMap;

    public SavedStateMap(String displayName, String associatedModel)
      {
        this.displayName = displayName;
        this.associatedModel = associatedModel;
        propertiesMap = new HashMap();
      }

    public void addProperty(String name, Object value)
      {
        propertiesMap.put(name, value);
        Object obj = propertiesMap.get(value);
      }

}
