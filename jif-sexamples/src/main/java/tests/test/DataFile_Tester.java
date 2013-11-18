/*
 * TextDataFile.java
 *
 * Created on July 10, 2006, 2:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tests.test;

import java.io.File;
import java.io.IOException;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;
import com.infomata.data.TabFormat;


/**
 *  Depends on datafile.jar
 * @author GBH
 */
public class DataFile_Tester {
   /** Creates a new instance of TextDataFile */
   public DataFile_Tester() {
   }

   /*
   Creating a reader for CSV file using
   ISO-8859-1
   */
   public static void main(String[] args) throws IOException {
      DataFile read = DataFileFactory.createReader("8859_1");

      read.setDataFormat(new CSVFormat());
      // first line is column header
      read.containsHeader(true);

      try {
         read.open(new File("/data/test.csv"));

         for (DataRow row = read.next(); row != null; row = read.next()) {
            String text = row.getString(0);

            // retrieval using column header
            int    number1 = row.getInt("FIRST_NUMBER", 0);
            double number2 = row.getDouble(2);

            // use the retrieved data ...
         }
      }
      finally {
         read.close();
      }

      /*
         Creating a writer for data file with
         European encoding ISO-8859-2 (European)
         using tab separated format.
         (rewrite existing file)
      */
      DataFile write = DataFileFactory.createWriter("8859_2", false);

      write.setDataFormat(new TabFormat());

      try {
         write.open(new File("/data/test.txt"));

         for (DataRow row = write.next(); row != null; row = write.next()) {
            row.add("some German text");
            row.add(123);
            row.add(13323.23d);
         }
      }
      finally {
         write.close();
      }
   }
}
