/*
 * Created on June 2, 2005
 *
 * This file is copyright 2005 by Neal Adam Walker. All rights reservered.
 */
package tests.gui.boxLayoutNB;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/** Simplies building GridBagConstraints objects for forms. Aligns label with 
 * Text boxes and other controls. Use <code>getComponent</code> for generic
 * controls. Use <code>getLabel</code> for labels.
 *
 * This is setup for Incors Alloy Look and Feel, your mileage may vary.
 * @author Adam Walker
 */
public class BaselineForm {
    protected GridBagConstraints gbc = new GridBagConstraints();
    protected double weight = 1.0;
    private boolean labelsRightJustistied = true;
    protected int lastX = -1;
    protected int lastY = 0;
    protected int cols = 2;
    
    /** Build forms the specified number of columns.
     */
    public BaselineForm(int cols){
        this.cols = cols;
        weight = 1.0/(cols/2);
        gbc.insets = new Insets(2,2,2,2);
    }
    
    public int getLastX(){
        return lastX;
    }
    public int getLastY(){
        return lastY;
    }
    public double getWeight(){
        return weight;        
    }
    
    public void setWeight(double w){
        weight = w;
    }
    
    /** Builds a GridBagConstraints object basing the properties on sensible defaults
     * considering the platform.
     */
    public GridBagConstraints getComponent(){
        lastX++;
        if(lastX==cols){
            lastX=0;
            lastY++;
        }
        return getComponent(lastX,lastY);
    }
    
    /** Builds a GridBagConstraints object basing the properties on sensible defaults
     * considering the platform.
     */
    public GridBagConstraints getComponent(int x, int y){
        return getComponent(x,y,1,1);
    }
    /** Builds a GridBagConstraints object basing the properties on sensible defaults
     * considering the platform.
     */
    public GridBagConstraints getComponent(int x, int y, int w){
        return getComponent(x,y,w,1);
    }
    /** Builds a GridBagConstraints object basing the properties on sensible defaults
     * considering the platform.
     */
    public GridBagConstraints getComponent(int x, int y, int w, int h){
        lastX = x;
        lastY = y;
        int useX = x;
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weighty = 0;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = weight;
        
        return gbc;
    }
    public GridBagConstraints getLabel(){
        lastX++;
        if(lastX==cols){
            lastX=0;
            lastY++;
        }
        return getLabel(lastX,lastY);
    }
    public GridBagConstraints getLabel(int x, int y){
        return getLabel(x,y,1,1);
    }
    public GridBagConstraints getLabel(int x, int y, int w, int h){
        GridBagConstraints gbc = getComponent(x,y,w,h);
        gbc.insets = new Insets(5,2,2,2);
        gbc.anchor = labelsRightJustistied?GridBagConstraints.NORTHEAST:GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        return gbc;
    }
    
}
