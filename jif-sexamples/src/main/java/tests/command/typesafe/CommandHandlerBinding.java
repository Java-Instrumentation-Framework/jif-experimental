
package tests.command.typesafe;


/**
 * Used to Bind Commands to CommandHandlers with parameterized types
 *
 * The point of this class is to enforce the 1-1 relationship between a command's param/return types and the handler's
 * param/return types
 *
 */
public class CommandHandlerBinding<ParamType, ReturnType> {
    private Command<ParamType, ReturnType> command;

    private CommandHandler<ParamType, ReturnType> handler;

    public CommandHandlerBinding(Command<ParamType, ReturnType> command, CommandHandler<ParamType, ReturnType> handler) {
        this.command = command;
        this.handler = handler;
    }

    public Command<ParamType, ReturnType> getCommand() {
        return command;
    }

    public CommandHandler<ParamType, ReturnType> getHandler() {
        return handler;
    }
}