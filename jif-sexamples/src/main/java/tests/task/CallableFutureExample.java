/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.task;

import java.util.*;
import java.util.concurrent.*;

public class CallableFutureExample {

    public static class WordLengthCallable
            implements Callable {

        private String word;

        public WordLengthCallable(String word) {
            this.word = word;
        }

        public Integer call() {
            return Integer.valueOf(word.length());
        }
    }

    public static void main(String args[]) throws Exception {
        args = new String[]{"this", "that", "anything", "whatshamacallit"};
        
        ExecutorService pool = Executors.newFixedThreadPool(3);
        
        Set<Future<Integer>> set = new HashSet<Future<Integer>>();
        
        for (String word : args) {
            Callable<Integer> callable = new WordLengthCallable(word);
            Future<Integer> future = pool.submit(callable);
            set.add(future);
        }
        
        int sum = 0;
        for (Future<Integer> future : set) {
            sum += future.get();
        }
        System.out.printf("The sum of lengths is %s%n", sum);
        System.exit(sum);
    }
}
