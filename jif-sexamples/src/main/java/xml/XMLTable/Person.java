package xml.XMLTable;
/********************************************************************
 * Copyright (c) 1999 The Bean Factory, LLC. 
 * All Rights Reserved.
 *
 * The Bean Factory, LLC. makes no representations or 
 * warranties about the suitability of the software, either express
 * or implied, including but not limited to the implied warranties of 
 * merchantableness, fitness for a particular purpose, or 
 * non-infringement. The Bean Factory, LLC. shall not be 
 * liable for any damages suffered by licensee as a result of using,
 * modifying or distributing this software or its derivatives.
 *
 *******************************************************************/

/********************************************************************
 <B>Person</B> sets the last name, first name, company name, and 
 email address of a person

 @version      : 1.1
 @author       : Nazamul Idris

********************************************************************/
public class Person {
//
// Data Members
//
    protected String lastName, firstName, company, email;


//
// Methods
//
/**
 Default Constructor 

 @param
 */
    public Person(){

    }

    /**
     Set the last name of a person
    
     @param     s   a last name
     */
    public  void setLastName(String s){
        lastName = s;
        
    }

    /**
     Set the first name of a person
     
     @param     s   a first name
     */
    public void setFirstName(String s){
        firstName = s;
    }

    /**
     Set the company name
     
     @param     s   a company name
     */
    public void setCompany(String s){
        company = s;
    }

    /**
     Set the email address of a person
     
     @param     s   an email address
     */
    public void setEmail(String s){
        email = s;
    }

    /**
     Return a last name
     
     @return    lastName    a last name
     */
    public String getLastName(){
        return lastName;
    }

    /**
     Return a first name
     
     @return    firstName    a first name
     */
    public String getFirstName(){
        return firstName;
    }
    
    /**
     Return a company name
     
     @return    company    a company name
     */
    public String getCompany(){
        return company;
    }

    /**
     Return an email address
     
     @return    email    an email address
     */
    public String getEmail(){
        return email;
    }


}//end of Person class

