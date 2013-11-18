package edu.mbl.jif.utils;

import edu.mbl.jif.utils.convert.ByteArray;
import java.security.MessageDigest;
import java.io.File;


/**
 * MessageDigest using MD5
 */


public class MsgDigest5
{
   /** md5 generator for file cache */
   static protected MessageDigest md5;

   //public MsgDigest5 () {}

   public static String getFileDigest (File file) {
      // assumes that the file exists
      try {
         md5 = MessageDigest.getInstance("MD5");
      }
      catch (Exception ex) {}
      md5.update(file.toURI().toString().getBytes());
      byte[] bytes = md5.digest();
      String md5String = ByteArray.byteArrayToString(bytes);
      System.out.println("  md5String: " + md5String);
      return md5String;
   }




   public static void main (String[] args) {
      long start = System.currentTimeMillis();

//      File file = new File(
//            "D:\\_TestImages\\ImagesTest\\tiff\\_PS_03_0825_1751_01.tiff");

      String directoryName = "D:\\_TestImages\\ImagesTest\\tiff\\";
      File directory = new File(directoryName);
      if (directory.isDirectory() == false) {
         if (directory.exists() == false) {
            System.out.println("There is no such directory!");
         } else {
            System.out.println("That file is not a directory.");
         }
      } else {
         //String[]
         File[] files = directory.listFiles();
         System.out.println("Files in directory \"" + directory + "\":");
         for (int i = 0; i < files.length; i++) {
            System.out.print("   " + files[i]);
            MsgDigest5.getFileDigest(files[i]);
         }

      }
      System.out.println(System.currentTimeMillis() - start);
   }
}
