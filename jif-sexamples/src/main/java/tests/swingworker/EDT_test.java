package tests.swingworker;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;


/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class EDT_test {
    public EDT_test() {
    }

    static boolean testIt() {
        if (!SwingUtilities.isEventDispatchThread()) {
            //store all the method arguments in final variables
            Callable callable = new Callable() {
                    public Boolean call() throws Exception {
                        return false; //invoke this method;
                    }
                };

            //Runnable
            FutureTask future = new FutureTask(callable);
            SwingUtilities.invokeLater(future);
            try {
                return (Boolean) future.get();
            } catch (InterruptedException e) {
                //unlikely to happen
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                //rethrow all exceptions properly
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof Error) {
                    throw (Error) cause;
                    //        } else if(cause instanceof one the checked exceptions of the method) {
                    //            throw (this exception)cause;
                } else {
                    throw new RuntimeException(cause);
                }
            }
        } else {
            return false; //invoke original method body
        }
    }

    public static void main(String[] args) {
        System.out.println("Done: " + testIt());
    }
}
