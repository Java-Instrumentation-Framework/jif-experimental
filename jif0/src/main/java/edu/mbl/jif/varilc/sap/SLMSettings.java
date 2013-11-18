package edu.mbl.jif.varilc.sap;

import edu.mbl.jif.io.xml.ObjectStoreXML;
import ij.IJ;

/**
 *
 * @author GBH
 */
public class SLMSettings {

    // Calibrated Mask settings for max. extinction and transmission
    public double[] ellipticalExtinct;
    public double[] ellipticalTransmit;
    public double[] circularExtinct;
    public double[] circularTransmit;
    // Calibrated Retarder settings... 5 settings x 9 sectors array
    public double[][] retardSetA;
    public double[][] retardSetB;


    public SLMSettings() {
        ellipticalExtinct = new double[8];
        ellipticalTransmit = new double[8];
        circularExtinct = new double[2];
        circularTransmit = new double[2];
        retardSetA = new double[5][9];
        retardSetB = new double[5][9];
    }


    public void setToDummyValues() {

        ellipticalExtinct = new double[]{1650, 1650, 1650, 1650, 1650, 1650, 1650, 1650};
        ellipticalTransmit = new double[]{850, 850, 850, 850, 850, 850, 850, 850};
        circularExtinct = new double[]{1650, 1650};
        circularTransmit = new double[]{850, 850};
        retardSetA = new double[][]{
            {6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28},
            {6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28},
            {6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28},
            {6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28},
            {6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28, 6.28}};
        retardSetB = new double[][]{
            {3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416},
            {3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416},
            {3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416},
            {3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416},
            {3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416, 3.1416}};
    /*
    ellipticalExtinct = {2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008};
    ellipticalTransmit = {0, 0, 0, 0, 0, 0, 0, 0};
    circularExtinct = {2111, 2222};
    circularTransmit = {0, 0};
    retardSetA = {
    {2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019},
    {2022, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029},
    {2033, 2032, 2033, 2034, 2035, 2036, 2037, 2038, 2039},
    {2044, 2042, 2043, 2044, 2045, 2046, 2047, 2048, 2049},
    {2055, 2052, 2053, 2054, 2055, 2056, 2057, 2058, 2059}};
    retardSetB = {
    {3011, 3012, 3013, 3014, 3015, 3016, 3017, 3018, 3019},
    {3022, 3022, 3023, 3024, 3025, 3026, 3027, 3028, 3029},
    {3033, 3032, 3033, 3034, 3035, 3036, 3037, 3038, 3039},
    {3044, 3042, 3043, 3044, 3045, 3046, 3047, 3048, 3049},
    {3055, 3052, 3053, 3054, 3055, 3056, 3057, 3058, 3059}};
     */
    }


    public void showSettings() {
        IJ.log("\nSLM Settings >>>>>>>> ");
        IJ.log("  ellipticalExtinct: ");
        for (int i = 0; i < ellipticalExtinct.length; i++) {
            IJ.log(ellipticalExtinct[i] + "  ");
        }
        IJ.log("");

        IJ.log("  ellipticalTransmit: ");
        for (int i = 0; i < ellipticalTransmit.length; i++) {
            IJ.log(ellipticalTransmit[i] + "  ");
        }
        IJ.log("");

        IJ.log("  circularExtinct: ");
        for (int i = 0; i < circularExtinct.length; i++) {
            IJ.log(circularExtinct[i] + "  ");
        }
        IJ.log("");

        IJ.log("  circularTransmit: ");
        for (int i = 0; i < circularTransmit.length; i++) {
            IJ.log(circularTransmit[i] + "  ");
        }
        IJ.log("");

        IJ.log("  retardSetA: ");
        for (int r = 0; r < retardSetA.length; r++) {
            for (int c = 0; c < retardSetA[r].length; c++) {
                IJ.log("  " + retardSetA[r][c]);
            }
            IJ.log("");
        }

        IJ.log("  retardSetB: ");
        for (int r = 0; r < retardSetB.length; r++) {
            for (int c = 0; c < retardSetB[r].length; c++) {
                IJ.log("  " + retardSetB[r][c]);
            }
            IJ.log("");
        }
        IJ.log("<<<<<<<< SLM Settings");


    }


    void saveSettings(String filename) {
        try {
            ObjectStoreXML.write(this, filename);
        } catch (Throwable ex) {
            //Logger.getLogger(SLMController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SLMSettings settings = new SLMSettings();
        settings.setToDummyValues();
        settings.saveSettings("SLMSetsDefault.xml");

    }


}
