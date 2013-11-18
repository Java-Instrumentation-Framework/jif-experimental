/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.acq.mode;

/**
 *
 * @author GBH
 */
public interface ImagingModeInterface {

    String getName();

    String getNameShort();

    String getIcon();

    void acquire();

    void acquireBkgd();

    void acquireBkgdScan();

    void beginScan();

    void endScan();

    void beginSeries();

    void endSeries();

    void enter();

    void exit();

    void initialize();

    void terminate();

    void process();  // to display during acq.

    void postProcess(); // after completion

    void load();

    void loadSettings();

    void saveSettings();

    void saveToFile();

    void saveToRawFile();

}
