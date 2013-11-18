/*
 * CommandPattern.java
 */
package tests.command;

import java.util.*;


interface Command_ {
    void execute();
}


class Hello implements Command_ {
    public void execute() {
        System.out.print("Hello ");
    }
}


class World implements Command_ {
    public void execute() {
        System.out.print("World! ");
    }
}


class IAm implements Command_ {
    public void execute() {
        System.out.print("I'm the command pattern!");
    }
}


// An object that holds commands:
class Macro {
    private List commands = new ArrayList();

    public void add(Command_ c) {
        commands.add(c);
    }

    public void run() {
        Iterator it = commands.iterator();
        while (it.hasNext())
            ((Command_) it.next()).execute();
    }
}


public class CommandPattern {
    Macro macro = new Macro();

    public void test() {
        macro.add(new Hello());
        macro.add(new World());
        macro.add(new IAm());
        macro.run();
    }

    public static void main(String[] args) {
        new CommandPattern().test();
    }
} ///:~
