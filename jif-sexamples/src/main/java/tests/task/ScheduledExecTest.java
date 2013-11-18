package tests.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class ScheduledExecTest {
    static ScheduledExecutorService sched;

    public ScheduledExecTest() {
        sched = Executors.newSingleThreadScheduledExecutor();
        runEveryTwoSeconds(20);
    }

    public void runEveryTwoSeconds(long howLong) {
        
        Runnable rTask = new Runnable() {
                public void run() {
                    /* Work to do */
                    System.out.println("Workin...");
                }
            };
            
        final ScheduledFuture<?> rTaskFuture =
                sched.scheduleAtFixedRate(rTask, 0, 2, TimeUnit.SECONDS);

        
        Runnable toRun = new Runnable() {
                public void run() {
                    rTaskFuture.cancel(true);
                    System.exit(0);
                }
            };
            
        sched.schedule(toRun, howLong, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new ScheduledExecTest();
    }
}
