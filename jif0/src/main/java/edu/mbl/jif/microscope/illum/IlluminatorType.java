package edu.mbl.jif.microscope.illum;

/**
 *
 * @author GBH
 */

// For use in binding a list to a JComboBox

import java.util.ArrayList;
import java.util.List;

// ... needs equals and toString methods
public class IlluminatorType {

    int illuminatorType;
    String description;

    public static List getIlluminatorTypeList()
      {
        return illuminatorTypeList;
      }
    
    static List<IlluminatorType> illuminatorTypeList;
    
    static {
        illuminatorTypeList = new ArrayList<IlluminatorType>();
        illuminatorTypeList.add(new IlluminatorType(1, "UNIBLITZ_1"));
        illuminatorTypeList.add(new IlluminatorType(2, "UNIBLITZ_2"));
        illuminatorTypeList.add(new IlluminatorType(3, "ZEISS"));
        illuminatorTypeList.add(new IlluminatorType(4, "EXPOS"));
    }

    public IlluminatorType(int illuminatorType, String description)
      {
        this.illuminatorType = illuminatorType;
        this.description = description;
      }

    public int getIlluminatorType()
      {
        return illuminatorType;
      }

    public String getDescription()
      {
        return description;
      }

    public boolean equals(Object other)
      {
        return other instanceof IlluminatorType && ((IlluminatorType) other).illuminatorType == illuminatorType;
      }

    public String toString()
      {
        return getDescription();
      }
}