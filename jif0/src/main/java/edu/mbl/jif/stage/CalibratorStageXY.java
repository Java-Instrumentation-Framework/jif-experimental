package edu.mbl.jif.stage;

import edu.mbl.jif.camacq.CamAcq;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.gui.dialog.DialogOkCancel;
import edu.mbl.jif.gui.util.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.zoom.ImagePanelZoomable;
import edu.mbl.jif.gui.imaging.PointGetter;
import edu.mbl.jif.gui.imaging.PointGetterCallback;
import edu.mbl.jif.gui.spatial.CoordinateTransformer;
import ij.IJ;
import ij.ImagePlus;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Point;
import java.awt.Window;
import java.awt.geom.Point2D;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class CalibratorStageXY implements PointGetterCallback {

  // for determining basklash
  public void doAcquireStagePositionSeries() {
    CamAcq camAcq = CamAcq.getInstance();
    if (camAcq == null) {
      return;
    }
    StageXYController stageCtrl = (StageXYController) camAcq.getController("stageXY");
    double xCenter = 8.0;
    double yCenter = 8.0;
    double delta = 20;

    Point2D[] positions = new Point2D.Double[]{
      new Point2D.Double(delta, 0),
      new Point2D.Double(-delta, 0),
      new Point2D.Double(delta, 0),
      new Point2D.Double(0, 0),
      new Point2D.Double(0, delta),
      new Point2D.Double(0, -delta),
      new Point2D.Double(0, delta),
      new Point2D.Double(0, 0)
    };
    int nPositions = 8;
    ImagePlus imgPlus = null;
    imgPlus = camAcq.newCaptureStackByte(nPositions);
    camAcq.setImagePlusForCapture(imgPlus); // sets default stack for capture
    camAcq.startAcq(true);
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
    }

    for (int i = 0; i < positions.length; ++i) {
      Point2D pos = positions[i];
      StringBuffer annot = new StringBuffer();
      annot.append("StagePosition (");
      annot.append(pos.getX());
      annot.append(", ");
      annot.append(pos.getY());
      annot.append(")");
      stageCtrl.moveRelative((int) pos.getX(), (int) pos.getY());
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
      }
      camAcq.captureImageToSlice(imgPlus, i, annot.toString());
    }
    camAcq.captureStackFinish();
    String stackTitle = imgPlus.getTitle();
    IJ.save(imgPlus, stackTitle + ".tif");
    camAcq.displayResume();
  }

  // for coordinate transform
  PointGetter pGet;
  DisplayLiveCamera display;
  ImagePanelZoomable iPane;
  // stage positions
  Point2D beginPoint;
  Point2D endPoint;
  int dxStage = 50, dyStage = 50;
  // screen positions
  Point firstPoint;
  Point secondPoint;
  int pointsGotten = 0;
  StageXYController stageCtrl;
  DialogOkCancel pointGetDialog;
  JPanel promptPanel = new JPanel();
  JLabel msgLabel = new JLabel();

  public void getPointsForCoord() {
    // assert displayOpen, stageXYisFunctional
    CamAcq camAcq = CamAcq.getInstance();
    if (camAcq == null) {
      return;
    }
    display = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
    if (display == null) {
      return;
    }
    // create PointGetter
    iPane = display.getImageDisplayPanel().getImagePane();
    pGet = new PointGetter(iPane);
    pGet.setCallback(this);
    // get stage position
    stageCtrl = (StageXYController) camAcq.getController("stageXY");
    if (stageCtrl != null) {
      // ++ take up slack
      beginPoint = stageCtrl.getPos();
      // prompt user to click on a point to track, near the center of the screen
      pointsGotten = 0;
      // control returns to #callbackFromPointGetter
      openPromptDialog();
      if (pointGetDialog.getReturnStatus() == DialogOkCancel.RET_CANCEL ||
              secondPoint == null) {
        iPane.restoreDefaultMouseInputAdapter();
        iPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        return;
      } else {
        finish();
      }
      transformer.showMatrix();
      System.out.println("toStage(firstPoint):" + transformer.toStage(firstPoint));
      System.out.println("toScreen(firstPoint):" + transformer.toScreen(firstPoint));
      double dXscreen = secondPoint.getX() - firstPoint.getX();
      double dYscreen = secondPoint.getY() - firstPoint.getY();
      Point2D p = new Point2D.Double(dXscreen, dYscreen);
      System.out.println("toStage(p):" + transformer.toStage(p));
      MovePointer.setTransformer(transformer);
    }
  }

  public void openPromptDialog() {
    StaticSwingUtils.dispatchToEDT(new Runnable() {

      public void run() {
        pGet.setupToPoint();
        // set modality of DisplayLive
        display.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        // open modal dialog
        pointGetDialog = new DialogOkCancel((Window) CamAcqJ.getInstance().getHostFrame(),
                "Screen/Stage Calibration",
                Dialog.ModalityType.APPLICATION_MODAL);
        promptPanel.add(msgLabel);
        msgLabel.setText("        Click on a point to track.                ");
        pointGetDialog.addPanel(promptPanel);
        pointGetDialog.enableOKButton(false);
        pointGetDialog.setLocation(200, 200);
        pointGetDialog.setVisible(true);
      }
    });
  }

  @Override
  public void callbackFromPointGetter(Point p) {
    if (pointsGotten == 0) {
      firstPoint = p;
      pointsGotten++;
      // Move stage (dxStage, dyStage),
      stageCtrl.moveRelativeWait(dxStage, dyStage, 500); // in units
      // get new stage position
      endPoint = stageCtrl.getPos();
      // prompt user to click on new position of tracking point, giving (dxScreen, dyScreen).
      msgLabel.setText("   Click on new position of the point to track.   ");
      pGet.setupToPoint();
    } else {
      secondPoint = p;
      pointGetDialog.enableOKButton(true);
      msgLabel.setText("Press OK to continue.");
    }
  }
  CoordinateTransformer transformer;

  public void finish() {
    // Calculate coordinate transform
    // make these relative (both starting points = (0,0)
    System.out.println("Stage begin: " + beginPoint);
    System.out.println("Stage end  : " + endPoint);
    System.out.println("firstpoint : " + firstPoint);
    System.out.println("secondpoint : " + secondPoint);
    double dXstage = endPoint.getX() - beginPoint.getX();
    double dYstage = endPoint.getY() - beginPoint.getY();
    double[][] toCoord = new double[][]{{0, 0}, {dXstage, dYstage}};
    double dXscreen = secondPoint.getX() - firstPoint.getX();
    double dYscreen = secondPoint.getY() - firstPoint.getY();
    double[][] fromCoord = new double[][]{{0, 0}, {dXscreen, dYscreen}};

    System.out.println("dScreen: " + dXscreen + ", " + dYscreen);
    System.out.println("dStage: " + dXstage + ", " + dYstage);

    transformer = CoordinateTransformer.createCoordinateTranformer(
            fromCoord, // screen
            toCoord); // stage


  }
}
