package edu.mbl.jif.utils.file;

public class FileChooserFixWinZip {
    /*
     * Unregisters the zipfldr.dll fixes JDialog slowness
     * issues. (Should be made optional)
     */
    public static void applyFileChooserTweak() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("windows")) {
            return;
        }
        try {
            System.out.print("Unregistering zipfldr.dll to speed up program " +
                    "(don’t worry, windows will reset this)...");
            Runtime.getRuntime().exec("regsvr32 /s %windir%/system32/zipfldr.dll");
            System.out.println("success");
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

}
