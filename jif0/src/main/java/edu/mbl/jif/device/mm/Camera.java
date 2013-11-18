/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.device.mm;

/**
 *
 * @author GBH
 */
public interface Camera
        extends Device {

    @Override
    DeviceType getType();

    static DeviceType Type = DeviceType.CameraDevice;

    // Camera API
    public int snapImage();

    public String getImageBuffer();

    public long getImageBufferSize();

    public int getImageWidth();

    public int getImageHeight();

    public int getImageBytesPerPixel();

    public int getBitDepth();

    public void setExposure(double exp_ms);

    public double getExposure();

    public int setROI(int x, int y, int xSize, int ySize);

    public int getROI(int x, int y, int xSize, int ySize);

    public int clearROI();

};
