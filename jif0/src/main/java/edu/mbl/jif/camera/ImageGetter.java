package edu.mbl.jif.camera;

import com.holub.asynch.Condition;

// GBH: this is the version of ImageGetter from moved here from PSj Nov.1.05
//
// ImageGetter is the object that is 'called-back' by the camera (QCamAPI)
// for discrete image acquisition.
//
// It enables multiFrame integration, with or without averaging (division)
// and puts the resulting image array into a destination array (byte[] or short[])
//
// Necessary to re-instanciate this on change of:
//    Bit-depth (8-bit or 12-bit)
//    Image size
//         ROI changed
//         Binning changed
//    Frames to average/integrate
//    Average or Integrate
//
public class ImageGetter {

  int depth;
  int width, height;
  int size = 0;
  boolean mirrorImage = false;
  int multiFrame = 1;
  boolean div = true;
  byte[] buffByte = null;
  short[] buffShort = null;
  short[] sumShort = null;
  int[] sumInt = null;
  //boolean frameDone = false;
  //boolean processDone = false;
  Condition frameDone = new Condition(false);
  Condition processDone = new Condition(false);
  String acqTime = "0";

  public ImageGetter(int depth, int width, int height,
          int multiFrame, boolean divide, boolean mirror) {
    this(depth, width, height);
    setMultiFrame(multiFrame, divide);
    setMirrorImage(mirror);
  }

  public ImageGetter(int depth, int width, int height) {
    this.depth = depth;
    this.width = width;
    this.height = height;
    // create buffer array
    size = width * height;
    if (depth == 8) {
      buffByte = new byte[size];
    } else {
      buffShort = new short[size];
    }
  }

  public void setMultiFrame(int _multiFrame, boolean _divide) {
    div = _divide;
    multiFrame = _multiFrame;
    if (multiFrame > 1) {
      // if frame-averaging, create summation array
      if (depth == 8) {
        sumShort = new short[size];
      } else {
        sumInt = new int[size];
      }
    }
  }

  public int getNumFrames() {
    return multiFrame;
  }

  public void setMirrorImage(boolean b) {
    mirrorImage = b;
  }

  //----------------------------------------------------------------
  // acquireImage
  //
  //  public synchronized void acquireImage(Object destImage) {
  //    // +++ remember trigger state... CameraInterface.
  //    int frmsAvg = Prefs.usr.getInt("framesToAvg", 1);
  //
  //    for (int i = 0; i < frmsAvg; i++) {
  //      CameraInterface.triggerSoft();
  //
  //      // wait for FrameDone
  //      while (!isFrameDone()) {
  //        try {
  //          Thread.sleep(5);
  //        } catch (InterruptedException e) {}
  //      }
  //      resetFrameDone();
  //    }
  //    try {
  //      putImageInto(destImage);
  //    } catch (Exception ex) {}
  //  }
  //----------------------------------------------------------------

  // frameDone is used as a flag
  synchronized void setFrameDone() {
    frameDone.set_true();
  }

  public synchronized void resetFrameDone() {
    frameDone.set_false();
  }

  public synchronized boolean isFrameDone() {
    return frameDone.is_true();
  }

  synchronized void setProcessDone() {
    processDone.set_true();
  }

  public synchronized void resetProcessDone() {
    processDone.set_false();
  }

  public synchronized boolean isProcessDone() {
    return processDone.is_true();
  }

  public String getAcqTimeString() {
    return acqTime;
  }


  //================================================================
  //----------------------------------------------------------------
  // Callback & process...
  //
  public synchronized void callBack() {
    // Callback method for acquisition (not display)
    // This method is called-back to from the camera when frame is done
    // (meaning the image data is in the pixels array.)
    //acqTime = TimerHR.getAsString();
    //TimerHR.mark("callBack, frameDone");
    setFrameDone();
    if (depth == 8) {
      processFrame(QCamJNI.pixels8);
    } else {
      processFrame(QCamJNI.pixels16);
    }
    setProcessDone();
  //TimerHR.mark("callBack, processDone");
  }

  public void processFrame(byte[] img) {
    synchronized (img) {
      System.arraycopy(img, 0, buffByte, 0, size);
    }
    if (multiFrame > 1) {
      for (int i = 0; i < size; i++) {
        sumShort[i] = (short) (sumShort[i] + (buffByte[i] & 0xff));
      }
    }
  }

  public void processFrame(short[] img) {
    synchronized (img) {
      System.arraycopy(img, 0, buffShort, 0, size);
    }
    if (multiFrame > 1) {
      for (int i = 0; i < size; i++) {
        sumInt[i] = (sumInt[i] + buffShort[i]); // & 0xffff;
      }
    }
  }


  //----------------------------------------------------------------
  // putImageInto
  // Puts the acquired image into the destination image (array)
  // with or without mirroring (flip horiz) the image
  public void putImageInto(Object destination) throws Exception {
    int frmsToAvg = multiFrame;
    int off = 0;
    //TimerHR.mark("putImageInto, start");
    // 8-bit Acquisition
    if (depth == 8) {
      if (destination instanceof byte[]) {
        if (frmsToAvg == 1) {
          // 8-bit, single --> byte
          if (mirrorImage) {
            for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
              for (int y = 0; y < height; y++) {
                off = y * (int) width;
                ((byte[]) destination)[x2 + off] = buffByte[x1 + off];
              }
            }
          } else {
            System.arraycopy(buffByte, 0, (byte[]) destination, 0,
                    size);
          }
        } else {
          // 8-bit, multiple --> byte
          if (mirrorImage) {
            for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
              for (int y = 0; y < height; y++) {
                off = y * (int) width;
                if (div) {
                  ((byte[]) destination)[x2 + off] = (byte) ((int) (sumShort[x1 + off]) / frmsToAvg);
                } else {
                  ((byte[]) destination)[x2 + off] = (byte) (sumShort[x1 + off]);
                }
                sumShort[x1 + off] = 0;
              }
            }
          } else { // not mirrorImage
            for (int i = 0; i < size; i++) {
              if (div) {
                ((byte[]) destination)[i] = (byte) ((int) (sumShort[i]) / frmsToAvg);
              } else {
                ((byte[]) destination)[i] = (byte) (sumShort[i]);
              }
              sumShort[i] = 0;
            }
          }
        }
      } else { // destination is short[]
        if (frmsToAvg == 1) {
          // 8-bit, single --> short
          if (mirrorImage) {
            for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
              for (int y = 0; y < height; y++) {
                off = y * (int) width;

                ((short[]) destination)[x2 + off] = (short) (buffByte[x1 + off] & 0xff);
              }
            }
          } else {
            for (int i = 0; i < size; i++) {
              ((short[]) destination)[i] = (short) (buffByte[i] & 0xff);
            }
          }
        } else {
          // 8-bit, multiple --> short
          if (mirrorImage) {
            for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
              for (int y = 0; y < height; y++) {
                off = y * (int) width;

                ((short[]) destination)[x2 + off] = (short) (buffByte[x1 + off] & 0xff);
              }
            }
          } else {
            for (int i = 0; i < size; i++) {
              if (div) {
                ((short[]) destination)[i] = (short) (sumShort[i] / frmsToAvg);
              } else {
                ((short[]) destination)[i] = sumShort[i];
              //System.arraycopy(sumShort, 0, (short[]) destination, 0, size);
              }
              sumShort[i] = 0;
            }
          }
        }
      }
    } else { // 12-bit Acquisition
      // 12-bit, single --> short)
      if (frmsToAvg == 1) {
        if (mirrorImage) {
          for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
            for (int y = 0; y < height; y++) {
              off = y * (int) width;

              ((short[]) destination)[x2 + off] = (short) (buffShort[x1 + off] & 0xffff);
            }
          }
        } else {
          for (int i = 0; i < size; i++) {
            ((short[]) destination)[i] = (short) (buffShort[i] & 0xffff);
          //System.arraycopy(buffShort, 0, (short[]) destination, 0, size);
          }
        }
      } else {
        // 12-bit, multiple --> short
        if (mirrorImage) {
          for (int x1 = 0, x2 = (int) width - 1; x1 < width; x1++, x2--) {
            for (int y = 0; y < height; y++) {
              off = y * (int) width;
              if (div) {
                ((short[]) destination)[x2 + off] = (short) (sumInt[x1 + off] / frmsToAvg);
              } else {
                ((short[]) destination)[x2 + off] = (short) (sumInt[x1 + off]);
              }
              sumInt[x1 + off] = 0;
            }
          }
        } else {
          for (int i = 0; i < size; i++) {
            if (div) {
              ((short[]) destination)[i] = (short) (sumInt[i] / frmsToAvg);
            } else {
              ((short[]) destination)[i] = (short) (sumInt[i]);
            }
            sumInt[i] = 0;
          }
        }
      }
    }
  //TimerHR.mark("putImageInto, done");


  }


  //-----------------------------------------------------------
  // flush the summation buffer
  public void flush() {
    if (sumShort != null) {
      for (int i = 0; i < size; i++) {
        sumShort[i] = 0;
      }
    }
    if (sumInt != null) {
      for (int i = 0; i < size; i++) {
        sumInt[i] = 0;
      }
    }
  }


  //================================================================
  public void nullOutArrays() {
    buffByte = null;
    buffShort = null;
    sumShort = null;
    sumInt = null;
  }

  public static void main(String[] args) {
    int n = 64;
    short result;
    int sumInt = 0;
    short s1 = 2048;
    for (int i = 0; i < n; i++) {
      sumInt = (sumInt + s1); //& 0xffff;
    }
    System.out.println("sumInt=" + sumInt);
    result = (short) (sumInt / n);
    System.out.println("result=" + result);


  }
}
