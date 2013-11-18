package edu.mbl.jif.utils.analytic;

import java.lang.*;


/**
*
*This class tests the Fmin class.
*
*@author Steve Verrill
*@version .5 --- March 25, 1998
*
*/


public class FminTest extends Object implements Fmin_methods {

   int id_f_to_min;
   double c,d,e;

   FminTest(int idtemp, double ctemp, double dtemp, double etemp) {

      id_f_to_min = idtemp;
      c = ctemp;
      d = dtemp;
      e = etemp;

   }

   public static void main (String args[]) {

      int another;
      int idtemp;
      double ctemp,dtemp,etemp;
      double a,b,tol,xmin;

      ctemp = dtemp = etemp = 0.0;

      another = 1;

      while (another == 1) { 

/*

   Console is a public domain class described in Cornell
   and Horstmann's Core Java (SunSoft Press, Prentice-Hall).

*/
   
         idtemp = Console.readInt("\nWhat function do you " +
         "want to minimize?\n\n" +
         "1 -- (x - c)(x - d)\n" +
         "2 -- (x - c)(x - d)(x - e)\n" +
         "3 -- sin(x)\n\n");

         if (idtemp == 1) {

            ctemp = Console.readDouble("\nWhat is the c value?  ");
            dtemp = Console.readDouble("\nWhat is the d value?  ");

         } else if (idtemp == 2) {

            ctemp = Console.readDouble("\nWhat is the c value?  ");
            dtemp = Console.readDouble("\nWhat is the d value?  ");
            etemp = Console.readDouble("\nWhat is the e value?  ");

         }

         FminTest fmintest = new FminTest(idtemp,ctemp,dtemp,etemp);

         a = Console.readDouble("\nWhat is the a value?  ");
         b = Console.readDouble("\nWhat is the b value?  ");
         tol = Console.readDouble("\nWhat is the tol value?  ");

         xmin = Fmin.fmin(a,b,fmintest,tol);

         System.out.print("\nThe xmin value is " + xmin + "\n");      

         another = Console.readInt("\nAnother test?" +
         "   0 - no   1 - yes\n\n");

      }

      System.out.print("\n");

   }


   public double f_to_minimize(double x) {

      double f;

      if (id_f_to_min == 1) {

         f = (x - c)*(x - d);

      } else if (id_f_to_min == 2) {

         f = (x - c)*(x - d)*(x - e);

      } else {

         f = Math.sin(x);

      }

      return f;         

   }


}


 class Console
{  /**
    * print a prompt on the console but don't print a newline
    * @param prompt the prompt string to display
    */

   public static void printPrompt(String prompt)
   {  System.out.print(prompt + " ");
      System.out.flush();
   }
   
   /**
    * read a string from the console. The string is 
    * terminated by a newline
    * @return the input string (without the newline)
    */
    
   public static String readString()
   {  int ch;
      String r = "";
      boolean done = false;
      while (!done)
      {  try
         {  ch = System.in.read();
            if (ch < 0 || (char)ch == '\n')
               done = true;
            else
               r = r + (char) ch;
         }
         catch(java.io.IOException e)
         {  done = true;
         }
      }
      return r;
   }

   /**
    * read a string from the console. The string is 
    * terminated by a newline
    * @param prompt the prompt string to display
    * @return the input string (without the newline)
    */
    
   public static String readString(String prompt)
   {  printPrompt(prompt);
      return readString();
   }

   /**
    * read a word from the console. The word is 
    * any set of characters terminated by whitespace
    * @return the 'word' entered
    */
    
   public static String readWord()
   {  int ch;
      String r = "";
      boolean done = false;
      while (!done)
      {  try
         {  ch = System.in.read();
            if (ch < 0 
               || java.lang.Character.isSpace((char)ch))
               done = true;
            else
               r = r + (char) ch;
         }
         catch(java.io.IOException e)
         {  done = true;
         }
      }
      return r;
   }

   /**
    * read an integer from the console. The input is 
    * terminated by a newline
    * @param prompt the prompt string to display
    * @return the input value as an int
    * @exception NumberFormatException if bad input
    */
    
   public static int readInt(String prompt)
   {  while(true)
      {  printPrompt(prompt);
         try
         {  return Integer.valueOf
               (readString().trim()).intValue();
         } catch(NumberFormatException e)
         {  System.out.println
               ("Not an integer. Please try again!");
         }
      }
   }

   /**
    * read a floating point number from the console. 
    * The input is terminated by a newline
    * @param prompt the prompt string to display
    * @return the input value as a double
    * @exception NumberFormatException if bad input
    */
    
   public static double readDouble(String prompt)
   {  while(true)
      {  printPrompt(prompt);
         try
         {  return Double.valueOf
               (readString().trim()).doubleValue();
         } catch(NumberFormatException e)
         {  System.out.println
         ("Not a floating point number. Please try again!");
         }
      }
   }
}
