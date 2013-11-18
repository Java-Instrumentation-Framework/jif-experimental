/*
 * Swing_timer.java
 *
 * Created on January 29, 2007, 8:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tests.timing;

import com.holub.asynch.Condition;
import java.util.*;
import java.awt.event.*;

// import com.holub.asynch.Condition;

public class Swing_timer
{
    private Condition done          = new Condition(false);
    private int       elapsed_time  = 0;

    // Create a one-second timer:

    javax.swing.Timer clock = 
        new javax.swing.Timer
        (   1000,
            new ActionListener()
            {   public void actionPerformed( ActionEvent e )
                {   synchronized( Swing_timer.this )
                    {   System.out.println( (new Date()).toString() );
                        if( ++elapsed_time == 5 )
                            done.set_true();        // Notify other threads
                                                    // that timer has finished.
                    }
                }
            }
        );

    Swing_timer() throws InterruptedException
    {   clock.start();
        done.wait_for_true(); // wait for timer to finish
    }

    public static void main(String[] args) throws InterruptedException
    {   new Swing_timer();
    }
}
