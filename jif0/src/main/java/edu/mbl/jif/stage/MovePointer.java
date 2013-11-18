package edu.mbl.jif.stage;

import edu.mbl.jif.gui.imaging.zoom.ImagePanelZoomable;
import edu.mbl.jif.camacq.CamAcqJ;
import edu.mbl.jif.camacq.InstrumentController;
import edu.mbl.jif.camera.display.DisplayLiveCamera;
import edu.mbl.jif.gui.imaging.*;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import edu.mbl.jif.gui.spatial.CoordinateTransformer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.event.MouseInputAdapter;

/**
 * MovePointer - controls XY stage movement by moving to the clicked point on the DisplayLive 
 * Marks center point coordinate and shows vector to mouse position (the new point if clicked).
 * @author GBH
 */
public class MovePointer implements ImageViewerPlugin {

    ImagePanelZoomable iPane;
    private boolean showMovePointer = false;
    GraphicOverlay overlay;
    MouseListenerPointMarker mListenerPointMarker = new MouseListenerPointMarker();
    int centerX;
    int centerY;
    Point newPoint = new Point();
    StageXYController stageCtrl;
    private static CoordinateTransformer transformer;

    public MovePointer() {
        DisplayLiveCamera display = ((InstrumentController) CamAcqJ.getInstance().getController()).getDisplayLive();
        if (display == null) {
            return;
        }
        this.iPane = display.getImageDisplayPanel().getImagePane();
        centerX = iPane.getImageDisplayed().getWidth() / 2;
        centerY = iPane.getImageDisplayed().getHeight() / 2;
        overlay = new GraphicOverlay() {

            public void drawGraphicOverlay(ZoomGraphics zg) {
                if (isShowMovePointer()) {
                    int wid = 3;
                    zg.setColor(Color.yellow);
                    zg.setStroke(new BasicStroke(1.0f));
                    zg.drawRect(centerX, centerY, wid, wid);
                    zg.drawLine(centerX, centerY, newPoint.getX(), newPoint.getY());
                }
            }

        };
        iPane.addGraphicOverlay(getOverlay());
        stageCtrl = (StageXYController) ((InstrumentController) CamAcqJ.getInstance().getController()).getController(
            "stageXY");
    }

    private void showVector(MouseEvent e) {
        Point2D zc = iPane.toUserSpace(e.getX(), e.getY());
        int x = (int) (zc.getX());
        int y = (int) (zc.getY());
        newPoint.setLocation(x, y);
    }

    public static  void setTransformer(CoordinateTransformer _transformer) {
        transformer = _transformer;
    }

    public void toggleMovePointer(boolean on) {
        if(transformer == null) {
            return;
        }
        if (on) {
            iPane.setMouseInputAdapter(mListenerPointMarker);
            // for custom Cursor
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            URL url = getClass().getResource("icons/movePointCursor.gif");
            Image cursorImage = toolkit.getImage(url);
            Point cursorHotSpot = new Point(15, 15);
            Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
            iPane.setCursor(customCursor);
            setShowMovePointer(true);
        } else {
            setShowMovePointer(false);
            iPane.restoreDefaultMouseInputAdapter();
            iPane.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
        }
    }

    private void moveToPoint(MouseEvent e) {
        Point2D zc = iPane.toUserSpace(e.getX(), e.getY());
        int x = (int) (zc.getX());
        int y = (int) (zc.getY());
        Point p = new Point(x, y);
        System.out.println("Move to point: " + p);
        int dx = (centerX - x);
        int dy = (centerY - y);
        Point2D dPoint = transformer.toStage(new Point2D.Double(dx, dy));  // to delta in stage units
        int dXstage = (int)( dPoint.getX() / stageCtrl.getMultiplier());
        int dYstage = (int)( dPoint.getY() / stageCtrl.getMultiplier());
        stageCtrl.moveRelative(dXstage, dYstage);
        //stageCtrl.moveRelative(dx, dy);
    }

    public void setShowMovePointer(boolean t) {
        showMovePointer = t;
    }

    private boolean isShowMovePointer() {
        return showMovePointer;
    }

    class MouseListenerPointMarker
        extends MouseInputAdapter {

        public void mouseClicked(MouseEvent e) {
            moveToPoint(e);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
            showVector(e);
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

    }

    @Override
    public List<AbstractButton> getButtons() {
        List<AbstractButton> buttons = new ArrayList<AbstractButton>();
        // JToggleButton and JButton are both javax.swing.AbstractButton

        // Toggle button
        JToggleButton buttonPoint = new JToggleButton();
        buttonPoint.setMargin(new Insets(0, 0, 0, 0));
        buttonPoint.setMinimumSize(new Dimension(16, 16));
        buttonPoint.setToolTipText("Move XY Stage to point mode");
        try {
            buttonPoint.setIcon(
                new ImageIcon(MovePointer.class.getResource("icons/screenPoint.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        buttonPoint.addItemListener(new  
              ItemListener( ) {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                int state = itemEvent.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    toggleMovePointer(true);
                } else {
                    toggleMovePointer(false);
                }
            }
        });
        buttons.add(buttonPoint);
        return buttons;
    }

    @Override
    public GraphicOverlay getOverlay() {
        return this.overlay;
    }

}
