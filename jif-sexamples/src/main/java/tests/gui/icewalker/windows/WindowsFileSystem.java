package tests.gui.icewalker.windows;
import java.io.*;

public class WindowsFileSystem extends OSFileSystem {
	
	public boolean isNT = false, isFAT = false;		
	public String programDirectory = "Program Files";		
	public String NT_HOME_DIR = installationDrive + "Documents And Settings";	
	
	public WindowsFileSystem() {
		checkFileSystem();
	}
	
	public boolean checkFileSystem() {
		findHomeDrive();
		NT_HOME_DIR = installationDrive + "Documents And Settings";
		File ntDir = new File(NT_HOME_DIR);
		
		if(ntDir.exists()) {
			isFAT = false;
			return isNT = true;
		} 
		
		isNT = false;
		return isFAT = true;		
	}
	
	public String getProgramInstallationDirectory() {			
		return installationDrive + programDirectory;
	}
	
	public String getDesktop() {
		if(isFAT) {
			return getBootFolder() + file_separator + "Desktop";
		}
		
		if(isNT) {
			return NT_HOME_DIR + file_separator + "All Users" + file_separator + "Desktop";
		}
		
		return installationDrive;
	}
	
	public String getCurrentUserDesktop() {
		if(isFAT) {
			return getBootFolder() + file_separator + "Desktop";
		}
		
		if(isNT) {
			String user = System.getProperty("user.name");
			return NT_HOME_DIR + file_separator + user + file_separator + "Desktop";
		}
		
		return installationDrive;
	}
	
	public String getStartMenu() {
		if(isFAT) {
			return getBootFolder() + file_separator + "Start Menu";
		}
		
		if(isNT) {
			return NT_HOME_DIR + file_separator + "All Users" + file_separator + "Start Menu";
		}
		
		return installationDrive;
	}
	
	public String getProgramsMenu() {
		return getStartMenu() + file_separator + "Programs";
	}
	
	public String getBootFolder() {
		File[] folders = new File(installationDrive).listFiles();
		for(int i = 0; i < folders.length; i++) {
			String path = folders[i].getAbsolutePath();
			String folderName = path.substring( path.lastIndexOf(file_separator) + 1, path.length() );
			
			if( new File(folderName + file_separator + "system").exists() ) {
				return installationDrive + folderName;
			}
		}
		
		if(isFAT) {
			if(new File(installationDrive + "WINDOWS").exists()) {
				return installationDrive + "WINDOWS";
			}			
		}
		
		if(isNT) {
			if(new File(installationDrive + "WINDOWS").exists()) {
				return installationDrive + "WINDOWS";
			}
			
			if(new File(installationDrive + "WINNT").exists()) {
				return installationDrive + "WINNT";
			}
		}
		
		return installationDrive + "WINDOWS";
	}
	
	
	public String getTempFolder() {
		return getBootFolder() + file_separator + "Temp";
	}
}