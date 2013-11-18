/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.BeanContext;

/*
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE,
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */
import java.beans.beancontext.BeanContextChildSupport;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServicesSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * A test program that creates all of the objects, a tests the service
 * capabilities. Run this program from the command line using java
 * DocumentTester
 */
public class DocumentTester {

    public static void main(String[] args)
      {
        BeanContextServicesSupport context = new BeanContextServicesSupport(); // a
        // bean
        // context
        DocumentBean doc1 = new DocumentBean("build.xml");
        context.add(doc1);
        context.addBeanContextServicesListener(doc1); // listen for new services
        WordCountServiceProvider provider = new WordCountServiceProvider();
        context.addService(WordCount.class, provider); // add the service to the context

      }

}

/**
 * A JavaBean that encapsulates a text file. When added to a bean context, this
 * bean listens for a WordCount service to become available. When the service
 * does become available, the DocumentBean requests an instance of the service.
 * The service then counts the number of words in the file, and prints a report
 * to standard output.
 */
final class DocumentBean extends BeanContextChildSupport {

    private File document;
    private BeanContextServices context;

    /**
     * Creates a new DocumentBean given the name of the file to read from.
     * 
     * @param fileName
     *            the name of the file to read from
     */
    public DocumentBean(String fileName)
      {
        document = new File(fileName);
      }

    /**
     * Called when this bean detects that a new service has been registered with
     * its context.
     * 
     * @param bcsae
     *            the BeanContextServiceAvailableEvent
     */
    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae)
      {
        System.out.println("[Detected a service being added to the context]");

        // Get a reference to the context
        BeanContextServices context = bcsae.getSourceAsBeanContextServices();
        System.out.println("Is the context offering a WordCount service? " + context.hasService(WordCount.class));

        // Use the service, if it's available
        if (context.hasService(WordCount.class)) {
            System.out.println("Attempting to use the service...");
            try {
                WordCount service = (WordCount) context.getService(this, this,
                    WordCount.class, document, this);
                System.out.println("Got the service!");
                service.countWords();
            } catch (Exception e) {
            }
        }
      }

    /**
     * Called when this bean detects that a service has been revoked from the
     * context.
     * 
     * @param bcsre
     *            the BeanContextServiceRevokedEvent
     */
    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre)
      {
        System.out.println("[Detected a service being revoked from the context]");
      }

}

/**
 * The WordCount service. Implementations of the countWords() method are
 * provided by the WordCountServiceProvider class.
 */
interface WordCount {

    /**
     * Counts the number of words in the file.
     */
    public abstract void countWords();

}

/**
 * This class is the factory that delivers a word counting service. The 3
 * methods defined in this class are the concrete implementations of the
 * BeanContextServiceProvider interface. For this demonstration, the primary
 * method of interest is getService(). The getService() methods returns a new
 * WordCount instance. It is called by the bean context when a nested JavaBean
 * requests the service.
 */
final class WordCountServiceProvider implements BeanContextServiceProvider {

    public Object getService(BeanContextServices bcs, Object requestor,
        Class serviceClass, Object serviceSelector)
      {

        // For this demo, we know that the cast from serviceSelector
        // to File will always work.
        final File document = (File) serviceSelector;

        /*
         * Return an instance of the service. The service itself is the
         * WordCount interface, which is implemented here using an anonymous
         * inner class.
         */
        return new WordCount() {

            public void countWords()
              {
                try {
                    // Create a Reader to the DocumentBean's File
                    BufferedReader br = new BufferedReader(new FileReader(
                        document));
                    String line = null;
                    int wordCount = 0;
                    while ((line = br.readLine()) != null) {
                        StringTokenizer st = new StringTokenizer(line);
                        while (st.hasMoreTokens()) {
                            System.out.println("Word " + (++wordCount) + " is: " + st.nextToken());
                        }
                    }
                    System.out.println("Total number of words in the document: " + wordCount);
                    System.out.println("[WordCount service brought to you by WordCountServiceProvider]");
                    br.close();
                } catch (Exception e) {
                }
              }

        };
      }

    public void releaseService(BeanContextServices bcs, Object requestor,
        Object service)
      {
        // do nothing
      }

    public Iterator getCurrentServiceSelectors(BeanContextServices bcs,
        Class serviceClass)
      {
        return null; // do nothing
      }

}//file: Test.txt
/*
This   text will  be analyzed  

by the WordCount 

service.


 */

