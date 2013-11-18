package tests.task;

/**
 *
 * @author GBH
 */

public class TaskEntry {
    
    private Task task; // The task to be done; it's a Command object
    private long repeatInterval; // How often task should be executed
    private long timeLastDone; // Time task was last done

    public TaskEntry(Task task, long repeatInterval) {
        this.task = task;
        this.repeatInterval = repeatInterval;
        this.timeLastDone = System.currentTimeMillis();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public long getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(long ri) {
        this.repeatInterval = ri;
    }

    public long getTimeLastDone() {
        return timeLastDone;
    }

    public void setTimeLastDone(long t) {
        this.timeLastDone = t;
    }

    public String toString() {
        return (task + " to be done every " + repeatInterval + " ms; last done " + timeLastDone);
    }
}
