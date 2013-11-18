package edu.mbl.jif.gui.keyboard;

import java.io.*;
/**
 *  A simple input class to read values typed at the command line. If an error
 *  occurs during input, any exceptions thrown are caught and a default value
 *  returned.
 *
 * @author     Graham Roberts
 * @author     Russel Winder
 * @version    1.2 Oct 2002
 */
public class StdInKeyboardInput {
    /**
     *  The buffered stream that connects to the keyboard so that we can read
     *  from it sensibly.
     */
    private final BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in));
    
    /**
     *  Read an <CODE>int</CODE> value from keyboard input.
     *
     * @return    The integer value read in, or zero if the input was invalid.
     */
    public final synchronized int readInteger() {
        String input = "";
        int value = 0;
        try {
            input = in.readLine();
        } catch (IOException e) {
        }
        if (input != null) {
            try {
                value = Integer.parseInt(input);
            } catch (NumberFormatException e) {
            }
        }
        
        return value;
    }
    
    /**
     *  Read a <CODE>long</CODE> value from keyboard input.
     *
     * @return    The long value read in, or 0L if the input was invalid.
     */
    public final synchronized long readLong() {
        String input = "";
        long value = 0L;
        try {
            input = in.readLine();
        } catch (IOException e) {
        }
        if (input != null) {
            try {
                value = Long.parseLong(input);
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }
    
    /**
     *  Read a <CODE>double</CODE> value from keyboard input.
     *
     * @return    The double value read in, or 0.0 if the input was invalid.
     */
    public final synchronized double readDouble() {
        String input = "";
        double value = 0.0D;
        try {
            input = in.readLine();
        } catch (IOException e) {
        }
        if (input != null) {
            try {
                value = Double.parseDouble(input);
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }
    
    /**
     *  Read a <CODE>float</CODE> value from keyboard input.
     *
     * @return    The float value read in, or 0.0F if the input was invalid.
     */
    public final synchronized float readFloat() {
        String input = "";
        float value = 0.0F;
        try {
            input = in.readLine();
        } catch (IOException e) {
        }
        if (input != null) {
            try {
                value = Float.parseFloat(input);
            } catch (NumberFormatException e) {
            }
        }
        return value;
    }
    
    /**
     *  Read a <CODE>char</CODE> value from keyboard input.
     *
     * @return    The char value read in, or ' ' (space) if the input was invalid.
     */
    public final synchronized char readCharacter() {
        char c = ' ';
        try {
            c = (char) in.read();
        } catch (IOException e) {
        }
        return c;
    }
    
    /**
     *  Read an <CODE>String</CODE> value from keyboard input.
     *
     * @return    The String value read in.
     */
    public final synchronized String readString() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException e) {
        }
        if (s == null) {
            s = "";
        }
        return s;
    }
    
    public static void main(String[] args) {
        StdInKeyboardInput in = new StdInKeyboardInput();
        System.out.print("Type an integer: ");
        int n = in.readInteger();
        System.out.println("Integer was: " + n);
        System.out.print("Type a long: ");
        long l = in.readLong();
        System.out.println("Long was: " + l);
        System.out.print("Type a double: ");
        double d = in.readDouble();
        System.out.println("Double was: " + d);
        System.out.print("Type a float: ");
        float f = in.readFloat();
        System.out.println("float was: " + f);
        System.out.print("Type a char: ");
        char c = in.readCharacter();
        System.out.println("char was: " + c);
        
        // Note that reading one character leaves the newline
        // character still in the input buffer. Do an extra
        // read to use it up.
        c = in.readCharacter();
        
        // Newlines are represented using two characters on Microsoft
        // Windows systems, so an further read is needed.
        // c = in.readCharacter() ;
        System.out.print("Type a String: ");
        String s = in.readString();
        System.out.println("String was: " + s);
        
    }
}
