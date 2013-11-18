package edu.mbl.jif.ps.calc;

import edu.mbl.jif.imaging.stats.ImageAnalyzer;
import edu.mbl.jif.imaging.stats.ImageStatistics;
import edu.mbl.jif.ps.PolStack;
import edu.mbl.jif.utils.prefs.Prefs;

import java.awt.Dimension;
import java.awt.Rectangle;

public class Ratioing {
    
   public static boolean doCorrection = false;
   //public static Rectangle roi = new Rectangle(0, 0, 0, 0);
   public static Rectangle acqRoi = new Rectangle(0, 0, 0, 0);
   public static float zeroValue = 0.0f;

   private Ratioing () {
   }


   // sets ratioing correction ON
   //  public static void setOn() {
   //    doCorrection = true;
   //  }
   // sets ratioing correction OFF
   //  public static void setOff() {
   //    doCorrection = false;
   //  }
   // check to see if ratioing correction is ON
   //  public static boolean isSet() {
   //    return doCorrection;
   //  }
   ///////////////////////////////////////////////////////////////////////
   // calcRatioingFactors(PolStack, BkgdStack)
   //
   static void calcRatioingFactors (PolStack pStack, PolStack bkgdStack) {
      Dimension dim = new Dimension(pStack.width, pStack.height);
      zeroValue = Math.round(pStack.psParms.zeroIntensity);
      // assigns values to PolStackParm
      pStack.psParms.ratioingAvg = new double[pStack.numSlices];
      //pStack.psParms.roiRatioing.setBounds(roi.getBounds());
      Rectangle roi = pStack.psParms.roiRatioing;
      //
      for (int k = 0; k < pStack.numSlices; k++) {
         if (k == 0) {
            pStack.psParms.ratioingAvg[k] =
                  getRatio(k, pStack.slice0, bkgdStack.slice0, dim, roi);
         }
         if (k == 1) {
            pStack.psParms.ratioingAvg[k] =
                  getRatio(k, pStack.slice1, bkgdStack.slice1, dim, roi);
         }
         if (k == 2) {
            pStack.psParms.ratioingAvg[k] =
                  getRatio(k, pStack.slice2, bkgdStack.slice2, dim, roi);
         }
         if (k == 3) {
            pStack.psParms.ratioingAvg[k] =
                  getRatio(k, pStack.slice3, bkgdStack.slice3, dim, roi);
         }
         if (k == 4) {
            pStack.psParms.ratioingAvg[k] =
                  getRatio(k, pStack.slice4, bkgdStack.slice4, dim, roi);
         }
      }
   }


   static double getRatio (int k, Object sampleSlice, Object bkgdSlice,
                           Dimension dim, Rectangle _roi) {
      double sampleAvg = 0;
      double bkgdAvg = 0;
      ImageStatistics imgStats;
      imgStats = ImageAnalyzer.getStats(sampleSlice, _roi, dim);
      sampleAvg = imgStats.meanInROI;
      imgStats = ImageAnalyzer.getStats(bkgdSlice, _roi, dim);
      bkgdAvg = imgStats.meanInROI;
      return (bkgdAvg - zeroValue) / (sampleAvg - zeroValue);
   }


   //----------------------------------------------------------------
   // Rationing ROI's
   //
   //----------------------------------------------------------------
   //
   public static void setAcqRoi (Rectangle roi) {
      acqRoi = roi;
      Prefs.usr.putInt("acqRatioRoi_X", (int) acqRoi.getX());
      Prefs.usr.putInt("acqRatioRoi_Y", (int) acqRoi.getY());
      Prefs.usr.putInt("acqRatioRoi_W", (int) acqRoi.getWidth());
      Prefs.usr.putInt("acqRatioRoi_H", (int) acqRoi.getHeight());
   }


   public static Rectangle retrieveAcqRoi () {
      acqRoi =
            new Rectangle(Prefs.usr.getInt("acqRatioRoi_X", 0),
                          Prefs.usr.getInt("acqRatioRoi_Y", 0),
                          Prefs.usr.getInt("acqRatioRoi_W", 0),
                          Prefs.usr.getInt("acqRatioRoi_H", 0));
      return acqRoi;
	 }
}

			
			

   //----------------------------------------------------------------
   //
   //-----------------------------------------------------------
   //
//  public static void clearROI() {
//    setROI(new Rectangle(0, 0, 0, 0));
//    //setOff();
//  }
//
//  public static void setROI(Rectangle _roi) {
//    roi = _roi;
//  }
//
//  public static boolean isRoiSet() {
//    return ((roi.width > 0) && (roi.height > 0));
//  }

   /////////////////////////////////////////////////////////////////////
   // getRatioingAverages
   //
   //  public static void getRatioingAverages(
   //      PolStack8 pStack, PolStack8 bkgdStack) {
   //    if (Ratioing.roi.width > 0) {
   //      pStack.psParms.ratioingAvg = new double[pStack.numSlices];
   //      pStack.psParms.roiRatioing.setBounds(Ratioing.roi.getBounds());
   //      pStack.psParms.zeroIntensity = getZeroValue();
   //      // Ratioing.calcRoiAverages(pStack, Ratioing.roi.getBounds(),
   //      // pStack.psParms.ratioingAvg);
   //      calcRatioingFactors(pStack, bkgdStack);
   //    }
   //  }
   /////////////////////////////////////////////////////////////////////
   // calcRoiAverages
   //
   //  static void calcRoiAverages(PolStack8 pStack, Rectangle roi,
   //      double[] ratioAvg) {
   //    double roiAvg = 0;
   // average the pixel values in the roi for each background slice
   //      for (int i = 0; i < ratioAvg.length; i++) {
   //         if (i == 0) {
   //            roiAvg =
   //               Averager.averageROIin(pStack.slice0, roi,
   //                  new Dimension(pStack.width, pStack.height));
   //         }
   //         if (i == 1) {
   //            roiAvg =
   //               Averager.averageROIin(pStack.slice1, roi,
   //                  new Dimension(pStack.width, pStack.height));
   //         }
   //         if (i == 2) {
   //            roiAvg =
   //               Averager.averageROIin(pStack.slice2, roi,
   //                  new Dimension(pStack.width, pStack.height));
   //         }
   //         if (i == 3) {
   //            roiAvg =
   //               Averager.averageROIin(pStack.slice3, roi,
   //                  new Dimension(pStack.width, pStack.height));
   //         }
   //         if (i == 4) {
   //            roiAvg =
   //               Averager.averageROIin(pStack.slice4, roi,
   //                  new Dimension(pStack.width, pStack.height));
   //         }
   //         if (roiAvg > -1) {
   //            ratioAvg[i] = roiAvg;
   //         } else {
   //            ratioAvg[i] = 0;
   //         }
   //      }
   //  }

