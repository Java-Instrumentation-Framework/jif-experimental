/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.acq;

/**
 *
 * @author GBH
 */
public class PostProcessorTest implements PostProcessor {

    @Override
    public void run()
      {
          System.out.println("Running post-processor");
      }

}
