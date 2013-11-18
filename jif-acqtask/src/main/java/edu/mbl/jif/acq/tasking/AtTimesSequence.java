/*
 * A sequence based on a list of specific times 
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import java.awt.Point;
import java.util.Date;
import java.util.List;

/**
 *
 * @author GBH
 */
public class AtTimesSequence extends StepListIterator implements Sequence {

   List<Date> times; // = new ArrayList<Date>();
   private static final String META = "TimePoint";

   public AtTimesSequence(List<Date> times) {
      this.times = times;
   }

   public void setTimes(List<Date> times) {
      this.times = times;
   }

   public List<Date> getTimes() {
      return times;
   }

   @Override
   public int getExtent() {
      return times.size();
   }

   @Override
   public void doNextStep(Object obj) throws StepException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doStep() throws StepException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public Date getBeginTime() {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void setBeginTime(Date beginTime) {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   }

   @Override
   public void setCurrentMetadata(Object obj) {
      getContext().put(META, String.valueOf("A point in time..."));
   }
}
