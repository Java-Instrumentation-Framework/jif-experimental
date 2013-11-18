package tests.command ;

public class Invoker {
    // Fields
    private Command command;

    // Methods
    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
