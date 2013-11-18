package tests.task;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class testFutureTask
{
   Perform perform = null;
   FutureTask<String> task = null;
   Thread thd = null;
   
   public testFutureTask()
   {
   }
   
   public static void main(String[] args)
   {
      testFutureTask test = new testFutureTask();
      test.begin();
   }
   
   private void begin()
   {
      String answer = null;
      perform = new Perform("what is the question");
      task = new FutureTask<String>(perform);
      ExecutorService es = Executors.newSingleThreadExecutor();
      es.submit (task);
      //thd = new Thread(task);
      //thd.start();
      System.out.println("\nPerforming the Task, (please wait) ... \n");
      try
      {
//         answer = task.get();  // run until complete
         answer = task.get(5000,TimeUnit.MILLISECONDS); // timeout in 5 seconds
      }
      catch (ExecutionException e)
      {
         e.printStackTrace();
      }
      catch (InterruptedException e)
      {
         answer = "got interrupted.";
      }
      catch (TimeoutException e)
      {
         answer = "tired of waiting, timed-out.";
      }
      System.out.println(answer);
      return;
   }
}

class Perform implements Callable
{
   private String input = null;
   private String output = null;
   
   public Perform(String input)
   {
      this.input = input;
   }
   
   public String call() throws Exception
   {
      output = "The response to '" + input + "' is 42";
      try
      {
         Thread.sleep(3000);
      }
      catch (InterruptedException e)
      {
         System.out.println("Perform::call(), sleep interrupted.");
      }
      return output;
   }
}
