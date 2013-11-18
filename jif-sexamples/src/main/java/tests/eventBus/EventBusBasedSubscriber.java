/*
 */
package tests.eventBus;

import java.util.Map;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class EventBusBasedSubscriber {

  public EventBusBasedSubscriber() {
    super();
    AnnotationProcessor.process(this);
  }

  @EventSubscriber
  public void onEvent(Map<String, Boolean> attributes) {
    System.out.println("Caught an event");
    System.out.println(attributes.toString());
  }
}
