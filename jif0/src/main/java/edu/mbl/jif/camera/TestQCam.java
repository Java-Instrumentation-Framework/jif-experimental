package edu.mbl.jif.camera;

import edu.mbl.jif.camera.display.DisplayImage16;
import edu.mbl.jif.camera.display.DisplayImage;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;
//import edu.mbl.jif.script.jython.JythonConsole;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;


public class TestQCam {
   
   public QCamera cam1 = null;
   public QCamera cam2 = null;
   double expos = 100f;
   double gain = 1.0f;
   
   float exposAcq = 20f;
   JPanel buttons = new JPanel();
   
   public TestQCam() {
   
      //      Thread.UncaughtExceptionHandler handler =
      //            new StackWindow("Show Exception Stack", 400, 200);
      //      Thread.setDefaultUncaughtExceptionHandler(handler);
   
      FrameForTest f = new FrameForTest();
      buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
   
      addButton("Open1",      new ActionListener() {
         public void actionPerformed(ActionEvent e) { openCamera1();     }}
      );
      addButton("Display ON", new ActionListener() {
         public void actionPerformed(ActionEvent e) {openDisplayA();    }}
      );
      addButton("DisplayTest", new ActionListener() {
         public void actionPerformed(ActionEvent e) {openDisplayZoom();    }}
      );
      addButton("Exp-",       new ActionListener() {
         public void actionPerformed(ActionEvent e) {decreaseExposure();}}
      );
      addButton("Exp+",       new ActionListener() {
         public void actionPerformed(ActionEvent e) {increaseExposure();}}
      );
      addButton("Gain-",       new ActionListener() {
         public void actionPerformed(ActionEvent e) {decreaseGain();}}
      );
      addButton("Gain+",       new ActionListener() {
         public void actionPerformed(ActionEvent e) {increaseGain();}}
      );
      addButton("Take8",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {testByteImage();   }}
      );
      addButton("Take16",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {testShortImage();   }}
      );
      addButton("Fast8",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {testFastFrame8();   }}
      );
      addButton("Fast16",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {testFastFrame16();   }}
      );
      addButton("acq1",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {testAcq1();   }}
      );
      addButton("RecStart",   new ActionListener() {
         public void actionPerformed(ActionEvent e) {testRecordVideo(); }}
      );
      addButton("RecStop",    new ActionListener() {
         public void actionPerformed(ActionEvent e) {stopRecordVideo(); }}
      );
      addButton("DisplayOFF", new ActionListener() {
         public void actionPerformed(ActionEvent e) {closeDisplayA();   }}
      );
      addButton("Close",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {closeCameras();    }}
      );
      addButton("Jython",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {openJythonConsole(); }}
      );
      addButton("Inspect",      new ActionListener() {
         public void actionPerformed(ActionEvent e) {inspectCam(); }}
      );
      f.add(buttons);
      f.setLocation(50,50);
      f.pack();
      f.setVisible(true);
      
      RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));
   

   }
   
   private void addButton(String label, ActionListener action) {
      Button b = new Button(label);
      if (action != null) b.addActionListener(action);
      buttons.add(b);
   }
   
   
   
   public void openCamera1() {
      // 1st camera...
      try {
         cam1 = new QCamera(1, 0);
         cam1.getCameraState();
         expos = cam1.getExposureStream();
         gain = cam1.getGainStream();
         //  cam1.initSettings(8, 10.0f, 1.0f);
      } catch (Exception ex) {
         return;
      }
   }
  
   
   
   public void openDisplayA() {
      System.out.println("opening Display A");
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            StreamSource s = cam1.getStreamSourceA();
            cam1.openDisplayA(s);
            cam1.startStreamA();
         }
      });
   }
   
   DisplayLiveCamera display;
   
   public void openDisplayZoom() {
      System.out.println("opening Display A");
      if(cam1 == null) return;
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            // Open the camera and get it's StreamSource
     //       display = new DisplayLive(cam1);
            cam1.startStream();
   //            ImageDisplayPanel viewPanel;
   //            StreamSource source;
   //            source = cam1.getStreamSource();
   //            if (source != null) {
   //               Dimension imageDim = new Dimension(cam1.getWidth(), cam1.getHeight());
   //               ImageDisplayContainer f = new ImageDisplayContainer();
   //               viewPanel = new ImageDisplayPanel(imageDim);
   //               viewPanel.setStreamingSource(source);
   //               f.addImageDisplayPanel(viewPanel);
   //               cam1.startStreamA();
   //            }
         }
      });
   }
   
   // Video ---------------------------------------------------------------
   StreamingVideoRecorder vidRecA = null;
   
   public void testRecordVideo() {
      startVideoRecorderA(cam1, "testVid");
   }
   
   public void stopRecordVideo() {
      stopVideoRecorderA();
   }
   
   public void startVideoRecorderA(StreamGenerator cam, String filename) {
//      StreamSource source = cam.getStreamSource();
//      cam.startStream();
//      vidRecA = new StreamingVideoRecorder(source, filename);
//      vidRecA.record();
   }
   
   public void stopVideoRecorderA() {
      vidRecA.finish();
   }
   
   
   
   //-------------------------------------------------------------------
   public void closeDisplayA() {
      cam1.closeDisplayA();
   }
   
   public void closeCameras() {
      if(cam1!=null)  cam1.close();
      if(cam2!=null)  cam2.close();
   }
   
   //      if (numCams > 1) {
   //         // 2nd camera....
   //         try {
   //            cam2 = new QCamera(2);
   //            cam2.getType();
   //            cam2.openDisplayB();
   //            testByteImage(cam2);
   //            //testShortImage(cam2);
   //            cam2.close();
   //         }
   //         catch (Exception ex) {
   //            return;
   //         }
   
   
   public void increaseExposure() {
      expos = expos + 1.0f;
      cam1.setExposureStream(expos);
      System.out.println("expos: " + expos);
   }
   
   public void decreaseExposure() {
      expos = expos - 1.0f;
      cam1.setExposureStream(expos);
      System.out.println("expos: " + expos);
   }
   
   float minGain= 1.0f;
   float maxGain = 20.0f;
   
   public void increaseGain() {
      if(gain < maxGain) {
         gain = gain + 0.1f;
         //System.out.println("expos: " + expos);
         cam1.setGainStream(gain);
      }
      System.out.println("");
   }
   
   public void decreaseGain() {
      if(gain > minGain + 0.1f) {
         gain = gain - 0.1f;
         cam1.setGainStream(gain);
      }
      System.out.println("");
   }
   
   
   // Acq ------------------------------------------------------------------
   public void testByteImage() {
      if (cam1 != null) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {

               cam1.setDepth(8);
               cam1.setExposureAcq(exposAcq);
               cam1.setGainAcq(gain);
   
               final byte[] imageArray = cam1.takeSnapshot8();
               if (imageArray != null) {
                  System.out.println("imageArray.length = " + imageArray.length);
   //            for (int i = 0; i < 100; i++) {
   //               System.out.println(imageArray[i]);
   //            }
                  DisplayImage ip = new DisplayImage(640, 480);
                  ip.updateImage(imageArray);
                  JFrame ft = new JFrame();
                  ft.add(ip);
                  ft.pack();
                  ft.setVisible(true);
               } else {
                  System.out.println("takeSnapshot8 failed.");
               }
            }
         });
      }
   }
   
   
   public void testShortImage() {
      // Test Short image
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            final short[] imageArray16 = cam1.takeSnapshot16();
            if (imageArray16 != null) {
               System.out.println("imageArray.length = " + imageArray16.length);
   //            for (int i = 0; i < 100; i++) {
   //               System.out.println(imageArray16[i]);
   //            }
               DisplayImage16 ip16 = new DisplayImage16(640, 480);
               ip16.updateImage(imageArray16);
               JFrame ft16 = new JFrame();
               ft16.add(ip16);
               ft16.pack();
               ft16.setVisible(true);
            } else {
               System.out.println("takeSnapshot16 failed.");
            }
   
         }
      });
   }
   
   
   public void testFastFrame8( ) {
      if (cam1 != null) {
         int n = 5;
         int size = cam1.getWidth() * cam1.getHeight();
         byte[][] imageArray = new byte[n][size];

         cam1.setDepth(8);
         cam1.setExposureAcq(exposAcq);
         cam1.setGainAcq(gain);
         cam1.enableFastAcq(8);
         for (int i = 0; i < 5; i++) {
            //imageArray = cam1.takeFastFrame();
            int ret = cam1.acqFast8(imageArray[i]);
            if (ret == 0) {
               System.out.println("imageArray.length = " + imageArray.length);
               DisplayImage ip = new DisplayImage(640, 480);
               ip.updateImage(imageArray[i]);
               JFrame ft = new JFrame();
               ft.add(ip);
               ft.setTitle(String.valueOf(i+1));
               ft.setLocation(10 + 5*i, 10 +5*i);
               ft.pack();
               ft.setVisible(true);
            } else {
               System.out.println("takeSnapshot8 failed.");
            }
            System.out.println("Done acquiring: " + i);
         }
         cam1.disableFastAcq();
      }
   }
   public void testFastFrame16( ) {
      if (cam1 != null) {
         cam1.getCameraState();
         int n = 5;
         int size = cam1.getWidth() * cam1.getHeight();
         short[][] imageArray = new short[n][size];
         cam1.setDepth(16);
         cam1.setExposureAcq(exposAcq);
         cam1.setGainAcq(gain);
         cam1.enableFastAcq(16);
         for (int i = 0; i < 5; i++) {
            //imageArray = cam1.takeFastFrame();
            int ret = cam1.acqFast16(imageArray[i]);
            if (ret == 0) {
               System.out.println("imageArray.length = " + imageArray.length);
               DisplayImage16 ip = new DisplayImage16(640, 480);
               ip.updateImage(imageArray[i]);
               JFrame ft = new JFrame();
               ft.add(ip);
               ft.setTitle(String.valueOf(i+1));
               ft.pack();
               ft.setVisible(true);
            } else {
               System.out.println("takeSnapshot16 failed.");
            }
            System.out.println("Done acquiring: " + i);
         }
         cam1.disableFastAcq();
      }
   }
   
   public void testAcq1() {
//      javax.swing.SwingUtilities.invokeLater(new Runnable() {
//         public void run() {
//            try {
//              // Acquisitioner acq = new Acquisitioner(cam1);
//               cam1.setDepth(8);
//               cam1.setExposureAcq(exposAcq);
//               cam1.setGainAcq(gain);
//               acq.setMultiFrame(4,true);
//               acq.start();
//               byte[] img = new byte[cam1.getWidth() * cam1.getHeight()];
//               acq.acquireImage(img);
//               acq.finish();
//               DisplayImage ip = new DisplayImage(640, 480);
//               ip.updateImage(img);
//               JFrame ft = new JFrame();
//               ft.add(ip);
//               ft.pack();
//               ft.setVisible(true);
//            } catch (Exception ex) {
//               ex.printStackTrace();
//               return;
//            }
//         }
//      });
   }
   
   
   // Diags -------------------------------------------------------------
   public void inspectCam() {
      org.pf.joi.Inspector.inspect(cam1);
   }
   
   public void openJythonConsole() {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
   
         public void run() {
   
            // TODO 
//            JythonConsole console = new JythonConsole();
//            console.setLocationRelativeTo(null);
//            console.setVisible(true);
         }
      });
   }
   
   
   // Test Main....................................
   
   public static void main(String[] args) {
   //      SwingUtilities.invokeLater(new Runnable()
   //      {
   //         public void run () {
      TestQCam testQCam = new TestQCam();
      // org.pf.joi.Inspector.inspect(testQCam);
   //         }
   //      });
   
   }
}
