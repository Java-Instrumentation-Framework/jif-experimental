package tests.eventBus;

import javax.swing.JLabel;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

public class StatusBar extends JLabel {

    public StatusBar() {
        AnnotationProcessor.process(this);
    }

    @EventSubscriber(eventClass = StatusEvent.class)
    public void onEvent(StatusEvent statusEvent) {
        this.setText(statusEvent.getStatusText());
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

