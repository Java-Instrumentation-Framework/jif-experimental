/*
Technology Exponent Common Utilities For Java (TECUJ)
Copyright (C) 2003,2004  Abdul Habra, Doug Estep.
www.tek271.com

This file is part of TECUJ.

TECUJ is free software; you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published
by the Free Software Foundation; version 2.

TECUJ is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with TECUJ; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

You can contact the author at free@tek271.com or ahabra@yahoo.com
*/

package edu.mbl.jif.utils.string;

import edu.mbl.jif.utils.string.ListOfString;
import java.util.*;
import org.apache.commons.lang.StringUtils;
//import com.tek271.util.collections.*;

/**
 * Helper class containing common string functions. Note that this class
 * extends org.apache.commons.lang.StringUtils.<p>
 * The StringUtils class is available to download from
 * http://jakarta.apache.org/commons/lang.html.
 * Make sure to include the downloaded jar file in your classpath, currently this
 * is commons-lang-2.0.jar
 * <p>Copyright: Copyright (c) 2003 Technology Exponent</p>
 * @author Doug Estep, Abdul Habra
 * @version 1.0
 */
public class StringUtility extends StringUtils {
  public static final String NEW_LINE = System.getProperty("line.separator");
  public static final String TAB = "\t";
  public static final String SINGLE_QUOTE = "'";
  public static final String DOUBLE_QUOTE = "\"";
  public static final String BLANK = " ";
  public static final String EQUAL = "=";
  public static final String COMMA = ",";
  public static final String DOT = ".";
  public static final String SEMI_COLON= ";";
  public static final String COLON = ":";
  public static final String QUESTION = "?";
  public static final String SLASH = "/";
  public static final String BACK_SLASH = "\\";
  public static final String LESS = "<";
  public static final String GREATER = ">";
  public static final String LPARAN = "(";
  public static final String RPARAN = ")";
  public static final String PIPE = "|";
  public static final String MINUS = "-";
  public static final String PLUS = "+";
  public static final String YES = "YES";
  public static final String NO = "NO";

  public static final String WHITE_SPACE = BLANK + TAB + NEW_LINE;

  /** simly faces */
  public static final String SML_HAPPY = ":-)";
  public static final String SML_WINK  = ";-)";
  public static final String SML_SAD   = ":-(";
  public static final String SML_CRY   = ":'-(";
  public static final String SML_DUMB  = "<:-)";
  public static final String SML_DRUNK = ":*)";
  public static final String SML_YUMM  = ":~)";

  public static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String ALPHA_SMALL= "abcdefghijklmnopqrstuvwxyz";
  public static final String DIGITS = "0123456789";
  public static final String ALPHA_NUMERIC = DIGITS + ALPHA_CAPS + ALPHA_SMALL;

  /*
   * No public constructor.
   */
  private StringUtility() {
  }

/**
 * Extract letters and/or numbers from the given string
 * @param aStr String to extract from.
 * @param aIsExtractAlpha extract letters?
 * @param aIsExtractNum boolean extract digits?
 * @return String
 */
  public static String extract(final String aStr,
                               final boolean aIsExtractAlpha,
                               final boolean aIsExtractNum) {
    if (aStr==null) return EMPTY;
    if (!aIsExtractAlpha && !aIsExtractNum) return EMPTY;

    int n= aStr.length();
    StringBuffer r=new StringBuffer(n);
    for (int i=0; i<n; i++) {
      char ch=aStr.charAt(i);
      if (aIsExtractAlpha && Character.isLetter(ch)) r.append(ch);
      else
      if (aIsExtractNum && Character.isDigit(ch)) r.append(ch);
    }
    return r.toString();
  }  // extract


  /**
   * Removes all non-alpha numeric characters from a string.
   * @param aStr The string to process.
   * @return String The formatted string.
   */
  public static String extractAlphaNum(final String aStr) {
    return extract(aStr, true, true);
  }

  /**
   * Returns only those characters within the string that are alpha.
   * @param str
   * @return String Returns alpha characters with the string passed.
   */
  public static String extractAlpha(final String aStr) {
    return extract(aStr, true, false);
  }

  /**
   * Get the total length of strings in the array
   * @param aArray of strings
   * @return length of all strings
   */
  public static int getLengthSum(final String[] aArray) {
    if (aArray==null) return 0;
    int sum=0;
    for (int i=0, n=aArray.length; i<n; i++)
      if (aArray[i] != null)
        sum += aArray[i].length();
    return sum;
  } // getLengthSum

  /**
  * Create a string of a repeated character.
  * @param aChar Character to repeat.
  * @param aCount How many times.
  * @return Repeated character string.
  */
  public static String repeat(final char aChar, final int aCount) {
    if (aCount == 0) return EMPTY;
    char[] buf = new char[aCount];
    Arrays.fill(buf, aChar);
    return new String(buf);
  } // repeat

  /**
   * Create a string of blank space characters.
   * @param aCount Size of string
   * @return The blanks string.
   */
  public static String blanks(final int aCount) {
    return repeat(' ', aCount);
  }

  /**
   * Append a single space before and after aStr
   * @param aStr The string to pad.
   * @return The padded string.
   */
  public static String pad(final String aStr) {
    return BLANK + aStr + BLANK;
  }

  /** Count how many aSub exist in aSource */
  public static int countSubstring(final String aSource, final String aSub) {
    int index=0, count=0;
    int subLen = aSub.length();

    while (true) {
      index = aSource.indexOf(aSub, index);
      if (index == -1) break;
      count++;
      index += subLen;
    } // while
    return count;
  } // countSubstring

  /** return the hex character for a single digit */
  private static char toHex(byte aDigit) {
    aDigit = (byte) (Math.abs(aDigit) % 16);
    int hex;
    if (aDigit < 10)
      hex = aDigit + '0';
    else
      hex = aDigit + 'A' - 10;
    return (char) hex;
  } // toHex

  /** the hex representation for a character */
  public static String toHex(char aChar) {
    StringBuffer buf = new StringBuffer(4);
    int digit;

    for (int i=0; i < 4; i++) {
      digit = aChar % 16;
      buf.append(toHex((byte)digit) );
      aChar /= 16;
    }
    buf.reverse();
    return buf.toString();
  } // toHex

/**
 * Convert the hex char into its decimal value, return -1 if not valid.
 * @param aHex char
 * @return int -1 if aHex is not in (0..9, A..F)
 */
  public static int fromHex(char aHex) {
    aHex= Character.toUpperCase(aHex);
    if (aHex >= '0' && aHex <='9') return aHex - '0';
    if (aHex >= 'A' && aHex <='F') return aHex - 'A' + 10;
    return -1;
  }  // fromHex

/**
 * Convert aHex into its numeric value.
 * @param aHex String A hex string to convert to long. The maximum length for aHex is
 * 15, in order to fit within a long.
 * @return -1 if error.
 */
  public static long fromHex(final String aHex) {
    int n=aHex.length();
    if (n>15) return -1;

    long r=0;
    int c;
    for (int i=0; i<n; i++) {
      c= fromHex(aHex.charAt(i));
      if (c==-1) return -1;
      r= r*16 + c;
    }
    return r;
  }  // fromHex

/** The Unicode representation for a char. */
  public static String toUnicode(final char aChar) {
    return "\\u" + toHex(aChar);
  } // toUnicode

/** The Unicode representation for a string. */
  public static String toUnicode(final String aString) {
    StringBuffer buf = new StringBuffer();
    for (int i=0; i<aString.length(); i++) {
      buf.append( toUnicode(aString.charAt(i)) );
    }
    return buf.toString();
  } // toUnicode

/**
 * Convert a unicode string for a single character to its numeric value.
 * @param aUnicode Example: \u0010 will return 16. The string must be 6 chars. long.
 * @return int -1 if error.
 */
  public static int fromUnicode(final String aUnicode) {
    if (aUnicode.length() != 6) return -1;
    String u= aUnicode.toUpperCase();
    if (!u.startsWith("\\U")) return -1;

    u= u.substring(2);
    return (int) fromHex(u);
  } // fromUnicode()

/** Unescape a string that may contain unicode chars */
  public static String unescapeUnicode(final String aString) {
    int n= aString.length();
    String caps= aString.toUpperCase();
    StringBuffer r= new StringBuffer(n);
    int start=0;
    while (true) {
      int i= caps.indexOf("\\U", start);
      if (i<0) {
        r.append( aString.substring(start) );
        break;
      }
      r.append( aString.substring(start, i) );
      String us= substring(aString, i, i+6);  // unicode string
      int ui= fromUnicode(us);  // its value
      if (ui<0) {  // not a valid unicode
        r.append(us.charAt(0));
        start=i+1;
      } else {  // valid unicode, convert it to one char
        r.append( (char) ui);
        start= i+6;
      }
    }  // while
    return r.toString();
  }  // enescapeUnicode

  /**
   * Check if aSubject starts with aTarget case insensetive
   * @param aSubject String to inspect
   * @param aTarget String to look for
   * @return true if aSubject starts with aTarget, false otherwise.
   */
  public static boolean startsWithIgnoreCase(final String aSubject, final String aTarget) {
    if (aSubject.length() < aTarget.length()) return false;
    String pre = aSubject.substring(0, aTarget.length() );
    return pre.equalsIgnoreCase(aTarget);
  } // startsWithIgnoreCase()

  /**
   * Check if aSubject starts with aTarget according to the aCaseSensetive flag
   * @param aSubject String to inspect
   * @param aTarget String to look for
   * @param aCaseSensetive Determine if the search is case sensetive or not.
   * @return true if aSubject starts with aTarget, false otherwise.
   */
  public static boolean startsWith(final String aSubject,
                                   final String aTarget,
                                   final boolean aCaseSensetive) {
    if (aCaseSensetive)
      return aSubject.startsWith(aTarget);
    else
      return startsWithIgnoreCase(aSubject, aTarget);
  } // startsWith()


/**
 * Check if aSubject ends with aTarget case insensetive
 * @param aSubject String to inspect
 * @param aSuffix String to look for
 * @return true if aSubject ends with aTarget, false otherwise.
 */
  public static boolean endsWithIgnoreCase(final String aSubject, final String aSuffix) {
    int sufLen= aSuffix.length();
    if (aSubject.length() < sufLen) return false;
    String suf= right(aSubject, sufLen);
    return suf.equalsIgnoreCase(aSuffix);
  } // startsWithIgnoreCase()

/**
 * Check if aSubject ends with aSuffix according to the aCaseSensetive flag
 * @param aSubject String to inspect
 * @param aSuffix String to look for
 * @param aCaseSensetive Determine if the search is case sensetive or not.
 * @return true if aSubject ends with aSuffix, false otherwise.
 */
  public static boolean endsWith(final String aSubject,
                                 final String aSuffix,
                                 final boolean aCaseSensetive) {
    if (aCaseSensetive)
      return aSubject.endsWith(aSuffix);
    else
      return endsWithIgnoreCase(aSubject, aSuffix);
  } // startsWith()

  /**
  * Check if aIndex>=0 and aIndex<aTarget.length()
  */
  public static boolean isValidIndex(final String aTarget, final int aIndex) {
    return ( (aIndex>=0) && (aIndex<aTarget.length()) );
  } // isValidIndex()

  /**
   * Replace every single quote in aStr with two single quotes. Useful for
   *   SQL queries.
   * @param aStr The string to replace its single quotes
   * @return The string with all single quotes replaced
   */
  public static String replaceQuote4Db(final String aStr) {
    return replace(aStr, SINGLE_QUOTE, "''");
  }

  /**
   * Replace all occurance of html tags with their encoding.
   * @param aStr The string to replace its html tags.
   * @return The string with all & replaced with &amp;amp; and
   *   < replaced with &amp;lt;
   */
  public static String replaceHtmlTags(final String aStr) {
    String s= replace(aStr, "&", "&#38;");
    return replace(s, "<", "&lt;");
  }

  /**
   * Replace all occurance of double quote with the escape code and double quote.
   * @param aStr The string to replace its double quotes.
   * @return The string with all double quotes replaced with escape code and double quote.
   */
  public static String replaceDoubleQuote4Html(final String aStr) {
    return replace(aStr, DOUBLE_QUOTE, "\\\"");
  }

  /**
   * Make sure aStr is prefixed with aPrefix.
   * @param aStr String to inspect.
   * @param aPrefix Prefix that must start the returned string.
   * @param aCaseSensetive is prefix case sensetive?
   * @return If aStr is blank return empty string, else return aStr but make
   *   sure it starts with aPrefix.
   */
  public static String prefix(String aStr, String aPrefix,
                              boolean aCaseSensetive ) {
    if (StringUtility.isBlank(aStr)) return StringUtility.EMPTY;
    aStr= aStr.trim();
    if (startsWith(aStr, aPrefix, aCaseSensetive)) return aStr;
    return aPrefix + aStr;
  }

/**
 * Make sure aStr is prefixed with aPrefix. Case insensetive.
 * @param aStr String to inspect.
 * @param aPrefix Prefix that must start the returned string.
 * @return If aStr is blank return empty string, else return aStr but make
 *   sure it starts with aPrefix.
 */
  public static String prefix(String aStr, String aPrefix) {
    return prefix(aStr, aPrefix, false);
  }

/** Return the last character in the string */
  public static char lastChar(final String aStr) {
    return aStr.charAt( aStr.length()-1 );
  } // lastChar

  /** Search a String to find the first index of any character in the given
   * set of characters starting at aStartIndex
   */
  public static int indexOfAny(final String aStr,
                               final String aSearchChars,
                               final int aStartIndex) {
    for(int i= aStartIndex, n= aStr.length(); i<n; i++) {
      char c= aStr.charAt(i);
      if (aSearchChars.indexOf(c)>=0) return i;
    }
    return -1;
  } // indexOfAny

/**
 * Search aStr to find the index of the first char that is not in aSearchChars.
 * @param aStr String String to search.
 * @param aSearchChars String chars to search for any but them.
 * @param aStartIndex int starting index in aStr
 * @return int
 */
  public static int indexOfAnyBut(final String aStr,
                                  final String aSearchChars,
                                  final int aStartIndex) {
    for(int i= aStartIndex, n= aStr.length(); i<n; i++) {
      char c= aStr.charAt(i);
      if (aSearchChars.indexOf(c) < 0) return i;
    }
    return -1;
  } // indexOfAnyBut


  /** Check if aCh is either a single quote or double quote */
  public static boolean isQuote(final char aCh) {
    return aCh=='"' || aCh=='\'';
  }

  /** Compares two Strings, returning true if they are equal, taking into consideration
   *  the case sensetivity parameter.
   */
  public static boolean equals(final String aStr1,
                               final String aStr2,
                               final boolean aCaseSensetive) {
    if (aCaseSensetive) return equals(aStr1, aStr2);
    return equalsIgnoreCase(aStr1, aStr2);
  } // equals

  /**
   * Check if a one-character string is a member of the given set of characters.
   * @param aOneChar A string of one char.
   * @param aSetOfChars The set of chars that can be valid.
   * @return true if aSetOfChars contains aOneChar.
   */
  public static boolean equalsAnyChar(final String aOneChar,
                                      final String aSetOfChars) {
    if (aOneChar.length() != 1) return false;
    return aSetOfChars.indexOf(aOneChar.charAt(0)) >=0;
  } // equals

  /**
   * Search a StringBuffer for the first occurance of aTarget starting at aFromIndex.
   * @param aBuffer StringBuffer
   * @param aFromIndex int
   * @param aTarget char
   * @return int index of aTarget, -1 if not found
   * Note that Java 1.4 StringBuffer has an equivelant method
   */
  public static int indexOfSB(final StringBuffer aBuffer,
                              final int aFromIndex,
                              final char aTarget) {
    for (int i=aFromIndex, n=aBuffer.length(); i<n; i++)
      if (aBuffer.charAt(i)==aTarget) return i;
    return -1;
  }  // indexOfSB

  /**
   * Search a StringBuffer for the first occurance of aTarget starting at first character.
   * @param aBuffer StringBuffer
   * @param aTarget char
   * @return int index of aTarget, -1 if not found
   * Note that Java 1.4 StringBuffer has an equivelant method
   */
  public static int indexOfSB(final StringBuffer aBuffer, final char aTarget) {
    return indexOfSB(aBuffer, 0, aTarget);
  }

  public static String toString(final StringBuffer aBuffer) {
    if (aBuffer==null  || aBuffer.length()==0) return EMPTY;
    return aBuffer.toString();
  }

/** Check is a string buffer is null or zero-length */
  public static boolean isEmpty(final StringBuffer aBuffer) {
    if (aBuffer==null) return true;
    if (aBuffer.length()==0) return true;
    return false;
  }

/** Check if aSet contains aItem */
  public static boolean in(final String aItem, final String[] aSet,
                           final boolean aCaseSensetive) {
    for (int i=0, n=aSet.length; i<n; i++) {
      if (equals(aItem, aSet[i], aCaseSensetive)) return true;
    }
    return false;
  }  // in

/** Check if aSet contains aItem */
  public static boolean in(final String aItem, final String[] aSet) {
    return in(aItem, aSet, true);
  }

/** Delete from aSource all chars that are in aSetToRomve */
  public static String deleteSet(final String aSource, final String aSetToRomve) {
    int n= aSource.length();
    StringBuffer b= new StringBuffer(n);
    char c;
    for (int i=0; i<n; i++) {
      c= aSource.charAt(i);
      if (aSetToRomve.indexOf(c) <0) b.append(c);
    }
    return b.toString();
  } // deleteSet


/** Delete from aSource all chars that are not in aSetToKeep */
  public static String deleteButSet(final String aSource, final String aSetToKeep) {
    int n= aSource.length();
    StringBuffer b= new StringBuffer(n);
    char c;
    for (int i=0; i<n; i++) {
      c= aSource.charAt(i);
      if (aSetToKeep.indexOf(c) >= 0) b.append(c);
    }
    return b.toString();
  }  // deleteButSet

/**
* Similar to String(char[] value, int offset, int count) but with checked
* offset and count.
*/
  public static String createInstance(char[] aValue, int aOffset, int aCount) {
    if (aCount <= 0) return EMPTY;
    int valLength = aValue.length;
    if (aOffset >= valLength) return EMPTY;
    aOffset = Math.max(aOffset, 0);

    if (aOffset + aCount > valLength)
      aCount = valLength - aOffset;
    return new String(aValue, aOffset, aCount);
  } // createInstance

/**
 * Counts how many times the substring list items appears in the source String.
 * @param aSource String The string to inspect.
 * @param aSubList ListOfString items of this list are counted in the source.
 * @return int
 */
  public static int countMatches(final String aSource, final ListOfString aSubList) {
    int r=0;
    for (int i=0, n=aSubList.size(); i<n; i++) {
      i += countMatches(aSource, aSubList.getItem(i));
    }
    return r;
  }

/**
* The last index in aSource for any item in aTarget.
*/
  public static int lastIndexOf(String aSource, List aTarget, int[] aListIndex) {
    ListIterator it = aTarget.listIterator();
    String item;
    int maxIndex=-1;
    int thisIndex;
    aListIndex[0] = -1;

    while (it.hasNext()) {
      item = (String) it.next();
      thisIndex = aSource.lastIndexOf(item);
      if (thisIndex > maxIndex) {
        maxIndex = thisIndex;
        aListIndex[0] = it.previousIndex();
      }
    } // while
    return maxIndex;
  } // lastIndexOf

/** Length of aString, 0 if aString is null */
  public static int length(final String aString) {
    if (aString==null) return 0;
    return aString.length();
  }

/**
 * Search aSource starting at aStartPos for the first occurance of aSearch
 * @param aSource String The string to search
 * @param aSearch String The string to search for
 * @param aStartPos int Starting index in the string to search
 * @param aCaseSensetive boolean is the search case sensetive
 * @return int index of first occurance
 */
  public static int indexOf(String aSource,
                            String aSearch,
                            final int aStartPos,
                            final boolean aCaseSensetive) {
    if (aCaseSensetive) return indexOf(aSource, aSearch, aStartPos);
    aSource= StringUtility.defaultString(aSource).toLowerCase();
    aSearch= StringUtility.defaultString(aSearch).toLowerCase();
    return indexOf(aSource, aSearch, aStartPos);
  }  // indexOf

  public static int indexOf(String aSource,
                            String aSearch,
                            final boolean aCaseSensetive) {
    return indexOf(aSource, aSearch, 0, aCaseSensetive);
  }  // indexOf


  /** for testing */
  public static void main(String[] args) {
    String s= "abc_12";
    System.out.println( extract(s, true, false)  );
    System.out.println( extractAlpha(s)  );
  }

}
