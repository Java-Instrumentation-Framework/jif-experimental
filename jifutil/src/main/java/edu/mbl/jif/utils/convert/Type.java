package edu.mbl.jif.utils.convert;

/**
 * Type.java
 *
 * Use this static class to easily convert any Java type to any Java type.
 * Works with byte, double, float, int, long, short, and String.
 *
 * Example of use:
 *
 * import com.neilmoomey.util.Type;
 * String s = "12345.6789";
 * float f = Type.toFloat(s);
 *
 * @version 1.0 28 Feb 2003
 * @author 	Neil Moomey
 * www.neilmoomey.com
 */

import java.util.regex.*;

public final class Type {

  /*************** toByte ************
   * byte = 8 bits, range = -128 .. 127
   ***********************************/

  /**
   * double to byte
   */
  public static byte toByte(double d) {
    if (d < -128 || d > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Double(d).byteValue();
    return b;
  }

  /**
   * float to byte
   */
  public static byte toByte(float f) {
    if (f < -128 || f > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Float(f).byteValue();
    return b;
  }

  /**
   * int to byte
   */
  public static byte toByte(int i) {
    if (i < -128 || i > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Integer(i).byteValue();
    return b;
  }

  /**
   * long to byte
   */
  public static byte toByte(long l) {
    if (l < -128 || l > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Long(l).byteValue();
    return b;
  }

  /**
   * short to byte
   */
  public static byte toByte(short sh) {
    if (sh < -128 || sh > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Short(sh).byteValue();
    return b;
  }

  /**
   * String to byte
   */
  public static byte toByte(String s) {
    long l = toLong(s);
    if (l < -128 || l > 127) {
      System.out.println("Number is too large. Range is -128 to 127");
      return 0;
    }
    byte b = new Long(l).byteValue();
    return b;
  }

  /*********************************** toDouble ***************************************
   * double = 64 bits, range = 1.7976931348623157 x 10^308, 4.9406564584124654 x 10^-324
   ************************************************************************************/

  /**
   * byte to double
   */
  public static double toDouble(byte b) {
    double d = new Byte(b).doubleValue();
    return d;
  }

  /**
   * float to double
   */
  public static double toDouble(float f) {
    double d = new Float(f).doubleValue();
    return d;
  }

  /**
   * int to double
   */
  public static double toDouble(int i) {
    double d = new Integer(i).doubleValue();
    return d;
  }

  /**
   * long to double
   */
  public static double toDouble(long l) {
    double d = new Long(l).doubleValue();
    return d;
  }

  /**
   * short to double
   */
  public static double toDouble(short s) {
    double d = new Short(s).doubleValue();
    return d;
  }

  /**
   * String to double
   */
  public static double toDouble(String s) {
    double d = new Double(s).doubleValue();
    return d;
  }

  /*************************** toFloat ********************************
   * float = 32 bits, range = 3 1.40239846 x 10^-45 to .40282347 x 10^38
   ********************************************************************/

  /**
   * byte to float
   */
  public static float toFloat(byte b) {
    float f = new Byte(b).floatValue();
    return f;
  }

  /**
   * double to float
   */
  public static float toFloat(double d) {
    if (d < 1.40239846 * Math.pow(10, -45) || d > 3.40282347 * Math.pow(10, 38)) {
      System.out.println(
          "Number is too large. Range is 1.40239846 x 10^-45 to 3.40282347 x 10^38 ");
      return 0;
    }
    float f = new Double(d).floatValue();
    return f;
  }

  /**
   * int to float
   */
  public static float toFloat(int i) {
    float f = new Integer(i).floatValue();
    return f;
  }

  /**
   * long to float
   */
  public static float toFloat(long l) {
    float f = new Long(l).floatValue();
    return f;
  }

  /**
   * short to float
   */
  public static float toFloat(short sh) {
    float f = new Short(sh).floatValue();
    return f;
  }

  /**
   * String to float
   */
  public static float toFloat(String s) {
    float f = new Float(s).floatValue();
    return f;
  }

  /************************ toInt ************************
   * int = 32 bits, range = -2,147,483,648 .. 2,147,483,647
   *******************************************************/

  /**
   * byte to int
   */
  public static int toInt(byte b) {
    int i = new Byte(b).intValue();
    return i;
  }

  /**
   * double to int
   */
  public static int toInt(double d) {
    if (d < -2147483648 || d > 2147483647) {
      System.out.println(
          "Number is too large. Range is -2,147,483,648 to 2,147,483,647");
      return 0;
    }
    int i = new Double(d).intValue();
    return i;
  }

  /**
   * float to int
   */
  public static int toInt(float f) {
    if (f < -2147483648 || f > 2147483647) {
      System.out.println(
          "Number is too large. Range is -2,147,483,648 to 2,147,483,647");
      return 0;
    }
    int i = new Float(f).intValue();
    return i;
  }

  /**
   * long to int
   */
  public static int toInt(long l) {
    int i = new Long(l).intValue();
    return i;
  }

  /**
   * short to int
   */
  public static int toInt(short sh) {
    int i = new Short(sh).intValue();
    return i;
  }

  /**
   * String to int
   */
  public static int toInt(String s) {

    long l = toLong(s);
    if (l < -2147483648 || l > 2147483647) {
      System.out.println(
          "Number is too large. Range is -2,147,483,648 to 2,147,483,647");
      return 0;
    }
    int i = new Long(l).intValue();
    return i;
  }

  /********************************** toLong **************************************
   * long = 64 bits, range = -9,223,372,036,854,775,808 .. 9,223,372,036,854,775,807
   ********************************************************************************/

  /**
   * byte to Long
   */
  public static long toLong(byte b) {
    long l = new Byte(b).longValue();
    return l;
  }

  /**
   * double to Long
   */
  public static long toLong(double d) {
    long l = new Double(d).longValue();
    return l;
  }

  /**
   * float to Long
   */
  public static long toLong(float f) {
    long l = new Float(f).longValue();
    return l;
  }

  /**
   * int to Long
   */
  public static long toLong(int i) {
    long l = new Long(i).longValue();
    return l;
  }

  /**
   * short to Long
   */
  public static long toLong(short sh) {
    long l = new Short(sh).longValue();
    return l;
  }

  /**
   * String to Long
   */
  public static long toLong(String s) {
    int index = s.indexOf(".");
    if (index != -1) s = s.substring(0, index);
    double d = toDouble(s);
    if (d < -9223372036854775808D || d > 9223372036854775807D) {
      System.out.println("Number is too large. Range is -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807");
      return 0;
    }

    long l = new Double(d).longValue();
    return l;
  }

  /**************** toShort ******************
   * short = 16 bits, range = -32,768 .. 32,767
   *******************************************/
  /**
   * byte to Short
   */
  public static short toShort(byte b) {
    short sh = new Byte(b).shortValue();
    return sh;
  }

  /**
   * double to Short
   */
  public static short toShort(double d) {
    if (d < -32768 || d > 32767) {
      System.out.println("Number is too large.  Range is -32,768 to 32,767");
      return 0;
    }
    short sh = new Double(d).shortValue();
    return sh;
  }

  /**
   * float to Short
   */
  public static short toShort(float f) {
    if (f < -32768 || f > 32767) {
      System.out.println("Number is too large.  Range is -32,768 to 32,767");
      return 0;
    }
    short sh = new Float(f).shortValue();
    return sh;
  }

  /**
   * int to Short
   */
  public static short toShort(int i) {
    if (i < -32768 || i > 32767) {
      System.out.println("Number is too large.  Range is -32,768 to 32,767");
      return 0;
    }
    short sh = new Integer(i).shortValue();
    return sh;
  }

  /**
   * long to Short
   */
  public static short toShort(long l) {
    if (l < -32768 || l > 32767) {
      System.out.println("Number is too large.  Range is -32,768 to 32,767");
      return 0;
    }
    short sh = new Long(l).shortValue();
    return sh;
  }

  /**
   * String to Short
   */
  public static short toShort(String s) {
    long l = toLong(s);
    if (l < -32768 || l > 32767) {
      System.out.println("Number is too large. Range is -32,768 to 32,767");
      return 0;
    }
    short sh = new Long(l).shortValue();
    return sh;
  }

  /******************** toString *********************/

  /**
   * byte to String
   */
  public static String toString(byte d) {
    String s = new Byte(d).toString();
    return s;
  }

  /**
   * double to String
   */
  public static String toString(double d) {
    String s = new Double(d).toString();
    return s;
  }

  /**
   * float to String
   */
  public static String toString(float f) {
    String s = new Float(f).toString();
    return s;
  }

  /**
   * int to String
   */
  public static String toString(int i) {
    String s = new Integer(i).toString();
    return s;
  }

  /**
   * long to String
   */
  public static String toString(long l) {
    String s = new Long(l).toString();
    return s;
  }

  /**
   * short to String
   */
  public static String toString(short sh) {
    String s = new Short(sh).toString();
    return s;
  }

  public static boolean isNumber(String s) {
    // Use Regex to see if it's a number (Requires J2SE 1.4 and above)
    Pattern p = Pattern.compile("[^0-9]");
    Matcher m = p.matcher(s);
    if (!m.find())return true;
    else return false;
  }


//----------------------------------------------------------------------

  public static String byteToHex(byte b) {
    int i = b & 0xFF;
    return Integer.toHexString(i);
  }

  public static int unsignedByteToInt(byte b) {
    return (int) b & 0xFF;
  }


//---------------------------------------------------------------------


  public static void main(String args[]) {
    // Test this class
    byte b = 127; // Max is 127
    double d = 120999.098D; // Max is 1.7976931348623157 x 10^308
    float f = 120999.098F; // Max is 3.40282347 x 10^38
    int i = 12099999; // Max is 2,147,483,647
    long l = 32; // Max is 9,223,372,036,854,775,807
    short sh = 32767; // Max is 32767
    String s = "-9223372036854775809";

    System.out.println("double to byte = " + Type.toByte(d));
    System.out.println("float to byte = " + Type.toByte(f));
    System.out.println("int to byte = " + Type.toByte(sh));
    System.out.println("long to byte = " + Type.toByte(l));
    System.out.println("short to byte = " + Type.toByte(sh));
    System.out.println("String to byte = " + Type.toByte(s));
    System.out.println("");
    System.out.println("byte to String = " + Type.toString(b));
    System.out.println("double to String = " + Type.toString(d));
    System.out.println("float to String = " + Type.toString(f));
    System.out.println("int to String = " + Type.toString(i));
    System.out.println("long to String = " + Type.toString(l));
    System.out.println("short to String = " + Type.toString(sh));
    System.out.println("");
    System.out.println("byte to double = " + Type.toDouble(b));
    System.out.println("float to double = " + Type.toDouble(f));
    System.out.println("int to double = " + Type.toDouble(i));
    System.out.println("long to double = " + Type.toDouble(l));
    System.out.println("short to double = " + Type.toDouble(sh));
    System.out.println("String to double = " + Type.toDouble(s));
    System.out.println("");
    System.out.println("byte to float = " + Type.toFloat(b));
    System.out.println("double to float = " + Type.toFloat(d));
    System.out.println("int to float = " + Type.toFloat(i));
    System.out.println("long to float = " + Type.toFloat(l));
    System.out.println("short to float = " + Type.toFloat(sh));
    System.out.println("String to float = " + Type.toFloat(s));
    System.out.println("");
    System.out.println("byte to int = " + Type.toInt(b));
    System.out.println("double to int = " + Type.toInt(d));
    System.out.println("float to int = " + Type.toInt(f));
    System.out.println("long to int = " + Type.toInt(l));
    System.out.println("short to int = " + Type.toInt(sh));
    System.out.println("String to int = " + Type.toInt(s));
    System.out.println("");
    System.out.println("byte to long = " + Type.toLong(b));
    System.out.println("double to long = " + Type.toLong(d));
    System.out.println("float to long = " + Type.toLong(f));
    System.out.println("long to long = " + Type.toLong(l));
    System.out.println("short to long = " + Type.toLong(sh));
    System.out.println("String to long = " + Type.toLong(s));
    System.out.println("");
    System.out.println("byte to short = " + Type.toShort(b));
    System.out.println("double to short = " + Type.toShort(d));
    System.out.println("float to short = " + Type.toShort(f));
    System.out.println("int to short = " + Type.toShort(i));
    System.out.println("long to short = " + Type.toShort(l));
    System.out.println("String to short = " + Type.toShort(s));
    System.out.println("");
  }
}
