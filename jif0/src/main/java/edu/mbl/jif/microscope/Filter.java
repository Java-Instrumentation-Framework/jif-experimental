/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.microscope;

/**
 *
 * @author GBH
 */
public interface Filter {
    // from MMStateDevice 
    
    public int setPosition(long pos);

    public int setPosition(String label);

    public int getPosition(long pos);

    public int getPosition(String label);

    public int getPositionLabel(long pos, String label);

    public int getLabelPosition(String label, long pos);

    public int setPositionLabel(long pos, String label);

    public int getNumberOfPositions();

}
