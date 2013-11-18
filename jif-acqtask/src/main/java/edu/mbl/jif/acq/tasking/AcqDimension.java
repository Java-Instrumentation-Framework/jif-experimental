/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking;

/**
 *
 * @author GBH
 */
class AcqDimension {
   private String name;
   private int iterations;

   public AcqDimension(String name, int iterations) {
      this.name = name;
      this.iterations = iterations;
   }

   public String getName() {
      return name;
   }

   public int getIterations() {
      return iterations;
   }
   
   
}
