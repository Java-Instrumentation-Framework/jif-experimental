package tests.xstream;

import edu.mbl.jif.io.*;

/**
Person joe = new Person("Joe", "Walnes");
joe.setPhone(new PhoneNumber(123, "1234-456");
joe.setFax(new PhoneNumber(123, "9999-999");
 */

public class xPerson {
   public String firstname;
   private String lastname;

   private xPhoneNumber phone;
   private xPhoneNumber fax;
   // ... constructors and methods
   public xPerson (String firstname, String lastname) {
      this.firstname = firstname;
      this.lastname = lastname;
   }


   public String getFirstname() {
       return firstname;
   }

   public String getLastname() {
     return lastname;
 }

   public void setPhone (xPhoneNumber n) {
      this.phone = n;
   }
   public xPhoneNumber getPhone () {
      return phone;
   }

   public void setFax (xPhoneNumber n) {
      this.fax = n;
   }
   public xPhoneNumber getFax () {
      return fax;
   }

}

