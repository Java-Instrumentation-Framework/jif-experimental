package tests.concurrency.multicore;

import java.util.concurrent.*;

public class OrderedCompletionServiceTest {
   private static int NUM_OF_TASKS = 50;

   public OrderedCompletionServiceTest() {}

   public void run() {
      long begTest = new java.util.Date().getTime();

      int nrOfProcessors = Runtime.getRuntime().availableProcessors();
      ExecutorService eservice = Executors.newFixedThreadPool(nrOfProcessors);
      CompletionService < Object > cservice = new ExecutorCompletionService < Object > (eservice);

      for (int index = 0; index < NUM_OF_TASKS; index++)
         cservice.submit(new Task(index));

      Object taskResult;
      for(int index = 0; index < NUM_OF_TASKS; index++) {
         try {
            taskResult = cservice.take().get();
            System.out.println("result "+taskResult);
         }
         catch (InterruptedException e) {}
         catch (ExecutionException e) {}
      }
      Double secs = new Double((new java.util.Date().getTime() - begTest)*0.001);
      System.out.println("run time " + secs + " secs");
   }

   public static void main(String[] args) {
      new OrderedCompletionServiceTest().run();
      System.exit(0);
   }
}