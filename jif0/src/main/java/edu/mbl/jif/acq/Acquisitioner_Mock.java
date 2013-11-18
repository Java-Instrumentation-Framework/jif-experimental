/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq;

import edu.mbl.jif.camera.CameraInterface;

/**
 *
 * @author GBH
 */
public class Acquisitioner_Mock implements ImageAcquisitioner {

    CameraInterface cam;

    public Acquisitioner_Mock(CameraInterface cam)
      {
        this.cam = cam;
      }

    @Override
    public void setDepth(int depth)
      {
      }

    @Override
    public void setMirrorImage(boolean t)
      {
      }

    @Override
    public void setMultiFrame(int n, boolean divide)
      {
      }

    @Override
    public void start()
      {
      }

    @Override
    public void start(boolean flushFirst)
      {
      }

    @Override
    public long acquireImage(Object imageArray)
      {
        cam.acqFast8((byte[])imageArray);
        return 0;
      }

    @Override
    public void finish()
      {
      }
}

