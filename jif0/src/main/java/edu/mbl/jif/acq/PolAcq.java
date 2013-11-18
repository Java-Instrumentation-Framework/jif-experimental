package edu.mbl.jif.acq;

import edu.mbl.jif.camacq.CamAcq;
import ij.ImagePlus;
import ij.gui.NewImage;
import edu.mbl.jif.varilc.camacq.VariLC_RT;

/**
 * jif.PolStack Acquisition
 */
public class PolAcq {
    // PolScope Stack Type

    public static final int TWO_FRAME = 2;
    public static final int THREE_FRAME = 3;
    public static final int FOUR_FRAME = 4;
    public static final int FIVE_FRAME = 5;
    int psStackType = FIVE_FRAME;
    int[] lcSet = new int[5]; // VariLC settings for each frame
    long LC_SettleTime = 50; //msec.
    int numberSlices = 5;
    CamAcq camAcq;
    VariLC_RT vlc;

    public PolAcq(CamAcq camAcq, VariLC_RT vlc) {
        this.camAcq = camAcq;
        this.vlc = vlc;
    }

    public void setStackType(int type) {
        psStackType = type;
        // set all to undefined
        lcSet[0] = -1;
        lcSet[1] = -1;
        lcSet[2] = -1;
        lcSet[3] = -1;
        lcSet[4] = -1;
        switch (psStackType) {
            case TWO_FRAME:
                numberSlices = 2;
                lcSet[0] = 1;
                lcSet[1] = 4;
                /** @todo check this - should it be 1-3 or 2-4 */
                break;
            case THREE_FRAME:
                numberSlices = 3;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                break;
            case FOUR_FRAME:
                numberSlices = 4;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                lcSet[3] = 3;
                break;
            case FIVE_FRAME:
                numberSlices = 5;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                lcSet[3] = 3;
                lcSet[4] = 4;
                break;
        }
    }
/////////////////////////////////////////////////////////////////////////////
// Acquire a PolStack of psStackType
//-------------------

    public synchronized ImagePlus acquirePolStack(String stackID, int exposures) {
        long acqW = camAcq.getWidth();
        long acqH = camAcq.getHeight();
        //   exposeTime = camAcq.getExposure() / 1000; // convert exposure to millisecs

        // +++++ LC_SettleTime = PSj.variLC.getSettlingTime();
        System.out.println("Acquiring PolStack...");
        // switch LC to first setting and wait for it to settle
        // ++++++++++ variLC.selectElement(lcSet[0]);
        // Setup
        camAcq.setMultiFrame(2);
        camAcq.startAcq();

        // instantiate ImagePlus for PolStack
        ImagePlus pStk = createStack(5, 8);

        try {
            Thread.currentThread().sleep(LC_SettleTime);
        } catch (InterruptedException e) {
        }

        // Acquire the slices
        if (numberSlices > 0) {
            acquirePolFrame(pStk.getStack().getPixels(1), 0);
        }
        if (numberSlices > 1) {
            acquirePolFrame(pStk.getStack().getPixels(2), 1);
        }
        if (numberSlices > 2) {
            acquirePolFrame(pStk.getStack().getPixels(3), 2);
        }
        if (numberSlices > 3) {
            acquirePolFrame(pStk.getStack().getPixels(4), 3);
        }
        if (numberSlices > 4) {
            acquirePolFrame(pStk.getStack().getPixels(5), 4);
        }
        // +++++++++++ PSj.variLC.selectElement(1);
        camAcq.finishAcq();
        System.out.println("PolStack acquired.");
        return pStk;
    }

    ImagePlus createStack(int numSlices, int depth) {
        ImagePlus iPlus;
        try {
            // Initialize the ImageJ ImagePlus stack & allocate image memory
            if (depth == 8) { // result = byte []
                iPlus = NewImage.createByteImage("Title", camAcq.getWidth(),
                        camAcq.getHeight(), numSlices, 0);
            } else {
                iPlus = NewImage.createShortImage("Title", camAcq.getWidth(),
                        camAcq.getHeight(), numSlices, 0);
            }
        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError in captureSeriesToStack");
            return null;
        }
        if (iPlus == null) {
            System.err.println("imp == null");
            return null;
        }
//      Stack stack = iPlus.getStack();
//      if (stack.getSize() == 0) {
//         error("stack size = 0");
//         return null;
//      }
        return iPlus;
    }
//////////////////////////////////////////////////////////////////////////
// acquirePolFrame
// Acquires a frame and sets-up the VariLC for the next PolStack slice

    synchronized void acquirePolFrame(final Object imageArray, int slice) {
        long timeSetLC = 0;
        camAcq.acquireImage(imageArray);
        // Command to VariLC to set to next element
        if ((slice + 1) < numberSlices) {
            // +++++ PSj.variLC.selectElement(lcSet[slice + 1]);
        }
    }

    /*  synchronized void acquirePolFrame (ImageGetter _iGet, final Object imageArray,
    int slice) {
    long timeSetLC = 0;
    if (camAcq.acquireFrames(_iGet)) {
    // Command to VariLC to set to next element
    if ((slice + 1) < numberSlices) {
    // ++++++ PSj.variLC.selectElement(lcSet[slice + 1]);
    timeSetLC = edu.mbl.jif.utils.time.TimerHR.currentTimeMillis();
    } while (!_iGet.isProcessDone()) { // wait for ProcessDone...
    try {
    Thread.currentThread().sleep(1);
    }
    catch (InterruptedException e) {}
    }
    try {
    _iGet.putImageInto(imageArray);
    }
    catch (Exception ex) {}
    long wait = LC_SettleTime - (edu.mbl.jif.utils.time.TimerHR.currentTimeMillis()
    - timeSetLC);
    if (wait > 0) {
    try {
    Thread.currentThread().sleep(LC_SettleTime);
    }
    catch (InterruptedException e) {}
    }
    }
    }
     */
}
