/*

 */

package tests.eventBus;
import java.util.HashMap;
import java.util.Map;

import org.bushe.swing.event.EventBus;
//import org.bushe.swing.event.annotation.AnnotationProcessor;


public class EventBusBasedPublisher {

  
  private String name;
  private Map<String,Boolean> attributes = new HashMap<String,Boolean>();
  
  public EventBusBasedPublisher() {
    super();
    //AnnotationProcessor.process(this);
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setAttributes(Map<String,Boolean> attributes) {
    this.attributes = attributes;
  }
  
  public void execute() throws Exception {
    System.out.println("Executing:" + name);
    attributes.put(name, Boolean.TRUE);
    EventBus.publish(attributes);
  }
}
