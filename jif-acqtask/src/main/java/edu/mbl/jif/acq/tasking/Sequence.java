/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.tasking;

import java.util.Date;

/**
 * A Sequence is a set of steps over time...
 * 
 * @author GBH
 */
public interface Sequence {

   Date getBeginTime();

   void setBeginTime(Date beginTime);

}
