/*
 * DataStoreDefn.java
 *
 * Created on January 30, 2007, 11:59 AM
 *
 */
package edu.mbl.jif.acq.tasking;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Defines how to organize and name the data that is output to the data store.
 * 
 * @author GBH
 */
public class DataStoreDefn {

   /* Demarcator 
   DIR: separate into another directory/folder
   * FILE: separate into another file
   * 
   * 
   */
    public enum Demarcator {
        DIR, FILE, ID, PAGE;
    }
    
    private String name;
    //int dataIdType;
    private String dataIdString = "none";
    private String prefix = "_";
    private String postfix = "~";
    // Metadata
    private MetadataValue metadata;

    /**
     * Creates a new instance of DataStoreDefn
     */
    public DataStoreDefn(String name, String prefix, String postfix, String metadataPropertyName) {
        this.setName(name);
        this.setPrefix(prefix);
        this.setPostfix(postfix);
        this.setMetadataProperty(metadataPropertyName);
    }

    public DataStoreDefn(String name, String prefix, String postfix, MetadataValue metadata) {
        this.setName(name);
        this.setPrefix(prefix);
        this.setPostfix(postfix);
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataIdString() {
        return dataIdString;
    }

    public void setDataIdString(String dataIdString) {
        this.dataIdString = new String(dataIdString);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public String getPostfix() {
        return postfix;
    }

    public void setMetadataProperty(String metadataPropertyName) {
        metadata = new MetadataValue(metadataPropertyName);
    }

    public void setMetadataProperties(String[] metadataProps) {
        metadata = new MetadataValue(metadataProps);
    }

    public MetadataValue getMetadata() {
        return metadata;
    }

//    public void setMetadataValue(String metadataValue) {
//        
//    }
//
//    public void setMetadataValue(String[] metadataValues) {
//        // @todo How to resolve objects in [stepsList] to a set of metadata pairs?
//       // this.metadataValue = String.valueOf( metadataValues);
//    }    
}

