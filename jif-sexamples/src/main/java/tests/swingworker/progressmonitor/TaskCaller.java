package tests.swingworker.progressmonitor;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author GBH
 */
public interface TaskCaller {
   
   void onCompleted();  //  console.append("Copy operation completed.\n");
   void onCancelled(CancellationException e); //  console.append("Copy operation canceled.\n");
   void onException(ExecutionException e); //  console.append("Exception occurred: " + e.getCause());
   
}
