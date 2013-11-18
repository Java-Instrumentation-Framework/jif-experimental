/*
 * TrivialSeriesTask.java
 *
 * Created on January 28, 2007, 3:40 PM
 *
 */
package edu.mbl.jif.acq.tasking;

import edu.mbl.jif.acq.tasking.except.StepException;
import edu.mbl.jif.acq.tasking.except.StepSetupException;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author GBH
 */
public class ListOfXYPoints extends StepListIterator {

   private static final String X_META = "xPosition";
   private static final String Y_META = "yPosition";

   public ListOfXYPoints(List<Point> steps) throws StepSetupException {
      super("position", null, steps);
      setDataStoreDefn(new DataStoreDefn("listOfPoints", "_", "~",
              new MetadataValue(new String[]{X_META, Y_META})));
      this.setSeriesCapable(true);
   }
//
//   @Override
//   public void doNextStep(int i) throws StepException {
//   }

   @Override
   public void doNextStep(Object obj) throws StepException {
      AcqLogger.INSTANCE.logEvent("step: " + getName() + ": " + (Point) obj);
      setCurrentValue(
              "x" + String.valueOf((int) ((Point) obj).getX())
              + "y" + String.valueOf((int) ((Point) obj).getY()));
      setCurrentMetadata(obj);
   }

   @Override
   public void doStep() throws StepException {
      AcqLogger.INSTANCE.logEvent("doStep: " + getName());
   }

   @Override
   public void setCurrentMetadata(Object obj) {
      getContext().put(X_META, String.valueOf(((Point) obj).getX()));
      getContext().put(Y_META, String.valueOf(((Point) obj).getY()));
      getDataStoreDefn().getMetadata().setValue(X_META, String.valueOf(((Point) obj).getX()));
      getDataStoreDefn().getMetadata().setValue(Y_META, String.valueOf(((Point) obj).getY()));
   }
}
