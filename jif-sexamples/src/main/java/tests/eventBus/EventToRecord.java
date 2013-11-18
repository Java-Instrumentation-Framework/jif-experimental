

package tests.eventBus;

public class EventToRecord { 
   String eventText;
   
   public EventToRecord(String statusText) { 
      this.eventText = statusText;
   }
   public String getEventText() {
      return eventText;
   }
}