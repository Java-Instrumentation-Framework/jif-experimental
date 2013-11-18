package tests.task;

import java.io.PrintStream;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScheduleTester {

  public static void main(String[] args) {
    // Get the scheduler
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Get a handle, starting now, with a 10 second delay
    final ScheduledFuture<?> timeHandle =
            scheduler.scheduleAtFixedRate(new TimePrinter(System.out), 0, 1, SECONDS);

    // Schedule the event, and run for some seconds
    scheduler.schedule(new Runnable() {  // terminator

      public void run() {
        timeHandle.cancel(false);
        System.exit(0);
      }
    }, 10, SECONDS);

  /**
   * On some platforms, you'll have to setup this infinite loop to see output 
   * while (true) { }
   */
  }
}

class TimePrinter implements Runnable {

  private PrintStream out;

  public TimePrinter(PrintStream out) {
    this.out = out;
  }

  public void run() {
    out.printf("Current time: %tr%n", new Date());
  }
}
/*
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // Get a handle, starting now, with a 10 second delay
    final ScheduledFuture<?> timeHandle =
            scheduler.scheduleAtFixedRate(new TimePrinter(System.out), 0, 1, SECONDS);

    // Schedule the event, and run for some seconds
    // create the terminator
    scheduler.schedule(new Runnable() {  // terminator
      public void run() {
        timeHandle.cancel(false);
      }
    }, 10, SECONDS);
// ??? is this ever necessary: scheduler.shutdown();
 *
 *
class ScheduledTask implements Runnable {
  public void run() {
    //out.printf("Current time: %tr%n", new Date());
  }
}
 */