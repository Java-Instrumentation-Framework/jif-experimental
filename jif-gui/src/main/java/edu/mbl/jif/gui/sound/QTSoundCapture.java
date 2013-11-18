package edu.mbl.jif.gui.sound;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


import java.awt.*;
import java.awt.event.*;

//import quicktime.*;
//import quicktime.util.QTUtils;
//import quicktime.io.*;
//
//import quicktime.std.StdQTConstants;
//import quicktime.std.sg.*;
//import quicktime.std.movies.*;

public class QTSoundCapture extends Frame{
//implements StdQTConstants, Runnable, Errors {

//   public static void main (String args[]) {
//      QTSoundCapture sr = new QTSoundCapture ("Sound Recording");
//      sr.show();
//      sr.toFront();
//
//   }

//
//   void createSGObject() throws QTException {
//         mGrabber = new SequenceGrabber();
//         mAudio = new SGSoundChannel (mGrabber);
//
//         SGDeviceList dl = mAudio.getDeviceList (0);
//         SGDeviceName name = dl.getDeviceName (0);
//
//         System.out.println (dl + "" +  name);
//         System.out.println (mAudio.getInputDriver());
//
//         mAudio.setUsage (seqGrabRecord);
//         t = new Thread (this,"SoundRecord Idle Method");
//   }
//
//
//   QTSoundCapture (String title) {
//      super (title);
//
//      try {
//         QTSession.open();
//            System.out.println ("Make sure you have connected an Input device or have a built in audio input device.");
//         createSGObject();
//
//      } catch (Exception ee) {
//         ee.printStackTrace();
//         QTSession.close();
//      }
//
//      setLayout(new GridLayout(1, 3, 2, 2));
//      add (startButton);
//      add (settingsButton);
//
//      startButton.addActionListener (new ActionListener () {
//         public void actionPerformed (ActionEvent event) {
//            try{
//
//               createMovie();
//
//               if (mGrabber == null)
//                  createSGObject();
//
//               mGrabber.setDataOutput (recordFile, seqGrabToDisk);
//               mGrabber.prepare(true, true);
//               mGrabber.startRecord();
//               t.start();
//               recording = true;
//               recordNotice = new Frame("Recording");
//
//               recordNotice.setLayout(new GridLayout(1, 3, 2, 2));
//               recordNotice.add (stopButton);
//
//         stopButton.addActionListener (new ActionListener () {
//            public void actionPerformed (ActionEvent event) {
//               if (recording) {
//                  try {
//                     tStop = true;
//                     mGrabber.stop();	//stop record
//                     recordNotice.hide();
//                     recordNotice = null;
//                     recording = false;
//
//                     mGrabber.disposeChannel (mAudio);
//                     mAudio = null;
//
//                     mGrabber.release();
//                     mGrabber = null;
//                     showMovie();
//                  } catch (QTException ee) {
//                     ee.printStackTrace();
//                   }
//               }
//            }
//         });
//
//               recordNotice.setBounds (40, 40, 200, 100);
//               recordNotice.show();
//               recordNotice.toFront();
//            } catch (QTException ee){
//               ee.printStackTrace();
//            }
//         }
//      });
//      settingsButton.addActionListener (new ActionListener () {
//         public void actionPerformed (ActionEvent event) {
//            try{
//               if (mAudio == null) {
//                  createSGObject();
//               }
//
//               if (recording == false)
//                  mAudio.settingsDialog ();
//            } catch (QTException ee){
//               ee.printStackTrace();
//            }
//         }
//      });
//
////		t = new Thread (this);
//
//      addNotify();
//      Insets insets = getInsets();
//      setBounds (0, 0, (insets.left + insets.right + 220), (insets.top + insets.bottom + 16));
//      addWindowListener (new WindowAdapter () {
//         public void windowClosing (WindowEvent e) {
//            try {
//             if (mGrabber != null)
//               mGrabber.stop();	//stop preview
//
//               if (recording)
//                  tStop = true;
//
//               if (mAudio != null)
//                  mGrabber.disposeChannel (mAudio);
//
//            } catch (QTException ee) {
//               ee.printStackTrace();
//            }
//            QTSession.close();
//            dispose();
//         }
//
//         public void windowClosed (WindowEvent e) {
//            System.exit(0);
//         }
//      });
//   }
//
//   private SGSoundChannel mAudio;
//   private Button startButton = new Button("Record");
//   private Button stopButton  = new Button("Stop");
//   private Button settingsButton = new Button("Settings");
//   private SequenceGrabber mGrabber;
//   private Thread t;
//   private boolean recording = false;
//   private QTFile recordFile;
//   private Frame recordNotice;
//   private boolean tStop = false;
//
//   void createMovie () throws QTException {
//   // We need to run the GC here to clean up any objects that
//   // have been allocated. The Play movie frame does
//   // not deallocate QT objects within that frame until the
//   // frame is disposed of completely
//      QTUtils.reclaimMemory();
//      FileDialog fd = new FileDialog (this, "Save Movie As...", FileDialog.SAVE);
//      fd.show();
//      if(fd.getFile() == null)
//         throw new QTIOException (userCanceledErr, "");
//      recordFile = new QTFile(fd.getDirectory() + fd.getFile());
//   }
//
//   void showMovie () {
////      PlayMovie pm = new PlayMovie(recordFile);
////      pm.show();
////      pm.toFront();
//   }
//
//   public void run () {
//      try {
//         for (;;) {
//            mGrabber.idle();
//            Thread.sleep(20);
//                if (tStop == true)
//                    break;
//         }
//      } catch (Exception e) {
//          e.printStackTrace();
//       }
//   }
}
