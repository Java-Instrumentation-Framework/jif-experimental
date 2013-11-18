package edu.mbl.jif.utils.color;

import java.awt.Color;

public class JifColor {

  public static  Color[] red = new Color[5];
  public static  Color[] orange = new Color[5];
  public static  Color[] yellow = new Color[5];
  public static  Color[] green = new Color[5];
  public static  Color[] aqua = new Color[5];
  public static  Color[] blue = new Color[5];
  public static  Color[] purple = new Color[5];
  public static  Color[] violet = new Color[5];
  public static  Color[] fushia = new Color[5];

  static {
    red[0] = new Color(120, 84, 84);
    red[1] = new Color(149, 104, 104);
    red[2] = new Color(171, 134, 134);
    red[3] = new Color(191, 164, 164);
    red[4] = new Color(213, 195, 195);
    orange[0] = new Color(120, 102, 84);
    orange[1] = new Color(149, 127, 104);
    orange[2] = new Color(171, 153, 134);
    orange[3] = new Color(191, 178, 164);
    orange[4] = new Color(213, 204, 195);
    yellow[0] = new Color(120, 113, 84);
    yellow[1] = new Color(149, 141, 104);
    yellow[2] = new Color(171, 164, 134);
    yellow[3] = new Color(191, 186, 164);
    yellow[4] = new Color(213, 209, 195);
    green[0] = new Color(84, 120, 84);
    green[1] = new Color(104, 149, 104);
    green[2] = new Color(134, 171, 134);
    green[3] = new Color(164, 191, 164);
    green[4] = new Color(195, 213, 195);
    aqua[0] = new Color(84, 120, 107);
    aqua[1] = new Color(104, 149, 134);
    aqua[2] = new Color(134, 171, 158);
    aqua[3] = new Color(164, 191, 182);
    aqua[4] = new Color(195, 213, 206);
    blue[0] = new Color(84, 90, 120);
    blue[1] = new Color(104, 113, 149);
    blue[2] = new Color(134, 141, 171);
    blue[3] = new Color(164, 169, 191);
    blue[4] = new Color(195, 198, 213);
    purple[0] = new Color(104, 84, 120);
    purple[1] = new Color(129, 104, 149);
    purple[2] = new Color(155, 134, 171);
    purple[3] = new Color(179, 164, 191);
    purple[4] = new Color(205, 195, 213);
    violet[0] = new Color(119, 84, 120);
    violet[1] = new Color(148, 104, 149);
    violet[2] = new Color(170, 134, 171);
    violet[3] = new Color(191, 164, 191);
    violet[4] = new Color(212, 195, 213);
    fushia[0] = new Color(120, 84, 93);
    fushia[1] = new Color(149, 104, 116);
    fushia[2] = new Color(171, 134, 144);
    fushia[3] = new Color(191, 164, 171);
    fushia[4] = new Color(213, 195, 199);
  }
	
    public static Color deriveColorHSB(Color base, float dH, float dS, float dB) {
        float hsb[] = Color.RGBtoHSB(
                base.getRed(), base.getGreen(), base.getBlue(), null);

        hsb[0] += dH;
        hsb[1] += dS;
        hsb[2] += dB;
        return Color.getHSBColor(
                hsb[0] < 0? 0 : (hsb[0] > 1? 1 : hsb[0]),
                hsb[1] < 0? 0 : (hsb[1] > 1? 1 : hsb[1]),
                hsb[2] < 0? 0 : (hsb[2] > 1? 1 : hsb[2]));
                                               
    }

}
