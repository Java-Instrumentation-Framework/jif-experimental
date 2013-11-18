/*
 */
package tests.eventBus;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class EventRecorder {

  public EventRecorder() {
    AnnotationProcessor.process(this);

    // create a writable file

    // create an editable text frame

    // append startup data.
  }

  public EventRecorder(String filePath) {
    // reopen an existing recording text file
  }

  @EventSubscriber(eventClass = StatusEvent.class)
  public void onEvent(EventToRecord event) {
    appendText(event.getEventText());
  }

  private void appendText(String eventText) {
    
  }
}

/*
 public class StatusBar extends JLabel implements EventSubscriber {

 public StatusBar() {
 EventBus.subscribe(StatusEvent.class, this);
 }

 public void onEvent(Object evt) {
 StatusEvent statusEvent = (StatusEvent) evt;
 this.setText(statusEvent.getStatusText());
 }

 }*/
