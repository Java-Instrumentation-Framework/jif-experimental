/*
 * DataStoreManager.java
 *
 * Created on January 31, 2007, 1:54 PM
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.imaging.tiff.MultipageTiffFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DataStoreManager
 * Saves data into dir/file structures as defined in 
 * the DataStoreScheme and DataStoreDefn.
 * Manages a set of output (.tif) files.
 * Is a singleton (using enum).
 * @author GBH
 */
public enum DataStoreManager {

  INSTANCE;
  String dataDirPath = "C:/";
  // DataStoreDefn's
  Map<String, DataStoreDefn> dSDefns = new HashMap<String, DataStoreDefn>();
  // List of Open TIFF files
  Map<String, MultipageTiffFile> openTiffs = new HashMap<String, MultipageTiffFile>();

  public void addDataStoreDefn(DataStoreDefn dStoreDefn) {
    dSDefns.put(dStoreDefn.getName(), dStoreDefn);
  }

  public void setDataDirectory(String dataDirPath) {
    this.dataDirPath = dataDirPath;
  }

  /*
   * resolves the set of DataStoreDefns
   */
  public String resolveSchema(DataStoreScheme dataStoreScheme) {
    StringBuffer path = new StringBuffer();
    StringBuffer filename = new StringBuffer();
    Iterator it = dataStoreScheme.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry) it.next();
      //AcqLogger.INSTANCE.logEvent(pairs.getKey() + ", disposition: " + pairs.getValue());
      String dataStoreDefnName = (String) pairs.getKey();
      //AcqLogger.INSTANCE.logEvent("dataStoreDefnName: " + dataStoreDefnName);
      DataStoreDefn def = (DataStoreDefn) dSDefns.get(dataStoreDefnName);
      if (def != null) {
        String defID = def.getDataIdString(); // the currentValue
        DataStoreDefn.Demarcator disposition = (DataStoreDefn.Demarcator) pairs.getValue();
        switch (disposition) {
          case DIR:
            path.append(def.getPrefix() + defID + def.getPostfix() + "/");
            break;
          case FILE:
            break;
          case ID:
            filename.append(def.getPrefix() + defID + def.getPostfix());
            break;
          case PAGE:
            break;
        }
      }
    }
    String result = path.toString() + filename.toString();
    return result;
  }

  public void getMetadataContext() {
    if (false) {
      return;
    }
    AcqLogger.INSTANCE.logEvent("MetadataContext [");
    List<String> msgs = new ArrayList<String>();
     for (Map.Entry pairs : this.dSDefns.entrySet()) {
        DataStoreDefn def = (DataStoreDefn) pairs.getValue();
        if (def != null) {
          String defID = def.getDataIdString();
          StringBuilder sb = new StringBuilder();
          sb.append("    " + defID + ":");
          MetadataValue metadata = def.getMetadata();
          Iterator itm = metadata.entrySet().iterator();
          while (itm.hasNext()) {
            Map.Entry pairsm = (Map.Entry) itm.next();
            sb.append(" " + pairsm.getKey() + "=" + pairsm.getValue() + ";");
          }
          msgs.add(sb.toString());
        }
     }
    AcqLogger.INSTANCE.logEvent(msgs);
    AcqLogger.INSTANCE.logEvent("]");
  }

  public void saveImageFileToDataStore(BufferedImage img, DataStoreScheme dataStoreScheme) {
    // Build the path/filename
    String filepath = resolveSchema(dataStoreScheme);
    String savePath = dataDirPath + filepath;
    AcqLogger.INSTANCE.logEvent("savePath: " + savePath);
    // Construct metadata
    this.getMetadataContext();
    // construct OME-XML metadata
    // construct Tiff metadata
   // MultipageTiffFile tiffFile = getTiffFile(savePath);
   // tiffFile.appendImage(img);
  }

  private MultipageTiffFile getTiffFile(String savePath) {
    MultipageTiffFile tiffFile = openTiffs.get(savePath);
    if (tiffFile != null) {
      return tiffFile;
    } else {
      tiffFile = new MultipageTiffFile(savePath);
    }
    return tiffFile;
  }

  public void endAcquisition() {
    // Close all Tiff files
    for (Map.Entry<String, MultipageTiffFile> file : openTiffs.entrySet()) {
      AcqLogger.INSTANCE.logEvent("endAcquisition: closing tiff file: " + 
					file.getKey() + ": " + file.getValue());
    }
  }
}
