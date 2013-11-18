/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.stateset;

/**
 *
 * @author GBH
 */
public class TestSavedStateBean2 { //extends SavedState {

//    public TestSavedStateBean2(Object model){
//        super(model);
//       super.setName("TestState2");
//    }
    protected String aString = "INITstate";

    public String getAString()
      {
        return aString;
      }

    public void setAString(String aString)
      {
        this.aString = aString;
      }

    protected int anInteger = 0;

    public int getAnInteger()
      {
        return anInteger;
      }

    public void setAnInteger(int anInteger)
      {
        this.anInteger = anInteger;
      }

    protected String aBoolean;

    public String getABoolean()
      {
        return aBoolean;
      }

    public void setABoolean(String aBoolean)
      {
        this.aBoolean = aBoolean;
      }

}
