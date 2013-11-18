/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tests.eventBus;

// import org.junit.Test;

public class EventBusEventsTest {


  public void testEvents() throws Exception {
    EventBusBasedSubscriber subscriber = new EventBusBasedSubscriber();
    EventBusBasedPublisher publisher = new EventBusBasedPublisher();
    for (int i = 0; i < 10; i++) {
      publisher.setName("PUB_" + i);
      publisher.execute();
    }
  }
    public static void main(String[] args) {
        try {
            (new EventBusEventsTest()).testEvents();
        } catch (Exception ex) {
        }
    }
}
