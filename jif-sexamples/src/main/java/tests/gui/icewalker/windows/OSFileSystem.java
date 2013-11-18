package tests.gui.icewalker.windows;

public abstract class OSFileSystem extends Object {
	public String installationDrive = "C:\\";
	public String user_home = System.getProperty("user.home");
	public String file_separator = System.getProperty("file.separator");
	
	protected OSFileSystem() {
		findHomeDrive();
	}
	
	public void findHomeDrive() {
		installationDrive = user_home.substring(0,user_home.indexOf(file_separator) + 1);
	}
	
	public String getFileSeparator() {
		return file_separator;
	}
	
	public abstract boolean checkFileSystem();
	
	public abstract String getProgramInstallationDirectory();
	
	public abstract String getDesktop();
	
	public abstract String getCurrentUserDesktop();
	
	public abstract String getStartMenu();
	
	public abstract String getProgramsMenu();
	
	public abstract String getBootFolder();
	
	public abstract String getTempFolder();
}