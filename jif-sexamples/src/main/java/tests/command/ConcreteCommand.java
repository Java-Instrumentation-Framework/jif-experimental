package tests.command ;

public class ConcreteCommand extends Command {
    ConcreteCommand(Receiver receiver) {
        super(receiver);
    }

    // Methods
    public void execute() {
        receiver.action();
    }
}
