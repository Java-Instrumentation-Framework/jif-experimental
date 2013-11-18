// U_Law -- read/generater *.au sound files.
// convert mu-law 8-bit sound encoding back and forth to 16-bit linear.
// Deals with images of sound files in RAM, not disk-based.

/* copyright (c) 1998-2003 Roedy Green, Canadian Mind Products
 * #327 - 964 Heywood Avenue
 * Victoria, BC Canada V8V 2Y5
 * tel: (250) 361-9093
 * mailto:roedy@mindprod.com
 * http://mindprod.com
 */

// based on anonymous source.
// Version 1.1 1998 November 10 - add name and address.
// Version 1.0 1997 December 5

package edu.mbl.jif.gui.sound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class U_Law
{

    private static final String EmbeddedCopyright =
            "copyright (c) 1998-2003 Roedy Green, Canadian Mind Products, http://mindprod.com";

    /**
     * constructor
     * @param b byte array image of mu-law encoded .au file, including headers
     */
    public U_Law (byte[] b) {
        magic = new String(b, 0, 4);
        try {
            DataInputStream bis = new DataInputStream(new ByteArrayInputStream(
                    b, 4, b.length - 4));
            dataLocation = bis.readInt();
            dataSize = bis.readInt();
            dataFormat = bis.readInt();
            samplingRate = bis.readInt();
            channelCount = bis.readInt();
            info = new String(b, info_offset, dataLocation - info_offset);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(4);
        }

        data = new byte[dataSize];
        System.arraycopy(b, dataLocation, data, 0, dataSize);

        if (debug) {
            System.out.println("magic <" + magic + "> dataLocation <" +
                               dataLocation + "> dataSize <" + dataSize +
                               "< dataFormat <" + dataFormat +
                               "> samplingRate <" + samplingRate +
                               "> channelCount <" + channelCount + "> info <" +
                               info + ">");
        }
    } // end constructor U_Law


    /**
     * constructor for 16-bit linear
     * @param info arbitrary string to label the *.au file internally
     * @param b linear array of shorts
     */
    public U_Law (String info, short[] b) {
        this.info = info;
        dataSize = b.length;
        data = new byte[b.length];

        /*
         * Convert a linear 16-bit PCM value to 8-bit u-law
         *
         * In order to simplify the encoding process, the original linear magnitude
         * is biased by adding 33 which shifts the encoding range from (0 - 8158) to
         * (33 - 8191). The result can be seen in the following encoding table:
         *
         *      Biased Linear Input Code        Compressed Code
         *      ------------------------        ---------------
         *      00000001wxyza                   000wxyz
         *      0000001wxyzab                   001wxyz
         *      000001wxyzabc                   010wxyz
         *      00001wxyzabcd                   011wxyz
         *      0001wxyzabcde                   100wxyz
         *      001wxyzabcdef                   101wxyz
         *      01wxyzabcdefg                   110wxyz
         *      1wxyzabcdefgh                   111wxyz
         *
         * Each biased linear code has a leading 1 which identifies the segment
         * number. The value of the segment number is equal to 7 minus the number
         * of leading 0's. The quantization interval is directly available as the
         * four bits wxyz.  * The trailing bits (a - h) are ignored.
         *
         * Ordinarily the complement of the resulting code word is used for
         * transmission, and so the code word is complemented before it is returned.
         *
         * For further information see John C. Bellamy's Digital Telephony, 1982,
         * John Wiley & Sons, pps 98-111 and 472-476.
         */
        for (int p = 0; p < b.length; p++) {
            int mask;
            int seg;
            byte uval;

            short pcm_val = b[p];

            /* Get the sign and the magnitude of the value. */
            if (pcm_val < 0) {
                pcm_val = (short) (BIAS - pcm_val);
                mask = 0x7F;
            }
            else {
                pcm_val += BIAS;
                mask = 0xFF;
            }

            /* Convert the scaled magnitude to segment number. */
            seg = calcSegment(pcm_val);

            /*
             * Combine the sign, segment, quantization bits;
             * and complement the code word.
             */
            if (seg >= 8) /* out of range, return maximum value. */
                     {
                data[p] = (byte) (0x7F ^ mask);
            }
            else {
                uval = (byte) ((seg << 4) | ((pcm_val >> (seg + 3)) & 0xF));
                data[p] = (byte) (uval ^ mask);
            }
        }
    } // end constructor U_Law


    /**
     * tolinear() - Convert a u-law values to 16-bit linear PCM
     *
     * First, a biased linear code is derived from the code word. An unbiased
     * output can then be obtained by subtracting from the biased code.
     */
    public short[] toLinear () {
        short[] pcm_array = new short[dataSize];

        for (int p = 0; p < dataSize; p++) {
            // uncomplement the codeword
            byte u_val = (byte) ((~data[p]) & 0xff);
            // Extract and bias the quantization bits.
            int t = ((u_val & 0xf) << 3) + BIAS;
            // shift up by the segment number
            t <<= (u_val & 0x70) >> 4;
            // subtract out the bias.
            pcm_array[p] = (short) ((u_val < 0) ? (BIAS - t) : (t - BIAS));
        }
        return pcm_array;
    } // end toLinear


    /**
     * add header to byte array representation to create mu-law encoded *.au file.
     */
    public byte[] toBytes () {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dataLocation = info_offset + info.length();
            dos.write(magic.getBytes(), 0, 4);
            dos.writeInt(dataLocation);
            dos.writeInt(dataSize);
            dos.writeInt(dataFormat);
            dos.writeInt(samplingRate);
            dos.writeInt(channelCount);
            dos.write(info.getBytes());
            dos.write(data);
            return bos.toByteArray();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(4);
        }
        return null;
    } // end toBytes


    /**
     * find which mu-law segment this 16-bit value fits in
     */
    private static int calcSegment (int val) {
        for (int i = 0; i < seg_end.length; i++) {
            if (val <= seg_end[i]) {
                return (i);
            }
        }

        return seg_end.length;
    } // end calcSegment


    /**
     * if true, causes debug info to be displayed
     */
    public boolean debug = true;

    /**
     * the first 4 signature characters of a *.au sound (snd) file.
     */
    String magic = ".snd";

    /**
     * offset of the mu-law sound data into the *.au file
     */
    int dataLocation;

    /**
     * size of the sound in samples.
     */
    int dataSize;

    /**
     * code for 8-bit mu-law encoding in the *.au file header.
     */
    int dataFormat = 1;

    /**
     * samples per second
     */
    int samplingRate = 8000;

    /**
     * for stereo this would be 2, the number of channels.
     * We only handle one channel with this class.
     */
    int channelCount = 1;

    /**
     * arbitrary string to label the *.au file internally
     */
    String info = " Built by U_Law ";

    /**
     * Just the samplings,  8-bit mu-law encoded.
     * Does not include the header.
     */
    byte[] data;

    /*
     * offset into a *.au file where you find the info field.
     */
    final static int info_offset = 24;

    /*
     * bias in mu-law encoding
     */
    final static int BIAS = 0x84; // +132

    /*
     * boundary points for various mu-law segments,
     * (range bands in the 16-bit linear values
     * Each segment is recorded
     * with a different amount of precision.
     */
    final static short[] seg_end = {
            0xFF,
            0x1FF,
            0x3FF,
            0x7FF,
            0xFFF,
            0x1FFF,
            0x3FFF,
            0x7FFF};

} // end class U_Law
