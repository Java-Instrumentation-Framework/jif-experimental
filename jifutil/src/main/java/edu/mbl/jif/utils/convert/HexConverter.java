package edu.mbl.jif.utils.convert;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Marine Biological Lab</p>
 * @author Grant B. Harris
 * @version 1.0
 */
public class HexConverter
{
   public static void main (String[] args) {
      test();
   }


   public static void test () {
      String hex = null;
      int out = 0;
      for (int i = -8388608; i < 8388607; i++) {
         try {
            hex = IntegerToHexTriplet(i);
            out = HexTripletToInteger(hex);
         }
         catch (Exception ee) {
            System.out.println("Exception in test.HexTripletToInteger: i = " + i
                  + " hex= " + hex + " out= " + out);
         }
      }

      //    try {
      //    HexTripletToInteger(IntegerToHexTriplet(1));
      //    HexTripletToInteger(IntegerToHexTriplet(-1));
      //    HexTripletToInteger(IntegerToHexTriplet(10000));
      //    HexTripletToInteger(IntegerToHexTriplet(-10000));
      //    HexTripletToInteger(IntegerToHexTriplet(0));
      //    HexTripletToInteger(IntegerToHex(1));
      //    HexTripletToInteger(IntegerToHex(-1));
      //    HexTripletToInteger(IntegerToHex(10000));
      //    HexTripletToInteger(IntegerToHex(-10000));
      //    HexTripletToInteger(IntegerToHex(0));
      //    // positive
      //    HexTripletToInteger("7FFFFF");  // 8388607
      //    HexTripletToInteger("000000");  // 0
      //    // negative
      //    HexTripletToInteger("FFFFFF");  // -1
      //    HexTripletToInteger("FFD8F0");  // -10000
      //    HexTripletToInteger("800000");  // -8388608
      //    } catch (Exception ee) {
      //     System.out.println("Exception in test.HexTripletToInteger");
      //    }
   }


   public static String IntegerToHexTriplet (int i) {
      String hS = null;
      String iS = Integer.toHexString(i);
      while (iS.length() < 6) {
         iS = "0" + iS;
      }
      if (iS.length() > 6) {
         hS = iS.substring(2, iS.length());
      } else {
         hS = iS;
      }
      hS = hS.toUpperCase();
      //System.out.println("IntegerToHexTriplet : " + i + " -> " + iS +", " + hS);
      return hS;
   }


   public static String IntegerToHex (int i) {
      String hS = null;
      String iS = Integer.toHexString(i);

      // no more than 6 chars
      if (iS.length() > 6) {
         hS = iS.substring(2, iS.length());
      } else {
         hS = iS;
      }
      hS = hS.toUpperCase();
      //System.out.println("IntegerToHex        : " + i + " -> " + iS +", " + hS);
      return hS;
   }


   public static int HexTripletToInteger (String h) throws Exception {
      int v = 0;
      try {
         Integer i = new Integer((int) Long.parseLong(h, 16));
         v = i.intValue();
      }
      catch (Exception ex) {
         throw ex;
      }
      if (v >= 8388608) {
         v = v - 16777216;
      }

      //System.out.println("HexTripletToInteger: " + h + " -> " + i + ", " + v);
      return v;
   }
}
