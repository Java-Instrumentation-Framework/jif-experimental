/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests.command.typesafe;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Finally, the top of the stack.  This is the class that is invoking the command.  Looks simple from up
 * here, doesn't it?
 */
public class MyClass extends MyBaseClass {

    public void doStuff() {
        System.out.println("Watch me invoke the command");
        File chosenFile = null;
        try {
            chosenFile = executeCommand(Command.CHOOSE_FILE, new ChooseFileCommandParameters("c:\\", "*.txt"));
        } catch (Exception ex) {
            Logger.getLogger(MyClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("The file you chose is: " + chosenFile);
    }

    public static void main(String[] args) {
        new MyClass().doStuff();
    }
}
