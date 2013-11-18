

package tests.eventBus;

public class StatusEvent { 
   String statusText;
   
   public StatusEvent(String statusText) { 
      this.statusText = statusText;
   }
   public String getStatusText() {
      return statusText;
   }
}