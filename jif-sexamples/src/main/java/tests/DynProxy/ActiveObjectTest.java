

package tests.DynProxy;

import java.lang.reflect.Proxy;

/*
 * From http://benpryor.com/blog/page/4/?/archives/16-API-Design-The-Principle-of-Audience_html
 */

public class ActiveObjectTest
{
    public static void main(String[] args)
    {
        AdderService addService = (AdderService)
            Proxy.newProxyInstance(
                ActiveObjectTest.class.getClassLoader(),
                new Class[] {AdderService.class},
                new ActiveObjectProxy(new Adder()));

        AdderResultsCallback callback = new AdderResultsCallback()
        {
            public void addResultsComputed(int x, int y, int sum)
            {
                System.out.println(x + " + " + y + " = " + sum);
            }
        };

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 60; i++)
        {
            addService.add(2 * i, 3 * i, callback);
        }
        long elapsed = System.currentTimeMillis() - startTime;

        System.out.println("All calls finished: " + elapsed + " ms elapsed");
    }


    private static interface AdderService
    {
        public void add(int x, int y, AdderResultsCallback callback);
    }

    private static interface AdderResultsCallback
    {
        public void addResultsComputed(int x, int y, int sum);
    }

    private static class Adder implements AdderService
    {
        public void add(int x, int y, AdderResultsCallback callback)
        {
            int sum = x + y;
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) { }
            callback.addResultsComputed(x, y, sum);
        }
    }
}
