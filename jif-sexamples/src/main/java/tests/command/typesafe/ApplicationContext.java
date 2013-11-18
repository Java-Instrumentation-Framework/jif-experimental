package tests.command.typesafe;

import java.util.List;

/**
* Handles registering command handlers (binding them to commands) and executing.  I called this
* ApplicationContext but it can be any class that's initialized on application startup and accessible to any
* code that needs to execute commands.  Most application frameworks have something like this.
*/
public class ApplicationContext {
    private List<CommandHandlerBinding> commandHandlerBindings;

    public void initializeApplication() {
        registerCommandHandler(Command.CHOOSE_FILE, new ChooseFileCommandHandler());
    }

    public <ParamType, ReturnType> void registerCommandHandler(Command<ParamType, ReturnType> command,
            CommandHandler<ParamType, ReturnType> handler) {
        CommandHandler<ParamType, ReturnType> existingHandler = getCommandHandler(command);
        if (existingHandler != null) {
            System.out.println("Command [" + command.getName() + "] already has a handler ["
                    + existingHandler.getClass().getName() + "] but is being set to [" + handler.getClass().getName()
                    + "]");
        }
        commandHandlerBindings.add(new CommandHandlerBinding<ParamType, ReturnType>(command, handler));
    }

    private <ParamType, ReturnType> CommandHandler<ParamType, ReturnType> getCommandHandler(Command<ParamType, ReturnType> command) {
        for (CommandHandlerBinding<ParamType, ReturnType> binding : commandHandlerBindings) {
            if (binding.getCommand().getName().equals(command.getName())) {
                return binding.getHandler();
            }
        }
        return null;
    }

    public <ParamType, ReturnType> ReturnType executeCommand(Command<ParamType, ReturnType> command,
            ParamType parameters) throws Exception {
        CommandHandler<ParamType, ReturnType> handler = getCommandHandler(command);
        if (handler == null) {
            throw new NullPointerException("No CommandHandler registered for Command [" + command.getName() + "]");
        }
        return handler.execute(parameters);
    }
}