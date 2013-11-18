/*
 * Localizable Enumeration
 */
package tests.enums;

import java.util.ResourceBundle;

/**
 *
 * @author GBH
 */
public class EnumLocalizable {
	public enum Text {
  YELL, SWEAR, BEG, GREET /* and more */ ;

  /** Resources for the default locale */
  private static final ResourceBundle res =
    ResourceBundle.getBundle("tests.Text");

  /** @return the locale-dependent message */
  public String toString() {
    return res.getString(name() + ".string");
  }
}
	public static void main(String[] args) {
		EnumLocalizable e = new EnumLocalizable();
		System.out.println("GREET" + EnumLocalizable.Text.GREET.toString());
		System.out.println("SWEAR" + EnumLocalizable.Text.SWEAR.toString());
	}
	
/*
 * 
# File com/example/Messages.properties
# default language (english) resources
YELL.string=HEY!
SWEAR.string=§$%&
BEG.string=Pleeeeeease!
GREET.string=Hello player!

# File com/example/Messages_de.properties
# german language resources
YELL.string=HEY!
SWEAR.string=%&$§
BEG.string=Biiiiiitte!
GREET.string=Hallo Spieler!
*/
}
