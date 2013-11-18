/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.sap;

import edu.mbl.jif.utils.FileUtil;

/**
 *
 * @author GBH
 */
public class RetardanceTable {

    // 9 sectors x 100 settings [0...4095] and their retardance values (double)
    // find the setting with closest value of retardance, 
    //   given a target retardance (extinction +/- swing)
    int nSettings = SLMModel.numPoints;
    int nSectors = 9;
    int[] setting = new int[nSettings];
    double[][] rets = new double[nSectors][nSettings];


    public RetardanceTable(String dataFile) {
        System.out.println("Retardance Table from: " + dataFile + "---------------------------------------------------");
        loadRetardanceCalibrationFromFile(dataFile);

    }

    // lookup setting for given retardance
    public int getSetting(int sector, double targetRetardance) {
        int settingToUse = 0;
        double expectedRet = 0;
        int set = 0;
        for (int i = 6; i < nSettings; i++) {
            double ret = rets[sector][i];
            if (ret < targetRetardance) {
                if (i == 0) {
                    System.err.println("Failed to find a setting for targetRetardance, target too HIGH.");
                    return 0;
                } else {
                    // which is the closest?
                    double diffDn = Math.abs(ret - targetRetardance);
                    double diffUp = Math.abs(rets[sector][i - 1] - targetRetardance);
                    //System.out.println(diffUp + " > < " + diffDn);
                    if (diffUp >= diffDn) {
                        // use lower setting
                        settingToUse = setting[i - 1];
                        expectedRet = rets[sector][i - 1];
                        break;
                    } else {
                        // use higher setting
                        settingToUse = setting[i];
                        expectedRet = rets[sector][i];
                        break;
                    }
                }
            }
        }
        System.err.println("Sector: " + sector + "  target: " + targetRetardance + ",  setting: " + settingToUse + " ... " + expectedRet);
        return settingToUse;
    }

    
    // lookup retardance for given setting
    public double getRetardance(int sector, int set) {
        double ret = 0;
        for (int i = 6; i < nSettings; i++) {
            if (set >= setting[i]) {
                ret = rets[sector][i];
                break;
            }
        }
        return ret;
    }


    public void loadRetardanceCalibrationFromFile(String dataFile) {
        String filename = FileUtil.getcwd() + dataFile;

        CSVFileToRead csv = new CSVFileToRead(dataFile);
        csv.readAllRows(setting, rets);
        for (int i = 0; i < setting.length; i++) {
            System.out.print("Set: " + setting[i] + ":  ");
            for (int j = 0; j < 9; j++) {
                System.out.print(rets[j][i] + "  ");
            }
            System.out.println();
        }
        csv.close();
    }


    public static void main(String[] args) {
        RetardanceTable retTableA = new RetardanceTable("RetACalib.csv");
        RetardanceTable retTableB = new RetardanceTable("RetBCalib.csv");
        retTableA.getSetting(0, 1.0);
        retTableA.getSetting(0, 2.3);
        retTableA.getSetting(0, 3.7);
        retTableA.getSetting(0, 9.9);
        retTableA.getSetting(3, 1.0);
        retTableA.getSetting(3, 2.3);
        retTableA.getSetting(3, 3.7);
        retTableA.getSetting(3, 9.9);
        retTableB.getSetting(0, 1.0);
        retTableB.getSetting(0, 2.3);
        retTableB.getSetting(0, 3.7);
        retTableB.getSetting(0, 9.9);
        retTableB.getSetting(3, 1.0);
        retTableB.getSetting(3, 2.3);
        retTableB.getSetting(3, 3.7);
        retTableB.getSetting(3, 9.9);
    }


}
