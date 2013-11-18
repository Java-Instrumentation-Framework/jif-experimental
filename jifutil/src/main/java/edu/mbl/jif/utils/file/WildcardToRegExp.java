/*
 * WildcardToRegExp.java
 *
 * Created on May 29, 2007, 10:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.file;

/**
 *
 * @author GBH
 */
public class WildcardToRegExp {

    /**
     * Converts a windows wildcard pattern to a regex pattern
     *
     * @param wildcard - Wildcard pattern containing * and ?
     *
     * @return - a regex pattern that is equivalent to the windows wildcard pattern
     */
    private static String wildcardToRegExp(String wildcard) {
        if (wildcard == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();

        char[] chars = wildcard.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if (chars[i] == '*') {
                buffer.append(".*");
            } else if (chars[i] == '?') {
                buffer.append(".");
            } else if ("+()^$.{}[]|\\".indexOf(chars[i]) != -1) {
                buffer.append('\\').append(chars[i]); // prefix all metacharacters with backslash
            } else {
                buffer.append(chars[i]);
            }
        }

        return buffer.toString().toLowerCase();
    }

}