package edu.mbl.jif.camera;

import edu.mbl.jif.camera.display.DisplayImage16;
import edu.mbl.jif.camera.display.DisplayImage;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.acq.Acquisitioner_Lu;
import edu.mbl.jif.gui.test.FrameForTest;
import edu.mbl.jif.gui.imaging.FrameImageDisplay;
import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.imaging.stream.StreamGenerator;
import edu.mbl.jif.imaging.stream.StreamSource;
// TODO import edu.mbl.jif.script.jython.JythonConsole;
import edu.mbl.jif.utils.diag.edt.ThreadCheckingRepaintManager;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;


public class TestLuCam {
   public LuCamera cam1 = null;
   public LuCamera cam2 = null;
   float           expos = 100f;
   float           gain = 1.0f;
   float           exposAcq = 1f;
   JPanel          buttons = new JPanel();
   DisplayLiveCamera     display;

   // Video ---------------------------------------------------------------
   StreamingVideoRecorder vidRecA = null;
   float                  minGain = 1.0f;
   float                  maxGain = 20.0f;

   public TestLuCam() {
      //      Thread.UncaughtExceptionHandler handler =
      //            new StackWindow("Show Exception Stack", 400, 200);
      //      Thread.setDefaultUncaughtExceptionHandler(handler);
      FrameForTest f = new FrameForTest();
      buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

       // <editor-fold defaultstate="collapsed" desc=" Buttons/Actions ">
       addButton("Open1",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   openCamera1();
               }

           });
       addButton("Open2",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   openCamera2();
               }

           });
       addButton("Display ON",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   openDisplayA();
               }

           });
       addButton("DisplayTest",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   openDisplayZoom();
               }

           });
       addButton("Exp-",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   decreaseExposure();
               }

           });
       addButton("Exp+",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   increaseExposure();
               }

           });
       addButton("Gain-",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   decreaseGain();
               }

           });
       addButton("Gain+",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   increaseGain();
               }

           });
       addButton("Take8",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testByteImage();
               }

           });
       addButton("Take16",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testShortImage();
               }

           });
       addButton("Fast8",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testFastFrame8();
               }

           });
       addButton("Fast16",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testFastFrame16();
               }

           });
       addButton("acq8",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testAcq8();
               }

           });
       addButton("acq16",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testAcq16();
               }

           });
       addButton("RecStart",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   testRecordVideo();
               }

           });
       addButton("RecStop",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   stopRecordVideo();
               }

           });
       addButton("DisplayOFF",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   closeDisplayA();
               }

           });
       addButton("Close",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   closeCameras();
               }

           });
       addButton("ImageJ",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   edu.mbl.jif.imagej.IJMaker.openImageJ();
               }

           });
       addButton("Jython",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   openJythonConsole();
               }

           });
       addButton("Inspect",
           new ActionListener() {

               public void actionPerformed(ActionEvent e) {
                   inspectCam();
               }

           });
// </editor-fold>
      f.add(buttons);
      f.setLocation(50, 50);
      f.pack();
      f.setVisible(true);
      //
      RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager(true));

      System.out.println("Check for Cameras... ");
      LuCamJNI.loadDLL();
      int numCams = LuCamJNI.getNumCameras();
      System.out.println("Number of Lumenera cameras: " + numCams);
   }

   private void addButton(String label, ActionListener action) {
      Button b = new Button(label);
      if (action != null) {
         b.addActionListener(action);
      }
      buttons.add(b);
   }

   public void openCamera1() {
      // 1st camera...
      try {
         cam1 = new LuCamera(1);
         cam1.initialize();
         cam1.getDeviceType();
         cam1.getCameraState();
         expos    = (float) cam1.getExposureStream();
         gain     = (float) cam1.getGainStream();
         //  cam1.initSettings(8, 10.0f, 1.0f);
      } catch (Exception ex) {
         return;
      }
   }

   public void openCamera2() {
      // 1st camera....
      try {
         cam2 = new LuCamera(2);
         cam2.getDeviceType();
         cam2.setDepth(8);
         cam2.setExposureAcq(exposAcq);
         cam2.setGainAcq(gain);
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

   public void openDisplayZoom() {
      System.out.println("opening Display A");
      if (cam1 == null) {
         return;
      }
      
      //SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
               // Open the camera and get it's StreamSource
      //         display = new DisplayLive(cam1);
      Runnable runnable = new Runnable() {
           public void run() {
             cam1.startStream();
           }
       };
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
       //     }
       //  });
   }

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
      //vidRecA.finish();
   }

   //-------------------------------------------------------------------
   public void closeDisplayA() {
      cam1.closeDisplayA();
   }

   public void closeCameras() {
      if (cam1 != null) {
         cam1.close();
      }
      if (cam2 != null) {
         cam2.close();
      }
   }

   //      if (numCams > 1) {
   //         // 2nd camera....
   //         try {
   //            cam2 = new LuCamera(2);
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

   public void increaseGain() {
      if (gain < maxGain) {
         gain = gain + 0.1f;
         //System.out.println("expos: " + expos);
         cam1.setGainStream(gain);
      }
      System.out.println("");
   }

   public void decreaseGain() {
      if (gain > (minGain + 0.1f)) {
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
               // >>> takeSnapshot16b
               //            final byte[] imageArrayB = cam1.takeSnapshot16();
               //            if (imageArrayB != null) {
               //               System.out.println("imageArrayB.length = " + imageArrayB.length);
               //               for (int i = 0; i < 100; i++) {
               //                  System.out.println(imageArrayB[i]);
               //               }
               //               short[] imageArray16 = new short[imageArrayB.length / 2];
               //               System.out.println("imageArray16.length = " + imageArray16.length);
               //               int[] imageArrayInt = new int[imageArrayB.length / 2];
               //               for (int i = 0; i < imageArrayB.length; i+=2) {
               //                  byte a = imageArrayB[i];
               //                  byte b = imageArrayB[i+1];
               //                  int q = (((a & 0xff) << 8) | (b & 0xff));  // returns int
               //                  imageArrayInt[i/2] = q;
               //                  imageArray16[i/2] = (short) q;
               //               }
               //               for (int i = 0; i < 100; i++) {
               //                  System.out.println(imageArrayInt[i]);
               //               }
               final short[]  imageArray16 = cam1.takeSnapshot16();
               cam1.setDepth(8);
               DisplayImage16 ip16 = new DisplayImage16(640, 480);
               ip16.updateImage(imageArray16);
               JFrame ft16 = new JFrame();
               ft16.add(ip16);
               ft16.pack();
               ft16.setVisible(true);
            }
         });
   }

   /*
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
    */
   public void testFastFrame8() {
      if (cam1 != null) {
         int      n = 5;
         int      size = cam1.getWidth() * cam1.getHeight();
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
               ft.setTitle(String.valueOf(i + 1));
               ft.setLocation(10 + (5 * i), 10 + (5 * i));
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

   public void testFastFrame16() {
      if (cam1 != null) {
         cam1.getCameraState();
         int       n = 5;
         int       size = cam1.getWidth() * cam1.getHeight();
         short[][] imageArray = new short[n][];
         cam1.setDepth(16);
         cam1.setExposureAcq(exposAcq);
         cam1.setGainAcq(gain);
         cam1.enableFastAcq(16);
         for (int i = 0; i < 5; i++) {
            imageArray[i] = cam1.acqFast16();
            //int ret = cam1.acqFast16(imageArray[i]);
            //if (ret == 0) {
            System.out.println("imageArray.length = " + imageArray.length);
            DisplayImage16 ip = new DisplayImage16(640, 480);
            ip.updateImage(imageArray[i]);

            JFrame ft = new JFrame();
            ft.add(ip);
            ft.setTitle(String.valueOf(i + 1));
            ft.setLocation(10 + (5 * i), 10 + (5 * i));
            ft.pack();
            ft.setVisible(true);
            //} else {
            //   System.out.println("takeSnapshot16 failed.");
            //}
            System.out.println("Done acquiring: " + i);
         }
         cam1.disableFastAcq();
         cam1.setDepth(8);
      }
   }

      public void testAcq16() {
      // @todo - Gotta suspend the display for fast acq.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               try {
                  Acquisitioner_Lu acq = new Acquisitioner_Lu(cam1);
                  cam1.setExposureAcq(exposAcq);
                  cam1.setGainAcq(gain);
                  acq.setDepth(16);
                  acq.setMultiFrame(1, true);
                  acq.start();
                  short[] img = new short[cam1.getWidth() * cam1.getHeight()];
                  acq.acquireImage(img);
                  acq.finish();
                  cam1.setDepth(8);
                  BufferedImage         bImage =
                     ImageFactoryGrayScale.createImage(cam1.getWidth(), cam1.getHeight(), 16, img);
                  FrameImageDisplay f =
                     new FrameImageDisplay(bImage.getWidth(), bImage.getHeight(), bImage);
                  f.setVisible(true);
                  //DisplayImage16 ip = new DisplayImage16(640, 480);
                  //ip.updateImage(img);
                  //JFrame ft = new JFrame();
                  //ft.add(ip);
                  //ft.pack();
                  //ft.setVisible(true);
               } catch (Exception ex) {
                  ex.printStackTrace();
                  return;
               }
            }
         });
   }
   public void testAcq8() {
      // @todo - Gotta suspend the display for fast acq.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               try {
                  Acquisitioner_Lu acq = new Acquisitioner_Lu(cam1);
                  cam1.setDepth(8);
                  cam1.setExposureAcq(exposAcq);
                  cam1.setGainAcq(gain);
                  acq.setDepth(8);
                  acq.setMultiFrame(1, true);
                  acq.start();
                  byte[] img = new byte[cam1.getWidth() * cam1.getHeight()];
                  acq.acquireImage(img);
                  acq.finish();
                  BufferedImage         bImage =
                     ImageFactoryGrayScale.createImage(cam1.getWidth(), cam1.getHeight(), 8, img);
                  FrameImageDisplay f =
                     new FrameImageDisplay(bImage.getWidth(), bImage.getHeight(), bImage);
                  f.setVisible(true);
                  //DisplayImage16 ip = new DisplayImage16(640, 480);
                  //ip.updateImage(img);
                  //JFrame ft = new JFrame();
                  //ft.add(ip);
                  //ft.pack();
                  //ft.setVisible(true);
               } catch (Exception ex) {
                  ex.printStackTrace();
                  return;
               }
            }
         });
   }

   // Diags -------------------------------------------------------------
   public void inspectCam() {
      org.pf.joi.Inspector.inspect(cam1);
   }

   public void openJythonConsole() {
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
            /* (non-Javadoc)
             * @see java.lang.Runnable#run()
             */
            public void run() {
               /*
                * Instantiates a new Jython Console and shows it.
                * The newly created <code>JythonConsole</code will be centered
                * in the middle of the screen. (setPositionRelativeTo(null)
                */
               // TODO
//               JythonConsole console = new JythonConsole();
//               console.setLocationRelativeTo(null);
//               console.setVisible(true);
            }
         });
   }

   // Test Main....................................
   public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable()
            {
               public void run () {
      TestLuCam testlucam = new TestLuCam();

      // org.pf.joi.Inspector.inspect(testlucam);
               }
            });
   }
}
