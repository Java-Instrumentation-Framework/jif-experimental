package tests.command ;

public class ImplementationFile {

    public ImplementationFile() {
        super();
    }

    public static void main(java.lang.String[] args) {

        // Create receiver, command, and invoker
        Receiver r = new Receiver();
        Command c = new ConcreteCommand(r);
        Invoker i = new Invoker();

        // Set and execute command
        i.setCommand(c);
        i.executeCommand();

    }
}
