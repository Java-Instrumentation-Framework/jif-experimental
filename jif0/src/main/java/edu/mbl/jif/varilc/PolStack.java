/*
 * PolStack.java
 *
 * Created on March 13, 2007, 9:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.varilc;

/**
 *
 * @author GBH
 */
public class PolStack {
    
    /** Creates a new instance of PolStack */
    public PolStack() {
    }
    
    
    
    //////////////////////////////////////////////////////////////////////////////
    // LCSwingMatrix: Retarder Settings {quarterWave , halfWave }
    // plus or minus swing
    int[][] retarderSwingMatrix = { {0, 0}, {1, 0}, {0, 1}, {0, -1}, { -1, 0}};
    /////////////////////////////////////////////////////////////////////////
    // setStackType
    // This allows the arbitrary arrangement of the VariLC settings
    // used to for Acquisition - it associates an VariLC(A,B)
    // setting for each frame in a Polstack.
    
    
    int[] lcSet = new int[5]; // VariLC settings for each frame
    int numberSlices;
    //
    private static final int TWO_FRAME = 2;
    private static final int THREE_FRAME = 3;
    private static final int FOUR_FRAME = 4;
    private static final int FIVE_FRAME = 5;
    //
    int psStackType = FIVE_FRAME; // default to 5-Frame acquisition
    
    public int getStackType() {
        return psStackType;
    }
    
    
    public void setStackType(int type) {
        psStackType = type;
        // set all to undefined
        lcSet[0] = -1;
        lcSet[1] = -1;
        lcSet[2] = -1;
        lcSet[3] = -1;
        lcSet[4] = -1;
        switch (psStackType) {
            case TWO_FRAME:
                numberSlices = 2;
                lcSet[0] = 1;
                lcSet[1] = 4;
                /** @todo check this - should it be 1-3 or 2-4 */
                break;
            case THREE_FRAME:
                numberSlices = 3;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                break;
            case FOUR_FRAME:
                numberSlices = 4;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                lcSet[3] = 3;
                break;
            case FIVE_FRAME:
                numberSlices = 5;
                lcSet[0] = 0;
                lcSet[1] = 1;
                lcSet[2] = 2;
                lcSet[3] = 3;
                lcSet[4] = 4;
                break;
        }
        //    System.out.println(
        //      "PolScope StackType set to: " + psStackType + "  [" + lcSet[0] + ","
        //      + lcSet[1] + "," + lcSet[2] + "," + lcSet[3] + "," + lcSet[4] + "]");
    }
    
}
