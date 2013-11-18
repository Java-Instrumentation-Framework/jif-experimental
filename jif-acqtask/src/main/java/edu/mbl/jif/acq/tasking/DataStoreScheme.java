/*
 * DataStoreScheme.java
 * Created on January 31, 2007, 1:54 PM
 */

package edu.mbl.jif.acq.tasking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a map of (name, spec) pairs that specify the disposition when saving from a Task
 * 
 * @author GBH
 */

public class DataStoreScheme extends HashMap{
    
    public DataStoreScheme() {
        super();
    }
    
    public void addDisposition(String dataStoreDefnName, DataStoreDefn.Demarcator disposition) {
        this.put(dataStoreDefnName, disposition);
    }
    
    public void dump() {
        Iterator it = this.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }
    }
    
    public static void main(String[] args) {
        DataStoreScheme dScheme = new DataStoreScheme();
        DataStoreDefn defn1 = new DataStoreDefn("def1", "_", "~", "metaDef1");
        dScheme.addDisposition(defn1.getName(), DataStoreDefn.Demarcator.ID);
        dScheme.dump();

    }
}
