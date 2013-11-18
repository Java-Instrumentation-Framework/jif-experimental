/*
 * ImageAcqStep.java
 *
 * Created on January 28, 2007, 2:33 PM
 */
package edu.mbl.jif.acq.tasking.test;

import edu.mbl.jif.acq.tasking.AbstractStep;
import edu.mbl.jif.acq.tasking.AcqLogger;
import edu.mbl.jif.acq.tasking.DataGeneratingStep;
import edu.mbl.jif.acq.tasking.DataStoreDefn;
import edu.mbl.jif.acq.tasking.DataStoreManager;
import edu.mbl.jif.acq.tasking.Metadata;
import edu.mbl.jif.acq.tasking.except.StepException;

/**
 *
 * @author GBH
 */
public class ImageAcqStep extends AbstractStep implements DataGeneratingStep {

   static final String NAME = "ImageAcqStep";
   static final String DS_NAME = "imageAcq";
   static final String META = "imageAcqzMeta";
   DataStoreDefn dsDefn = new DataStoreDefn(DS_NAME, "|", "|", META);

   public ImageAcqStep(String name) {
      super(name);
      //metadataPropertyName = "imageAcqMetaProp-" + name;
      //DataStoreDefn dataStoreDefn = new DataStoreDefn(name, prefix, postfix, metadataPropertyName);
      setDataStoreDefn(dsDefn);
      // create DataStoreScheme, where each site is in a separate directory
      getDataStoreScheme().addDisposition("listOfPoints", DataStoreDefn.Demarcator.DIR);
      getDataStoreScheme().addDisposition("zSeries1", DataStoreDefn.Demarcator.ID);
      getDataStoreScheme().addDisposition("tSeries1", DataStoreDefn.Demarcator.ID);
      getDataStoreScheme().addDisposition("tStep", DataStoreDefn.Demarcator.ID);

   }

      @Override
   public void doStep() throws StepException {
      AcqLogger.INSTANCE.logEvent("doStep: " + getName());
      
      //store it using DataStoreScheme
      setCurrentValue(getName());
      setCurrentMetadata("channel");
      DataStoreManager.INSTANCE.saveImageFileToDataStore(null, getDataStoreScheme());
      
   }


   @Override
   public void setCurrentMetadata(Object obj) {
      getDataStoreDefn().getMetadata().setValue(META, String.valueOf(obj));
   }

   @Override
   public Metadata generateMetadata() {
      return null;
   }

   @Override
   public boolean storeData() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public int getExtent() {
      return 1;
   }
   
}
