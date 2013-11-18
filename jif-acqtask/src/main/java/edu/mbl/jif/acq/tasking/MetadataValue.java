/*
 * MetadataValue.java
 *
 * Created on February 17, 2007, 9:50 AM
 */
package edu.mbl.jif.acq.tasking;

import java.util.HashMap;


/**
 *
 * @author GBH
 */

// @todo Relate to OME metadata...

public class MetadataValue extends HashMap {
    
    /** Creates a new instance of MetadataValue */
    public MetadataValue(String propertyName, String value) {
        this.put(propertyName, value);
    }

    public MetadataValue(String propertyName) {
        this.put(propertyName, "");
    }

    public MetadataValue(String[] propertyNames) {
        for (String p : propertyNames) {
            this.put(p, "");
        }
    }

    public void addProperty(String propertyName) {
        this.put(propertyName, "");
    }

    public void setValue(String propertyName, String value) {
        this.put(propertyName, value);
    }

    public String getValue(String propertyName) {
        return (String) this.get(propertyName);
    }
}
