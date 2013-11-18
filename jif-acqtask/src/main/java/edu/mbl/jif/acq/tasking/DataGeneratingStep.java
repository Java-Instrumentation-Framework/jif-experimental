
package edu.mbl.jif.acq.tasking;

/**
 *
 * @author GBH
 */
public interface DataGeneratingStep {

   Metadata generateMetadata();

   boolean storeData();
}
