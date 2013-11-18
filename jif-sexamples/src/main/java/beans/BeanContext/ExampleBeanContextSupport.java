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
import java.beans.beancontext.BeanContextSupport;

/**
 * A test program that adds a bean to a beancontext, and reports on various
 * aspects of the context's membership state. This program also shows that a
 * bean's getBeanContext() method can be called to get a reference to its
 * enclosing context.
 */
public class ExampleBeanContextSupport {
  private static BeanContextSupport context = new BeanContextSupport(); // The
                                      // BeanContext

  private static BeanContextChildSupport bean = new BeanContextChildSupport(); // The
                                         // JavaBean

  public static void main(String[] args) {
    report();

    // Add the bean to the context
    System.out.println("Adding bean to context...");
    context.add(bean);

    report();
  }

  private static void report() {
    // Print out a report of the context's membership state.
    System.out.println("=============================================");

    // Is the context empty?
    System.out.println("Is the context empty? " + context.isEmpty());

    // Has the context been set for the child bean?
    boolean result = (bean.getBeanContext() != null);
    System.out.println("Does the bean have a context yet? " + result);

    // Number of children in the context
    System.out.println("Number of children in the context: "
        + context.size());

    // Is the specific bean a member of the context?
    System.out.println("Is the bean a member of the context? "
        + context.contains(bean));

    // Equality test
    if (bean.getBeanContext() != null) {
      boolean isEqual = (bean.getBeanContext() == context); // true means
                                  // both
                                  // references
                                  // point to
                                  // the same
                                  // object
      System.out.println("Contexts are the same? " + isEqual);
    }
    System.out.println("=============================================");
  }
}