/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.varilc.sap;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;
import edu.mbl.jif.io.csv.CSVFileWrite;
import edu.mbl.jif.utils.FileUtil;
import info.monitorenter.util.Range;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GBH
 */
public class RetDataAnalyzer {

    DataSet[] dataSets;
    int numIDs = 9;
    double zero = 0;


    public RetDataAnalyzer() {
    }


    public void go() {
        createDataSets(numIDs);
        readInMatrix();
        plotRaw();
        //normalize(dataSets);
        //generate();
    }


    private void readInMatrix() {
        // Write out raw data as matrix
        //CSVFileRead dataFile;
        DataFile read = null;
        DataRow row;
        String filename = FileUtil.getcwd() + "/SLMDatafeb1/" + "Raw" + "-Matrix-" + "retarderA" + ".csv";
        System.out.println("filename " + filename);
        try {
            read = DataFileFactory.createReader("8859_1");
            read.setDataFormat(new CSVFormat());
            // first line is column header
            read.containsHeader(false);
            read.open(new File(filename));

            //row = read.next();
            row = read.next();
            for (int j = 0; j < SLMModel.numPoints; j++) {
                //for (row = read.next(); row != null; row = read.next()) {
                row = read.next();
                int point = row.getInt(0);
                double setting = row.getDouble(1);
                for (int i = 0; i < numIDs; i++) {
                    //System.out.println("j: " + j + " i: " + i + "  rowSize: " + row.size());
                    dataSets[i].addPoint(point, setting,
                            row.getDouble(2 + i) - zero);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (read != null) {
            try {
                read.close();
            } catch (IOException ex) {
                Logger.getLogger(RetDataAnalyzer.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }


    private void normalize(DataSet[] dataSets) {
        double[] max = new double[numIDs];
        double[] min = new double[numIDs];
        // find max/min
        for (int i = 0; i < numIDs; i++) {
            max[i] = 0;
            min[i] = 254;
            for (int j = 0; j < SLMModel.numPoints; j++) {
                if (max[i] < dataSets[i].getMeasurement(j)) {
                    max[i] = dataSets[i].getMeasurement(j);
                }
                if (dataSets[i].getMeasurement(j) > 0 && min[i] > dataSets[i].getMeasurement(j)) {
                    min[i] = dataSets[i].getMeasurement(j);
                }
            }
        }
        // normalize
        for (int i = 0; i < numIDs; i++) {
            for (int j = 0; j < SLMModel.numPoints; j++) {
                dataSets[i].measurement[j] = (dataSets[i].measurement[j] - min[i]) / (max[i] - min[i]);
            }
        }
        writeOutMatrix(dataSets, "Normalized");
        plotNormalized();
    }


    private void generate() {
        // Plot & record RetardanceCurve
        for (int i = 0; i < numIDs; i++) {
            System.out.println("\n" + ">>>>>>> ID: " + i);
            double threeBackValue = 0;
            double twoBackValue = 0;
            double lastValue = 0;
            boolean inSection1 = true;
            boolean inSection2 = false;
            boolean inSection3 = false;
            for (int j = 5; j < SLMModel.numPoints; j++) {
                System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
                double s = dataSets[i].getSetting(j);
                double m = dataSets[i].getMeasurement(j);
                double lastAvg = (lastValue + twoBackValue + threeBackValue) /3;
                double avg = (m + lastValue + twoBackValue) /3;                
                if (inSection1 && (lastAvg > avg)) {
                    inSection1 = false;
                    inSection2 = true;
                }
                if (inSection2 && (lastAvg< avg)) {
                    inSection2 = false;
                    inSection3 = true;
                }
                threeBackValue = twoBackValue;
                twoBackValue = lastValue;
                lastValue = m;
                double ret = 0;
                if (inSection1) {
                    ret = 3.0 * Math.PI - 2 * Math.asin(Math.sqrt(m));
                }
                if (inSection2) {
                    ret = Math.PI + 2 * Math.asin(Math.sqrt(m));
                }
                if (inSection3) {
                    ret = Math.PI - 2 * Math.asin(Math.sqrt(m));
                }
                dataSets[i].measurement[j] = ret;
            }
        }
        writeOutMatrix(dataSets, "Retardance");
        plotRetardance();

    }


    private void writeOutMatrix(DataSet[] dataSets, String title) {
        // Write out raw data as matrix
        CSVFileWrite dataFile;
        String filename = FileUtil.getcwd() + "/SLMDatafeb1/" + title + ".csv";
        if (filename != null) {
            dataFile = new CSVFileWrite(filename);
            //Object[] head = new Object[]{"zeroIntensity", slmModel.getZeroIntensity()};
            //dataFile.(head);
            if (dataFile != null) {
                for (int j = 0; j <
                        SLMModel.numPoints; j++) {
                    double s = dataSets[0].getSetting(j);
                    //System.out.println("\n" + j + ": " + s + " = ");
                    Object[] d = new Object[numIDs + 2];
                    d[0] = j;
                    d[1] = (int) s;
                    for (int i = 0; i <
                            numIDs; i++) {
                        double m = dataSets[i].getMeasurement(j);
                        d[i + 2] = m;
                    }
                    dataFile.writeRow(d);
                }
            }
            dataFile.close();
        }
    }


    public void plotNormalized() {
        MeasurementPlotter mPlot = new MeasurementPlotter("Normalized",
                numIDs, null, new Range(0, 1), new Rectangle(10, 10, 400,
                300));

        for (int i = 0; i < numIDs; i++) {
            //System.out.println("\n" + ">>>>>>> ID: " + i);
            for (int j = 0; j < SLMModel.numPoints; j++) {
                //System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
                double s = dataSets[i].getSetting(j);
                double m = dataSets[i].getMeasurement(j);
                mPlot.recordData(i, j, s, m);
            }
        }
    }


    public void plotRaw() {
        MeasurementPlotter mPlot = new MeasurementPlotter("Raw",
                numIDs, null, new Range(0, 254), new Rectangle(10, 10, 400,
                300));

        for (int i = 0; i < numIDs; i++) {
            //System.out.println("\n" + ">>>>>>> ID: " + i);
            for (int j = 0; j < SLMModel.numPoints; j++) {
                //System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
                double s = dataSets[i].getSetting(j);
                double m = dataSets[i].getMeasurement(j);
                mPlot.recordData(i, j, s, m);
            }
        }
    }


    public void plotRetardance() {
        MeasurementPlotter mPlot = new MeasurementPlotter("Retardance",
                numIDs, null, new Range(0, 10), new Rectangle(10, 10, 400,
                300));

        for (int i = 0; i < numIDs; i++) {
            //System.out.println("\n" + ">>>>>>> ID: " + i);
            for (int j = 0; j < SLMModel.numPoints; j++) {
                //System.out.println(dataSets[i].setting[j] + " => " + dataSets[i].measurement[j]);
                double s = dataSets[i].getSetting(j);
                double m = dataSets[i].getMeasurement(j);
                mPlot.recordData(i, j, s, m);
            }
        }
    }

// <editor-fold defaultstate="collapsed" desc=">>>--- DataSet ---<<<" >
    class DataSet {

        int id;
        double[] setting;
        double[] measurement;


        public DataSet(final int id,
                        int numPoints) {
            this.id = id;
            setting = new double[numPoints];
            measurement = new double[numPoints];
        }


        public void addPoint(int point,
                              final double setting,
                              final double measurement) {
            this.setting[point] = setting;
            this.measurement[point] = measurement;
        }


        public double getSetting(int point) {
            return setting[point];
        }


        public double getMeasurement(int point) {
            return measurement[point];
        }


    }


    private void createDataSets(int numIDs) {
        dataSets = new DataSet[numIDs];
        for (int i = 0; i <
                numIDs; i++) {
            dataSets[i] = new DataSet(i, SLMModel.numPoints);
        }

    }


    public void dumpDataSet() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> DataSet Dump:");
        if (dataSets != null) {
            for (int i = 0; i <
                    numIDs; i++) {
                for (int j = 0; j <
                        SLMModel.numPoints; j++) {
                    System.out.println(i + ", " + j + " == " +
                            dataSets[i].getMeasurement(j) + " @ " +
                            dataSets[i].getSetting(j));
                }

            }
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<< DataSet Dump");

    }
// </editor-fold>

    public static void main(String[] args) {
        RetDataAnalyzer r = new RetDataAnalyzer();
        r.go();
    }


}
