/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.microscope.illum;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Allows reverse lookup
 * @author GBH
 */

public enum EnumTest 
{
     WAITING("Waiting"),
     READY("Ready"),
     SKIPPED("Skipped"),
     COMPLETED("Completed");

     private static final Map<String,EnumTest> lookup 
          = new HashMap<String,EnumTest>();

     static {
          for(EnumTest s : EnumSet.allOf(EnumTest.class))
               lookup.put(s.getCode(), s);
     }

     private String code;

     private EnumTest(String code) {
          this.code = code;
     }

     public String getCode() { return code; }

     public static EnumTest get(int code) { 
          return lookup.get(code); 
     }
     
    @Override
     public String toString(){
     return code;
}
//     
//     public static Object[] getList() {
//         for (Object object : lookup.values()) {
//             System.out.println(object);
//         }
//         return new String[]{""};
//     }
}
