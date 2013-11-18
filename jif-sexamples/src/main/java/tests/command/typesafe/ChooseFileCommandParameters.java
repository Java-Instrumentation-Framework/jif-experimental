package tests.command.typesafe;

/**
* Parameters for the file chooser command
*/
public class ChooseFileCommandParameters extends CommandParameters {
    private String path;

    private String filter;

    public ChooseFileCommandParameters (String path, String filter) {
        this.path = path;
        this.filter = filter;
    }

    public String getPath() {
        return path;
    }

    public String getFilter() {
       return filter;
    }
}