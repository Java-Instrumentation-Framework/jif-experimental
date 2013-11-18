package tests.command.typesafe;

/**
* The base class for anything.
*/
public abstract class MyBaseClass {
    private ApplicationContext applicationContext;

    public <ParamType, ReturnType> ReturnType executeCommand(
            Command<ParamType, ReturnType> command, ParamType parameters) throws Exception {
      return getApplicationContext().executeCommand(command, parameters);
    }

    private ApplicationContext getApplicationContext() {
       return applicationContext;
    }

}