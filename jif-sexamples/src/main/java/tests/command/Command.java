package tests.command;

public abstract class Command
{
  // Fields
  protected Receiver receiver;

  // Constructors
  public Command( Receiver receiver )
  {
    this.receiver = receiver;
  }

  // Methods
  public abstract void execute();
}
