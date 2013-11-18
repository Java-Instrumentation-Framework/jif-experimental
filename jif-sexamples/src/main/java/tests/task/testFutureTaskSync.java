package tests.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class testFutureTaskSync {

//    Perform perform = null;
//    FutureTask<Boolean> task = null;
//    Thread thd = null;
//
//    public testFutureTaskSync() {
//    }
//
//    public static void main(String[] args) {
//        testFutureTaskSync test = new testFutureTaskSync();
//        test.begin();
//    }
//
//    private void begin() {
//        Boolean result = false;
//        perform = new Perform("what is the question");
//        task = new FutureTask<Boolean>(perform);
//        ExecutorService es = Executors.newSingleThreadExecutor();
//        es.submit(task);
//        //thd = new Thread(task);
//        //thd.start();
//        System.out.println("\nPerforming the Task, (please wait) ... \n");
//        try {
////         result = task.get();  // run until complete
//            result = task.get(5000, TimeUnit.MILLISECONDS); // timeout in 5 seconds
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            result = false;
//        } catch (TimeoutException e) {
//            result = false;
//        }
//        System.out.println(result);
//        return;
//    }
//
//}
//
//class Perform implements Callable {
//
//    private String input = null;
//    private String output = null;
//
//    public Perform(final String input) {
//        this.input = input;
//    }
//
//    public String call() throws Exception {
//        output = "The response to '" + input + "' is 42";
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            System.out.println("Perform::call(), sleep interrupted.");
//        }
//        return output;
//    }

}
