/*
From http://www.rbgrn.net/content/40-advanced-typesafe-command-pattern-using-java-generics
 * Advanced Typesafe Command Pattern using Java Generics
 */

package tests.command.typesafe;

import java.io.File;

/** the command class defines the interface for a command and has the available commands listed out in my
* implementation.  The important thing is the interface itself but the Command constants can be located in any
* class or classes.
*/
public class Command<ParamType, ReturnType> {

    public static final Command<ChooseFileCommandParameters, File> CHOOSE_FILE =
            new Command<ChooseFileCommandParameters, File>("Choose File");

//    public static final Command<AnotherCommandParams, String> ANOTHER_COMMAND = new Command<AnotherCommandParams, String>(
//            "Another Command");

    private String name;

    private Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}