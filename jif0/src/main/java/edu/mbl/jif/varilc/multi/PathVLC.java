package edu.mbl.jif.varilc.multi;

import java.io.Serializable;

/*  This is an optical Path containing a set of retarders that may 
 *  be set to one of a number of settings 
 * 
 * Todo: Add FirePropChanges for Binding...
 *
 */ 

public class PathVLC
      implements Serializable
{
   String name = "-";
   ControlVLC ctrl;
   Retarder[] retarders;
   public float[][] setting; // [#retarders][#settings]
   public int numSettings;
   int setTo = -1;

   public PathVLC () {
   }


   public PathVLC (String _name, ControlVLC ctrl, Retarder[] retarders, int _settings) {
      name = _name;
      this.ctrl = ctrl;
      this.retarders = retarders;
      // setting = new SettingVLC[_settings];
      numSettings = _settings;
      setting = new float[retarders.length][numSettings];
   }

	public Retarder[] getRetarders() {
		return retarders;
	}


	 public String getName() {
		 return name;
	 }
	 
	 public int getSetTo() {
		 return setTo;
	 }
	 
   public int getNumRetarders () {
      return retarders.length;
   }


   public void setSetting (int n, float[] set) {
      for (int i = 0; i < set.length; i++) {
         setting[i][n] = set[i];
         System.out.println("setSetting: " + n + " - " + set[i]);
      }
   }


   public float[]  getSetting (int n) {
      float[] s = new float[numSettings];
      for (int i = 0; i < numSettings ; i++) {
         s[i] = setting[i][n];
      }
      return s;
   }

   public String getSettingsString () {
      StringBuffer sb = new StringBuffer();
      for (int s = 0; s < numSettings; s++) {
         sb.append("\nSetting " + s + ": ");
         for (int r = 0; r < retarders.length; r++) {
            System.out.println(setting[r][s] + "  ");
         }
         sb.append("\n");
      }
      sb.append("\n");
      return sb.toString();
   }


   public void switchToSetting (int n) {
      if (n <= numSettings) {
         setTo = n;
         System.out.println("switchToSetting: " + name + " - " + n);
         for (int i = 0; i < retarders.length; i++) {
            (retarders[i]).setTo(setting[i][n]);
         }
             // if (xmitImmediately) {
         ctrl.transmitSettings();
         // }
      }

   }

   // called by spinners in adjuster
   void adjustRetardance (int n, float retardance) {
      // Open adjustment panel
   }

}
