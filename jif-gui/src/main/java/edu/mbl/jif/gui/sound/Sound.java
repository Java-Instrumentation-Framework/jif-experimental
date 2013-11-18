package edu.mbl.jif.gui.sound;

import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Sound {
    public Sound() {
    }
    /////////////////////////////////////////////////////////////////////////
    // Sound
    //
    public static void beep() {
        //if (Prefs.usr.getBoolean("soundFeedback", true)) {
        SoundClip clickSoundClip = new SoundClip("ding.wav");
        clickSoundClip.play();
//      } else {
//         Toolkit.getDefaultToolkit().beep();
//      }
    }
    
    
    //----------------------------------------------------------------
    // Sounds
    //
    public static void soundAllDone() {
        playSoundFile("longGong.au");
    }
    
    public static void soundMistake() {
        playSoundFile("wee.au");
    }
    public static void soundBell() {
        playSoundFile("bell.au");
    }
    public static void soundDone() {
        playSoundFile("ding.wav");
    }
    
    
    public static void soundClick() {
        playSoundFile("click4.au");
    }
    
    
    public static void playSoundFile(final String soundFile) {
        //if (Prefs.usr.getBoolean("soundFeedback", true)) {
        
        dispatchToEDT(new Runnable() {
            public void run() {
                try {
                    SoundClip clickSoundClip = new SoundClip(soundFile);
                    clickSoundClip.play();
                } catch (Exception ex) {
                }
            }
        });
//        final SoundClip clickSoundClip = new SoundClip(soundFile);
//        new Thread(new Runnable() {
//            public void run() {
//                clickSoundClip.play();
//            }
//        }).start();
        //}
        
    }
    
    public static void dispatchToEDT(Runnable runnable) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(runnable);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            runnable.run();
        }
    }
    
    
    
    public static void main(String[] args) {
//        Sound.soundMistake();
//        JifUtils.waitFor(1000);
//        Sound.soundBell();
//        JifUtils.waitFor(1000);
//        Sound.soundClick();
//        JifUtils.waitFor(1000);
//        Sound.soundDone();       
//        JifUtils.waitFor(1000);
//        Sound.soundAllDone();       
//        JifUtils.waitFor(1000);
//
//        Sound.beep();
    }
}
