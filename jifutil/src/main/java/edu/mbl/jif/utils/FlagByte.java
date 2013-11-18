package edu.mbl.jif.utils;


// get[0] : least-signficant bit
// get[7] : most-signficant bit
public class FlagByte
{
    byte b;

    public FlagByte (byte _b) {
        b = _b;
    }


    public boolean get (int bit_index) {
        return ((b & getMask(bit_index)) != 0);
    }


    public void set (int bit_index) {
        b |= getMask(bit_index);
    }


    public void clear (int bit_index) {
        b &= getMask(bit_index);
    }


    private byte getMask (int bit_index) {
        return (byte) (1 << bit_index);
    }


    public static void main (String[] args) {
        byte aB = 1;
        FlagByte fb = new FlagByte(aB);

    }
    
    public void dump() {
        for (int i = 0; i < 8; i++) {
            System.out.println("bit[" + i + "] " + get(i));
        }
        
    }
}
