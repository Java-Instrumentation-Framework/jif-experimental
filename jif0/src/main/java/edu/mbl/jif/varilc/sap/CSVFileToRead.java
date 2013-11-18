/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.sap;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class CSVFileToRead {

    DataRow row;
    DataFile read;


    /** Creates a new instance of TextFile */
    public CSVFileToRead(String filepath) {
        try {
            File file = new File(filepath);
            read = DataFileFactory.createReader("8859_1");
            read.setDataFormat(new CSVFormat());
            // first line is column header
            read.containsHeader(false);
            read.open(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void readAllRows(int[] setting, double[][] rets) {
        try {
            int n = 0;
            for (row = read.next(); row != null; row = read.next()) {
                setting[n] = row.getInt(1);
                //System.out.println("seting: " + setting[n]);
                for (int i = 0; i < 9; i++) {
                    rets[i][n] = row.getDouble(i + 2);
                }
                n++;
            }
        } catch (IOException ex) {
            Logger.getLogger(CSVFileToRead.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }


    public void close() {
        try {
            read.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        CSVFileToRead csv = new CSVFileToRead("RetACalib.csv");
        int[] setting = new int[100];
        double[][] rets = new double[9][100];
        //Object[] items = new Object[]{n, x, s};
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


}
