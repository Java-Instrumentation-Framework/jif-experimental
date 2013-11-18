package edu.mbl.jif.utils.convert;

/**
 * <p>Title: HexString</p>
 * <p>Description: Convert strings of hex to formatted strings of hex.  A string of
 * 0122ff would become [0x01][0x22][0xff].  Also converts from binary to hex and back.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Carter Engineering</p>
 * @author Greg Carter, contains code from Roedy Green 1996-2002 Canadian Mind Products copied
 * from http://mindprod.com/jglosshex.html.
 * @version 1.0
 */

public class HexString {

  public HexString() {
  }

  /**
   * convert a single char to corresponding nibble.
   *
   * @param c char to convert. must be 0-9 a-f A-F, no
   * spaces, plus or minus signs.
   *
   * @return corresponding integer
   */
  private static int charToNibble(char c) {
    if ('0' <= c && c <= '9') {
      return c - '0';
    }
    else if ('a' <= c && c <= 'f') {
      return c - 'a' + 0xa;
    }
    else if ('A' <= c && c <= 'F') {
      return c - 'A' + 0xa;
    }
    else {
      throw new NumberFormatException("Invalid hex character: " + c);
    }
  }

  /**
   * Convert a hex string to a byte array.
   * Permits upper or lower case hex.
   *
   * @param s String must have even number of characters.
   * and be formed only of digits 0-9 A-F or
   * a-f. No spaces, minus or plus signs.
   * @return corresponding byte array.
   */
  public static byte[] fromHexString(String s) throws NumberFormatException {
    int stringLength = s.length();
    if ( (stringLength & 0x1) != 0) {
      throw new NumberFormatException(
          "fromHexString requires an even number of hex characters");
    }
    byte[] b = new byte[stringLength / 2];

    for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
      int high = charToNibble(s.charAt(i));
      int low = charToNibble(s.charAt(i + 1));
      b[j] = (byte) ( (high << 4) | low);
    }
    return b;
  }

  static final String HEXPREFIX = "0x";
  /**
   *
   * Converts a hex formated string, [0x66][0xFF][0xCC]
   * to byte array.
   * */
  public static byte[] fromHexFormattedString(String s) throws
      NumberFormatException {
    //String temp = new String(s);
    String newValue = new String(s);
    StringBuffer currentOutBuffer = new StringBuffer();
    StringBuffer currentInBuffer = new StringBuffer(s);
    int i;
    i = s.indexOf(HEXPREFIX);
    //strip out the 0x
    while ( (i != -1) && (i + 2 < s.length())) {

      currentOutBuffer.append(newValue.substring(i + HEXPREFIX.length(),
                                                 i + HEXPREFIX.length() + 2));
      currentInBuffer.delete(i, i + HEXPREFIX.length());
      newValue = currentInBuffer.toString();
      i = newValue.indexOf(HEXPREFIX);
    }
    //delete trailing ']' if there is one.
    i = currentOutBuffer.toString().indexOf("]");
    if (i != -1) {
      currentOutBuffer.deleteCharAt(i);
    }
    return fromHexString(currentOutBuffer.toString());
  }

  static final String HEXFORMATPREFIX = "[0x";
  /**
   *
   * Converts a sequence of hex such as A466C908 to formatted hex string
   * [0xA4][0x66][0xC9][0x08]
   * */
  public static String toHexFormattedString(String s) throws
      NumberFormatException {
    if (s.length() == 0) {
      return s;
    }
    int stringLength = s.length();
    if ( (stringLength & 0x1) != 0) {
      throw new NumberFormatException(
          "fromHexString requires an even number of hex characters");
    }
    StringBuffer currentOutBuffer = new StringBuffer();
    for (int i = 0; i < (stringLength / 2); i++) {
      currentOutBuffer.append(HEXFORMATPREFIX);
      currentOutBuffer.append(s.substring(i * 2, i * 2 + 2));
      currentOutBuffer.append(']');
    }
    return currentOutBuffer.toString().toUpperCase();
  }

  int _i;
  /**
       * Checks if the string is properly formatted hex string, in the process converts
   * to formatted string, ie if input was ff2211, output will be [0xff][0x22][0x11].
       * Also allows partial strings such as [0xff][0x22]1 which would be returned as
   * [0xff][0x22][0x1].
   * */
  public String isValidHexString(String s) throws NumberFormatException {
    _i = 0;
    String outBuff = new String();
    while (_i < s.length()) {
      outBuff += getNextChunk(s);
    }
    return outBuff;
  }

  static final int PARSE_CHUNK_BEGIN = 0; //want a hex char or format prefix of '[0x'
  static final int PARSE_CHUNK_WANT_NEXT_CHAR = 1; //want a hex char
  static final int PARSE_CHUNK_WANT_FORMAT_END = 2; //want the ending ']'
  static final int PARSE_CHUNK_WANT_FIRST_CHAR_WITH_FORMAT = 3; //want hex characters followed by ']'
  static final int PARSE_CHUNK_WANT_SECOND_CHAR_WITH_FORMAT = 4; //want hex characters followed by ']'
  static final int PARSE_CHUNK_DONE = 10; //found chunk
  /**
   *
   * A chunk is defined as any of the following:
   * single hex char ex 'f' '1' '2' 'A'
   * double hex char ex 'ff' '11' '22' 'AA'
   * formated single hex char [0xf]
   * formated double hex char [0xf4]
   * */
  private String getNextChunk(String s) throws NumberFormatException {
    StringBuffer buff = new StringBuffer(s);
    StringBuffer outBuff = new StringBuffer();
    int state = PARSE_CHUNK_BEGIN;
    //int i;
    for (; ( (_i < s.length()) && (state != PARSE_CHUNK_DONE)); ) {
      char c = buff.charAt(_i);
      switch (state) {
        case PARSE_CHUNK_BEGIN:
          if (isHex(c)) {
            outBuff.append(HEXFORMATPREFIX);
            outBuff.append(c);
            state = PARSE_CHUNK_WANT_NEXT_CHAR;
            _i++;
            continue;
          }
          else if (c == '[') {
            String prefix = buff.substring(_i, _i + 3); //a '[' is only valid with '[0x'
            if (!prefix.equalsIgnoreCase(HEXFORMATPREFIX)) {
              throw new NumberFormatException("invalid hex sequence");
            }
            outBuff.append(HEXFORMATPREFIX);
            state = PARSE_CHUNK_WANT_FIRST_CHAR_WITH_FORMAT;
            _i += 3;
            continue;
          }
          else {
            throw new NumberFormatException("invalid hex sequence");
          }
        case PARSE_CHUNK_WANT_NEXT_CHAR:

          //valid next char are hex char or [
          if (isHex(c)) {
            outBuff.append(c);
            state = PARSE_CHUNK_DONE;
            outBuff.append(']');
            _i++;
            continue;
          }
          else if (c == '[') {
            //start of another hex char, this one only has one hex char signal done
            outBuff.append(']');
            state = PARSE_CHUNK_DONE;
            continue;
          }
          else {
            throw new NumberFormatException("invalid hex sequence");
          }
        case PARSE_CHUNK_WANT_FIRST_CHAR_WITH_FORMAT:
          if (isHex(c)) {
            outBuff.append(c);
            state = PARSE_CHUNK_WANT_SECOND_CHAR_WITH_FORMAT;
            _i++;
            continue;
          }
          else if (c == ']') { //an empty [0x]
            outBuff.append(']');
            state = PARSE_CHUNK_DONE;
            _i++;
            continue;
          }
          else {
            throw new NumberFormatException("invalid hex sequence");
          }
        case PARSE_CHUNK_WANT_SECOND_CHAR_WITH_FORMAT:
          if (isHex(c)) {
            outBuff.append(c);
            state = PARSE_CHUNK_WANT_FORMAT_END;
            _i++;
            continue;
          }
          else if (c == ']') { //an [0xf] with one hex char
            outBuff.append(']');
            state = PARSE_CHUNK_DONE;
            _i++;
            continue;
          }
          else {
            throw new NumberFormatException("invalid hex sequence");
          }
        case PARSE_CHUNK_WANT_FORMAT_END:

          //valid next ']' if hex or '[0x' signals new hex value so start again
          if (c == ']') { //an [0xff] with two hex char
            outBuff.append(']');
            state = PARSE_CHUNK_DONE;
            _i++;
            continue;
          }
          else {
            throw new NumberFormatException("invalid hex sequence");
          }
      }
    }
    switch (state) {
      case PARSE_CHUNK_WANT_NEXT_CHAR:
      case PARSE_CHUNK_WANT_SECOND_CHAR_WITH_FORMAT:
        outBuff.append(']');
        break;
      case PARSE_CHUNK_WANT_FIRST_CHAR_WITH_FORMAT:
      case PARSE_CHUNK_WANT_FORMAT_END:
        throw new NumberFormatException("invalid hex sequence");
    }
    return outBuff.toString();
  }

  private static boolean isHex(char c) {
    if (! ('0' <= c && c <= '9') &&
        ! ('a' <= c && c <= 'f') &&
        ! ('A' <= c && c <= 'F')) {
      return false;
    }
    return true;
  }

  /**
   *
   * Fast convert a byte array to a hex string
   * with possible leading zero.
   * */
  public static String toHexString(byte[] b, int offset, int len) {
    StringBuffer sb = new StringBuffer(b.length * 2);
    for (int i = offset; i < len; i++) {
// look up high nibble char
      sb.append(hexChar[ (b[i] & 0xf0) >>> 4]);

// look up low nibble char
      sb.append(hexChar[b[i] & 0x0f]);
    }
    return toHexFormattedString(sb.toString());

  }

  public static String toHexString(byte[] b) {
    return toHexString(b, 0, b.length);
  }

// table to convert a nibble to a hex char.
  static char[] hexChar = {
      '0', '1', '2', '3',
      '4', '5', '6', '7',
      '8', '9', 'a', 'b',
      'c', 'd', 'e', 'f'}
      ;
}
