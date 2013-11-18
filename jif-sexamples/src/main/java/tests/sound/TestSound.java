// Build a silly au file and play it

/* copyright (c) 1998-2001
 * Roedy Green
 * Canadian Mind Products
 * #327 - 964 Heywood Avenue
 * Victoria, BC Canada V8V 2Y5
 * tel: (250) 361-9093
 * mailto:roedy@mindprod.com
 * http://mindprod.com
 */

// version 1.2 2001 March 26 - add documentation
// Version 1.1 1998 November 10 - add name and address.
// Version 1.0 1997 December 5

package tests.sound;

import java.io.ByteArrayInputStream;

import sun.audio.AudioPlayer;

public class TestSound
   {
   public static void main(String[] args)
      {
      try
         {  // make 4 seconds of sound
         int length = 4;

         // 8000 samples per second
         short[] pcm_linear = new short[8000*length];

         for ( int i=0; i<pcm_linear.length; i++ )
            {
            //  more interesting noise
             pcm_linear[i] = (short)(16000*Math.sin((double)(i*i)/10000.0 * (2*3.1416)));

            // bump up the volume so range between -16,000 .. +16,000
            // make a sine wave of 120 Hz.
            // Since you are only sampling at 8000 samples per second,
            // can't expect this to work above 4000 Hz.
            // You will get a stroboscopic beat effect.
            //pcm_linear[i] = (short)(16000*Math.sin(i*(120. *  2 * Math.PI / 8000.)));
            }
         System.out.println("Creating U_Law");
         Mu_Law ulaw = new Mu_Law(" Whoop, whoop, whoop", pcm_linear);

         byte[] newb = ulaw.toBytes();

         System.out.println("Playing U_Law");
         ByteArrayInputStream bis = new ByteArrayInputStream(newb);

         AudioPlayer.player.start(bis);
         Thread.sleep(length*1000);

         }
      catch ( Exception e )
         {
         e.printStackTrace();
         }
      } // end main
   } // end TestSound
