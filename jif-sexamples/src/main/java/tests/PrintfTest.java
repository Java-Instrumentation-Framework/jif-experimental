package tests;

/*
 * OutputTest.java
 *
 * Created on June 1, 2004, 12:31 PM
 */

//comment the package line out to run in the default package.

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author  cpresser
 */
public class PrintfTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Set up some variables with different types of data.
        String word = "Information";
        int integer = 24;
        double real = 12*Math.PI;
        BigInteger bigInt = new BigInteger("123456789012345678901234567890123456789012345678901234567890");
        Date today = Calendar.getInstance().getTime();

        //a series of output statements to show some of the functionality of the printf method.
        //The comments below indicate the output from each section:
        System.out.printf("String output tests:\n");
        System.out.printf("A string-------------------------------|%s|\n", word);
       // System.out.printf("An upper-case string-------------------|%^s|\n", word);
        System.out.printf("A string in 15 spaces------------------|%15s|\n", word);
        System.out.printf("A string left justified in 15 spaces---|%-15s|\n", word);
        System.out.printf("A string within 5 characters-----------|%.5s|\n", word);
        System.out.printf("A string in a space of 5 to 7 chars----|%5.7s|\n", word);
        System.out.printf("\n");
        /* OUTPUT:
      String output tests:
      A string-------------------------------|Information|
      An upper-case string-------------------|INFORMATION|
      A string in 15 spaces------------------|    Information|
      A string left justified in 15 spaces---|Information    |
      A string within 5 characters-----------|Infor|
      A string in a space of 5 to 7 chars----|Informa|
         */

        System.out.printf("Integer output tests:\n");
        System.out.printf("An integer-----------------------------|%d|\n", integer);
        System.out.printf("An integer in 4 cols-------------------|%4d|\n", integer);
        System.out.printf("An integer as an octal-----------------|%o|\n", integer);
        System.out.printf("An integer as hex----------------------|%x|\n", integer);
        System.out.printf("\n");
        /* OUTPUT:
      Integer output tests:
      An integer-----------------------------|24|
      An integer as an octal-----------------|30|
      An integer as hex----------------------|18|
         */

        System.out.printf("Double output tests:\n");
        System.out.printf("A double-------------------------------|%f|\n", real);
        System.out.printf("A double in scientific notation--------|%e|\n", real);
        System.out.printf("A double in 8 col, precision 2---------|%8.2f|\n", real);
        System.out.printf("A double with precision of 2-----------|%.2f|\n", real);
        System.out.printf("A double with precision of 10----------|%.10f|\n", real);
        System.out.printf("A double as a hex number---------------|%a|\n", real);
        System.out.printf("\n");
        /* OUTPUT:
      Double output tests:
      A double-------------------------------|37.699112|
      A double in scientific notation--------|3.769911e+01|
      A double with precision of 2-----------|37.70|
      A double with precision of 10----------|37.6991118431|
      A double as a hex number---------------|0x1.2d97c7f3321d2p5|
         */

        System.out.printf("BigInteger output test:\n");
        System.out.printf("A BigInteger---------------------------|%d|\n", bigInt);
        System.out.printf("\n");
        /* OUTPUT:
      BigInteger output test:
      A BigInteger---------------------------|123456789012345678901234567890123456789012345678901234567890|
         *
         */

        System.out.printf("Date output tests:\n"); //see multiple arguments section for others
        System.out.printf("A Date object as a string--------------|%s|\n", today);
        System.out.printf("A formatted Date object----------------|%tc|\n", today);
        System.out.printf("An ISO8602 format Date object----------|%tF|\n", today);
        System.out.printf("Time in 12 hour format ----------------|%tr|\n", today);
        System.out.printf("Time in 24 hour format-----------------|%tR|\n", today);
        System.out.printf("Time in 24 hour format w/ seconds -----|%tT|\n", today);
        System.out.printf("Date-----------------------------------|%tD|\n", today);
        System.out.printf("\n");
        /* OUTPUT:
      Date output tests:
      A Date object as a string--------------|Tue Jun 01 15:25:41 EDT 2004|
      A formatted Date object----------------|Tue Jun 01 15:25:41 EDT 2004|
      An ISO8602 format Date object----------|2004-06-01|
      Time in 12 hour format ----------------|03:25:41 PM|
      Time in 24 hour format-----------------|15:25|
      Time in 24 hour format w/ seconds -----|15:25:41|
      Date-----------------------------------|06/01/04|
         *
         */

        System.out.printf("Multiple Argument tests:\n");
        System.out.printf("%1$s %2$d, %1$s %3$d, %1$s %4$d\n", "label", 1, 2, 3);
        System.out.printf("Date mmddyyyy--------------------------|%1$tm%1$td%1$tY|\n", today);
        /* OUTPUT:
      Multiple Argument tests:
      label 1, label 2, label 3
      Date mmddyyyy--------------------------|06012004|
         *
         */

        //There are many other formats/conversions see java.util.Formatter for details
    }
   


}
