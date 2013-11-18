/*
 * SundialApp.java
 *
 * Created on September 17, 2007, 6:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


/**
 * Sun Compass/Clock: Plots a sun compass/clock for a specified latitude.
 *
 * Copyright (C) 2005 Christopher Molloy. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * @author <a href="http://christophermolloy.com/">Chris Molloy</a>
 * @version 2.0.1
 */
public class SundialApp extends JFrame {
    public static final double C_DECLINATION = 23.45d;
    private JFrame cSelf;
    private GUIPane cPane;
    private PageFormat mPageFormat;
    private Calendar cCalendar;
    private double cLatitude;
    private double cLongitude;
    private String[] cMonths = {
            "January", "February", "March", "April", "May", "June", "July", "August", "September",
            "October", "November", "December"
        };
    private String[] cMonthShort = {
            "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC", "JAN", "FEB", "MAR", "APR", "MAY"
        };
    private int[] cTMTAdjustment = {
            3, 6, 8, 10, 11, 13, 14, 14, 14, 14, 14, 13, 12, 11, 10, 9, 7, 6, 4, 3, 1, 0, -1, -2, -3,
            -3, -4, -4, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 6, 6, 6, 6, 5, 5, 4, 2, 1, -1, -2, -4,
            -6, -7, -9, -11, -12, -14, -15, -16, -16, -16, -16, -16, -15, -14, -12, -11, -9, -6, -4,
            -2, 1, 3
        };

    private SundialApp() {
        super("Sun Compass/Clock");
        cSelf = this;

        PrinterJob pj = PrinterJob.getPrinterJob();
        mPageFormat = pj.defaultPage();
        cCalendar = new GregorianCalendar();
        cLatitude = -41.2833d; // Wellington, NZ

        JMenuBar lMenuBar = new JMenuBar();
        JMenu lFile = new JMenu("File", true);
        lFile.add(new FilePrintAction())
             .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        lFile.add(new FilePageSetupAction())
             .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                Event.CTRL_MASK | Event.SHIFT_MASK));
        lMenuBar.add(lFile);

        JMenu lMenuDisplay = new JMenu("Display");
        lMenuDisplay.add(new DisplayDateAction())
                    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
        lMenuDisplay.add(new DisplayLatitudeAction())
                    .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
        lMenuBar.add(lMenuDisplay);
        this.setJMenuBar(lMenuBar);

        cPane = new GUIPane();
        setContentPane(cPane);

        addWindowListener(new WindowClose());
        setSize(400, 600);
        setVisible(true);
    }

    public static void main(String[] args) {
        setDefaultLookAndFeelDecorated(true);
        new SundialApp();
    }

    private class GUIPane extends JPanel {
        GUIPane() {
            super();
        }

        public void paint(Graphics _G) {
            int mWidth;
            int mHeight;
            int mW2;
            int mH2;
            int mGap;
            int mG2;
            int mRadius;
            int mShift;
            double mAngle;
            double mX1;
            double mY1;
            double mX2;
            double mY2;
            double mStart;
            double mDecRad;
            Arc2D.Double mDec;
            Dimension mPanelSize;
            Graphics2D mG2D;
            FontRenderContext mFontRenderContext;
            Rectangle2D mBounds;
            AffineTransform mTransform;
            AffineTransform mRotate;

            mG2D = (Graphics2D) _G;
            mPanelSize = getSize();
            mWidth = mPanelSize.width;
            mHeight = mPanelSize.height;
            mW2 = mWidth / 2;
            mH2 = mHeight / 2;
            mTransform = AffineTransform.getTranslateInstance(mW2, mH2);
            mG2D.transform(mTransform);

            mGap = Math.min(mWidth, mHeight) / 40;
            mG2 = mGap * 2;
            mRadius = (Math.min(mWidth, mHeight) - mG2) / 2;
            mFontRenderContext = mG2D.getFontRenderContext();

            // Clear canvas
            mG2D.setPaint(Color.white);

            //mG2D.clearRect(-mW2, -mH2, mWidth, mHeight);
            Rectangle2D.Double lClip = new Rectangle2D.Double(-mW2, -mH2, mWidth, mHeight);
            mG2D.clip(lClip);
            mG2D.fill(lClip);
            mG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            mG2D.setFont(new Font("SansSerif", Font.PLAIN, 12));

            // Draw compass lines
            mDecRad = (double) Math.max(mWidth, mHeight) * 2.0d;

            Color lC1 = new Color(223, 223, 223);
            Color lC2 = new Color(239, 239, 239);
            for (int ii = 1; ii < 72; ii++) {
                mAngle = (Math.PI / 36.0) * (double) ii;
                mX1 = Math.cos(mAngle) * mDecRad;
                mY1 = Math.sin(mAngle) * mDecRad;
                if ((ii % 2) == 0) {
                    mG2D.setPaint(lC1);
                } else {
                    mG2D.setPaint(lC2);
                }
                mG2D.drawLine(0, 0, (int) mX1, (int) mY1);
            }
            mG2D.setPaint(Color.white);
            mG2D.fill(new Ellipse2D.Double(-mRadius, -mRadius, mRadius * 2, mRadius * 2));

            // Draw copyright
            mG2D.setPaint(Color.lightGray);

            String lCopy = new String("Copyright Christopher Molloy 2005. All rights reserved.");
            mBounds = mG2D.getFont().getStringBounds(lCopy, mFontRenderContext);
            mG2D.drawString(lCopy, -(int) mBounds.getWidth() / 2,
                -mH2 + ((int) mBounds.getHeight() * 2));
            lCopy = new String("www.christophermolloy.com ? outdoor living ?");
            mBounds = mG2D.getFont().getStringBounds(lCopy, mFontRenderContext);
            mG2D.drawString(lCopy, -(int) mBounds.getWidth() / 2,
                -mH2 + ((int) mBounds.getHeight() * 3));
            lCopy = new String("hiking & camping ? sun compass/clock");
            mBounds = mG2D.getFont().getStringBounds(lCopy, mFontRenderContext);
            mG2D.drawString(lCopy, -(int) mBounds.getWidth() / 2,
                -mH2 + ((int) mBounds.getHeight() * 4));

            // Draw NS parallels
            for (int ii = 1; ii < 48; ii++) {
                mAngle = (Math.PI / 48.0) * (double) ii;
                mX1 = Math.cos(mAngle) * (double) mRadius;
                mY1 = Math.sin(mAngle) * (double) mRadius;

                int kk = 6;
                if ((ii % 4) == 0) {
                    mG2D.setPaint(Color.darkGray);
                    kk = ii / 4;
                    mY2 = -mY1;
                } else {
                    mG2D.setPaint(Color.lightGray);
                    mAngle = (cLatitude / 180.0d) * Math.PI;
                    mY2 = mY1 * -Math.sin(mAngle);
                    if (cLatitude > 0.0d) {
                        mY1 = -mY1;
                    }
                }
                mG2D.drawLine((int) mX1, (int) mY1, (int) mX1, (int) mY2);
                if (kk != 6) {
                    String lHour = new String(Integer.toString(18 - kk));
                    mBounds = mG2D.getFont().getStringBounds(lHour, mFontRenderContext);
                    mShift = (cLatitude < 0.0d) ? (int) (mY1 + mBounds.getHeight()) : (-(int) mY1 -
                        4);
                    mG2D.drawString(lHour, (int) mX1 - ((kk > 6) ? (int) mBounds.getWidth() : 0),
                        mShift);
                }
            }

            String lHour = new String("NOON");
            mBounds = mG2D.getFont().getStringBounds(lHour, mFontRenderContext);
            mShift = (cLatitude < 0.0d) ? (mRadius + (int) mBounds.getHeight()) : (-mRadius - 4);
            mG2D.setPaint(Color.darkGray);
            mG2D.drawString(lHour, -(int) mBounds.getWidth() / 2, mShift);

            // Draw EW parallel and latitude arc
            mAngle = (cLatitude / 180.0d) * Math.PI;
            mY1 = -Math.sin(mAngle) * (double) mRadius;
            // mG2D.setPaint(Color.lightGray);
            // mG2D.drawLine(-mW2, (int)mY1, mW2, (int)mY1);
            mG2D.setPaint(Color.black);
            mStart = 330.0d;
            if (cLatitude < 0.0d) {
                mStart = mStart - 180.0d;
            } else {
                mY1 = -mY1 - 1.0d;
            }
            mDec = new Arc2D.Double(-(double) mRadius, -mY1, (double) mRadius * 2.0d, mY1 * 2.0d,
                    mStart, 240.0d, Arc2D.OPEN);

            // Draw declination circle
            mAngle = (C_DECLINATION / 180.0d) * Math.PI;
            mDecRad = Math.abs(Math.sin(mAngle) * (double) mRadius);

            Stroke store = mG2D.getStroke();
            Stroke months = new BasicStroke(mGap, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
            mG2D.setStroke(months);
            mG2D.setPaint(Color.lightGray);
            for (double ii = 0.0d; ii < 360.0; ii += 60.0d) {
                mG2D.draw(new Arc2D.Double(-(int) mDecRad, -(int) mDecRad, (int) (mDecRad * 2.0d),
                        (int) (mDecRad * 2.0d), ii + 22.5d, 30.0d, Arc2D.OPEN));
            }

            mG2D.setStroke(store);
            mG2D.setPaint(Color.white);
            mG2D.fill(new Ellipse2D.Double(-(int) mDecRad, -(int) mDecRad, (int) (mDecRad * 2.0d),
                    (int) (mDecRad * 2.0d)));
            mG2D.setPaint(Color.black);
            mG2D.draw(new Ellipse2D.Double(-(int) mDecRad, -(int) mDecRad, (int) (mDecRad * 2.0d),
                    (int) (mDecRad * 2.0d)));

            for (int ii = 1; ii < 48; ii++) {
                mAngle = (Math.PI / 24.0) * (double) ii;
                mX1 = Math.cos(mAngle) * (mDecRad + (double) (((ii % 4) == 1) ? mGap : (mGap / 2)));
                mY1 = Math.sin(mAngle) * (mDecRad + (double) (((ii % 4) == 1) ? mGap : (mGap / 2)));
                mX2 = Math.cos(mAngle) * mDecRad;
                mY2 = Math.sin(mAngle) * mDecRad;
                mG2D.drawLine((int) mX2, (int) mY2, (int) mX1, (int) mY1);
            }

            // Draw gnomon line and circle
            mAngle = ((70.0d * Math.PI) / 48.0d) -
                (((double) cCalendar.get(Calendar.DAY_OF_YEAR) / 365.25d) * 2.0d * Math.PI);
            mX1 = Math.cos(mAngle) * mDecRad;
            mY1 = Math.sin(mAngle) * mDecRad;
            mG2D.setPaint(Color.lightGray);
            mG2D.drawLine((int) mX1, -(int) mY1, 0, -(int) mY1);
            mG2D.setPaint(Color.black);
            mG2D.draw(new Ellipse2D.Double(-mGap, -(int) mY1 - mGap, mG2, mG2));

            // Draw month names
            mG2D.setPaint(Color.darkGray);
            mTransform = mG2D.getTransform(); // store original co-ordinate space
            for (int ii = 0; ii < 12; ii++) {
                mRotate = AffineTransform.getRotateInstance((((double) ii * Math.PI) / 6.0d) -
                        (Math.PI / 24.0d));
                mG2D.transform(mRotate);
                mBounds = mG2D.getFont().getStringBounds(cMonthShort[ii], mFontRenderContext);
                mG2D.drawString(cMonthShort[ii], -(int) (mBounds.getWidth() / 2.0d),
                    (int) mBounds.getHeight() - (int) mDecRad);
                mG2D.setTransform(mTransform); // reset original co-ordinate space	
            }

            // Draw axes
            mG2D.setPaint(Color.black);
            mG2D.draw(new Ellipse2D.Double(-mRadius, -mRadius, mRadius * 2, mRadius * 2));
            mG2D.drawLine(-mW2, 0, mW2, 0);
            mG2D.drawLine(0, -mH2, 0, mH2);
            mG2D.drawLine(-mGap, mGap - mH2, 0, -mH2);
            mG2D.drawLine(0, -mH2, mGap, mGap - mH2);
            mG2D.drawLine(-mGap, mG2 - mH2, 0, mGap - mH2);
            mG2D.drawLine(0, mGap - mH2, mGap, mG2 - mH2);
            mG2D.draw(mDec);

            // Draw text
            mG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

            String lGnomon = new String("Gnomon date shown: " +
                    cCalendar.get(Calendar.DAY_OF_MONTH) + " " +
                    cMonths[cCalendar.get(Calendar.MONTH)]);
            mBounds = mG2D.getFont().getStringBounds(lGnomon, mFontRenderContext);
            mG2D.drawString(lGnomon, mGap - mW2, mH2 - mGap - (int) mBounds.getHeight());

            NumberFormat lNF = NumberFormat.getInstance();
            DecimalFormat lDF = (DecimalFormat) lNF;
            lDF.applyPattern("0.0000");

            String lLat = new String("Latitude shown: " + lDF.format(Math.abs(cLatitude)) +
                    ((cLatitude < 0.0d) ? " S" : " N"));
            mBounds = mG2D.getFont().getStringBounds(lLat, mFontRenderContext);
            mG2D.drawString(lLat, mW2 - (int) mBounds.getWidth() - mGap, mH2 - mGap);

            cCalendar.set(Calendar.HOUR_OF_DAY, 12);
            cCalendar.set(Calendar.MINUTE, 0);

            int lOffset = (cCalendar.get(Calendar.DAY_OF_YEAR) - 1) % 5;
            int lBase = (cCalendar.get(Calendar.DAY_OF_YEAR) - 1) / 5;
            if (lOffset == 0) {
                cCalendar.add(Calendar.MINUTE, cTMTAdjustment[lBase % 73]);
            } else {
                cCalendar.add(Calendar.MINUTE,
                    cTMTAdjustment[lBase % 73] +
                    (int) ((double) (cTMTAdjustment[(lBase + 1) % 73] - cTMTAdjustment[lBase % 73]) * ((double) lOffset / 5.0d)));
            }

            String lTMT = new String("0" + cCalendar.get(Calendar.MINUTE));
            lTMT = new String("Time of Meridian Transit: " + cCalendar.get(Calendar.HOUR_OF_DAY) +
                    ":" + lTMT.substring(lTMT.length() - 2));
            mG2D.drawString(lTMT, mGap - mW2, mH2 - mGap);

            mG2D.setPaint(Color.black);
            mG2D.draw(lClip);
        }
    }

    private class WindowClose extends WindowAdapter {
        public void windowClosing(WindowEvent _WindowEvent) {
            System.exit(0);
        }
    }

    private class DisplayDateAction extends AbstractAction {
        public DisplayDateAction() {
            super("Gnomon Date...");
        }

        public void actionPerformed(ActionEvent _ActionEvent) {
            String d = new String("0" + cCalendar.get(Calendar.DAY_OF_MONTH));
            String m = new String("0" + (cCalendar.get(Calendar.MONTH) + 1));
            String s = (String) JOptionPane.showInputDialog(cSelf,
                    "Enter the gnomon date (dd/mm):", "Gnomon Date", JOptionPane.QUESTION_MESSAGE,
                    null, null, d.substring(d.length() - 2) + "/" + m.substring(m.length() - 2));
            if ((s != null) && (s.length() > 0)) {
                try {
                    String[] lDateBits = s.split("/");
                    int lDay = Integer.parseInt(lDateBits[0]);
                    int lMonth = Integer.parseInt(lDateBits[1]);
                    if ((lDay > 0) && (lDay < 32) && (lMonth > 0) && (lMonth < 13)) {
                        cCalendar.set(Calendar.DAY_OF_MONTH, lDay);
                        cCalendar.set(Calendar.MONTH, lMonth - 1);
                        cPane.repaint();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private class DisplayLatitudeAction extends AbstractAction {
        public DisplayLatitudeAction() {
            super("Latitude...");
        }

        public void actionPerformed(ActionEvent _ActionEvent) {
            String s = (String) JOptionPane.showInputDialog(cSelf,
                    "Enter your latitude (decimal, +ve if North, -ve if South):", "Latitude",
                    JOptionPane.QUESTION_MESSAGE, null, null, "" + cLatitude);
            if ((s != null) && (s.length() > 0)) {
                try {
                    double lLat = Double.parseDouble(s);
                    if ((lLat <= 90.0d) && (lLat >= -90.0d)) {
                        cLatitude = lLat;
                        cPane.repaint();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public class FilePrintAction extends AbstractAction {
        public FilePrintAction() {
            super("Print");
        }

        public void actionPerformed(ActionEvent _ActionEvent) {
            PrinterJob pj = PrinterJob.getPrinterJob();

            //ComponentPrintable cp = new ComponentPrintable(getContentPane());
            //pj.setPrintable(cp, mPageFormat);
            if (pj.printDialog()) {
                try {
                    pj.print();
                } catch (PrinterException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public class FilePageSetupAction extends AbstractAction {
        public FilePageSetupAction() {
            super("Page Setup...");
        }

        public void actionPerformed(ActionEvent _ActionEvent) {
            PrinterJob pj = PrinterJob.getPrinterJob();
            mPageFormat = pj.pageDialog(mPageFormat);
        }
    }
}
