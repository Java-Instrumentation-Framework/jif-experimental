package tests.concurrency;

/**
 *
 * @author GBH
 */
public class ThreadSuspendResume {

    public static void main(String args[]) {
        Worker worker = new Worker();
        worker.setPriority(Thread.currentThread().getPriority() - 1);
        try {
            worker.start();
            for (int i = 0; i < 5; i++) {
                Thread.sleep(5000);    // Wait 5 seconds.
                worker.safeSuspend();  // Suspend the thread.
                Thread.sleep(1000);    // Wait a second for it to resume
                worker.safeResume();   // Resume the thread
            }
            worker.safeStop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Worker extends Thread {

    private int count;
    private boolean done = false;
    private boolean suspended = false;

    synchronized void safeStop() {
        System.out.println("stopped");
        done = true;
    }

    synchronized void safeSuspend() {
        System.out.println("suspended");
        suspended = true;
    }

    synchronized void safeResume() {
        System.out.println("resumed");
        suspended = false;
        notify();
    }

    synchronized boolean ok() {
        return (!done);
    }

    synchronized void doWork() {
        if (!suspended) {
            // do something useful
            ++count;
        } else {
            while (suspended) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void run() {
        while (ok()) {
            doWork();
        }
    }

}